package amorphia.alloygery.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LayerPattern
{
	public static final Codec<LayerPattern> CODEC = Codec.list(Layer.CODEC).xmap(LayerPattern::new, pattern -> pattern.layers);

	public final List<Layer> layers;

	public LayerPattern(List<Layer> layers)
	{
		this.layers = layers;
	}

	public Layer rollNext(@Nullable Layer previous, RandomSource random)
	{
		int totalWeight = 0;
		for(Layer layer : layers)
		{
			if (layer != previous)
			{
				totalWeight += layer.weight;
			}
		}

		int rolled = random.nextInt(totalWeight);
		for(Layer layer : layers)
		{
			if (layer == previous)
				continue;

			rolled -= layer.weight;
			if(rolled < 0)
				return layer;
		}

		return null;
	}

	public static OverworldBuilder overworldBuilder()
	{
		return new OverworldBuilder();
	}

	public static OverworldSurfaceBuilder overworldSurfaceBuilder()
	{
		return new OverworldSurfaceBuilder();
	}

	public static NetherBuilder netherBuilder()
	{
		return new NetherBuilder();
	}

	public static EndBuilder endBuilder()
	{
		return new EndBuilder();
	}

	public static abstract class Builder
	{
		protected final List<Layer> layers = new ArrayList<>();

		public LayerPattern build()
		{
			return new LayerPattern(layers);
		}
	}

	public static class OverworldBuilder extends Builder
	{
		public OverworldBuilder layer(Consumer<Layer.OverworldBuilder> builder)
		{
			Layer.OverworldBuilder layerBuilder = new Layer.OverworldBuilder();
			builder.accept(layerBuilder);
			layers.add(layerBuilder.build());
			return this;
		}
	}

	public static class OverworldSurfaceBuilder extends Builder
	{
		public OverworldSurfaceBuilder layer(Consumer<Layer.OverworldSurfaceBuilder> builder)
		{
			Layer.OverworldSurfaceBuilder layerBuilder = new Layer.OverworldSurfaceBuilder();
			builder.accept(layerBuilder);
			layers.add(layerBuilder.build());
			return this;
		}
	}

	public static class NetherBuilder extends Builder
	{
		public NetherBuilder layer(Consumer<Layer.NetherBuilder> builder)
		{
			Layer.NetherBuilder layerBuilder = new Layer.NetherBuilder();
			builder.accept(layerBuilder);
			layers.add(layerBuilder.build());
			return this;
		}
	}

	public static class EndBuilder extends Builder
	{
		public EndBuilder layer(Consumer<Layer.EndBuilder> builder)
		{
			Layer.EndBuilder layerBuilder = new Layer.EndBuilder();
			builder.accept(layerBuilder);
			layers.add(layerBuilder.build());
			return this;
		}
	}
}
