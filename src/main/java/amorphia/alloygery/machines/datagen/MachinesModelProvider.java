package amorphia.alloygery.machines.datagen;

import amorphia.alloygery.datagen.AlloygeryModelProvider;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MachinesModelProvider implements AlloygeryModelProvider.IAlloygeryModelProvider
{
    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators)
    {
        for(Block block : MachinesModule.BLOCKS.values())
        {
            if (block instanceof MachineBlockModelDataGenerator blockWithDataGen)
            {
                blockWithDataGen.generateBlockModel(blockModelGenerators);
            }
            else blockModelGenerators.createTrivialCube(block);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators)
    {
        for(Item item : MachinesModule.ITEMS.values())
        {
            if (item instanceof MachineItemModelDataGenerator itemWithDataGen)
            {
                itemWithDataGen.generateItemModel(itemModelGenerators);
            }
            else itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
    }

    public interface MachineBlockModelDataGenerator
    {
        void generateBlockModel(BlockModelGenerators blockModelGenerators);
    }

    public interface MachineItemModelDataGenerator
    {
        void generateItemModel(ItemModelGenerators itemModelGenerators);
    }
}
