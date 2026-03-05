package amorphia.alloygery.machines.datagen;

import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;
import amorphia.alloygery.machines.MachinesModule;

public class MachinesBlockLootTableProvider implements AlloygeryBlockLootTableProvider.IAlloygeryBlockLootTableProvider
{
    @Override
    public void generate(AlloygeryBlockLootTableProvider builder)
    {
        MachinesModule.BLOCKS.values().forEach(block -> {
            if (block instanceof MachineBlockLootTableDataGenerator blockWithDataGen)
            {
                blockWithDataGen.generateBlockLootTable(builder);
            }
            else builder.dropSelf(block);
        });
    }

    public interface MachineBlockLootTableDataGenerator
    {
        void generateBlockLootTable(AlloygeryBlockLootTableProvider builder);
    }
}
