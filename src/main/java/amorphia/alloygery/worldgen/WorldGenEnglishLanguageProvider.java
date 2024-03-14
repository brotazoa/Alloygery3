package amorphia.alloygery.worldgen;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class WorldGenEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		WorldGenModule.BLOCKS.forEach((path, block) -> translationBuilder.add(block, AlloygeryEnglishLanguageProvider.englishNameFromPath(path)));
	}
}
