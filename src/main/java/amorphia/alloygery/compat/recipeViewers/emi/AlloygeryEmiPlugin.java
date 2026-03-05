package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import amorphia.alloygery.machines.alloyKiln.SimpleAlloyingRecipe;
import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlockTransformRecipe;
import amorphia.alloygery.machines.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.furnace.FurnaceWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.kiln.KilnWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.smoker.SmokerWithHeatExchangerBlock;
import amorphia.alloygery.machines.kiln.FiringRecipe;
import amorphia.alloygery.machines.smithing_anvil.SmithingAnvilRecipe;
import amorphia.alloygery.machines.tailoring_table.TailoringTableRecipe;
import amorphia.alloygery.machines.woodcutter.WoodcutterRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.Direction;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class AlloygeryEmiPlugin implements EmiPlugin
{
	public static final EmiStack ALLOY_KILN = EmiStack.of(MachinesModule.ALLOY_KILN);
	public static final EmiStack HEAT_EXCHANGER = EmiStack.of(MachinesModule.HEAT_EXCHANGER);
	public static final EmiStack SMITHING_ANVIL = EmiStack.of(MachinesModule.SMITHING_ANVIL);
	public static final EmiStack TAILORING_TABLE = EmiStack.of(MachinesModule.TAILORING_TABLE);
	public static final EmiStack WOODCUTTER = EmiStack.of(MachinesModule.WOODCUTTER);
	public static final EmiStack KILN = EmiStack.of(MachinesModule.KILN);

	public static final EmiStack ALLOY_KILN_WITH_EXCHANGER = EmiStack.of(MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER);
	public static final EmiStack BLAST_FURNACE_WITH_HEAT_EXCHANGER = EmiStack.of(MachinesModule.BLAST_FURNACE_WITH_HEAT_EXCHANGER);
	public static final EmiStack FURNACE_WITH_HEAT_EXCHANGER = EmiStack.of(MachinesModule.FURNACE_WITH_HEAT_EXCHANGER);
	public static final EmiStack KILN_WITH_HEAT_EXCHANGER = EmiStack.of(MachinesModule.KILN_WITH_HEAT_EXCHANGER);
	public static final EmiStack SMOKER_WITH_HEAT_EXCHANGER = EmiStack.of(MachinesModule.SMOKER_WITH_HEAT_EXCHANGER);

	public static final EmiRecipeCategory ALLOYING = new EmiRecipeCategory(Alloygery.asResource("emi_alloying"), ALLOY_KILN_WITH_EXCHANGER);
	public static final EmiRecipeCategory SIMPLE_ALLOYING = new EmiRecipeCategory(Alloygery.asResource("emi_simple_alloying"), ALLOY_KILN);
	public static final EmiRecipeCategory SMITHING = new EmiRecipeCategory(Alloygery.asResource("emi_smithing_anvil"), SMITHING_ANVIL);
	public static final EmiRecipeCategory TAILORING = new EmiRecipeCategory(Alloygery.asResource("emi_tailoring"), TAILORING_TABLE);
	public static final EmiRecipeCategory WOODCUTTING = new EmiRecipeCategory(Alloygery.asResource("emi_woodcutting"), WOODCUTTER);
	public static final EmiRecipeCategory FIRING = new EmiRecipeCategory(Alloygery.asResource("emi_firing"), KILN);

	public static final EmiRecipeCategory HEAT_EXCHANGER_BLASTING = new EmiRecipeCategory(Alloygery.asResource("emi_heat_exchanger_blasting"), BLAST_FURNACE_WITH_HEAT_EXCHANGER);
	public static final EmiRecipeCategory HEAT_EXCHANGER_SMELTING = new EmiRecipeCategory(Alloygery.asResource("emi_heat_exchanger_smelting"), FURNACE_WITH_HEAT_EXCHANGER);
	public static final EmiRecipeCategory HEAT_EXCHANGER_FIRING = new EmiRecipeCategory(Alloygery.asResource("emi_heat_exchanger_firing"), KILN_WITH_HEAT_EXCHANGER);
	public static final EmiRecipeCategory HEAT_EXCHANGER_SMOKING = new EmiRecipeCategory(Alloygery.asResource("emi_heat_exchanger_smoking"), SMOKER_WITH_HEAT_EXCHANGER);

	public static final EmiRecipeCategory HEAT_EXCHANGER_TRANSFORM = new EmiRecipeCategory(Alloygery.asResource("emi_heat_exchanger_transform"), HEAT_EXCHANGER);

	@Override
	public void register(EmiRegistry emiRegistry)
	{
		emiRegistry.addCategory(ALLOYING);
		emiRegistry.addCategory(SIMPLE_ALLOYING);
		emiRegistry.addCategory(SMITHING);
		emiRegistry.addCategory(TAILORING);
		emiRegistry.addCategory(WOODCUTTING);
		emiRegistry.addCategory(FIRING);
		emiRegistry.addCategory(HEAT_EXCHANGER_BLASTING);
		emiRegistry.addCategory(HEAT_EXCHANGER_SMELTING);
		emiRegistry.addCategory(HEAT_EXCHANGER_FIRING);
		emiRegistry.addCategory(HEAT_EXCHANGER_SMOKING);
		emiRegistry.addCategory(HEAT_EXCHANGER_TRANSFORM);

		emiRegistry.addWorkstation(ALLOYING, ALLOY_KILN_WITH_EXCHANGER);
		emiRegistry.addWorkstation(SIMPLE_ALLOYING, ALLOY_KILN);
		emiRegistry.addWorkstation(SMITHING, SMITHING_ANVIL);
		emiRegistry.addWorkstation(TAILORING, TAILORING_TABLE);
		emiRegistry.addWorkstation(WOODCUTTING, WOODCUTTER);
		emiRegistry.addWorkstation(FIRING, KILN);
		emiRegistry.addWorkstation(HEAT_EXCHANGER_BLASTING, BLAST_FURNACE_WITH_HEAT_EXCHANGER);
		emiRegistry.addWorkstation(HEAT_EXCHANGER_SMELTING, FURNACE_WITH_HEAT_EXCHANGER);
		emiRegistry.addWorkstation(HEAT_EXCHANGER_FIRING, KILN_WITH_HEAT_EXCHANGER);
		emiRegistry.addWorkstation(HEAT_EXCHANGER_SMOKING, SMOKER_WITH_HEAT_EXCHANGER);
		emiRegistry.addWorkstation(HEAT_EXCHANGER_TRANSFORM, HEAT_EXCHANGER);

		for (AlloyingRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(AlloyingRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new AlloyingEmiRecipe(recipe));
		}

		for(SimpleAlloyingRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(SimpleAlloyingRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new SimpleAlloyingEmiRecipe(recipe));
		}

		for(SmithingAnvilRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(SmithingAnvilRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new SingleIngredientEmiRecipe(recipe, SMITHING));
		}

		for(TailoringTableRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(TailoringTableRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new SingleIngredientEmiRecipe(recipe, TAILORING));
		}

		for(WoodcutterRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(WoodcutterRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new SingleIngredientEmiRecipe(recipe, WOODCUTTING));
		}

		for(FiringRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(FiringRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new FiringEmiRecipe(recipe));
			emiRegistry.addRecipe(new HeatExchangerCookingEmiRecipe(recipe, HEAT_EXCHANGER_FIRING, 4, MachinesModule.KILN_WITH_HEAT_EXCHANGER.defaultBlockState()
					.setValue(KilnWithHeatExchangerBlock.FACING, Direction.SOUTH)
					.setValue(KilnWithHeatExchangerBlock.LIT, true))
			);
		}

		for(SmeltingRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING))
		{
			emiRegistry.addRecipe(new HeatExchangerCookingEmiRecipe(recipe, HEAT_EXCHANGER_SMELTING, 2, MachinesModule.FURNACE_WITH_HEAT_EXCHANGER.defaultBlockState()
					.setValue(FurnaceWithHeatExchangerBlock.FACING, Direction.SOUTH)
					.setValue(FurnaceWithHeatExchangerBlock.LIT, true))
			);
		}

		for(BlastingRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING))
		{
			emiRegistry.addRecipe(new HeatExchangerCookingEmiRecipe(recipe, HEAT_EXCHANGER_BLASTING, 4, MachinesModule.BLAST_FURNACE_WITH_HEAT_EXCHANGER.defaultBlockState()
					.setValue(BlastFurnaceWithHeatExchangerBlock.FACING, Direction.SOUTH)
					.setValue(BlastFurnaceWithHeatExchangerBlock.LIT, true))
			);
		}

		for(SmokingRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(RecipeType.SMOKING))
		{
			emiRegistry.addRecipe(new HeatExchangerCookingEmiRecipe(recipe, HEAT_EXCHANGER_SMOKING, 4, MachinesModule.SMOKER_WITH_HEAT_EXCHANGER.defaultBlockState()
					.setValue(SmokerWithHeatExchangerBlock.FACING, Direction.SOUTH)
					.setValue(SmokerWithHeatExchangerBlock.LIT, true))
			);
		}

		for(HeatExchangerBlockTransformRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(HeatExchangerBlockTransformRecipe.Type.INSTANCE))
		{
			emiRegistry.addRecipe(new PlaceOnHeatExchangerEmiRecipe(recipe));
		}
	}
}
