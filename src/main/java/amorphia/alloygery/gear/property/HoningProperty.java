package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class HoningProperty extends Property
{
    public static final Codec<HoningProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(HoningProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(HoningProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(HoningProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(HoningProperty::applyWhenBroken)
    ).apply(instance, HoningProperty::new));

    public static HoningProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new HoningProperty(partType, operation, value);
    }

    public static HoningProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new HoningProperty(partType, operation, value, applyWhenBroken);
    }

    public static float compute(ItemStack stack)
    {
        return PropertyHelper.computeFloatPropertyValue(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(HoningProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                ),
                Float.MIN_VALUE,
                Float.MAX_VALUE
        );
    }

    public HoningProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public HoningProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        HoningProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.HONING;
    }
}
