package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.property.OccludeVibrationsProperty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.Listener.class)
public class VibrationSystemOccludeBlockBreakVibrationMixin
{
	@Inject(method = "handleGameEvent", at = @At("HEAD"), cancellable = true)
	private void doesBlockBreakCauseVibration(ServerLevel level, GameEvent event, GameEvent.Context context, Vec3 pos, CallbackInfoReturnable<Boolean> cir)
	{
		Entity entity = context.sourceEntity();
		if (event == GameEvent.BLOCK_DESTROY && entity instanceof ServerPlayer player)
		{
			ItemStack toolStack = player.getItemBySlot(EquipmentSlot.MAINHAND);

			if(toolStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
				toolStack = VanillaItemConverter.convert(toolStack);

			if(OccludeVibrationsProperty.compute(toolStack))
				cir.setReturnValue(false);
		}
	}
}
