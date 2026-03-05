package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.machines.AbstractSingleIngredientRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SingleIngredientEmiRecipe implements EmiRecipe
{
	private final ResourceLocation id;
	private final List<EmiIngredient> inputs;
	private final int inputAmount;
	private final List<EmiStack> output;
	private final EmiRecipeCategory category;

	public SingleIngredientEmiRecipe(AbstractSingleIngredientRecipe recipe, EmiRecipeCategory category)
	{
		this.id = recipe.getId();
		this.inputs = List.of(EmiIngredient.of(recipe.getIngredient()));
		this.inputAmount = recipe.getMaterialCost();
		this.output = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
		this.category = category;
	}

	@Override
	public EmiRecipeCategory getCategory()
	{
		return category;
	}

	@Override
	public @Nullable ResourceLocation getId()
	{
		return this.id;
	}

	@Override
	public List<EmiIngredient> getInputs()
	{
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs()
	{
		return output;
	}

	@Override
	public int getDisplayWidth()
	{
		return 85;
	}

	@Override
	public int getDisplayHeight()
	{
		return 28;
	}

	@Override
	public void addWidgets(WidgetHolder widgets)
	{
		widgets.addSlot(inputs.get(0).setAmount(inputAmount), 5, 5);
		widgets.addSlot(output.get(0), 62,5).recipeContext(this);
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 30, 5);
	}
}
