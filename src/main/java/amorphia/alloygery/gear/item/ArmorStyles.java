package amorphia.alloygery.gear.item;

import amorphia.alloygery.gear.property.*;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum ArmorStyles
{
	// TODO: make this data driven

    // base
    CHAIN(0.1f),
    LEATHER(0.2f),
    WOOL(0.3f),

    // plate
    SCALE(0.4f),
    PLATE(0.5f),
    HEAVY_PLATE(0.6f),
    ;

    public static final Codec<ArmorStyles> CODEC = Codec.STRING.xmap(ArmorStyles::getByName, ArmorStyles::getName);

    public static final ArmorStyles[] VALUES_CACHE = ArmorStyles.values();

    public static ArmorStyles getByName(String name)
    {
        return Arrays.stream(VALUES_CACHE).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    private final float type;

    ArmorStyles(float type)
    {
        this.type = type;
    }

    public float getTypeFloat()
    {
        return this.type;
    }

    public String getName()
    {
        return name().toLowerCase(Locale.ROOT);
    }

	public void addStyleProperties(List<Property> properties)
	{
		switch (this)
		{
			case CHAIN -> {
				properties.add(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.5f));
				properties.add(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.5f));
			}
			case LEATHER -> {
				properties.add(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.0f));
				properties.add(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.0f));
			}
			case WOOL -> {
				properties.add(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 0.2f));
				properties.add(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 0.5f));
			}
			case PLATE -> {
				properties.add(EncumbranceProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f));
				properties.add(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f));
				properties.add(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f));
				properties.add(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f));
			}
			case SCALE -> {
				properties.add(EncumbranceProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f));
				properties.add(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f));
				properties.add(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f));
				properties.add(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f));
			}
			case HEAVY_PLATE -> {
				properties.add(EncumbranceProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f));
				properties.add(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f));
				properties.add(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f));
				properties.add(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f));
			}
		}
	}
}
