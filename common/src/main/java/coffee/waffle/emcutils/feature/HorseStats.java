package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.event.TooltipCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class HorseStats {
	public static void init() {
		TooltipCallback.ITEM.register((itemStack, list, tooltipContext, type) -> {
			if (!Util.isOnEMC() || !isHorseSpawnEgg(itemStack)) return;

			for (Text text : list) {
				if (text.getString().startsWith("This horse is a")) return;
			}

			list.add(Text.empty());

			double horseRating = getRating(itemStack);

			list.add(Text.of("This horse is a " + horseRating + " out of 10").copy().formatted(Formatting.GREEN));

			itemStack.getItem().appendTooltip(itemStack, tooltipContext, list, type);
		});
	}

	@SuppressWarnings("RegExpRedundantEscape")
	private static boolean isHorseSpawnEgg(ItemStack item) {
		if (item == null ||
			!(item.getItem() instanceof SpawnEggItem) ||
			item.get(DataComponentTypes.CUSTOM_DATA) == null) return false;

		double health = 0D;
		double speed = 0D;
		double jump = 0D;

		if (item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("EntityTag") != null) {
			try {
				String tagString = item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("EntityTag").toString();

				JsonObject entityTag = JsonParser.parseString(tagString).getAsJsonObject();
				JsonArray attributes = entityTag.getAsJsonArray("Attributes");

				for (JsonElement element : attributes) {
					JsonObject attribute = element.getAsJsonObject();
					String name = attribute.get("Name").getAsString();
					double base = attribute.get("Base").getAsDouble();

					if (name.endsWith("movementSpeed") || name.endsWith("movement_speed")) {
						speed = base * 400;
					} else if (name.endsWith("jumpStrength") || name.endsWith("jump_strength")) {
						jump = base * 100;
					} else if (name.endsWith("maxHealth") || name.endsWith("max_health")) {
						health = base;
					}
				}
			} catch (NullPointerException e) {
				return false;
			}
		} else if (item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display") != null) {
			try {
				String display = item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display").toString();

				JsonArray originalLoreArray = JsonParser.parseString(display).getAsJsonObject().getAsJsonArray("OriginalLore");
				for (int i = 0; i < originalLoreArray.size(); i++) {
					String value = originalLoreArray.get(i).getAsString();
					switch (value) {
						case "\"spd\"" ->
							speed = Double.parseDouble(originalLoreArray.get(i + 1).getAsString().replaceAll("\"", ""));
						case "\"jmp\"" ->
							jump = Double.parseDouble(originalLoreArray.get(i + 1).getAsString().replaceAll("\"", ""));
						case "\"mhp\"" ->
							health = Double.parseDouble(originalLoreArray.get(i + 1).getAsString().replaceAll("\"", ""));
					}
				}
			} catch (NullPointerException e) {
				return false;
			}
		}

		return health + speed + jump != 0D;
	}

	private static double getRating(ItemStack item) {
		double speed = 0D;
		double jump = 0D;
		double health = 0D;

		if (item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("EntityTag") != null) {
				String tagString = item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("EntityTag").toString();

				JsonObject entityTag = JsonParser.parseString(tagString).getAsJsonObject();
				JsonArray attributes = entityTag.getAsJsonArray("Attributes");

				for (JsonElement element : attributes) {
					JsonObject attribute = element.getAsJsonObject();
					String name = attribute.get("Name").getAsString();
					double base = attribute.get("Base").getAsDouble();

					if (name.endsWith("movementSpeed") || name.endsWith("movement_speed")) {
						speed = base * 400;
					} else if (name.endsWith("jumpStrength") || name.endsWith("jump_strength")) {
						jump = base * 100;
					} else if (name.endsWith("maxHealth") || name.endsWith("max_health")) {
						health = base;
					}
				}
		} else if (item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display") != null) {
				String display = item.get(DataComponentTypes.CUSTOM_DATA).copyNbt().get("display").toString();

				JsonArray originalLoreArray = JsonParser.parseString(display).getAsJsonObject().getAsJsonArray("OriginalLore");
				for (int i = 0; i < originalLoreArray.size(); i++) {
					String value = originalLoreArray.get(i).getAsString();
					switch (value) {
						case "\"spd\"" ->
							speed = Double.parseDouble(originalLoreArray.get(i + 1).getAsString().replaceAll("\"", ""));
						case "\"jmp\"" ->
							jump = Double.parseDouble(originalLoreArray.get(i + 1).getAsString().replaceAll("\"", ""));
						case "\"mhp\"" ->
							health = Double.parseDouble(originalLoreArray.get(i + 1).getAsString().replaceAll("\"", ""));
					}
				}
		}

		double speedRating = speed / 139.5 * 10;
		double jumpRating = jump / 100 * 10;
		double healthRating = health / 30 * 10;

		double overallRating = (speedRating + jumpRating + healthRating) / 3;

		return (double) Math.round(overallRating * 100d) / 100d;
	}
}
