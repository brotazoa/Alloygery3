package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.machines.MachinesModule;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class TailoringReiCategory extends SingleIngredientReiCategory
{
	public static final Component TITLE = Component.translatable("category.rei.alloygery.tailoring");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(MachinesModule.TAILORING_TABLE);

	@Override
	public CategoryIdentifier<? extends SingleIngredientReiDisplay> getCategoryIdentifier()
	{
		return AlloygeryReiPlugin.TAILORING;
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
