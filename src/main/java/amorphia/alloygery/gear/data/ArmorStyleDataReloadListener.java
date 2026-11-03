package amorphia.alloygery.gear.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.armorStyle.ArmorStyle;
import amorphia.alloygery.gear.armorStyle.ArmorStyleRegistry;
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

public class ArmorStyleDataReloadListener implements SimpleSynchronousResourceReloadListener
{
	public static final ArmorStyleDataReloadListener INSTANCE = new ArmorStyleDataReloadListener();
	public static final ResourceLocation ID = Alloygery.asResource("armor_style_data_reload_listener");

	@Override
	public ResourceLocation getFabricId()
	{
		return ID;
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{
		ArmorStyleRegistry.resetRegistryToInitialValues();

		AtomicInteger numberRead = new AtomicInteger();
		AtomicInteger numberModified = new AtomicInteger();
		AtomicInteger numberSkipped = new AtomicInteger();

		resourceManager.listResources("alloygery_armor_styles", path -> path.getPath().endsWith(".json")).forEach((location, resource) -> {
			Alloygery.LOGGER.debug("Reading Armor Style from datapack: {}", location.toString());
			numberRead.getAndIncrement();

			try (BufferedReader reader = resource.openAsReader())
			{
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

				DataResult<ArmorStyle> dataResult = ArmorStyle.CODEC.parse(JsonOps.INSTANCE, json);
				ArmorStyle style = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
				ResourceLocation armorStyleIdentifier = style.getIdentifier();
				if (ArmorStyleRegistry.contains(armorStyleIdentifier))
					numberModified.getAndIncrement();

				ArmorStyleRegistry.load(armorStyleIdentifier, ArmorStyle.createData(style));
			}
			catch (IOException thrown)
			{
				numberSkipped.getAndIncrement();
				Alloygery.LOGGER.error("Error occurred while reading Armor Style data: {}", location.toString(), thrown);
			}
		});

		final int total = numberRead.get();
		final int modified = numberModified.get();
		final int skipped = numberSkipped.get();
		Alloygery.LOGGER.info("Read {} Armor Styles from datapacks. Loaded {} as new, and modified {} existing. {} Armor Styles were skipped due to failed resource conditions or errors.", total, total - modified - skipped, modified, skipped);
	}
}
