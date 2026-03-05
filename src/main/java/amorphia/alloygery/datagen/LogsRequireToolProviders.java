package amorphia.alloygery.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.advancement.WrongToolForDropsTrigger;
import amorphia.alloygery.machines.MachinesModule;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class LogsRequireToolProviders
{
	public static class PackMetadataProvider extends PackMetadataGenerator
	{
		public PackMetadataProvider(PackOutput output)
		{
			super(output);
			Component description = Component.translatable("pack.alloygery.logs_require_tool.description");
			add(PackMetadataSection.TYPE, new PackMetadataSection(description, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA)));
		}
	}

	public static class LogsRequireToolTagProvider extends FabricTagProvider.BlockTagProvider
	{
		public static final TagKey<Block> LOG_BLOCKS = TagKey.create(Registries.BLOCK, Alloygery.asResource("log_blocks_require_tool"));

		public LogsRequireToolTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture)
		{
			super(output, registriesFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			getOrCreateTagBuilder(LOG_BLOCKS).add(
					Blocks.ACACIA_LOG,
					Blocks.ACACIA_WOOD,
					Blocks.STRIPPED_ACACIA_LOG,
					Blocks.STRIPPED_ACACIA_WOOD,

					Blocks.BIRCH_LOG,
					Blocks.BIRCH_WOOD,
					Blocks.STRIPPED_BIRCH_LOG,
					Blocks.STRIPPED_BIRCH_WOOD,

					Blocks.CHERRY_LOG,
					Blocks.CHERRY_WOOD,
					Blocks.STRIPPED_CHERRY_LOG,
					Blocks.STRIPPED_CHERRY_WOOD,

					Blocks.DARK_OAK_LOG,
					Blocks.DARK_OAK_WOOD,
					Blocks.STRIPPED_DARK_OAK_LOG,
					Blocks.STRIPPED_DARK_OAK_WOOD,

					Blocks.JUNGLE_LOG,
					Blocks.JUNGLE_WOOD,
					Blocks.STRIPPED_JUNGLE_LOG,
					Blocks.STRIPPED_JUNGLE_WOOD,

					Blocks.MANGROVE_LOG,
					Blocks.MANGROVE_WOOD,
					Blocks.STRIPPED_MANGROVE_LOG,
					Blocks.STRIPPED_MANGROVE_WOOD,

					Blocks.OAK_LOG,
					Blocks.OAK_WOOD,
					Blocks.STRIPPED_OAK_LOG,
					Blocks.STRIPPED_OAK_WOOD,

					Blocks.SPRUCE_LOG,
					Blocks.SPRUCE_WOOD,
					Blocks.STRIPPED_SPRUCE_LOG,
					Blocks.STRIPPED_SPRUCE_WOOD
			);
		}
	}

	public static class AdvancementsProvider extends FabricAdvancementProvider
	{
		public AdvancementsProvider(FabricDataOutput output)
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

			Advancement logsRequireTool = Advancement.Builder.recipeAdvancement().parent(root)
					.display(
							Items.OAK_LOG,
							Component.translatable("advancements.alloygery.logs_require_tool.title"),
							Component.translatable("advancements.alloygery.logs_require_tool.description"),
							null,
							FrameType.TASK,
							true,
							false,
							false
					)
					.addCriterion("wrong_tool", WrongToolForDropsTrigger.Conditions.wrongToolForDrops(
							BlockPredicate.Builder.block().of(LogsRequireToolTagProvider.LOG_BLOCKS)
					))
					.build(Alloygery.asResource("game_changes/logs_require_tool"));

			consumer.accept(root);
			consumer.accept(logsRequireTool);
		}
	}
}
