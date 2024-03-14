package amorphia.alloygery.craftingMaterials;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import amorphia.alloygery.datagen.AlloygeryModelProvider;
import amorphia.alloygery.craftingMaterials.tinted.TintedItem;
import amorphia.alloygery.craftingMaterials.tinted.TintedItemReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static amorphia.alloygery.craftingMaterials.CraftingMaterialTypes.*;

public class CraftingMaterialModule
{
	public static Map<String, Block> BLOCKS = new LinkedHashMap<>();
	public static Map<String, Item> ITEMS = new LinkedHashMap<>();

	private static void makeCraftingMaterials(CraftingMaterial material, EnumSet<CraftingMaterialTypes> materialTypes, CraftingMaterialVariantTypes variantType)
	{
		if (materialTypes.contains(RAW_BLOCK))
		{
			Block block = new Block(FabricBlockSettings.copyOf(Blocks.STONE).requiresTool().strength(5.0f, 6.0f));
			BLOCKS.put("raw_" + material.getName() + "_block", block);
			BlockItem item = new BlockItem(block, new Item.Properties());
			item.registerBlocks(Item.BY_BLOCK, item);
			AlloygeryCreativeTabs.VANILLA_NATURAL_BLOCKS.add(item);
			Registry.register(BuiltInRegistries.BLOCK, Alloygery.asResource("raw_" + material.getName() + "_block"), block);
			Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), BuiltInRegistries.BLOCK.getKey(block)), item);
		}

		if (materialTypes.contains(RAW))
		{
			Item raw = new Item(new Item.Properties());
			ITEMS.put("raw_" + material.getName(), raw);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(raw);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource("raw_" + material.getName()), raw);
		}

		if (materialTypes.contains(RAW_NUGGET))
		{
			Item rawNugget = new Item(new Item.Properties());
			ITEMS.put("raw_" + material.getName() + "_nugget", rawNugget);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(rawNugget);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource("raw_" + material.getName() + "_nugget"), rawNugget);
		}

		if (materialTypes.contains(NUGGET))
		{
			// tinted item
			Item nugget = new TintedItem(material, NUGGET, variantType);
			ITEMS.put(material.getName() + "_nugget", nugget);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(nugget);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource(material.getName() + "_nugget"), nugget);
		}

		if (materialTypes.contains(INGOT))
		{
			// tinted item
			Item ingot = new TintedItem(material, INGOT, variantType);
			ITEMS.put(material.getName() + "_ingot", ingot);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(ingot);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource(material.getName() + "_ingot"), ingot);
		}

		if (materialTypes.contains(BLOCK))
		{
			Block block = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).requiresTool().strength(5.0f, 6.0f).sounds(SoundType.METAL));
			BLOCKS.put(material.getName() + "_block", block);
			BlockItem item = new BlockItem(block, new Item.Properties());
			item.registerBlocks(Item.BY_BLOCK, item);
			AlloygeryCreativeTabs.VANILLA_BUILDING_BLOCKS.add(item);
			Registry.register(BuiltInRegistries.BLOCK, Alloygery.asResource(material.getName() + "_block"), block);
			Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), BuiltInRegistries.BLOCK.getKey(block)), item);
		}
	}

	public static void initialize()
	{
		for(CraftingMaterial material : CraftingMaterials.VALUES_CACHE)
		{
			makeCraftingMaterials(material, material.getCraftingMaterialTypes(), material.getVariantType());
		}
	}

	public static void initializeClient()
	{
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(TintedItemReloadListener.INSTANCE);
	}

	public static void initializeDataGen(FabricDataGenerator dataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryModelProvider.addProvider(new CraftingMaterialModelProvider());
		AlloygeryEnglishLanguageProvider.addProvider(new CraftingMaterialEnglishLanguageProvider());
	}
}
