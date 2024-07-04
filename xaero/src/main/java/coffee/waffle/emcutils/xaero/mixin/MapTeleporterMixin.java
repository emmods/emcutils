package coffee.waffle.emcutils.xaero.mixin;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.container.EmpireResidence;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.teleport.MapTeleporter;
import xaero.map.world.MapWorld;

@Pseudo
@Mixin(MapTeleporter.class)
abstract class MapTeleporterMixin {
	@Inject(method = "teleport", at = @At(value = "INVOKE_ASSIGN", target = "Lxaero/map/world/MapWorld;getTeleportCommandFormat()Ljava/lang/String;", remap = false), cancellable = true, remap = false)
	private void emcutils$xaero$enableMapTeleportation(Screen screen, MapWorld mapWorld, int x, int y, int z, RegistryKey<World> d, CallbackInfo ci) {
		if (Util.isOnEMC()) {
			EmpireResidence res = Util.currentServer.getResidenceByLoc(new Vec3d(x, y, z));

			if (res != null) {
				MinecraftClient.getInstance().player.networkHandler.sendCommand(res.visitCommand);
				ci.cancel();
			}
		}
	}
}
