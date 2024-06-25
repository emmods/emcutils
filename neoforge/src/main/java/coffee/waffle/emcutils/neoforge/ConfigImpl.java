package coffee.waffle.emcutils.neoforge;

import coffee.waffle.emcutils.Config.ChatAlertSound;
import coffee.waffle.emcutils.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.Config.TabListSortType;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.EnumValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigImpl {
	public static final ModConfigSpec SPEC;
	public static BooleanValue chatButtonsEnabled;
	public static BooleanValue tabListShowAllServers;
	public static EnumValue<TabListSortType> tabListSortType;
	public static EnumValue<TabListCurrentServerPlacement> tabListCurrentServerPlacement;
	public static IntValue chatAlertPitch;
	public static EnumValue<ChatAlertSound> chatAlertSound;
	public static BooleanValue dontRunResidenceCollector;
	public static IntValue totalVaultPages;

	ConfigImpl(ModConfigSpec.Builder builder) {
		builder.comment("Empire Minecraft Utilities Configuration");
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
		builder.build();
	}

	static {
		Pair<ConfigImpl, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ConfigImpl::new);
		SPEC = pair.getRight();
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
}
