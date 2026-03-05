package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class MobilityProperty extends Property
{
	public static final Codec<MobilityProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PartTypes.CODEC.fieldOf("part_type").forGetter(MobilityProperty::getPartType),
			PropertyOperation.CODEC.fieldOf("operation").forGetter(MobilityProperty::getOperation),
			Codec.FLOAT.fieldOf("value").forGetter(MobilityProperty::getValue),
			Codec.BOOL.fieldOf("apply_when_broken").forGetter(MobilityProperty::applyWhenBroken)
	).apply(instance, MobilityProperty::new));

	public static MobilityProperty of(PartTypes partType, PropertyOperation operation, float value)
	{
		return new MobilityProperty(partType, operation, value);
	}

	public static MobilityProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
	{
		return new MobilityProperty(partType, operation, value, applyWhenBroken);
	}

	public static float compute(ItemStack stack)
	{
		return PropertyHelper.computeFloatPropertyValue(PropertyHelper.brokenFilter(
				PropertyHelper.getPropertiesOfTypeFromItemStack(MobilityProperty.class, stack), PropertyHelper.isBroken(stack)
		), 0.0f, Float.MAX_VALUE);
	}

	public MobilityProperty(PartTypes partType, PropertyOperation operation, float value)
	{
		super(partType, operation, value);
	}

	public MobilityProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
	{
		super(partType, operation, value, applyWhenBroken);
	}

	@Override
	public boolean equalsIgnoreValue(Property other)
	{
		if(other == null || other.getClass() != getClass()) return false;
		MobilityProperty property = getClass().cast(other);
		return partType == property.partType && operation == property.operation;
	}

	@Override
	public PropertyType<?> getPropertyType()
	{
		return PropertyType.MOBILITY;
	}
}
