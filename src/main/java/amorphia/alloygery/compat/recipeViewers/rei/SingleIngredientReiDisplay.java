package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.machines.AbstractSingleIngredientRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.RegistryAccess;

import java.util.Collections;
import java.util.Optional;

public class SingleIngredientReiDisplay extends BasicDisplay
{
	final int inputCount;
	final CategoryIdentifier<SingleIngredientReiDisplay> category;

	public SingleIngredientReiDisplay(AbstractSingleIngredientRecipe recipe, CategoryIdentifier<SingleIngredientReiDisplay> category)
	{
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))), Optional.of(recipe.getId()));
		this.inputCount = recipe.getMaterialCost();
		this.category = category;
	}

	@Override
	public CategoryIdentifier<SingleIngredientReiDisplay> getCategoryIdentifier()
	{
		return category;
	}
}
