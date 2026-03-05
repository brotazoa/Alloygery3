package amorphia.alloygery.compat.recipeViewers.rei;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ReiEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		translationBuilder.add("category.rei.alloygery.alloying", "Advanced Alloying");
		translationBuilder.add("category.rei.alloygery.simple_alloying", "Alloying");
		translationBuilder.add("category.rei.alloygery.smithing_anvil", "Smithing Anvil");
		translationBuilder.add("category.rei.alloygery.tailoring", "Tailoring");
		translationBuilder.add("category.rei.alloygery.woodcutting", "Woodcutting");
		translationBuilder.add("category.rei.alloygery.firing", "Firing");
		translationBuilder.add("category.rei.alloygery.heat_exchanger_smelting", "Advanced Smelting");
		translationBuilder.add("category.rei.alloygery.heat_exchanger_blasting", "Advanced Blasting");
		translationBuilder.add("category.rei.alloygery.heat_exchanger_smoking", "Advanced Smoking");
		translationBuilder.add("category.rei.alloygery.heat_exchanger_firing", "Advanced Firing");
		translationBuilder.add("category.rei.alloygery.heat_exchanger_transform", "Placing On Heat Exchanger");
	}
}
