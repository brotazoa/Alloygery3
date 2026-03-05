package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.dynamicProviders.IDynamicFireproof;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityFireproofMixin
{
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    public void isDynamicFireImmune(CallbackInfoReturnable<Boolean> cir)
    {
		ItemStack stack = getItem();
		if(stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
			stack = VanillaItemConverter.convert(stack);

        if(stack.getItem() instanceof IDynamicFireproof dynamicFireproof)
        {
            cir.setReturnValue(dynamicFireproof.isFireproof(stack));
        }
    }
}
