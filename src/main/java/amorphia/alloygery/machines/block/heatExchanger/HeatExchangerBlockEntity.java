package amorphia.alloygery.machines.block.heatExchanger;

import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HeatExchangerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible
{
	private static final int[] TOP_SLOTS = new int[] { 0 };
	private static final int[] SIDE_SLOTS = new int[] { 0 };
	private static final int[] BOTTOM_SLOTS = new int[] { 0 };

	protected static final int FUEL_SLOT = 0;

	protected static final int HEAT_CONVERSION_RATE = 8;
	protected static final int MAX_HEATED_TIME = 1600;
	protected static final int HEAT_GIVEN_THRESHOLD = 1000;
	protected static final int AMBIENT_COOLING_TIME = 20;

	protected int totalBurningTime;
	protected int burningTimeRemaining;
	protected int heatedTimeRemaining;
	protected int ambientCoolingTime;

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

	protected final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> HeatExchangerBlockEntity.this.totalBurningTime;
				case 1 -> HeatExchangerBlockEntity.this.burningTimeRemaining;
				case 2 -> HeatExchangerBlockEntity.this.heatedTimeRemaining;
				case 3 -> HeatExchangerBlockEntity.this.ambientCoolingTime;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value)
		{
			switch (index)
			{
				case 0 -> HeatExchangerBlockEntity.this.totalBurningTime = value;
				case 1 -> HeatExchangerBlockEntity.this.burningTimeRemaining = value;
				case 2 -> HeatExchangerBlockEntity.this.heatedTimeRemaining = value;
				case 3 -> HeatExchangerBlockEntity.this.ambientCoolingTime = value;
			}
		}

		@Override
		public int getCount()
		{
			return 4;
		}
	};

	public HeatExchangerBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.HEAT_EXCHANGER_BLOCK_ENTITY, pos, blockState);
	}

	protected static void serverTick(Level level, BlockPos pos, BlockState state, HeatExchangerBlockEntity blockEntity)
	{
		final boolean wasLit = blockEntity.isLit();

		// burn fuel if needed
		blockEntity.burnFuel();

		// ambient cooling timer
		blockEntity.ambientCoolingTime = blockEntity.heatedTimeRemaining > 0 ?
				Mth.clamp(blockEntity.ambientCoolingTime + 1, 0, AMBIENT_COOLING_TIME) :
				Mth.clamp(blockEntity.ambientCoolingTime - 1, 0, AMBIENT_COOLING_TIME);

		if (blockEntity.burningTimeRemaining > 0)
		{
			final int heatedTimeGiven = Mth.clamp(blockEntity.heatedTimeRemaining + Math.min(HEAT_CONVERSION_RATE, blockEntity.burningTimeRemaining), 0, MAX_HEATED_TIME) - blockEntity.heatedTimeRemaining;
			blockEntity.heatedTimeRemaining += heatedTimeGiven;
			blockEntity.burningTimeRemaining -= heatedTimeGiven;
		}

		if(level.getBlockEntity(pos.above()) instanceof IHeatConsumer heatConsumer && blockEntity.heatedTimeRemaining > HEAT_GIVEN_THRESHOLD)
		{
			final int heatOverThreshold = blockEntity.heatedTimeRemaining - HEAT_GIVEN_THRESHOLD;
			final int heatGiven = heatConsumer.giveHeat(Math.min(HEAT_CONVERSION_RATE, heatOverThreshold));
			blockEntity.heatedTimeRemaining -= heatGiven;
		}

		if (blockEntity.ambientCoolingTime >= AMBIENT_COOLING_TIME)
		{
			blockEntity.ambientCoolingTime = 0;
			blockEntity.heatedTimeRemaining = Mth.clamp(blockEntity.heatedTimeRemaining - 1, 0, MAX_HEATED_TIME);
		}

		blockEntity.updateLitProperty(level, pos, state, wasLit);
	}

	protected void burnFuel()
	{
		if(this.burningTimeRemaining > 0) return;

		ItemStack fuelStack = this.inventory.get(FUEL_SLOT);
		if(fuelStack.isEmpty()) return;

		this.burningTimeRemaining = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuelStack.getItem(), 0);
		this.totalBurningTime = this.burningTimeRemaining;

		if (totalBurningTime > 0)
		{
			Item fuelItem = fuelStack.getItem();
			fuelStack.shrink(1);
			if (fuelStack.isEmpty())
			{
				Item remainder = fuelItem.getCraftingRemainingItem();
				this.inventory.set(FUEL_SLOT, remainder == null ? ItemStack.EMPTY : new ItemStack(remainder));
			}

			setChanged();
		}
	}

	protected void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit)
	{
		if (wasLit != isLit())
		{
			state = state.setValue(HeatExchangerBlock.LIT, isLit());
			level.setBlock(pos, state, 3);
			setChanged();
		}
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);

		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.inventory);

		this.totalBurningTime = tag.getInt("TotalBurningTime");
		this.burningTimeRemaining = tag.getInt("BurningTimeRemaining");
		this.heatedTimeRemaining = tag.getInt("HeatedTimeRemaining");
		this.ambientCoolingTime = tag.getInt("AmbientCoolingTime");

		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);

		ContainerHelper.saveAllItems(tag, this.inventory);

		tag.putInt("TotalBurningTime", this.totalBurningTime);
		tag.putInt("BurningTimeRemaining", this.burningTimeRemaining);
		tag.putInt("HeatedTimeRemaining", this.heatedTimeRemaining);
		tag.putInt("AmbientCoolingTime", this.ambientCoolingTime);
	}

	public boolean isLit()
	{
		return this.heatedTimeRemaining > 0;
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
		if(index != FUEL_SLOT) return true;

		ItemStack fuelStack = this.inventory.get(FUEL_SLOT);
		return AbstractFurnaceBlockEntity.isFuel(stack) || stack.is(Items.BUCKET) && !fuelStack.is(Items.BUCKET);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		if (direction == Direction.DOWN && index == FUEL_SLOT)
		{
			return stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
		}

		return true;
	}

	@Override
	protected Component getDefaultName()
	{
		return Component.translatable("container.alloygery.heat_exchanger");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return null;
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
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize())
		{
			stack.setCount(this.getMaxStackSize());
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

	@Override
	public void fillStackedContents(StackedContents contents)
	{
		for(ItemStack itemStack : this.inventory)
		{
			contents.accountStack(itemStack);
		}
	}
}
