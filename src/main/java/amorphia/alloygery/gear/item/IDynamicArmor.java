package amorphia.alloygery.gear.item;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.attribute.GearAttributeAppliers;
import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import amorphia.alloygery.gear.client.DynamicGearItemModelPredicateProvider;
import amorphia.alloygery.gear.dynamicProviders.IDynamicFireProtection;
import amorphia.alloygery.gear.dynamicProviders.IDynamicFreezeProtection;
import amorphia.alloygery.gear.dynamicProviders.IDynamicWalkOnPowderedSnow;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.PropertyHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IDynamicArmor extends IDynamicGear, IDynamicFireProtection, IDynamicFreezeProtection, IDynamicWalkOnPowderedSnow, DynamicGearItemModelPredicateProvider.ICanBeImproved,
		DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling
{
	TagKey<Item> IMPROVABLE_ARMOR = TagKey.create(Registries.ITEM, Alloygery.asResource("improvable_armor"));

	@Override
    default Multimap<Attribute, AttributeModifier> getDynamicAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        if (stack.getItem() instanceof ArmorItem armorItem && armorItem.getType().getSlot() == slot)
        {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            List<Attribute> attributesOnStack = Lists.newArrayList();
            for (AttributeProperty property : PropertyHelper.getPropertiesOfTypeFromItemStack(AttributeProperty.class, stack))
            {
                if (!attributesOnStack.contains(property.getTargetAttribute()))
                {
                    attributesOnStack.add(property.getTargetAttribute());
                }
            }
            attributesOnStack.forEach(attribute -> GearAttributeAppliers.applyAttribute(attribute, stack, builder));

            return builder.build();
        }
        else return IDynamicGear.super.getDynamicAttributeModifiers(slot, stack);
    }

    @Override
    default PartTypes[] getParts()
    {
        return new PartTypes[] {
                PartTypes.ARMOR_BASE,
                PartTypes.ARMOR_PLATE,
                PartTypes.ARMOR_IMPROVEMENT,
				PartTypes.ARMOR_UPGRADE
        };
    }

    @Override
    default PartTypes getPrimaryPart()
    {
        return PartTypes.ARMOR_BASE;
    }

    @Override
    default int getMaterialColor(ItemStack stack, int tintIndex)
    {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof IDynamicArmor))
            return -1;

        if (!NBTHelper.hasAlloygeryTag(stack))
        {
            stack = stack.copy();
            stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(stack.getItem().getDefaultInstance()));
        }

        return switch (tintIndex)
        {
			/*
			1: base
			2: upgrade
			3: plate
			4: improvement
			5: trim
			 */

			// case 0 -> -1;
			case 1 -> getColorForPart(stack, PartTypes.ARMOR_BASE);
			case 2 -> NBTHelper.isUpgraded(stack)
					? getColorForPart(stack, PartTypes.ARMOR_UPGRADE)
					: NBTHelper.hasPartTag(stack, PartTypes.ARMOR_PLATE)
							? NBTHelper.partHasDyeColor(stack, PartTypes.ARMOR_PLATE) ? NBTHelper.getPartDyeColorFromStack(stack, PartTypes.ARMOR_PLATE) : -1
							: NBTHelper.hasPartTag(stack, PartTypes.ARMOR_IMPROVEMENT) ? getColorForPart(stack, PartTypes.ARMOR_IMPROVEMENT) : AlloygeryMaterialColors.getFromTrim(stack).color();

			case 3 -> NBTHelper.isUpgraded(stack)
					? NBTHelper.hasPartTag(stack, PartTypes.ARMOR_PLATE)
							? NBTHelper.partHasDyeColor(stack, PartTypes.ARMOR_PLATE) ? NBTHelper.getPartDyeColorFromStack(stack, PartTypes.ARMOR_PLATE) : -1
							: NBTHelper.hasPartTag(stack, PartTypes.ARMOR_IMPROVEMENT) ? getColorForPart(stack, PartTypes.ARMOR_IMPROVEMENT) : AlloygeryMaterialColors.getFromTrim(stack).color()
					: NBTHelper.hasPartTag(stack, PartTypes.ARMOR_IMPROVEMENT) ? getColorForPart(stack, PartTypes.ARMOR_IMPROVEMENT) : AlloygeryMaterialColors.getFromTrim(stack).color();

			case 4 -> NBTHelper.isUpgraded(stack)
					? NBTHelper.hasPartTag(stack, PartTypes.ARMOR_PLATE)
							? NBTHelper.hasPartTag(stack, PartTypes.ARMOR_IMPROVEMENT) ? getColorForPart(stack, PartTypes.ARMOR_IMPROVEMENT) : AlloygeryMaterialColors.getFromTrim(stack).color()
							: AlloygeryMaterialColors.getFromTrim(stack).color()
					: AlloygeryMaterialColors.getFromTrim(stack).color();

			case 5 -> AlloygeryMaterialColors.getFromTrim(stack).color();
            default -> -1;
        };
    }

    @Override
    default float calculateImprovementTypePredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed)
    {
        return NBTHelper.hasPartTag(stack, PartTypes.ARMOR_IMPROVEMENT) ? NBTHelper.getImprovementTypeFromTag(NBTHelper.getPartTagFromItemStack(stack, PartTypes.ARMOR_IMPROVEMENT)).getTypeFloat() : 0.0f;
    }
}
