package coffee.waffle.emcutils.fabric;

import coffee.waffle.emcutils.Config.ChatAlertSound;
import coffee.waffle.emcutils.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.Config.TabListSortType;
import eu.midnightdust.lib.config.MidnightConfig;

public class ConfigImpl extends MidnightConfig {
	@Entry public static boolean chatButtonsEnabled = true;
	@Entry public static boolean tabListShowAllServers = true;
	@Entry public static TabListSortType tabListSortType = TabListSortType.SERVER_ASCENDING;
	@Entry public static TabListCurrentServerPlacement tabListCurrentServerPlacement = TabListCurrentServerPlacement.TOP;
	@Entry(min = -15, max = 30) public static int chatAlertPitch = 0;
	@Entry public static ChatAlertSound chatAlertSound = ChatAlertSound.LEVEL_UP;
	@Entry public static boolean dontRunResidenceCollector = false;
	@Entry(min = 1, max = 255) public static int totalVaultPages = 255;
	@Entry(min = 0, max = 10) public static int precisionPoints = 2;
	@Entry(min = 0, max = 10) public static double aquaLowerRange = 9.9;
	@Entry(min = 0, max = 10) public static double greenLowerRange = 9.5;
	@Entry(min = 0, max = 10) public static double yellowLowerRange = 9.0;
	@Entry(min = 0, max = 10) public static double redLowerRange = 8.0;

	public static boolean chatButtonsEnabled() {
		return chatButtonsEnabled;
	}

	public static boolean tabListShowAllServers() {
		return tabListShowAllServers;
	}

	public static TabListSortType tabListSortType() {
		return tabListSortType;
	}

	public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
		return tabListCurrentServerPlacement;
	}

	public static int chatAlertPitch() {
		return chatAlertPitch;
	}

	public static ChatAlertSound chatAlertSound() {
		return chatAlertSound;
	}

	public static boolean dontRunResidenceCollector() {
		return dontRunResidenceCollector;
	}

	public static int totalVaultPages() {
		return totalVaultPages;
	}

	public static int precisionPoints() {
		return precisionPoints;
	}

	public static double aquaLowerRange() {
		return aquaLowerRange;
	}

	public static double greenLowerRange() {
		return greenLowerRange;
	}

	public static double yellowLowerRange() {
		return yellowLowerRange;
	}

	public static double redLowerRange() {
		return redLowerRange;
	}
}
