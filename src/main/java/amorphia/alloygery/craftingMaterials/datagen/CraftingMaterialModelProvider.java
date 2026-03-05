package amorphia.alloygery.craftingMaterials.datagen;

import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.AlloygeryModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CraftingMaterialModelProvider implements AlloygeryModelProvider.IAlloygeryModelProvider
{
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator)
	{
		for(Block block : CraftingMaterialModule.BLOCKS.values())
		{
			blockStateModelGenerator.createTrivialCube(block);
		}
	}

	public void generateItemModels(ItemModelGenerators itemModelGenerator)
	{
		for(Item item : CraftingMaterialModule.ITEMS.values())
		{
			if (item instanceof IItemModelGen itemWithModelGen)
			{
				itemWithModelGen.generateItemModel(itemModelGenerator);
			}
			else
			{
				itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
			}
		}
	}

	public interface IItemModelGen
	{
		void generateItemModel(ItemModelGenerators itemModelGenerator);
	}
}
