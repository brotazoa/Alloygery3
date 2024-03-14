package amorphia.alloygery.worldgen;

import amorphia.alloygery.datagen.AlloygeryModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.world.level.block.Block;

public class WorldGenModelProvider implements AlloygeryModelProvider.IAlloygeryModelProvider
{
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator)
	{
		for(Block block : WorldGenModule.BLOCKS.values())
		{
			blockStateModelGenerator.createTrivialCube(block);
		}
	}

	public void generateItemModels(ItemModelGenerators itemModelGenerator)
	{

	}
}
