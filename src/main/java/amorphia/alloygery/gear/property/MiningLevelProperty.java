package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public class MiningLevelProperty extends Property
{
    public static final Codec<MiningLevelProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(MiningLevelProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(MiningLevelProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(MiningLevelProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(MiningLevelProperty::applyWhenBroken)
    ).apply(instance, MiningLevelProperty::new));

    public static MiningLevelProperty of(PartTypes partTypes, PropertyOperation operation, float value)
    {
        return new MiningLevelProperty(partTypes, operation, value);
    }

    public static MiningLevelProperty of(PartTypes partTypes, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new MiningLevelProperty(partTypes, operation, value, applyWhenBroken);
    }

    public static int compute(ItemStack stack)
    {
        return PropertyHelper.computeIntegerPropertyValueRoundDown(
                PropertyHelper.brokenFilter(
                        PropertyHelper.getPropertiesOfTypeFromItemStack(MiningLevelProperty.class, stack),
                        PropertyHelper.isBroken(stack)
                ),
                -1,
                Integer.MAX_VALUE
        );
    }

    public MiningLevelProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public MiningLevelProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        MiningLevelProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.MINING_LEVEL;
    }
}
