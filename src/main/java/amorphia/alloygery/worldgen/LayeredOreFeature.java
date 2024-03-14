package amorphia.alloygery.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LayeredOreFeature extends Feature<LayeredOreConfiguration>
{
	public LayeredOreFeature()
	{
		super(LayeredOreConfiguration.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<LayeredOreConfiguration> cntx)
	{
		RandomSource random = cntx.random();
		BlockPos origin = cntx.origin();
		WorldGenLevel worldGenLevel = cntx.level();
		LayeredOreConfiguration config = cntx.config();
		List<LayerPattern> patterns = config.layerPatterns;

		if(patterns.isEmpty())
			return false;

		LayerPattern layerPattern = patterns.get(random.nextInt(patterns.size()));

		int placedAmount = 0;
		int size = config.size;
		int radius = Mth.ceil(config.size / 2f);
		int x0 = origin.getX() - radius;
		int y0 = origin.getY() - radius;
		int z0 = origin.getZ() - radius;
		int width = size + 1;
		int depth = size + 1;
		int height = size + 1;

		if(origin.getY() >= worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX(), origin.getZ()))
			return false;

		List<Layer> resolvedLayers = new ArrayList<>();
		List<Float> layerDiameterOffsets = new ArrayList<>();

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(worldGenLevel);
		int layerCoordinate = random.nextInt(4);
		int slantyCoordinate = random.nextInt(3);
		float slope = random.nextFloat() * 0.75f;

		try
		{
			for (int x = 0; x < width; x++)
			{
				float dx = x * 2f / width - 1;
				if (dx * dx > 1) continue;

				for (int y = 0; y < height; y++)
				{
					float dy = y * 2f / height - 1;
					if (dx * dx + dy * dy > 1) continue;

					if (worldGenLevel.isOutsideBuildHeight(y0 + y)) continue;

					for (int z = 0; z < depth; z++)
					{
						float dz = z * 2f / depth - 1;

						int layerIndex = layerCoordinate == 0 ? z : layerCoordinate == 1 ? x : y;
						if (slantyCoordinate != layerCoordinate)
						{
							layerIndex += Mth.floor((slantyCoordinate == 0 ? z : slantyCoordinate == 1 ? x : y) * slope);
						}

						while (layerIndex >= resolvedLayers.size())
						{
							Layer next = layerPattern.rollNext(resolvedLayers.isEmpty() ? null : resolvedLayers.get(resolvedLayers.size() - 1),
									random);
							float offset = random.nextFloat() * 0.5f + 0.5f;
							for (int i = 0; i < next.minSize + random.nextInt(next.maxSize); i++)
							{
								resolvedLayers.add(next);
								layerDiameterOffsets.add(offset);
							}
						}

						if (dx * dx + dy * dy + dz * dz > 1 * layerDiameterOffsets.get(layerIndex)) continue;

						Layer layer = resolvedLayers.get(layerIndex);
						List<OreConfiguration.TargetBlockState> targetStateList = layer.rollBlock(random);

						int currentX = x0 + x;
						int currentY = y0 + y;
						int currentZ = z0 + z;

						mutablePos.set(currentX, currentY, currentZ);
						if (!worldGenLevel.ensureCanWrite(mutablePos)) continue;

						LevelChunkSection levelChunkSection = bulkSectionAccess.getSection(mutablePos);
						if (levelChunkSection == null) continue;

						int sectionX = SectionPos.sectionRelative(currentX);
						int sectionY = SectionPos.sectionRelative(currentY);
						int sectionZ = SectionPos.sectionRelative(currentZ);
						BlockState blockState = levelChunkSection.getBlockState(sectionX, sectionY, sectionZ);

						for (OreConfiguration.TargetBlockState targetState : targetStateList)
						{
							if(targetState.state.isAir()) continue;

							if(!canPlace(blockState, bulkSectionAccess::getBlockState, random, config, targetState, mutablePos)) continue;

							levelChunkSection.setBlockState(sectionX, sectionY, sectionZ, targetState.state, false);
							++placedAmount;
							break;
						}
					}
				}
			}
		}
		catch (Throwable thrown)
		{
			try
			{
				bulkSectionAccess.close();
			}
			catch (Throwable thrown_harder)
			{
				thrown.addSuppressed(thrown_harder);
			}
			throw thrown;
		}

		bulkSectionAccess.close();

		return placedAmount > 0;
	}

	public boolean canPlace(BlockState state, Function<BlockPos, BlockState> adjacentStateAccessor, RandomSource random,
			LayeredOreConfiguration config, OreConfiguration.TargetBlockState targetState, BlockPos.MutableBlockPos mutableBlockPos)
	{
		if(!targetState.target.test(state, random))
			return false;

		if(shouldSkipAirCheck(random, config.discardOnAirChance))
			return true;

		return !isAdjacentToAir(adjacentStateAccessor, mutableBlockPos);
	}

	protected boolean shouldSkipAirCheck(RandomSource random, float chance)
	{
		return random.nextFloat() >= chance;
	}
}
