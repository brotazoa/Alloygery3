package amorphia.alloygery.compat.recipeViewers.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleIngredientReiCategory implements DisplayCategory<SingleIngredientReiDisplay>
{
	@Override
	public List<Widget> setupDisplay(SingleIngredientReiDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, getDisplayWidth(display), getDisplayHeight())));

		widgets.add(Widgets.createSlot(new Point(bounds.x + 5, bounds.y + 5)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 62, bounds.y + 5)).entries(display.getOutputEntries().get(0)).markOutput());
		widgets.add(Widgets.createArrow(new Point(bounds.x + 30, bounds.y + 5)));

		return widgets;
	}

	@Override
	public int getDisplayWidth(SingleIngredientReiDisplay display)
	{
		return 85;
	}

	@Override
	public int getDisplayHeight()
	{
		return 28;
	}
}
