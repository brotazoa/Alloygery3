package amorphia.alloygery.advancement;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.MachinesModule;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class GameChangesAdvancementsProvider extends FabricAdvancementProvider
{
	public GameChangesAdvancementsProvider(FabricDataOutput output)
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

		Advancement ironRequiresBronze = Advancement.Builder.recipeAdvancement().parent(root)
				.display(
						Items.IRON_ORE,
						Component.translatable("advancements.alloygery.iron_requires_bronze.title"),
						Component.translatable("advancements.alloygery.iron_requires_bronze.description"),
						null,
						FrameType.TASK,
						true,
						false,
						false
				)
				.addCriterion("wrong_tool", WrongToolForDropsTrigger.Conditions.wrongToolForDrops(
						BlockPredicate.Builder.block().of(BlockTags.IRON_ORES)
				))
				.build(Alloygery.asResource("game_changes/iron_requires_bronze"));

		// game changes
		consumer.accept(root);
		consumer.accept(ironRequiresBronze);
	}
}
