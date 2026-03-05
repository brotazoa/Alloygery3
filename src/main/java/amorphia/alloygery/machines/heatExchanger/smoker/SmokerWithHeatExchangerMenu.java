package amorphia.alloygery.machines.heatExchanger.smoker;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class SmokerWithHeatExchangerMenu extends AbstractFurnaceWithHeatExchangerMenu
{
    public SmokerWithHeatExchangerMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.SMOKER_WITH_HEAT_EXCHANGER_MENU_TYPE, RecipeType.SMOKING, RecipeBookType.SMOKER, containerId, playerInventory);
    }

    public SmokerWithHeatExchangerMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData)
    {
        super(MachinesModule.SMOKER_WITH_HEAT_EXCHANGER_MENU_TYPE, RecipeType.SMOKING, RecipeBookType.SMOKER, containerId, playerInventory, furnaceContainer, furnaceData);
    }
}
