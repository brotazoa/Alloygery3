package amorphia.alloygery.gear.toolType;

import amorphia.alloygery.gear.data.IToolTypeData;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class ToolType
{
	public static final Codec<ToolType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("type").forGetter(ToolType::getIdentifier),
			Codec.BOOL.fieldOf("merge").forGetter(ToolType::shouldMergeOnApply),
			Property.CODEC.listOf().fieldOf("properties").forGetter(ToolType::getProperties)
	).apply(instance, (resourceLocation, merge, propertyList) -> {
		ToolType toolType = new ToolType();
		toolType.identifier = resourceLocation;
		toolType.mergeFlag = merge;
		for(Property p : propertyList)
			toolType.addProperty(p);
		return toolType;
	}));

	ResourceLocation identifier;
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
		return this.identifier.getPath().replace("alloygery_tool_types/", "");
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

	public static ToolType copy(ToolType other)
	{
		ToolType copy = new ToolType();
		copy.identifier = other.identifier;
		override(copy, other);

		return copy;
	}

	public static ToolType override(ToolType original, ToolType other)
	{
		if(other == null || other == original)
			return original;

		original.properties.clear();
		other.properties.forEach(original::addProperty);

		return original;
	}

	public static ToolType merge(ToolType original, ToolType other)
	{
		if(other == null || other == original)
			return original;

		other.properties.forEach(original::addProperty);

		return original;
	}

	public static IToolTypeData createData(ToolType toolType)
	{
		return toolType.mergeFlag ? applyTo -> merge(applyTo, copy(toolType)) : applyTo -> override(applyTo, copy(toolType));
	}

	public static class ToolTypeBuilder
	{
		private final ToolType toolType;

		public ToolTypeBuilder(ResourceLocation identifier)
		{
			this.toolType = new ToolType();
			this.toolType.identifier = identifier;
		}

		public ToolTypeBuilder setMergeFlag(boolean shouldMerge)
		{
			this.toolType.mergeFlag = shouldMerge;
			return this;
		}

		public ToolTypeBuilder property(Property property)
		{
			this.toolType.addProperty(property);
			return this;
		}

		public ToolTypeBuilder applyData(IToolTypeData data)
		{
			if(data != null)
				data.applyTo(this.toolType);

			return this;
		}

		public ToolType build()
		{
			return this.toolType;
		}
	}
}
