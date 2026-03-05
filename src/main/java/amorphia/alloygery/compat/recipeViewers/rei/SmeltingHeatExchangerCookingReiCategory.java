package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.compat.recipeViewers.RecipeViewerCommonBlockStateRenderers;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.furnace.FurnaceWithHeatExchangerBlock;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SmeltingHeatExchangerCookingReiCategory extends HeatExchangerCookingReiCategory
{
	public static final Component TITLE = Component.translatable("category.rei.alloygery.heat_exchanger_smelting");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(MachinesModule.FURNACE_WITH_HEAT_EXCHANGER);

	@Override
	public void drawFurnaceBlockState(GuiGraphics graphics, int x, int y)
	{
		RecipeViewerCommonBlockStateRenderers.drawHeatExchangerWithBlock(
				graphics, x, y, MachinesModule.FURNACE_WITH_HEAT_EXCHANGER.defaultBlockState()
						.setValue(FurnaceWithHeatExchangerBlock.FACING, Direction.SOUTH)
						.setValue(FurnaceWithHeatExchangerBlock.LIT, true)
		);
	}

	@Override
	public CategoryIdentifier<? extends HeatExchangerCookingReiDisplay> getCategoryIdentifier()
	{
		return AlloygeryReiPlugin.HEAT_EXCHANGER_SMELTING;
	}

	@Override
	public Component getTitle()
	{
		return TITLE;
	}

	@Override
	public Renderer getIcon()
	{
		return ICON;
	}
}
