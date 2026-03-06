package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class PlacedFeatures
{
	public static final ResourceKey<PlacedFeature> TIN_ORE = key("tin_ore");

	public static final ResourceKey<PlacedFeature> NETHER_ORE_VEINS = key("nether_ore_veins");
	public static final ResourceKey<PlacedFeature> END_ORE_VEINS = key("end_ore_veins");

	public static final ResourceKey<PlacedFeature> OVERWORLD_SURFACE_ORE_VEINS = key("overworld_surface_veins");
	public static final ResourceKey<PlacedFeature> OVERWORLD_SHALLOW_ORE_VEINS = key("overworld_shallow_veins");
	public static final ResourceKey<PlacedFeature> OVERWORLD_DEEP_ORE_VEINS = key("overworld_deep_veins");
	public static final ResourceKey<PlacedFeature> OVERWORLD_RICH_ORE_VEINS = key("overworld_rich_veins");

	public static final ResourceKey<PlacedFeature> OVERWORLD_SWAMP_IRON_ORE_VEINS = key("overworld_swamp_iron_ore_veins");
	public static final ResourceKey<PlacedFeature> OVERWORLD_MESSA_GOLD_ORE_VEINS = key("overworld_messa_gold_ore_veins");

	private static ResourceKey<PlacedFeature> key(String name)
	{
		return ResourceKey.create(Registries.PLACED_FEATURE, Alloygery.asResource(name));
	}

	public static void bootstrap(BootstapContext<PlacedFeature> cntx)
	{
		HolderGetter<ConfiguredFeature<?, ?>> featureLookup = cntx.lookup(Registries.CONFIGURED_FEATURE);

		Holder<ConfiguredFeature<?, ?>> tinOre = featureLookup.getOrThrow(ConfiguredFeatures.TIN_ORE);
		PlacementUtils.register(cntx, TIN_ORE, tinOre, placement(CountPlacement.of(8), -63, 70));

		Holder<ConfiguredFeature<?, ?>> netherVeins = featureLookup.getOrThrow(ConfiguredFeatures.NETHER_ORE_VEINS);
		Holder<ConfiguredFeature<?, ?>> endVeins = featureLookup.getOrThrow(ConfiguredFeatures.END_ORE_VEINS);

		PlacementUtils.register(cntx, NETHER_ORE_VEINS, netherVeins, placement(RarityFilter.onAverageOnceEvery(18), 40, 90));
		PlacementUtils.register(cntx, END_ORE_VEINS, endVeins, placement(RarityFilter.onAverageOnceEvery(25), -30, 70));

		Holder<ConfiguredFeature<?, ?>> overworldSurface = featureLookup.getOrThrow(ConfiguredFeatures.OVERWORLD_SURFACE_ORE_VEINS);
		Holder<ConfiguredFeature<?, ?>> overworldShallow = featureLookup.getOrThrow(ConfiguredFeatures.OVERWORLD_SHALLOW_ORE_VEINS);
		Holder<ConfiguredFeature<?, ?>> overworldDeep = featureLookup.getOrThrow(ConfiguredFeatures.OVERWORLD_DEEP_ORE_VEINS);
		Holder<ConfiguredFeature<?, ?>> overworldRich = featureLookup.getOrThrow(ConfiguredFeatures.OVERWORLD_RICH_ORE_VEINS);

		Holder<ConfiguredFeature<?, ?>> overworldSwamp = featureLookup.getOrThrow(ConfiguredFeatures.OVERWOLD_SWAMP_IRON_ORE_VEINS);
		Holder<ConfiguredFeature<?, ?>> overworldMessa = featureLookup.getOrThrow(ConfiguredFeatures.OVERWOLD_MESSA_GOLD_ORE_VEINS);

		PlacementUtils.register(cntx, OVERWORLD_SURFACE_ORE_VEINS, overworldSurface, List.of(
				RarityFilter.onAverageOnceEvery(11),
				HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
				InSquarePlacement.spread(),
				ConfigPlacementFilter.INSTANCE
				)
		);
		PlacementUtils.register(cntx, OVERWORLD_SHALLOW_ORE_VEINS, overworldShallow, placement(RarityFilter.onAverageOnceEvery(18), 20, 80));
		PlacementUtils.register(cntx, OVERWORLD_DEEP_ORE_VEINS, overworldDeep, placement(RarityFilter.onAverageOnceEvery(10), -60, 20));
		PlacementUtils.register(cntx, OVERWORLD_RICH_ORE_VEINS, overworldRich, placement(RarityFilter.onAverageOnceEvery(24), -60, 0));

		PlacementUtils.register(cntx, OVERWORLD_SWAMP_IRON_ORE_VEINS, overworldSwamp, placement(RarityFilter.onAverageOnceEvery(8), -40, 60));
		PlacementUtils.register(cntx, OVERWORLD_MESSA_GOLD_ORE_VEINS, overworldMessa, placement(RarityFilter.onAverageOnceEvery(8), -40, 70));

	}

	private static List<PlacementModifier> placement(PlacementModifier frequency, int minHeight, int maxHeight)
	{
		return List.of(
				frequency,
				InSquarePlacement.spread(),
				HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
				ConfigPlacementFilter.INSTANCE
		);
	}
}
