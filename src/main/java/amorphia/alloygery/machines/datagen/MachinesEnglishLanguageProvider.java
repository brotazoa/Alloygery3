package amorphia.alloygery.machines.datagen;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import amorphia.alloygery.machines.MachinesModule;
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
		translationBuilder.add("gui.recipebook.toggleRecipes.fireable", "Fireable");

		translationBuilder.add("container.alloygery.heat_exchanger", "Heat Exchanger");
		translationBuilder.add("container.alloygery.alloy_kiln_with_heat_exchanger", "Alloy Kiln");
		translationBuilder.add("container.alloygery.blast_furnace_with_heat_exchanger", "Blast Furnace");
		translationBuilder.add("container.alloygery.furnace_with_heat_exchanger", "Furnace");
		translationBuilder.add("container.alloygery.kiln_with_heat_exchanger", "Kiln");
		translationBuilder.add("container.alloygery.smoker_with_heat_exchanger", "Smoker");

        translationBuilder.add("tooltip.alloygery.heat_exchanger.heat_description", "Heat");
        translationBuilder.add("tooltip.alloygery.heat_exchanger.working_threshold_description", "Working Threshold");

        translationBuilder.add("container.alloygery.woodcutter", "Woodcutter");

        translationBuilder.add("container.alloygery.smithing_anvil", "Smithing Anvil");
        translationBuilder.add("tooltip.alloygery.smithing_anvil.not_enough_material", "Not Enough Material");
        translationBuilder.add("tooltip.alloygery.not_enough_material", "Not Enough Material");

        translationBuilder.add("container.alloygery.tailoring_table", "Tailoring Table");

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
        translateStat(MachinesModule.INTERACT_WITH_WOODCUTTER, translationBuilder);
        translateStat(MachinesModule.INTERACT_WITH_SMITING_ANVIL, translationBuilder);
	}

	private void translateStat(ResourceLocation stat, FabricLanguageProvider.TranslationBuilder builder)
	{
		builder.add(stat.toLanguageKey("stat"), AlloygeryEnglishLanguageProvider.englishNameFromResourceLocation(stat));
	}
}
