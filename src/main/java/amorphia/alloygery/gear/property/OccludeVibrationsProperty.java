package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class OccludeVibrationsProperty extends Property
{
	public static final Codec<OccludeVibrationsProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PartTypes.CODEC.fieldOf("part_type").forGetter(OccludeVibrationsProperty::getPartType),
			PropertyOperation.CODEC.fieldOf("operation").forGetter(OccludeVibrationsProperty::getOperation),
			Codec.FLOAT.fieldOf("value").forGetter(OccludeVibrationsProperty::getValue),
			Codec.BOOL.fieldOf("apply_when_broken").forGetter(OccludeVibrationsProperty::applyWhenBroken)
	).apply(instance, OccludeVibrationsProperty::new));

	public static OccludeVibrationsProperty of(PartTypes partType, PropertyOperation operation, float value)
	{
		return new OccludeVibrationsProperty(partType, operation, value);
	}

	public static OccludeVibrationsProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
	{
		return new OccludeVibrationsProperty(partType, operation, value, applyWhenBroken);
	}

	public static boolean compute(ItemStack stack)
	{
		return PropertyHelper.computeBooleanPropertyValue(
				PropertyHelper.brokenFilter(
						PropertyHelper.getPropertiesOfTypeFromItemStack(OccludeVibrationsProperty.class, stack),
						PropertyHelper.isBroken(stack)
				)
		);
	}

	public OccludeVibrationsProperty(PartTypes partType, PropertyOperation operation, float value)
	{
		this(partType, operation, value, false);
	}

	public OccludeVibrationsProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
	{
		super(partType, operation, value, applyWhenBroken);
	}

	@Override
	public boolean equalsIgnoreValue(Property other)
	{
		if(other == null || other.getClass() != getClass()) return false;
		OccludeVibrationsProperty property = getClass().cast(other);
		return partType == property.partType && operation == property.operation;
	}

	@Override
	public PropertyType<?> getPropertyType()
	{
		return PropertyType.OCCLUDE_VIBRATIONS;
	}
}
