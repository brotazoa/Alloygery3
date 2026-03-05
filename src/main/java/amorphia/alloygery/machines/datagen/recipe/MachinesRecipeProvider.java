package amorphia.alloygery.machines.datagen.recipe;

import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class MachinesRecipeProvider implements AlloygeryRecipeProvider.IAlloygeryRecipeProvider
{
    @Override
    public void buildRecipes(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
    {
        MachinesModule.ITEMS.values().forEach(item -> {
            if (item instanceof IRecipeGen itemWithRecipeGen)
            {
                itemWithRecipeGen.generateRecipe(provider, exporter);
            }
        });

        MachinesModule.BLOCKS.values().forEach(block -> {
            if (block instanceof IRecipeGen blockWithRecipeGen)
            {
                blockWithRecipeGen.generateRecipe(provider, exporter);
            }
        });

        makeFiringRecipes(exporter);
        makeWoodcuttingRecipes(exporter);
        makeHeatExchangerTransformRecipes(exporter);
    }

    public interface IRecipeGen
    {
        void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter);
    }

    private void makeWoodcuttingRecipes(Consumer<FinishedRecipe> exporter)
    {
        makeWoodcuttingRecipesForWoodSet(exporter, Items.ACACIA_PLANKS, Items.ACACIA_BUTTON, Items.ACACIA_DOOR, Items.ACACIA_FENCE_GATE, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_SLAB, Items.ACACIA_STAIRS, Items.ACACIA_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.BAMBOO_PLANKS, Items.BAMBOO_BUTTON, Items.BAMBOO_DOOR, Items.BAMBOO_FENCE_GATE, Items.BAMBOO_TRAPDOOR, Items.BAMBOO_FENCE, Items.BAMBOO_SLAB, Items.BAMBOO_STAIRS, Items.BAMBOO_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.BIRCH_PLANKS, Items.BIRCH_BUTTON, Items.BIRCH_DOOR, Items.BIRCH_FENCE_GATE, Items.BIRCH_TRAPDOOR, Items.BIRCH_FENCE, Items.BIRCH_SLAB, Items.BIRCH_STAIRS, Items.BIRCH_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.CHERRY_PLANKS, Items.CHERRY_BUTTON, Items.CHERRY_DOOR, Items.CHERRY_FENCE_GATE, Items.CHERRY_TRAPDOOR, Items.CHERRY_FENCE, Items.CHERRY_SLAB, Items.CHERRY_STAIRS, Items.CHERRY_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.CRIMSON_PLANKS, Items.CRIMSON_BUTTON, Items.CRIMSON_DOOR, Items.CRIMSON_FENCE_GATE, Items.CRIMSON_TRAPDOOR, Items.CRIMSON_FENCE, Items.CRIMSON_SLAB, Items.CRIMSON_STAIRS, Items.CRIMSON_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.DARK_OAK_PLANKS, Items.DARK_OAK_BUTTON, Items.DARK_OAK_DOOR, Items.DARK_OAK_FENCE_GATE, Items.DARK_OAK_TRAPDOOR, Items.DARK_OAK_FENCE, Items.DARK_OAK_SLAB, Items.DARK_OAK_STAIRS, Items.DARK_OAK_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.JUNGLE_PLANKS, Items.JUNGLE_BUTTON, Items.JUNGLE_DOOR, Items.JUNGLE_FENCE_GATE, Items.JUNGLE_TRAPDOOR, Items.JUNGLE_FENCE, Items.JUNGLE_SLAB, Items.JUNGLE_STAIRS, Items.JUNGLE_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.MANGROVE_PLANKS, Items.MANGROVE_BUTTON, Items.MANGROVE_DOOR, Items.MANGROVE_FENCE_GATE, Items.MANGROVE_TRAPDOOR, Items.MANGROVE_FENCE, Items.MANGROVE_SLAB, Items.MANGROVE_STAIRS, Items.MANGROVE_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.OAK_PLANKS, Items.OAK_BUTTON, Items.OAK_DOOR, Items.OAK_FENCE_GATE, Items.OAK_TRAPDOOR, Items.OAK_FENCE, Items.OAK_SLAB, Items.OAK_STAIRS, Items.OAK_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.SPRUCE_PLANKS, Items.SPRUCE_BUTTON, Items.SPRUCE_DOOR, Items.SPRUCE_FENCE_GATE, Items.SPRUCE_TRAPDOOR, Items.SPRUCE_FENCE, Items.SPRUCE_SLAB, Items.SPRUCE_STAIRS, Items.SPRUCE_SIGN);
        makeWoodcuttingRecipesForWoodSet(exporter, Items.WARPED_PLANKS, Items.WARPED_BUTTON, Items.WARPED_DOOR, Items.WARPED_FENCE_GATE, Items.WARPED_TRAPDOOR, Items.WARPED_FENCE, Items.WARPED_SLAB, Items.WARPED_STAIRS, Items.WARPED_SIGN);
    }

    private void makeWoodcuttingRecipesForWoodSet(Consumer<FinishedRecipe> exporter, Item plank, Item button, Item door, Item gate, Item trapdoor, Item fence, Item slab, Item stair, Item sign)
    {
        SingleIngredientRecipeBuilder.woodcutting(button, plank, exporter);
        SingleIngredientRecipeBuilder.woodcutting(door, plank, exporter);
        SingleIngredientRecipeBuilder.woodcutting(gate, plank, exporter);
        SingleIngredientRecipeBuilder.woodcutting(trapdoor, plank, exporter);
        SingleIngredientRecipeBuilder.woodcutting(fence, plank, exporter);
        SingleIngredientRecipeBuilder.woodcutting(slab, plank, 1, 2, null, exporter);
        SingleIngredientRecipeBuilder.woodcutting(stair, plank, exporter);
        SingleIngredientRecipeBuilder.woodcutting(sign, plank, exporter);
    }

    private void makeFiringRecipes(Consumer<FinishedRecipe> exporter)
    {
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.STONE, Items.COBBLESTONE).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.SMOOTH_STONE, Items.STONE).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.CRACKED_STONE_BRICKS, Items.STONE_BRICKS).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.DEEPSLATE, Items.COBBLED_DEEPSLATE).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.CRACKED_DEEPSLATE_BRICKS, Items.DEEPSLATE_BRICKS).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.CRACKED_DEEPSLATE_TILES, Items.DEEPSLATE_TILES).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.SMOOTH_SANDSTONE, Items.SANDSTONE).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.SMOOTH_RED_SANDSTONE, Items.RED_SANDSTONE).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.CRACKED_NETHER_BRICKS, Items.NETHER_BRICKS).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.SMOOTH_BASALT, Items.BASALT).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICKS).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.SMOOTH_QUARTZ, Items.QUARTZ_BLOCK).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.TERRACOTTA, Items.CLAY).experience(0.35f).save(exporter);

        // glazed terracotta
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.WHITE_GLAZED_TERRACOTTA, Items.WHITE_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.ORANGE_GLAZED_TERRACOTTA, Items.ORANGE_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.MAGENTA_GLAZED_TERRACOTTA, Items.MAGENTA_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.LIGHT_BLUE_GLAZED_TERRACOTTA, Items.LIGHT_BLUE_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.YELLOW_GLAZED_TERRACOTTA, Items.YELLOW_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.LIME_GLAZED_TERRACOTTA, Items.LIME_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.PINK_GLAZED_TERRACOTTA, Items.PINK_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.GRAY_GLAZED_TERRACOTTA, Items.GRAY_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.LIGHT_GRAY_GLAZED_TERRACOTTA, Items.LIGHT_GRAY_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.CYAN_GLAZED_TERRACOTTA, Items.CYAN_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.PURPLE_GLAZED_TERRACOTTA, Items.PURPLE_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.BLUE_GLAZED_TERRACOTTA, Items.BLUE_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.BROWN_GLAZED_TERRACOTTA, Items.BROWN_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.GREEN_GLAZED_TERRACOTTA, Items.GREEN_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.RED_GLAZED_TERRACOTTA, Items.RED_TERRACOTTA).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.BLACK_GLAZED_TERRACOTTA, Items.BLACK_TERRACOTTA).save(exporter);

        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.GLASS, Ingredient.of(ItemTags.SAND)).unlockedBy("has_sand", RecipeProvider.has(ItemTags.SAND)).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.BRICK, Items.CLAY_BALL).experience(0.3f).save(exporter);
        FiringRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.NETHER_BRICK, Items.NETHERRACK).save(exporter);
    }

    private void makeHeatExchangerTransformRecipes(Consumer<FinishedRecipe> exporter)
    {
        // alloy kiln
        HeatExchangerBlockTransformRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("alloy_kiln").asItem()), MachinesModule.BLOCKS.get("alloy_kiln_with_heat_exchanger").asItem()
        ).save(exporter);
        HeatExchangerBlockRevertRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("alloy_kiln_with_heat_exchanger").asItem()), MachinesModule.BLOCKS.get("alloy_kiln").asItem()
        ).save(exporter);

        // kiln
        HeatExchangerBlockTransformRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("kiln").asItem()), MachinesModule.BLOCKS.get("kiln_with_heat_exchanger").asItem()
        ).save(exporter);
        HeatExchangerBlockRevertRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("kiln_with_heat_exchanger").asItem()), MachinesModule.BLOCKS.get("kiln").asItem()
        ).save(exporter);

        // furnace
        HeatExchangerBlockTransformRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(Blocks.FURNACE.asItem()), MachinesModule.BLOCKS.get("furnace_with_heat_exchanger").asItem()
        ).save(exporter);
        HeatExchangerBlockRevertRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("furnace_with_heat_exchanger").asItem()), Blocks.FURNACE.asItem()
        ).save(exporter);

        // blast furnace
        HeatExchangerBlockTransformRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(Blocks.BLAST_FURNACE.asItem()), MachinesModule.BLOCKS.get("blast_furnace_with_heat_exchanger").asItem()
        ).save(exporter);
        HeatExchangerBlockRevertRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("blast_furnace_with_heat_exchanger").asItem()), Blocks.BLAST_FURNACE.asItem()
        ).save(exporter);

        // smoker
        HeatExchangerBlockTransformRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(Blocks.SMOKER.asItem()), MachinesModule.BLOCKS.get("smoker_with_heat_exchanger").asItem()
        ).save(exporter);
        HeatExchangerBlockRevertRecipeBuilder.create(
                RecipeCategory.MISC, Ingredient.of(MachinesModule.BLOCKS.get("smoker_with_heat_exchanger").asItem()), Blocks.SMOKER.asItem()
        ).save(exporter);
    }
}
