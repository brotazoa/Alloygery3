package amorphia.alloygery.gear.toolType;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.data.IToolTypeData;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ToolTypeRegistry
{
	private static final ResourceLocation DEFAULT_KEY = Alloygery.asResource("alloygery_tool_types/unknown");
	private static ToolType DEFAULT_TOOL_TYPE = null;
	private static final Logger LOGGER = LogManager.getLogger();

	private static final BiMap<ResourceLocation, ToolType> TOOL_TYPES = HashBiMap.create();
	private static final Map<ResourceLocation, IToolTypeData> REGISTERED_TOOL_TYPES = Maps.newHashMap();
	private static final List<ResourceLocation> LOADED_IDENTIFIERS = Lists.newArrayList();

	public static ToolType register(ResourceLocation identifier, ToolType toolType)
	{
		if(identifier == null || toolType == null)
			throw LOGGER.throwing(new IllegalArgumentException("Cannot register null values"));

		LOGGER.debug("Registering tool type: {}", identifier.toString());

		if (TOOL_TYPES.containsKey(identifier))
		{
			if(get(identifier) == toolType)
				throw LOGGER.throwing(new IllegalArgumentException("Tried to register a Tool Type twice." + identifier));
			else
				throw LOGGER.throwing(new IllegalArgumentException("Tried to override an existing registered Tool Type. " + identifier + " Load updated data instead."));
		}

		TOOL_TYPES.put(identifier, toolType);
		REGISTERED_TOOL_TYPES.put(identifier, applyTo -> {
			applyTo.properties.clear();
			applyTo.identifier = toolType.identifier;
			applyTo.mergeFlag = toolType.mergeFlag;
			toolType.properties.forEach(applyTo::addProperty);
			return applyTo;
		});

		if(DEFAULT_KEY.equals(identifier))
			DEFAULT_TOOL_TYPE = toolType;

		return get(identifier);
	}

	public static ToolType load(ResourceLocation identifier, IToolTypeData data)
	{
		if(identifier == null || data == null)
			throw LOGGER.throwing(new IllegalArgumentException("Cannot load null values"));

		if (TOOL_TYPES.containsKey(identifier))
		{
			LOGGER.debug("Updating existing Tool Type: {}", identifier.toString());
			data.applyTo(get(identifier));
		}
		else
		{
			LOGGER.debug("Loaded Tool Type {} was never registered.", identifier.toString());
			LOADED_IDENTIFIERS.add(identifier);
			TOOL_TYPES.put(identifier, data.applyTo(new ToolType()));
		}

		return get(identifier);
	}

	public static ToolType get(ResourceLocation identifier)
	{
		return TOOL_TYPES.getOrDefault(identifier, getDefaultToolType());
	}

	public static ResourceLocation identify(ToolType toolType)
	{
		return TOOL_TYPES.inverse().getOrDefault(toolType, getDefaultKey());
	}

	public static ResourceLocation getDefaultKey()
	{
		return DEFAULT_KEY;
	}

	public static ToolType getDefaultToolType()
	{
		return DEFAULT_TOOL_TYPE;
	}

	public static boolean contains(ToolType toolType)
	{
		return TOOL_TYPES.containsValue(toolType);
	}

	public static boolean contains(ResourceLocation identifier)
	{
		return TOOL_TYPES.containsKey(identifier);
	}

	public static void forEach(BiConsumer<ResourceLocation, ToolType> forEachConsumer)
	{
		TOOL_TYPES.forEach(forEachConsumer);
	}

	public static Stream<ToolType> stream()
	{
		return TOOL_TYPES.values().stream();
	}

	public static void resetRegistryToInitialValues()
	{
		LOADED_IDENTIFIERS.forEach(TOOL_TYPES::remove);
		LOADED_IDENTIFIERS.clear();
		REGISTERED_TOOL_TYPES.forEach((resourceLocation, data) -> data.applyTo(get(resourceLocation)));
	}
}
