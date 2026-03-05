package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class FireProtectionProperty extends Property
{
    public static final Codec<FireProtectionProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(FireProtectionProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(FireProtectionProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(FireProtectionProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(FireProtectionProperty::applyWhenBroken)
    ).apply(instance, FireProtectionProperty::new));

    public static FireProtectionProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new FireProtectionProperty(partType, operation, value);
    }

    public static FireProtectionProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new FireProtectionProperty(partType, operation, value, applyWhenBroken);
    }

    public static int compute(ItemStack stack)
    {
        return PropertyHelper.computeIntegerPropertyValueRoundUp(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(FireProtectionProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                ), 0, Integer.MAX_VALUE
        );
    }

    public FireProtectionProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public FireProtectionProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        FireProtectionProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.FIRE_PROTECTION;
    }
}
