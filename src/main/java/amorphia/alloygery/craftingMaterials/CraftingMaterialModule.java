package amorphia.alloygery.craftingMaterials;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import amorphia.alloygery.craftingMaterials.client.CraftingMaterialClientReloadListener;
import amorphia.alloygery.craftingMaterials.datagen.*;
import amorphia.alloygery.craftingMaterials.item.DyeablePanelItem;
import amorphia.alloygery.craftingMaterials.item.IngotItem;
import amorphia.alloygery.craftingMaterials.item.NuggetItem;
import amorphia.alloygery.craftingMaterials.item.PanelItem;
import amorphia.alloygery.datagen.*;
import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static amorphia.alloygery.craftingMaterials.CraftingMaterialTypes.*;

public class CraftingMaterialModule
{
	public static Map<String, Block> BLOCKS = new LinkedHashMap<>();
	public static Map<String, Item> ITEMS = new LinkedHashMap<>();

	public static void initialize()
	{
		for(CraftingMaterial material : CraftingMaterials.VALUES_CACHE)
		{
			makeCraftingMaterials(material, material.getCraftingMaterialTypes(), material.getVariantType());
		}

		final ResourceLocation HUSK_LOOT_TABLE = Alloygery.asVanillaResource("entities/husk");
		LootTableEvents.MODIFY.register((resourceManager, lootDataManager, resourceLocation, builder, lootTableSource) -> {
			if (lootTableSource.isBuiltin() && resourceLocation.equals(HUSK_LOOT_TABLE))
			{
				LootPool.Builder pool = LootPool.lootPool();
				pool.setRolls(ConstantValue.exactly(1.0f));
				pool.setBonusRolls(ConstantValue.exactly(3.0f));
				pool.with(LootItem.lootTableItem(ITEMS.get("tin_ingot")).build());
				pool.conditionally(LootItemKilledByPlayerCondition.killedByPlayer().build());
				pool.conditionally(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.11f, 0.02f).build());
				builder.pool(pool.build());
			}
		});
	}

	public static void initializeClient()
	{
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(CraftingMaterialClientReloadListener.INSTANCE);
	}

	public static void initializeDataGen(FabricDataGenerator dataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryModelProvider.addProvider(new CraftingMaterialModelProvider());
		AlloygeryEnglishLanguageProvider.addProvider(new CraftingMaterialEnglishLanguageProvider());
		AlloygeryBlockLootTableProvider.addProvider(new CraftingMaterialBlockLootTableProvider());
		AlloygeryItemTagProvider.addProvider(CraftingMaterialTagProvider.INSTANCE);
		AlloygeryBlockTagProvider.addProvider(CraftingMaterialTagProvider.INSTANCE);
        AlloygeryRecipeProvider.addProvider(new CraftingMaterialRecipeProvider());
	}

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
			Item nugget = new NuggetItem(new Item.Properties(), material);
			ITEMS.put(material.getName() + "_nugget", nugget);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(nugget);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource(material.getName() + "_nugget"), nugget);
		}

		if (materialTypes.contains(INGOT))
		{
			Item ingot = new IngotItem(new Item.Properties(), material);
			ITEMS.put(material.getName() + "_ingot", ingot);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(ingot);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource(material.getName() + "_ingot"), ingot);
		}

		if (materialTypes.contains(PANEL))
		{
			//TODO: make this better
			final boolean dyeable = Stream.of(CraftingMaterials.LEATHER, CraftingMaterials.RABBIT_HIDE, CraftingMaterials.WOOL).anyMatch(m -> m == material);

			Item panel = dyeable ? new DyeablePanelItem(new Item.Properties(), material) : new PanelItem(new Item.Properties(), material);
			ITEMS.put(material.getName() + "_panel", panel);
			AlloygeryCreativeTabs.VANILLA_INGREDIENTS.add(panel);
			Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource(material.getName() + "_panel"), panel);
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
}
