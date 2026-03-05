package amorphia.alloygery.machines;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import amorphia.alloygery.datagen.*;
import amorphia.alloygery.datagen.loot.AlloygeryBlockLootTableProvider;
import amorphia.alloygery.machines.alloyKiln.*;
import amorphia.alloygery.machines.datagen.MachinesTagProvider;
import amorphia.alloygery.machines.datagen.recipe.MachinesRecipeProvider;
import amorphia.alloygery.machines.heatExchanger.*;
import amorphia.alloygery.machines.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerMenu;
import amorphia.alloygery.machines.heatExchanger.alloyKiln.AlloyKilnWithHeatExchangerScreen;
import amorphia.alloygery.machines.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerMenu;
import amorphia.alloygery.machines.heatExchanger.blastFurnace.BlastFurnaceWithHeatExchangerScreen;
import amorphia.alloygery.machines.heatExchanger.furnace.FurnaceWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.furnace.FurnaceWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.heatExchanger.furnace.FurnaceWithHeatExchangerMenu;
import amorphia.alloygery.machines.heatExchanger.furnace.FurnaceWithHeatExchangerScreen;
import amorphia.alloygery.machines.heatExchanger.kiln.KilnWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.kiln.KilnWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.heatExchanger.kiln.KilnWithHeatExchangerMenu;
import amorphia.alloygery.machines.heatExchanger.kiln.KilnWithHeatExchangerScreen;
import amorphia.alloygery.machines.heatExchanger.smoker.SmokerWithHeatExchangerBlock;
import amorphia.alloygery.machines.heatExchanger.smoker.SmokerWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.heatExchanger.smoker.SmokerWithHeatExchangerMenu;
import amorphia.alloygery.machines.heatExchanger.smoker.SmokerWithHeatExchangerScreen;
import amorphia.alloygery.machines.kiln.*;
import amorphia.alloygery.machines.datagen.MachinesBlockLootTableProvider;
import amorphia.alloygery.machines.datagen.MachinesEnglishLanguageProvider;
import amorphia.alloygery.machines.datagen.MachinesModelProvider;
import amorphia.alloygery.machines.smithing_anvil.SmithingAnvilBlock;
import amorphia.alloygery.machines.smithing_anvil.SmithingAnvilMenu;
import amorphia.alloygery.machines.smithing_anvil.SmithingAnvilRecipe;
import amorphia.alloygery.machines.smithing_anvil.SmithingAnvilScreen;
import amorphia.alloygery.machines.tailoring_table.TailoringTableBlock;
import amorphia.alloygery.machines.tailoring_table.TailoringTableMenu;
import amorphia.alloygery.machines.tailoring_table.TailoringTableRecipe;
import amorphia.alloygery.machines.tailoring_table.TailoringTableScreen;
import amorphia.alloygery.machines.woodcutter.WoodcutterBlock;
import amorphia.alloygery.machines.woodcutter.WoodcutterMenu;
import amorphia.alloygery.machines.woodcutter.WoodcutterRecipe;
import amorphia.alloygery.machines.woodcutter.WoodcutterScreen;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
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

	public static final Block HEAT_EXCHANGER = new HeatExchangerBlock();

	public static final Block FURNACE_WITH_HEAT_EXCHANGER = new FurnaceWithHeatExchangerBlock();
	public static final Block KILN_WITH_HEAT_EXCHANGER = new KilnWithHeatExchangerBlock();
	public static final Block ALLOY_KILN_WITH_HEAT_EXCHANGER = new AlloyKilnWithHeatExchangerBlock();
	public static final Block BLAST_FURNACE_WITH_HEAT_EXCHANGER = new BlastFurnaceWithHeatExchangerBlock();
	public static final Block SMOKER_WITH_HEAT_EXCHANGER = new SmokerWithHeatExchangerBlock();

	public static final Block KILN = new KilnBlock();
	public static RecipeSerializer<FiringRecipe> FIRING_RECIPE_SERIALIZER;

    public static final Block WOODCUTTER = new WoodcutterBlock();
    public static RecipeSerializer<WoodcutterRecipe> WOODCUTTING_RECIPE_SERIALIZER;

    public static final Block SMITHING_ANVIL = new SmithingAnvilBlock();
    public static RecipeSerializer<SmithingAnvilRecipe> SMITHING_ANVIL_RECIPE_SERIALIZER;

    public static final Block TAILORING_TABLE = new TailoringTableBlock();
    public static RecipeSerializer<TailoringTableRecipe> TAILORING_TABLE_RECIPE_SERIALIZER;

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
	public static MenuType<KilnMenu> KILN_MENU_TYPE;
    public static MenuType<HeatExchangerMenu> HEAT_EXCHANGER_MENU_TYPE;
    public static MenuType<AlloyKilnWithHeatExchangerMenu> ALLOY_KILN_WITH_HEAT_EXCHANGER_MENU_TYPE;
    public static MenuType<KilnWithHeatExchangerMenu> KILN_WITH_HEAT_EXCHANGER_MENU_TYPE;
    public static MenuType<FurnaceWithHeatExchangerMenu> FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE;
    public static MenuType<BlastFurnaceWithHeatExchangerMenu> BLAST_FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE;
    public static MenuType<SmokerWithHeatExchangerMenu> SMOKER_WITH_HEAT_EXCHANGER_MENU_TYPE;
    public static MenuType<WoodcutterMenu> WOODCUTTER_MENU_TYPE;
    public static MenuType<SmithingAnvilMenu> SMITHING_ANVIL_MENU_TYPE;
    public static MenuType<TailoringTableMenu> TAILORING_TABLE_MENU_TYPE;

	// stats
	public static final ResourceLocation INTERACT_WITH_ALLOY_KILN = Alloygery.asResource("interact_with_alloy_kiln");
	public static final ResourceLocation INTERACT_WITH_KILN = Alloygery.asResource("interact_with_kiln");
	public static final ResourceLocation INTERACT_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_ALLOY_KILN_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_alloy_kiln_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_FURNACE_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_furnace_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_BLAST_FURNACE_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_blast_furnace_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_KILN_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_kiln_with_heat_exchanger");
	public static final ResourceLocation INTERACT_WITH_SMOKER_WITH_HEAT_EXCHANGER = Alloygery.asResource("interact_with_smoker_with_heat_exchanger");
    public static final ResourceLocation INTERACT_WITH_WOODCUTTER = Alloygery.asResource("interact_with_woodcutter");
    public static final ResourceLocation INTERACT_WITH_SMITING_ANVIL = Alloygery.asResource("interact_with_smithing_anvil");
    public static final ResourceLocation INTERACT_WITH_TAILORING_TABLE = Alloygery.asResource("interact_with_tailoring_table");

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

        registerBlock("woodcutter", WOODCUTTER);
        registerBlock("smithing_anvil", SMITHING_ANVIL);
        registerBlock("tailoring_table", TAILORING_TABLE);

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
		FIRING_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, FiringRecipe.Type.ID, new SimpleCookingSerializer<>(FiringRecipe::new, 100));

		Registry.register(BuiltInRegistries.RECIPE_TYPE, HeatExchangerBlockRevertRecipe.Type.ID, HeatExchangerBlockRevertRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, HeatExchangerBlockRevertRecipe.Type.ID, HeatExchangerBlockRevertRecipe.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, HeatExchangerBlockTransformRecipe.Type.ID, HeatExchangerBlockTransformRecipe.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, HeatExchangerBlockTransformRecipe.Type.ID, HeatExchangerBlockTransformRecipe.Serializer.INSTANCE);

        Registry.register(BuiltInRegistries.RECIPE_TYPE, WoodcutterRecipe.Type.ID, WoodcutterRecipe.Type.INSTANCE);
        WOODCUTTING_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, WoodcutterRecipe.Type.ID, new WoodcutterRecipe.Serializer(WoodcutterRecipe::new));

        Registry.register(BuiltInRegistries.RECIPE_TYPE, SmithingAnvilRecipe.Type.ID, SmithingAnvilRecipe.Type.INSTANCE);
        SMITHING_ANVIL_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, SmithingAnvilRecipe.Type.ID, new SmithingAnvilRecipe.Serializer(SmithingAnvilRecipe::new));

        Registry.register(BuiltInRegistries.RECIPE_TYPE, TailoringTableRecipe.Type.ID, TailoringTableRecipe.Type.INSTANCE);
        TAILORING_TABLE_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TailoringTableRecipe.Type.ID, new TailoringTableRecipe.Serializer(TailoringTableRecipe::new));

		// menus
		ALLOY_KILN_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("alloy_kiln"), new MenuType<>(AlloyKilnMenu::new, FeatureFlags.VANILLA_SET));
		KILN_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("kiln"), new MenuType<>(KilnMenu::new, FeatureFlags.VANILLA_SET));
        HEAT_EXCHANGER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("heat_exchanger"), new MenuType<>(HeatExchangerMenu::new, FeatureFlags.VANILLA_SET));
        ALLOY_KILN_WITH_HEAT_EXCHANGER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("alloy_kiln_with_heat_exchanger"), new MenuType<>(AlloyKilnWithHeatExchangerMenu::new, FeatureFlags.VANILLA_SET));
        KILN_WITH_HEAT_EXCHANGER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("kiln_with_heat_exchanger"), new MenuType<>(KilnWithHeatExchangerMenu::new, FeatureFlags.VANILLA_SET));
        FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("furnace_with_heat_exchanger"), new MenuType<>(FurnaceWithHeatExchangerMenu::new, FeatureFlags.VANILLA_SET));
        BLAST_FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("blast_furnace_with_heat_exchanger"), new MenuType<>(BlastFurnaceWithHeatExchangerMenu::new, FeatureFlags.VANILLA_SET));
        SMOKER_WITH_HEAT_EXCHANGER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("smoker_with_heat_exchanger"), new MenuType<>(SmokerWithHeatExchangerMenu::new, FeatureFlags.VANILLA_SET));
        WOODCUTTER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("woodcutter"), new MenuType<>(WoodcutterMenu::new, FeatureFlags.VANILLA_SET));
        SMITHING_ANVIL_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("smithing_anvil"), new MenuType<>(SmithingAnvilMenu::new, FeatureFlags.VANILLA_SET));
        TAILORING_TABLE_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, Alloygery.asResource("tailoring_table"), new MenuType<>(TailoringTableMenu::new, FeatureFlags.VANILLA_SET));

		// stats
		registerCustomStat(INTERACT_WITH_ALLOY_KILN);
		registerCustomStat(INTERACT_WITH_KILN);
		registerCustomStat(INTERACT_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_ALLOY_KILN_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_BLAST_FURNACE_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_FURNACE_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_KILN_WITH_HEAT_EXCHANGER);
		registerCustomStat(INTERACT_WITH_SMOKER_WITH_HEAT_EXCHANGER);
        registerCustomStat(INTERACT_WITH_WOODCUTTER);
        registerCustomStat(INTERACT_WITH_SMITING_ANVIL);
        registerCustomStat(INTERACT_WITH_TAILORING_TABLE);
	}

	public static void initializeClient()
	{
		MenuScreens.register(ALLOY_KILN_MENU_TYPE, AlloyKilnScreen::new);
		MenuScreens.register(KILN_MENU_TYPE, KilnScreen::new);
        MenuScreens.register(HEAT_EXCHANGER_MENU_TYPE, HeatExchangerScreen::new);
        MenuScreens.register(ALLOY_KILN_WITH_HEAT_EXCHANGER_MENU_TYPE, AlloyKilnWithHeatExchangerScreen::new);
        MenuScreens.register(KILN_WITH_HEAT_EXCHANGER_MENU_TYPE, KilnWithHeatExchangerScreen::new);
        MenuScreens.register(FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE, FurnaceWithHeatExchangerScreen::new);
        MenuScreens.register(BLAST_FURNACE_WITH_HEAT_EXCHANGER_MENU_TYPE, BlastFurnaceWithHeatExchangerScreen::new);
        MenuScreens.register(SMOKER_WITH_HEAT_EXCHANGER_MENU_TYPE, SmokerWithHeatExchangerScreen::new);
        MenuScreens.register(WOODCUTTER_MENU_TYPE, WoodcutterScreen::new);
        MenuScreens.register(SMITHING_ANVIL_MENU_TYPE, SmithingAnvilScreen::new);
        MenuScreens.register(TAILORING_TABLE_MENU_TYPE, TailoringTableScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(WOODCUTTER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TAILORING_TABLE, RenderType.cutout());
	}

	public static void initializeDataGen(FabricDataGenerator fabricDataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryEnglishLanguageProvider.addProvider(new MachinesEnglishLanguageProvider());
		AlloygeryModelProvider.addProvider(new MachinesModelProvider());
		AlloygeryBlockLootTableProvider.addProvider(new MachinesBlockLootTableProvider());
        AlloygeryRecipeProvider.addProvider(new MachinesRecipeProvider());
		AlloygeryBlockTagProvider.addProvider(MachinesTagProvider.INSTANCE);
		AlloygeryItemTagProvider.addProvider(MachinesTagProvider.INSTANCE);
	}
}
