package coffee.waffle.emcutils.feature;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Nameplates {
	@Nullable
	public static PlayerListEntry findPlayerListEntry(PlayerEntity player) {
		Collection<PlayerListEntry> playerList = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList();

		PlayerListEntry entry = null;
		for (PlayerListEntry playerListEntry : playerList) {
			List<Text> siblings = playerListEntry.getDisplayName().getSiblings();
			if (siblings.getFirst().contains(player.getName())) {
				entry = playerListEntry;
			}
		}

		return entry;
	}

	public static MutableText parseDisplayName(PlayerListEntry entry) {
		List<Text> siblings = Lists.newArrayList(Objects.requireNonNull(entry.getDisplayName()).getSiblings());

		MutableText text = Text.empty();
		for (Text sibling : siblings) {
			text.append(sibling);
		}

		return text;
	}
}
