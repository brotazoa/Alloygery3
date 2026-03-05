package amorphia.alloygery.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.machines.MachinesModule;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class VanillaRecipeOverridesProviders
{
	public static class PackMetadataProvider extends PackMetadataGenerator
	{
		public PackMetadataProvider(PackOutput output)
		{
			super(output);
			Component description = Component.translatable("pack.alloygery.vanilla_recipe_overrides.description");
			add(PackMetadataSection.TYPE, new PackMetadataSection(description, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA)));
		}
	}

	public static class AdvancementsProvider extends FabricAdvancementProvider
	{
		public AdvancementsProvider(FabricDataOutput output)
		{
			super(output);
		}

		@Override
		public void generateAdvancement(Consumer<Advancement> consumer)
		{
			Advancement root = Advancement.Builder.recipeAdvancement()
					.display(
							MachinesModule.ALLOY_KILN,
							Component.translatable("advancements.alloygery.game_changes.title"),
							Component.translatable("advancements.alloygery.game_changes.description"),
							Alloygery.asResource("textures/gui/advancements/bricks.png"),
							FrameType.TASK,
							false,
							false,
							false
					)
					.addCriterion("alloygery_installed", PlayerTrigger.TriggerInstance.tick())
					.build(Alloygery.asResource("game_changes/root"));

			Advancement smithingRequiresSteel = Advancement.Builder.recipeAdvancement().parent(root)
					.display(
							Items.SMITHING_TABLE,
							Component.translatable("advancements.alloygery.smithing_requires_steel.title"),
							Component.translatable("advancements.alloygery.smithing_requires_steel.description"),
							null,
							FrameType.TASK,
							false,
							false,
							false
					)
					.addCriterion("alloygery_installed", new ImpossibleTrigger.TriggerInstance())
					.build(Alloygery.asResource("game_changes/smithing_requires_steel"));

			Advancement anvilRequiresSteel = Advancement.Builder.recipeAdvancement().parent(root)
					.display(
							Items.ANVIL,
							Component.translatable("advancements.alloygery.anvil_requires_steel.title"),
							Component.translatable("advancements.alloygery.anvil_requires_steel.description"),
							null,
							FrameType.TASK,
							false,
							false,
							false
					)
					.addCriterion("alloygery_installed", new ImpossibleTrigger.TriggerInstance())
					.build(Alloygery.asResource("game_changes/anvil_requires_steel"));

			consumer.accept(root);
			consumer.accept(smithingRequiresSteel);
			consumer.accept(anvilRequiresSteel);
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
			final TagKey<Item> steel = TagKey.create(Registries.ITEM, Alloygery.asCommonResource("steel_ingots"));
			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SMITHING_TABLE)
					.define('i', steel)
					.define('p', ItemTags.PLANKS)
					.pattern("ii")
					.pattern("pp")
					.pattern("pp")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter);

			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.ANVIL)
					.define('i', steel)
					.define('b', TagKey.create(Registries.ITEM, Alloygery.asCommonResource("steel_blocks")))
					.pattern("bbb")
					.pattern(" i ")
					.pattern("iii")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter);

			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.LODESTONE)
					.define('s', Items.CHISELED_STONE_BRICKS)
					.define('i', CraftingMaterialModule.ITEMS.get("nickel_ingot"))
					.pattern("sss")
					.pattern("sis")
					.pattern("sss")
					.unlockedBy("has_nickel", RecipeProvider.has(CraftingMaterialModule.ITEMS.get("nickel_ingot")))
					.save(exporter, Alloygery.asResource("lodestone"));

			ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.ACTIVATOR_RAIL, 12)
					.define('i', steel)
					.define('s', Items.STICK)
					.define('t', Items.REDSTONE_TORCH)
					.pattern("isi")
					.pattern("iti")
					.pattern("isi")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter, Alloygery.asResource("activator_rail"));

			ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.DETECTOR_RAIL, 12)
					.define('i', steel)
					.define('r', Items.REDSTONE)
					.define('p', Items.STONE_PRESSURE_PLATE)
					.pattern("i i")
					.pattern("ipi")
					.pattern("iri")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter, Alloygery.asResource("detector_rail"));

			ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.POWERED_RAIL, 12)
					.define('i', steel)
					.define('s', Items.STICK)
					.define('r', Items.REDSTONE)
					.define('g', TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_ingots")))
					.pattern("i i")
					.pattern("gsg")
					.pattern("iri")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter, Alloygery.asResource("powered_rail"));

			ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.RAIL, 32)
					.define('i', steel)
					.define('s', Items.STICK)
					.pattern("i i")
					.pattern("isi")
					.pattern("i i")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter, Alloygery.asResource("rail"));

			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.HOPPER, 4)
					.define('c', TagKey.create(Registries.ITEM, Alloygery.asCommonResource("chests")))
					.define('i', steel)
					.pattern("i i")
					.pattern("ici")
					.pattern(" i ")
					.unlockedBy("has_steel", RecipeProvider.has(steel))
					.save(exporter, Alloygery.asResource("hopper"));
		}

		@Override
		protected ResourceLocation getRecipeIdentifier(ResourceLocation identifier)
		{
			return identifier;
		}
	}
}
