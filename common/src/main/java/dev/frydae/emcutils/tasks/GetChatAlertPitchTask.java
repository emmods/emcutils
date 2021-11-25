package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.interfaces.Task;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Util;

public class GetChatAlertPitchTask implements Task {
  @Override
  public void execute() {
    Util.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND_PITCH;
    Util.getPlayer().sendChatMessage("/ps set chatalertpitch");
  }

  @Override
  public String getDescription() {
    return "Get Chat Alert Pitch";
  }
}
