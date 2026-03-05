package amorphia.alloygery.machines.heatExchanger.alloyKiln;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.alloyKiln.AlloyingRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AlloyKilnWithHeatExchangerMenu extends AbstractContainerMenu
{
    private final Container alloyKilnInventory;
    private final ContainerData containerData;
    private final Level level;

    public AlloyKilnWithHeatExchangerMenu(int syncId, Inventory playerInventory)
    {
        this(syncId, playerInventory, new SimpleContainer(5), new SimpleContainerData(5));
    }

    public AlloyKilnWithHeatExchangerMenu(int syncId, Inventory playerInventory, Container alloyKilnInventory, ContainerData containerData)
    {
        super(MachinesModule.ALLOY_KILN_WITH_HEAT_EXCHANGER_MENU_TYPE, syncId);
        this.alloyKilnInventory = alloyKilnInventory;
        this.containerData = containerData;
        this.level = playerInventory.player.level();
        this.addDataSlots(containerData);

        checkContainerDataCount(containerData, 5);
        checkContainerSize(alloyKilnInventory, 5);

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

        // result slot
        this.addSlot(new Slot(alloyKilnInventory, 4, 108, 60){
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
                if (player instanceof ServerPlayer serverPlayer && this.container instanceof AlloyKilnWithHeatExchangerBlockEntity alloyKilnBlockEntity)
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
            if (index == AlloyKilnWithHeatExchangerBlockEntity.OUTPUT_SLOT)
            {
                slotItem.onCraftedBy(slotStack, player.level(), player);
                if (!this.moveItemStackTo(slotStack, AlloyKilnWithHeatExchangerBlockEntity.OUTPUT_SLOT + 1, slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, newStack);
            }
            // input slots to inventory
            else if (index < AlloyKilnWithHeatExchangerBlockEntity.OUTPUT_SLOT)
            {
                if (!this.moveItemStackTo(slotStack, AlloyKilnWithHeatExchangerBlockEntity.OUTPUT_SLOT + 1, slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // inventory to input slots
            else if (this.level.getRecipeManager().getRecipeFor(AlloyingRecipe.Type.INSTANCE, new SimpleContainer(slotStack), this.level).isPresent())
            {
                if(!this.moveItemStackTo(slotStack, 0, AlloyKilnWithHeatExchangerBlockEntity.OUTPUT_SLOT, false))
                    return ItemStack.EMPTY;
            }
            // inventory to hot bar
            else if (index < slots.size() - 9)
            {
                if(!this.moveItemStackTo(slotStack, slots.size() - 9, slots.size(), false))
                    return ItemStack.EMPTY;
            }
            // hot bar to inventory
            else if (!this.moveItemStackTo(slotStack, AlloyKilnWithHeatExchangerBlockEntity.OUTPUT_SLOT + 1, slots.size() - 9, false))
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

    public int getSmeltingProgress()
    {
        final int remaining = this.containerData.get(1);
        final int total = this.containerData.get(0);
        return remaining == 0 || total == 0 ? 0 : remaining * 24 / total;
    }

    public int getHeatedTimeRemaining()
    {
        final int remaining = this.containerData.get(3);
        final int total = this.containerData.get(2);
        return remaining == 0 || total == 0 ? 0 : remaining * 16 / total;
    }

    public int getHeatValue()
    {
        return this.containerData.get(3);
    }

    public boolean isLit() {
        return this.containerData.get(0) > 0;
    }

    public boolean isHeatExchangerLit()
    {
        return this.containerData.get(4) == 1;
    }
}
