package amorphia.alloygery.machines;

import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class AbstractSingleIngredientMenu<R extends AbstractSingleIngredientRecipe> extends AbstractContainerMenu
{
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    protected final ContainerLevelAccess access;
    protected final DataSlot selectedRecipeIndex;
    protected final Level level;
    protected final RecipeType<R> recipeType;

    protected SoundEvent uiItemTakeSoundEvent = SoundEvents.UI_STONECUTTER_TAKE_RESULT;

    private List<R> recipes;
    private ItemStack input;

    long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener;
    final Container container;
    final ResultContainer resultContainer;

    public AbstractSingleIngredientMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, RecipeType<R> recipeType)
    {
        this(menuType, containerId, playerInventory, recipeType, ContainerLevelAccess.NULL);
    }

    public AbstractSingleIngredientMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, RecipeType<R> recipeType, final ContainerLevelAccess access)
    {
        super(menuType, containerId);

        this.access = access;
        this.selectedRecipeIndex = DataSlot.standalone();
        this.level = playerInventory.player.level();
        this.recipeType = recipeType;

        this.recipes = Lists.newArrayList();
        this.input = ItemStack.EMPTY;

        this.slotUpdateListener = () -> {};

        this.container = new SimpleContainer(1) {
            @Override
            public void setChanged()
            {
                super.setChanged();
                AbstractSingleIngredientMenu.this.slotsChanged(this);
                AbstractSingleIngredientMenu.this.slotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();

        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack)
            {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                AbstractSingleIngredientMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());

                final int materialCost = getMaterialCost();
                ItemStack itemStack = AbstractSingleIngredientMenu.this.inputSlot.remove(materialCost);
                if (!itemStack.isEmpty())
                {
                    AbstractSingleIngredientMenu.this.setupResultSlot();
                }

                access.execute((level1, blockPos) -> {
                    long l = level1.getGameTime();
                    if(AbstractSingleIngredientMenu.this.lastSoundTime != l)
                    {
                        // TODO: Replace with custom sound event
                        level1.playSound(null, blockPos, AbstractSingleIngredientMenu.this.uiItemTakeSoundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
                        AbstractSingleIngredientMenu.this.lastSoundTime = l;
                    }
                });

                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems()
            {
                return List.of(AbstractSingleIngredientMenu.this.inputSlot.getItem());
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

        this.addDataSlot(this.selectedRecipeIndex);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            Item slotItem = slotStack.getItem();
            newStack = slotStack.copy();

            // output slot to inventory
            if (index == OUTPUT_SLOT)
            {
                slotItem.onCraftedBy(slotStack, player.level(), player);
                if (!this.moveItemStackTo(slotStack, OUTPUT_SLOT + 1, this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, newStack);
            }
            // input slot to inventory
            else if (index == INPUT_SLOT)
            {
                if (!this.moveItemStackTo(slotStack, OUTPUT_SLOT + 1, this.slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // inventory to input slot
            else if (this.level.getRecipeManager().getRecipeFor(recipeType, new SimpleContainer(slotStack), this.level).isPresent())
            {
                if (!this.moveItemStackTo(slotStack, INPUT_SLOT, INPUT_SLOT + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // inventory to hot bar
            else if (index > OUTPUT_SLOT && index < this.slots.size() - 9)
            {
                if(!this.moveItemStackTo(slotStack, this.slots.size() - 9, this.slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            }
            // hot bar to inventory
            else if (index > this.slots.size() - 9 && !this.moveItemStackTo(slotStack, OUTPUT_SLOT + 1, this.slots.size() - 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty())
            {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();

            if (slotStack.getCount() == newStack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }

        return newStack;
    }

    @Override
    public void slotsChanged(Container container)
    {
        ItemStack itemStack = this.inputSlot.getItem();
        if (!itemStack.is(this.input.getItem()))
        {
            this.input = itemStack.copy();
            this.setupRecipeList(container, itemStack);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot)
    {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if (this.isValidRecipeIndex(id))
        {
            this.selectedRecipeIndex.set(id);
            this.setupResultSlot();
        }

        return true;
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level1, blockPos) -> this.clearContainer(player, this.container));
    }

    public void registerUpdateListener(Runnable listener)
    {
        this.slotUpdateListener = listener;
    }

    public int getSelectedRecipeIndex()
    {
        return this.selectedRecipeIndex.get();
    }

    public List<R> getRecipes()
    {
        return this.recipes;
    }

    public int getNumRecipes()
    {
        return this.recipes.size();
    }

    public boolean hasInputItem()
    {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    public boolean canAfford()
    {
        return canAfford(this.selectedRecipeIndex.get());
    }

    public boolean canAfford(int index)
    {
        if(!isValidRecipeIndex(index)) return false;

        return getRecipes().get(index).canAfford(this.container, this.level);
    }

    protected boolean isValidRecipeIndex(int recipeIndex)
    {
        return recipeIndex >= 0 && recipeIndex < this.recipes.size();
    }

    protected int getMaterialCost()
    {
        if(!isValidRecipeIndex(this.selectedRecipeIndex.get())) return -1;
        return getRecipes().get(this.selectedRecipeIndex.get()).getMaterialCost();
    }

    protected void setupRecipeList(Container container, ItemStack stack)
    {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!stack.isEmpty())
        {
            this.recipes = this.level.getRecipeManager().getRecipesFor(recipeType, container, this.level);
        }
    }

    protected void setupResultSlot()
    {
        if(!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get()) && canAfford())
        {
            R recipe = this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack itemStack = recipe.assemble(this.container, this.level.registryAccess());
            if (itemStack.isItemEnabled(this.level.enabledFeatures()))
            {
                this.resultContainer.setRecipeUsed(recipe);
                this.resultSlot.set(itemStack);
            }
            else
            {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        }
        else
        {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }
}
