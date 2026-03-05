package amorphia.alloygery.machines.tailoring_table;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.datagen.MachinesModelProvider;
import amorphia.alloygery.machines.datagen.MachinesTagProvider;
import amorphia.alloygery.machines.datagen.recipe.MachinesRecipeProvider;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TailoringTableBlock extends Block implements MachinesModelProvider.MachineBlockModelDataGenerator, MachinesRecipeProvider.IRecipeGen, MachinesTagProvider.IBlockTagGen
{
    private static final Component TITLE = Component.translatable("container.alloygery.tailoring_table");

    public static final DirectionProperty FACING;
    public static final VoxelShape SHAPE;

    public TailoringTableBlock()
    {
        this(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE).nonOpaque());
    }

    public TailoringTableBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        player.awardStat(MachinesModule.INTERACT_WITH_TAILORING_TABLE);

        MenuProvider menu = state.getMenuProvider(level, pos);
        if(menu != null)
            player.openMenu(menu);

        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return new SimpleMenuProvider((syncId, inventory, player) -> new TailoringTableMenu(syncId, inventory, ContainerLevelAccess.create(level, pos)), TITLE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
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
        final TagKey<Item> stripped_logs = TagKey.create(Registries.ITEM, Alloygery.asCommonResource("stripped_logs"));
        final TagKey<Item> hides = TagKey.create(Registries.ITEM, Alloygery.asCommonResource("hides"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, this.asItem())
                .define('s', stripped_logs)
                .define('l', hides)
                .define('w', ItemTags.WOOL)
                .pattern("lw")
                .pattern("ss")
                .unlockedBy("has_stripped_logs", RecipeProvider.has(stripped_logs))
                .save(exporter);
    }

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/axe"))).add(this);
	}

    static
    {
        FACING = HorizontalDirectionalBlock.FACING;

        final VoxelShape l1 = Block.box(1.0, 0.0, 1.0, 4.0, 10.0, 4.0);
        final VoxelShape l2 = Block.box(1.0, 0.0, 12.0, 4.0, 10.0, 15.0);
        final VoxelShape l3 = Block.box(12.0, 0.0, 1.0, 15.0, 10.0, 4.0);
        final VoxelShape l4 = Block.box(12.0, 0.0, 12.0, 15.0, 10.0, 15.0);
        final VoxelShape t = Block.box(0.0, 10.0, 0.0, 16.0, 14.0, 16.0);

        SHAPE = Shapes.or(l1, l2, l3, l4, t);
    }
}
