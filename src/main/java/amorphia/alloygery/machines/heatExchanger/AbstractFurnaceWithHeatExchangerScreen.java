package amorphia.alloygery.machines.heatExchanger;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class AbstractFurnaceWithHeatExchangerScreen<T extends AbstractFurnaceWithHeatExchangerMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener
{
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private static final int lit_x = 56;
    private static final int lit_y = 35;
    private static final int lit_overlay_x = 216;
    private static final int lit_overlay_y = 0;
    private static final int lit_width = 16;
    private static final int lit_height = 16;
    private static final int progress_x = 79;
    private static final int progress_y = 34;
    private static final int progress_overlay_x = 176;
    private static final int progress_overlay_y = 14;
    private static final int progress_width = 24;
    private static final int progress_height = 17;
    private static final int heat_exchanger_x = 56;
    private static final int heat_exchanger_y = 53;
    private static final int heat_exchanger_overlay_x = 200;
    private static final int heat_exchanger_overlay_y = 0;
    private static final int heat_exchanger_width = 16;
    private static final int heat_exchanger_height = 16;

    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private final ResourceLocation background;

    private boolean narrow;

    public AbstractFurnaceWithHeatExchangerScreen(T menu, AbstractFurnaceRecipeBookComponent recipeBookComponent, Inventory playerInventory, Component title, ResourceLocation background)
    {
        super(menu, playerInventory, title);
        this.recipeBookComponent = recipeBookComponent;
        this.background = background;
    }

    @Override
    public void init()
    {
        super.init();
        this.narrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.narrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, button -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            button.setPosition(this.leftPos + 20, this.height / 2 - 49);
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void containerTick()
    {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        if (this.recipeBookComponent.isVisible() && this.narrow) {
            this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            this.recipeBookComponent.renderGhostRecipe(guiGraphics, this.leftPos, this.topPos, true, partialTick);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        guiGraphics.blit(this.background, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isHeatExchangerLit())
        {
            guiGraphics.blit(this.background, this.leftPos + heat_exchanger_x, this.topPos + heat_exchanger_y, heat_exchanger_overlay_x, heat_exchanger_overlay_y, heat_exchanger_width, heat_exchanger_height);
        }

        int lit = this.menu.getHeatedTimeRemaining();
        guiGraphics.blit(this.background, this.leftPos + lit_x, this.topPos + lit_y + lit_height - lit, lit_overlay_x, lit_overlay_y + lit_height - lit, lit_width, lit + 1);

        int progress = this.menu.getSmeltingProgress();
        guiGraphics.blit(this.background, this.leftPos + progress_x, this.topPos + progress_y, progress_overlay_x, progress_overlay_y, progress + 1, progress_height);
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button))
        {
            return true;
        }
        else
        {
            return this.narrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type)
    {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        return !this.recipeBookComponent.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton)
    {
        boolean bl = mouseX < (double) guiLeft || mouseY < (double) guiTop || mouseX >= (double) (guiLeft + this.imageWidth) || mouseY >= (double) (guiTop + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && bl;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        return this.recipeBookComponent.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    @Override
    public void recipesUpdated()
    {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent()
    {
        return this.recipeBookComponent;
    }
}
