package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Config;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.UUID;

import static coffee.waffle.emcutils.Util.id;
import static coffee.waffle.emcutils.Util.plural;

@SuppressWarnings("SpellCheckingInspection")
public class VaultScreen extends HandledScreen<VaultScreenHandler> implements ScreenHandlerProvider<VaultScreenHandler> {
	public static final ScreenHandlerType<VaultScreenHandler> GENERIC_9X7 = ScreenHandlerType.register("generic_63", VaultScreenHandler::new);
	private static final Identifier TEXTURE = id("textures/gui/container/generic_63.png");
	private final int vaultPage;
	private final int[] slotOffsets = {8, 26, 44, 62, 80, 98, 116, 134, 152};
	private boolean shouldCallClose = true;

	public VaultScreen(VaultScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundHeight = 114 + 7 * 18;
		this.playerInventoryTitleY = this.backgroundHeight - 94;

		String page = title.getString().split(" ")[1];
		this.vaultPage = NumberUtils.isParsable(page) ? Integer.parseInt(page) : 1;
	}

	/**
	 * @param amount   the amount of pages to move
	 * @param positive whether the amount of pages
	 * @return a {@link ItemStack player head} with a left or right arrow
	 */
	private ItemStack getHead(int amount, boolean positive) {
		ItemStack stack = Items.PLAYER_HEAD.getDefaultStack();
		String head = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + (positive ?
			"ZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MC" :
			"NWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNi") + "J9fX0=";

		stack.set(DataComponentTypes.CUSTOM_NAME, formattedText(String.format("Go %s %s page%s", positive ? "forward" : "back", amount, plural(amount))));
		GameProfile profile = new GameProfile(UUID.fromString("1635371d-8f8b-4a90-8495-4e7df6c946b2"), "MrFrydae");
		profile.getProperties().put("textures", new Property("Value", head));

		stack.set(DataComponentTypes.PROFILE, new ProfileComponent(profile));

		return stack;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		for (int i = 4; i > 0; i--) {
			if (vaultPage > i) {
				drawButton(context, getHead(i, false), mouseX, mouseY, slotOffsets[4 - i], (vaultPage - i) + "");
			}
		}

		ItemStack chest = Items.CHEST.getDefaultStack();
		chest.set(DataComponentTypes.CUSTOM_NAME, formattedText("View your vaults"));
		drawButton(context, chest, mouseX, mouseY, slotOffsets[4], "");

		//noinspection ConstantValue
		for (int i = 1; i <= 4; i++) {
			if (vaultPage <= Config.totalVaultPages() - i) {
				drawButton(context, getHead(i, true), mouseX, mouseY, slotOffsets[4 + i], (vaultPage + i) + "");
			}
		}

		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	private void drawButton(DrawContext context, ItemStack button, int mouseX, int mouseY, int buttonX, String amountText) {
		this.drawItem(context, button, x + buttonX, y + 125, amountText);

		if (mouseX >= x + buttonX && mouseX <= x + buttonX + 15) {
			if (mouseY >= y + 126 && mouseY <= y + 141) {
				context.getMatrices().translate(0, 0, 225);
				context.fillGradient(x + buttonX, y + 125, x + buttonX + 16, y + 125 + 16, 0x80ffffff, 0x80ffffff);
				context.drawItemTooltip(textRenderer, button, mouseX, mouseY);
				context.getMatrices().translate(0, 0, -225);
			}
		}
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (int i = 4; i > 0; i--) {
			if (vaultPage > i) {
				handleClick(slotOffsets[4 - i], mouseX, mouseY, "vault " + (vaultPage - i));
			}
		}

		handleClick(slotOffsets[4], mouseX, mouseY, "vaults");

		//noinspection ConstantValue
		for (int i = 1; i <= 4; i++) {
			if (vaultPage <= Config.totalVaultPages() - i) {
				handleClick(slotOffsets[4 + i], mouseX, mouseY, "vault " + (vaultPage + i));
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@SuppressWarnings("ConstantConditions")
	private void handleClick(int buttonX, double mouseX, double mouseY, String command) {
		if (mouseX >= x + buttonX && mouseX < x + buttonX + 16) {
			if (mouseY >= y + 126 && mouseY <= y + 141) {
				this.shouldCallClose = false;
				ClientPlayerEntity player = client.player;
				player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_SNARE.value(), 4F, 1F);
				player.networkHandler.sendCommand(command);
			}
		}
	}

	@Override
	public void close() {
		if (shouldCallClose) super.close();
		else shouldCallClose = true;
	}

	private Text formattedText(String text) {
		return Text.literal(text).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN)).withItalic(false));
	}
}
