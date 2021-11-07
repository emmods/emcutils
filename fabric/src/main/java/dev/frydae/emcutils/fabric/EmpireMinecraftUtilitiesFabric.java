package dev.frydae.emcutils.fabric;

import dev.frydae.emcutils.EmpireMinecraftUtilities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

import static dev.frydae.emcutils.EmpireMinecraftUtilities.MODID;

public class EmpireMinecraftUtilitiesFabric implements ClientModInitializer {
  public static final boolean hasVoxelMap = FabricLoader.getInstance().isModLoaded("voxelmap");
  public static final boolean hasXaeroMap = FabricLoader.getInstance().isModLoaded("xaeroworldmap");

  @Override
  public void onInitializeClient() {
    EmpireMinecraftUtilities.initClient();

    if (hasVoxelMap) LogManager.getLogger(MODID).info(MODID + " found VoxelMap - enabling integrations");
    if (hasXaeroMap) LogManager.getLogger(MODID).info(MODID + " found Xaero's World Map - enabling integrations");

    LogManager.getLogger(MODID).info("Initialized " + MODID);
  }
}
