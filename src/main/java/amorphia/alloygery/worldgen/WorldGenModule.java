package amorphia.alloygery.worldgen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import amorphia.alloygery.datagen.AlloygeryModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.LinkedHashMap;
import java.util.Map;

public class WorldGenModule
{
	public static final Map<String, Block> BLOCKS = new LinkedHashMap<>();

	public static final Block TIN_ORE = makeOreBlock("tin_ore");
	public static final Block DEEPSLATE_TIN_ORE = makeOreBlock("deepslate_tin_ore");

	public static final Block TEALLITE = makeOreBlock("teallite");
	public static final Block CUPROLINE = makeOreBlock("cuproline");
	public static final Block FERONYTE = makeOreBlock("feronyte");
	public static final Block AURORUM = makeOreBlock("aurorum");
	public static final Block NICKELINE = makeOreBlock("nickeline");
	public static final Block TITANITE = makeOreBlock("titanite");

	private static Block makeOreBlock(String path)
	{
		Block block = new Block(FabricBlockSettings.copyOf(Blocks.STONE).requiresTool().strength(3.0f, 3.0f));
		BLOCKS.put(path, block);
		return block;
	}

	private static void registerBlocks()
	{
		for(Map.Entry<String, Block> entry : BLOCKS.entrySet())
		{
			Registry.register(BuiltInRegistries.BLOCK, Alloygery.asResource(entry.getKey()), entry.getValue());
		}
	}

	private static void registerItems()
	{
		for(Map.Entry<String, Block> entry : BLOCKS.entrySet())
		{
			BlockItem item = new BlockItem(entry.getValue(), new Item.Properties());
			item.registerBlocks(Item.BY_BLOCK, item);
			AlloygeryCreativeTabs.VANILLA_NATURAL_BLOCKS.add(item);
			Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), BuiltInRegistries.BLOCK.getKey(entry.getValue())), item);
		}
	}

	public static void initialize()
	{
		WorldGenModule.registerBlocks();
		WorldGenModule.registerItems();

		Features.init();
		PlacementModifiers.init();

		BiomeModifiers.bootstrap();
	}

	public static void initializeClient()
	{

	}

	public static void initializeDataGenerator(FabricDataGenerator fabricDataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryModelProvider.addProvider(new WorldGenModelProvider());
		AlloygeryEnglishLanguageProvider.addProvider(new WorldGenEnglishLanguageProvider());
		AlloygeryBlockLootTableProvider.addProvider(new WorldGenBlockLootTableProvider());
		AlloygeryBlockTagProvider.addProvider(new WorldGenTagProvider());

//		pack.addProvider(WorldGenDataProvider::new);

		FabricDataGenerator.Pack worldgenPack = fabricDataGenerator.createBuiltinResourcePack(Alloygery.asResource("worldgen"));
		worldgenPack.addProvider(WorldGenDataProvider::new);
		worldgenPack.addProvider((FabricDataGenerator.Pack.Factory<WorldgenPackMetadataProvider>) WorldgenPackMetadataProvider::new);
	}
}
