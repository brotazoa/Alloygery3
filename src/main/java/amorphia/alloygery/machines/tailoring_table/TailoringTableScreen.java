package amorphia.alloygery.machines.tailoring_table;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TailoringTableScreen extends AbstractSingleIngredientScreen<TailoringTableMenu>
{
    private static final ResourceLocation BACKGROUND = Alloygery.asResource("textures/gui/tailoring_table.png");

    public TailoringTableScreen(TailoringTableMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, BACKGROUND);
    }
}
