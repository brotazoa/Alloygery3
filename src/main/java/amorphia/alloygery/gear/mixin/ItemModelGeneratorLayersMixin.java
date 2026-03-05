package amorphia.alloygery.gear.mixin;

import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorLayersMixin
{
    @Shadow @Final
    public static List<String> LAYERS;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci)
    {
        /*
        Alloygery needs layer5 to render its dynamic items
        Forgero also mixes into here and adds up to layer10.
        Forgero checks the underlying list for exactly 5 layers, so if Alloygery adds a 6th layer first, Forgero's mixin will not trigger
        Therefore, in the spirit of compatibility Alloygery will also add up to layer10
         */
        if (LAYERS.size() < 6)
        {
            LAYERS.addAll(List.of("layer5", "layer6", "layer7", "layer8", "layer9", "layer10"));
        }
    }
}
