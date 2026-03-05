package amorphia.alloygery.machines;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AbstractSingleIngredientScreen<T extends AbstractSingleIngredientMenu<? extends AbstractSingleIngredientRecipe>> extends AbstractContainerScreen<T>
{
    private static final Component NOT_ENOUGH_MATERIAL = Component.translatable("tooltip.alloygery.not_enough_material").withStyle(ChatFormatting.DARK_RED);

    private static final int scroll_thumb_x = 176;
    private static final int scroll_thumb_y = 0;
    private static final int scroll_thumb_width = 12;
    private static final int scroll_thumb_height = 15;
    private static final int scrollbar_height = 54;
    private static final int scrollbar_scrollable_height = 41;
    private static final int scrollbar_x = 119;
    private static final int scrollbar_y = 15;

    private static final int recipes_area_x = 52;
    private static final int recipes_area_y = 14;
    private static final int recipes_rows = 3;
    private static final int recipes_tile_width = 64;
    private static final int recipes_tile_height = 18;

    private static final int arrow_x = 200;
    private static final int arrow_y = 0;
    private static final int arrow_width = 12;
    private static final int arrow_height = 15;
    private static final int arrow_in_recipe_x = 30;
    private static final int arrow_in_recipe_y = 0;

    private static final int result_in_recipe_x = 46;

    private final ResourceLocation background;

    private float scrollOffset;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public AbstractSingleIngredientScreen(T menu, Inventory playerInventory, Component title, ResourceLocation background)
    {
        super(menu, playerInventory, title);
        this.background = background;
        menu.registerUpdateListener(this::containerChanged);
        --this.titleLabelY;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(background, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        final int scroll = (int) ((float) scrollbar_scrollable_height * this.scrollOffset);
        guiGraphics.blit(background, this.leftPos + scrollbar_x, this.topPos + scrollbar_y + scroll, scroll_thumb_x + (this.isScrollbarActive() ? 0 : scroll_thumb_width), scroll_thumb_y, scroll_thumb_width, scroll_thumb_height);

        final int left = this.leftPos + recipes_area_x;
        final int top = this.topPos + recipes_area_y;
        final int maxIndex = this.startIndex + recipes_rows;
        this.renderButtons(guiGraphics, mouseX, mouseY, left, top, maxIndex);
        this.renderRecipes(guiGraphics, left, top, maxIndex);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y)
    {
        super.renderTooltip(guiGraphics, x, y);
        if (this.displayRecipes)
        {
            final int left = this.leftPos + recipes_area_x;
            final int top = this.topPos + recipes_area_y;
            final int maxIndex = this.startIndex + recipes_rows;
            List<? extends AbstractSingleIngredientRecipe> recipes = this.menu.getRecipes();

            for (int i = this.startIndex; i < maxIndex && i < this.menu.getNumRecipes(); ++i)
            {
                final int localIndex = i - this.startIndex;
                final int tile_x = left;
                final int tile_y = top + localIndex * recipes_tile_height + 2;
                if (x >= tile_x && y >= tile_y && x < tile_x + recipes_tile_width && y < tile_y + recipes_tile_height)
                {
                    final boolean over_result = x >= tile_x + (recipes_tile_width - recipes_tile_width / 3);
                    if (over_result)
                    {
                        final ItemStack result = recipes.get(i).getResultItem(this.minecraft.level.registryAccess());
                        guiGraphics.renderTooltip(this.font, result, x, y);
                    }
                    else if(!this.menu.canAfford(i))
                    {
                        guiGraphics.renderTooltip(this.font, NOT_ENOUGH_MATERIAL, x, y);
                    }
                }
            }
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int maxVisibleIndex)
    {
        for(int i = this.startIndex; i < maxVisibleIndex && i < this.menu.getNumRecipes(); ++i)
        {
            final int localIndex = i - this.startIndex;
            final int tile_x = x;
            final int tile_y = y + localIndex * recipes_tile_height + 2;
            int buttonOffset = this.imageHeight;

            if(i == this.menu.getSelectedRecipeIndex())
            {
                buttonOffset += recipes_tile_height;
            }
            else if (mouseX >= tile_x && mouseY >= tile_y && mouseX < tile_x + recipes_tile_width && mouseY < tile_y + recipes_tile_height)
            {
                buttonOffset += recipes_tile_height * 2;
            }

            guiGraphics.blit(background, tile_x, tile_y - 1, 0, buttonOffset, recipes_tile_width, recipes_tile_height);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int maxVisibleIndex)
    {
        List<? extends AbstractSingleIngredientRecipe> recipes = this.menu.getRecipes();
        for(int i = this.startIndex; i < maxVisibleIndex && i < this.menu.getNumRecipes(); ++i)
        {
            final int localIndex = i - this.startIndex;
            final int tile_x = x;
            final int tile_y = y + localIndex * recipes_tile_height + 2;

            // arrow
            final int arrow = arrow_x + (this.menu.canAfford(i) ? 0 : arrow_width);
            guiGraphics.blit(background, tile_x + arrow_in_recipe_x, tile_y + arrow_in_recipe_y, arrow, arrow_y, arrow_width, arrow_height);

            // material item
            ItemStack material = this.menu.inputSlot.getItem().copy();
            material.setCount(recipes.get(i).getMaterialCost());
            guiGraphics.renderItem(material, tile_x + 4, tile_y);
            guiGraphics.renderItemDecorations(this.font, material, tile_x + 4, tile_y, material.getCount() == 1 ? "1" : null);

            // result item
            ItemStack result = recipes.get(i).assemble(this.menu.container, this.minecraft.level.registryAccess());
            guiGraphics.renderItem(result, tile_x + result_in_recipe_x, tile_y);
            if (result.getCount() > 1)
            {
                guiGraphics.renderItemDecorations(this.font, result, tile_x + result_in_recipe_x, tile_y);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.scrolling = false;
        if (this.displayRecipes)
        {
            final int left = this.leftPos + recipes_area_x;
            final int top = this.topPos + recipes_area_y;
            final int maxIndex = this.startIndex + recipes_rows;

            for(int i = this.startIndex; i < maxIndex; ++i)
            {
                int localIndex = i - this.startIndex;
                double x = mouseX - (double) (left);
                double y = mouseY - (double) (top + localIndex * recipes_tile_height);
                if (x >= 0.0 && y >= 0.0 && x < (double) recipes_tile_width && y < (double) recipes_tile_height && this.menu.clickMenuButton(this.minecraft.player, i))
                {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
                    return true;
                }
            }

            final int scrollBarLeft = this.leftPos + scrollbar_x;
            final int scrollBarTop = this.topPos + scrollbar_y;
            if (mouseX >= (double) scrollBarLeft && mouseY >= (double) scrollBarTop && mouseX < (double) (scrollBarLeft + scroll_thumb_width) && mouseY < (double) (scrollBarTop + scrollbar_height))
            {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        if (this.scrolling && this.isScrollbarActive())
        {
            final int top = this.topPos + recipes_area_y;
            final int bottom = top + 54;
            this.scrollOffset = ((float) mouseY - (float) top - 7.5f) / ((float) (bottom - top) - 15.0f);
            this.scrollOffset = Mth.clamp(this.scrollOffset, 0.0f, 1.0f);
            this.startIndex = (int) ((double) (this.scrollOffset * (float) this.getOffscreenRows()) + (double) 0.5f);
            return true;
        }
        else
        {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        if (this.isScrollbarActive())
        {
            final int offscreenRows = this.getOffscreenRows();
            float f = (float) delta / (float) offscreenRows;
            this.scrollOffset = Mth.clamp(this.scrollOffset - f, 0.0f, 1.0f);
            this.startIndex = (int) ((double) (this.scrollOffset * (float) offscreenRows) + (double) 0.5f);
        }

        return true;
    }

    protected int getOffscreenRows()
    {
        return (this.menu.getNumRecipes() - recipes_rows);
    }

    private boolean isScrollbarActive()
    {
        return this.displayRecipes && this.menu.getNumRecipes() > recipes_rows;
    }

    private void containerChanged()
    {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes)
        {
            this.scrollOffset = 0.0f;
            this.startIndex = 0;
        }
    }
}
