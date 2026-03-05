package amorphia.alloygery.machines.heatExchanger.alloyKiln;

import amorphia.alloygery.Alloygery;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlloyKilnWithHeatExchangerScreen extends AbstractContainerScreen<AlloyKilnWithHeatExchangerMenu>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = Alloygery.asResource("textures/gui/alloy_kiln_with_heat_exchanger.png");

    private static final int lit_x = 62;
    private static final int lit_y = 53;
    private static final int lit_overlay_x = 206;
    private static final int lit_overlay_y = 0;
    private static final int lit_width = 16;
    private static final int lit_height = 16;
    private static final int progress_x = 108;
    private static final int progress_y = 30;
    private static final int progress_overlay_x = 176;
    private static final int progress_overlay_y = 14;
    private static final int progress_width = 9;
    private static final int progress_height = 21;
    private static final int heat_exchanger_x = 62;
    private static final int heat_exchanger_y = 71;
    private static final int heat_exchanger_overlay_x = 190;
    private static final int heat_exchanger_overlay_y = 0;
    private static final int heat_exchanger_width = 16;
    private static final int heat_exchanger_height = 16;

    public AlloyKilnWithHeatExchangerScreen(AlloyKilnWithHeatExchangerMenu menu, Inventory playerInventory, Component title)
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
        renderHeatExchanger(guiGraphics);
        renderProgress(guiGraphics);
        renderLit(guiGraphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y)
    {
        super.renderTooltip(guiGraphics, x, y);

        if (x > this.leftPos + lit_x && x < this.leftPos + lit_x + lit_width && y > this.topPos + lit_y && y < this.topPos + lit_y + lit_height)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("tooltip.alloygery.heat_exchanger.heat_description").append(Component.literal(": " + super.menu.getHeatValue())).withStyle(
                    ChatFormatting.DARK_RED), x, y);
        }
    }

    private void renderHeatExchanger(GuiGraphics guiGraphics)
    {
        if (super.menu.isHeatExchangerLit())
        {
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + heat_exchanger_x, this.topPos + heat_exchanger_y, heat_exchanger_overlay_x, heat_exchanger_overlay_y, heat_exchanger_width, heat_exchanger_height);
        }
    }

    private void renderLit(GuiGraphics guiGraphics)
    {
        int lit = super.menu.getHeatedTimeRemaining();
        if (lit > 0)
        {
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + lit_x, this.topPos + lit_y + lit_height - lit, lit_overlay_x, lit_overlay_y + lit_height - lit, lit_width, lit);
        }
    }

    private void renderProgress(GuiGraphics guiGraphics)
    {
        int progress = super.menu.getSmeltingProgress();
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + progress_x, this.topPos + progress_y, progress_overlay_x, progress_overlay_y, progress_width, progress + 1);
    }
}
