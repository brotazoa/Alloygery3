package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.dynamicProviders.IDynamicEnchantability;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperDynamicEnchantabilityMixin
{
    @Inject(method = "getEnchantmentCost", at = @At("HEAD"))
    private static void recalculateEnchantabilityBeforeGettingEnchantmentCost(RandomSource random, int enchantNum, int power, ItemStack itemStack, CallbackInfoReturnable<Integer> cir)
    {
        if (itemStack.getItem() instanceof IDynamicEnchantability dynamicEnchantability)
        {
            dynamicEnchantability.calculateAndCacheEnchantability(itemStack);
        }
    }

    @Inject(method = "selectEnchantment", at = @At("HEAD"))
    private static void recalculateEnchantabilityBeforeSelectingEnchantments(RandomSource random, ItemStack itemStack, int level, boolean allowTreasure, CallbackInfoReturnable<List<EnchantmentInstance>> cir)
    {
        if (itemStack.getItem() instanceof IDynamicEnchantability dynamicEnchantability)
        {
            dynamicEnchantability.calculateAndCacheEnchantability(itemStack);
        }
    }
}
