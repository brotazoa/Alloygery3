package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.compat.recipeViewers.RecipeViewerCommonBlockStateRenderers;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

import java.util.ArrayList;
import java.util.List;

public class PlaceOnHeatExchangerReiCategory implements DisplayCategory<PlaceOnHeatExchangerReiDisplay>
{
	public static final Component TITLE = Component.translatable("category.rei.alloygery.heat_exchanger_transform");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(MachinesModule.HEAT_EXCHANGER);

	@Override
	public List<Widget> setupDisplay(PlaceOnHeatExchangerReiDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, getDisplayWidth(display), getDisplayHeight())));

		widgets.add(Widgets.createArrow(new Point(bounds.x + 50, bounds.y + 25)));

		widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(bounds.getX() + 13, bounds.getY() + 25, 0);

			RecipeViewerCommonBlockStateRenderers.drawBlock(
					graphics, 0, 0, display.inBlockState
							.setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
			);

			poseStack.popPose();
		}));

		widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(bounds.getX() + 13, bounds.getY() + 60, 0);

			RecipeViewerCommonBlockStateRenderers.drawBlock(
					graphics, 0, 0, MachinesModule.HEAT_EXCHANGER.defaultBlockState()
							.setValue(HeatExchangerBlock.FACING, Direction.SOUTH)
			);

			poseStack.popPose();
		}));

		widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(bounds.getX() + 83, bounds.getY() + 51, 0);

			RecipeViewerCommonBlockStateRenderers.drawHeatExchangerWithBlock(
					graphics, 0, 0, display.outBlockState
							.setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
			);

			poseStack.popPose();
		}));

		return widgets;
	}

	@Override
	public CategoryIdentifier<? extends PlaceOnHeatExchangerReiDisplay> getCategoryIdentifier()
	{
		return AlloygeryReiPlugin.HEAT_EXCHANGER_TRANSFORM;
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
	public int getDisplayWidth(PlaceOnHeatExchangerReiDisplay display)
	{
		return 120;
	}

	@Override
	public int getDisplayHeight()
	{
		return 68;
	}
}
