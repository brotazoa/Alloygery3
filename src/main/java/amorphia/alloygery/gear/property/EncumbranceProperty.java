package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EncumbranceProperty extends Property
{
	public static final Codec<EncumbranceProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PartTypes.CODEC.fieldOf("part_type").forGetter(EncumbranceProperty::getPartType),
			PropertyOperation.CODEC.fieldOf("operation").forGetter(EncumbranceProperty::getOperation),
			Codec.FLOAT.fieldOf("value").forGetter(EncumbranceProperty::getValue),
			Codec.BOOL.fieldOf("apply_when_broken").forGetter(EncumbranceProperty::applyWhenBroken)
	).apply(instance, EncumbranceProperty::new));

	public static EncumbranceProperty of(PartTypes partType, PropertyOperation operation, float value)
	{
		return new EncumbranceProperty(partType, operation, value);
	}

	public static EncumbranceProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
	{
		return new EncumbranceProperty(partType, operation, value, applyWhenBroken);
	}

	public static float compute(ItemStack stack)
	{
		return PropertyHelper.computeFloatPropertyValue(PropertyHelper.brokenFilter(
				PropertyHelper.getPropertiesOfTypeFromItemStack(EncumbranceProperty.class, stack), PropertyHelper.isBroken(stack)
		), 0.0f, Float.MAX_VALUE);
	}

	public EncumbranceProperty(PartTypes partType, PropertyOperation operation, float value)
	{
		super(partType, operation, value);
	}

	public EncumbranceProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
	{
		super(partType, operation, value, applyWhenBroken);
	}

	@Override
	public boolean equalsIgnoreValue(Property other)
	{
		if(other == null || other.getClass() != getClass()) return false;
		EncumbranceProperty property = getClass().cast(other);
		return partType == property.partType && operation == property.operation;
	}

	@Override
	public PropertyType<?> getPropertyType()
	{
		return PropertyType.ENCUMBRANCE;
	}
}
