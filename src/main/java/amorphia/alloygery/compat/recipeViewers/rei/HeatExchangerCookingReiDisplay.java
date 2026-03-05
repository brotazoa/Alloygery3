package amorphia.alloygery.compat.recipeViewers.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Optional;

public class HeatExchangerCookingReiDisplay extends BasicDisplay
{
	final int cookingTime;
	final float experience;
	final CategoryIdentifier<HeatExchangerCookingReiDisplay> category;

	public HeatExchangerCookingReiDisplay(AbstractCookingRecipe recipe, CategoryIdentifier<HeatExchangerCookingReiDisplay> category, int timeMultiplier)
	{
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))), Optional.of(recipe.getId()));
		this.cookingTime = recipe.getCookingTime() / timeMultiplier;
		this.experience = recipe.getExperience();
		this.category = category;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return category;
	}
}
