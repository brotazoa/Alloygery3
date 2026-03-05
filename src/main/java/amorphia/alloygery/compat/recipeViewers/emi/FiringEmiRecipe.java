package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.machines.kiln.FiringRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FiringEmiRecipe implements EmiRecipe
{
	private final ResourceLocation id;
	private final EmiIngredient input;
	private final EmiStack output;
	private final int cookingTime;
	private final float experience;

	public FiringEmiRecipe(FiringRecipe recipe)
	{
		this.id = recipe.getId();
		this.input = EmiIngredient.of(recipe.getIngredients().get(0));
		this.output = EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY));
		this.cookingTime = recipe.getCookingTime();
		this.experience = recipe.getExperience();
	}

	@Override
	public EmiRecipeCategory getCategory()
	{
		return AlloygeryEmiPlugin.FIRING;
	}

	@Override
	public @Nullable ResourceLocation getId()
	{
		return this.id;
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
		return 82;
	}

	@Override
	public int getDisplayHeight()
	{
		return 38;
	}

	@Override
	public void addWidgets(WidgetHolder widgets)
	{
		widgets.addFillingArrow(24, 5, 50 * cookingTime).tooltip((mx, my) -> List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", cookingTime / 20f).getVisualOrderText())));
		widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 24);
		widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 24, 4000, false, true, true);
		widgets.addSlot(input, 0, 4);
		widgets.addSlot(output, 56, 0).large(true).recipeContext(this);
		widgets.addText(Component.translatable("emi.cooking.experience", experience).getVisualOrderText(), 26, 28, -1, true);
	}
}
