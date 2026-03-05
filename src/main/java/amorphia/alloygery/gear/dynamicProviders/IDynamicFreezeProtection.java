package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.FreezeProtectionProperty;
import net.minecraft.world.item.ItemStack;

public interface IDynamicFreezeProtection
{
    default int getExtraFreezeProtectionTicks(ItemStack dynamicGearStack)
    {
        return FreezeProtectionProperty.compute(dynamicGearStack);
    }
}
