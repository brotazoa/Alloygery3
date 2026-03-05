package amorphia.alloygery.gear.dynamicProviders;

import net.minecraft.world.item.ItemStack;

public interface IDynamicEnchantability
{
    void calculateAndCacheEnchantability(ItemStack dynamicGearStack);
}
