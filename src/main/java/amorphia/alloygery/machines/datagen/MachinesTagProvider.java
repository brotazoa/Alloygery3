package amorphia.alloygery.machines.datagen;

import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MachinesTagProvider implements AlloygeryBlockTagProvider.IAlloygeryBlockTagProvider, AlloygeryItemTagProvider.IAlloygeryItemTagProvider
{
	public static final MachinesTagProvider INSTANCE = new MachinesTagProvider();

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		for(Block block : MachinesModule.BLOCKS.values())
		{
			if (block instanceof IBlockTagGen blockWithTagGen)
			{
				blockWithTagGen.addBlockTags(provider, lookup);
			}
		}
	}

	@Override
	public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
	{
		for(Item item : MachinesModule.ITEMS.values())
		{
			if (item instanceof IItemTagGen itemWithTagGen)
			{
				itemWithTagGen.addItemTags(provider, lookup);
			}
		}
	}

	public interface IBlockTagGen
	{
		void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup);
	}

	public interface IItemTagGen
	{
		void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup);
	}
}
