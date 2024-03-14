package amorphia.alloygery.craftingMaterials;

import java.util.EnumSet;

public interface CraftingMaterial
{
	String getName();

	int getColor();

	EnumSet<CraftingMaterialTypes> getCraftingMaterialTypes();

	CraftingMaterialVariantTypes getVariantType();
}
