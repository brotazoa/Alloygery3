package amorphia.alloygery.machines.kiln;

import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class KilnMenu extends AbstractFurnaceMenu
{
	public KilnMenu(int containerId, Inventory playerInventory)
	{
		super(MachinesModule.KILN_MENU_TYPE, FiringRecipe.Type.INSTANCE, RecipeBookType.FURNACE, containerId, playerInventory);
	}

	public KilnMenu(int containerId, Inventory playerInventory, Container kilnContainer, ContainerData kilnData)
	{
		super(MachinesModule.KILN_MENU_TYPE, FiringRecipe.Type.INSTANCE, RecipeBookType.FURNACE, containerId, playerInventory, kilnContainer, kilnData);
	}
}
