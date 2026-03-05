package amorphia.alloygery.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Predicate;

public class BiomeModifiers
{
	public static void bootstrap()
	{
		Predicate<BiomeSelectionContext> isOverworld = BiomeSelectors.foundInOverworld();
		Predicate<BiomeSelectionContext> isNether = BiomeSelectors.foundInTheNether();
		Predicate<BiomeSelectionContext> isEnd = BiomeSelectors.foundInTheEnd();

		Predicate<BiomeSelectionContext> isSwampLike = BiomeSelectors.tag(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS);
		Predicate<BiomeSelectionContext> isMesaLike = BiomeSelectors.tag(BiomeTags.IS_BADLANDS);

		addOreVein(isOverworld, PlacedFeatures.TIN_ORE);

		addOreVein(isOverworld, PlacedFeatures.OVERWORLD_SHALLOW_ORE_VEINS);
		addOreVein(isOverworld, PlacedFeatures.OVERWORLD_DEEP_ORE_VEINS);
		addOreVein(isOverworld, PlacedFeatures.OVERWORLD_RICH_ORE_VEINS);
		addOreVein(isSwampLike, PlacedFeatures.OVERWORLD_SWAMP_IRON_ORE_VEINS);
		addOreVein(isMesaLike, PlacedFeatures.OVERWORLD_MESSA_GOLD_ORE_VEINS);
		addOreVein(isNether, PlacedFeatures.NETHER_ORE_VEINS);
		addOreVein(isEnd, PlacedFeatures.END_ORE_VEINS);
	}

	private static void addOreVein(Predicate<BiomeSelectionContext> biomeTest, ResourceKey<PlacedFeature> feature)
	{
		BiomeModifications.addFeature(biomeTest, GenerationStep.Decoration.UNDERGROUND_ORES, feature);
	}
}
