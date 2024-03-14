package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class PlacementModifiers
{
	public static final PlacementModifierType<ConfigPlacementFilter> CONFIG_FILTER;

	public static void init()
	{

	}

	static
	{
		CONFIG_FILTER = Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, Alloygery.asResource("config_filter"), () -> ConfigPlacementFilter.CODEC);
	}
}
