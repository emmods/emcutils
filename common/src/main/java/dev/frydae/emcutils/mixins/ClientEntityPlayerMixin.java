package dev.frydae.emcutils.mixins;

import com.google.common.collect.Lists;
import dev.frydae.emcutils.interfaces.ChatCallback;
import dev.frydae.emcutils.interfaces.CommandCallback;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientEntityPlayerMixin {
  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"), method = "sendChatMessage", cancellable = true)
  public void onPreSendMessage(String message, CallbackInfo info) {
    if (message.startsWith("/")) {
      message = message.substring(1);
      String[] parts = message.split(" ");
      ActionResult commandResult = CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(Util.getPlayer(), parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());

      if (commandResult == ActionResult.FAIL) {
        info.cancel();
      }
    } else {
      ActionResult messageResult = ChatCallback.PRE_SEND_MESSAGE.invoker().onPreSendMessage(Util.getPlayer(), message);

      if (messageResult == ActionResult.FAIL) {
        info.cancel();
      }
    }
  }

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER), method = "sendChatMessage")
  public void onPostSendMessage(String message, CallbackInfo info) {
    if (message.startsWith("/")) {
      message = message.substring(1);
      String[] parts = message.split(" ");
      CommandCallback.POST_EXECUTE_COMMAND.invoker().onPostExecuteCommand(Util.getPlayer(), parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
    } else {
      ChatCallback.POST_SEND_MESSAGE.invoker().onPostSendMessage(Util.getPlayer(), message);
    }
  }
}
