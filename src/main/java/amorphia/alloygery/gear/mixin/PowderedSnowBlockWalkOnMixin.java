package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.property.WalkOnPowderedSnowProperty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderedSnowBlockWalkOnMixin
{
    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void canDynamicItemWalkOnPowderedSnow(Entity entity, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(entity instanceof LivingEntity livingEntity && WalkOnPowderedSnowProperty.compute(livingEntity.getItemBySlot(EquipmentSlot.FEET)));
    }
}
