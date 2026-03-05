package amorphia.alloygery.machines.woodcutter;

import amorphia.alloygery.machines.AbstractSingleIngredientMenu;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class WoodcutterMenu extends AbstractSingleIngredientMenu<WoodcutterRecipe>
{
    public WoodcutterMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.WOODCUTTER_MENU_TYPE, containerId, playerInventory, WoodcutterRecipe.Type.INSTANCE);
        this.uiItemTakeSoundEvent = SoundEvents.VILLAGER_WORK_TOOLSMITH;
    }

    public WoodcutterMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access)
    {
        super(MachinesModule.WOODCUTTER_MENU_TYPE, containerId, playerInventory, WoodcutterRecipe.Type.INSTANCE, access);
        this.uiItemTakeSoundEvent = SoundEvents.VILLAGER_WORK_TOOLSMITH;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(this.access, player, MachinesModule.WOODCUTTER);
    }
}
