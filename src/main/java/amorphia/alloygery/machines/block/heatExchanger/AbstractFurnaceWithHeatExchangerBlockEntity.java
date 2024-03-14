package amorphia.alloygery.machines.block.heatExchanger;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFurnaceWithHeatExchangerBlockEntity<R extends AbstractCookingRecipe> extends AbstractSmelterWithHeatExchangerBlockEntity<R>
{
	private static final int[] TOP_SLOTS = new int[] { 0 };
	private static final int[] SIDE_SLOTS = new int[] { 1 };
	private static final int[] BOTTOM_SLOTS = new int[] { 1 };

	protected static final int SLOT_INPUT = 0;
	protected static final int SLOT_RESULT = 1;

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

	protected AbstractFurnaceWithHeatExchangerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState,
			RecipeType<R> recipeType)
	{
		super(type, pos, blockState, recipeType);
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);

		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.inventory);

		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);

		ContainerHelper.saveAllItems(tag, this.inventory);
	}

	@Override
	protected int getSmeltingDuration(Level level, AbstractSmelterWithHeatExchangerBlockEntity<R> blockEntity)
	{
		return blockEntity.recipeQuickCheck.getRecipeFor(blockEntity, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
	}

	@Override
	protected void removeIngredient(Ingredient ingredient)
	{
		ItemStack actual = this.getItem(SLOT_INPUT);
		if (!actual.isEmpty() && ingredient.test(actual))
		{
			actual.shrink(1);
			this.setItem(SLOT_INPUT, actual.isEmpty() ? ItemStack.EMPTY : actual);
		}
	}

	@Override
	public void dropExperience(ServerPlayer player, ServerLevel level, Vec3 position)
	{
		if(recipesUsed == null || recipesUsed.isEmpty()) return;

		List<Recipe<?>> recipes = new ArrayList<>();
		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet())
		{
			level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
				recipes.add(recipe);
				float exp = (float) entry.getIntValue() * ((AbstractCookingRecipe)recipe).getExperience();
				int expAsInt = Mth.floor(exp);
				float random = Mth.frac(exp);
				if (random != 0.0f && Math.random() < random)
				{
					expAsInt++;
				}

				ExperienceOrb.award(level, position, expAsInt);
			});
		}

		if(player != null)
			player.awardRecipes(recipes);

		this.recipesUsed.clear();
	}

	@Override
	protected int getResultSlot()
	{
		return SLOT_RESULT;
	}

	@Override
	protected int getCoolingSpeed()
	{
		return 2;
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return switch (side)
		{
			case DOWN -> BOTTOM_SLOTS;
			case UP -> TOP_SLOTS;
			default -> SIDE_SLOTS;
		};
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction)
	{
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack)
	{
		return index != SLOT_RESULT;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		return true;
	}

	@Nullable
	@Override
	public Recipe<?> getRecipeUsed()
	{
		return null;
	}

	@Override
	public void fillStackedContents(StackedContents contents)
	{
		for(ItemStack itemStack : this.inventory)
		{
			contents.accountStack(itemStack);
		}
	}

	@Override
	public int getContainerSize()
	{
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount)
	{
		return ContainerHelper.removeItem(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		return ContainerHelper.takeItem(this.inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{
		ItemStack actual = this.inventory.get(slot);
		boolean sameNotEmpty = !stack.isEmpty() && ItemStack.isSameItemSameTags(actual, stack);

		this.inventory.set(slot, stack);
		if(stack.getCount() > this.getMaxStackSize())
			stack.setCount(this.getMaxStackSize());

		if (slot < SLOT_RESULT && !sameNotEmpty)
		{
			this.smeltingProgress = 0;
			this.totalSmeltingTime = getSmeltingDuration(this.level, this);
			this.setChanged();
		}
	}

	@Override
	public boolean stillValid(Player player)
	{
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent()
	{
		this.inventory.clear();
	}
}
