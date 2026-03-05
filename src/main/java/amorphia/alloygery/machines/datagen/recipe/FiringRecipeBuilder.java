package amorphia.alloygery.machines.datagen.recipe;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.kiln.FiringRecipe;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FiringRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private final CookingBookCategory bookCategory;
    private final Item result;
    private final Ingredient ingredient;
    private float experience = 0.1f;
    private int cookingTime = 100;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private String group;

    public static FiringRecipeBuilder create(RecipeCategory category, Item result, Item ingredient)
    {
        FiringRecipeBuilder builder = new FiringRecipeBuilder(category, determineRecipeCategory(result), result, Ingredient.of(ingredient));
        builder.unlockedBy("has_ingredient", RecipeProvider.has(ingredient));
        return builder;
    }

    public static FiringRecipeBuilder create(RecipeCategory category, Item result, Ingredient ingredient)
    {
        return new FiringRecipeBuilder(category, determineRecipeCategory(result), result, ingredient);
    }

    private FiringRecipeBuilder(RecipeCategory category, CookingBookCategory bookCategory, Item result, Ingredient ingredient)
    {
        this.category = category;
        this.bookCategory = bookCategory;
        this.result = result;
        this.ingredient = ingredient;
    }

    public FiringRecipeBuilder experience(float experience)
    {
        this.experience = experience;
        return this;
    }

    public FiringRecipeBuilder cookingTime(int cookingTime)
    {
        this.cookingTime = cookingTime;
        return this;
    }

    @Override
    public FiringRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
    {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public FiringRecipeBuilder group(@Nullable String groupName)
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
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer)
    {
        save(finishedRecipeConsumer, RecipeBuilder.getDefaultRecipeId(this.getResult()).withSuffix("_from_firing"));
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId)
    {
        ensureValid(recipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion(
                "has_the_recipe",
                RecipeUnlockedTrigger.unlocked(recipeId)
        ).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(recipeId, this.group == null ? "" : this.group, this.bookCategory, this.ingredient, this.result, this.experience, this.cookingTime, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private static CookingBookCategory determineRecipeCategory(ItemLike result)
    {
        return result.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    static class Result implements FinishedRecipe
    {
        private final ResourceLocation id;
        private final String group;
        private final CookingBookCategory category;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        Result(ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, Item result, float experience, int cookingTime, Advancement.Builder advancement,
                ResourceLocation advancementId)
        {
            this.id = id;
            this.group = group;
            this.category = category;
            this.ingredient = ingredient;
            this.result = result;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.addProperty("category", this.category.getSerializedName());
            json.add("ingredient", this.ingredient.toJson());
            json.addProperty("result", BuiltInRegistries.ITEM.getKey(this.result).toString());
            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        @Override
        public ResourceLocation getId()
        {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return MachinesModule.FIRING_RECIPE_SERIALIZER;
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
