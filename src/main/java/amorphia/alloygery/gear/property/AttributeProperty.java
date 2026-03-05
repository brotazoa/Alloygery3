package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.item.PartTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AttributeProperty extends Property
{
    public static final Codec<AttributeProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("attribute").forGetter(attributeProperty -> BuiltInRegistries.ATTRIBUTE.getKey(attributeProperty.getTargetAttribute())),
            PartTypes.CODEC.fieldOf("part_type").forGetter(AttributeProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(AttributeProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(AttributeProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(AttributeProperty::applyWhenBroken)
    ).apply(instance, (resourceLocation, partTypes, operation1, aFloat, aBool) -> new AttributeProperty(BuiltInRegistries.ATTRIBUTE.get(resourceLocation), partTypes, operation1, aFloat, aBool)));

    public static AttributeProperty of(Attribute targetAttribute, PartTypes partType, PropertyOperation operation, float value)
    {
        return new AttributeProperty(targetAttribute, partType, operation, value);
    }

    public static AttributeProperty of(Attribute targetAttribute, PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new AttributeProperty(targetAttribute, partType, operation, value, applyWhenBroken);
    }

    public static float compute(Attribute attribute, ItemStack stack)
    {
		return PropertyHelper.computePropertyValue(
				PropertyHelper.brokenFilter(
						PropertyHelper.getPropertiesOfTypeFromItemStack(AttributeProperty.class, stack).stream().filter(p -> p.targetAttribute.equals(attribute)).toList(),
						PropertyHelper.isBroken(stack)
				)
		);
    }

    protected final Attribute targetAttribute;

    public AttributeProperty(Attribute targetAttribute, PartTypes partType, PropertyOperation operation, float value)
    {
        this(targetAttribute, partType, operation, value, false);
    }

    public AttributeProperty(Attribute targetAttribute, PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
        this.targetAttribute = targetAttribute;
    }

    public Attribute getTargetAttribute()
    {
        return targetAttribute;
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        AttributeProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation && targetAttribute == property.targetAttribute;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.ATTRIBUTE;
    }
}
