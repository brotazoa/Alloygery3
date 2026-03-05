package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.LovedByPiglinsProperty;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

public interface IDynamicPiglinLoved
{
    default boolean isLovedByPiglins(ItemStack dynamicGearStack)
    {
        return dynamicGearStack.is(ItemTags.PIGLIN_LOVED) || LovedByPiglinsProperty.compute(dynamicGearStack);
    }
}
