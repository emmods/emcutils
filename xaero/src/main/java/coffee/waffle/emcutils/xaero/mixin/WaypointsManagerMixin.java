package coffee.waffle.emcutils.xaero.mixin;

import coffee.waffle.emcutils.container.EmpireResidence;
import coffee.waffle.emcutils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;

@Pseudo
@Mixin(WaypointsManager.class)
abstract class WaypointsManagerMixin {
	@Inject(method = "teleportToWaypoint(Lxaero/common/minimap/waypoints/Waypoint;Lxaero/common/minimap/waypoints/WaypointWorld;Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("HEAD"), cancellable = true)
	public void emcutils$xaero$teleportToResidence(Waypoint w, WaypointWorld world, Screen screen, boolean respectHiddenCoords, CallbackInfo ci) {
		if (world != null) {
			if (Util.isOnEMC) {
				EmpireResidence res = Util.currentServer.getResidenceByLoc(new Vec3d(w.getX(), 64, w.getZ()));

				if (res != null) {
					MinecraftClient.getInstance().player.networkHandler.sendCommand(res.visitCommand);
					ci.cancel();
				}
			}
		}
	}
}
