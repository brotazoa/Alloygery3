package amorphia.alloygery.machines.alloyKiln;

import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec3;

public class AlloyKilnMenu extends AbstractContainerMenu
{
	private final Container alloyKilnInventory;
	private final ContainerData containerData;
	private final Level level;

	public AlloyKilnMenu(int syncId, Inventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleContainer(6), new SimpleContainerData(4));
	}

	public AlloyKilnMenu(int syncId, Inventory playerInventory, Container alloyKilnInventory, ContainerData containerData)
	{
		super(MachinesModule.ALLOY_KILN_MENU_TYPE, syncId);
		this.alloyKilnInventory = alloyKilnInventory;
		this.containerData = containerData;
		this.level = playerInventory.player.level();
		this.addDataSlots(containerData);

		checkContainerDataCount(containerData, 4);
		checkContainerSize(alloyKilnInventory, 6);

		alloyKilnInventory.startOpen(playerInventory.player);

		// input slots
		for (int inputRow = 0; inputRow < 2; inputRow++)
		{
			for (int inputColumn = 0; inputColumn < 2; inputColumn++)
			{
				// TODO: replace inline values with constants from screen
				this.addSlot(new Slot(alloyKilnInventory, inputColumn + inputRow * 2, 53 + inputColumn * 18, 17 + inputRow * 18));
			}
		}

		// fuel slot
		this.addSlot(new Slot(alloyKilnInventory, 4, 62, 71){
			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return AbstractFurnaceBlockEntity.isFuel(stack) || FurnaceFuelSlot.isBucket(stack);
			}

			@Override
			public int getMaxStackSize(ItemStack stack)
			{
				return FurnaceFuelSlot.isBucket(stack) ? 1 : super.getMaxStackSize(stack);
			}
		});

		// result slot
		this.addSlot(new Slot(alloyKilnInventory, 5, 108, 60){
			private int removeCount = 0;

			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return false;
			}

			@Override
			public ItemStack remove(int amount)
			{
				if (this.hasItem())
				{
					this.removeCount += Math.min(amount, this.getItem().getCount());
				}
				return super.remove(amount);
			}

			@Override
			protected void onQuickCraft(ItemStack stack, int amount)
			{
				this.removeCount += amount;
				super.onQuickCraft(stack, amount);
			}

			@Override
			public void onTake(Player player, ItemStack stack)
			{
				stack.onCraftedBy(player.level(), player, this.removeCount);
				if (player instanceof ServerPlayer serverPlayer && this.container instanceof AlloyKilnBlockEntity alloyKilnBlockEntity)
				{
					alloyKilnBlockEntity.dropExperience(serverPlayer, serverPlayer.serverLevel(), Vec3.atCenterOf(alloyKilnBlockEntity.getBlockPos()));
				}
				this.removeCount = 0;

				super.onTake(player, stack);
			}
		});

		// player inventory
		for (int playerRow = 0; playerRow < 3; playerRow++)
		{
			for (int playerColumn = 0; playerColumn < 9; playerColumn++)
			{
				// TODO: replace inline values with constants from screen
				this.addSlot(new Slot(playerInventory, playerColumn + playerRow * 9 + 9, 8 + playerColumn * 18, 102 + playerRow * 18));
			}
		}

		// player hot bar
		for(int barColumn = 0; barColumn < 9; barColumn++)
		{
			// TODO: replace inline values with constants from screen
			this.addSlot(new Slot(playerInventory, barColumn, 8 + barColumn * 18, 160));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem())
		{
			ItemStack slotStack = slot.getItem();
			Item slotItem = slotStack.getItem();
			newStack = slotStack.copy();

			// result slot to inventory
			if (index == AlloyKilnBlockEntity.OUTPUT_SLOT)
			{
				slotItem.onCraftedBy(slotStack, player.level(), player);
				if (!this.moveItemStackTo(slotStack, AlloyKilnBlockEntity.OUTPUT_SLOT + 1, slots.size(), true))
				{
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(slotStack, newStack);
			}
			// input slots to inventory
			else if (index < AlloyKilnBlockEntity.OUTPUT_SLOT)
			{
				if (!this.moveItemStackTo(slotStack, AlloyKilnBlockEntity.OUTPUT_SLOT + 1, slots.size(), false))
				{
					return ItemStack.EMPTY;
				}
			}
			// inventory to fuel slot
			else if (AbstractFurnaceBlockEntity.isFuel(slotStack))
			{
				if(!this.moveItemStackTo(slotStack, AlloyKilnBlockEntity.FUEL_SLOT, AlloyKilnBlockEntity.FUEL_SLOT + 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			// inventory to input slots
			else if (this.level.getRecipeManager().getRecipeFor(SimpleAlloyingRecipe.Type.INSTANCE, new SimpleContainer(slotStack), this.level).isPresent())
			{
				if(!this.moveItemStackTo(slotStack, 0, AlloyKilnBlockEntity.FUEL_SLOT, false))
					return ItemStack.EMPTY;
			}
			// inventory to hot bar
			else if (index < slots.size() - 9)
			{
				if(!this.moveItemStackTo(slotStack, slots.size() - 9, slots.size(), false))
					return ItemStack.EMPTY;
			}
			// hot bar to inventory
			else if (!this.moveItemStackTo(slotStack, AlloyKilnBlockEntity.OUTPUT_SLOT + 1, slots.size() - 9, false))
			{
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty())
			{
				slot.setByPlayer(ItemStack.EMPTY);
			}
			else
			{
				slot.setChanged();
			}

			if(slotStack.getCount() == newStack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, slotStack);
		}

		return newStack;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return this.alloyKilnInventory.stillValid(player);
	}

	public int getLitTimeRemaining()
	{
		final int remaining = containerData.get(0);
		final int total = containerData.get(1);
		return total == 0 || remaining == 0 ? 0 : remaining * 14 / total;
	}

	public int getSmeltingProgress()
	{
		final int progress = containerData.get(2);
		final int duration = containerData.get(3);
		return duration == 0 || progress == 0 ? 0 : progress * 21 / duration;
	}
}
