package amorphia.alloygery.machines.heatExchanger.blastFurnace;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerScreen;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlastFurnaceWithHeatExchangerScreen extends AbstractFurnaceWithHeatExchangerScreen<BlastFurnaceWithHeatExchangerMenu>
{
    private static final ResourceLocation TEXTURE = Alloygery.asResource("textures/gui/blast_furnace_with_heat_exchanger.png");

    public BlastFurnaceWithHeatExchangerScreen(BlastFurnaceWithHeatExchangerMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, new BlastingRecipeBookComponent(), playerInventory, title, TEXTURE);
    }
}
