package amorphia.alloygery.craftingMaterials;

import java.util.Arrays;
import java.util.EnumSet;

import static amorphia.alloygery.craftingMaterials.CraftingMaterialTypes.*;
import static amorphia.alloygery.craftingMaterials.CraftingMaterialVariantTypes.*;

public enum CraftingMaterials implements CraftingMaterial
{
	TIN("tin", EnumSet.of(RAW, RAW_NUGGET, RAW_BLOCK, NUGGET, INGOT, BLOCK, PANEL)),
	COPPER("copper", EnumSet.of(RAW_NUGGET, NUGGET, PANEL), SHINY),
	BRONZE("bronze", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL), DULL),
	IRON("iron", EnumSet.of(RAW_NUGGET, PANEL)),
	GOLD("gold", EnumSet.of(RAW_NUGGET, PANEL), SHINY),
	ANTANIUM("antanium", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL), SHINY),
	STEEL("steel", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL), DULL),
	NICKEL("nickel", EnumSet.of(RAW, RAW_NUGGET, RAW_BLOCK, NUGGET, INGOT, BLOCK, PANEL), DULL),
	INVAR("invar", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL), DULL),
	CONSTANTAN("constantan", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL)),
	TITANIUM("titanium", EnumSet.of(RAW, RAW_NUGGET, RAW_BLOCK, NUGGET, INGOT, BLOCK, PANEL)),
	TITANIUM_GOLD("titanium_gold", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL), SHINY),
	NITINOL("nitinol", EnumSet.of(NUGGET, INGOT, BLOCK, PANEL)),

	NETHERITE("netherite", EnumSet.of(PANEL), DULL),

	LEATHER("leather", EnumSet.of(PANEL)),
	RABBIT_HIDE("rabbit_hide", EnumSet.of(PANEL)),
	WOOL("wool", EnumSet.of(PANEL)),
	;

	public static final CraftingMaterials[] VALUES_CACHE = CraftingMaterials.values();

	public static CraftingMaterial byName(String name)
	{
		return Arrays.stream(VALUES_CACHE).filter(v -> v.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
	}

	private final String name;
	private final EnumSet<CraftingMaterialTypes> materialTypes;
	private final CraftingMaterialVariantTypes variantType;

	CraftingMaterials(String name, EnumSet<CraftingMaterialTypes> materialTypes)
	{
		this(name, materialTypes, CraftingMaterialVariantTypes.NORMAL);
	}

	CraftingMaterials(String name, EnumSet<CraftingMaterialTypes> materialTypes, CraftingMaterialVariantTypes variantType)
	{
		this.name = name;
		this.materialTypes = materialTypes;
		this.variantType = variantType;
	}

	@Override
	public String getName()
	{
		return this.name;
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
