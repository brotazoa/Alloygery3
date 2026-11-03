package amorphia.alloygery.gear.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.toolType.ToolType;
import amorphia.alloygery.gear.toolType.ToolTypeRegistry;
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

public class ToolTypeDataReloadListener implements SimpleSynchronousResourceReloadListener
{
	public static final ToolTypeDataReloadListener INSTANCE = new ToolTypeDataReloadListener();
	public static final ResourceLocation ID = Alloygery.asResource("tool_type_data_reload_listener");

	@Override
	public ResourceLocation getFabricId()
	{
		return ID;
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{
		ToolTypeRegistry.resetRegistryToInitialValues();

		AtomicInteger numberRead = new AtomicInteger();
		AtomicInteger numberModified = new AtomicInteger();
		AtomicInteger numberSkipped = new AtomicInteger();

		resourceManager.listResources("alloygery_tool_types", path -> path.getPath().endsWith(".json")).forEach((location, resource) -> {
			Alloygery.LOGGER.debug("Reading Tool Type from datapack: {}", location.toString());
			numberRead.getAndIncrement();

			try (BufferedReader reader = resource.openAsReader())
			{
				ResourceLocation trimmedLocation = new ResourceLocation(location.getNamespace(), location.getPath().substring(0, location.getPath().length() - ".json".length()));

				JsonElement jsonElement = JsonParser.parseReader(reader);
				if (!jsonElement.isJsonObject())
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

				DataResult<ToolType> dataResult = ToolType.CODEC.parse(JsonOps.INSTANCE, json);
				ToolType toolType = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
				if(ToolTypeRegistry.contains(toolType))
					numberModified.getAndIncrement();

				ToolTypeRegistry.load(trimmedLocation, ToolType.createData(toolType));
			}
			catch (IOException thrown)
			{
				numberSkipped.getAndIncrement();
				Alloygery.LOGGER.error("Error occurred while reading Tool Type data: {}", location.toString(), thrown);
			}
		});

		final int total = numberRead.get();
		final int modified = numberModified.get();
		final int skipped = numberSkipped.get();
		Alloygery.LOGGER.info("Read {} Tool Types from datapacks. Loaded {} as new, and modified {} existing. {} Tool Types were skipped due to failed resource conditions or errors.", total, total - modified - skipped, modified, skipped);
	}
}
