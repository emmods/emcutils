package coffee.waffle.emcutils.forge;

import coffee.waffle.emcutils.Config.ChatAlertSound;
import coffee.waffle.emcutils.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.Config.TabListSortType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigImpl {
	public static final ForgeConfigSpec SPEC;
	public static BooleanValue chatButtonsEnabled;
	public static BooleanValue tabListShowAllServers;
	public static EnumValue<TabListSortType> tabListSortType;
	public static EnumValue<TabListCurrentServerPlacement> tabListCurrentServerPlacement;
	public static IntValue chatAlertPitch;
	public static EnumValue<ChatAlertSound> chatAlertSound;
	public static BooleanValue dontRunResidenceCollector;
	public static IntValue totalVaultPages;
	public static IntValue precisionPoints;
	public static DoubleValue aquaLowerRange;
	public static DoubleValue greenLowerRange;
	public static DoubleValue yellowLowerRange;
	public static DoubleValue redLowerRange;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder()
			.comment("Empire Minecraft Utilities Configuration");
		builder.push("tabList").comment("Tab List Settings");
		tabListShowAllServers = builder.comment("Show all servers in the Player Tab List")
			.define("tabListShowAllServers", true);
		tabListSortType = builder.comment("How to sort players")
			.defineEnum("tabListSortType", TabListSortType.SERVER_ASCENDING);
		tabListCurrentServerPlacement = builder.comment("Where to put your current server")
			.defineEnum("tabListCurrentServerPlacement", TabListCurrentServerPlacement.TOP);
		builder.pop();
		builder.push("chatAlert").comment("Chat Alert Settings");
		chatAlertPitch = builder.comment("The pitch of pressing chat channel buttons")
			.defineInRange("chatAlertPitch", 0, -15, 30);
		chatAlertSound = builder.comment("The sound of pressing chat channel buttons")
			.defineEnum("chatAlertSound", ChatAlertSound.LEVEL_UP);
		builder.pop();
		builder.push("misc").comment("Miscellaneous Settings");
		chatButtonsEnabled = builder.comment("Whether chat buttons are enabled")
			.define("chatButtonsEnabled", true);
		dontRunResidenceCollector = builder.comment("Don't run residence collector")
			.define("dontRunResidenceCollector", false);
		totalVaultPages = builder.comment("Limit for vault page buttons")
			.defineInRange("totalVaultPages", 255, 1, 255);
		builder.pop();
		builder.push("honse").comment("Honse Settings");
		precisionPoints = builder.comment("Honse precision points (hi marvel)")
				.defineInRange("precisionPoints", 2, 0, 10);
		aquaLowerRange = builder.comment("Aqua colored horse rating, lower range")
				.defineInRange("precisionPoints", 9.9, 0, 10);
		greenLowerRange = builder.comment("Green colored horse rating, lower range")
				.defineInRange("precisionPoints", 9.5, 0, 10);
		yellowLowerRange = builder.comment("Yellow colored horse rating, lower range")
				.defineInRange("precisionPoints", 9.0, 0, 10);
		redLowerRange = builder.comment("Red colored horse rating, lower range")
				.defineInRange("precisionPoints", 8.0, 0, 10);
		builder.pop();
		SPEC = builder.build();
	}

	public static boolean chatButtonsEnabled() {
		return chatButtonsEnabled.get();
	}

	public static boolean tabListShowAllServers() {
		return tabListShowAllServers.get();
	}

	public static TabListSortType tabListSortType() {
		return tabListSortType.get();
	}

	public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
		return tabListCurrentServerPlacement.get();
	}

	public static int chatAlertPitch() {
		return chatAlertPitch.get();
	}

	public static ChatAlertSound chatAlertSound() {
		return chatAlertSound.get();
	}

	public static boolean dontRunResidenceCollector() {
		return dontRunResidenceCollector.get();
	}

	public static int totalVaultPages() {
		return totalVaultPages.get();
	}

	public static int precisionPoints() {
		return precisionPoints.get();
	}

	public static double aquaLowerRange() {
		return aquaLowerRange.get();
	}

	public static double greenLowerRange() {
		return greenLowerRange.get();
	}

	public static double yellowLowerRange() {
		return yellowLowerRange.get();
	}

	public static double redLowerRange() {
		return redLowerRange.get();
	}
}
