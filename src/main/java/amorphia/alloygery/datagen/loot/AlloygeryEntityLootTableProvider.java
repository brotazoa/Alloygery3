package amorphia.alloygery.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class AlloygeryEntityLootTableProvider extends SimpleFabricLootTableProvider
{
	private static final List<IAlloygeryEntityLootTableProvider> providers = new ArrayList<>();

	public static void addProvider(IAlloygeryEntityLootTableProvider provider)
	{
		providers.add(provider);
	}

	public AlloygeryEntityLootTableProvider(FabricDataOutput output)
	{
		super(output, LootContextParamSets.ENTITY);
	}

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output)
	{
		providers.forEach(provider -> provider.generate(output));
	}

	public interface IAlloygeryEntityLootTableProvider
	{
		void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer);
	}
}
