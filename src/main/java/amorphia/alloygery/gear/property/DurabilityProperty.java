package amorphia.alloygery.gear.property;

import amorphia.alloygery.gear.attribute.SlotMultipliers;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.part.ArmorPart;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;

public class DurabilityProperty extends Property
{
    private static final EnumMap<ArmorItem.Type, Integer> ARMOR_DURABILITY_MULTIPLIER_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });

    public static final Codec<DurabilityProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PartTypes.CODEC.fieldOf("part_type").forGetter(DurabilityProperty::getPartType),
            PropertyOperation.CODEC.fieldOf("operation").forGetter(DurabilityProperty::getOperation),
            Codec.FLOAT.fieldOf("value").forGetter(DurabilityProperty::getValue),
            Codec.BOOL.fieldOf("apply_when_broken").forGetter(DurabilityProperty::applyWhenBroken)
    ).apply(instance, DurabilityProperty::new));

    public static DurabilityProperty of(PartTypes partType, PropertyOperation operation, float value)
    {
        return new DurabilityProperty(partType, operation, value);
    }

    public static DurabilityProperty of(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        return new DurabilityProperty(partType, operation, value, applyWhenBroken);
    }

    public static int compute(ItemStack stack)
    {
        final float durability = PropertyHelper.computePropertyValue(PropertyHelper.getPropertiesOfTypeFromItemStack(DurabilityProperty.class, stack));
        final float durability_by_slot = durability * SlotMultipliers.getDurabilitySlotMultiplier(stack);
        final int durability_rounded = Math.max(0, Math.round(durability_by_slot));

        return durability_rounded;
    }

    public static float getDurabilitySlotMultiplier(ItemStack stack)
    {
		if(stack == null || stack.isEmpty())
			return 1.0f;

		if(stack.getItem() instanceof ArmorItem armorItem)
			return ARMOR_DURABILITY_MULTIPLIER_PER_TYPE.get(armorItem.getType());

		return 1.0f;
    }

    public DurabilityProperty(PartTypes partType, PropertyOperation operation, float value)
    {
        this(partType, operation, value, false);
    }

    public DurabilityProperty(PartTypes partType, PropertyOperation operation, float value, boolean applyWhenBroken)
    {
        super(partType, operation, value, applyWhenBroken);
    }

    @Override
    public boolean equalsIgnoreValue(Property other)
    {
        if(other == null || other.getClass() != getClass()) return false;
        DurabilityProperty property = getClass().cast(other);
        return partType == property.partType && operation == property.operation;
    }

    @Override
    public PropertyType<?> getPropertyType()
    {
        return PropertyType.DURABILITY;
    }
}
