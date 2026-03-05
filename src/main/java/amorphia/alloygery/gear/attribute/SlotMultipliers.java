package amorphia.alloygery.gear.attribute;

import net.minecraft.Util;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;

public class SlotMultipliers
{
	private static final EnumMap<ArmorItem.Type, Float> ARMOR_MULTIPLIER_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
		enumMap.put(ArmorItem.Type.BOOTS, 0.10f);
		enumMap.put(ArmorItem.Type.LEGGINGS, 0.25f);
		enumMap.put(ArmorItem.Type.CHESTPLATE, 0.50f);
		enumMap.put(ArmorItem.Type.HELMET, 0.15f);
	});

	private static final EnumMap<ArmorItem.Type, Integer> ARMOR_DURABILITY_MULTIPLIER_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
		enumMap.put(ArmorItem.Type.BOOTS, 13);
		enumMap.put(ArmorItem.Type.LEGGINGS, 15);
		enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
		enumMap.put(ArmorItem.Type.HELMET, 11);
	});

	public static float getSlotMultiplier(ItemStack gearStack)
	{
		if(gearStack == null || gearStack.isEmpty())
			return 1.0f;

		if(gearStack.getItem() instanceof ArmorItem armorItem)
			return ARMOR_MULTIPLIER_PER_TYPE.get(armorItem.getType());

		return 1.0f;
	}

	public static float getDurabilitySlotMultiplier(ItemStack stack)
	{
		if(stack == null || stack.isEmpty())
			return 1.0f;

		if(stack.getItem() instanceof ArmorItem armorItem)
			return ARMOR_DURABILITY_MULTIPLIER_PER_TYPE.get(armorItem.getType());

		return 1.0f;
	}
}
