package amorphia.alloygery.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class LayeredOreConfiguration implements FeatureConfiguration
{
	public static final Codec<LayeredOreConfiguration> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.list(LayerPattern.CODEC)
						.fieldOf("layer_patterns")
						.forGetter(config -> config.layerPatterns),
				Codec.intRange(0, 64)
						.fieldOf("size")
						.forGetter(config -> config.size),
				Codec.floatRange(0.0f, 1.0f)
						.fieldOf("discard_on_air_exposure_chance")
						.forGetter(config -> config.discardOnAirChance)
		).apply(instance, LayeredOreConfiguration::new);
	});

	public final List<LayerPattern> layerPatterns;
	public final int size;
	public final float discardOnAirChance;

	public LayeredOreConfiguration(List<LayerPattern> layerPatterns, int size, float discardOnAirChance)
	{
		this.layerPatterns = layerPatterns;
		this.size = size;
		this.discardOnAirChance = discardOnAirChance;
	}
}
