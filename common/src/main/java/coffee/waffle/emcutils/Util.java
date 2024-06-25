package coffee.waffle.emcutils;

import coffee.waffle.emcutils.container.EmpireServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Util {
	public static final String MODID = "emcutils";
	public static final Logger LOG = LoggerFactory.getLogger(MODID);
	public static boolean isOnEMC = false;
	public static EmpireServer currentServer;
	public static String onJoinCommand;
	public static int playerGroupId = 0;

	public static void setCurrentServer(String name) {
		for (EmpireServer server : EmpireServer.values()) {
			if (server.name.equalsIgnoreCase(name)) {
				currentServer = server;
				return;
			}
		}

		currentServer = EmpireServer.NULL;
	}

	public static int getMinValue(int[] arr) {
		return Collections.min(Arrays.stream(arr).boxed().toList());
	}

	public static int getMaxValue(int[] arr) {
		return Collections.max(Arrays.stream(arr).boxed().toList());
	}

	public static void runResidenceCollector() {
		if (Config.dontRunResidenceCollector()) {
			LOG.info(MODID + " is not going to run the residence collector - some features will not work as intended. " +
				"Disable 'Don't run residence collector' to get rid of this message.");
		}

		HttpClient client = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.NORMAL)
			.connectTimeout(Duration.ofSeconds(30))
			.build();
		ExecutorService executor = Executors.newCachedThreadPool();
		IntStream.rangeClosed(1, 10).forEach(i -> executor.submit(() -> {
			Thread.currentThread().setName("Residence Collector " + i);
			EmpireServer.getById(i).collectResidences(client);
		}));
		executor.shutdown();
	}

	public static String plural(long count) {
		return count == 1 ? "" : "s";
	}

	public static Identifier id(String id) {
		return Identifier.of(MODID, id);
	}
}
