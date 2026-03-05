package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.client.DynamicArmorHumanoidArmorLayerRenderer;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.item.IDynamicArmor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class DynamicArmorHumanoidArmorLayerMixin <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M>
{
    @Shadow
    protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    @Shadow
    @Final
    private TextureAtlas armorTrimAtlas;

    @Shadow
    protected abstract void renderTrim(ArmorMaterial armorMaterial, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorTrim trim, A model, boolean innerTexture);

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Shadow
    protected abstract void renderGlint(PoseStack poseStack, MultiBufferSource buffer, int packedLight, A model);

    private DynamicArmorHumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer)
    {
        super(renderer);
    }

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void dynamicArmor_renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci)
    {
        ItemStack itemStack = livingEntity.getItemBySlot(slot);

		if(itemStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
		{
			itemStack = VanillaItemConverter.convert(itemStack);
		}

        final Item item = itemStack.getItem();
        if (item instanceof IDynamicArmor)
        {
            if (item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == slot)
            {
                getParentModel().copyPropertiesTo(model);
                setPartVisibility(model, slot);
                DynamicArmorHumanoidArmorLayerRenderer.renderModel(poseStack, buffer, packedLight, itemStack, model, slot, armorTrimAtlas);

                final boolean innerTexture = usesInnerModel(slot);
                ArmorTrim.getTrim(livingEntity.level().registryAccess(), itemStack).ifPresent(armorTrim -> {
                    this.renderTrim(armorItem.getMaterial(), poseStack, buffer, packedLight, armorTrim, model, innerTexture);
                });

                if (itemStack.hasFoil())
                {
                    renderGlint(poseStack, buffer, packedLight, model);
                }

                ci.cancel();
            }
        }
    }
}
