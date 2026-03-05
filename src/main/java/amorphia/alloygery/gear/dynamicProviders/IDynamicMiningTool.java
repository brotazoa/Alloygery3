package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.property.MiningLevelProperty;
import amorphia.alloygery.gear.property.MiningSpeedProperty;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface IDynamicMiningTool
{
    default int getMiningLevel(ItemStack dynamicGearStack)
    {
        return MiningLevelProperty.compute(dynamicGearStack);
    }

    default float getMiningSpeed(ItemStack dynamicGearStack)
    {
        return MiningSpeedProperty.compute(dynamicGearStack);
    }

    boolean isEffectiveOn(BlockState targetBlockState, ItemStack dynamicGearStack);

    default float getDynamicMiningSpeed(BlockState targetBlockState, ItemStack dynamicGearStack)
    {
        return isEffectiveOn(targetBlockState, dynamicGearStack) ? getMiningSpeed(dynamicGearStack) : 1.0f;
    }

    default boolean isCorrectToolForDrops(BlockState targetBlockState, ItemStack dynamicGearStack)
    {
        final int miningLevel = getMiningLevel(dynamicGearStack);
        return isEffectiveOn(targetBlockState, dynamicGearStack) && miningLevel >= MiningLevelManager.getRequiredMiningLevel(targetBlockState);
    }
}
