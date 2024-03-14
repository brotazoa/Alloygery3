package amorphia.alloygery.machines;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.resources.ResourceLocation;

public class MachinesEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		// container translations
		translationBuilder.add("container.alloygery.alloy_kiln", "Alloy Kiln");
		translationBuilder.add("container.alloygery.kiln", "Kiln");

		translationBuilder.add("container.alloygery.heat_exchanger", "Heat Exchanger");
		translationBuilder.add("container.alloygery.alloy_kiln_with_heat_exchanger", "Alloy Kiln With Heat Exchanger");
		translationBuilder.add("container.alloygery.blast_furnace_with_heat_exchanger", "Blast Furnace With Heat Exchanger");
		translationBuilder.add("container.alloygery.furnace_with_heat_exchanger", "Furnace With Heat Exchanger");
		translationBuilder.add("container.alloygery.kiln_with_heat_exchanger", "Kiln With Heat Exchanger");
		translationBuilder.add("container.alloygery.smoker_with_heat_exchanger", "Smoker With Heat Exchanger");

		// blocks
		MachinesModule.BLOCKS.forEach((path, block) -> translationBuilder.add(block, AlloygeryEnglishLanguageProvider.englishNameFromPath(path)));

		// stats
		translateStat(MachinesModule.INTERACT_WITH_ALLOY_KILN, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_KILN, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_HEAT_EXCHANGER, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_ALLOY_KILN_WITH_HEAT_EXCHANGER, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_BLAST_FURNACE_WITH_HEAT_EXCHANGER, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_FURNACE_WITH_HEAT_EXCHANGER, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_KILN_WITH_HEAT_EXCHANGER, translationBuilder);
		translateStat(MachinesModule.INTERACT_WITH_SMOKER_WITH_HEAT_EXCHANGER, translationBuilder);
	}

	private void translateStat(ResourceLocation stat, FabricLanguageProvider.TranslationBuilder builder)
	{
		builder.add(stat.toLanguageKey("stat"), AlloygeryEnglishLanguageProvider.englishNameFromResourceLocation(stat));
	}
}
