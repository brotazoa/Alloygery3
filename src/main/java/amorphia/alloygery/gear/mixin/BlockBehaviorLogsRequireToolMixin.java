package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.datagen.LogsRequireToolProviders;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviorLogsRequireToolMixin
{
	@Inject(method = "requiresCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
	private void logBlocksRequireToolForDrops(CallbackInfoReturnable<Boolean> cir)
	{
		BlockState blockState = (BlockState) (Object) this;
		if(blockState.is(LogsRequireToolProviders.LogsRequireToolTagProvider.LOG_BLOCKS))
		{
			cir.setReturnValue(true);
		}
	}
}
