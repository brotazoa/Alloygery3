package amorphia.alloygery.machines.smithing_vice;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmithingViceScreen extends AbstractSingleIngredientScreen<SmithingViceMenu>
{
	private static final ResourceLocation BACKGROUND = Alloygery.asResource("textures/gui/smithing_vice.png");

	public SmithingViceScreen(SmithingViceMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title, BACKGROUND);
	}
}
