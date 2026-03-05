package amorphia.alloygery.machines.heatExchanger.blastFurnace;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class BlastFurnaceWithHeatExchangerMenu extends AbstractFurnaceWithHeatExchangerMenu
{
    public BlastFurnaceWithHeatExchangerMenu(int containerId, Inventory playerInventory)
    {
        super(MachinesModule.BLAST_FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE, RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, containerId, playerInventory);
    }

    public BlastFurnaceWithHeatExchangerMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData)
    {
        super(MachinesModule.BLAST_FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE, RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, containerId, playerInventory, furnaceContainer, furnaceData);
    }
}
