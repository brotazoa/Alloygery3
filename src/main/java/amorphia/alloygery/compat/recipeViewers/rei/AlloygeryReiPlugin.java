package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import amorphia.alloygery.machines.alloyKiln.SimpleAlloyingRecipe;
import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlockTransformRecipe;
import amorphia.alloygery.machines.kiln.FiringRecipe;
import amorphia.alloygery.machines.smithing_anvil.SmithingAnvilRecipe;
import amorphia.alloygery.machines.tailoring_table.TailoringTableRecipe;
import amorphia.alloygery.machines.woodcutter.WoodcutterRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class AlloygeryReiPlugin implements REIClientPlugin
{
	public static final CategoryIdentifier<AlloyingReiDisplay> ALLOYING = CategoryIdentifier.of(AlloyingRecipe.Type.ID);
	public static final CategoryIdentifier<SimpleAlloyingReiDisplay> SIMPLE_ALLOYING = CategoryIdentifier.of(SimpleAlloyingRecipe.Type.ID);
	public static final CategoryIdentifier<SingleIngredientReiDisplay> SMITHING_ANVIL = CategoryIdentifier.of(SmithingAnvilRecipe.Type.ID);
	public static final CategoryIdentifier<SingleIngredientReiDisplay> TAILORING = CategoryIdentifier.of(TailoringTableRecipe.Type.ID);
	public static final CategoryIdentifier<SingleIngredientReiDisplay> WOODCUTTING = CategoryIdentifier.of(WoodcutterRecipe.Type.ID);
	public static final CategoryIdentifier<DefaultCookingDisplay> FIRING = CategoryIdentifier.of(FiringRecipe.Type.ID);
	public static final CategoryIdentifier<HeatExchangerCookingReiDisplay> HEAT_EXCHANGER_SMELTING = CategoryIdentifier.of(Alloygery.asResource("heat_exchanger_smelting"));
	public static final CategoryIdentifier<HeatExchangerCookingReiDisplay> HEAT_EXCHANGER_BLASTING = CategoryIdentifier.of(Alloygery.asResource("heat_exchanger_blasting"));
	public static final CategoryIdentifier<HeatExchangerCookingReiDisplay> HEAT_EXCHANGER_SMOKING = CategoryIdentifier.of(Alloygery.asResource("heat_exchanger_smoking"));
	public static final CategoryIdentifier<HeatExchangerCookingReiDisplay> HEAT_EXCHANGER_FIRING = CategoryIdentifier.of(Alloygery.asResource("heat_exchanger_firing"));
	public static final CategoryIdentifier<PlaceOnHeatExchangerReiDisplay> HEAT_EXCHANGER_TRANSFORM = CategoryIdentifier.of(HeatExchangerBlockTransformRecipe.Type.ID);

	@Override
	public void registerCategories(CategoryRegistry registry)
	{
		registry.add(new AlloyingReiCategory());
		registry.add(new SimpleAlloyingReiCategory());
		registry.add(new SmithingAnvilReiCategory());
		registry.add(new TailoringReiCategory());
		registry.add(new WoodcuttingReiCategory());
		registry.add(new FiringReiCategory());
		registry.add(new SmeltingHeatExchangerCookingReiCategory());
		registry.add(new BlastingHeatExchangerCookingReiCategory());
		registry.add(new SmokingHeatExchangerCookingReiCategory());
		registry.add(new FiringHeatExchangerCookingReiCategory());
		registry.add(new PlaceOnHeatExchangerReiCategory());

		registry.addWorkstations(ALLOYING, EntryStacks.of(MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER));
		registry.addWorkstations(SIMPLE_ALLOYING, EntryStacks.of(MachinesModule.ALLOY_KILN));
		registry.addWorkstations(SMITHING_ANVIL, EntryStacks.of(MachinesModule.SMITHING_ANVIL));
		registry.addWorkstations(TAILORING, EntryStacks.of(MachinesModule.TAILORING_TABLE));
		registry.addWorkstations(WOODCUTTING, EntryStacks.of(MachinesModule.WOODCUTTER));
		registry.addWorkstations(FIRING, EntryStacks.of(MachinesModule.KILN));
		registry.addWorkstations(HEAT_EXCHANGER_SMELTING, EntryStacks.of(MachinesModule.FURNACE_WITH_HEAT_EXCHANGER));
		registry.addWorkstations(HEAT_EXCHANGER_BLASTING, EntryStacks.of(MachinesModule.BLAST_FURNACE_WITH_HEAT_EXCHANGER));
		registry.addWorkstations(HEAT_EXCHANGER_SMOKING, EntryStacks.of(MachinesModule.SMOKER_WITH_HEAT_EXCHANGER));
		registry.addWorkstations(HEAT_EXCHANGER_FIRING, EntryStacks.of(MachinesModule.KILN_WITH_HEAT_EXCHANGER));
		registry.addWorkstations(HEAT_EXCHANGER_TRANSFORM, EntryStacks.of(MachinesModule.HEAT_EXCHANGER));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry)
	{
		assert Minecraft.getInstance().level != null;
		for(AlloyingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(AlloyingRecipe.Type.INSTANCE))
		{
			registry.add(new AlloyingReiDisplay(recipe, 2));
		}

		for(SimpleAlloyingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SimpleAlloyingRecipe.Type.INSTANCE))
		{
			registry.add(new SimpleAlloyingReiDisplay(recipe, 1));
		}

		for(SmithingAnvilRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SmithingAnvilRecipe.Type.INSTANCE))
		{
			registry.add(new SingleIngredientReiDisplay(recipe, SMITHING_ANVIL));
		}

		for(TailoringTableRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TailoringTableRecipe.Type.INSTANCE))
		{
			registry.add(new SingleIngredientReiDisplay(recipe, TAILORING));
		}

		for(WoodcutterRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(WoodcutterRecipe.Type.INSTANCE))
		{
			registry.add(new SingleIngredientReiDisplay(recipe, WOODCUTTING));
		}

		for(FiringRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FiringRecipe.Type.INSTANCE))
		{
			registry.add(new DefaultCookingDisplay(recipe)
			{
				@Override
				public CategoryIdentifier<?> getCategoryIdentifier()
				{
					return FIRING;
				}
			});

			registry.add(new HeatExchangerCookingReiDisplay(recipe, HEAT_EXCHANGER_FIRING, 4));
		}

		for(SmeltingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING))
		{
			registry.add(new HeatExchangerCookingReiDisplay(recipe, HEAT_EXCHANGER_SMELTING, 2));
		}

		for(BlastingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING))
		{
			registry.add(new HeatExchangerCookingReiDisplay(recipe, HEAT_EXCHANGER_BLASTING, 4));
		}

		for(SmokingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.SMOKING))
		{
			registry.add(new HeatExchangerCookingReiDisplay(recipe, HEAT_EXCHANGER_SMOKING, 4));
		}

		for(HeatExchangerBlockTransformRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(HeatExchangerBlockTransformRecipe.Type.INSTANCE))
		{
			registry.add(new PlaceOnHeatExchangerReiDisplay(recipe));
		}
	}
}
