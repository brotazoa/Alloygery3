package amorphia.alloygery.machines.heatExchanger.furnace;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FurnaceWithHeatExchangerScreen extends AbstractFurnaceWithHeatExchangerScreen<FurnaceWithHeatExchangerMenu>
{
    private static final ResourceLocation TEXTURE = Alloygery.asResource("textures/gui/furnace_with_heat_exchanger.png");

    public FurnaceWithHeatExchangerScreen(FurnaceWithHeatExchangerMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, new SmeltingRecipeBookComponent(), playerInventory, title, TEXTURE);
    }
}
