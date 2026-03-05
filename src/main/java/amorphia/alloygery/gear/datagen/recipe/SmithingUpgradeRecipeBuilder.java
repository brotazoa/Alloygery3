package amorphia.alloygery.gear.datagen.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.IDynamicGear;
import amorphia.alloygery.gear.material.AlloygeryDefaultMaterials;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.recipe.SmithingUpgradeRecipe;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SmithingUpgradeRecipeBuilder
{
	private final RecipeCategory category;
	private final Ingredient template;
	private final Ingredient base;
	private final Ingredient addition;
	private final AlloygeryMaterial upgradeMaterial;
	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

	public static SmithingUpgradeRecipeBuilder netheriteUpgrade()
	{
		return new SmithingUpgradeRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
				Ingredient.of(IDynamicGear.UPGRADEABLE_EQUIPMENT),
				Ingredient.of(Items.NETHERITE_INGOT),
				AlloygeryDefaultMaterials.NETHERITE
		);
	}

	public SmithingUpgradeRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, AlloygeryMaterial upgradeMaterial)
	{
		this.category = category;
		this.template = template;
		this.base = base;
		this.addition = addition;
		this.upgradeMaterial = upgradeMaterial;
	}

	public SmithingUpgradeRecipeBuilder unlocks(String key, CriterionTriggerInstance criterion)
	{
		this.advancement.addCriterion(key, criterion);
		return this;
	}

	public void save(Consumer<FinishedRecipe> exporter, ResourceLocation recipeId)
	{
		this.ensureValid(recipeId);
		this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		exporter.accept(new Result(recipeId, this.template, this.base, this.addition, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.upgradeMaterial));
	}

	private void ensureValid(ResourceLocation recipeId)
	{
		if(this.advancement.getCriteria().isEmpty())
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
	}

	public record Result(ResourceLocation id, Ingredient template, Ingredient base, Ingredient addition, Advancement.Builder advancement, ResourceLocation advancementId, AlloygeryMaterial upgradeMaterial) implements FinishedRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject json)
		{
			json.add("template", this.template.toJson());
			json.add("base", this.base.toJson());
			json.add("addition", this.addition.toJson());
			json.addProperty("upgrade_material", upgradeMaterial.getMaterialIdentifier().toString());
		}

		@Override
		public ResourceLocation getId()
		{
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType()
		{
			return SmithingUpgradeRecipe.Serializer.INSTANCE;
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
