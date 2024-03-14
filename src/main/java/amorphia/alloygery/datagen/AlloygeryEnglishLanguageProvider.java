package amorphia.alloygery.datagen;

import amorphia.alloygery.Alloygery;
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
	}

	public interface IAlloygeryEnglishLanguageProvider
	{
		void generateTranslations(TranslationBuilder translationBuilder);
	}
}
