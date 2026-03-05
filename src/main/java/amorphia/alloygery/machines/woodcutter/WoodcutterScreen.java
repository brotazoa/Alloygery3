package amorphia.alloygery.machines.woodcutter;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WoodcutterScreen extends AbstractSingleIngredientScreen<WoodcutterMenu>
{
    private static final ResourceLocation BACKGROUND = Alloygery.asResource("textures/gui/woodcutter.png");

    public WoodcutterScreen(WoodcutterMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, BACKGROUND);
    }
}
