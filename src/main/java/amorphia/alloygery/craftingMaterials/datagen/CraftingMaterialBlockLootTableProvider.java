package amorphia.alloygery.craftingMaterials.datagen;

import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;

public class CraftingMaterialBlockLootTableProvider implements AlloygeryBlockLootTableProvider.IAlloygeryBlockLootTableProvider
{
    @Override
    public void generate(AlloygeryBlockLootTableProvider builder)
    {
        CraftingMaterialModule.BLOCKS.values().forEach(builder::dropSelf);
    }
}
