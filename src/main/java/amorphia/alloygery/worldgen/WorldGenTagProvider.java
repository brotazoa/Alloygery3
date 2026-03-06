package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class WorldGenTagProvider implements AlloygeryBlockTagProvider.IAlloygeryBlockTagProvider
{
	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(WorldGenModule.OVERWORLD_SURFACE_ORE_REPLACEABLES).add(
				Blocks.DIRT,
				Blocks.GRASS_BLOCK,
				Blocks.SAND,
				Blocks.RED_SAND,
				Blocks.PODZOL,
				Blocks.COARSE_DIRT,
				Blocks.ROOTED_DIRT,
				Blocks.MUD
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_1"))).add(
				WorldGenModule.AURORUM,
				WorldGenModule.CUPROLINE,
				WorldGenModule.TEALLITE,
				WorldGenModule.TIN_ORE,
				WorldGenModule.DEEPSLATE_TIN_ORE
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_2"))).add(
				WorldGenModule.FERONYTE
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_3"))).add(
				WorldGenModule.NICKELINE
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_4"))).add(
				WorldGenModule.TITANITE
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/pickaxe"))).add(
				WorldGenModule.TIN_ORE,
				WorldGenModule.DEEPSLATE_TIN_ORE,
				WorldGenModule.TEALLITE,
				WorldGenModule.CUPROLINE,
				WorldGenModule.FERONYTE,
				WorldGenModule.AURORUM,
				WorldGenModule.NICKELINE,
				WorldGenModule.TITANITE
		);
	}
}
