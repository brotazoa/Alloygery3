package amorphia.alloygery.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AlloygeryEnglishLanguageProvider extends FabricLanguageProvider
{
	private static final List<IAlloygeryEnglishLanguageProvider> providers = new ArrayList<>();

	public static void addProvider(IAlloygeryEnglishLanguageProvider provider)
	{
		providers.add(provider);
	}

	public static String englishNameFromResourceLocation(ResourceLocation resourceLocation)
	{
		return AlloygeryEnglishLanguageProvider.englishNameFromPath(resourceLocation.getPath());
	}

	public static String englishNameFromPath(String path)
	{
		return Arrays.stream(path.toLowerCase(Locale.ENGLISH).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	public AlloygeryEnglishLanguageProvider(FabricDataOutput output)
	{
		super(output, "en_us");
	}

	@Override
	public void generateTranslations(TranslationBuilder translationBuilder)
	{
		try
		{
			Path existing = dataOutput.getModContainer().findPath("assets/" + Alloygery.MODID + "/lang/en_us.existing.json").get();
			translationBuilder.add(existing);
		}
		catch (Exception thrown)
		{
			throw new RuntimeException("Failed to add existing language file", thrown);
		}

		for(IAlloygeryEnglishLanguageProvider provider : providers)
		{
			provider.generateTranslations(translationBuilder);
		}

		translationBuilder.add(AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.getIcon().toLanguageKey("creative_tab"), "Alloygery Parts");

		translationBuilder.add("pack.alloygery.worldgen.description", "Worldgen for Alloygery. Defaults to enabled for new worlds.");
		translationBuilder.add("pack.alloygery.advancements.description", "Advancements for Alloygery. This modifies Vanilla advancements so if you are experiencing issues with advancements try disabling this pack.");
		translationBuilder.add("pack.alloygery.logs_require_tool.description", "Logs require the correct tool for item drops.");
		translationBuilder.add("pack.alloygery.easier_starting_recipes.description", "A collection of recipes to help speed up the very early game. Defaults to enabled for new worlds.");
		translationBuilder.add("pack.alloygery.convert_vanilla_gear.description", "Convert Vanilla tools and armor into their Alloygery equivalents. Defaults to enabled for new worlds. If you disable this remember to disable to companion Resource/Data pack.");
		translationBuilder.add("pack.alloygery.vanilla_recipe_overrides.description", "Opinionated changes to some Vanilla recipes. Defaults to enabled for new worlds.");
	}

	public interface IAlloygeryEnglishLanguageProvider
	{
		void generateTranslations(TranslationBuilder translationBuilder);
	}
}
