package amorphia.alloygery.craftingMaterials;

import java.util.Arrays;
import java.util.Locale;

public enum CraftingMaterialTypes
{
	RAW,
	RAW_NUGGET,
	RAW_BLOCK,
	NUGGET,
	INGOT,
	BLOCK,
	;

	public static final CraftingMaterialTypes[] VALUES_CACHE = CraftingMaterialTypes.values();

	public static CraftingMaterialTypes byName(String name)
	{
		return Arrays.stream(VALUES_CACHE).filter(v -> v.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
	}

	public String getName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
}
