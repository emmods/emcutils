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

package dev.frydae.emcutils.mixins.xaero;

import dev.frydae.emcutils.utils.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.AXaeroMinimap;
import xaero.common.settings.ModSettings;

/**
 * A set of Overwrites into Xaero's configs. These are safe to do
 * because no other mod that I could find mixins into this class.
 */
@Pseudo
@Mixin(ModSettings.class)
public class ModSettingsMixin {
  @Shadow(remap = false) public static int serverSettings;
  @Shadow(remap = false) private boolean entityRadar;
  @Shadow(remap = false) private AXaeroMinimap modMain;

  /**
   * @reason Force cave maps off on EMC
   * @author wafflecoffee
   */
  @Overwrite(remap = false)
  public boolean caveMapsDisabled() {
    if (Util.isOnEMC) return true;
    else return (serverSettings & 16384) != 16384;
  }

  /**
   * @reason Force radars off on EMC
   * @author wafflecoffee
   */
  @Overwrite(remap = false)
  public boolean getEntityRadar() {
    if (Util.isOnEMC) return false;
    else return this.entityRadar && !this.modMain.isFairPlay();
  }
}