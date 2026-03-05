package amorphia.alloygery.gear.datagen.recipe;

import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.recipe.SmithingImprovementRecipe;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SmithingImprovementRecipeBuilder
{
	private final RecipeCategory category;
	private final Ingredient template;
	private final Ingredient base;
	private final Ingredient addition;
	private final AlloygeryMaterial improvementMaterial;
	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

	public static SmithingImprovementRecipeBuilder tipped(Ingredient addition, AlloygeryMaterial material)
	{
		SmithingImprovementRecipeBuilder builder = new SmithingImprovementRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(GearModule.ITEMS.get("tipped_template")),
				Ingredient.of(IDynamicTool.IMPROVABLE_TOOL),
				addition,
				material
		);

		builder.unlocks("has_template", RecipeProvider.has(GearModule.ITEMS.get("tipped_template")));
		return builder;
	}

	public static SmithingImprovementRecipeBuilder plated(Ingredient addition, AlloygeryMaterial material)
	{
		SmithingImprovementRecipeBuilder builder = new SmithingImprovementRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(GearModule.ITEMS.get("plated_template")),
				Ingredient.of(IDynamicTool.IMPROVABLE_TOOL),
				addition,
				material
		);

		builder.unlocks("has_template", RecipeProvider.has(GearModule.ITEMS.get("plated_template")));
		return builder;
	}

	public static SmithingImprovementRecipeBuilder wrapped(Ingredient addition, AlloygeryMaterial material)
	{
		SmithingImprovementRecipeBuilder builder = new SmithingImprovementRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(GearModule.ITEMS.get("wrapped_template")),
				Ingredient.of(IDynamicTool.IMPROVABLE_TOOL),
				addition,
				material
		);

		builder.unlocks("has_template", RecipeProvider.has(GearModule.ITEMS.get("wrapped_template")));
		return builder;
	}

	public static SmithingImprovementRecipeBuilder engraved(Ingredient addition, AlloygeryMaterial material)
	{
		SmithingImprovementRecipeBuilder builder = new SmithingImprovementRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(GearModule.ITEMS.get("engraved_template")),
				Ingredient.of(IDynamicArmor.IMPROVABLE_ARMOR),
				addition,
				material
		);

		builder.unlocks("has_template", RecipeProvider.has(GearModule.ITEMS.get("engraved_template")));
		return builder;
	}

	public static SmithingImprovementRecipeBuilder reinforced(Ingredient addition, AlloygeryMaterial material)
	{
		SmithingImprovementRecipeBuilder builder = new SmithingImprovementRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(GearModule.ITEMS.get("reinforced_template")),
				Ingredient.of(IDynamicArmor.IMPROVABLE_ARMOR),
				addition,
				material
		);

		builder.unlocks("has_template", RecipeProvider.has(GearModule.ITEMS.get("reinforced_template")));
		return builder;
	}

	public static SmithingImprovementRecipeBuilder padded(Ingredient addition, AlloygeryMaterial material)
	{
		SmithingImprovementRecipeBuilder builder = new SmithingImprovementRecipeBuilder(
				RecipeCategory.MISC,
				Ingredient.of(GearModule.ITEMS.get("padded_template")),
				Ingredient.of(IDynamicArmor.IMPROVABLE_ARMOR),
				addition,
				material
		);

		builder.unlocks("has_template", RecipeProvider.has(GearModule.ITEMS.get("padded_template")));
		return builder;
	}

	public SmithingImprovementRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, AlloygeryMaterial improvementMaterial)
	{
		this.category = category;
		this.template = template;
		this.base = base;
		this.addition = addition;
		this.improvementMaterial = improvementMaterial;
	}

	public SmithingImprovementRecipeBuilder unlocks(String key, CriterionTriggerInstance criterion)
	{
		this.advancement.addCriterion(key, criterion);
		return this;
	}

	public void save(Consumer<FinishedRecipe> exporter, ResourceLocation recipeId)
	{
		this.ensureValid(recipeId);
		this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(
				RequirementsStrategy.OR);
		exporter.accept(new Result(recipeId, this.template, this.base, this.addition, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.improvementMaterial));
	}

	private void ensureValid(ResourceLocation recipeId)
	{
		if(this.advancement.getCriteria().isEmpty())
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
	}

	public record Result(ResourceLocation id, Ingredient template, Ingredient base, Ingredient addition, Advancement.Builder advancement, ResourceLocation advancementId, AlloygeryMaterial improvementMaterial) implements
			FinishedRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject json)
		{
			json.add("template", this.template.toJson());
			json.add("base", this.base.toJson());
			json.add("addition", this.addition.toJson());
			json.addProperty("improvement_material", improvementMaterial.getMaterialIdentifier().toString());
		}

		@Override
		public ResourceLocation getId()
		{
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType()
		{
			return SmithingImprovementRecipe.Serializer.INSTANCE;
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
