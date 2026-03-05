package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.compat.recipeViewers.RecipeViewerCommonBlockStateRenderers;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import amorphia.alloygery.machines.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerBlock;
import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlloyingEmiRecipe implements EmiRecipe
{
	private final ResourceLocation id;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> output;
	private final int time;
	private final float experience;

	AlloyingEmiRecipe(AlloyingRecipe recipe)
	{
		this.id = recipe.getId();
		this.inputs = Lists.newArrayList();
		recipe.getIngredients().forEach(ingredient -> inputs.add(EmiIngredient.of(ingredient)));
		this.output = Lists.newArrayList(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
		this.time = recipe.getSmeltingTime() / 2;
		this.experience = recipe.getExperience();
	}

	@Override
	public EmiRecipeCategory getCategory()
	{
		return AlloygeryEmiPlugin.ALLOYING;
	}

	@Override
	public @Nullable ResourceLocation getId()
	{
		return this.id;
	}

	@Override
	public List<EmiIngredient> getInputs()
	{
		return this.inputs;
	}

	@Override
	public List<EmiStack> getOutputs()
	{
		return this.output;
	}

	@Override
	public int getDisplayWidth()
	{
		return 138;
	}

	@Override
	public int getDisplayHeight()
	{
		return 46;
	}

	@Override
	public void addWidgets(WidgetHolder widgets)
	{
		widgets.addSlot(inputs.get(0), 5, 5);
		widgets.addSlot(inputs.size() > 1 ? inputs.get(1) : EmiIngredient.of(Ingredient.EMPTY), 23, 5);
		widgets.addSlot(inputs.size() > 2 ? inputs.get(2) : EmiIngredient.of(Ingredient.EMPTY), 5, 23);
		widgets.addSlot(inputs.size() > 3 ? inputs.get(3) : EmiIngredient.of(Ingredient.EMPTY), 23, 23);

		widgets.addFillingArrow(82, 13, (time / 20) * 1000).tooltip(
				(mx, my) -> List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", time / 20f).getVisualOrderText()))
		);

		widgets.addSlot(output.get(0), 115, 13).recipeContext(this);

		widgets.addDrawable(49, 41, 24, 24, (draw, mouseX, mouseY, delta) -> RecipeViewerCommonBlockStateRenderers.drawHeatExchangerWithBlock(
				draw, 0, 0, MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER.defaultBlockState().setValue(AlloyKilnWithHeatExchangerBlock.FACING, Direction.SOUTH).setValue(AlloyKilnWithHeatExchangerBlock.LIT, true))
		);

		widgets.addText(Component.translatable("emi.cooking.experience", experience).getVisualOrderText(), 84, 31, -1, true);
	}
}
