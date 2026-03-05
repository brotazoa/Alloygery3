package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class MiningSpeedProperty extends Property
{
    public static final Codec<MiningSpeedProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(MiningSpeedProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(MiningSpeedProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(MiningSpeedProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(MiningSpeedProperty::applyWhenBroken)
    ).apply(instance, MiningSpeedProperty::new));

    public static MiningSpeedProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new MiningSpeedProperty(partType, operation, value);
    }

    public static MiningSpeedProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new MiningSpeedProperty(partType, operation, value, applyWhenBroken);
    }

    public static float compute(ItemStack stack)
    {
        return PropertyHelper.computeFloatPropertyValue(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(MiningSpeedProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                ),
                1.0f,
                Float.MAX_VALUE
        );
    }

    public MiningSpeedProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public MiningSpeedProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        MiningSpeedProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.MINING_SPEED;
    }
}
