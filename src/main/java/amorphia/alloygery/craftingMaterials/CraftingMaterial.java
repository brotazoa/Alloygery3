package amorphia.alloygery.craftingMaterials;

import java.util.EnumSet;

public interface CraftingMaterial
{
	String getName();

	EnumSet<CraftingMaterialTypes> getCraftingMaterialTypes();

	CraftingMaterialVariantTypes getVariantType();
}
