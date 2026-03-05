package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.compat.recipeViewers.RecipeViewerCommonBlockStateRenderers;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SimpleAlloyingReiCategory implements DisplayCategory<SimpleAlloyingReiDisplay>
{
	public static final Component TITLE = Component.translatable("category.rei.alloygery.simple_alloying");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(MachinesModule.ALLOY_KILN);

	@Override
	public List<Widget> setupDisplay(SimpleAlloyingReiDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, getDisplayWidth(display), getDisplayHeight())));

		widgets.add(Widgets.createSlot(new Point(bounds.x + 5, bounds.y + 5)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 23, bounds.y + 5)).entries(display.getInputEntries().size() > 1 ? display.getInputEntries().get(1) : EntryIngredients.of(ItemStack.EMPTY)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 5, bounds.y + 23)).entries(display.getInputEntries().size() > 2 ? display.getInputEntries().get(2) : EntryIngredients.of(ItemStack.EMPTY)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 23, bounds.y + 23)).entries(display.getInputEntries().size() > 3 ? display.getInputEntries().get(3) : EntryIngredients.of(ItemStack.EMPTY)).markInput());

		widgets.add(Widgets.withTooltip(Widgets.createArrow(new Point(bounds.x + 82, bounds.y + 13)), Component.translatable("category.rei.campfire.time", display.cookingTime / 20f)));

		widgets.add(Widgets.createSlot(new Point(bounds.x + 115, bounds.y + 13)).entries(display.getOutputEntries().get(0)).markOutput());

		widgets.add(Widgets.createLabel(new Point(bounds.x + 94, bounds.y + 31), Component.translatable("category.rei.cooking.xp", display.experience)));

		widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(bounds.getX() + 49, bounds.getY() + 30, 0);

			RecipeViewerCommonBlockStateRenderers.drawBlock(
					graphics, 0, 0, MachinesModule.ALLOY_KILN.defaultBlockState()
							.setValue(AlloyKilnWithHeatExchangerBlock.FACING, Direction.SOUTH)
							.setValue(AlloyKilnWithHeatExchangerBlock.LIT, true));

			poseStack.popPose();
		}));

		return widgets;
	}

	@Override
	public CategoryIdentifier<? extends SimpleAlloyingReiDisplay> getCategoryIdentifier()
	{
		return AlloygeryReiPlugin.SIMPLE_ALLOYING;
	}

	@Override
	public Component getTitle()
	{
		return TITLE;
	}

	@Override
	public Renderer getIcon()
	{
		return ICON;
	}

	@Override
	public int getDisplayWidth(SimpleAlloyingReiDisplay display)
	{
		return 138;
	}

	@Override
	public int getDisplayHeight()
	{
		return 46;
	}
}
