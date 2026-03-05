package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class WorldGenBlockLootTableProvider implements AlloygeryBlockLootTableProvider.IAlloygeryBlockLootTableProvider
{
    @Override
    public void generate(AlloygeryBlockLootTableProvider builder)
    {
        makeOreLootTable(WorldGenModule.TEALLITE, CraftingMaterialModule.ITEMS.get("raw_tin_nugget"), builder);
        makeOreLootTable(WorldGenModule.CUPROLINE, CraftingMaterialModule.ITEMS.get("raw_copper_nugget"), builder);
        makeOreLootTable(WorldGenModule.FERONYTE, CraftingMaterialModule.ITEMS.get("raw_iron_nugget"), builder);
        makeOreLootTable(WorldGenModule.AURORUM, CraftingMaterialModule.ITEMS.get("raw_gold_nugget"), builder);
        makeOreLootTable(WorldGenModule.NICKELINE, CraftingMaterialModule.ITEMS.get("raw_nickel_nugget"), builder);
        makeOreLootTable(WorldGenModule.TITANITE, CraftingMaterialModule.ITEMS.get("raw_titanium_nugget"), builder);
		makeOreLootTable(WorldGenModule.TIN_ORE, CraftingMaterialModule.ITEMS.get("raw_tin"), builder);
		makeOreLootTable(WorldGenModule.DEEPSLATE_TIN_ORE, CraftingMaterialModule.ITEMS.get("raw_tin"), builder);
    }

    private void makeOreLootTable(Block oreBlock, Item oreItem, AlloygeryBlockLootTableProvider builder)
    {
        if(oreBlock == null || oreItem == null)
            Alloygery.LOGGER.error("Null values");

        builder.add(oreBlock, BlockLootSubProvider.createSilkTouchDispatchTable(
                oreBlock,
                builder.applyExplosionDecay(
                        oreBlock,
                        LootItem.lootTableItem(oreItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                )
        ));
    }
}
