package amorphia.alloygery.craftingMaterials;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class CraftingMaterialEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		CraftingMaterialModule.ITEMS.forEach((path, item) -> translationBuilder.add(item, AlloygeryEnglishLanguageProvider.englishNameFromPath(path)));
		CraftingMaterialModule.BLOCKS.forEach((path, block) -> translationBuilder.add(block, AlloygeryEnglishLanguageProvider.englishNameFromPath(path)));
	}
}
