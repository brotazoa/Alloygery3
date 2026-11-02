package amorphia.alloygery.gear.armorStyle;

import amorphia.alloygery.gear.data.IArmorStyleData;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class ArmorStyle
{
	public static final Codec<ArmorStyle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("style").forGetter(ArmorStyle::getIdentifier),
			Codec.FLOAT.fieldOf("type").forGetter(ArmorStyle::getTypeFloat),
			Codec.BOOL.fieldOf("merge").forGetter(ArmorStyle::shouldMergeOnApply),
			Property.CODEC.listOf().fieldOf("properties").forGetter(ArmorStyle::getProperties)
	).apply(instance, (resourceLocation, type, merge, propertyList) -> {
		ArmorStyle style = new ArmorStyle();
		style.identifier = resourceLocation;
		style.typeFloat = type;
		style.mergeFlag = merge;
		for(Property p : propertyList)
			style.addProperty(p);
		return style;
	}));

	ResourceLocation identifier;
	float typeFloat;
	boolean mergeFlag = false;
	final List<Property> properties = Lists.newArrayList();

	public ResourceLocation getIdentifier()
	{
		return this.identifier;
	}

	public String getIdentifierString()
	{
		return this.identifier.toString();
	}

	public String getSimpleName()
	{
		return this.identifier.getPath().replace("alloygery_armor_styles/", "");
	}

	public float getTypeFloat()
	{
		return this.typeFloat;
	}

	public List<Property> getProperties()
	{
		return Collections.unmodifiableList(this.properties);
	}

	public boolean shouldMergeOnApply()
	{
		return this.mergeFlag;
	}

	<P extends Property> void addProperty(P property)
	{
		if(property == null)
			return;

		this.properties.stream().filter(p -> p.equalsIgnoreValue(property)).findFirst().ifPresent(this.properties::remove);
		this.properties.add(property);
	}

	public static ArmorStyle copy(ArmorStyle original)
	{
		ArmorStyle copy = new ArmorStyle();
		copy.identifier = original.identifier;
		override(copy, original);
		return copy;
	}

	public static ArmorStyle override(ArmorStyle original, ArmorStyle other)
	{
		if(other == null || other == original)
			return original;

		original.properties.clear();
		original.typeFloat = other.typeFloat;
		other.properties.forEach(original::addProperty);

		return original;
	}

	public static ArmorStyle merge(ArmorStyle original, ArmorStyle other)
	{
		if(other == null || other == original)
			return original;

		other.properties.forEach(original::addProperty);

		return original;
	}

	public static IArmorStyleData createData(ArmorStyle style)
	{
		return style.mergeFlag ? applyToStyle -> merge(applyToStyle, copy(style)) : applyToStyle -> override(applyToStyle, copy(style));
	}

	public static class ArmorStyleBuilder
	{
		private final ArmorStyle style;

		public ArmorStyleBuilder(ResourceLocation identifier)
		{
			this.style = new ArmorStyle();
			this.style.identifier = identifier;
		}

		public ArmorStyleBuilder type(float typeFloat)
		{
			this.style.typeFloat = typeFloat;
			return this;
		}

		public ArmorStyleBuilder setMergeFlag(boolean shouldMerge)
		{
			this.style.mergeFlag = shouldMerge;
			return this;
		}

		public ArmorStyleBuilder property(Property property)
		{
			this.style.addProperty(property);
			return this;
		}

		public ArmorStyleBuilder applyData(IArmorStyleData data)
		{
			if (data != null)
				data.applyTo(this.style);

			return this;
		}

		public ArmorStyle build()
		{
			return this.style;
		}
	}
}
