package coffee.waffle.emcutils.forge;

import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.Util;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;

@Mod(MODID)
public class EMCUtilsForge {
	public EMCUtilsForge(FMLJavaModLoadingContext context) {
		context.getModEventBus().addListener(this::clientSetupEvent);

		context.registerConfig(ModConfig.Type.CLIENT, ConfigImpl.SPEC);

		movePacks("vt-dark-vault", "dark-ui-vault");

		context.registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "", (a, b) -> b));

		LOG.info("Initialized " + MODID);
	}

	private static void movePacks(String... packs) {
		try {
			Files.createDirectories(Paths.get(FMLPaths.GAMEDIR + "/resourcepacks"));
		} catch (FileAlreadyExistsException ignored) {
		} catch (IOException e) {
			LOG.warn("Could not create resource packs folder");
			return;
		}

		for (String pack : packs) {
			try (InputStream packZip = EMCUtilsForge.class.getResourceAsStream("/resourcepacks/" + pack + ".zip")) {
				Files.copy(packZip, Paths.get("resourcepacks/" + pack + ".zip")); // This works in prod but not dev
			} catch (FileAlreadyExistsException ignored) {
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void clientSetupEvent(FMLClientSetupEvent event) {
		HandledScreens.register(VaultScreen.GENERIC_9X7, VaultScreen::new);
		Util.runResidenceCollector();
	}

	@SubscribeEvent
	public static void tooltipEvent(ItemTooltipEvent event) {
		//TooltipCallback.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), event.get(), event.getFlags());
	}
}
