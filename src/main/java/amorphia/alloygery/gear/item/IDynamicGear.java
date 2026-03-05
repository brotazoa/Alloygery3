package amorphia.alloygery.gear.item;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.client.DynamicGearClientReloadListener;
import amorphia.alloygery.gear.client.DynamicGearItemModelPredicateProvider;
import amorphia.alloygery.gear.dynamicProviders.*;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.NBTHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IDynamicGear extends IDynamicAttributeModifiers, IDynamicEnchantability, IDynamicFireproof, IDynamicMaxDamage, IDynamicPiglinLoved, IMadeFromParts,
        DynamicGearItemModelPredicateProvider.ICanBeUpgraded, DynamicGearClientReloadListener.IDynamicClientTintColor
{
	TagKey<Item> UPGRADEABLE_EQUIPMENT = TagKey.create(Registries.ITEM, Alloygery.asResource("upgradeable_equipment"));

    default int getItemBarStep(ItemStack stack)
    {
        return Math.round(13.0f - (float) stack.getDamageValue() * 13.0f / (float) getMaxDamage(stack));
    }

    default int getItemBarColor(ItemStack stack)
    {
        final float durability = (float) getMaxDamage(stack);
        final float f = Math.max(0.0f, (durability - stack.getDamageValue()) / durability);
        return Mth.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

	default int getColorForPart(ItemStack stack, PartTypes part)
	{
		return NBTHelper.partHasDyeColor(stack, part) ? NBTHelper.getPartDyeColorFromStack(stack, part) : getMaterialColorFromMaterial(MaterialHelper.getMaterialForPartType(part, stack));
	}
}
