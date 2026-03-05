package amorphia.alloygery.gear.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.AlloygeryMaterialRegistry;
import amorphia.alloygery.gear.material.MaterialHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class AlloygeryMaterialDataReloadListener implements SimpleSynchronousResourceReloadListener
{
    public static final AlloygeryMaterialDataReloadListener INSTANCE = new AlloygeryMaterialDataReloadListener();
    public static final ResourceLocation ID = Alloygery.asResource("alloygery_material_data_reload_listener");

    private AlloygeryMaterialDataReloadListener() {} // no op

    @Override
    public ResourceLocation getFabricId()
    {
        return ID;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
        AlloygeryMaterialRegistry.resetMaterialRegistryToInitialRegisteredValues();

		AtomicInteger numberRead = new AtomicInteger();
		AtomicInteger numberModified = new AtomicInteger();
		AtomicInteger numberSkipped = new AtomicInteger();

        resourceManager.listResources("alloygery_material", path -> path.getPath().endsWith(".json")).forEach((location, resource) -> {
            Alloygery.LOGGER.debug("Reading material from datapack: {}", location.toString());
			numberRead.getAndIncrement();

            try (BufferedReader reader = resource.openAsReader())
            {
                ResourceLocation trimmedLocation = new ResourceLocation(location.getNamespace(), location.getPath().substring(0, location.getPath().length() - ".json".length()));

                JsonElement jsonElement = JsonParser.parseReader(reader);
                if(!jsonElement.isJsonObject())
                {
					numberSkipped.getAndIncrement();
                    Alloygery.LOGGER.info("Error reading {}, not a json object.", location.toString());
                    return;
                }

                JsonObject json = jsonElement.getAsJsonObject();
                if (!ResourceConditions.objectMatchesConditions(json))
                {
					numberSkipped.getAndIncrement();
                    Alloygery.LOGGER.info("Load conditions not met for {}", location.toString());
                    return;
                }

                DataResult<AlloygeryMaterial> dataResult = AlloygeryMaterial.CODEC.parse(JsonOps.INSTANCE, json);
                AlloygeryMaterial material = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
				if(AlloygeryMaterialRegistry.contains(trimmedLocation))
					numberModified.getAndIncrement();

                AlloygeryMaterialRegistry.load(trimmedLocation, MaterialHelper.createMaterialDataFromMaterial(material));
            }
            catch (IOException thrown)
            {
				numberSkipped.getAndIncrement();
                Alloygery.LOGGER.error("Error occurred while reading material data: {}", location.toString(), thrown);
            }
        });

		final int total = numberRead.get();
		final int modified = numberModified.get();
		final int skipped = numberSkipped.get();
		Alloygery.LOGGER.info("Read {} materials from datapacks. Loaded {} as new, and modified {} existing materials. {} materials were skipped due to failed resource conditions or errors.", total, total - modified - skipped, modified, skipped);
    }
}
