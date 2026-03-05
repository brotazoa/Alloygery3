package amorphia.alloygery.worldgen;

import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class LayerPatterns
{
	// tin
	// shallow
	public static final Supplier<LayerPattern> TEALLITE_SHALLOW = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.TEALLITE).weight(3).size(1, 2))
					.layer(l -> l.block(Blocks.CLAY).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.DEEPSLATE).weight(1).size(1, 1))
					.layer(l -> l.passiveBlock().weight(1).size(1, 2))
					.build();
	// deep
	public static final Supplier<LayerPattern> TEALLITE_DEEP = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.TEALLITE).weight(3).size(1, 2))
					.layer(l -> l.block(Blocks.DRIPSTONE_BLOCK).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.TUFF).weight(1).size(1, 1))
					.layer(l -> l.passiveBlock().weight(1).size(1, 2))
					.build();

	// copper
	// shallow
	public static final Supplier<LayerPattern> CUPROLINE_SHALLOW = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.CUPROLINE).weight(3).size(1, 2))
					.layer(l -> l.block(Blocks.DRIPSTONE_BLOCK).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.DEEPSLATE).weight(1).size(1, 1))
					.build();
	// deep
	public static final Supplier<LayerPattern> CUPROLINE_DEEP = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.CUPROLINE).weight(3).size(1, 2))
					.layer(l -> l.block(Blocks.TUFF).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.GRANITE).block(Blocks.DRIPSTONE_BLOCK).weight(1).size(1, 2))
					.build();
	// rich
	public static final Supplier<LayerPattern> CUPROLINE_RICH = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.CUPROLINE).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.RAW_COPPER_BLOCK)
								 .block(Blocks.DRIPSTONE_BLOCK)
								 .block(Blocks.GRANITE)
								 .block(Blocks.RED_SANDSTONE)
								 .block(Blocks.SMOOTH_RED_SANDSTONE)
								 .weight(1)
								 .size(1, 1))
					.layer(l -> l.block(Blocks.CALCITE).block(Blocks.DIORITE).weight(2).size(1, 2))
					.build();

	// iron
	// deep
	public static final Supplier<LayerPattern> FERONYTE_DEEP = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.FERONYTE).weight(3).size(1, 2))
					.layer(l -> l.block(Blocks.BASALT).block(Blocks.SMOOTH_BASALT).block(Blocks.BLACKSTONE).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.TUFF).weight(1).size(1, 1))
					.build();
	// rich
	public static final Supplier<LayerPattern> FERONYTE_RICH = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.FERONYTE).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.RAW_IRON_BLOCK)
								 .block(Blocks.DRIPSTONE_BLOCK)
								 .block(Blocks.GRANITE)
								 .block(Blocks.DRIPSTONE_BLOCK)
								 .block(Blocks.GRANITE)
								 .weight(1)
								 .size(1, 1))
					.layer(l -> l.block(Blocks.BASALT).block(Blocks.SMOOTH_BASALT).block(Blocks.BLACKSTONE).weight(2).size(1, 1))
					.build();

	// gold
	// deep
	public static final Supplier<LayerPattern> AURORUM_DEEP = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.AURORUM).weight(3).size(1, 2))
					.layer(l -> l.block(Blocks.SANDSTONE).block(Blocks.SMOOTH_SANDSTONE).weight(1).size(1, 2))
					.layer(l -> l.passiveBlock().weight(1).size(1, 1))
					.build();
	// rich
	public static final Supplier<LayerPattern> AURORUM_RICH = () ->
			LayerPattern.overworldBuilder()
					.layer(l -> l.block(WorldGenModule.AURORUM).weight(1).size(1, 1))
					.layer(l -> l.block(Blocks.RAW_GOLD_BLOCK)
								 .block(Blocks.SANDSTONE)
								 .block(Blocks.SMOOTH_SANDSTONE)
								 .weight(1)
								 .size(1, 1))
					.layer(l -> l.block(Blocks.SANDSTONE).block(Blocks.SMOOTH_SANDSTONE).weight(2).size(1, 2))
					.layer(l -> l.passiveBlock().weight(1).size(1, 1))
					.build();

	// nickel nether vein
	public static final Supplier<LayerPattern> NICKELINE = () -> LayerPattern.netherBuilder()
			.layer(l -> l.block(WorldGenModule.NICKELINE).weight(4).size(2, 5))
			.layer(l -> l.block(Blocks.BASALT).block(Blocks.SMOOTH_BASALT).weight(2).size(2, 3))
			.layer(l -> l.passiveBlock().weight(1).size(1, 2))
			.build();

	// titanium end vein
	public static final Supplier<LayerPattern> TITANITE = () -> LayerPattern.endBuilder()
			.layer(l -> l.block(WorldGenModule.TITANITE).weight(2).size(2, 5))
			.layer(l -> l.passiveBlock().weight(1).size(1, 2)).build();
}
