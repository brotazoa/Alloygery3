package amorphia.alloygery.craftingMaterials;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryModelProvider;
import amorphia.alloygery.craftingMaterials.tinted.TintedItem;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CraftingMaterialModelProvider implements AlloygeryModelProvider.IAlloygeryModelProvider
{
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator)
	{
		for(Block block : CraftingMaterialModule.BLOCKS.values())
		{
			blockStateModelGenerator.createTrivialCube(block);
		}
	}

	public void generateItemModels(ItemModelGenerators itemModelGenerator)
	{
		for(Item item : CraftingMaterialModule.ITEMS.values())
		{
			if (item instanceof TintedItem tintedItem)
			{
				ResourceLocation texture = Alloygery.asResource("item/template/" + tintedItem.getMaterialType().getName() + "/" + tintedItem.getVariant().getName() + "_template");
				ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(texture), itemModelGenerator.output);
			}
			else
				itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
		}
	}
}
