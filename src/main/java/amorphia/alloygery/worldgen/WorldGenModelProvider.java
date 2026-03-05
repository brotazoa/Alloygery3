package amorphia.alloygery.worldgen;

import amorphia.alloygery.datagen.AlloygeryModelProvider;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.blockstates.VariantProperty;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class WorldGenModelProvider implements AlloygeryModelProvider.IAlloygeryModelProvider
{
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator)
	{
		blockStateModelGenerator.createTrivialCube(WorldGenModule.TIN_ORE);
		blockStateModelGenerator.createTrivialCube(WorldGenModule.DEEPSLATE_TIN_ORE);

		createVariantOre(WorldGenModule.TEALLITE, blockStateModelGenerator);
		createVariantOre(WorldGenModule.CUPROLINE, blockStateModelGenerator);
		createVariantOre(WorldGenModule.FERONYTE, blockStateModelGenerator);
		createVariantOre(WorldGenModule.AURORUM, blockStateModelGenerator);
		createVariantOre(WorldGenModule.NICKELINE, blockStateModelGenerator);
		createVariantOre(WorldGenModule.TITANITE, blockStateModelGenerator);
	}

	public void generateItemModels(ItemModelGenerators itemModelGenerator)
	{

	}

	private static void createVariantOre(Block block, BlockModelGenerators generator)
	{
		ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), generator.modelOutput);
		generator.createSuffixedVariant(block, "_0", ModelTemplates.CUBE_ALL, TextureMapping::cube);
		generator.createSuffixedVariant(block, "_1", ModelTemplates.CUBE_ALL, TextureMapping::cube);
		generator.createSuffixedVariant(block, "_2", ModelTemplates.CUBE_ALL, TextureMapping::cube);
		generator.createSuffixedVariant(block, "_3", ModelTemplates.CUBE_ALL, TextureMapping::cube);

		generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(
				block,
				new Variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_0")),
				new Variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_1")),
				new Variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_2")),
				new Variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_3"))
		));
	}
}
