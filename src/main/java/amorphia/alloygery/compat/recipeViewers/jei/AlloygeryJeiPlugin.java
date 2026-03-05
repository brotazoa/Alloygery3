package amorphia.alloygery.compat.recipeViewers.jei;

import amorphia.alloygery.Alloygery;
import mezz.jei.api.IModPlugin;
import net.minecraft.resources.ResourceLocation;

public class AlloygeryJeiPlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Alloygery.asResource("jei_plugin");
	}
}
