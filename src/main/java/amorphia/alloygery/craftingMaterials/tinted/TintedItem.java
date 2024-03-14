package amorphia.alloygery.craftingMaterials.tinted;

import amorphia.alloygery.craftingMaterials.CraftingMaterialTypes;
import amorphia.alloygery.craftingMaterials.CraftingMaterialVariantTypes;
import amorphia.alloygery.craftingMaterials.CraftingMaterial;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;

import java.util.List;

public class TintedItem extends Item
{
	public static final List<TintedItem> ITEMS = Lists.newArrayList();

	private final CraftingMaterial craftingMaterial;
	private final CraftingMaterialTypes materialType;
	private final CraftingMaterialVariantTypes variant;

	public TintedItem(CraftingMaterial craftingMaterial, CraftingMaterialTypes materialType)
	{
		this(craftingMaterial, materialType, CraftingMaterialVariantTypes.NORMAL);
	}

	public TintedItem(CraftingMaterial craftingMaterial, CraftingMaterialTypes materialType, CraftingMaterialVariantTypes variant)
	{
		this(craftingMaterial, materialType, variant, new Item.Properties());
	}

	public TintedItem(CraftingMaterial craftingMaterial, CraftingMaterialTypes materialType, CraftingMaterialVariantTypes variant, Item.Properties properties)
	{
		super(properties);
		this.craftingMaterial = craftingMaterial;
		this.materialType = materialType;
		this.variant = variant;
		ITEMS.add(this);
	}

	public CraftingMaterial getCraftingMaterial()
	{
		return craftingMaterial;
	}

	public CraftingMaterialTypes getMaterialType()
	{
		return materialType;
	}

	public CraftingMaterialVariantTypes getVariant()
	{
		return variant;
	}
}
