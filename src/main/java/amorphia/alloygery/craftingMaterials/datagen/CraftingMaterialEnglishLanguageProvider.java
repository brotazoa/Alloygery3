package amorphia.alloygery.craftingMaterials.datagen;

import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class CraftingMaterialEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		CraftingMaterialModule.ITEMS.forEach((path, item) -> translationBuilder.add(item, AlloygeryEnglishLanguageProvider.englishNameFromPath(path)));
		CraftingMaterialModule.BLOCKS.forEach((path, block) -> translationBuilder.add(block, AlloygeryEnglishLanguageProvider.englishNameFromPath(path)));

		translateCommonItemTag("antanium_blocks", translationBuilder);
		translateCommonItemTag("antanium_ingots", translationBuilder);
		translateCommonItemTag("antanium_nuggets", translationBuilder);
		translateCommonItemTag("bronze_blocks", translationBuilder);
		translateCommonItemTag("bronze_ingots", translationBuilder);
		translateCommonItemTag("bronze_nuggets", translationBuilder);
		translateCommonItemTag("carbon_sources", translationBuilder);
		translateCommonItemTag("constantan_blocks", translationBuilder);
		translateCommonItemTag("constantan_ingots", translationBuilder);
		translateCommonItemTag("constantan_nuggets", translationBuilder);
		translateCommonItemTag("copper_sources", translationBuilder);
		translateCommonItemTag("crystals", translationBuilder);
		translateCommonItemTag("gold_sources", translationBuilder);
		translateCommonItemTag("hides", translationBuilder);
		translateCommonItemTag("invar_blocks", translationBuilder);
		translateCommonItemTag("invar_ingots", translationBuilder);
		translateCommonItemTag("invar_nuggets", translationBuilder);
		translateCommonItemTag("iron_sources", translationBuilder);
		translateCommonItemTag("nickel_blocks", translationBuilder);
		translateCommonItemTag("nickel_ingots", translationBuilder);
		translateCommonItemTag("nickel_nuggets", translationBuilder);
		translateCommonItemTag("nickel_sources", translationBuilder);
		translateCommonItemTag("nitinol_blocks", translationBuilder);
		translateCommonItemTag("nitinol_ingots", translationBuilder);
		translateCommonItemTag("nitinol_nuggets", translationBuilder);
		translateCommonItemTag("raw_copper_nuggets", translationBuilder);
		translateCommonItemTag("raw_gold_nuggets", translationBuilder);
		translateCommonItemTag("raw_iron_nuggets", translationBuilder);
		translateCommonItemTag("raw_nickel_blocks", translationBuilder);
		translateCommonItemTag("raw_nickel_nuggets", translationBuilder);
		translateCommonItemTag("raw_nickel_ores", translationBuilder);
		translateCommonItemTag("raw_tin_blocks", translationBuilder);
		translateCommonItemTag("raw_tin_nuggets", translationBuilder);
		translateCommonItemTag("raw_tin_ores", translationBuilder);
		translateCommonItemTag("raw_titanium_blocks", translationBuilder);
		translateCommonItemTag("raw_titanium_nuggets", translationBuilder);
		translateCommonItemTag("raw_titanium_ores", translationBuilder);
		translateCommonItemTag("steel_blocks", translationBuilder);
		translateCommonItemTag("steel_ingots", translationBuilder);
		translateCommonItemTag("steel_nuggets", translationBuilder);
		translateCommonItemTag("stripped_logs", translationBuilder);
		translateCommonItemTag("tin_blocks", translationBuilder);
		translateCommonItemTag("tin_ingots", translationBuilder);
		translateCommonItemTag("tin_nuggets", translationBuilder);
		translateCommonItemTag("tin_sources", translationBuilder);
		translateCommonItemTag("titanium_blocks", translationBuilder);
		translateCommonItemTag("titanium_gold_blocks", translationBuilder);
		translateCommonItemTag("titanium_gold_ingots", translationBuilder);
		translateCommonItemTag("titanium_gold_nuggets", translationBuilder);
		translateCommonItemTag("titanium_ingots", translationBuilder);
		translateCommonItemTag("titanium_nuggets", translationBuilder);
		translateCommonItemTag("titanium_sources", translationBuilder);
	}

	private void translateCommonItemTag(String tagPath, FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		translationBuilder.add("tag.item.c." + tagPath, AlloygeryEnglishLanguageProvider.englishNameFromPath(tagPath));
	}
}
