package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.dynamicProviders.IDynamicFireProtection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public abstract class EntityFireImmuneTicksMixin
{
    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Inject(method = "getFireImmuneTicks", at = @At("HEAD"), cancellable = true)
    private void getDynamicGearFireImmuneTicks(CallbackInfoReturnable<Integer> cir)
    {
        AtomicInteger extraTicks = new AtomicInteger();
        Iterable<ItemStack> iterable = getArmorSlots();
        if(iterable != null)
        {
            iterable.forEach(stack -> {
				ItemStack gearStack = stack;
				if(gearStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
					gearStack = VanillaItemConverter.convert(gearStack);

				if(gearStack.getItem() instanceof IDynamicFireProtection fireProtectionItem)
					extraTicks.addAndGet(fireProtectionItem.getExtraFireImmuneTicks(gearStack));
            });
        }
        cir.setReturnValue(cir.getReturnValueI() + extraTicks.get());
    }
}
