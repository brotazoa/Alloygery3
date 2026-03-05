package amorphia.alloygery.craftingMaterials.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterial;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.craftingMaterials.CraftingMaterials;
import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.machines.datagen.recipe.AlloyingRecipeBuilder;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Consumer;

public class CraftingMaterialRecipeProvider implements AlloygeryRecipeProvider.IAlloygeryRecipeProvider
{
    @Override
    public void buildRecipes(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
    {
        // tin recipes
        makeRawOreRecipesFromMaterial(CraftingMaterials.TIN, exporter);
        makeMetalRecipesFromMaterial(CraftingMaterials.TIN, exporter);
        makeSmeltingRecipesFromMaterial(CraftingMaterials.TIN, exporter);

        // copper recipes
        makeRawOreRecipes(CraftingMaterialModule.ITEMS.get("raw_copper_nugget"), Items.RAW_COPPER, null, exporter);
        makeMetalRecipes(CraftingMaterialModule.ITEMS.get("copper_nugget"), Items.COPPER_INGOT, null, exporter);
        makeSmeltingRecipes(CraftingMaterialModule.ITEMS.get("raw_copper_nugget"), null, CraftingMaterialModule.ITEMS.get("copper_nugget"), null, exporter);

        // bronze recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.BRONZE, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("bronze_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("tin_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_sources")))
        ), false, exporter);

        // iron recipes
        makeRawOreRecipes(CraftingMaterialModule.ITEMS.get("raw_iron_nugget"), Items.RAW_IRON, null, exporter);
        makeSmeltingRecipes(CraftingMaterialModule.ITEMS.get("raw_iron_nugget"), null, Items.IRON_NUGGET, null, exporter);

        // gold recipes
        makeRawOreRecipes(CraftingMaterialModule.ITEMS.get("raw_gold_nugget"), Items.RAW_GOLD, null, exporter);
        makeSmeltingRecipes(CraftingMaterialModule.ITEMS.get("raw_gold_nugget"), null, Items.GOLD_NUGGET, null, exporter);

        // antanium recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.ANTANIUM, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("antanium_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("iron_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("iron_sources")))
        ), false, exporter);

        // steel recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.STEEL, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("steel_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("iron_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("carbon_sources")))
        ), true, exporter);

		// netherite recipes
		makeAlloyingRecipe(Items.NETHERITE_INGOT, Lists.newArrayList(
				Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_sources"))),
				Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_sources"))),
				Ingredient.of(Items.NETHERITE_SCRAP),
				Ingredient.of(Items.NETHERITE_SCRAP)
		), true, exporter);

        // nickel recipes
        makeRawOreRecipesFromMaterial(CraftingMaterials.NICKEL, exporter);
        makeMetalRecipesFromMaterial(CraftingMaterials.NICKEL, exporter);
        makeSmeltingRecipesFromMaterial(CraftingMaterials.NICKEL, exporter);

        // invar recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.INVAR, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("invar_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("nickel_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("iron_sources")))
        ), true, exporter);

        // constantan recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.CONSTANTAN, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("constantan_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("nickel_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_sources")))
        ), false, exporter);

        // titanium recipes
        makeRawOreRecipesFromMaterial(CraftingMaterials.TITANIUM, exporter);
        makeMetalRecipesFromMaterial(CraftingMaterials.TITANIUM, exporter);
        makeSmeltingRecipesFromMaterial(CraftingMaterials.TITANIUM, exporter);

        // titanium gold recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.TITANIUM_GOLD, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("titanium_gold_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("titanium_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_sources")))
        ), true, exporter);

        // nitinol recipes
        makeMetalRecipesFromMaterial(CraftingMaterials.NITINOL, exporter);
        makeAlloyingRecipe(CraftingMaterialModule.ITEMS.get("nitinol_ingot"), Lists.newArrayList(
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("titanium_sources"))),
                Ingredient.of(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("nickel_sources")))
        ), true, exporter);
    }

    private void makeRawOreRecipesFromMaterial(CraftingMaterial material, Consumer<FinishedRecipe> exporter)
    {
        final Item raw = CraftingMaterialModule.ITEMS.get("raw_" + material.getName());
        final Item raw_nugget = CraftingMaterialModule.ITEMS.get("raw_" + material.getName() + "_nugget");
        final Item raw_block = CraftingMaterialModule.BLOCKS.get("raw_" + material.getName() + "_block").asItem();

        makeRawOreRecipes(raw_nugget, raw, raw_block, exporter);
    }

    private void makeRawOreRecipes(Item raw_nugget, Item raw, Item raw_block, Consumer<FinishedRecipe> exporter)
    {
        if (raw != null && raw_nugget != null)
        {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, raw_nugget, 4)
                    .requires(raw)
                    .unlockedBy("has_raw", RecipeProvider.has(raw))
                    .unlockedBy("has_item", RecipeProvider.has(raw_nugget))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(raw_nugget).withSuffix("_from_raw_ore"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, raw)
                    .define('n', raw_nugget)
                    .pattern("nn")
                    .pattern("nn")
                    .unlockedBy("has_nugget", RecipeProvider.has(raw_nugget))
                    .unlockedBy("has_item", RecipeProvider.has(raw))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(raw).withSuffix("_from_raw_nugget"));
        }

        if (raw_block != null && raw != null)
        {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, raw, 9)
                    .requires(raw_block)
                    .unlockedBy("has_block", RecipeProvider.has(raw_block))
                    .unlockedBy("has_item", RecipeProvider.has(raw))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(raw).withSuffix("_from_raw_block"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, raw_block)
                    .define('r', raw)
                    .pattern("rrr")
                    .pattern("rrr")
                    .pattern("rrr")
                    .unlockedBy("has_raw", RecipeProvider.has(raw))
                    .unlockedBy("has_item", RecipeProvider.has(raw_block))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(raw_block).withSuffix("_from_raw_ore"));
        }
    }

    private void makeMetalRecipesFromMaterial(CraftingMaterial material, Consumer<FinishedRecipe> exporter)
    {
        final Item ingot = CraftingMaterialModule.ITEMS.get(material.getName() + "_ingot");
        final Item nugget = CraftingMaterialModule.ITEMS.get(material.getName() + "_nugget");
        final Item block = CraftingMaterialModule.BLOCKS.get(material.getName() + "_block").asItem();

        makeMetalRecipes(nugget, ingot, block, exporter);
    }

    private void makeMetalRecipes(Item nugget, Item ingot, Item block, Consumer<FinishedRecipe> exporter)
    {
        if (nugget != null && ingot != null)
        {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
                    .requires(ingot)
                    .unlockedBy("has_ingot", RecipeProvider.has(ingot))
                    .unlockedBy("has_item", RecipeProvider.has(nugget))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(nugget).withSuffix("_from_ingot"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot)
                    .define('n', nugget)
                    .pattern("nnn")
                    .pattern("nnn")
                    .pattern("nnn")
                    .unlockedBy("has_nugget", RecipeProvider.has(nugget))
                    .unlockedBy("has_item", RecipeProvider.has(ingot))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(ingot).withSuffix("_from_nuggets"));
        }

        if (ingot != null && block != null)
        {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9)
                    .requires(block)
                    .unlockedBy("has_block", RecipeProvider.has(block))
                    .unlockedBy("has_item", RecipeProvider.has(ingot))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(ingot).withSuffix("_from_block"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block)
                    .define('i', ingot)
                    .pattern("iii")
                    .pattern("iii")
                    .pattern("iii")
                    .unlockedBy("has_ingot", RecipeProvider.has(ingot))
                    .unlockedBy("has_item", RecipeProvider.has(block))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(block).withSuffix("_from_ingots"));
        }
    }

    private void makeSmeltingRecipesFromMaterial(CraftingMaterial material, Consumer<FinishedRecipe> exporter)
    {
        final Item ingot = CraftingMaterialModule.ITEMS.get(material.getName() + "_ingot");
        final Item nugget = CraftingMaterialModule.ITEMS.get(material.getName() + "_nugget");
        final Item raw = CraftingMaterialModule.ITEMS.get("raw_" + material.getName());
        final Item raw_nugget = CraftingMaterialModule.ITEMS.get("raw_" + material.getName() + "_nugget");

        makeSmeltingRecipes(raw_nugget, raw, nugget, ingot, exporter);
    }

    private void makeSmeltingRecipes(Item raw_nugget, Item raw, Item nugget, Item ingot, Consumer<FinishedRecipe> exporter)
    {
        if (raw_nugget != null && nugget != null)
        {
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(raw_nugget), RecipeCategory.MISC, nugget, 0.7f, 200)
                    .unlockedBy("has_raw", RecipeProvider.has(raw_nugget))
                    .unlockedBy("has_item", RecipeProvider.has(nugget))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(nugget).withSuffix("_from_smelting_raw_nugget"));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(raw_nugget), RecipeCategory.MISC, nugget, 0.7f, 100)
                    .unlockedBy("has_raw", RecipeProvider.has(raw_nugget))
                    .unlockedBy("has_item", RecipeProvider.has(nugget))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(nugget).withSuffix("_from_blasting_raw_nugget"));
        }

        if (raw != null && ingot != null)
        {
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(raw), RecipeCategory.MISC, ingot, 0.7f, 200)
                    .unlockedBy("has_raw", RecipeProvider.has(raw))
                    .unlockedBy("has_item", RecipeProvider.has(ingot))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(ingot).withSuffix("_from_smelting_raw_ore"));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(raw), RecipeCategory.MISC, ingot, 0.7f, 100)
                    .unlockedBy("has_raw", RecipeProvider.has(raw))
                    .unlockedBy("has_item", RecipeProvider.has(ingot))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(ingot).withSuffix("_from_blasting_raw_ore"));
        }
    }

    private void makeAlloyingRecipe(Item result, List<Ingredient> ingredients, boolean advanced, Consumer<FinishedRecipe> exporter)
    {
        final int ingredientCount = ingredients.size();
        final float experience = 0.7f * ingredientCount;
        final int time = 200 * ingredientCount;

        if (!advanced)
        {
            AlloyingRecipeBuilder.simple(RecipeCategory.MISC, result)
                    .ingredient(ingredients)
                    .count(ingredientCount)
                    .experience(experience)
                    .smeltingTime(time)
                    .unlockedBy("has_item", RecipeProvider.has(result))
                    .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix("_from_alloying_simple"));
        }

        AlloyingRecipeBuilder.advanced(RecipeCategory.MISC, result)
                .ingredient(ingredients)
                .count(ingredientCount)
                .experience(experience)
                .smeltingTime(time)
                .unlockedBy("has_item", RecipeProvider.has(result))
                .save(exporter);
    }
}
