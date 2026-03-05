package amorphia.alloygery.machines.woodcutter;

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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WoodcutterBlock extends Block implements MachinesModelProvider.MachineBlockModelDataGenerator, MachinesRecipeProvider.IRecipeGen, MachinesTagProvider.IBlockTagGen
{
    private static final Component TITLE = Component.translatable("container.alloygery.woodcutter");
    private static final DirectionProperty FACING;

    protected static final VoxelShape SHAPE;

    public WoodcutterBlock()
    {
        this(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5f).requiresTool().ignitedByLava());
    }

    public WoodcutterBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        player.awardStat(MachinesModule.INTERACT_WITH_WOODCUTTER);

        MenuProvider menu = state.getMenuProvider(level, pos);
        if(menu != null)
            player.openMenu(menu);

        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return new SimpleMenuProvider((syncId, inventory, player) -> new WoodcutterMenu(syncId, inventory, ContainerLevelAccess.create(level, pos)), TITLE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    static
    {
        FACING = HorizontalDirectionalBlock.FACING;
        SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 9.0F, 16.0F);
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
                .define('w', TagKey.create(Registries.ITEM, Alloygery.asCommonResource("stripped_logs")))
                .define('i', Items.IRON_INGOT)
                .pattern(" i ")
                .pattern("www")
                .unlockedBy("has_ingot", RecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
    }

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/axe"))).add(this);
	}
}
