package amorphia.alloygery.machines.heatExchanger;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Recipe;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSmelterWithHeatExchangerBlock extends BaseEntityBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	protected AbstractSmelterWithHeatExchangerBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(level.isClientSide) return InteractionResult.SUCCESS;

		this.openContainer(level, pos, player);
		return InteractionResult.CONSUME;
	}

	protected abstract void openContainer(Level level, BlockPos pos, Player player);

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
			if (blockEntity instanceof AbstractSmelterWithHeatExchangerBlockEntity<?> entity)
			{
				entity.setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof AbstractSmelterWithHeatExchangerBlockEntity<?> furnaceEntity)
			{
				if (level instanceof ServerLevel serverLevel)
				{
					Containers.dropContents(level, pos, furnaceEntity);
					furnaceEntity.dropExperience(null, serverLevel, Vec3.atCenterOf(pos));
				}

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

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	protected static <T extends BlockEntity, R extends Recipe<Container>> BlockEntityTicker<T> createSmelterTicker(Level level, BlockEntityType<T> serverType,
			BlockEntityType<? extends AbstractSmelterWithHeatExchangerBlockEntity<R>> clientType)
	{
		return level.isClientSide ? null : createTickerHelper(serverType, clientType, AbstractSmelterWithHeatExchangerBlockEntity::serverTick);
	}
}
