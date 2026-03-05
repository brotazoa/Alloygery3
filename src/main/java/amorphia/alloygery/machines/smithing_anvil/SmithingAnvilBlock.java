package amorphia.alloygery.machines.smithing_anvil;

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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SmithingAnvilBlock extends Block implements SimpleWaterloggedBlock, MachinesModelProvider.MachineBlockModelDataGenerator, MachinesRecipeProvider.IRecipeGen, MachinesTagProvider.IBlockTagGen
{
    private static final Component TITLE = Component.translatable("container.alloygery.smithing_anvil");

    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;

    private static final VoxelShape X_AXIS_SHAPE;
    private static final VoxelShape Z_AXIS_SHAPE;

    public SmithingAnvilBlock()
    {
        this(FabricBlockSettings.copyOf(Blocks.ANVIL));
    }

    public SmithingAnvilBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        player.awardStat(MachinesModule.INTERACT_WITH_SMITING_ANVIL);

        MenuProvider menu = state.getMenuProvider(level, pos);
        if(menu != null)
            player.openMenu(menu);

        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return new SimpleMenuProvider((syncId, inventory, player) -> new SmithingAnvilMenu(syncId, inventory, ContainerLevelAccess.create(level, pos)), TITLE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise())
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
        Direction direction = state.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_SHAPE : Z_AXIS_SHAPE;
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
                .pattern("iii")
                .pattern(" i ")
                .pattern("iii")
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

        final VoxelShape z_base = Block.box(3.0, 0.0, 1.0, 13.0, 1.0, 15.0);
        final VoxelShape x_base = Block.box(1.0, 0.0, 3.0, 15.0, 1.0, 13.0);
        final VoxelShape z_step = Block.box(4.0, 1.0, 2.0, 12.0, 2.0, 14.0);
        final VoxelShape x_step = Block.box(2.0, 1.0, 4.0, 14.0, 2.0, 12.0);
        final VoxelShape z_middle = Block.box(6.0, 2.0, 3.0, 10.0, 5.0, 13.0);
        final VoxelShape x_middle = Block.box(3.0, 2.0, 6.0, 13.0, 5.0, 10.0);
        final VoxelShape z_top = Block.box(5.0, 5.0, 1.0, 11.0, 9.0, 15.0);
        final VoxelShape x_top = Block.box(1.0, 5.0, 5.0, 15.0, 9.0, 11.0);

        X_AXIS_SHAPE = Shapes.or(x_base, x_step, x_middle, x_top);
        Z_AXIS_SHAPE = Shapes.or(z_base, z_step, z_middle, z_top);
    }
}
