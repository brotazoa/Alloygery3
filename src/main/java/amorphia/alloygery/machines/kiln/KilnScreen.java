package amorphia.alloygery.machines.kiln;

import amorphia.alloygery.Alloygery;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class KilnScreen extends AbstractFurnaceScreen<KilnMenu>
{
	private static final ResourceLocation TEXTURE = Alloygery.asResource("textures/gui/kiln.png");

	public KilnScreen(KilnMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, new FiringRecipeBookComponent(), playerInventory, title, TEXTURE);
	}
}
