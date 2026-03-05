package amorphia.alloygery.machines.heatExchanger.alloyKiln;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractSmelterWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlloyKilnWithHeatExchangerBlockEntity extends AbstractSmelterWithHeatExchangerBlockEntity<AlloyingRecipe>
{
	private static final int[] TOP_SLOTS = new int[] { 0, 1, 2, 3 };
	private static final int[] SIDE_SLOTS = new int[] { 4 };
	private static final int[] BOTTOM_SLOTS = new int[] { 4 };

	protected static final int OUTPUT_SLOT = 4;

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);

	public AlloyKilnWithHeatExchangerBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER_BLOCK_ENTITY, pos, blockState, AlloyingRecipe.Type.INSTANCE);
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
	protected int getSmeltingDuration(Level level, AbstractSmelterWithHeatExchangerBlockEntity<AlloyingRecipe> blockEntity)
	{
		return blockEntity.recipeQuickCheck.getRecipeFor(blockEntity, level).map(AlloyingRecipe::getSmeltingTime).orElse(200);
	}

	@Override
	protected void removeIngredient(Ingredient ingredient)
	{
		for(int i = 0; i < 4; i++)
		{
			ItemStack itemStack = this.getItem(i);
			if (!itemStack.isEmpty() && ingredient.test(itemStack))
			{
				itemStack.shrink(1);
				this.setItem(i, itemStack.isEmpty() ? ItemStack.EMPTY : itemStack);
				break;
			}
		}
	}

	@Override
	protected void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit)
	{
		if (wasLit != isLit())
		{
			state = state.setValue(AlloyKilnWithHeatExchangerBlock.LIT, isLit());
			level.setBlock(pos, state, 3);
			setChanged();
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
				float exp = (float) entry.getIntValue() * ((AlloyingRecipe)recipe).getExperience();
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
		return OUTPUT_SLOT;
	}

	@Override
	protected int getSmeltingSpeed()
	{
		return 2;
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
		return index != OUTPUT_SLOT;
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
	protected Component getDefaultName()
	{
		return Component.translatable("container.alloygery.alloy_kiln_with_heat_exchanger");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return new AlloyKilnWithHeatExchangerMenu(containerId, inventory, this, dataAccess);
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

		if (slot < OUTPUT_SLOT && !sameNotEmpty)
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
