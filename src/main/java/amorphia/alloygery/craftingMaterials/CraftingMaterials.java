package amorphia.alloygery.craftingMaterials;

import java.util.Arrays;
import java.util.EnumSet;

import static amorphia.alloygery.craftingMaterials.CraftingMaterialTypes.*;
import static amorphia.alloygery.craftingMaterials.CraftingMaterialVariantTypes.*;

public enum CraftingMaterials implements CraftingMaterial
{
	TIN("tin", 14547455, EnumSet.of(RAW, RAW_NUGGET, RAW_BLOCK, NUGGET, INGOT, BLOCK)),
	COPPER("copper", 15433553, EnumSet.of(RAW_NUGGET, NUGGET)),
	BRONZE("bronze", 7556410, EnumSet.of(NUGGET, INGOT, BLOCK), DULL),
	IRON("iron", 15198183, EnumSet.of(RAW_NUGGET)),
	GOLD("gold", 16573743, EnumSet.of(RAW_NUGGET), SHINY),
	ANTANIUM("antanium", 14329677, EnumSet.of(NUGGET, INGOT, BLOCK), SHINY),
	STEEL("steel", 4408907, EnumSet.of(NUGGET, INGOT, BLOCK), DULL),
	NICKEL("nickel", 6314062, EnumSet.of(RAW, RAW_NUGGET, RAW_BLOCK, NUGGET, INGOT, BLOCK), DULL),
	INVAR("invar", 10789019, EnumSet.of(NUGGET, INGOT, BLOCK), DULL),
	CONSTANTAN("constantan", 11558984, EnumSet.of(NUGGET, INGOT, BLOCK)),
	TITANIUM("titanium", 5990506, EnumSet.of(RAW, RAW_NUGGET, RAW_BLOCK, NUGGET, INGOT, BLOCK)),
	TITANIUM_GOLD("titanium_gold", 13086590, EnumSet.of(NUGGET, INGOT, BLOCK), SHINY),
	NITINOL("nitinol", 6185051, EnumSet.of(NUGGET, INGOT, BLOCK)),
	;

	public static final CraftingMaterials[] VALUES_CACHE = CraftingMaterials.values();

	public static CraftingMaterial byName(String name)
	{
		return Arrays.stream(VALUES_CACHE).filter(v -> v.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
	}

	private final String name;
	private final int color;
	private final EnumSet<CraftingMaterialTypes> materialTypes;
	private final CraftingMaterialVariantTypes variantType;

	CraftingMaterials(String name, int color, EnumSet<CraftingMaterialTypes> materialTypes)
	{
		this(name, color, materialTypes, CraftingMaterialVariantTypes.NORMAL);
	}

	CraftingMaterials(String name, int color, EnumSet<CraftingMaterialTypes> materialTypes, CraftingMaterialVariantTypes variantType)
	{
		this.name = name;
		this.color = color;
		this.materialTypes = materialTypes;
		this.variantType = variantType;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public int getColor()
	{
		return this.color;
	}

	@Override
	public EnumSet<CraftingMaterialTypes> getCraftingMaterialTypes()
	{
		return this.materialTypes;
	}

	@Override
	public CraftingMaterialVariantTypes getVariantType()
	{
		return this.variantType;
	}
}
