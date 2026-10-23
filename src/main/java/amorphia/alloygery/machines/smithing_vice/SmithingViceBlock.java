package amorphia.alloygery.machines.smithing_vice;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.datagen.MachinesModelProvider;
import amorphia.alloygery.machines.datagen.MachinesTagProvider;
import amorphia.alloygery.machines.datagen.recipe.MachinesRecipeProvider;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class SmithingViceBlock extends Block implements SimpleWaterloggedBlock, MachinesModelProvider.MachineBlockModelDataGenerator, MachinesRecipeProvider.IRecipeGen, MachinesTagProvider.IBlockTagGen
{
	private static final Component TITLE = Component.translatable("container.alloygery.smithing_vice");

	public static final DirectionProperty FACING;
	public static final BooleanProperty WATERLOGGED;

	private static final VoxelShape NORTH;
	private static final VoxelShape SOUTH;
	private static final VoxelShape EAST;
	private static final VoxelShape WEST;

	public SmithingViceBlock()
	{
		this(FabricBlockSettings.copyOf(Blocks.ANVIL));
	}

	public SmithingViceBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(level.isClientSide) return InteractionResult.SUCCESS;

		player.awardStat(MachinesModule.INTERACT_WITH_SMITHING_VICE);

		MenuProvider menu = state.getMenuProvider(level, pos);
		if(menu != null)
			player.openMenu(menu);

		return InteractionResult.CONSUME;
	}

	@Override
	public @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
	{
		return new SimpleMenuProvider((syncId, inventory, player) -> new SmithingViceMenu(syncId, inventory, ContainerLevelAccess.create(level, pos)), TITLE);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())
				.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return switch (state.getValue(HorizontalDirectionalBlock.FACING))
		{
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case EAST -> EAST;
			case WEST -> WEST;
			default -> NORTH;
		};
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror)
	{
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type)
	{
		return false;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state)
	{
		return true;
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Override
	public void generateBlockModel(BlockModelGenerators blockModelGenerators)
	{
		blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(this, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(this))).with(BlockModelGenerators.createHorizontalFacingDispatch()));
	}

	@Override
	public void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, this.asItem())
				.define('i', Items.IRON_INGOT)
				.define('p', ItemTags.PLANKS)
				.pattern("i i")
				.pattern("ppp")
				.unlockedBy("has_ingot", RecipeProvider.has(Items.IRON_INGOT))
				.save(exporter);
	}

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/pickaxe"))).add(this);
	}

	static
	{
		FACING = HorizontalDirectionalBlock.FACING;
		WATERLOGGED = BlockStateProperties.WATERLOGGED;

		NORTH = Stream.of(
				Block.box(5, 0, 4, 11, 1, 13),
				Block.box(2, 0, 7, 5, 1, 10),
				Block.box(11, 0, 7, 14, 1, 10),
				Block.box(6, 1, 3, 10, 3, 14),
				Block.box(6, 3, 12, 10, 5, 14),
				Block.box(6, 5, 12, 10, 7, 13),
				Block.box(5, 1, 7, 11, 5, 9),
				Block.box(6, 5, 7, 10, 6, 9),
				Block.box(6, 6, 8, 10, 7, 9)
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, NORTH);
		EAST = rotateShape(Direction.NORTH, Direction.EAST, NORTH);
		WEST = rotateShape(Direction.NORTH, Direction.WEST, NORTH);
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape)
	{
		VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };
		int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.join(buffer[1],
					Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanOp.OR));
			buffer[0] = buffer[1];
			buffer[1] = Shapes.empty();
		}
		return buffer[0];
	}
}
