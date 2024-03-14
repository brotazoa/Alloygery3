package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenDataProvider extends DatapackBuiltinEntriesProvider
{
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap);

	public WorldGenDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, BUILDER, Set.of(Alloygery.MODID));
	}

	public static DataProvider.Factory<WorldGenDataProvider> makeFactory(CompletableFuture<HolderLookup.Provider> registries)
	{
		return output -> new WorldGenDataProvider(output, registries);
	}

	@Override
	public @NotNull String getName()
	{
		return "Alloygery's Worldgen Data";
	}
}
