package amorphia.alloygery.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.ArrayList;
import java.util.List;

public class Layer
{
	public static final Codec<Layer> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.list(Codec.list(OreConfiguration.TargetBlockState.CODEC))
						.fieldOf("targets")
						.forGetter(layer -> layer.targets),
				Codec.intRange(0, Integer.MAX_VALUE)
						.fieldOf("min_size")
						.forGetter(layer -> layer.minSize),
				Codec.intRange(0, Integer.MAX_VALUE)
						.fieldOf("max_size")
						.forGetter(layer -> layer.maxSize),
				Codec.intRange(0, Integer.MAX_VALUE)
						.fieldOf("weight")
						.forGetter(layer -> layer.weight)
		).apply(instance, Layer::new);
	});

	public final List<List<OreConfiguration.TargetBlockState>> targets;
	public final int minSize;
	public final int maxSize;
	public final int weight;

	public Layer(List<List<OreConfiguration.TargetBlockState>> targets, int minSize, int maxSize, int weight)
	{
		this.targets = targets;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.weight = weight;
	}

	public List<OreConfiguration.TargetBlockState> rollBlock(RandomSource random)
	{
		return targets.size() == 1 ? targets.get(0) : targets.get(random.nextInt(targets.size()));
	}

	public static class Builder
	{
		protected static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		protected static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
		protected static final RuleTest NETHER_ORE_REPLACEABLES = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
		protected static final RuleTest END_STONE_ORE_REPLACEABLES = new BlockMatchTest(Blocks.END_STONE);

		protected final List<List<OreConfiguration.TargetBlockState>> targets = new ArrayList<>();
		protected int minSize = 1;
		protected int maxSize = 1;
		protected int weight = 1;

		public Builder block(RuleTest target, BlockState block)
		{
			this.targets.add(ImmutableList.of(OreConfiguration.target(target, block)));
			return this;
		}

		public Builder weight(int weight)
		{
			this.weight = weight;
			return this;
		}

		public Builder size(int min, int max)
		{
			this.minSize = min;
			this.maxSize = max;
			return this;
		}

		public Layer build()
		{
			return new Layer(targets, minSize, maxSize, weight);
		}
	}

	public static class OverworldBuilder extends Builder
	{
		public OverworldBuilder passiveBlock()
		{
			return blocks(Blocks.STONE.defaultBlockState(), Blocks.DEEPSLATE.defaultBlockState());
		}

		public OverworldBuilder block(Block block)
		{
			return block(block.defaultBlockState());
		}

		public OverworldBuilder block(BlockState state)
		{
			return blocks(state, state);
		}

		public OverworldBuilder blocks(Block stone, Block deepslate)
		{
			return blocks(stone.defaultBlockState(), deepslate.defaultBlockState());
		}

		public OverworldBuilder blocks(BlockState stone, BlockState deepslate)
		{
			this.targets.add(ImmutableList.of(OreConfiguration.target(STONE_ORE_REPLACEABLES, stone), OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, deepslate)));
			return this;
		}
	}

	public static class NetherBuilder extends Builder
	{
		public NetherBuilder passiveBlock()
		{
			return block(Blocks.NETHERRACK.defaultBlockState());
		}

		public NetherBuilder block(Block block)
		{
			return block(block.defaultBlockState());
		}

		public NetherBuilder block(BlockState block)
		{
			block(NETHER_ORE_REPLACEABLES, block);
			return this;
		}
	}

	public static class EndBuilder extends Builder
	{
		public EndBuilder passiveBlock()
		{
			return block(Blocks.END_STONE.defaultBlockState());
		}

		public EndBuilder block(Block block)
		{
			return block(block.defaultBlockState());
		}

		public EndBuilder block(BlockState block)
		{
			block(END_STONE_ORE_REPLACEABLES, block);
			return this;
		}
	}
}
