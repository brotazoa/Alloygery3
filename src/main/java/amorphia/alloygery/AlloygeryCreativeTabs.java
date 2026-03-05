package amorphia.alloygery;

import net.minecraft.world.item.CreativeModeTabs;

public class AlloygeryCreativeTabs
{
	public static final VanillaCreativeTabWrapper VANILLA_BUILDING_BLOCKS = new VanillaCreativeTabWrapper(CreativeModeTabs.BUILDING_BLOCKS);
	public static final VanillaCreativeTabWrapper VANILLA_NATURAL_BLOCKS = new VanillaCreativeTabWrapper(CreativeModeTabs.NATURAL_BLOCKS);
	public static final VanillaCreativeTabWrapper VANILLA_INGREDIENTS = new VanillaCreativeTabWrapper(CreativeModeTabs.INGREDIENTS);
	public static final VanillaCreativeTabWrapper VANILLA_FUNCTIONAL_BLOCKS = new VanillaCreativeTabWrapper(CreativeModeTabs.FUNCTIONAL_BLOCKS);
	public static final VanillaCreativeTabWrapper VANILLA_TOOLS = new VanillaCreativeTabWrapper(CreativeModeTabs.TOOLS_AND_UTILITIES);
	public static final VanillaCreativeTabWrapper VANILLA_COMBAT = new VanillaCreativeTabWrapper(CreativeModeTabs.COMBAT);

	public static final AlloygeryCreativeTab ALLOYGERY_PART_ITEMS = new AlloygeryCreativeTab(Alloygery.asResource("part_items_tab"));

	public static void initialize()
	{
		VANILLA_BUILDING_BLOCKS.build();
		VANILLA_NATURAL_BLOCKS.build();
		VANILLA_INGREDIENTS.build();
		VANILLA_FUNCTIONAL_BLOCKS.build();
		VANILLA_TOOLS.build();
		VANILLA_COMBAT.build();

		ALLOYGERY_PART_ITEMS.build();
	}
}
