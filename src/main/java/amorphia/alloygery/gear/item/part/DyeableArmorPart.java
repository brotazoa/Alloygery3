package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import amorphia.alloygery.gear.item.ArmorStyles;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class DyeableArmorPart extends ArmorPart implements DyeableLeatherItem
{
	public DyeableArmorPart(AlloygeryMaterial material, ArmorStyles style)
	{
		super(material, style);
	}

	public DyeableArmorPart(Properties properties, AlloygeryMaterial material, ArmorStyles style)
	{
		super(properties, material, style);
	}

	@Override
	public int getColor(ItemStack stack)
	{
		return DyeableLeatherItem.super.hasCustomColor(stack) ? DyeableLeatherItem.super.getColor(stack) : AlloygeryMaterialColors.get(getPartMaterial().getMaterialIdentifier()).color();
	}

	@Override
	public int getMaterialColor(ItemStack stack, int tintIndex)
	{
		return tintIndex == 1 ? getColor(stack) : super.getMaterialColor(stack, tintIndex);
	}
}
