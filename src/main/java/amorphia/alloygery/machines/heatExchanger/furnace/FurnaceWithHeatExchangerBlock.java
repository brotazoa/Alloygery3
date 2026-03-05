package amorphia.alloygery.machines.heatExchanger.furnace;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.machines.datagen.MachinesBlockLootTableProvider;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.datagen.MachinesModelProvider;
import amorphia.alloygery.machines.datagen.MachinesTagProvider;
import amorphia.alloygery.machines.heatExchanger.AbstractSmelterWithHeatExchangerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FurnaceWithHeatExchangerBlock extends AbstractSmelterWithHeatExchangerBlock implements
		MachinesBlockLootTableProvider.MachineBlockLootTableDataGenerator, MachinesModelProvider.MachineBlockModelDataGenerator, MachinesTagProvider.IBlockTagGen
{
	public FurnaceWithHeatExchangerBlock()
	{
		super(FabricBlockSettings.copyOf(Blocks.FURNACE).requiresTool().strength(3.5f).luminance(state -> state.getValue(LIT) ? 13 : 0));
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player)
	{
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FurnaceWithHeatExchangerBlockEntity)
        {
            player.openMenu((MenuProvider) blockEntity);
            player.awardStat(MachinesModule.INTERACT_WITH_FURNACE_WITH_HEAT_EXCHANGER);
        }
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new FurnaceWithHeatExchangerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
	{
		return createSmelterTicker(level, blockEntityType, MachinesModule.FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (state.getValue(LIT))
		{
			double x = pos.getX() + 0.5;
			double y = pos.getY();
			double z = pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1)
			{
				//TODO: Replace Sound Event
				level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
			}

			Direction facing = state.getValue(FACING);
			Direction.Axis axis = facing.getAxis();
			double d = random.nextDouble() * 0.6 - 0.3;
			double dx = axis == Direction.Axis.X ? facing.getStepX() * 0.52 : d;
			double dy = random.nextDouble() * 6.0 / 16.0 + 0.5;
			double dz = axis == Direction.Axis.Z ? facing.getStepZ() * 0.52 : d;
			level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
		}
	}

	@Override
	public void generateBlockLootTable(AlloygeryBlockLootTableProvider builder)
	{
		builder.dropOther(this, Items.FURNACE);
	}

    @Override
    public void generateBlockModel(BlockModelGenerators blockModelGenerators)
    {
        blockModelGenerators.createFurnace(this, TexturedModel.ORIENTABLE);
    }

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/pickaxe"))).add(this);
	}
}
