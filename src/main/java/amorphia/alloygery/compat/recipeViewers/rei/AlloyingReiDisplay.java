package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.RegistryAccess;

import java.util.Collections;
import java.util.Optional;

public class AlloyingReiDisplay extends BasicDisplay
{
	final int cookingTime;
	final float experience;

	public AlloyingReiDisplay(AlloyingRecipe recipe, int timeMultiplier)
	{
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))), Optional.of(recipe.getId()));
		this.cookingTime = recipe.getSmeltingTime() / timeMultiplier;
		this.experience = recipe.getExperience();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return AlloygeryReiPlugin.ALLOYING;
	}
}
