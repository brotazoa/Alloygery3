package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.FireProtectionProperty;
import net.minecraft.world.item.ItemStack;

public interface IDynamicFireProtection
{
    default int getExtraFireImmuneTicks(ItemStack dynamicGearStack)
    {
        return FireProtectionProperty.compute(dynamicGearStack);
    }
}
