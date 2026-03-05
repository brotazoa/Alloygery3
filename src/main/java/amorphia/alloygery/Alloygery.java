package amorphia.alloygery;

import amorphia.alloygery.advancement.AdvancementModule;
import amorphia.alloygery.advancement.ModifyVanillaAdvancementsProviders;
import amorphia.alloygery.compat.CompatibilityModule;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.EasyStartRecipesProviders;
import amorphia.alloygery.datagen.*;
import amorphia.alloygery.datagen.VanillaRecipeOverridesProviders;
import amorphia.alloygery.datagen.loot.*;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.worldgen.WorldGenModule;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.PortingLibLoot;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Alloygery implements ModInitializer, ClientModInitializer, DataGeneratorEntrypoint
{
	public static final String MODID = "alloygery";
	public static final Logger LOGGER = LogManager.getLogger();

	public static final LazyRegistrar<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = LazyRegistrar.create(PortingLibLoot.GLOBAL_LOOT_MODIFIER_SERIALIZERS_KEY, MODID);

	@Override
	public void onInitialize()
	{
		FabricLoader.getInstance().getModContainer(MODID).ifPresent(
				modContainer -> {
					ResourceManagerHelper.registerBuiltinResourcePack(asResource("worldgen"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
					ResourceManagerHelper.registerBuiltinResourcePack(asResource("advancements"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
					ResourceManagerHelper.registerBuiltinResourcePack(asResource("logs_require_tool"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
					ResourceManagerHelper.registerBuiltinResourcePack(asResource("easier_starting_recipes"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
					ResourceManagerHelper.registerBuiltinResourcePack(asResource("convert_vanilla_gear_to_alloygery_gear"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
					ResourceManagerHelper.registerBuiltinResourcePack(asResource("vanilla_recipe_overrides"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
				}
		);

		LOOT_MODIFIERS.register("add_item", AddItemLootModifier.CODEC);
		LOOT_MODIFIERS.register("add_table", AddLootTableLootModifier.CODEC);
		LOOT_MODIFIERS.register("replace_item", ReplaceItemLootModifier.CODEC);
		LOOT_MODIFIERS.register();

		WorldGenModule.initialize();
		CraftingMaterialModule.initialize();
		MachinesModule.initialize();
		GearModule.initialize();
		AdvancementModule.initialize();
		CompatibilityModule.initialize();

		AlloygeryCreativeTabs.initialize();
	}

	@Override
	public void onInitializeClient()
	{
		WorldGenModule.initializeClient();
		CraftingMaterialModule.initializeClient();
		MachinesModule.initializeClient();
		GearModule.initializeClient();
		AdvancementModule.initializeClient();
		CompatibilityModule.initializeClient();
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		WorldGenModule.initializeDataGenerator(fabricDataGenerator, pack);
		CraftingMaterialModule.initializeDataGen(fabricDataGenerator, pack);
		MachinesModule.initializeDataGen(fabricDataGenerator, pack);
		GearModule.initializeDataGen(fabricDataGenerator, pack);
		AdvancementModule.initializeDataGen(fabricDataGenerator, pack);
		CompatibilityModule.initializeDatagen(fabricDataGenerator, pack);

		pack.addProvider(AlloygeryModelProvider::new);
		pack.addProvider(AlloygeryEnglishLanguageProvider::new);
		pack.addProvider(AlloygeryBlockLootTableProvider::new);
		pack.addProvider(AlloygeryChestLootTableProvider::new);
		pack.addProvider(AlloygeryEntityLootTableProvider::new);
		pack.addProvider((FabricDataGenerator.Pack.Factory<AlloygeryGlobalLootModifierDataProvider>) AlloygeryGlobalLootModifierDataProvider::new);
		pack.addProvider(AlloygeryItemTagProvider::new);
		pack.addProvider(AlloygeryBlockTagProvider::new);
		pack.addProvider(AlloygeryRecipeProvider::new);

		FabricDataGenerator.Pack advancementsPack = fabricDataGenerator.createBuiltinResourcePack(Alloygery.asResource("advancements"));
		advancementsPack.addProvider(ModifyVanillaAdvancementsProviders.AdvancementsProvider::new);
		advancementsPack.addProvider((FabricDataGenerator.Pack.Factory<ModifyVanillaAdvancementsProviders.PackMetadataProvider>) ModifyVanillaAdvancementsProviders.PackMetadataProvider::new);

		FabricDataGenerator.Pack logsRequireToolPack = fabricDataGenerator.createBuiltinResourcePack(Alloygery.asResource("logs_require_tool"));
		logsRequireToolPack.addProvider(LogsRequireToolProviders.LogsRequireToolTagProvider::new);
		logsRequireToolPack.addProvider(LogsRequireToolProviders.AdvancementsProvider::new);
		logsRequireToolPack.addProvider((FabricDataGenerator.Pack.Factory<LogsRequireToolProviders.PackMetadataProvider>) LogsRequireToolProviders.PackMetadataProvider::new);

		FabricDataGenerator.Pack easyStartPack = fabricDataGenerator.createBuiltinResourcePack(Alloygery.asResource("easier_starting_recipes"));
		easyStartPack.addProvider(EasyStartRecipesProviders.RecipeProvider::new);
		easyStartPack.addProvider((FabricDataGenerator.Pack.Factory<EasyStartRecipesProviders.PackMetadataProvider>) EasyStartRecipesProviders.PackMetadataProvider::new);

		FabricDataGenerator.Pack vanillaOverridesPack = fabricDataGenerator.createBuiltinResourcePack(asResource("vanilla_recipe_overrides"));
		vanillaOverridesPack.addProvider(VanillaRecipeOverridesProviders.RecipeProvider::new);
		vanillaOverridesPack.addProvider(VanillaRecipeOverridesProviders.AdvancementsProvider::new);
		vanillaOverridesPack.addProvider((FabricDataGenerator.Pack.Factory<VanillaRecipeOverridesProviders.PackMetadataProvider>) VanillaRecipeOverridesProviders.PackMetadataProvider::new);
	}

	public static ResourceLocation asResource(String path)
	{
		return new ResourceLocation(MODID, path);
	}

	public static ResourceLocation asCommonResource(String path)
	{
		return new ResourceLocation("c", path);
	}

	public static ResourceLocation asFabricResource(String path)
	{
		return new ResourceLocation("fabric", path);
	}

	public static ResourceLocation asVanillaResource(String path)
	{
		return new ResourceLocation("minecraft", path);
	}
}
