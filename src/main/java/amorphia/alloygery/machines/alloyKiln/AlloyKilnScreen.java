package amorphia.alloygery.machines.alloyKiln;

import amorphia.alloygery.Alloygery;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlloyKilnScreen extends AbstractContainerScreen<AlloyKilnMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = Alloygery.asResource("textures/gui/alloy_kiln.png");

    private static final int lit_offset_x = 63;
    private static final int lit_offset_y = 54;
    private static final int lit_overlay_offset_x = 176;
    private static final int lit_overlay_offset_y = 0;
    private static final int progress_offset_x = 108;
    private static final int progress_offset_y = 30;
    private static final int progress_overlay_offset_x = 176;
    private static final int progress_overlay_offset_y = 14;

	public AlloyKilnScreen(AlloyKilnMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
	{
		guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		renderProgress(guiGraphics);
		renderLit(guiGraphics);
	}

	private void renderLit(GuiGraphics guiGraphics)
	{
		int lit = super.menu.getLitTimeRemaining();
		if (lit > 0)
		{
			guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + lit_offset_x, this.topPos + lit_offset_y + 14 - lit, lit_overlay_offset_x, lit_overlay_offset_y + 14 - lit, 14, lit);
		}
	}

	private void renderProgress(GuiGraphics guiGraphics)
	{
		int progress = super.menu.getSmeltingProgress();
		guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + progress_offset_x, this.topPos + progress_offset_y, progress_overlay_offset_x, progress_overlay_offset_y, 9, progress + 1);
	}
}
