package amorphia.alloygery.machines.smithing_anvil;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmithingAnvilScreen extends AbstractSingleIngredientScreen<SmithingAnvilMenu>
{
    private static final ResourceLocation BACKGROUND = Alloygery.asResource("textures/gui/smithing_anvil.png");

    public SmithingAnvilScreen(SmithingAnvilMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, BACKGROUND);
    }
}
