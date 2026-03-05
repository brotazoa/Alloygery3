package amorphia.alloygery.machines.heatExchanger.kiln;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerMenu;
import amorphia.alloygery.machines.kiln.FiringRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class KilnWithHeatExchangerMenu extends AbstractFurnaceWithHeatExchangerMenu
{
    public KilnWithHeatExchangerMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.KILN_WITH_HEAT_EXCHANGER_MENU_TYPE, FiringRecipe.Type.INSTANCE, RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public KilnWithHeatExchangerMenu(int containerId, Inventory playerInventory, Container kilnContainer, ContainerData kilnData)
    {
        super(MachinesModule.KILN_WITH_HEAT_EXCHANGER_MENU_TYPE, FiringRecipe.Type.INSTANCE, RecipeBookType.FURNACE, containerId, playerInventory, kilnContainer, kilnData);
    }
}
