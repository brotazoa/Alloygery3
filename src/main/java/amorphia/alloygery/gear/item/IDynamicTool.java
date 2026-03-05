package amorphia.alloygery.gear.item;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.attribute.GearAttributeAppliers;
import amorphia.alloygery.gear.client.DynamicGearItemModelPredicateProvider;
import amorphia.alloygery.gear.dynamicProviders.IDynamicMiningTool;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.Property;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IDynamicTool extends IDynamicGear, IDynamicMiningTool, DynamicGearItemModelPredicateProvider.ICanHaveToolBinding, DynamicGearItemModelPredicateProvider.ICanBeImproved
{
    TagKey<Item> IMPROVABLE_TOOL = TagKey.create(Registries.ITEM, Alloygery.asResource("improvable_tool"));

    @Override
    default PartTypes[] getParts()
    {
        return new PartTypes[]{
                PartTypes.TOOL_HEAD,
                PartTypes.TOOL_HANDLE,
                PartTypes.TOOL_BINDING,
                PartTypes.TOOL_IMPROVEMENT,
				PartTypes.TOOL_UPGRADE
        };
    }

    @Override
    default PartTypes getPrimaryPart()
    {
        return PartTypes.TOOL_HEAD;
    }

    @Override
    default int getMaterialColor(ItemStack stack, int tintIndex)
    {
        if(stack == null || stack.isEmpty() || !(stack.getItem() instanceof IDynamicTool))
            return -1;

        if(!NBTHelper.hasAlloygeryTag(stack))
        {
            stack = stack.copy();
            stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(stack.getItem().getDefaultInstance()));
        }

		/*
		0: handle
		1: head
		2: binding
		3: upgrade
		4: improvement
		 */

        return switch (tintIndex)
        {
			case 0 -> getColorForPart(stack, PartTypes.TOOL_HANDLE);
			// case 1 -> -1;
			case 2 -> NBTHelper.hasPartTag(stack, PartTypes.TOOL_BINDING) ? getColorForPart(stack, PartTypes.TOOL_BINDING) : getColorForPart(stack, PartTypes.TOOL_UPGRADE);
			case 3 -> NBTHelper.hasPartTag(stack, PartTypes.TOOL_UPGRADE) ? getColorForPart(stack, PartTypes.TOOL_UPGRADE) : getColorForPart(stack, PartTypes.TOOL_IMPROVEMENT);
			case 4 -> getColorForPart(stack, PartTypes.TOOL_IMPROVEMENT);
            default -> -1;
        };
    }

    @Override
    default float calculateImprovementTypePredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed)
    {
        return NBTHelper.hasPartTag(stack, PartTypes.TOOL_IMPROVEMENT) ? NBTHelper.getImprovementTypeFromTag(NBTHelper.getPartTagFromItemStack(stack, PartTypes.TOOL_IMPROVEMENT)).getTypeFloat() : 0.0f;
    }

    @Override
    default Multimap<Attribute, AttributeModifier> getDynamicAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        if (slot == EquipmentSlot.MAINHAND)
        {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            List<Attribute> attributesOnStack = Lists.newArrayList();
            for(AttributeProperty property : PropertyHelper.getPropertiesOfTypeFromItemStack(AttributeProperty.class, stack))
            {
                if(!attributesOnStack.contains(property.getTargetAttribute()))
                {
                    attributesOnStack.add(property.getTargetAttribute());
                }
            }
            attributesOnStack.forEach(attribute -> GearAttributeAppliers.applyAttribute(attribute, stack, builder));

            return builder.build();
        }
        else return IDynamicGear.super.getDynamicAttributeModifiers(slot, stack);
    }

	void addBaseToolProperties(List<Property> properties);

    ToolTypes getToolType();
}
