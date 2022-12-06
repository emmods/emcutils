package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.event.ChatCallback;
import coffee.waffle.emcutils.event.CommandCallback;
import coffee.waffle.emcutils.event.ServerJoinCallback;
import coffee.waffle.emcutils.feature.UsableItems;
import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.listener.ChatListener;
import coffee.waffle.emcutils.listener.CommandListener;
import coffee.waffle.emcutils.util.Util;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Shadow @Final private MinecraftClient client;
	@Unique private boolean emcutils$online = false;

	@Inject(at = @At("HEAD"), method = "sendChatCommand")
	void emcutils$onPreExecuteCommand(String message, CallbackInfo info) {
		String[] parts = message.split(" ");
		CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(client.player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).toList() : Lists.newArrayList());
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;onGameMessage(Lnet/minecraft/text/Text;Z)V", shift = At.Shift.AFTER), method = "onGameMessage")
	void emcutils$onPostReceiveMessage(GameMessageS2CPacket packet, CallbackInfo info) {
		ChatCallback.POST_RECEIVE_MESSAGE.invoker().onPostReceiveMessage(client.player, packet.content());
	}

	@Inject(at = @At("TAIL"), method = "onGameJoin")
	void emcutils$onJoinEMC(GameJoinS2CPacket packet, CallbackInfo info) {
		if (Util.isOnEMC) {
			if (!emcutils$online) {
				ChatListener.init();
				CommandListener.init();
				UsableItems.init();

				emcutils$online = true;
			}

			ServerJoinCallback.WORLD_LOADED.register(Util::executeJoinCommands);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreens;open(Lnet/minecraft/screen/ScreenHandlerType;Lnet/minecraft/client/MinecraftClient;ILnet/minecraft/text/Text;)V"), method = "onOpenScreen", cancellable = true)
	void emcutils$changeToVaultScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
		if (Util.isOnEMC && packet.getName().getString().startsWith("Page: ") && packet.getScreenHandlerType() == ScreenHandlerType.GENERIC_9X6) {
			var pageTitle = packet.getName().copy();
			if (pageTitle.getString().split(" ")[1].contains("69")) {
				pageTitle = pageTitle.append(Text.of(" ... nice"));
			}
			HandledScreens.open(VaultScreen.GENERIC_9X7, MinecraftClient.getInstance(), packet.getSyncId(), pageTitle);
			ci.cancel();
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;add(Lnet/minecraft/client/toast/Toast;)V"), method = "onServerMetadata", require = 0)
	void emcutils$noInsecureChatWarning(ToastManager tm, Toast toast) {
		if (!Util.isOnEMC) tm.add(toast);
	}
}
