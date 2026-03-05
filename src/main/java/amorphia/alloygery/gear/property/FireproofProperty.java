package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class FireproofProperty extends Property
{
    public static final Codec<FireproofProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(FireproofProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(FireproofProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(FireproofProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(FireproofProperty::applyWhenBroken)
    ).apply(instance, FireproofProperty::new));

    public static FireproofProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new FireproofProperty(partType, operation, value);
    }

    public static FireproofProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new FireproofProperty(partType, operation, value, applyWhenBroken);
    }

    public static boolean compute(ItemStack stack)
    {
        return PropertyHelper.computeBooleanPropertyValue(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(FireproofProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                )
        );
    }

    public FireproofProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public FireproofProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        FireproofProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.FIREPROOF;
    }
}
