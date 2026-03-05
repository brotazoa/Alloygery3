package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.FireproofProperty;
import net.minecraft.world.item.ItemStack;

public interface IDynamicFireproof
{
    default boolean isFireproof(ItemStack dynamicGearStack)
    {
        return FireproofProperty.compute(dynamicGearStack);
    }
}
