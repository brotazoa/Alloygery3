package amorphia.alloygery.machines.heatExchanger.kiln;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerScreen;
import amorphia.alloygery.machines.kiln.FiringRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class KilnWithHeatExchangerScreen extends AbstractFurnaceWithHeatExchangerScreen<KilnWithHeatExchangerMenu>
{
    private static final ResourceLocation TEXTURE = Alloygery.asResource("textures/gui/kiln_with_heat_exchanger.png");

    public KilnWithHeatExchangerScreen(KilnWithHeatExchangerMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, new FiringRecipeBookComponent(), playerInventory, title, TEXTURE);
    }
}
