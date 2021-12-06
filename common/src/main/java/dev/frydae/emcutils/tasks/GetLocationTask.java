package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.interfaces.Task;
import dev.frydae.emcutils.listeners.ChatListener;
import net.minecraft.client.MinecraftClient;

public class GetLocationTask implements Task {
  @Override
  public void execute() {
    ChatListener.currentMessage = ChatListener.ChatMessage.LOCATION;
    MinecraftClient.getInstance().player.sendChatMessage("/loc");
  }

  @Override
  public String getDescription() {
    return "Getting current location";
  }
}
