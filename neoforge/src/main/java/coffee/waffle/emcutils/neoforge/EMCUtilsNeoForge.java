package coffee.waffle.emcutils.neoforge;

import coffee.waffle.emcutils.event.TooltipCallback;
import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.Util;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;

@Mod(MODID)
public class EMCUtilsNeoForge {
	public EMCUtilsNeoForge(ModContainer container, IEventBus modBus) {
		modBus.addListener(this::clientSetupEvent);
		modBus.addListener(this::registerScreen);
		NeoForge.EVENT_BUS.addListener(this::tooltipEvent);

		var registry = DeferredRegister.create(Registries.SCREEN_HANDLER, MODID);
		registry.register(modBus);
		registry.register("generic_63", () -> VaultScreen.GENERIC_9X7);

		container.registerConfig(ModConfig.Type.CLIENT, ConfigImpl.SPEC);

		movePacks("vt-dark-vault", "dark-ui-vault");

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
			try (InputStream packZip = EMCUtilsNeoForge.class.getResourceAsStream("/resourcepacks/" + pack + ".zip")) {
				Files.copy(packZip, Paths.get("resourcepacks/" + pack + ".zip")); // This works in prod but not dev
			} catch (FileAlreadyExistsException ignored) {
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void registerScreen(RegisterMenuScreensEvent event) {
		event.register(VaultScreen.GENERIC_9X7, VaultScreen::new);
	}

	@SubscribeEvent
	public void clientSetupEvent(FMLClientSetupEvent event) {
		Util.runResidenceCollector();
	}

	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		TooltipCallback.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), event.getContext(), event.getFlags());
	}
}
