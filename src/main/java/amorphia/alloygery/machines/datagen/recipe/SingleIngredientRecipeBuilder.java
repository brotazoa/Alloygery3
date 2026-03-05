package amorphia.alloygery.machines.datagen.recipe;

import amorphia.alloygery.machines.MachinesModule;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SingleIngredientRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final Ingredient ingredient;
    private final int cost;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type;
    private String group;

    // smithing anvil
    public static SingleIngredientRecipeBuilder smithingAnvil(RecipeCategory category, ItemLike result, Ingredient ingredient, int cost, int count)
    {
        return new SingleIngredientRecipeBuilder(category, MachinesModule.SMITHING_ANVIL_RECIPE_SERIALIZER, result, count, ingredient, cost);
    }

    public static void smithingAnvil(ItemLike result, TagKey<Item> ingredient, Consumer<FinishedRecipe> exporter)
    {
        smithingAnvil(result, ingredient, 1, 1, null, exporter);
    }

    public static void smithingAnvil(ItemLike result, Item ingredient, Consumer<FinishedRecipe> exporter)
    {
        smithingAnvil(result, ingredient, 1, 1, null, exporter);
    }

    public static void smithingAnvil(ItemLike result, TagKey<Item> ingredient, int cost, int count, String suffix, Consumer<FinishedRecipe> exporter)
    {
        smithingAnvil(RecipeCategory.MISC, result, Ingredient.of(ingredient), cost, count)
                .unlockedBy("has_ingredient", RecipeProvider.has(ingredient))
                .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix == null || suffix.isEmpty() ? "_from_smithing_anvil" : "_from_smithing_anvil_" + suffix));
    }

    public static void smithingAnvil(ItemLike result, Item ingredient, int cost, int count, String suffix, Consumer<FinishedRecipe> exporter)
    {
        smithingAnvil(RecipeCategory.MISC, result, Ingredient.of(ingredient), cost, count)
                .unlockedBy("has_ingredient", RecipeProvider.has(ingredient))
                .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix == null || suffix.isEmpty() ? "_from_smithing_anvil" : "_from_smithing_anvil_" + suffix));
    }

    // woodcutting
    public static SingleIngredientRecipeBuilder woodcutting(RecipeCategory category, ItemLike result, Ingredient ingredient, int cost, int count)
    {
        return new SingleIngredientRecipeBuilder(category, MachinesModule.WOODCUTTING_RECIPE_SERIALIZER, result, count, ingredient, cost);
    }

    public static void woodcutting(ItemLike result, TagKey<Item> ingredient, Consumer<FinishedRecipe> exporter)
    {
        woodcutting(result, ingredient, 1, 1, null, exporter);
    }

    public static void woodcutting(ItemLike result, Item ingredient, Consumer<FinishedRecipe> exporter)
    {
        woodcutting(result, ingredient, 1, 1, null, exporter);
    }

    public static void woodcutting(ItemLike result, TagKey<Item> ingredient, int cost, int count, String suffix, Consumer<FinishedRecipe> exporter)
    {
        woodcutting(RecipeCategory.MISC, result, Ingredient.of(ingredient), cost, count)
                .unlockedBy("has_ingredient", RecipeProvider.has(ingredient))
                .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix == null || suffix.isEmpty() ? "_from_woodcutting" : "_from_woodcutting_" + suffix));
    }

    public static void woodcutting(ItemLike result, Item ingredient, int cost, int count, String suffix, Consumer<FinishedRecipe> exporter)
    {
        woodcutting(RecipeCategory.MISC, result, Ingredient.of(ingredient), cost, count)
                .unlockedBy("has_ingredient", RecipeProvider.has(ingredient))
                .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix == null || suffix.isEmpty() ? "_from_woodcutting" : "_from_woodcutting_" + suffix));
    }

    // tailoring
    public static SingleIngredientRecipeBuilder tailoring(RecipeCategory category, ItemLike result, Ingredient ingredient, int cost, int count)
    {
        return new SingleIngredientRecipeBuilder(category, MachinesModule.TAILORING_TABLE_RECIPE_SERIALIZER, result, count, ingredient, cost);
    }

    public static void tailoring(ItemLike result, TagKey<Item> ingredient, Consumer<FinishedRecipe> exporter)
    {
        tailoring(result, ingredient, 1, 1, null, exporter);
    }

    public static void tailoring(ItemLike result, Item ingredient, Consumer<FinishedRecipe> exporter)
    {
        tailoring(result, ingredient, 1, 1, null, exporter);
    }

    public static void tailoring(ItemLike result, TagKey<Item> ingredient, int cost, int count, String suffix, Consumer<FinishedRecipe> exporter)
    {
        tailoring(RecipeCategory.MISC, result, Ingredient.of(ingredient), cost, count)
                .unlockedBy("has_ingredient", RecipeProvider.has(ingredient))
                .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix == null || suffix.isEmpty() ? "_from_tailoring" : "_from_tailoring_" + suffix));
    }

    public static void tailoring(ItemLike result, Item ingredient, int cost, int count, String suffix, Consumer<FinishedRecipe> exporter)
    {
        tailoring(RecipeCategory.MISC, result, Ingredient.of(ingredient), cost, count)
                .unlockedBy("has_ingredient", RecipeProvider.has(ingredient))
                .save(exporter, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix == null || suffix.isEmpty() ? "_from_tailoring" : "_from_tailoring_" + suffix));
    }

    public SingleIngredientRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, ItemLike result, int count, Ingredient ingredient, int cost)
    {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
        this.ingredient = ingredient;
        this.cost = cost;
        this.type = type;
    }

    @Override
    public SingleIngredientRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
    {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public SingleIngredientRecipeBuilder group(@Nullable String groupName)
    {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId)
    {
        ensureValie(recipeId);

        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(RequirementsStrategy.OR);

        finishedRecipeConsumer.accept(new Result(recipeId, this.type, this.group == null ? "" : this.group, this.ingredient, this.result, this.cost, this.count, this.advancement,
                recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValie(ResourceLocation id)
    {
        if(this.ingredient.isEmpty())
            throw new IllegalStateException("Missing ingredient for recipe " + id + "!");

        if (cost < 1)
            throw new IllegalStateException("Ingredient Count is less than 1 for recipe " + id + "!");

        if(result == null || result.asItem() == Items.AIR)
            throw new IllegalStateException("Missing output for recipe " + id + "!");

        if (count < 1)
            throw new IllegalStateException("Result Count is less than 1 for recipe " + id + "!");

        if(this.advancement.getCriteria().isEmpty())
            throw new IllegalStateException("No way to obtain recipe " + id);
    }

    public static class Result implements FinishedRecipe
    {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final int cost;
        private final int count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;

        public Result(ResourceLocation id, RecipeSerializer<?> type, String group, Ingredient ingredient, Item result, int cost, int count, Advancement.Builder advancement, ResourceLocation advancementId)
        {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.cost = cost;
            this.count = count;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.type = type;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            if (!this.group.isEmpty())
            {
                json.addProperty("group", this.group);
            }

            json.add("ingredient", this.ingredient.toJson());
            json.addProperty("material_cost", this.cost);

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1)
            {
                resultObject.addProperty("count", this.count);
            }
            json.add("result", resultObject);
        }

        @Override
        public ResourceLocation getId()
        {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return this.type;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement()
        {
            return this.advancement.serializeToJson();
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId()
        {
            return this.advancementId;
        }
    }
}
