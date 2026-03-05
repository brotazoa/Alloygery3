package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class EmiEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		translationBuilder.add("emi.category.alloygery.emi_alloying", "Advanced Alloying");
		translationBuilder.add("emi.category.alloygery.emi_simple_alloying", "Alloying");
		translationBuilder.add("emi.category.alloygery.emi_smithing_anvil", "Smithing Anvil");
		translationBuilder.add("emi.category.alloygery.emi_tailoring", "Tailoring");
		translationBuilder.add("emi.category.alloygery.emi_woodcutting", "Woodcutting");
		translationBuilder.add("emi.category.alloygery.emi_firing", "Firing");
		translationBuilder.add("emi.category.alloygery.emi_heat_exchanger_smelting", "Advanced Smelting");
		translationBuilder.add("emi.category.alloygery.emi_heat_exchanger_blasting", "Advanced Blasting");
		translationBuilder.add("emi.category.alloygery.emi_heat_exchanger_smoking", "Advanced Smoking");
		translationBuilder.add("emi.category.alloygery.emi_heat_exchanger_firing", "Advanced Firing");
		translationBuilder.add("emi.category.alloygery.emi_heat_exchanger_transform", "Placing On Heat Exchanger");
	}
}
