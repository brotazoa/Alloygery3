package amorphia.alloygery.machines.heatExchanger.smoker;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerScreen;
import net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmokerWithHeatExchangerScreen extends AbstractFurnaceWithHeatExchangerScreen<SmokerWithHeatExchangerMenu>
{
    private static final ResourceLocation TEXTURE = Alloygery.asResource("textures/gui/smoker_with_heat_exchanger.png");

    public SmokerWithHeatExchangerScreen(SmokerWithHeatExchangerMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, new SmokingRecipeBookComponent(), playerInventory, title, TEXTURE);
    }
}
