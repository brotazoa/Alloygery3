package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlockTransformRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Optional;

public class PlaceOnHeatExchangerReiDisplay extends BasicDisplay
{
	final BlockState inBlockState;
	final BlockState outBlockState;

	public PlaceOnHeatExchangerReiDisplay(HeatExchangerBlockTransformRecipe recipe)
	{
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))), Optional.of(recipe.getId()));

		if (recipe.getIngredient().getItems()[0].getItem() instanceof BlockItem blockItem)
		{
			inBlockState = blockItem.getBlock().defaultBlockState();
			outBlockState = recipe.transformBlock(inBlockState);
		}
		else throw new IllegalStateException("[REI display creation] HeatExchangerBlockTransformRecipe input is not a block");
	}
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return AlloygeryReiPlugin.HEAT_EXCHANGER_TRANSFORM;
	}
}
