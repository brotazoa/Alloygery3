package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.advancement.BlockBrokenTrigger;
import amorphia.alloygery.machines.MachinesModule;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class WorldGenAdvancementsProvider extends FabricAdvancementProvider
{
	protected WorldGenAdvancementsProvider(FabricDataOutput output)
	{
		super(output);
	}

	@Override
	public void generateAdvancement(Consumer<Advancement> consumer)
	{
		Advancement root = Advancement.Builder.recipeAdvancement()
				.display(
						MachinesModule.ALLOY_KILN,
						Component.translatable("advancements.alloygery.game_changes.title"),
						Component.translatable("advancements.alloygery.game_changes.description"),
						Alloygery.asResource("textures/gui/advancements/bricks.png"),
						FrameType.TASK,
						false,
						false,
						false
				)
				.addCriterion("alloygery_installed", PlayerTrigger.TriggerInstance.tick())
				.build(Alloygery.asResource("game_changes/root"));

		Advancement veinsOfOre = Advancement.Builder.recipeAdvancement().parent(root)
				.display(
						WorldGenModule.CUPROLINE,
						Component.translatable("advancements.alloygery.game_changes.veins_of_ore.title"),
						Component.translatable("advancements.alloygery.game_changes.veins_of_ore.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				).addCriterion("mine_any_ore_vein", BlockBrokenTrigger.Conditions.blockBroken(
						BlockPredicate.Builder.block().of(
								WorldGenModule.AURORUM,
								WorldGenModule.CUPROLINE,
								WorldGenModule.FERONYTE,
								WorldGenModule.NICKELINE,
								WorldGenModule.TEALLITE,
								WorldGenModule.TITANITE
						),
						true
				)).build(Alloygery.asResource("game_changes/veins_of_ore"));

		consumer.accept(root);
		consumer.accept(veinsOfOre);
	}
}
