package amorphia.alloygery.craftingMaterials.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterial;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.craftingMaterials.CraftingMaterialTypes;
import amorphia.alloygery.craftingMaterials.CraftingMaterials;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class CraftingMaterialTagProvider implements AlloygeryItemTagProvider.IAlloygeryItemTagProvider, AlloygeryBlockTagProvider.IAlloygeryBlockTagProvider
{
	public static final CraftingMaterialTagProvider INSTANCE = new CraftingMaterialTagProvider();

	@Override
	public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(BlockTags.NEEDS_STONE_TOOL).add(
				CraftingMaterialModule.BLOCKS.get("raw_tin_block"),
				CraftingMaterialModule.BLOCKS.get("tin_block"),
				CraftingMaterialModule.BLOCKS.get("bronze_block")
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_1"))).add(
				CraftingMaterialModule.BLOCKS.get("raw_tin_block"),
				CraftingMaterialModule.BLOCKS.get("tin_block"),
				CraftingMaterialModule.BLOCKS.get("bronze_block")
		);

		provider.tagBuilderOf(BlockTags.NEEDS_IRON_TOOL).add(
				CraftingMaterialModule.BLOCKS.get("antanium_block"),
				CraftingMaterialModule.BLOCKS.get("steel_block"),
				Blocks.IRON_BLOCK,
				Blocks.RAW_IRON_BLOCK,
				Blocks.IRON_ORE,
				Blocks.DEEPSLATE_IRON_ORE
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_2"))).add(
				CraftingMaterialModule.BLOCKS.get("antanium_block"),
				CraftingMaterialModule.BLOCKS.get("steel_block"),
				Blocks.IRON_BLOCK,
				Blocks.RAW_IRON_BLOCK,
				Blocks.IRON_ORE,
				Blocks.DEEPSLATE_IRON_ORE
		);

		provider.tagBuilderOf(BlockTags.NEEDS_DIAMOND_TOOL).add(
				CraftingMaterialModule.BLOCKS.get("raw_nickel_block"),
				CraftingMaterialModule.BLOCKS.get("nickel_block"),
				CraftingMaterialModule.BLOCKS.get("invar_block"),
				CraftingMaterialModule.BLOCKS.get("constantan_block")
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_3"))).add(
				CraftingMaterialModule.BLOCKS.get("raw_nickel_block"),
				CraftingMaterialModule.BLOCKS.get("nickel_block"),
				CraftingMaterialModule.BLOCKS.get("invar_block"),
				CraftingMaterialModule.BLOCKS.get("constantan_block")
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asFabricResource("needs_tool_level_4"))).add(
				CraftingMaterialModule.BLOCKS.get("raw_titanium_block"),
				CraftingMaterialModule.BLOCKS.get("titanium_block"),
				CraftingMaterialModule.BLOCKS.get("titanium_gold_block"),
				CraftingMaterialModule.BLOCKS.get("nitinol_block")
		);

		provider.tagBuilderOf(TagKey.create(Registries.BLOCK, Alloygery.asVanillaResource("mineable/pickaxe"))).add(
				CraftingMaterialModule.BLOCKS.get("raw_tin_block"),
				CraftingMaterialModule.BLOCKS.get("tin_block"),
				CraftingMaterialModule.BLOCKS.get("bronze_block"),
				CraftingMaterialModule.BLOCKS.get("antanium_block"),
				CraftingMaterialModule.BLOCKS.get("steel_block"),
				CraftingMaterialModule.BLOCKS.get("raw_nickel_block"),
				CraftingMaterialModule.BLOCKS.get("nickel_block"),
				CraftingMaterialModule.BLOCKS.get("invar_block"),
				CraftingMaterialModule.BLOCKS.get("constantan_block"),
				CraftingMaterialModule.BLOCKS.get("raw_titanium_block"),
				CraftingMaterialModule.BLOCKS.get("titanium_block"),
				CraftingMaterialModule.BLOCKS.get("titanium_gold_block"),
				CraftingMaterialModule.BLOCKS.get("nitinol_block")
		);
	}

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        for(CraftingMaterial material : CraftingMaterials.VALUES_CACHE)
        {
            if(material.getCraftingMaterialTypes().contains(CraftingMaterialTypes.INGOT))
                makeCommonIngotTag(material, provider);

            if(material.getCraftingMaterialTypes().contains(CraftingMaterialTypes.NUGGET))
                makeCommonNuggetTag(material, provider);

            if(material.getCraftingMaterialTypes().contains(CraftingMaterialTypes.RAW_NUGGET))
                makeCommonRawNuggetTag(material, provider);

            if(material.getCraftingMaterialTypes().contains(CraftingMaterialTypes.RAW))
                makeCommonRawOreTag(material, provider);

            if(material.getCraftingMaterialTypes().contains(CraftingMaterialTypes.BLOCK))
                makeCommonBlockTag(material, provider);

            if(material.getCraftingMaterialTypes().contains(CraftingMaterialTypes.RAW_BLOCK))
                makeCommonRawBlockTag(material, provider);
        }

        makeCommonTag(provider, "copper_ingots", Items.COPPER_INGOT);
        makeCommonTag(provider, "gold_ingots", Items.GOLD_INGOT);
        makeCommonTag(provider, "iron_ingots", Items.IRON_INGOT);
		makeCommonTag(provider, "ingots", Items.NETHERITE_INGOT);
		makeCommonTag(provider, "netherite_ingots", Items.NETHERITE_INGOT);
        makeCommonTag(provider, "hides", Items.LEATHER, Items.RABBIT_HIDE);
		makeCommonTag(provider, "stripped_logs",
				Items.STRIPPED_ACACIA_LOG,
				Items.STRIPPED_BIRCH_LOG,
				Items.STRIPPED_CHERRY_LOG,
				Items.STRIPPED_CRIMSON_STEM,
				Items.STRIPPED_DARK_OAK_LOG,
				Items.STRIPPED_JUNGLE_LOG,
				Items.STRIPPED_MANGROVE_LOG,
				Items.STRIPPED_OAK_LOG,
				Items.STRIPPED_SPRUCE_LOG,
				Items.STRIPPED_WARPED_STEM
		);

        // sources
        makeCommonTag(provider, "tin_sources", CraftingMaterialModule.ITEMS.get("raw_tin"), CraftingMaterialModule.ITEMS.get("tin_ingot"));
        makeCommonTag(provider, "nickel_sources", CraftingMaterialModule.ITEMS.get("raw_nickel"), CraftingMaterialModule.ITEMS.get("nickel_ingot"));
        makeCommonTag(provider, "titanium_sources", CraftingMaterialModule.ITEMS.get("raw_titanium"), CraftingMaterialModule.ITEMS.get("titanium_ingot"));
        makeCommonTag(provider, "copper_sources", Items.RAW_COPPER, Items.COPPER_INGOT);
        makeCommonTag(provider, "iron_sources", Items.RAW_IRON, Items.IRON_INGOT);
        makeCommonTag(provider, "gold_sources", Items.RAW_GOLD, Items.GOLD_INGOT);
        makeCommonTag(provider, "carbon_sources", Items.CHARCOAL);
		makeCommonTag(provider, "crystals", Items.AMETHYST_SHARD, Items.DIAMOND, Items.EMERALD, Items.LAPIS_LAZULI, Items.REDSTONE, Items.QUARTZ);
    }

    private void makeCommonTag(AlloygeryItemTagProvider provider, String tagName, Item... items)
    {
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource(tagName))).add(items);
    }

    private void makeCommonIngotTag(CraftingMaterial material, AlloygeryItemTagProvider provider)
    {
		provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("ingots"))).add(Alloygery.asResource(material.getName() + "_ingot"));
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource(material.getName() + "_ingots"))).add(Alloygery.asResource(material.getName() + "_ingot"));
    }

    private void makeCommonNuggetTag(CraftingMaterial material, AlloygeryItemTagProvider provider)
    {
		provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("nuggets"))).add(Alloygery.asResource(material.getName() + "_nugget"));
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource(material.getName() + "_nuggets"))).add(Alloygery.asResource(material.getName() + "_nugget"));
    }

    private void makeCommonRawNuggetTag(CraftingMaterial material, AlloygeryItemTagProvider provider)
    {
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("raw_" + material.getName() + "_nuggets"))).add(Alloygery.asResource("raw_" + material.getName() + "_nugget"));
    }

    private void makeCommonRawOreTag(CraftingMaterial material, AlloygeryItemTagProvider provider)
    {
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("raw_" + material.getName() + "_ores"))).add(Alloygery.asResource("raw_" + material.getName()));
    }

    private void makeCommonBlockTag(CraftingMaterial material, AlloygeryItemTagProvider provider)
    {
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource(material.getName() + "_blocks"))).add(Alloygery.asResource(material.getName() + "_block"));
    }

    private void makeCommonRawBlockTag(CraftingMaterial material, AlloygeryItemTagProvider provider)
    {
        provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("raw_" + material.getName() + "_blocks"))).add(Alloygery.asResource("raw_" + material.getName() + "_block"));
    }
}
