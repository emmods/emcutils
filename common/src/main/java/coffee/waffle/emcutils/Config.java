package coffee.waffle.emcutils;

import coffee.waffle.emcutils.feature.TabListOrganizer.EnhancedTabListEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("unused")
public class Config {
	@ExpectPlatform
	public static boolean chatButtonsEnabled() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static boolean tabListShowAllServers() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static TabListSortType tabListSortType() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static int chatAlertPitch() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static ChatAlertSound chatAlertSound() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static boolean dontRunResidenceCollector() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static int totalVaultPages() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static int precisionPoints() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static double aquaLowerRange() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static double greenLowerRange() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static double yellowLowerRange() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	@ExpectPlatform
	public static double redLowerRange() {
		throw new AssertionError("ExpectPlatform didn't apply!");
	}

	public enum TabListSortType {
		NAME_ASCENDING {
			@Override
			public int compare(EnhancedTabListEntry entry1, EnhancedTabListEntry entry2) {
				return entry1.playerName.compareToIgnoreCase(entry2.playerName);
			}
		},
		NAME_DESCENDING {
			@Override
			public int compare(EnhancedTabListEntry entry1, EnhancedTabListEntry entry2) {
				return entry2.playerName.compareToIgnoreCase(entry1.playerName);
			}
		},
		SERVER_ASCENDING {
			@Override
			public int compare(EnhancedTabListEntry entry1, EnhancedTabListEntry entry2) {
				return Integer.compare(entry1.server.tabListRank, entry2.server.tabListRank);
			}
		},
		SERVER_DESCENDING {
			@Override
			public int compare(EnhancedTabListEntry entry1, EnhancedTabListEntry entry2) {
				return Integer.compare(entry2.server.tabListRank, entry1.server.tabListRank);
			}
		};

		public int compare(EnhancedTabListEntry entry1, EnhancedTabListEntry entry2) {
			return 0;
		}
	}

	public enum TabListCurrentServerPlacement {TOP, BOTTOM, MIXED}

	public enum ChatAlertSound {
		LEVEL_UP(SoundEvents.ENTITY_PLAYER_LEVELUP),
		ORB_PICKUP(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
		NOTE_PLING(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value()),
		ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP),
		NULL(null);

		public final SoundEvent soundEvent;

		ChatAlertSound(SoundEvent soundEvent) {
			this.soundEvent = soundEvent;
		}
	}
}
