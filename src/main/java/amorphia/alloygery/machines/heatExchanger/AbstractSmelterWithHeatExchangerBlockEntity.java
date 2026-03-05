package amorphia.alloygery.machines.heatExchanger;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSmelterWithHeatExchangerBlockEntity<R extends Recipe<Container>> extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder,
		StackedContentsCompatible, IHeatConsumer
{
	protected int totalSmeltingTime;
	protected int smeltingProgress;
	protected int totalHeatedTime = getMaxHeatedTime();
	protected int heatedTimeRemaining;
    protected boolean heatExchangerLit = false;

	public final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	public final RecipeManager.CachedCheck<Container, R> recipeQuickCheck;

	public final ContainerData dataAccess = new ContainerData()
	{
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> AbstractSmelterWithHeatExchangerBlockEntity.this.totalSmeltingTime;
				case 1 -> AbstractSmelterWithHeatExchangerBlockEntity.this.smeltingProgress;
				case 2 -> AbstractSmelterWithHeatExchangerBlockEntity.this.totalHeatedTime;
				case 3 -> AbstractSmelterWithHeatExchangerBlockEntity.this.heatedTimeRemaining;
                case 4 -> AbstractSmelterWithHeatExchangerBlockEntity.this.heatExchangerLit ? 1 : 0;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value)
		{
			switch (index)
			{
				case 0 -> AbstractSmelterWithHeatExchangerBlockEntity.this.totalSmeltingTime = value;
				case 1 -> AbstractSmelterWithHeatExchangerBlockEntity.this.smeltingProgress = value;
				case 2 -> AbstractSmelterWithHeatExchangerBlockEntity.this.totalHeatedTime = value;
				case 3 -> AbstractSmelterWithHeatExchangerBlockEntity.this.heatedTimeRemaining = value;
                case 4 -> AbstractSmelterWithHeatExchangerBlockEntity.this.heatExchangerLit = value == 1;
			}
		}

		@Override
		public int getCount()
		{
			return 5;
		}
	};

	protected AbstractSmelterWithHeatExchangerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, RecipeType<R> recipeType)
	{
		super(type, pos, blockState);

		this.recipeQuickCheck = RecipeManager.createCheck(recipeType);
	}

	protected static <R extends Recipe<Container>> void serverTick(Level level, BlockPos pos, BlockState state, AbstractSmelterWithHeatExchangerBlockEntity<R> blockEntity)
	{
		final boolean wasLit = blockEntity.isLit();

        if(level.getBlockEntity(pos.below()) instanceof HeatExchangerBlockEntity heatExchanger)
        {
            blockEntity.heatExchangerLit = heatExchanger.isLit();
        }

		Optional<R> recipeOptional = blockEntity.recipeQuickCheck.getRecipeFor(blockEntity, level);
		if (recipeOptional.isPresent() && blockEntity.canSmelt(level, blockEntity, recipeOptional.get()))
		{
			// set total smelting time to duration from recipe
			blockEntity.totalSmeltingTime = blockEntity.getSmeltingDuration(level, blockEntity);

			// if heated, increase smelting progress
			blockEntity.smeltingProgress = blockEntity.heatedTimeRemaining > 0 ? blockEntity.smeltingProgress + blockEntity.getSmeltingSpeed() :
					// if not heated, decrease smelting progress
					blockEntity.smeltingProgress - blockEntity.getCoolingSpeed();

			// clamp smelting progress between 0 and total smelting time
			blockEntity.smeltingProgress = Mth.clamp(blockEntity.smeltingProgress, 0, blockEntity.totalSmeltingTime);

			// consume heat
			blockEntity.consumeHeat(blockEntity.getSmeltingSpeed());

			// if smelting progress is higher than recipe smelting time, craft result
			if (blockEntity.smeltingProgress >= blockEntity.totalSmeltingTime)
			{
				// reset progress
				blockEntity.smeltingProgress = 0;

				// remove recipe ingredients from input
				recipeOptional.get().getIngredients().forEach(blockEntity::removeIngredient);

				// update the result stack
				ItemStack actual = blockEntity.getItem(blockEntity.getResultSlot());
				ItemStack result = recipeOptional.get().assemble(blockEntity, level.registryAccess());
				if (actual.isEmpty())
				{
					blockEntity.setItem(blockEntity.getResultSlot(), result);
				}
				else
				{
					actual.grow(result.getCount());
				}

				// set last used recipe
				blockEntity.setRecipeUsed(recipeOptional.get());

				// send updates
				setChanged(level, pos, state);
			}
		}
		else
		{
			// reset smelting progress if no valid recipe is present
			blockEntity.smeltingProgress = 0;
		}

		// update lit property
		blockEntity.updateLitProperty(level, pos, state, wasLit);
	}

	protected abstract int getSmeltingDuration(Level level, AbstractSmelterWithHeatExchangerBlockEntity<R> blockEntity);

	protected boolean canSmelt(Level level, AbstractSmelterWithHeatExchangerBlockEntity<R> blockEntity, R recipe)
	{
		ItemStack actual = blockEntity.getItem(blockEntity.getResultSlot());
		ItemStack expected = recipe.getResultItem(level.registryAccess());
		return recipe.matches(blockEntity, level) && (actual.isEmpty() || ItemStack.isSameItemSameTags(actual, expected)) && actual.getCount() + expected.getCount() <= actual.getMaxStackSize();
	}

	protected abstract void removeIngredient(Ingredient ingredient);

	protected abstract void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit);

	public abstract void dropExperience(ServerPlayer player, ServerLevel level, Vec3 position);

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);

		this.totalSmeltingTime = tag.getInt("TotalSmeltingTime");
		this.smeltingProgress = tag.getInt("SmeltingProgress");
		this.totalHeatedTime = tag.getInt("TotalHeatedTime");
		this.heatedTimeRemaining = tag.getInt("HeatedTimeRemaining");
        this.heatExchangerLit = tag.getBoolean("HeatExchangerLit");

		CompoundTag recipesUsedTag = tag.getCompound("RecipesUsed");
		recipesUsedTag.getAllKeys().forEach(key -> this.recipesUsed.put(new ResourceLocation(key), recipesUsedTag.getInt(key)));
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);

		tag.putInt("TotalSmeltingTime", this.totalSmeltingTime);
		tag.putInt("SmeltingProgress", this.smeltingProgress);
		tag.putInt("TotalHeatedTime", this.totalHeatedTime);
		tag.putInt("HeatedTimeRemaining", this.heatedTimeRemaining);
        tag.putBoolean("HeatExchangerLit", this.heatExchangerLit);

		CompoundTag recipesUsedTag = new CompoundTag();
		this.recipesUsed.forEach((id, i) -> recipesUsedTag.putInt(id.toString(), i));
		tag.put("RecipesUsed", recipesUsedTag);
	}

	@Override
	public void consumeHeat(int amount)
	{
		this.heatedTimeRemaining = Mth.clamp(this.heatedTimeRemaining - amount, 0, this.totalHeatedTime);
	}

	@Override
	public int giveHeat(int amount)
	{
		int given = Mth.clamp(this.heatedTimeRemaining + amount, 0, this.totalHeatedTime) - this.heatedTimeRemaining;
		this.heatedTimeRemaining += given;
		return given;
	}

	protected boolean isLit()
	{
		return this.smeltingProgress > 0;
	}

    protected boolean isHeatExchangerLit()
    {
        return this.heatExchangerLit;
    }

	protected abstract int getResultSlot();

	protected abstract int getSmeltingSpeed();

	protected abstract int getCoolingSpeed();

	protected int getMaxHeatedTime()
	{
		return 300;
	}

	@Override
	public void setRecipeUsed(Recipe<?> recipe)
	{
		if(recipe == null)
			return;

		ResourceLocation id = recipe.getId();
		this.recipesUsed.addTo(id, 1);
	}

	@Override
	public void awardUsedRecipes(Player player, List<ItemStack> items)
	{
		// cancel inherited function
	}
}
