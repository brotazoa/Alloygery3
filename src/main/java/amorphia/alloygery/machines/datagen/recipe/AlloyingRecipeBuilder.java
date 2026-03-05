package amorphia.alloygery.machines.datagen.recipe;

import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import amorphia.alloygery.machines.alloyKiln.SimpleAlloyingRecipe;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class AlloyingRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private final Item result;
    private int count = 1;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private float experience = 0.7f;
    private int smeltingTime = 800;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private String group;
    private final RecipeSerializer<? extends AlloyingRecipe> serializer;

    public static AlloyingRecipeBuilder simple(RecipeCategory category, Item result)
    {
        return new AlloyingRecipeBuilder(category, result, SimpleAlloyingRecipe.Serializer.INSTANCE);
    }

    public static AlloyingRecipeBuilder advanced(RecipeCategory category, Item result)
    {
        return new AlloyingRecipeBuilder(category, result, AlloyingRecipe.Serializer.INSTANCE);
    }

    private AlloyingRecipeBuilder(RecipeCategory category, Item result, RecipeSerializer<? extends AlloyingRecipe> serializer)
    {
        this.category = category;
        this.result = result;
        this.serializer = serializer;
    }

    public AlloyingRecipeBuilder count(int count)
    {
        this.count = count;
        return this;
    }

    public AlloyingRecipeBuilder experience(float experience)
    {
        this.experience = experience;
        return this;
    }

    public AlloyingRecipeBuilder smeltingTime(int smeltingTime)
    {
        this.smeltingTime = smeltingTime;
        return this;
    }

    public AlloyingRecipeBuilder ingredient(Ingredient ingredient)
    {
        if(ingredient == null || ingredient.isEmpty())
            throw new IllegalArgumentException("Can not add null ingredient to alloying recipe");

        ingredients.add(ingredient);
        return this;
    }

    public AlloyingRecipeBuilder ingredient(List<Ingredient> list)
    {
        for(Ingredient ingredient : list)
        {
            ingredient(ingredient);
        }

        return this;
    }

    @Override
    public AlloyingRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
    {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public AlloyingRecipeBuilder group(@Nullable String groupName)
    {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer)
    {
        save(finishedRecipeConsumer, RecipeBuilder.getDefaultRecipeId(this.getResult()).withSuffix("_from_alloying"));
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId)
    {
        this.ensureValid(recipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion(
                "has_the_recipe",
                RecipeUnlockedTrigger.unlocked(recipeId)
        ).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(recipeId, this.group == null ? "" : this.group, this.ingredients, this.result, this.count, this.experience, this.smeltingTime, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.serializer));
    }

    private void ensureValid(ResourceLocation id)
    {
        if(this.ingredients.isEmpty())
            throw new IllegalStateException("Missing ingredients for Alloying Recipe " + id + "!");

        if(result == null || result.asItem() == Items.AIR)
            throw new IllegalStateException("Missing output for Alloying Recipe " + id + "!");

        if(ingredients.size() > 4)
            throw new IllegalStateException("Alloying recipe " + id + " has more than 4 ingredients.");

        if(this.advancement.getCriteria().isEmpty())
            throw new IllegalStateException("No way to obtain recipe " + id);
    }

    static class Result implements FinishedRecipe
    {
        private final ResourceLocation id;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Item result;
        private final int count;
        private final float experience;
        private final int time;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<? extends AlloyingRecipe> serializer;

        public Result(ResourceLocation id, String group, List<Ingredient> ingredients, Item result, int count, float experience, int smeltingTime, Advancement.Builder advancement, ResourceLocation advancementId, RecipeSerializer<? extends AlloyingRecipe> serializer)
        {
            this.id = id;
            this.group = group;
            this.ingredients = ingredients;
            this.result = result;
            this.count = count;
            this.experience = experience;
            this.time = smeltingTime;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.serializer = serializer;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            if (!this.group.isEmpty())
            {
                json.addProperty("group", this.group);
            }

            JsonArray inputArray = new JsonArray();
            for(Ingredient input : ingredients)
            {
                inputArray.add(input.toJson());
            }
            json.add("inputs", inputArray);

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if(this.count > 1)
            {
                resultObject.addProperty("count", this.count);
            }
            json.add("output", resultObject);

            json.addProperty("experience", this.experience);
            json.addProperty("smelting_time", this.time);
        }

        @Override
        public ResourceLocation getId()
        {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return this.serializer;
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
