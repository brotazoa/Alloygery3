package amorphia.alloygery.gear.datagen.recipe;

import amorphia.alloygery.gear.recipe.ArmorPlatingRecipeShapeless;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ArmorPlatingShapelessRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder
{
	private final RecipeCategory category;
	private final Item result;
	private final int count;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
	@Nullable
	private String group;

	public ArmorPlatingShapelessRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
		this.category = category;
		this.result = result.asItem();
		this.count = count;
	}

	public static ArmorPlatingShapelessRecipeBuilder armorPlating(ItemLike result, Ingredient base, Ingredient part)
	{
		return new ArmorPlatingShapelessRecipeBuilder(RecipeCategory.COMBAT, result, 1).requires(base).requires(part);
	}

	public ArmorPlatingShapelessRecipeBuilder requires(TagKey<Item> tag)
	{
		return this.requires(Ingredient.of(tag));
	}

	public ArmorPlatingShapelessRecipeBuilder requires(ItemLike item)
	{
		return this.requires(item, 1);
	}

	public ArmorPlatingShapelessRecipeBuilder requires(ItemLike item, int quantity)
	{
		for(int i = 0; i < quantity; ++i)
		{
			this.requires(Ingredient.of(item));
		}

		return this;
	}

	public ArmorPlatingShapelessRecipeBuilder requires(Ingredient ingredient)
	{
		return this.requires(ingredient, 1);
	}

	public ArmorPlatingShapelessRecipeBuilder requires(Ingredient ingredient, int quantity)
	{
		for(int i = 0; i < quantity; ++i)
		{
			this.ingredients.add(ingredient);
		}

		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
	{
		this.advancement.addCriterion(criterionName, criterionTrigger);
		return this;
	}

	@Override
	public RecipeBuilder group(@Nullable String groupName)
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
		save(finishedRecipeConsumer, RecipeBuilder.getDefaultRecipeId(this.result).withSuffix("_from_armor_plating"));
	}

	@Override
	public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId)
	{
		this.ensureValid(recipeId);
		this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		finishedRecipeConsumer.accept(new Result(recipeId, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceLocation id) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}

	public static class Result extends CraftingRecipeBuilder.CraftingResult
	{
		private final ResourceLocation id;
		private final Item result;
		private final int count;
		private final String group;
		private final List<Ingredient> ingredients;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation id, Item result, int count, String group, CraftingBookCategory category, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId)
		{
			super(category);
			this.id = id;
			this.result = result;
			this.count = count;
			this.group = group;
			this.ingredients = ingredients;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		public void serializeRecipeData(JsonObject json) {
			super.serializeRecipeData(json);
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			JsonArray jsonArray = new JsonArray();

			for(Ingredient ingredient : this.ingredients) {
				jsonArray.add(ingredient.toJson());
			}

			json.add("ingredients", jsonArray);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
			if (this.count > 1) {
				jsonObject.addProperty("count", this.count);
			}

			json.add("result", jsonObject);
		}

		@Override
		public ResourceLocation getId()
		{
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType()
		{
			return ArmorPlatingRecipeShapeless.Serializer.INSTANCE;
		}

		@Override
		public @Nullable JsonObject serializeAdvancement()
		{
			return advancement.serializeToJson();
		}

		@Override
		public @Nullable ResourceLocation getAdvancementId()
		{
			return this.advancementId;
		}
	}
}
