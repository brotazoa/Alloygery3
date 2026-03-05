package amorphia.alloygery.craftingMaterials.item;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterial;
import amorphia.alloygery.craftingMaterials.datagen.CraftingMaterialModelProvider;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class PanelItem extends Item implements CraftingMaterialModelProvider.IItemModelGen
{
	protected final CraftingMaterial craftingMaterial;

	public PanelItem(Properties properties, CraftingMaterial craftingMaterial)
	{
		super(properties);
		this.craftingMaterial = craftingMaterial;
	}

	@Override
	public void generateItemModel(ItemModelGenerators itemModelGenerator)
	{
		ResourceLocation texture = Alloygery.asResource("template/crafting_material/" + craftingMaterial.getVariantType().getName() + "_panel_template_" + craftingMaterial.getName());
		ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(this), TextureMapping.layer0(texture), itemModelGenerator.output);
	}
}
