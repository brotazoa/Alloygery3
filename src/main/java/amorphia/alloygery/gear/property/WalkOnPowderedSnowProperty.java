package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class WalkOnPowderedSnowProperty extends Property
{
    public static final Codec<WalkOnPowderedSnowProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(WalkOnPowderedSnowProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(WalkOnPowderedSnowProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(WalkOnPowderedSnowProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(WalkOnPowderedSnowProperty::applyWhenBroken)
    ).apply(instance, WalkOnPowderedSnowProperty::new));

    public static WalkOnPowderedSnowProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new WalkOnPowderedSnowProperty(partType, operation, value);
    }

    public static WalkOnPowderedSnowProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new WalkOnPowderedSnowProperty(partType, operation, value, applyWhenBroken);
    }

    public static boolean compute(ItemStack stack)
    {
        if(stack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.BOOTS)
            return PropertyHelper.computeBooleanPropertyValue(
                    PropertyHelper.brokenFilter(
                            PropertyHelper.getPropertiesOfTypeFromItemStack(WalkOnPowderedSnowProperty.class, stack),
                            PropertyHelper.isBroken(stack)
                    )
            );

        return false;
    }

    public WalkOnPowderedSnowProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public WalkOnPowderedSnowProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        WalkOnPowderedSnowProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.WALK_ON_POWDERED_SNOW;
    }
}
