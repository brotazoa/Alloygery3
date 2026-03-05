package amorphia.alloygery.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class AlloygeryChestLootTableProvider extends SimpleFabricLootTableProvider
{
	private static final List<IAlloygeryChestLootTableProvider> providers = new ArrayList<>();

	public static void addProvider(IAlloygeryChestLootTableProvider provider)
	{
		providers.add(provider);
	}

	public AlloygeryChestLootTableProvider(FabricDataOutput output)
	{
		super(output, LootContextParamSets.CHEST);
	}

	@Override
	public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> output)
	{
		providers.forEach(provider -> provider.generate(output));
	}

	public interface IAlloygeryChestLootTableProvider
	{
		void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer);
	}
}
