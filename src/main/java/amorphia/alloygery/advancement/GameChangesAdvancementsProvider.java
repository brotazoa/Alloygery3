package amorphia.alloygery.advancement;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.worldgen.WorldGenModule;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class GameChangesAdvancementsProvider extends FabricAdvancementProvider
{
	public GameChangesAdvancementsProvider(FabricDataOutput output)
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

		Advancement ironRequiresBronze = Advancement.Builder.recipeAdvancement().parent(root)
				.display(
						Items.IRON_ORE,
						Component.translatable("advancements.alloygery.game_changes.iron_requires_bronze.title"),
						Component.translatable("advancements.alloygery.game_changes.iron_requires_bronze.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				)
				.addCriterion("wrong_tool", BlockBrokenTrigger.Conditions.blockBroken(
						BlockPredicate.Builder.block().of(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, WorldGenModule.FERONYTE),
						false
				))
				.build(Alloygery.asResource("game_changes/iron_requires_bronze"));

		Advancement alloying = Advancement.Builder.advancement().parent(root)
				.display(
						MachinesModule.ALLOY_KILN,
						Component.translatable("advancements.alloygery.game_changes.alloying.title"),
						Component.translatable("advancements.alloygery.game_changes.alloying.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				)
				.addCriterion("place_alloy_kiln", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
						MachinesModule.ALLOY_KILN
				)).build(Alloygery.asResource("game_changes/alloying"));

		Advancement bronze = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("bronze_ingot"),
						Component.translatable("advancements.alloygery.game_changes.bronze.title"),
						Component.translatable("advancements.alloygery.game_changes.bronze.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_bronze", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("bronze_ingot")).build()
				)).build(Alloygery.asResource("game_changes/bronze"));

		Advancement antanium = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("antanium_ingot"),
						Component.translatable("advancements.alloygery.game_changes.antanium.title"),
						Component.translatable("advancements.alloygery.game_changes.antanium.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_antanium", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("antanium_ingot")).build()
				)).build(Alloygery.asResource("game_changes/antanium"));

		Advancement steel = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("steel_ingot"),
						Component.translatable("advancements.alloygery.game_changes.steel.title"),
						Component.translatable("advancements.alloygery.game_changes.steel.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_steel", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("steel_ingot")).build()
				)).build(Alloygery.asResource("game_changes/steel"));

		Advancement constantan = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("constantan_ingot"),
						Component.translatable("advancements.alloygery.game_changes.constantan.title"),
						Component.translatable("advancements.alloygery.game_changes.constantan.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_constantan", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("constantan_ingot")).build()
				)).build(Alloygery.asResource("game_changes/constantan"));

		Advancement invar = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("invar_ingot"),
						Component.translatable("advancements.alloygery.game_changes.invar.title"),
						Component.translatable("advancements.alloygery.game_changes.invar.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_invar", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("invar_ingot")).build()
				)).build(Alloygery.asResource("game_changes/invar"));

		Advancement titanium_gold = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("titanium_gold_ingot"),
						Component.translatable("advancements.alloygery.game_changes.titanium_gold.title"),
						Component.translatable("advancements.alloygery.game_changes.titanium_gold.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_titanium_gold", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("titanium_gold_ingot")).build()
				)).build(Alloygery.asResource("game_changes/titanium_gold"));

		Advancement nitinol = Advancement.Builder.advancement().parent(alloying)
				.display(
						CraftingMaterialModule.ITEMS.get("nitinol_ingot"),
						Component.translatable("advancements.alloygery.game_changes.nitinol.title"),
						Component.translatable("advancements.alloygery.game_changes.nitinol.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("has_nitinol", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(CraftingMaterialModule.ITEMS.get("nitinol_ingot")).build()
				)).build(Alloygery.asResource("game_changes/nitinol"));

		// game changes
		consumer.accept(root);
		consumer.accept(ironRequiresBronze);
		consumer.accept(alloying);
		consumer.accept(bronze);
		consumer.accept(steel);
		consumer.accept(constantan);
		consumer.accept(invar);
		consumer.accept(titanium_gold);
		consumer.accept(nitinol);
	}
}
