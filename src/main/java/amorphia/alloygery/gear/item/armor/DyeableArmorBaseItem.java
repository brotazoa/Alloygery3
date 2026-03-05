package amorphia.alloygery.gear.item.armor;

import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
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
	public void setColor(ItemStack stack, int color)
	{
		DyeableLeatherItem.super.setColor(stack, color);
		NBTHelper.getPartTagFromItemStack(stack, PartTypes.ARMOR_BASE).putInt(AlloygeryNBTKeys.DYE_COLOR, color);
	}

	@Override
	public void clearColor(ItemStack stack)
	{
		DyeableLeatherItem.super.clearColor(stack);
		NBTHelper.getPartTagFromItemStack(stack, PartTypes.ARMOR_BASE).remove(AlloygeryNBTKeys.DYE_COLOR);
	}
}
