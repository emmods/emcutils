package coffee.waffle.emcutils.event;

import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

@FunctionalInterface
public interface TooltipCallback {
	Event<TooltipCallback> ITEM = new Event<>(TooltipCallback.class, (listeners) -> (itemStack, list, tooltipContext, type) -> {
		for (TooltipCallback listener : listeners) listener.append(itemStack, list, tooltipContext, type);
	});

	void append(ItemStack itemStack, List<Text> list, TooltipContext tooltipContext, TooltipType type);
}
