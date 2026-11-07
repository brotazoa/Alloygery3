package amorphia.alloygery.gear.datagen.convert;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.loot.ReplaceItemLootModifier;
import amorphia.alloygery.gear.GearModule;
import io.github.fabricators_of_create.porting_lib.loot.GlobalLootModifierProvider;
import io.github.fabricators_of_create.porting_lib.loot.LootTableIdCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ConvertLootProvider extends GlobalLootModifierProvider
{
	public ConvertLootProvider(PackOutput output)
	{
		super(output, Alloygery.MODID);
	}

	@Override
	protected void start()
	{
		this.add("bonus_chest_replace_wooden_axe", new ReplaceItemLootModifier(
				new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("chests/spawn_bonus_chest")).build() },
				Items.WOODEN_AXE,
				GearModule.ITEMS.get("flint_hatchet"),
				1
		));

		this.add("bonus_chest_replace_wooden_pickaxe", new ReplaceItemLootModifier(
				new LootItemCondition[] { LootTableIdCondition.builder(Alloygery.asVanillaResource("chests/spawn_bonus_chest")).build() },
				Items.WOODEN_PICKAXE,
				GearModule.ITEMS.get("flint_pickaxe"),
				1
		));
	}
}
