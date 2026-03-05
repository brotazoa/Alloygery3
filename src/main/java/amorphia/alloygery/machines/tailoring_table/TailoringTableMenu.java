package amorphia.alloygery.machines.tailoring_table;

import amorphia.alloygery.machines.AbstractSingleIngredientMenu;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class TailoringTableMenu extends AbstractSingleIngredientMenu<TailoringTableRecipe>
{
    public TailoringTableMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.TAILORING_TABLE_MENU_TYPE, containerId, playerInventory, TailoringTableRecipe.Type.INSTANCE);
        this.uiItemTakeSoundEvent = SoundEvents.UI_LOOM_TAKE_RESULT;
    }

    public TailoringTableMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access)
    {
        super(MachinesModule.TAILORING_TABLE_MENU_TYPE, containerId, playerInventory, TailoringTableRecipe.Type.INSTANCE, access);
        this.uiItemTakeSoundEvent = SoundEvents.UI_LOOM_TAKE_RESULT;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(this.access, player, MachinesModule.TAILORING_TABLE);
    }
}
