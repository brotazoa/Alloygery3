package amorphia.alloygery.gear.item;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.Locale;

public enum UpgradeTypes
{
    NETHERITE,
    ;

    public static final Codec<UpgradeTypes> CODEC = Codec.STRING.xmap(UpgradeTypes::getByName, UpgradeTypes::getName);

    public static final UpgradeTypes[] VALUES_CACHE = UpgradeTypes.values();

    public static UpgradeTypes getByName(String name)
    {
        return Arrays.stream(VALUES_CACHE).filter(v -> v.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    public String getName()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
