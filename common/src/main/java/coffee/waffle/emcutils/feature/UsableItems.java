package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.event.TooltipCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static coffee.waffle.emcutils.Util.plural;

public class UsableItems {
	public static void init() {
		TooltipCallback.ITEM.register((itemStack, list, tooltipContext, type) -> {
			if (!Util.isOnEMC() || !isUsableItemWithCooldown(itemStack)) return;

			for (Text text : list) {
				if (text.getString().startsWith("Usable in: ") ||
					text.getString().equalsIgnoreCase("Can be used now")) return;
			}

			list.add(Text.empty());

			long untilUsable = getSecondsUntilUsable(itemStack);

			if (untilUsable > 0) {
				list.add(Text.of("Usable in: " + formatTime(untilUsable, 1)).copy().formatted(Formatting.RED));
			} else {
				list.add(Text.of("Can be used now").copy().formatted(Formatting.GREEN));
			}

			itemStack.getItem().appendTooltip(itemStack, tooltipContext, list, type);
		});
	}

	private static boolean isUsableItemWithCooldown(ItemStack item) {
		if (item == null ||
			item.get(DataComponentTypes.CUSTOM_DATA) == null ||
			item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display") == null) return false;

		String displayString = item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display").toString();

		JsonObject display = JsonParser.parseString(displayString).getAsJsonObject();
		JsonArray originalLore = display.getAsJsonArray("OriginalLore");

		boolean usable = false;

		if (originalLore != null) {
			for (int i = 0; i < originalLore.size(); i++) {
				JsonObject metaLine;
				try {
					metaLine = JsonParser.parseString(originalLore.get(i).getAsString()).getAsJsonObject();
				} catch (IllegalStateException e) {
					continue;
				}

				if (metaLine.has("extra")) {
					String text = metaLine.getAsJsonArray("extra").get(0).toString();

					if (text.equals("\"__usableItem\"")) usable = true;

					if (text.equals("\"useTimer\"") && usable) return true;
				}
			}
		}
		return false;
	}

	private static long getSecondsUntilUsable(ItemStack item) {
		String displayString = item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display").toString();

		JsonObject display = JsonParser.parseString(displayString).getAsJsonObject();
		JsonArray originalLore = display.getAsJsonArray("OriginalLore");

		if (originalLore == null) return 0L;

		int useTimerLine = -1;

		for (int i = 0; i < originalLore.size(); i++) {
			JsonObject metaLine;
			try {
				metaLine = JsonParser.parseString(originalLore.get(i).getAsString()).getAsJsonObject();
			} catch (IllegalStateException e) {
				continue;
			}

			if (metaLine.has("extra")) {
				String text = metaLine.getAsJsonArray("extra").get(0).toString();

				if (text.equals("\"useTimer\"")) useTimerLine = i;
			}
		}

		if (useTimerLine == -1) return 0L;

		String unparsed = originalLore.get(useTimerLine + 1).getAsString();
		try {
			long time = Long.parseLong(unparsed.substring(1, unparsed.length() - 1));

			return Math.max(0, (time - System.currentTimeMillis()) / 1000L);
		} catch (NumberFormatException e) {
			// item has not been used since before NBT format was changed and is therefore safe to use
			return 0;
		}
	}

	public static String formatTime(long seconds, int depth) {
		if (seconds < 60) {
			return seconds + " second" + plural(seconds);
		}

		if (seconds < 3600) {
			long count = (long) Math.ceil(seconds) / 60;

			String res = String.format("%s minute%s", count, plural(count));

			long remaining = seconds % 60;

			if (depth > 0 && remaining >= 5) {
				return res + ", " + formatTime(remaining, --depth);
			}
			return res;
		}

		if (seconds < 86400) {
			long count = (long) Math.ceil(seconds) / 3600;
			String res = count + " hour" + plural(count);

			if (depth > 0) {
				return res + ", " + formatTime(seconds % 3600, --depth);
			}

			return res;
		}
		long count = (long) Math.ceil(seconds) / 86400;
		String res = count + " day" + plural(count);

		if (depth > 0) {
			return res + ", " + formatTime(seconds % 86400, --depth);
		}

		return res;
	}
}
