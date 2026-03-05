package amorphia.alloygery.compat.recipeViewers.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeatExchangerCookingEmiRecipe implements EmiRecipe
{
	private final ResourceLocation id;
	private final EmiIngredient input;
	private final EmiStack output;
	private final int cookingTime;
	private final float experience;
	private final EmiRecipeCategory category;
	private final BlockState furnace;

	public HeatExchangerCookingEmiRecipe(AbstractCookingRecipe recipe, EmiRecipeCategory category, int cookingMultiplier, BlockState furnaceBlockState)
	{
		this.id = new ResourceLocation(recipe.getId().getNamespace(), "/" + recipe.getId().getPath());
		this.input = EmiIngredient.of(recipe.getIngredients().get(0));
		this.output = EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY));
		this.cookingTime = recipe.getCookingTime() / cookingMultiplier;
		this.experience = recipe.getExperience();
		this.category = category;
		this.furnace = furnaceBlockState;
	}

	@Override
	public EmiRecipeCategory getCategory()
	{
		return category;
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
		return 46;
	}

	@Override
	public void addWidgets(WidgetHolder widgets)
	{
		widgets.addSlot(input, 5, 13);
		widgets.addFillingArrow(64, 13, (cookingTime / 20) * 1000).tooltip(
				(mx, my) -> List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", cookingTime / 20f).getVisualOrderText()))
		);
		widgets.addSlot(output, 97, 13).recipeContext(this);
		widgets.addText(Component.translatable("emi.cooking.experience", experience).getVisualOrderText(), 66, 31, -1, true);

		widgets.addDrawable(31, 41, 24, 24, new HeatExchangerWithBlockRenderWidget(furnace));
	}
}
