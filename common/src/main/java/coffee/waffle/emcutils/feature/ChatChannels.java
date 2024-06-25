package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Config;
import coffee.waffle.emcutils.Config.ChatAlertSound;
import coffee.waffle.emcutils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class ChatChannels {
	public static ChatChannel currentChannel = null;
	public static boolean inPrivateConversation = false;
	public static String targetUsername = null;
	public static int targetGroupId = 0;
	private static long lastClickedButtonTime = 0L;
	private static final ClientPlayerEntity player = MinecraftClient.getInstance().player;
	private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

	public static void handleChatScreenRender(Screen screen, DrawContext context) {
		if (Util.isOnEMC && Config.chatButtonsEnabled()) {
			for (ChatChannel channel : ChatChannel.values()) {
				if (channel == ChatChannel.SUPPORTER && Util.playerGroupId < 2) break;
				if (channel == ChatChannel.MODERATOR && Util.playerGroupId < 5) break;
				drawButton(screen, context, channel);
			}

			if (inPrivateConversation) drawPrivateConversation(screen, context);
		}
	}

	public static void handleChatScreenMouseClicked(Screen screen, double mouseX, double mouseY) {
		if (Util.isOnEMC && Config.chatButtonsEnabled()) {
			for (ChatChannel channel : ChatChannel.values()) {
				if (channel == ChatChannel.SUPPORTER && Util.playerGroupId < 2) break;
				if (channel == ChatChannel.MODERATOR && Util.playerGroupId < 5) break;

				if (isInBounds(screen, channel.name, channel.getOffset(), mouseX, mouseY) && (System.currentTimeMillis() - lastClickedButtonTime) >= 1000L && currentChannel != channel) {
					lastClickedButtonTime = System.currentTimeMillis();
					currentChannel = channel;
					channel.executeCommand();

					if (Config.chatAlertSound() != ChatAlertSound.NULL)
						player.playSound(Config.chatAlertSound().soundEvent, 5, Config.chatAlertPitch());

					// Cancel private conversation if in one
					inPrivateConversation = false;

					return;
				}
			}
		}
	}

	private static boolean isInBounds(Screen screen, String text, int offset, double mouseX, double mouseY) {
		int width = textRenderer.getWidth(text);
		int height = textRenderer.fontHeight;

		// Check X coordinate
		if (mouseX < offset + 1 || mouseX >= offset + width) {
			return false;
		}

		// Check Y coordinate
		return !(mouseY < screen.height - 32) && !(mouseY >= screen.height - (32 - height - 4));
	}

	private static void drawButton(Screen screen, DrawContext context, ChatChannel channel) {
		int width = textRenderer.getWidth(channel.name);
		int height = textRenderer.fontHeight;

		if (currentChannel == channel && !inPrivateConversation) {
			context.fill(channel.getOffset(), screen.height - 33, channel.getOffset() + width + 5, screen.height - (32 - height - 4), (0xff << 24) | channel.color);
		}

		context.fill(channel.getOffset() + 1, screen.height - 32, channel.getOffset() + width + 4, screen.height - (32 - height - 3), (0xc0 << 24));
		context.drawText(textRenderer, Text.of(channel.name), channel.getOffset() + 3, screen.height - 30, channel.color, true);
	}

	private static void drawPrivateConversation(Screen screen, DrawContext context) {
		int fullWidth = textRenderer.getWidth("PM with: " + targetUsername);
		int nameWidth = textRenderer.getWidth(targetUsername);
		int height = textRenderer.fontHeight;

		context.fill(screen.width - 3, screen.height - 33, screen.width - fullWidth - 8, screen.height - (32 - height - 4), (0xff << 24) | Formatting.LIGHT_PURPLE.getColorValue());
		context.fill(screen.width - 4, screen.height - 32, screen.width - fullWidth - 7, screen.height - (32 - height - 3), (0xc0 << 24));
		context.drawText(textRenderer, Text.of("PM with: "), screen.width - fullWidth - 5, screen.height - 30, Formatting.WHITE.getColorValue(), true);
		context.drawText(textRenderer, Text.of(targetUsername), screen.width - nameWidth - 5, screen.height - 30, groupIdToFormatting(targetGroupId).getColorValue(), true);
	}

	public static Formatting groupIdToFormatting(int groupId) {
		return switch (groupId) {
			case 0 -> Formatting.BLACK;
			case 2 -> Formatting.GRAY;
			case 3 -> Formatting.GOLD;
			case 4 -> Formatting.DARK_AQUA;
			case 5 -> Formatting.YELLOW;
			case 6 -> Formatting.BLUE;
			case 7 -> Formatting.DARK_GREEN;
			case 8 -> Formatting.GREEN;
			case 9, 10 -> Formatting.DARK_PURPLE;
			default -> Formatting.WHITE;
		};
	}

	public enum ChatChannel {
		COMMUNITY("Community", "cc", Formatting.DARK_GREEN.getColorValue(), null),
		DISCORD("Discord", "cd", 0x7087d6, COMMUNITY),
		MARKET("Market", "cm", Formatting.GOLD.getColorValue(), DISCORD),
		SERVER("Server", "cs", Formatting.RED.getColorValue(), MARKET),
		LOCAL("Local", "cl", Formatting.YELLOW.getColorValue(), SERVER),
		RESIDENCE("Residence", "cr", Formatting.BLUE.getColorValue(), LOCAL),
		GROUP("Group", "cg", Formatting.DARK_AQUA.getColorValue(), RESIDENCE),
		SUPPORTER("Supporter", "cp", 0xfbbf00, GROUP),
		MODERATOR("Moderator", "cx", Formatting.LIGHT_PURPLE.getColorValue(), SUPPORTER);

		private final String name;
		private final String command;
		private final Integer color;
		private final ChatChannel adjustAgainst;

		ChatChannel(String name, String command, Integer color, ChatChannel adjustAgainst) {
			this.name = name;
			this.command = command;
			this.color = color;
			this.adjustAgainst = adjustAgainst;
		}

		public static ChatChannel getChannelByName(String name) {
			return Arrays.stream(values()).filter(value -> value.name.equalsIgnoreCase(name)).findFirst().orElse(null);
		}

		public int getOffset() {
			if (adjustAgainst == null) {
				return 2;
			}

			return adjustAgainst.getOffset() + textRenderer.getWidth(adjustAgainst.name) + 6;
		}

		public void executeCommand() {
			player.networkHandler.sendCommand(command);
		}
	}
}
