package amorphia.alloygery.datagen;

import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.datagen.recipe.BaseArmorRecipeShapedBuilder;
import amorphia.alloygery.gear.property.PrimitiveProperty;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class EasyStartRecipesProviders
{
	public static class PackMetadataProvider extends PackMetadataGenerator
	{
		public PackMetadataProvider(PackOutput output)
		{
			super(output);
			Component description = Component.translatable("pack.alloygery.easier_starting_recipes.description");
			add(PackMetadataSection.TYPE, new PackMetadataSection(description, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA)));
		}
	}

	public static class RecipeProvider extends FabricRecipeProvider
	{
		public RecipeProvider(FabricDataOutput output)
		{
			super(output);
		}

		@Override
		public void buildRecipes(Consumer<FinishedRecipe> exporter)
		{
			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STICK)
					.requires(ItemTags.SAPLINGS)
					.unlockedBy("has_sapling", RecipeProvider.has(ItemTags.SAPLINGS))
					.save(exporter, RecipeBuilder.getDefaultRecipeId(Items.STICK).withSuffix("_from_saplings"));

			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.FLINT)
					.requires(Items.GRAVEL)
					.unlockedBy("has_gravel", RecipeProvider.has(Items.GRAVEL))
					.save(exporter, RecipeBuilder.getDefaultRecipeId(Items.FLINT).withSuffix("_from_gravel"));

			// leather
			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_leather_helmet"))
					.define('l', Items.LEATHER)
					.pattern("lll")
					.pattern("l l")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_leather", RecipeProvider.has(Items.LEATHER))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_leather_chestplate"))
					.define('l', Items.LEATHER)
					.pattern("l l")
					.pattern("lll")
					.pattern("lll")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_leather", RecipeProvider.has(Items.LEATHER))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_leather_leggings"))
					.define('l', Items.LEATHER)
					.pattern("lll")
					.pattern("l l")
					.pattern("l l")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_leather", RecipeProvider.has(Items.LEATHER))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_leather_boots"))
					.define('l', Items.LEATHER)
					.pattern("l l")
					.pattern("l l")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_leather", RecipeProvider.has(Items.LEATHER))
					.save(exporter);

			//rabbit hide
			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_rabbit_hide_helmet"))
					.define('l', Items.RABBIT_HIDE)
					.pattern("lll")
					.pattern("l l")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_rabbit_hide", RecipeProvider.has(Items.RABBIT_HIDE))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_rabbit_hide_chestplate"))
					.define('l', Items.RABBIT_HIDE)
					.pattern("l l")
					.pattern("lll")
					.pattern("lll")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_rabbit_hide", RecipeProvider.has(Items.RABBIT_HIDE))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_rabbit_hide_leggings"))
					.define('l', Items.RABBIT_HIDE)
					.pattern("lll")
					.pattern("l l")
					.pattern("l l")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_rabbit_hide", RecipeProvider.has(Items.RABBIT_HIDE))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_rabbit_hide_boots"))
					.define('l', Items.RABBIT_HIDE)
					.pattern("l l")
					.pattern("l l")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_rabbit_hide", RecipeProvider.has(Items.RABBIT_HIDE))
					.save(exporter);

			// wool
			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_wool_helmet"))
					.define('w', Items.WHITE_WOOL)
					.pattern("www")
					.pattern("w w")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_wool", RecipeProvider.has(Items.WHITE_WOOL))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_wool_chestplate"))
					.define('w', Items.WHITE_WOOL)
					.pattern("w w")
					.pattern("www")
					.pattern("www")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_wool", RecipeProvider.has(Items.WHITE_WOOL))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_wool_leggings"))
					.define('w', Items.WHITE_WOOL)
					.pattern("www")
					.pattern("w w")
					.pattern("w w")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_wool", RecipeProvider.has(Items.WHITE_WOOL))
					.save(exporter);

			BaseArmorRecipeShapedBuilder.shaped(RecipeCategory.COMBAT, GearModule.ITEMS.get("base_wool_boots"))
					.define('w', Items.WHITE_WOOL)
					.pattern("w w")
					.pattern("w w")
					.property(PrimitiveProperty.of())
					.unlockedBy("has_wool", RecipeProvider.has(Items.WHITE_WOOL))
					.save(exporter);
		}
	}
}
