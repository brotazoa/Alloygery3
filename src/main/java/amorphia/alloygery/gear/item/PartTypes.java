package amorphia.alloygery.gear.item;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.Locale;

public enum PartTypes
{
    TOOL_TYPE,
    TOOL_HEAD,
    TOOL_BINDING,
    TOOL_HANDLE,
    TOOL_IMPROVEMENT,
    TOOL_UPGRADE,

    BOW_TYPE,
    BOW_LIMB,
    BOW_BINDING,
    BOW_STRING,
    BOW_IMPROVEMENT,
    BOW_UPGRADE,

    ARMOR_TYPE,
    ARMOR_BASE,
    ARMOR_PLATE,
    ARMOR_IMPROVEMENT,
    ARMOR_TRIM,
    ARMOR_UPGRADE,
	ARMOR_STYLE,

    SHIELD_TYPE,
    SHIELD_BASE,
    SHIELD_PLATE,
    SHIELD_IMPROVEMENT,
    SHIELD_UPGRADE,
    ;

    public static final Codec<PartTypes> CODEC = Codec.STRING.xmap(PartTypes::getByName, PartTypes::getName);

    public static final PartTypes[] VALUES_CACHE = PartTypes.values();

    public static PartTypes getByName(String name)
    {
        return Arrays.stream(VALUES_CACHE).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

	public static boolean isImprovement(PartTypes part)
	{
		return part == TOOL_IMPROVEMENT || part == ARMOR_IMPROVEMENT || part == BOW_IMPROVEMENT || part == SHIELD_IMPROVEMENT;
	}

	public static boolean isUpgrade(PartTypes part)
	{
		return part == TOOL_UPGRADE || part == ARMOR_UPGRADE || part == BOW_UPGRADE || part == SHIELD_UPGRADE;
	}

    public String getName()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
