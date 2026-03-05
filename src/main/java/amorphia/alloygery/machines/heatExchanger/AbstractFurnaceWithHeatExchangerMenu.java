package amorphia.alloygery.machines.heatExchanger;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AbstractFurnaceWithHeatExchangerMenu extends RecipeBookMenu<Container>
{
    private final Container container;
    private final ContainerData data;
    private final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType recipeBookType;

    protected AbstractFurnaceWithHeatExchangerMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int containerId,
            Inventory playerInventory)
    {
        this(menuType, recipeType, recipeBookType, containerId, playerInventory, new SimpleContainer(2), new SimpleContainerData(5));
    }

    protected AbstractFurnaceWithHeatExchangerMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int containerId,
            Inventory playerInventory, Container container, ContainerData data)
    {
        super(menuType, containerId);
        this.recipeType = recipeType;
        this.recipeBookType = recipeBookType;
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();

        checkContainerSize(container, 2);
        checkContainerDataCount(data, 5);

        // input
        this.addSlot(new Slot(container, 0, 56, 17));

        // result
        this.addSlot(new Slot(container, 1, 116, 35){
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
                if (player instanceof ServerPlayer serverPlayer && this.container instanceof AbstractFurnaceWithHeatExchangerBlockEntity<?> entity)
                {
                    entity.dropExperience(serverPlayer, serverPlayer.serverLevel(), Vec3.atCenterOf(entity.getBlockPos()));
                }
                this.removeCount = 0;

                super.onTake(player, stack);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addDataSlots(data);
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
            if (index == AbstractFurnaceWithHeatExchangerBlockEntity.SLOT_RESULT)
            {
                slotItem.onCraftedBy(slotStack, player.level(), player);
                if (!this.moveItemStackTo(slotStack, AbstractFurnaceWithHeatExchangerBlockEntity.SLOT_RESULT + 1, slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, newStack);
            }
            // input slots to inventory
            else if (index < AbstractFurnaceWithHeatExchangerBlockEntity.SLOT_RESULT)
            {
                if (!this.moveItemStackTo(slotStack, AbstractFurnaceWithHeatExchangerBlockEntity.SLOT_RESULT + 1, slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // inventory to input slots
            else if (this.level.getRecipeManager().getRecipeFor(recipeType, new SimpleContainer(slotStack), this.level).isPresent())
            {
                if(!this.moveItemStackTo(slotStack, 0, AbstractFurnaceWithHeatExchangerBlockEntity.SLOT_RESULT, false))
                    return ItemStack.EMPTY;
            }
            // inventory to hot bar
            else if (index < slots.size() - 9)
            {
                if(!this.moveItemStackTo(slotStack, slots.size() - 9, slots.size(), false))
                    return ItemStack.EMPTY;
            }
            // hot bar to inventory
            else if (!this.moveItemStackTo(slotStack, AbstractFurnaceWithHeatExchangerBlockEntity.SLOT_RESULT + 1, slots.size() - 9, false))
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

    public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(itemHelper);
        }

    }

    public void clearCraftingContent() {
        this.getSlot(0).set(ItemStack.EMPTY);
    }

    public boolean recipeMatches(Recipe<? super Container> recipe) {
        return recipe.matches(this.container, this.level);
    }

    public int getResultSlotIndex() {
        return 1;
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return 2;
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public int getSmeltingProgress()
    {
        final int remaining = this.data.get(1);
        final int total = this.data.get(0);
        return remaining == 0 || total == 0 ? 0 : remaining * 24 / total;
    }

    public int getHeatedTimeRemaining()
    {
        final int remaining = this.data.get(3);
        final int total = this.data.get(2);
        return remaining == 0 || total == 0 ? 0 : remaining * 16 / total;
    }

    public int getHeatValue()
    {
        return this.data.get(3);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public boolean isHeatExchangerLit()
    {
        return this.data.get(4) == 1;
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != 1;
    }
}
