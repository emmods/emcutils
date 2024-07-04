package coffee.waffle.emcutils.feature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class VaultScreenHandler extends ScreenHandler {
	private final Inventory inventory = new SimpleInventory(9 * 6);
	private final int rows = 6;

	public VaultScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(VaultScreen.GENERIC_9X7, syncId);
		checkSize(inventory, rows * 9);
		inventory.onOpen(playerInventory.player);
		int i = (6 - 3) * 18;

		for (int row = 0; row < 6; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
			}
		}

		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + i));
			}
		}

		for (int row = 0; row < 9; ++row) {
			this.addSlot(new Slot(playerInventory, row, 8 + row * 18, 161 + i));
		}
	}

	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();

			if (index < this.rows * 9) {
				if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, this.rows * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}

		return itemStack;
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.inventory.onClose(player);
	}
}
