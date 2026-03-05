package amorphia.alloygery.gear.item;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.Locale;

public enum ToolTypes
{
    // basic
    HATCHET,
    KNIFE,

    // vanilla
    AXE,
    HOE,
    PICKAXE,
    SHOVEL,
    SWORD,

    // aoe
    SCYTHE,
    HAMMER,
    EXCAVATOR,

    // melee / ranged
    SPEAR,

    // bows
    BOW,
    LONGBOW,
    RECURVE,
    CROSSBOW,

    // shield
    SHIELD,
    BUCKLER,
    ;

    public static final Codec<ToolTypes> CODEC = Codec.STRING.xmap(ToolTypes::getByName, ToolTypes::getName);

    public static final ToolTypes[] VALUES_CACHE = ToolTypes.values();

    public static ToolTypes getByName(String name)
    {
        return Arrays.stream(VALUES_CACHE).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    public String getName()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
