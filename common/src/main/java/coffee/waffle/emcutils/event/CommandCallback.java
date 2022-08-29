package coffee.waffle.emcutils.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.List;

public interface CommandCallback {
	Event<PreExecuteCommand> PRE_EXECUTE_COMMAND = new Event<>(PreExecuteCommand.class,
		(listeners) -> (player, command, args) -> {
			for (PreExecuteCommand listener : listeners) {
				ActionResult result = listener.onPreExecuteCommand(player, command, args);

				if (result != ActionResult.PASS) {
					return result;
				}
			}

			return ActionResult.PASS;
		}
	);

	Event<PostExecuteCommand> POST_EXECUTE_COMMAND = new Event<>(PostExecuteCommand.class,
		(listeners) -> (player, command, args) -> {
			for (PostExecuteCommand listener : listeners) {
				ActionResult result = listener.onPostExecuteCommand(player, command, args);

				if (result != ActionResult.PASS) {
					return result;
				}
			}

			return ActionResult.PASS;
		}
	);


	@FunctionalInterface
	interface PreExecuteCommand {
		ActionResult onPreExecuteCommand(ClientPlayerEntity player, String command, List<String> args);
	}

	@FunctionalInterface
	interface PostExecuteCommand {
		ActionResult onPostExecuteCommand(ClientPlayerEntity player, String command, List<String> args);
	}
}
