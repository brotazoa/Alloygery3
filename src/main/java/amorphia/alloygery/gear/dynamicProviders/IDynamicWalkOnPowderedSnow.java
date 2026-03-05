package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.WalkOnPowderedSnowProperty;
import net.minecraft.world.item.ItemStack;

public interface IDynamicWalkOnPowderedSnow
{
    default boolean canWalkOnPowderedSnow(ItemStack dynamicGearStack)
    {
        return WalkOnPowderedSnowProperty.compute(dynamicGearStack);
    }
}
