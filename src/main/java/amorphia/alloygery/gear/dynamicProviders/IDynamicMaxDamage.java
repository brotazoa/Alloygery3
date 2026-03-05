package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.DurabilityProperty;
import amorphia.alloygery.gear.property.PropertyHelper;
import net.minecraft.world.item.ItemStack;

public interface IDynamicMaxDamage
{
    default int getMaxDamage(ItemStack dynamicGearStack)
    {
        return DurabilityProperty.compute(dynamicGearStack);
    }

    default boolean isBroken(ItemStack stack)
    {
        return PropertyHelper.isBroken(stack);
    }
}
