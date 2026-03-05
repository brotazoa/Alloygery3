package amorphia.alloygery.machines.heatExchanger.furnace;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class FurnaceWithHeatExchangerMenu extends AbstractFurnaceWithHeatExchangerMenu
{
    public FurnaceWithHeatExchangerMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE, RecipeType.SMELTING, RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public FurnaceWithHeatExchangerMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData)
    {
        super(MachinesModule.FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE, RecipeType.SMELTING, RecipeBookType.FURNACE, containerId, playerInventory, furnaceContainer, furnaceData);
    }
}
