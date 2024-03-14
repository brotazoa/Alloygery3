package amorphia.alloygery.machines.block.heatExchanger;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.recipe.HeatExchangerBlockRevertRecipe;
import amorphia.alloygery.machines.recipe.HeatExchangerBlockTransformRecipe;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class HeatExchangerBlock extends BaseEntityBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public HeatExchangerBlock()
	{
		super(FabricBlockSettings.copyOf(Blocks.FURNACE).requiresTool().strength(3.5f).luminance(state -> state.getValue(LIT) ? 13 : 0));
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(level.isClientSide) return InteractionResult.SUCCESS;

		player.awardStat(MachinesModule.INTERACT_WITH_HEAT_EXCHANGER);
		// TODO: open container

		return InteractionResult.CONSUME;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return super.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		if (stack.hasCustomHoverName())
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof HeatExchangerBlockEntity heatExchangerBlockEntity)
			{
				heatExchangerBlockEntity.setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		HeatExchangerBlockTransformRecipe.applyRecipeInWorld(level, pos);

		super.onPlace(state, level, pos, oldState, movedByPiston);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		if(pos.above().equals(neighborPos)) HeatExchangerBlockTransformRecipe.applyRecipeInWorld(level, pos);

		super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof HeatExchangerBlockEntity heatExchangerBlockEntity)
			{
				if (level instanceof ServerLevel serverLevel)
				{
					Containers.dropContents(level, pos, heatExchangerBlockEntity);
				}

				HeatExchangerBlockRevertRecipe.applyRecipeInWorld(level, pos.above());

				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, movedByPiston);
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new HeatExchangerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
	{
		return level.isClientSide ? null : createTickerHelper(blockEntityType, MachinesModule.HEAT_EXCHANGER_BLOCK_ENTITY, HeatExchangerBlockEntity::serverTick);
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
}
