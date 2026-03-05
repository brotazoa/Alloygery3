package amorphia.alloygery.advancement;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.machines.MachinesModule;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModifyVanillaAdvancementsProviders
{
	public static class PackMetadataProvider extends PackMetadataGenerator
	{
		public PackMetadataProvider(PackOutput output)
		{
			super(output);
			Component description = Component.translatable("pack.alloygery.advancements.description");
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
			Advancement storyRoot = Advancement.Builder.advancement()
					.display(
							Items.GRASS_BLOCK,
							Component.translatable("advancements.story.root.title"),
							Component.translatable("advancements.story.root.description"),
							Alloygery.asVanillaResource("textures/gui/advancements/backgrounds/stone.png"),
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("first_spawn", PlayerTrigger.TriggerInstance.tick())
					.build(Alloygery.asVanillaResource("story/root"));

			Advancement flintAge = Advancement.Builder.advancement().parent(storyRoot)
					.display(
							GearModule.ITEMS.get("flint_hatchet"),
							Component.translatable("advancements.alloygery.flint_age.title"),
							Component.translatable("advancements.alloygery.flint_age.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_flint_hatchet", InventoryChangeTrigger.TriggerInstance.hasItems(GearModule.ITEMS.get("flint_hatchet")))
					.build(Alloygery.asResource("story/flint_age"));

			Advancement crafting = Advancement.Builder.advancement().parent(flintAge)
					.display(
							Items.CRAFTING_TABLE,
							Component.translatable("advancements.alloygery.crafting.title"),
							Component.translatable("advancements.alloygery.crafting.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("place_crafting_table", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.CRAFTING_TABLE))
					.build(Alloygery.asResource("story/crafting"));

			Advancement storyMineStone = Advancement.Builder.advancement().parent(crafting)
					.display(
							GearModule.ITEMS.get("flint_pickaxe"),
							Component.translatable("advancements.story.mine_stone.title"),
							Component.translatable("advancements.alloygery.mine_stone.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_stone", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(ItemTags.STONE_TOOL_MATERIALS).build()
					))
					.build(Alloygery.asVanillaResource("story/mine_stone"));

			Advancement storyUpgradeTools = Advancement.Builder.advancement().parent(storyMineStone)
					.display(
							GearModule.ITEMS.get("stone_pickaxe"),
							Component.translatable("advancements.story.upgrade_tools.title"),
							Component.translatable("advancements.story.upgrade_tools.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("has_stone_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(
									Items.STONE_PICKAXE,
									GearModule.ITEMS.get("stone_pickaxe")
							).build()
					))
					.build(Alloygery.asVanillaResource("story/upgrade_tools"));

			Advancement alloying = Advancement.Builder.advancement().parent(storyUpgradeTools)
					.display(
							MachinesModule.ALLOY_KILN,
							Component.translatable("advancements.alloygery.alloying.title"),
							Component.translatable("advancements.alloygery.alloying.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("place_alloy_kiln", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MachinesModule.ALLOY_KILN))
					.build(Alloygery.asResource("story/alloying"));

			Advancement bronzeAge = Advancement.Builder.advancement().parent(alloying)
					.display(
							CraftingMaterialModule.ITEMS.get("bronze_ingot"),
							Component.translatable("advancements.alloygery.bronze_age.title"),
							Component.translatable("advancements.alloygery.bronze_age.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_bronze_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CraftingMaterialModule.ITEMS.get("bronze_ingot")))
					.build(Alloygery.asResource("story/bronze_age"));

			Advancement bronzeTools = Advancement.Builder.advancement().parent(bronzeAge)
					.display(
							GearModule.ITEMS.get("bronze_pickaxe"),
							Component.translatable("advancements.alloygery.bronze_tools.title"),
							Component.translatable("advancements.alloygery.bronze_tools.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("has_bronze_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(
									GearModule.ITEMS.get("bronze_pickaxe")
							).build()
					))
					.build(Alloygery.asResource("story/bronze_tools"));

			Advancement storySmeltIron = Advancement.Builder.advancement().parent(bronzeTools)
					.display(
							Items.IRON_INGOT,
							Component.translatable("advancements.story.smelt_iron.title"),
							Component.translatable("advancements.story.smelt_iron.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
					.build(Alloygery.asVanillaResource("story/smelt_iron"));

			Advancement smithingAnvil = Advancement.Builder.advancement().parent(storySmeltIron)
					.display(
							MachinesModule.SMITHING_ANVIL,
							Component.translatable("advancements.alloygery.smithing_anvil.title"),
							Component.translatable("advancements.alloygery.smithing_anvil.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("place_smithing_anvil", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MachinesModule.SMITHING_ANVIL))
					.build(Alloygery.asResource("story/smithing_anvil"));

			Advancement storyIronTools = Advancement.Builder.advancement().parent(smithingAnvil)
					.display(
							Items.IRON_PICKAXE,
							Component.translatable("advancements.story.iron_tools.title"),
							Component.translatable("advancements.alloygery.iron_tools.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("has_iron_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(
									Items.IRON_PICKAXE,
									GearModule.ITEMS.get("iron_pickaxe")
							).build()
					))
					.build(Alloygery.asVanillaResource("story/iron_tools"));

			Advancement storyLavaBucket = Advancement.Builder.advancement().parent(storySmeltIron)
					.display(
							Items.LAVA_BUCKET,
							Component.translatable("advancements.story.lava_bucket.title"),
							Component.translatable("advancements.story.lava_bucket.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("has_lava_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LAVA_BUCKET))
					.build(Alloygery.asVanillaResource("story/lava_bucket"));

			Advancement storyObtainArmor = Advancement.Builder.advancement().parent(crafting)
					.display(
							Items.LEATHER_CHESTPLATE,
							Component.translatable("advancements.story.obtain_armor.title"),
							Component.translatable("advancements.alloygery.obtain_armor.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(
									TagKey.create(Registries.ITEM, Alloygery.asResource("base_armor_items"))
							).build()
					))
					.build(Alloygery.asVanillaResource("story/obtain_armor"));

			Advancement tailoring = Advancement.Builder.advancement().parent(storyObtainArmor)
					.display(
							MachinesModule.TAILORING_TABLE,
							Component.translatable("advancements.alloygery.tailoring.title"),
							Component.translatable("advancements.alloygery.tailoring.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("place_tailoring_table", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MachinesModule.TAILORING_TABLE))
					.build(Alloygery.asResource("story/tailoring"));

			Advancement plateUp = Advancement.Builder.advancement().parent(tailoring)
					.display(
							GearModule.ITEMS.get("iron_chestplate"),
							Component.translatable("advancements.alloygery.plate_up.title"),
							Component.translatable("advancements.alloygery.plate_up.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("plate_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(
									TagKey.create(Registries.ITEM, Alloygery.asResource("plated_armor_items"))
							).build()
					))
					.build(Alloygery.asResource("story/plate_up"));

			Advancement storyDiamonds = Advancement.Builder.advancement().parent(storyIronTools)
					.display(
							Items.DIAMOND,
							Component.translatable("advancements.story.mine_diamond.title"),
							Component.translatable("advancements.story.mine_diamond.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
					.build(Alloygery.asVanillaResource("story/mine_diamond"));

			Advancement storyShinyGear = Advancement.Builder.advancement().parent(storyDiamonds)
					.display(
							Items.DIAMOND_CHESTPLATE,
							Component.translatable("advancements.story.shiny_gear.title"),
							Component.translatable("advancements.alloygery.shiny_gear.description"),
							null,
							FrameType.CHALLENGE,
							true,
							true,
							false
					)
					.addCriterion("has_diamond_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(Items.DIAMOND_HELMET, GearModule.ITEMS.get("diamond_helmet")).build(),
							ItemPredicate.Builder.item().of(Items.DIAMOND_CHESTPLATE, GearModule.ITEMS.get("diamond_chestplate")).build(),
							ItemPredicate.Builder.item().of(Items.DIAMOND_LEGGINGS, GearModule.ITEMS.get("diamond_leggings")).build(),
							ItemPredicate.Builder.item().of(Items.DIAMOND_BOOTS, GearModule.ITEMS.get("diamond_boots")).build()
					))
					.build(Alloygery.asVanillaResource("story/shiny_gear"));

			// story
			consumer.accept(storyRoot);
			consumer.accept(flintAge);
			consumer.accept(crafting);
			consumer.accept(storyMineStone);
			consumer.accept(storyUpgradeTools);
			consumer.accept(alloying);
			consumer.accept(bronzeAge);
			consumer.accept(bronzeTools);
			consumer.accept(storySmeltIron);
			consumer.accept(smithingAnvil);
			consumer.accept(storyIronTools);
			consumer.accept(storyLavaBucket);
			consumer.accept(storyObtainArmor);
			consumer.accept(tailoring);
			consumer.accept(plateUp);
			consumer.accept(storyDiamonds);
			consumer.accept(storyShinyGear);

			Advancement netherRoot = Advancement.Builder.advancement()
					.display(
							Items.NETHER_BRICKS,
							Component.translatable("advancements.nether.root.title"),
							Component.translatable("advancements.nether.root.description"),
							Alloygery.asVanillaResource("textures/gui/advancements/backgrounds/nether.png"),
							FrameType.TASK,
							false,
							false,
							false
					)
					.addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ResourceKey.create(Registries.DIMENSION, Alloygery.asVanillaResource("the_nether"))))
					.build(Alloygery.asVanillaResource("nether/root"));

			Advancement netherObtainAncientDebris = Advancement.Builder.advancement().parent(netherRoot)
					.display(
							Items.ANCIENT_DEBRIS,
							Component.translatable("advancements.nether.obtain_ancient_debris.title"),
							Component.translatable("advancements.nether.obtain_ancient_debris.description"),
							null,
							FrameType.TASK,
							true,
							true, false
					)
					.addCriterion("ancient_debris", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ANCIENT_DEBRIS))
					.build(Alloygery.asVanillaResource("nether/obtain_ancient_debris"));

			Advancement netherNetheriteArmor = Advancement.Builder.advancement().parent(netherObtainAncientDebris)
					.display(
							Items.NETHERITE_CHESTPLATE,
							Component.translatable("advancements.nether.netherite_armor.title"),
							Component.translatable("advancements.nether.netherite_armor.description"),
							null,
							FrameType.CHALLENGE,
							true,
							true,
							false
					)
					.addCriterion("netherite_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(Items.NETHERITE_HELMET, GearModule.ITEMS.get("netherite_helmet")).build(),
							ItemPredicate.Builder.item().of(Items.NETHERITE_CHESTPLATE, GearModule.ITEMS.get("netherite_chestplate")).build(),
							ItemPredicate.Builder.item().of(Items.NETHERITE_LEGGINGS, GearModule.ITEMS.get("netherite_leggings")).build(),
							ItemPredicate.Builder.item().of(Items.NETHERITE_BOOTS, GearModule.ITEMS.get("netherite_boots")).build()
					))
					.build(Alloygery.asVanillaResource("nether/netherite_armor"));

			Advancement heatedExchange = Advancement.Builder.advancement().parent(netherRoot)
					.display(
							MachinesModule.HEAT_EXCHANGER,
							Component.translatable("advancements.alloygery.heated_exchange.title"),
							Component.translatable("advancements.alloygery.heated_exchange.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("place_heat_exchanger", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MachinesModule.HEAT_EXCHANGER))
					.build(Alloygery.asResource("nether/heated_exchange"));

			Advancement cookingWithConduction = Advancement.Builder.advancement().parent(heatedExchange)
					.display(
							MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER,
							Component.translatable("advancements.alloygery.cooking_with_conduction.title"),
							Component.translatable("advancements.alloygery.cooking_with_conduction.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("convert_alloy_kiln", BlockConvertedTrigger.Conditions.blockConvert(
							BlockPredicate.Builder.block().of(MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER)
					))
					.build(Alloygery.asResource("nether/cooking_with_conduction"));

			Advancement steelAge = Advancement.Builder.advancement().parent(cookingWithConduction)
					.display(
							CraftingMaterialModule.ITEMS.get("steel_ingot"),
							Component.translatable("advancements.alloygery.steel_age.title"),
							Component.translatable("advancements.alloygery.steel_age.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_steel_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CraftingMaterialModule.ITEMS.get("steel_ingot")))
					.build(Alloygery.asResource("nether/steel_age"));

			// nether
			consumer.accept(netherRoot);
			consumer.accept(netherObtainAncientDebris);
			consumer.accept(netherNetheriteArmor);
			consumer.accept(heatedExchange);
			consumer.accept(cookingWithConduction);
			consumer.accept(steelAge);

			Advancement endRoot = Advancement.Builder.advancement()
					.display(
							Items.END_STONE,
							Component.translatable("advancements.end.root.title"),
							Component.translatable("advancements.end.root.description"),
							Alloygery.asVanillaResource("textures/gui/advancements/backgrounds/end.png"),
							FrameType.TASK,
							false,
							false,
							false
					)
					.addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ResourceKey.create(Registries.DIMENSION, Alloygery.asVanillaResource("the_end"))))
					.build(Alloygery.asVanillaResource("end/root"));

			Advancement titaniumAge = Advancement.Builder.advancement().parent(endRoot)
					.display(
							CraftingMaterialModule.ITEMS.get("titanium_ingot"),
							Component.translatable("advancements.alloygery.titanium_age.title"),
							Component.translatable("advancements.alloygery.titanium_age.description"),
							null,
							FrameType.TASK,
							true,
							true,
							false
					)
					.addCriterion("has_titanium_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CraftingMaterialModule.ITEMS.get("titanium_ingot")))
					.build(Alloygery.asResource("end/titanium_age"));

			// end
			consumer.accept(endRoot);
			consumer.accept(titaniumAge);
		}
	}
}
