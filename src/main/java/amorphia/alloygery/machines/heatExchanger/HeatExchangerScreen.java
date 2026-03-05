package amorphia.alloygery.machines.heatExchanger;

import amorphia.alloygery.Alloygery;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class HeatExchangerScreen extends AbstractContainerScreen<HeatExchangerMenu>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = Alloygery.asResource("textures/gui/heat_exchanger.png");

    private static final int lit_x = 81;
    private static final int lit_y = 37;
    private static final int lit_overlay_x = 176;
    private static final int lit_overlay_y = 0;
    private static final int lit_width = 14;
    private static final int lit_height = 14;
    private static final int progress_x = 80;
    private static final int progress_y = 18;
    private static final int progress_overlay_x = 190;
    private static final int progress_overlay_y = 0;
    private static final int progress_width = 16;
    private static final int progress_height = 16;

    public HeatExchangerScreen(HeatExchangerMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
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

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y)
    {
        super.renderTooltip(guiGraphics, x, y);
        if (x > this.leftPos + progress_x && x < this.leftPos + progress_x + progress_width && y > this.topPos + progress_y && y < this.topPos + progress_y + progress_height)
        {
            // draw heat tooltip
            final int heatValue = super.menu.getHeatValue();
            final ChatFormatting color = heatValue < HeatExchangerBlockEntity.HEAT_GIVEN_THRESHOLD ? ChatFormatting.DARK_RED : ChatFormatting.RED;
            Component heat = Component.translatable("tooltip.alloygery.heat_exchanger.heat_description").append(Component.literal(": " + heatValue)).withStyle(color);
            Component threshold = Component.translatable("tooltip.alloygery.heat_exchanger.working_threshold_description").append(Component.literal(": " + HeatExchangerBlockEntity.HEAT_GIVEN_THRESHOLD)).withStyle(color);

            guiGraphics.renderComponentTooltip(this.font, List.of(heat, threshold), x, y);
        }
    }

    private void renderLit(GuiGraphics guiGraphics)
    {
        int lit = super.menu.getLitTimeRemaining();
        if (lit > 0)
        {
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + lit_x, this.topPos + lit_y + lit_height - lit, lit_overlay_x, lit_overlay_y + lit_height - lit, lit_width, lit);
        }
    }

    private void renderProgress(GuiGraphics guiGraphics)
    {
        int progress = super.menu.getHeatedTimeRemaining();
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + progress_x, this.topPos + progress_y + progress_height - progress, progress_overlay_x, progress_overlay_y + progress_height - progress, progress_width, progress);
    }
}
