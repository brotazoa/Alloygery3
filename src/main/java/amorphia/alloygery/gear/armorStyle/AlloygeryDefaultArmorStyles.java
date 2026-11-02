package amorphia.alloygery.gear.armorStyle;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.EncumbranceProperty;
import amorphia.alloygery.gear.property.PropertyOperation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class AlloygeryDefaultArmorStyles
{
	public static final ArmorStyle CHAIN;
	public static final ArmorStyle LEATHER;
	public static final ArmorStyle WOOL;
	public static final ArmorStyle SCALE;
	public static final ArmorStyle PLATE;
	public static final ArmorStyle HEAVY_PLATE;

	public static final List<ArmorStyle> ALL_DEFAULT_ARMOR_STYLES;

	private static ArmorStyle register(ArmorStyle style)
	{
		return ArmorStyleRegistry.register(style.identifier, style);
	}

	private static ResourceLocation asArmorStyle(String name)
	{
		return Alloygery.asResource("alloygery_armor_styles/" + name);
	}

	static
	{
		CHAIN = register(new ArmorStyle.ArmorStyleBuilder(asArmorStyle("chain"))
				.type(0.1f)
				.property(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.5f))
				.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.5f))
				.build()
		);

		LEATHER = register(new ArmorStyle.ArmorStyleBuilder(asArmorStyle("leather"))
				.type(0.2f)
				.property(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.0f))
				.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 1.0f))
				.build()
		);

		WOOL = register(new ArmorStyle.ArmorStyleBuilder(asArmorStyle("wool"))
				.type(0.3f)
				.property(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 0.2f))
				.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_BASE, PropertyOperation.MULTIPLY_TOTAL, 0.5f))
				.build()
		);

		PLATE = register(new ArmorStyle.ArmorStyleBuilder(asArmorStyle("plate"))
				.type(0.4f)
				.property(EncumbranceProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f))
				.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f))
				.property(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f))
				.property(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 0.5f))
				.build()
		);

		SCALE = register(new ArmorStyle.ArmorStyleBuilder(asArmorStyle("scale"))
				.type(0.5f)
				.property(EncumbranceProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f))
				.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f))
				.property(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f))
				.property(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.0f))
				.build()
		);

		HEAVY_PLATE = register(new ArmorStyle.ArmorStyleBuilder(asArmorStyle("heavy_plate"))
				.type(0.6f)
				.property(EncumbranceProperty.of(PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f))
				.property(AttributeProperty.of(Attributes.ARMOR, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f))
				.property(AttributeProperty.of(Attributes.ARMOR_TOUGHNESS, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f))
				.property(AttributeProperty.of(Attributes.KNOCKBACK_RESISTANCE, PartTypes.ARMOR_PLATE, PropertyOperation.MULTIPLY_PART, 1.5f))
				.build()
		);

		ALL_DEFAULT_ARMOR_STYLES = List.of(CHAIN, LEATHER, WOOL, PLATE, SCALE, HEAVY_PLATE);
	}
}
