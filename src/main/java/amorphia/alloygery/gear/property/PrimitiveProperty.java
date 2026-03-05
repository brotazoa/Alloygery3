package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class PrimitiveProperty extends Property
{
    public static final Codec<PrimitiveProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(PrimitiveProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(PrimitiveProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(PrimitiveProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(PrimitiveProperty::applyWhenBroken)
    ).apply(instance, PrimitiveProperty::new));

    public static PrimitiveProperty of()
    {
        return new PrimitiveProperty();
    }

    public static boolean compute(ItemStack stack)
    {
        return PropertyHelper.computeBooleanPropertyValue(PropertyHelper.getPropertiesOfTypeFromItemStack(PrimitiveProperty.class, stack));
    }

    public PrimitiveProperty()
    {
        this(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 1.0f);
    }

    public PrimitiveProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, true);
    }

    public PrimitiveProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        PrimitiveProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.PRIMITIVE;
    }
}
