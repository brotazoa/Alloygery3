package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class Features
{
	public static final LayeredOreFeature LAYERED_ORE = new LayeredOreFeature();

	public static void init()
	{
		Registry.register(BuiltInRegistries.FEATURE, Alloygery.asResource("layered_ore"), LAYERED_ORE);
	}
}
