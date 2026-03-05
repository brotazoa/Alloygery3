package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ConfiguredFeatures
{
	public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE = key("tin_ore");

	public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_ORE_VEINS = key("nether_ore_veins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_ORE_VEINS = key("end_ore_veins");

	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SHALLOW_ORE_VEINS = key("overworld_shallow_ore_veins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_DEEP_ORE_VEINS = key("overworld_deep_ore_veins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RICH_ORE_VEINS = key("overworld_rich_ore_veins");

	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWOLD_SWAMP_IRON_ORE_VEINS = key("overworld_swamp_iron_ore_veins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWOLD_MESSA_GOLD_ORE_VEINS = key("overworld_messa_gold_ore_veins");

	private static ResourceKey<ConfiguredFeature<?, ?>> key(String name)
	{
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Alloygery.asResource(name));
	}

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> cntx)
	{
		RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

		List<OreConfiguration.TargetBlockState> tinTargetStates = List.of(
				OreConfiguration.target(stoneReplaceable, WorldGenModule.TIN_ORE.defaultBlockState()),
				OreConfiguration.target(deepslateReplaceable, WorldGenModule.DEEPSLATE_TIN_ORE.defaultBlockState())
		);

		FeatureUtils.register(cntx, TIN_ORE, Feature.ORE, new OreConfiguration(tinTargetStates, 8));

		List<LayerPattern> shallow = List.of(
				LayerPatterns.TEALLITE_SHALLOW.get(),
				LayerPatterns.CUPROLINE_SHALLOW.get(),
				LayerPatterns.FERONYTE_DEEP.get()
		);
		FeatureUtils.register(cntx, OVERWORLD_SHALLOW_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(shallow, 32, 0));

		List<LayerPattern> deep = List.of(
			//LayerPatterns.TEALLITE_DEEP.get(),
			//LayerPatterns.CUPROLINE_DEEP.get(),
			LayerPatterns.FERONYTE_DEEP.get(),
			LayerPatterns.AURORUM_DEEP.get()
		);
		FeatureUtils.register(cntx, OVERWORLD_DEEP_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(deep, 24, 0));

		List<LayerPattern> rich = List.of(
				LayerPatterns.CUPROLINE_RICH.get(),
				LayerPatterns.FERONYTE_RICH.get(),
				LayerPatterns.AURORUM_RICH.get()
		);
		FeatureUtils.register(cntx, OVERWORLD_RICH_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(rich, 16, 0));

		List<LayerPattern> swamp = List.of(
				LayerPatterns.FERONYTE_DEEP.get(),
				LayerPatterns.FERONYTE_RICH.get()
		);
		FeatureUtils.register(cntx, OVERWOLD_SWAMP_IRON_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(swamp, 24, 0));

		List<LayerPattern> messa = List.of(
				LayerPatterns.AURORUM_DEEP.get(),
				LayerPatterns.AURORUM_RICH.get()
		);
		FeatureUtils.register(cntx, OVERWOLD_MESSA_GOLD_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(messa, 24, 0));

		List<LayerPattern> netherLayerPatterns = List.of(
				LayerPatterns.NICKELINE.get()
		);
		FeatureUtils.register(cntx, NETHER_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(netherLayerPatterns, 32, 0));

		List<LayerPattern> endLayerPatterns = List.of(
				LayerPatterns.TITANITE.get()
		);
		FeatureUtils.register(cntx, END_ORE_VEINS, Features.LAYERED_ORE, new LayeredOreConfiguration(endLayerPatterns, 32, 0));
	}
}
