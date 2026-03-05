package amorphia.alloygery.compat;

import amorphia.alloygery.compat.recipeViewers.emi.EmiEnglishLanguageProvider;
import amorphia.alloygery.compat.recipeViewers.rei.ReiEnglishLanguageProvider;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CompatibilityModule
{
	public static void initialize()
	{

	}

	public static void initializeClient()
	{

	}

	public static void initializeDatagen(FabricDataGenerator dataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryEnglishLanguageProvider.addProvider(new EmiEnglishLanguageProvider());
		AlloygeryEnglishLanguageProvider.addProvider(new ReiEnglishLanguageProvider());
	}
}
