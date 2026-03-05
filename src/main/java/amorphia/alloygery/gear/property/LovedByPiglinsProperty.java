package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class LovedByPiglinsProperty extends Property
{
    public static final Codec<LovedByPiglinsProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(LovedByPiglinsProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(LovedByPiglinsProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(LovedByPiglinsProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(LovedByPiglinsProperty::applyWhenBroken)
    ).apply(instance, LovedByPiglinsProperty::new));

    public static LovedByPiglinsProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new LovedByPiglinsProperty(partType, operation, value);
    }

    public static LovedByPiglinsProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new LovedByPiglinsProperty(partType, operation, value, applyWhenBroken);
    }

    public static boolean compute(ItemStack stack)
    {
        return PropertyHelper.computeBooleanPropertyValue(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(LovedByPiglinsProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                )
        );
    }

    public LovedByPiglinsProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public LovedByPiglinsProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        LovedByPiglinsProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.LOVED_BY_PIGLINS;
    }
}
