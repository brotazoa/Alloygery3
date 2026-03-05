package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlockTransformRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlaceOnHeatExchangerEmiRecipe implements EmiRecipe
{
	private final ResourceLocation id;
	private final EmiIngredient input;
	private final EmiStack output;

	private final BlockState inBlockState;
	private final BlockState outBlockState;

	public PlaceOnHeatExchangerEmiRecipe(HeatExchangerBlockTransformRecipe recipe)
	{
		this.id = recipe.getId();
		this.input = EmiIngredient.of(recipe.getIngredient());
		this.output = EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY));

		if (recipe.getIngredient().getItems()[0].getItem() instanceof BlockItem blockItem)
		{
			inBlockState = blockItem.getBlock().defaultBlockState();
			outBlockState = recipe.transformBlock(inBlockState);
		}
		else throw new IllegalStateException("[EMI recipe creation] HeatExchangerBlockTransformRecipe input is not a block");
	}

	@Override
	public EmiRecipeCategory getCategory()
	{
		return AlloygeryEmiPlugin.HEAT_EXCHANGER_TRANSFORM;
	}

	@Override
	public @Nullable ResourceLocation getId()
	{
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs()
	{
		return List.of(input);
	}

	@Override
	public List<EmiStack> getOutputs()
	{
		return List.of(output);
	}

	@Override
	public int getDisplayWidth()
	{
		return 120;
	}

	@Override
	public int getDisplayHeight()
	{
		return 68;
	}

	@Override
	public void addWidgets(WidgetHolder widgets)
	{
		widgets.addDrawable(13, 25, 24, 24, new BlockRenderWidget(inBlockState.setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH)));
		widgets.addDrawable(13, 60, 24, 24, new BlockRenderWidget(MachinesModule.HEAT_EXCHANGER.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH)));
		widgets.addDrawable(83, 51, 24, 24, new HeatExchangerWithBlockRenderWidget(outBlockState.setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH)));

		widgets.addTexture(EmiTexture.EMPTY_ARROW, 50, 25);
	}
}
