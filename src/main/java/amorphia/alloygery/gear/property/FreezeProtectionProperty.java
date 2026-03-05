package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class FreezeProtectionProperty extends Property
{
    public static final Codec<FreezeProtectionProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(FreezeProtectionProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(FreezeProtectionProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(FreezeProtectionProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(FreezeProtectionProperty::applyWhenBroken)
    ).apply(instance, FreezeProtectionProperty::new));

    public static FreezeProtectionProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new FreezeProtectionProperty(partType, operation, value);
    }

    public static FreezeProtectionProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new FreezeProtectionProperty(partType, operation, value, applyWhenBroken);
    }

    public static int compute(ItemStack stack)
    {
        return PropertyHelper.computeIntegerPropertyValueRoundUp(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(FreezeProtectionProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                ), 0, Integer.MAX_VALUE
        );
    }

    public FreezeProtectionProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public FreezeProtectionProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        FreezeProtectionProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.FREEZE_PROTECTION;
    }
}
