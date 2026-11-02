package amorphia.alloygery.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.loot.*;
import amorphia.alloygery.gear.GearModule;
import io.github.fabricators_of_create.porting_lib.loot.GlobalLootModifierProvider;
import io.github.fabricators_of_create.porting_lib.loot.LootTableIdCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
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

	public static class ChestLootProvider extends SimpleFabricLootTableProvider
	{
		public ChestLootProvider(FabricDataOutput output)
		{
			super(output, LootContextParamSets.CHEST);
		}

		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			consumer.accept(Alloygery.asResource("chests/igloo_loot"), LootTable.lootTable()
					.withPool(LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1.0f))
							.setBonusRolls(UniformGenerator.between(1.0f, 2.0f))
							.add(EmptyLootItem.emptyItem().setWeight(20))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("base_wool_helmet")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("base_wool_chestplate")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("base_wool_leggings")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("base_wool_boots")))
					)
			);

			consumer.accept(Alloygery.asResource("chests/end_city_loot"), LootTable.lootTable()
					.withPool(LootPool.lootPool()
							.setRolls(UniformGenerator.between(2.0f, 4.0f))
							.setBonusRolls(UniformGenerator.between(2.0f, 4.0f))
							.add(EmptyLootItem.emptyItem().setWeight(20))
							.add(LootItem.lootTableItem(CraftingMaterialModule.ITEMS.get("antanium_ingot")))
							.add(LootItem.lootTableItem(CraftingMaterialModule.ITEMS.get("steel_ingot")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_axe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_axe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_axe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_hoe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_hoe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_hoe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_pickaxe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_pickaxe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_pickaxe_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_shovel_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_shovel_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_shovel_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_sword_blade")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_sword_blade")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_sword_blade")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_hammer_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_hammer_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_hammer_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_excavator_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_excavator_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_excavator_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_knife_blade")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_knife_blade")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_knife_blade")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("amethyst_hatchet_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("emerald_hatchet_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("diamond_hatchet_head")).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
					)
			);
		}
	}

	public static class EntityLootProvider extends SimpleFabricLootTableProvider
	{
		public EntityLootProvider(FabricDataOutput output)
		{
			super(output, LootContextParamSets.ENTITY);
		}

		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{

		}
	}

	public static class LootModifiers extends GlobalLootModifierProvider
	{
		public LootModifiers(FabricDataOutput output)
		{
			super(output, Alloygery.MODID);
		}

		@Override
		public void start()
		{
			this.add("add_igloo_loot", new AddLootTableLootModifier(
					new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("chests/igloo")).build() },
					Alloygery.asResource("chests/igloo_loot")
			));

			this.add("add_end_city_loot", new AddLootTableLootModifier(
					new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("chests/end_city")).build() },
					Alloygery.asResource("chests/end_city_loot")
			));
		}
	}
}
