package amorphia.alloygery.machines.smithing_anvil;

import amorphia.alloygery.machines.AbstractSingleIngredientMenu;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;

public class SmithingAnvilMenu extends AbstractSingleIngredientMenu<SmithingAnvilRecipe>
{
    public SmithingAnvilMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.SMITHING_ANVIL_MENU_TYPE, containerId, playerInventory, SmithingAnvilRecipe.Type.INSTANCE);
        this.uiItemTakeSoundEvent = SoundEvents.ANVIL_USE;
    }

    public SmithingAnvilMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access)
    {
        super(MachinesModule.SMITHING_ANVIL_MENU_TYPE, containerId, playerInventory, SmithingAnvilRecipe.Type.INSTANCE, access);
        this.uiItemTakeSoundEvent = SoundEvents.ANVIL_USE;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(this.access, player, MachinesModule.SMITHING_ANVIL);
    }
}
