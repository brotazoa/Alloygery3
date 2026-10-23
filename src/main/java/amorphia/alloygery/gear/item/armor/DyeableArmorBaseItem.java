package amorphia.alloygery.gear.item.armor;

import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.MaterialHelper;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class DyeableArmorBaseItem extends ArmorBaseItem implements DyeableLeatherItem
{
    public DyeableArmorBaseItem(Type type)
    {
        super(type);
    }

	@Override
	public int getColor(ItemStack stack)
	{
		return DyeableLeatherItem.super.hasCustomColor(stack) ? DyeableLeatherItem.super.getColor(stack) : AlloygeryMaterialColors.get(MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_BASE, stack).getMaterialIdentifier()).color();
	}

	@Override
	public int getMaterialColor(ItemStack stack, int tintIndex)
	{
		return tintIndex == 1 ? getColor(stack) : super.getMaterialColor(stack, tintIndex);
	}
}
