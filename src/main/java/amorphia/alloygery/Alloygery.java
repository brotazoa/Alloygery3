package amorphia.alloygery;

import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import amorphia.alloygery.datagen.AlloygeryModelProvider;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.worldgen.WorldGenModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Alloygery implements ModInitializer, ClientModInitializer, DataGeneratorEntrypoint
{
	public static final String MODID = "alloygery";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize()
	{
		WorldGenModule.initialize();
		CraftingMaterialModule.initialize();
		MachinesModule.initialize();

		AlloygeryCreativeTabs.initialize();
	}

	@Override
	public void onInitializeClient()
	{
		WorldGenModule.initializeClient();
		CraftingMaterialModule.initializeClient();
		MachinesModule.initializeClient();
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		WorldGenModule.initializeDataGenerator(fabricDataGenerator, pack);
		CraftingMaterialModule.initializeDataGen(fabricDataGenerator, pack);
		MachinesModule.initializeDataGen(fabricDataGenerator, pack);

		pack.addProvider(AlloygeryModelProvider::new);
		pack.addProvider(AlloygeryEnglishLanguageProvider::new);
	}

	public static ResourceLocation asResource(String path)
	{
		return new ResourceLocation(MODID, path);
	}
}
