package amorphia.alloygery.gear.armorStyle;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.data.IArmorStyleData;
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

public class ArmorStyleRegistry
{
	private static final ResourceLocation DEFAULT_KEY = Alloygery.asResource("alloygery_armor_styles/leather");
	private static ArmorStyle DEFAULT_STYLE = null;
	private static final Logger LOGGER = LogManager.getLogger();

	private static final BiMap<ResourceLocation, ArmorStyle> STYLES = HashBiMap.create();
	private static final Map<ResourceLocation, IArmorStyleData> REGISTERED_DATA = Maps.newHashMap();
	private static final List<ResourceLocation> LOADED_IDENTIFIERS = Lists.newArrayList();

	public static ArmorStyle register(ResourceLocation identifier, ArmorStyle armorStyle)
	{
		if(identifier == null || armorStyle == null)
			throw LOGGER.throwing(new IllegalArgumentException("Can not register null values"));

		LOGGER.debug("Registering style: {}", identifier.toString());

		if (STYLES.containsKey(identifier))
		{
			if(get(identifier) == armorStyle)
				throw LOGGER.throwing(new IllegalArgumentException("Tried to register an Armor Style twice." + identifier));
			else
				throw LOGGER.throwing(new IllegalArgumentException("Tried to override an existing registered Armor Style. " + identifier + " Load updated data instead."));
		}

		STYLES.put(identifier, armorStyle);
		REGISTERED_DATA.put(identifier, applyTo ->
		{
			applyTo.properties.clear();
			applyTo.identifier = armorStyle.identifier;
			applyTo.typeFloat = armorStyle.typeFloat;
			applyTo.mergeFlag = armorStyle.mergeFlag;
			armorStyle.properties.forEach(applyTo::addProperty);
			return applyTo;
		});

		if(DEFAULT_KEY.equals(identifier))
			DEFAULT_STYLE = armorStyle;

		return get(identifier);
	}

	public static ArmorStyle load(ResourceLocation identifier, IArmorStyleData data)
	{
		if(identifier == null || data == null)
			throw LOGGER.throwing(new IllegalArgumentException("Can not load null values"));

		if (STYLES.containsKey(identifier))
		{
			LOGGER.debug("Updating existing Armor Style: {}", identifier.toString());
			data.applyTo(get(identifier));
		}
		else
		{
			LOGGER.debug("Loaded Armor Style {} was never registered.", identifier.toString());
			LOADED_IDENTIFIERS.add(identifier);
			STYLES.put(identifier, data.applyTo(new ArmorStyle()));
		}

		return get(identifier);
	}

	public static ArmorStyle get(ResourceLocation identifier)
	{
		return STYLES.getOrDefault(identifier, getDefaultStyle());
	}

	public static ResourceLocation identify(ArmorStyle style)
	{
		return STYLES.inverse().getOrDefault(style, getDefaultKey());
	}

	public static boolean contains(ArmorStyle style)
	{
		return STYLES.containsValue(style);
	}

	public static boolean contains(ResourceLocation identifier)
	{
		return STYLES.containsKey(identifier);
	}

	public static ResourceLocation getDefaultKey()
	{
		return DEFAULT_KEY;
	}

	public static ArmorStyle getDefaultStyle()
	{
		return DEFAULT_STYLE;
	}

	public static void forEach(BiConsumer<ResourceLocation, ArmorStyle> forEachConsumer)
	{
		STYLES.forEach(forEachConsumer);
	}

	public static Stream<ArmorStyle> stream()
	{
		return STYLES.values().stream();
	}

	public static void resetRegistryToInitialValues()
	{
		LOADED_IDENTIFIERS.forEach(STYLES::remove);
		LOADED_IDENTIFIERS.clear();
		REGISTERED_DATA.forEach((resourceLocation, iArmorStyleData) -> iArmorStyleData.applyTo(get(resourceLocation)));
	}
}
