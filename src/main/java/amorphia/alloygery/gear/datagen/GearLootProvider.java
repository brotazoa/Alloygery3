package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.loot.*;
import amorphia.alloygery.gear.GearModule;
import io.github.fabricators_of_create.porting_lib.loot.LootTableIdCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class GearLootProvider
{
	public static class ChestLootProvider implements AlloygeryChestLootTableProvider.IAlloygeryChestLootTableProvider
	{
		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			consumer.accept(Alloygery.asResource("chests/alloygery_simple_dungeon_loot"), LootTable.lootTable()
					.withPool(LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1.0f))
							.setBonusRolls(ConstantValue.exactly(2.0f))
							.add(EmptyLootItem.emptyItem().setWeight(30))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("tipped_template")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("plated_template")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("wrapped_template")))
					)
			);

			consumer.accept(Alloygery.asResource("chests/alloygery_abandoned_mineshaft_loot"), LootTable.lootTable()
					.withPool(LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1.0f))
							.setBonusRolls(ConstantValue.exactly(2.0f))
							.add(EmptyLootItem.emptyItem().setWeight(20))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("tipped_template")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("plated_template")))
							.add(LootItem.lootTableItem(GearModule.ITEMS.get("wrapped_template")))
					)
			);
		}
	}

	public static class EntityLootProvider implements AlloygeryEntityLootTableProvider.IAlloygeryEntityLootTableProvider
	{
		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{

		}
	}

	public static class LootModifiers implements AlloygeryGlobalLootModifierDataProvider.IGlobalLootModifierDataProvider
	{
		@Override
		public void generate(AlloygeryGlobalLootModifierDataProvider provider)
		{
			provider.add("add_abandoned_mineshaft_loot", new AddLootTableLootModifier(
					new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("chests/abandoned_mineshaft")).build() },
					Alloygery.asResource("chests/alloygery_abandoned_mineshaft_loot")
			));

			provider.add("add_simple_dungeon_loot", new AddLootTableLootModifier(
					new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("chests/simple_dungeon")).build() },
					Alloygery.asResource("chests/alloygery_simple_dungeon_loot")
			));

			provider.add("husk_drops_tin", new ReplaceItemLootModifier(
					new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("entity/husk")).build() },
					Items.IRON_INGOT,
					CraftingMaterialModule.ITEMS.get("tin_ingot"),
					1
			));
		}
	}
}
