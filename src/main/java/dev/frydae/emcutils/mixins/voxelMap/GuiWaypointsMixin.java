/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.frydae.emcutils.mixins.voxelMap;

import com.mamiyaotaru.voxelmap.gui.GuiWaypoints;
import com.mamiyaotaru.voxelmap.util.Waypoint;
import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(GuiWaypoints.class)
public abstract class GuiWaypointsMixin {

  protected Waypoint selectedWaypoint;

  @Inject(method = "teleportClicked", at = @At("HEAD"), remap = false, cancellable = true)
  public void handleTeleport(CallbackInfo ci) {
    if (Util.isOnEMC) {
      Vec3d pos = new Vec3d(selectedWaypoint.getX(), selectedWaypoint.getY(), selectedWaypoint.getZ());

      EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(pos);
      if (res != null) {
        Util.getPlayer().sendChatMessage(res.getVisitCommand());

      }
      ci.cancel();
    }
  }
}