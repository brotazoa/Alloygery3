package amorphia.alloygery.gear.material;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.data.IAlloygeryMaterialData;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class AlloygeryMaterialRegistry
{
    private static final ResourceLocation DEFAULT_KEY = Alloygery.asResource("materials/unknown");
    private static AlloygeryMaterial DEFAULT_MATERIAL = null;

    private static final BiMap<ResourceLocation, AlloygeryMaterial> MATERIALS = HashBiMap.create();

    private static final Map<ResourceLocation, IAlloygeryMaterialData> REGISTERED_MATERIAL_DATA = Maps.newHashMap();
    private static final List<ResourceLocation> LOADED_MATERIAL_IDENTIFIERS = Lists.newArrayList();

    public static AlloygeryMaterial register(ResourceLocation identifier, AlloygeryMaterial material)
    {
        if(identifier == null || material == null)
            throw new IllegalArgumentException("[Alloygery Material Registry] Can not register null values");

        Alloygery.LOGGER.debug("[Alloygery Material Registry] Registering material: {}", identifier.toString());

        if (MATERIALS.containsKey(identifier))
        {
            if (get(identifier) == material)
                throw new IllegalArgumentException("[Alloygery Material Registry] Tried to register a material twice. " + identifier.toString());
            else
                throw new IllegalArgumentException("[Alloygery Material Registry] Tried to override an existing registered material. " + identifier.toString() + " Load updated material data instead.");
        }

        MATERIALS.put(identifier, material);
        REGISTERED_MATERIAL_DATA.put(identifier, MaterialHelper.createMaterialDataFromMaterial(material));
        if(DEFAULT_KEY.equals(identifier))
            DEFAULT_MATERIAL = material;

        return get(identifier);
    }

    public static AlloygeryMaterial load(ResourceLocation identifier, IAlloygeryMaterialData data)
    {
        if(identifier == null || data == null)
            throw new IllegalArgumentException("[AlloygeryMaterialRegistry] Can not load null values");

        if(MATERIALS.containsKey(identifier))
        {
            Alloygery.LOGGER.debug("[AlloygeryMaterialRegistry] Updating existing material: {}", identifier.toString());
            data.applyTo(get(identifier));
        }
        else
        {
            Alloygery.LOGGER.debug("[AlloygeryMaterialRegistry] Loaded material {} was never registered.", identifier.toString());
            LOADED_MATERIAL_IDENTIFIERS.add(identifier);
            MATERIALS.put(identifier, new MaterialHelper.MaterialBuilder(identifier).applyData(data).build());
        }

        return get(identifier);
    }

    public static AlloygeryMaterial get(ResourceLocation identifier)
    {
        if(identifier == null)
            return getDefaultMaterial();

        return MATERIALS.getOrDefault(identifier, getDefaultMaterial());
    }

    public static ResourceLocation identify(AlloygeryMaterial material)
    {
        if(material == null)
            return getDefaultKey();

        return MATERIALS.inverse().getOrDefault(material, getDefaultKey());
    }

	public static boolean contains(AlloygeryMaterial material)
	{
		return MATERIALS.containsValue(material);
	}

	public static boolean contains(ResourceLocation location)
	{
		return MATERIALS.containsKey(location);
	}

    public static ResourceLocation getDefaultKey()
    {
        return DEFAULT_KEY;
    }

    public static AlloygeryMaterial getDefaultMaterial()
    {
        return DEFAULT_MATERIAL;
    }

    public static void forEach(BiConsumer<ResourceLocation, AlloygeryMaterial> forEachConsumer)
    {
        MATERIALS.forEach(forEachConsumer);
    }

    public static Stream<AlloygeryMaterial> stream()
    {
        return MATERIALS.values().stream();
    }

    public static void resetMaterialRegistryToInitialRegisteredValues()
    {
        LOADED_MATERIAL_IDENTIFIERS.forEach(MATERIALS::remove);
        LOADED_MATERIAL_IDENTIFIERS.clear();
        REGISTERED_MATERIAL_DATA.forEach((id, mat) -> mat.applyTo(AlloygeryMaterialRegistry.get(id)));
    }
}
