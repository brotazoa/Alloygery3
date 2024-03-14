package amorphia.alloygery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

import java.util.ArrayList;
import java.util.List;

public class AlloygeryModelProvider extends FabricModelProvider
{
	private static final List<IAlloygeryModelProvider> providers = new ArrayList<>();

	public static void addProvider(IAlloygeryModelProvider provider)
	{
		providers.add(provider);
	}

	public AlloygeryModelProvider(FabricDataOutput output)
	{
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator)
	{
		for(IAlloygeryModelProvider provider : providers)
		{
			provider.generateBlockStateModels(blockStateModelGenerator);
		}
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator)
	{
		for(IAlloygeryModelProvider provider : providers)
		{
			provider.generateItemModels(itemModelGenerator);
		}
	}

	public interface IAlloygeryModelProvider
	{
		void generateBlockStateModels(BlockModelGenerators blockModelGenerators);

		void generateItemModels(ItemModelGenerators itemModelGenerators);
	}
}
