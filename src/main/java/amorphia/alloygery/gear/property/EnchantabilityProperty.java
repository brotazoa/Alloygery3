package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class EnchantabilityProperty extends Property
{
    public static final Codec<EnchantabilityProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(EnchantabilityProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(EnchantabilityProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(EnchantabilityProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(EnchantabilityProperty::applyWhenBroken)
    ).apply(instance, EnchantabilityProperty::new));

    public static EnchantabilityProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new EnchantabilityProperty(partType, operation, value);
    }

    public static EnchantabilityProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new EnchantabilityProperty(partType, operation, value, applyWhenBroken);
    }

    public static int compute(ItemStack stack)
    {
        return PropertyHelper.computeIntegerPropertyValueRoundDown(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(EnchantabilityProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                ),
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
        );
    }

    public EnchantabilityProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public EnchantabilityProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value,applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        EnchantabilityProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.ENCHANTABILITY;
    }
}
