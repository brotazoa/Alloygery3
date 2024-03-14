package amorphia.alloygery.machines;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import amorphia.alloygery.machines.block.alloyKiln.AlloyKilnBlock;
import amorphia.alloygery.machines.block.alloyKiln.AlloyKilnBlockEntity;
import amorphia.alloygery.machines.block.alloyKiln.AlloyKilnMenu;
import amorphia.alloygery.machines.block.heatExchanger.*;
import amorphia.alloygery.machines.block.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerBlock;
import amorphia.alloygery.machines.block.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.block.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerBlock;
import amorphia.alloygery.machines.block.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.block.heatExchanger.furnace.FurnaceWithHeatExchangerBlock;
import amorphia.alloygery.machines.block.heatExchanger.furnace.FurnaceWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.block.heatExchanger.kiln.KilnWithHeatExchangerBlock;
import amorphia.alloygery.machines.block.heatExchanger.kiln.KilnWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.block.heatExchanger.smoker.SmokerWithHeatExchangerBlock;
import amorphia.alloygery.machines.block.heatExchanger.smoker.SmokerWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.block.kiln.KilnBlock;
import amorphia.alloygery.machines.block.kiln.KilnBlockEntity;
import amorphia.alloygery.machines.recipe.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.LinkedHashMap;
import java.util.Map;

public class MachinesModule
{
	public static Map<String, Block> BLOCKS = new LinkedHashMap<>();
	public static Map<String, Item> ITEMS = new LinkedHashMap<>();

	// blocks
	public static final Block ALLOY_KILN = new AlloyKilnBlock();
	public static final Block KILN = new KilnBlock();

	public static final Block HEAT_EXCHANGER = new HeatExchangerBlock();

	public static final Block FURNACE_WITH_HEAT_EXCHANGER = new FurnaceWithHeatExchangerBlock();
	public static final Block KILN_WITH_HEAT_EXCHANGER = new KilnWithHeatExchangerBlock();
	public static final Block ALLOY_KILN_WITH_HEAT_EXCHANGER = new AlloyKilnWithHeatExchangerBlock();
	public static final Block BLAST_FURNACE_WITH_HEAT_EXCHANGER = new BlastFurnaceWithHeatExchangerBlock();
	public static final Block SMOKER_WITH_HEAT_EXCHANGER = new SmokerWithHeatExchangerBlock();

	// block entities
	public static BlockEntityType<AlloyKilnBlockEntity> ALLOY_KILN_BLOCK_ENTITY;
	public static BlockEntityType<KilnBlockEntity> KILN_BLOCK_ENTITY;
	public static BlockEntityType<HeatExchangerBlockEntity> HEAT_EXCHANGER_BLOCK_ENTITY;

	public static BlockEntityType<AlloyKilnWithHeatExchangerBlockEntity> ALLOY_KILN_WITH_HEAT_EXCHANGER_BLOCK_ENTITY;
	public static BlockEntityType<KilnWithHeatExchangerBlockEntity> KILN_WITH_HEAT_EXCHANGER_BLOCK_ENTITY;
	public static BlockEntityType<FurnaceWithHeatExchangerBlockEntity> FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY;
	public static BlockEntityType<BlastFurnaceWithHeatExchangerBlockEntity> BLAST_FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY;
	public static BlockEntityType<SmokerWithHeatExchangerBlockEntity> SMOKER_WITH_HEAT_EXCHANGER_BLOCK_ENTITY;

	// menus
	public static MenuType<AlloyKilnMenu> ALLOY_KILN_MENU_TYPE;

	// stats
	public static final ResourceLocation INTERACT_WITH_ALLOY_KILN = Alloygery.asResource("interact_with_alloy_kiln");
	public static final ResourceLocation INTERACT_WITH_KILN = Alloygery.asResource("interact_with_kiln");
	public static final ResourceLocation INTERACT_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_ALLOY_KILN_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_alloy_kiln_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_FURNACE_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_furnace_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_BLAST_FURNACE_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_blast_furnace_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_KILN_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_kiln_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_SMOKER_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_smoker_with_heat_exchanger");


	private static void registerBlock(String path, Block block)
	{
		Registry.register(BuiltInRegistries.BLOCK, Alloygery.asResource(path), block);
		BLOCKS.put(path, block);

		BlockItem bi = new BlockItem(block, new Item.Properties());
		bi.registerBlocks(Item.BY_BLOCK, bi);
		Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), BuiltInRegistries.BLOCK.getKey(block)), bi);

		AlloygeryCreativeTabs.VANILLA_FUNCTIONAL_BLOCKS.add(bi);
	}

	private static void registerCustomStat(ResourceLocation stat)
	{
		Registry.register(BuiltInRegistries.CUSTOM_STAT, stat.getPath(), stat);
		Stats.CUSTOM.get(stat, StatFormatter.DEFAULT);
	}

	public static void initialize()
	{
		// blocks
		registerBlock("alloy_kiln", ALLOY_KILN);
		registerBlock("kiln", KILN);
		registerBlock("heat_exchanger", HEAT_EXCHANGER);

		registerBlock("alloy_kiln_with_heat_exchanger", ALLOY_KILN_WITH_HEAT_EXCHANGER);
		registerBlock("blast_furnace_with_heat_exchanger", BLAST_FURNACE_WITH_HEAT_EXCHANGER);
		registerBlock("furnace_with_heat_exchanger", FURNACE_WITH_HEAT_EXCHANGER);
		registerBlock("kiln_with_heat_exchanger", KILN_WITH_HEAT_EXCHANGER);
		registerBlock("smoker_with_heat_exchanger", SMOKER_WITH_HEAT_EXCHANGER);

		// block entities
		ALLOY_KILN_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("alloy_kiln_block_entity"),
				FabricBlockEntityTypeBuilder.create(AlloyKilnBlockEntity::new, ALLOY_KILN).build());
		KILN_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("kiln_block_entity"),
				FabricBlockEntityTypeBuilder.create(KilnBlockEntity::new, KILN).build());
		HEAT_EXCHANGER_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("heat_exchanger_block_entity"),
				FabricBlockEntityTypeBuilder.create(HeatExchangerBlockEntity::new, HEAT_EXCHANGER).build());

		ALLOY_KILN_WITH_HEAT_EXCHANGER_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("alloy_kiln_with_heat_exchanger_block_entity"),
				FabricBlockEntityTypeBuilder.create(AlloyKilnWithHeatExchangerBlockEntity::new, ALLOY_KILN_WITH_HEAT_EXCHANGER).build());
		BLAST_FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("blast_furnace_with_heat_exchanger_block_entity"),
				FabricBlockEntityTypeBuilder.create(BlastFurnaceWithHeatExchangerBlockEntity::new, BLAST_FURNACE_WITH_HEAT_EXCHANGER).build());
		FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("furnace_with_heat_exchanger_block_entity"),
				FabricBlockEntityTypeBuilder.create(FurnaceWithHeatExchangerBlockEntity::new, FURNACE_WITH_HEAT_EXCHANGER).build());
		KILN_WITH_HEAT_EXCHANGER_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("kiln_with_heat_exchanger_block_entity"),
				FabricBlockEntityTypeBuilder.create(KilnWithHeatExchangerBlockEntity::new, KILN_WITH_HEAT_EXCHANGER).build());
		SMOKER_WITH_HEAT_EXCHANGER_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Alloygery.asResource("smoker_with_heat_exchanger_block_entity"),
				FabricBlockEntityTypeBuilder.create(SmokerWithHeatExchangerBlockEntity::new, SMOKER_WITH_HEAT_EXCHANGER).build());

		// recipe types
		Registry.register(BuiltInRegistries.RECIPE_TYPE, AlloyingRecipe.Type.ID, AlloyingRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, AlloyingRecipe.Type.ID, AlloyingRecipe.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, SimpleAlloyingRecipe.Type.ID, SimpleAlloyingRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, SimpleAlloyingRecipe.Type.ID, SimpleAlloyingRecipe.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, FiringRecipe.Type.ID, FiringRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, FiringRecipe.Type.ID, FiringRecipe.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, HeatExchangerBlockRevertRecipe.Type.ID, HeatExchangerBlockRevertRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, HeatExchangerBlockRevertRecipe.Type.ID, HeatExchangerBlockRevertRecipe.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, HeatExchangerBlockTransformRecipe.Type.ID, HeatExchangerBlockTransformRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, HeatExchangerBlockTransformRecipe.Type.ID, HeatExchangerBlockTransformRecipe.Serializer.INSTANCE);

		// menus
		ALLOY_KILN_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("alloy_kiln"), new MenuType<>(AlloyKilnMenu::new, FeatureFlags.VANILLA_SET));

		// stats
		registerCustomStat(INTERACT_WITH_ALLOY_KILN);
		registerCustomStat(INTERACT_WITH_KILN);
		registerCustomStat(INTERACT_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_ALLOY_KILN_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_BLAST_FURNACE_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_FURNACE_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_KILN_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_SMOKER_WITH_HEAT_EXCHANGER);
	}

	public static void initializeClient()
	{

	}

	public static void initializeDataGen(FabricDataGenerator fabricDataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryEnglishLanguageProvider.addProvider(new MachinesEnglishLanguageProvider());
	}
}
