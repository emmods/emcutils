package coffee.waffle.emcutils.container;

import coffee.waffle.emcutils.Util;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Objects;

import static coffee.waffle.emcutils.Util.LOG;

public enum EmpireServer {
	NULL(0, "NULL", 0, 'N'),
	SMP1(1, "SMP1", 1, '1'),
	SMP2(2, "SMP2", 2, '2'),
	SMP3(4, "SMP3", 3, '3'),
	SMP4(5, "SMP4", 4, '4'),
	SMP5(6, "SMP5", 5, '5'),
	SMP6(7, "SMP6", 6, '6'),
	SMP7(8, "SMP7", 7, '7'),
	SMP8(9, "SMP8", 8, '8'),
	SMP9(10, "SMP9", 9, '9'),
	UTOPIA(3, "UTOPIA", 10, 'U'),
	GAMES(100, "GAMES", 11, 'G'),
	STAGE(200, "STAGE", 12, 'S');

	private final int id;
	public final String name;
	public final int tabListRank;
	private final char tabListDisplay;
	private final String command;
	private final List<EmpireResidence> residences;

	EmpireServer(int id, String name, int tabListRank, char tabListDisplay) {
		this.id = id;
		this.name = name;
		this.tabListRank = tabListRank;
		this.tabListDisplay = tabListDisplay;
		this.command = name.toLowerCase();

		this.residences = Lists.newArrayList();
	}

	public static EmpireServer getById(int id) {
		for (EmpireServer server : values()) {
			if (server.id == id) {
				return server;
			}
		}

		return NULL;
	}

	public static EmpireServer getByTabListDisplay(char display) {
		for (EmpireServer server : values()) {
			if (server.tabListDisplay == display) {
				return server;
			}
		}

		return NULL;
	}

	public static List<EmpireResidence> getAllResidences() {
		List<EmpireResidence> list = Lists.newArrayList();

		for (EmpireServer value : values()) {
			list.addAll(value.residences);
		}

		return list;
	}

	public void sendToServer() {
		Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendCommand(command);
		Util.setCurrentServer(name);
	}

	public EmpireResidence getResidenceByLoc(Vec3d pos) {
		for (EmpireResidence residence : residences) {
			if (pos.getX() <= residence.southEastCorner.getX() && pos.getX() >= residence.northWestCorner.getX()) {
				if (pos.getZ() <= residence.southEastCorner.getZ() && pos.getZ() >= residence.northWestCorner.getZ()) {
					return residence;
				}
			}
		}

		return null;
	}

	public void collectResidences(HttpClient client) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://" + name.toLowerCase() + ".emc.gs/tiles/_markers_/marker_town.json"))
			.header("Accept", "application/json")
			.header("User-Agent", "emcutils residence collector")
			.GET()
			.build();

		try {
			HttpResponse<String> content = client.send(request, BodyHandlers.ofString());

			String str = content.body()
				.replace("<b>", "").replace("<\\/b>", "")
				.replace("<div>", "").replace("<\\/div>", "")
				.replace("<h3>", "").replace("\\/v", "v")
				.replace("&#39;", "'").replace("<\\/h3>", "::")
				.replace("<br \\/>", "::");

			JsonObject object = JsonParser.parseString(str).getAsJsonObject()
				.getAsJsonObject("sets")
				.getAsJsonObject("empire.residences")
				.getAsJsonObject("areas");

			object.entrySet().forEach(e -> {
				var resDesc = e.getValue().getAsJsonObject().get("desc").getAsString();
				var resLabel = e.getValue().getAsJsonObject().get("label").getAsString();
				if (!resDesc.contains("Address") || resLabel.contains(".")) {
					return;
				}

				residences.add(new EmpireResidence(this, e.getValue().getAsJsonObject()));
			});
			LOG.info("Loaded residences for: {}", name.toLowerCase());
		} catch (IOException | InterruptedException e) {
			LOG.info("Residence collector for {} failed; you may find the 'Don't run residence collector' option to be useful. This option will prevent the residence collector from running at all, which, on very slow connections, will help prevent requests which will fail anyway.", name.toLowerCase());
		}
	}
}
