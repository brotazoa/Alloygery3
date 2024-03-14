package amorphia.alloygery.craftingMaterials;

import java.util.Arrays;
import java.util.Locale;

public enum CraftingMaterialVariantTypes
{
	DULL,
	NORMAL,
	SHINY,
	;

	public static final CraftingMaterialVariantTypes[] VALUES_CACHE = CraftingMaterialVariantTypes.values();

	public static CraftingMaterialVariantTypes byName(String name)
	{
		return Arrays.stream(VALUES_CACHE).filter(v -> v.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
	}

	public String getName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
}
