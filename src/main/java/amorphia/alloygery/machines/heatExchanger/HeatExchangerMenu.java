package amorphia.alloygery.machines.heatExchanger;

import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class HeatExchangerMenu extends AbstractContainerMenu
{
    private final Container heatExchangerInventory;
    private final ContainerData containerData;

    public HeatExchangerMenu(int syncId, Inventory playerInventory)
    {
        this(syncId, playerInventory, new SimpleContainer(1), new SimpleContainerData(4));
    }

    public HeatExchangerMenu(int syncId, Inventory playerInventory, Container heatExchangerInventory, ContainerData containerData)
    {
        super(MachinesModule.HEAT_EXCHANGER_MENU_TYPE, syncId);
        this.heatExchangerInventory = heatExchangerInventory;
        this.containerData = containerData;
        this.addDataSlots(containerData);

        checkContainerSize(heatExchangerInventory, 1);
        checkContainerDataCount(containerData, 4);

        heatExchangerInventory.startOpen(playerInventory.player);

        // fuel slot
        this.addSlot(new Slot(heatExchangerInventory, 0, 80, 53){
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

        // player inventory
        for (int playerRow = 0; playerRow < 3; playerRow++)
        {
            for (int playerColumn = 0; playerColumn < 9; playerColumn++)
            {
                // TODO: replace inline values with constants from screen
                this.addSlot(new Slot(playerInventory, playerColumn + playerRow * 9 + 9, 8 + playerColumn * 18, 84 + playerRow * 18));
            }
        }

        // player hot bar
        for(int barColumn = 0; barColumn < 9; barColumn++)
        {
            // TODO: replace inline values with constants from screen
            this.addSlot(new Slot(playerInventory, barColumn, 8 + barColumn * 18, 142));
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
            newStack = slotStack.copy();

            if (index == HeatExchangerBlockEntity.FUEL_SLOT)
            {
                if (!this.moveItemStackTo(slotStack, HeatExchangerBlockEntity.FUEL_SLOT + 1, slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // inventory to fuel slot
			else if (AbstractFurnaceBlockEntity.isFuel(slotStack))
            {
                if(!this.moveItemStackTo(slotStack, HeatExchangerBlockEntity.FUEL_SLOT, HeatExchangerBlockEntity.FUEL_SLOT + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // inventory to hot bar
            else if (index < slots.size() - 9)
            {
                if(!this.moveItemStackTo(slotStack, slots.size() - 9, slots.size(), false))
                    return ItemStack.EMPTY;
            }
            // hot bar to inventory
            else if (!this.moveItemStackTo(slotStack, HeatExchangerBlockEntity.FUEL_SLOT + 1, slots.size() - 9, false))
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
        return this.heatExchangerInventory.stillValid(player);
    }

    public int getLitTimeRemaining()
    {
        final int remaining = containerData.get(1);
        final int total = containerData.get(0);
        return total == 0 || remaining == 0 ? 0 : remaining * 14 / total;
    }

    public int getHeatedTimeRemaining()
    {
        final int remaining = containerData.get(2);
        return remaining == 0 ? 0 : remaining * 14 / HeatExchangerBlockEntity.MAX_HEATED_TIME;
    }

    public int getHeatValue()
    {
        return containerData.get(2);
    }
}
