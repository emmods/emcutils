package coffee.waffle.emcutils.journey;

import coffee.waffle.emcutils.container.EmpireResidence;
import coffee.waffle.emcutils.Util;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.JourneyMapPlugin;
import journeymap.api.v2.client.display.Context;
import journeymap.api.v2.client.event.MappingEvent;
import journeymap.api.v2.client.fullscreen.ModPopupMenu;
import journeymap.api.v2.common.event.ClientEventRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;

@JourneyMapPlugin(apiVersion = "2.0.0")
public class EMCUtilsJourney implements IClientPlugin {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void initialize(@NotNull final IClientAPI api) {
		LOG.info(MODID + " found JourneyMap - enabling integrations");

		ClientEventRegistry.MAPPING_EVENT.subscribe(MODID, event -> {
			if (event.getStage() == MappingEvent.Stage.MAPPING_STARTED && Util.isOnEMC) {
				// Disable cave maps on EMC
				assert client.world != null;
				if (!client.world.getRegistryKey().getValue().getPath().contains("nether")) {
					for (Context.UI ui : Context.UI.all()) {
						api.toggleDisplay(null, Context.MapType.Underground, ui, false);
					}
				}

				// Set world names on EMC
				event.setWorldId(Util.currentServer.name.toLowerCase());
			}
		});

		// Turn off radars on EMC
		ClientEventRegistry.ENTITY_RADAR_UPDATE_EVENT.subscribe(MODID, event -> {
			if (Util.isOnEMC) event.cancel();
		});

		// Add residence TP button
		ClientEventRegistry.FULLSCREEN_POPUP_MENU_EVENT.subscribe(MODID, event -> {
			if (Util.isOnEMC) event.getPopupMenu().addMenuItem("Teleport to Residence", new TeleportToResidenceAction());
		});
	}

	@Override
	public String getModId() {
		return MODID;
	}

	private static class TeleportToResidenceAction implements ModPopupMenu.Action {
		@Override
		public void doAction(final @NotNull BlockPos pos) {
			if (Util.isOnEMC) {
				EmpireResidence res = Util.currentServer.getResidenceByLoc(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
				if (res != null) MinecraftClient.getInstance().player.networkHandler.sendCommand(res.visitCommand);
			}
		}
	}
}
