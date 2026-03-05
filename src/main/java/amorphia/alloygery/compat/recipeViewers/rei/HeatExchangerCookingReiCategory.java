package amorphia.alloygery.compat.recipeViewers.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class HeatExchangerCookingReiCategory implements DisplayCategory<HeatExchangerCookingReiDisplay>
{
	@Override
	public List<Widget> setupDisplay(HeatExchangerCookingReiDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, getDisplayWidth(display), getDisplayHeight())));

		widgets.add(Widgets.createSlot(new Point(bounds.x + 5, bounds.y + 13)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 97, bounds.y + 13)).entries(display.getOutputEntries().get(0)).markOutput());

		widgets.add(Widgets.withTooltip(Widgets.createArrow(new Point(bounds.x + 64, bounds.y + 13)), Component.translatable("category.rei.campfire.time", display.cookingTime / 20f)));
		widgets.add(Widgets.createLabel(new Point(bounds.x + 76, bounds.y + 31), Component.translatable("category.rei.cooking.xp", display.experience)));

		widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(bounds.getX() + 31, bounds.getY() + 41, 0);

			drawFurnaceBlockState(graphics, 0, 0);

			poseStack.popPose();
		}));

		return widgets;
	}

	public abstract void drawFurnaceBlockState(GuiGraphics graphics, int x, int y);

	@Override
	public int getDisplayWidth(HeatExchangerCookingReiDisplay display)
	{
		return 120;
	}

	@Override
	public int getDisplayHeight()
	{
		return 46;
	}
}
