package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.OccludeVibrationsProperty;
import net.minecraft.world.item.ItemStack;

public interface IOccludeVibrations
{
	default boolean occludesVibrations(ItemStack dynamicGearStack)
	{
		return OccludeVibrationsProperty.compute(dynamicGearStack);
	}
}
