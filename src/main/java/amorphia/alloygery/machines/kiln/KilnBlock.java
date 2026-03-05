package amorphia.alloygery.machines.kiln;

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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.data.recipes.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class KilnBlock extends AbstractFurnaceBlock implements MachinesModelProvider.MachineBlockModelDataGenerator, MachinesRecipeProvider.IRecipeGen, MachinesTagProvider.IBlockTagGen
{
	public KilnBlock()
	{
		super(FabricBlockSettings.copyOf(Blocks.FURNACE).requiresTool().strength(3.5f).luminance(state -> state.getValue(LIT) ? 13 : 0));
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player)
	{
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof KilnBlockEntity)
		{
			player.openMenu((MenuProvider) blockEntity);
			player.awardStat(MachinesModule.INTERACT_WITH_KILN);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new KilnBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
	{
		return createFurnaceTicker(level, blockEntityType, MachinesModule.KILN_BLOCK_ENTITY);
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
    public void generateBlockModel(BlockModelGenerators blockModelGenerators)
    {
        blockModelGenerators.createFurnace(this, TexturedModel.ORIENTABLE);
    }

    @Override
    public void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, this.asItem())
                .define('m', Items.PACKED_MUD)
                .define('b', Items.MUD_BRICKS)
                .define('f', Items.FURNACE)
                .pattern("mmm")
                .pattern("mfm")
                .pattern("bbb")
                .unlockedBy("has_furnace", RecipeProvider.has(Items.FURNACE))
                .save(exporter);
    }

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/pickaxe"))).add(this);
	}
}
