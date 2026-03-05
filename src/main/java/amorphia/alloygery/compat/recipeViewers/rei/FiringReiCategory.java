package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.machines.MachinesModule;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.cooking.DefaultCookingCategory;
import net.minecraft.world.item.ItemStack;

public class FiringReiCategory extends DefaultCookingCategory
{
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(MachinesModule.KILN);

	public FiringReiCategory()
	{
		super(AlloygeryReiPlugin.FIRING, ICON, "category.rei.alloygery.firing");
	}
}
