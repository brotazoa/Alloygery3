package amorphia.alloygery.gear.client;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.ArmorStyles;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.ImprovementTypes;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class DynamicArmorHumanoidArmorLayerRenderer
{
    public static <T extends LivingEntity, A extends HumanoidModel<T>> void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, A model, EquipmentSlot slot, TextureAtlas atlas)
    {
        if(armorStack == null || armorStack.isEmpty() || !(armorStack.getItem() instanceof IDynamicArmor))
            return;

        if (!NBTHelper.hasAlloygeryTag(armorStack))
        {
            armorStack = armorStack.copy();
            armorStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(armorStack.getItem().getDefaultInstance()));
        }

        if (NBTHelper.hasPartTag(armorStack, PartTypes.ARMOR_BASE))
        {
			final AlloygeryMaterial baseMaterial = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_BASE, armorStack);
			final ArmorStyles baseStyle = NBTHelper.getArmorStyleFromStack(armorStack, PartTypes.ARMOR_BASE);
			final ResourceLocation textureLocation = Alloygery.asResource(
					"models/armor/base_" + getArmorTypeFromEquipmentSlot(slot).getName() + "_" + baseStyle.getName() + "_" + MaterialHelper.getSimpleName(baseMaterial)
			);
			DynamicArmorHumanoidArmorLayerRenderer.renderModelPartWithTexture(model, armorStack, PartTypes.ARMOR_BASE, textureLocation, atlas, poseStack, buffer, packedLight);
        }

		if(NBTHelper.hasPartTag(armorStack, PartTypes.ARMOR_UPGRADE))
		{
			final AlloygeryMaterial material = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_UPGRADE, armorStack);
			final ResourceLocation textureLocation = Alloygery.asResource(
					"models/armor/upgrade_" + getArmorTypeFromEquipmentSlot(slot).getName() + "_" + MaterialHelper.getSimpleName(material)
			);
			DynamicArmorHumanoidArmorLayerRenderer.renderModelPartWithTexture(model, armorStack, PartTypes.ARMOR_UPGRADE, textureLocation, atlas, poseStack, buffer, packedLight);
		}

        if (NBTHelper.hasPartTag(armorStack, PartTypes.ARMOR_PLATE))
        {
			final AlloygeryMaterial plateMaterial = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_PLATE, armorStack);
			final ArmorStyles plateStyle = NBTHelper.getArmorStyleFromStack(armorStack, PartTypes.ARMOR_PLATE);
			final ResourceLocation textureLocation = Alloygery.asResource(
					"models/armor/plate_" + getArmorTypeFromEquipmentSlot(slot).getName() + "_" + plateStyle.getName() + "_" + MaterialHelper.getSimpleName(plateMaterial)
			);
			DynamicArmorHumanoidArmorLayerRenderer.renderModelPartWithTexture(model, armorStack, PartTypes.ARMOR_PLATE, textureLocation, atlas, poseStack, buffer, packedLight);
        }

        if (NBTHelper.hasPartTag(armorStack, PartTypes.ARMOR_IMPROVEMENT))
        {
			final AlloygeryMaterial material = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_IMPROVEMENT, armorStack);
			final ImprovementTypes improvement = NBTHelper.getImprovementTypeFromTag(NBTHelper.getPartTagFromItemStack(armorStack, PartTypes.ARMOR_IMPROVEMENT));
			final ResourceLocation textureLocation = Alloygery.asResource(
					"models/armor/improvement_" + getArmorTypeFromEquipmentSlot(slot).getName() + "_" + improvement.getName() + "_" + MaterialHelper.getSimpleName(material)
			);
			DynamicArmorHumanoidArmorLayerRenderer.renderModelPartWithTexture(model, armorStack, PartTypes.ARMOR_IMPROVEMENT, textureLocation, atlas, poseStack, buffer, packedLight);
        }
    }

	private static <T extends LivingEntity, A extends HumanoidModel<T>> void renderModelPartWithTexture(A model, ItemStack armorStack, PartTypes part, ResourceLocation textureLocation,
			TextureAtlas atlas, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
	{
		final TextureAtlasSprite sprite = atlas.getSprite(textureLocation);
		final VertexConsumer vertexConsumer = sprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet()));
		if (NBTHelper.partHasDyeColor(armorStack, part))
		{
			final int color = NBTHelper.getPartDyeColorFromStack(armorStack, part);
			final float red = (float) (color >> 16 & 255) / 255.0f;
			final float green = (float) (color >> 8 & 255) / 255.0f;
			final float blue = (float) (color & 255) / 255.0f;
			model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
		}
		else
		{
			model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

    private static ArmorItem.Type getArmorTypeFromEquipmentSlot(EquipmentSlot slot)
    {
        return switch (slot)
        {
            case HEAD -> ArmorItem.Type.HELMET;
            case CHEST -> ArmorItem.Type.CHESTPLATE;
            case LEGS -> ArmorItem.Type.LEGGINGS;
            case FEET -> ArmorItem.Type.BOOTS;
            default -> throw new EnumConstantNotPresentException(ArmorItem.Type.class, slot.getName());
        };
    }
}
