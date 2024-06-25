package coffee.waffle.emcutils.fabric;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.event.TooltipCallback;
import coffee.waffle.emcutils.feature.VaultScreen;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;
import static coffee.waffle.emcutils.Util.id;
import static net.fabricmc.fabric.api.resource.ResourcePackActivationType.NORMAL;

public class EMCUtilsFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MidnightConfig.init(MODID, ConfigImpl.class);

		ModContainer mod = FabricLoader.getInstance().getModContainer(MODID).get();

		// These don't work in dev for whatever reason, but work in prod
		ResourceManagerHelper.registerBuiltinResourcePack(id("dark-ui-vault"), mod, NORMAL);
		ResourceManagerHelper.registerBuiltinResourcePack(id("vt-dark-vault"), mod, NORMAL);

		Util.runResidenceCollector();

		HandledScreens.register(VaultScreen.GENERIC_9X7, VaultScreen::new);

		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			TooltipCallback.ITEM.invoker().append(stack, lines, context, type);
		});

		LOG.info("Initialized " + MODID);
	}
}
