package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;

public abstract class Property
{
    public static final Codec<Property> CODEC = PropertyType.REGISTRY.byNameCodec().dispatch("property_type", Property::getPropertyType, PropertyType::codec);

    protected final PartTypes partType;
    protected final PropertyOperation operation;
    protected final float value;
    protected final boolean applyWhenBroken;

    public Property(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public Property(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        this.partType = partType;
        this.operation = operation;
        this.value = value;
        this.applyWhenBroken = applyWhenBroken;
    }

    public PartTypes getPartType()
    {
        return partType;
    }

    public PropertyOperation getOperation()
    {
        return operation;
    }

    public float getValue()
    {
        return value;
    }

    public boolean applyWhenBroken()
    {
        return applyWhenBroken;
    }

    public abstract boolean equalsIgnoreValue(Property other);

    public abstract PropertyType<?> getPropertyType();
}
