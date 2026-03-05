package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.property.OccludeVibrationsProperty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityArmorOccludesVibrationsMixin
{
	@Inject(method = "isSteppingCarefully", at = @At("HEAD"), cancellable = true)
	private void doesDynamicArmorOccludeVibrations(CallbackInfoReturnable<Boolean> cir)
	{
		Entity entity = (Entity) (Object) this;
		if (entity instanceof Player player)
		{
			ItemStack feetStack = player.getItemBySlot(EquipmentSlot.FEET);

			if(feetStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
				feetStack = VanillaItemConverter.convert(feetStack);

			cir.setReturnValue(OccludeVibrationsProperty.compute(feetStack));
		}
	}
}
