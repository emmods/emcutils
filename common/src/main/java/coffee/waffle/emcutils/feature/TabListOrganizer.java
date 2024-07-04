package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Config;
import coffee.waffle.emcutils.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.Config.TabListSortType;
import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.container.EmpireServer;
import com.google.common.collect.Lists;
import net.minecraft.client.network.PlayerListEntry;

import java.util.List;
import java.util.stream.Collectors;

public class TabListOrganizer {
	public static List<PlayerListEntry> sortPlayers(List<PlayerListEntry> original) {
		List<EnhancedTabListEntry> enhanced = Lists.newArrayList();
		List<EnhancedTabListEntry> currentServer = Lists.newArrayList();

		if (!Util.isOnEMC()) {
			return original;
		}

		for (PlayerListEntry entry : original) {
			// literal{[U] }
			char server = entry.getDisplayName().toString().charAt(9);
			String playerName = entry.getDisplayName().getSiblings().get(0).getString();

			EnhancedTabListEntry enhancedEntry = new EnhancedTabListEntry(EmpireServer.getByTabListDisplay(server), playerName, entry);

			enhanced.add(enhancedEntry);

			if (EmpireServer.getByTabListDisplay(server) == Util.currentServer) {
				currentServer.add(enhancedEntry);
			}
		}

		if (!Config.tabListShowAllServers()) {
			enhanced = enhanced.stream().filter(e -> e.server == Util.currentServer).collect(Collectors.toList());
		}

		// This ensures that the names are in alphabetical order before any other sort.
		enhanced.sort(TabListSortType.NAME_ASCENDING::compare);
		currentServer.sort(TabListSortType.NAME_ASCENDING::compare);

		// This sorts based on what config option you have set
		enhanced.sort(Config.tabListSortType()::compare);

		List<EnhancedTabListEntry> sorted = Lists.newArrayList();

		if (Config.tabListCurrentServerPlacement() == TabListCurrentServerPlacement.TOP) {
			enhanced.removeAll(currentServer);
			sorted.addAll(currentServer);
			sorted.addAll(enhanced);
		} else if (Config.tabListCurrentServerPlacement() == TabListCurrentServerPlacement.BOTTOM) {
			enhanced.removeAll(currentServer);
			sorted.addAll(enhanced);
			sorted.addAll(currentServer);
		} else {
			sorted.addAll(enhanced);
		}

		return sorted.stream().map(e -> e.entry).collect(Collectors.toList());
	}

	public static class EnhancedTabListEntry {
		public EmpireServer server;
		public String playerName;
		public PlayerListEntry entry;

		public EnhancedTabListEntry(EmpireServer server, String playerName, PlayerListEntry entry) {
			this.server = server;
			this.playerName = playerName;
			this.entry = entry;
		}
	}
}
