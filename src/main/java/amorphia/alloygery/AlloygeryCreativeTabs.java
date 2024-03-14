package amorphia.alloygery;

import net.minecraft.world.item.CreativeModeTabs;

public class AlloygeryCreativeTabs
{
	public static final IAlloygeryCreativeTab VANILLA_BUILDING_BLOCKS = new VanillaCreativeTabWrapper(CreativeModeTabs.BUILDING_BLOCKS);
	public static final IAlloygeryCreativeTab VANILLA_NATURAL_BLOCKS = new VanillaCreativeTabWrapper(CreativeModeTabs.NATURAL_BLOCKS);
	public static final IAlloygeryCreativeTab VANILLA_INGREDIENTS = new VanillaCreativeTabWrapper(CreativeModeTabs.INGREDIENTS);
	public static final IAlloygeryCreativeTab VANILLA_FUNCTIONAL_BLOCKS = new VanillaCreativeTabWrapper(CreativeModeTabs.FUNCTIONAL_BLOCKS);

	public static void initialize()
	{
		VANILLA_BUILDING_BLOCKS.build();
		VANILLA_NATURAL_BLOCKS.build();
		VANILLA_INGREDIENTS.build();
		VANILLA_FUNCTIONAL_BLOCKS.build();
	}
}
