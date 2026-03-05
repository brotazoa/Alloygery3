package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.dynamicProviders.IDynamicFreezeProtection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public abstract class EntityFreezeTicksMixin
{
    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Inject(method = "getTicksRequiredToFreeze", at = @At("HEAD"), cancellable = true)
    private void getDynamicGearTicksRequiredToFreeze(CallbackInfoReturnable<Integer> cir)
    {
        AtomicInteger extraTicks = new AtomicInteger();
        Iterable<ItemStack> iterable = getArmorSlots();
        if (iterable != null)
        {
            iterable.forEach(stack -> {
				ItemStack gearStack = stack;
				if(gearStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
					gearStack = VanillaItemConverter.convert(gearStack);

                if(gearStack.getItem() instanceof IDynamicFreezeProtection freezeProtectionItem)
                    extraTicks.addAndGet(freezeProtectionItem.getExtraFreezeProtectionTicks(gearStack));
            });
        }
        cir.setReturnValue(140 + extraTicks.get());
    }
}
