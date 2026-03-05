package amorphia.alloygery.machines.datagen.recipe;

import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlockRevertRecipe;
import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlockTransformRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeatExchangerBlockRevertRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private final Ingredient input;
    private final Item output;
    private String group;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public static HeatExchangerBlockRevertRecipeBuilder create(RecipeCategory category, Ingredient input, Item output)
    {
        return new HeatExchangerBlockRevertRecipeBuilder(category, input, output);
    }

    private HeatExchangerBlockRevertRecipeBuilder(RecipeCategory category, Ingredient input, Item output)
    {
        this.category = category;
        this.input = input;
        this.output = output;
    }

    @Override
    public HeatExchangerBlockRevertRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
    {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public HeatExchangerBlockRevertRecipeBuilder group(@Nullable String groupName)
    {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.output;
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer)
    {
        save(finishedRecipeConsumer, RecipeBuilder.getDefaultRecipeId(this.getResult()).withSuffix("_from_heat_exchanger_revert"));
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId)
    {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion(
                "has_the_recipe",
                RecipeUnlockedTrigger.unlocked(recipeId)
        ).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(recipeId, this.group == null ? "" : this.group, this.input, this.output, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    static class Result implements FinishedRecipe
    {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final Item output;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        Result(ResourceLocation id, String group, Ingredient input, Item output, Advancement.Builder advancement, ResourceLocation advancementId)
        {
            this.id = id;
            this.group = group;
            this.input = input;
            this.output = output;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            if (!this.group.isEmpty())
            {
                json.addProperty("group", this.group);
            }

            json.add("input", input.toJson());

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.output).toString());
            json.add("output", resultObject);
        }

        @Override
        public ResourceLocation getId()
        {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return HeatExchangerBlockRevertRecipe.Serializer.INSTANCE;
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
