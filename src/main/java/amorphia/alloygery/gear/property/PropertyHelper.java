package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.IDynamicGear;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.item.ImprovementTypes;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.item.PartTypes;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PropertyHelper
{
    public static List<Property> getPropertiesFromItemStack(ItemStack stack)
    {
        List<Property> properties = Lists.newArrayList();

		if(stack.getItem() instanceof IDynamicGear && !NBTHelper.hasAlloygeryTag(stack))
		{
			stack = stack.copy();
			stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(stack.getItem().getDefaultInstance()));
		}

        if (stack.getItem() instanceof IDynamicTool dynamicTool)
        {
            dynamicTool.addBaseToolProperties(properties);
        }

        properties.addAll(NBTHelper.getNBTPropertiesFromItemStack(stack));

        List<PartTypes> parts = NBTHelper.getPartTypesListFromItemStack(stack);
        for(PartTypes type : parts)
        {
			List<Property> materialProperties = MaterialHelper.getMaterialForPartType(type, stack).getPropertiesByPart(type);

			if(NBTHelper.partHasArmorStyle(stack, type))
				NBTHelper.getArmorStyleFromStack(stack, type).addStyleProperties(properties);

			if(PartTypes.isImprovement(type))
			{
				ImprovementTypes improvement = NBTHelper.getImprovementType(stack);
				if(improvement != null)
				{
					improvement.addImprovementProperties(properties);
				}
			}

			properties.addAll(materialProperties);
        }

        return properties;
    }

    public static List<Property> getPropertiesByPartTypeFromItemStack(PartTypes partType, ItemStack stack)
    {
        return getPropertiesFromItemStack(stack).stream().filter(p -> p.getPartType().equals(partType)).toList();
    }

    public static <P extends Property> List<P> getPropertiesOfTypeFromItemStack(Class<P> propertyClass, ItemStack stack)
    {
        return getPropertiesOfType(propertyClass, getPropertiesFromItemStack(stack));
    }

    public static <P extends Property> List<P> getPropertiesOfType(Class<P> propertyClass, List<Property> properties)
    {
        return properties.stream().filter(p -> p.getClass().equals(propertyClass)).map(propertyClass::cast).toList();
    }

    public static <P extends Property> List<P> brokenFilter(List<P> properties, boolean isBroken)
    {
        return properties.stream().filter(p -> p.applyWhenBroken() || p.applyWhenBroken() == isBroken).toList();
    }

    public static boolean isBroken(ItemStack stack)
    {
        return stack.getDamageValue() >= stack.getMaxDamage();
    }

    public static <P extends Property> float computePropertyValue(List<P> properties)
    {
        float base = 0.0f;
        for (P property : properties.stream().filter(p -> p.operation.equals(PropertyOperation.BASE)).toList())
            base += property.value;

		float multiplyBase = 0.0f;
		float adjustedBaseAbs = base == 0.0f ? 1.0f : Math.abs(base);
		for(P property : properties.stream().filter(p -> p.operation.equals(PropertyOperation.MULTIPLY_BASE)).toList())
			multiplyBase += adjustedBaseAbs * property.value - adjustedBaseAbs;

        float addition = 0.0f;
		for(PartTypes partType : PartTypes.VALUES_CACHE)
		{
			List<P> propertiesByPart = properties.stream().filter(p -> p.partType.equals(partType)).toList();
			if(propertiesByPart.isEmpty())
				continue;

			float partAddition = 0.0f;
			for(P property : propertiesByPart.stream().filter(p -> p.operation.equals(PropertyOperation.ADDITION)).toList())
				partAddition += property.value;

			for(P property : propertiesByPart.stream().filter(p -> p.operation.equals(PropertyOperation.MULTIPLY_PART)).toList())
				partAddition *= property.value;

			addition += partAddition;
		}

		float baseTotal = base + addition + multiplyBase;
		for(P property : properties.stream().filter(p -> p.operation.equals(PropertyOperation.MULTIPLY_TOTAL)).toList())
			baseTotal = baseTotal == 0 ? property.value : baseTotal * property.value;

		return baseTotal;

//        float multiplyBase = 0.0f;
//        final float absBase = Math.abs(base);
//        final float adjustedAbsBase = absBase == 0.0f ? 1.0f : absBase;
//        for (P property : properties.stream().filter(p -> p.operation.equals(PropertyOperation.MULTIPLY_BASE)).toList())
//            multiplyBase += adjustedAbsBase * property.value - adjustedAbsBase;
//
//        final float baseTotal = base + addition + multiplyBase;
//        final float absBaseTotal = Math.abs(baseTotal);
//        final float adjustedAbsBaseTotal = absBaseTotal == 0.0f ? 1.0f : absBaseTotal;
//        float multiplyTotal = 0.0f;
//        for (P property : properties.stream().filter(p -> p.operation.equals(PropertyOperation.MULTIPLY_TOTAL)).toList())
//            multiplyTotal += adjustedAbsBaseTotal * property.value - adjustedAbsBaseTotal;
//
//        return baseTotal + multiplyTotal;
    }

    public static int computeIntegerPropertyValueRoundDown(List<? extends Property> properties, Integer min, Integer max)
    {
        return Math.min(max, Math.max(min, (int) Math.floor(computePropertyValue(properties))));
    }

    public static int computeIntegerPropertyValueRoundUp(List<? extends Property> properties, Integer min, Integer max)
    {
        return Math.min(max, Math.max(min, (int) Math.ceil(computePropertyValue(properties))));
    }

    public static float computeFloatPropertyValue(List<? extends Property> properties, Float min, Float max)
    {
        return Math.min(max, Math.max(min, computePropertyValue(properties)));
    }

    public static boolean computeBooleanPropertyValue(List<? extends Property> properties)
    {
        return computePropertyValue(properties) > 0;
    }
}
