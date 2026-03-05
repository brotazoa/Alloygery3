package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.dynamicProviders.IDynamicPiglinLoved;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAiDynamicPiglinLovedMixin
{
    @Inject(method = "isLovedItem", at = @At("RETURN"), cancellable = true)
    private static void isDynamicItemLoved(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir)
    {
        if(itemStack.getItem() instanceof IDynamicPiglinLoved dynamicPiglinLoved)
        {
            cir.setReturnValue(dynamicPiglinLoved.isLovedByPiglins(itemStack));
        }
    }
}
