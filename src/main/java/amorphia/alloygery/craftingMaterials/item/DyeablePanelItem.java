package amorphia.alloygery.craftingMaterials.item;

import amorphia.alloygery.craftingMaterials.CraftingMaterial;
import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class DyeablePanelItem extends PanelItem implements DyeableLeatherItem
{
	public DyeablePanelItem(Properties properties, CraftingMaterial craftingMaterial)
	{
		super(properties, craftingMaterial);
	}

	@Override
	public int getColor(ItemStack stack)
	{
		return DyeableLeatherItem.super.hasCustomColor(stack) ? DyeableLeatherItem.super.getColor(stack) : AlloygeryMaterialColors.get(this.craftingMaterial.getName()).color();
	}
}
