package amorphia.alloygery.gear.datagen.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.ToolTypes;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.item.part.HandGuard;
import amorphia.alloygery.gear.item.part.ToolBinding;
import amorphia.alloygery.gear.item.part.ToolHandle;
import amorphia.alloygery.gear.material.AlloygeryDefaultMaterials;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.PrimitiveProperty;
import amorphia.alloygery.machines.datagen.recipe.SingleIngredientRecipeBuilder;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumSet;
import java.util.function.Consumer;

import static amorphia.alloygery.gear.item.PartTypes.*;
import static amorphia.alloygery.gear.item.ToolTypes.*;

public class GearRecipeProvider implements AlloygeryRecipeProvider.IAlloygeryRecipeProvider
{
    @Override
    public void buildRecipes(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
    {
        GearModule.ITEMS.values().forEach(item -> {
            if (item instanceof IRecipeGen itemWithRecipeGen)
            {
                itemWithRecipeGen.generateRecipe(provider, exporter);
            }
        });

        GearRecipeProvider.generatePrimitiveToolSets(exporter);

        makeWoodcutterRecipes(exporter);
        makeSmithingAnvilRecipes(exporter);
        makeTailoringRecipes(exporter);

		SmithingUpgradeRecipeBuilder.netheriteUpgrade().unlocks("has_netherite", RecipeProvider.has(Items.NETHERITE_INGOT)).save(exporter, Alloygery.asResource("netherite_smithing_upgrade"));

		SmithingImprovementRecipeBuilder.tipped(Ingredient.of(Items.DIAMOND), AlloygeryDefaultMaterials.DIAMOND).save(exporter, Alloygery.asResource("diamond_tipped_improvement"));
		SmithingImprovementRecipeBuilder.tipped(Ingredient.of(Items.AMETHYST_SHARD), AlloygeryDefaultMaterials.AMETHYST).save(exporter, Alloygery.asResource("amethyst_tipped_improvement"));
		SmithingImprovementRecipeBuilder.tipped(Ingredient.of(Items.EMERALD), AlloygeryDefaultMaterials.EMERALD).save(exporter, Alloygery.asResource("emerald_tipped_improvement"));
		SmithingImprovementRecipeBuilder.tipped(Ingredient.of(Items.LAPIS_LAZULI), AlloygeryDefaultMaterials.LAPIS).save(exporter, Alloygery.asResource("lapis_tipped_improvement"));
		SmithingImprovementRecipeBuilder.tipped(Ingredient.of(Items.REDSTONE), AlloygeryDefaultMaterials.REDSTONE).save(exporter, Alloygery.asResource("redstone_tipped_improvement"));
		SmithingImprovementRecipeBuilder.tipped(Ingredient.of(Items.QUARTZ), AlloygeryDefaultMaterials.QUARTZ).save(exporter, Alloygery.asResource("quartz_tipped_improvement"));

		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("tin_ingot")), AlloygeryDefaultMaterials.TIN).save(exporter, Alloygery.asResource("tin_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(Items.COPPER_INGOT), AlloygeryDefaultMaterials.COPPER).save(exporter, Alloygery.asResource("copper_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("bronze_ingot")), AlloygeryDefaultMaterials.BRONZE).save(exporter, Alloygery.asResource("bronze_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(Items.IRON_INGOT), AlloygeryDefaultMaterials.IRON).save(exporter, Alloygery.asResource("iron_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(Items.GOLD_INGOT), AlloygeryDefaultMaterials.GOLD).save(exporter, Alloygery.asResource("gold_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("antanium_ingot")), AlloygeryDefaultMaterials.ANTANIUM).save(exporter, Alloygery.asResource("antanium_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("steel_ingot")), AlloygeryDefaultMaterials.STEEL).save(exporter, Alloygery.asResource("steel_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("nickel_ingot")), AlloygeryDefaultMaterials.NICKEL).save(exporter, Alloygery.asResource("nickel_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("invar_ingot")), AlloygeryDefaultMaterials.INVAR).save(exporter, Alloygery.asResource("invar_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("constantan_ingot")), AlloygeryDefaultMaterials.CONSTANTAN).save(exporter, Alloygery.asResource("constantan_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("titanium_ingot")), AlloygeryDefaultMaterials.TITANIUM).save(exporter, Alloygery.asResource("titanium_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("titanium_gold_ingot")), AlloygeryDefaultMaterials.TITANIUM_GOLD).save(exporter, Alloygery.asResource("titanium_gold_plated_improvement"));
		SmithingImprovementRecipeBuilder.plated(Ingredient.of(CraftingMaterialModule.ITEMS.get("nitinol_ingot")), AlloygeryDefaultMaterials.NITINOL).save(exporter, Alloygery.asResource("nitinol_plated_improvement"));

		SmithingImprovementRecipeBuilder.wrapped(Ingredient.of(CraftingMaterialModule.ITEMS.get("leather_panel")), AlloygeryDefaultMaterials.LEATHER).save(exporter, Alloygery.asResource("leather_wrapped_improvement"));
		SmithingImprovementRecipeBuilder.wrapped(Ingredient.of(CraftingMaterialModule.ITEMS.get("rabbit_hide_panel")), AlloygeryDefaultMaterials.RABBIT_HIDE).save(exporter, Alloygery.asResource("rabbit_hide_wrapped_improvement"));
//		SmithingImprovementRecipeBuilder.wrapped(Ingredient.of(Items.PHANTOM_MEMBRANE), AlloygeryDefaultMaterials.PHANTOM_MEMBRANE).save(exporter, Alloygery.asResource("phantom_membrane_wrapped_improvement"));
		SmithingImprovementRecipeBuilder.wrapped(Ingredient.of(CraftingMaterialModule.ITEMS.get("wool_panel")), AlloygeryDefaultMaterials.WOOL).save(exporter, Alloygery.asResource("wool_wrapped_improvement"));

		RecipeProvider.copySmithingTemplate(exporter, GearModule.ITEMS.get("tipped_template"), TagKey.create(Registries.ITEM, Alloygery.asCommonResource("crystals")));
		RecipeProvider.copySmithingTemplate(exporter, GearModule.ITEMS.get("plated_template"), TagKey.create(Registries.ITEM, Alloygery.asCommonResource("ingots")));
		RecipeProvider.copySmithingTemplate(exporter, GearModule.ITEMS.get("wrapped_template"), TagKey.create(Registries.ITEM, Alloygery.asCommonResource("hides")));
    }

    public interface IRecipeGen
    {
        void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter);
    }

    private void makeSmithingAnvilRecipes(Consumer<FinishedRecipe> exporter)
    {
        EnumSet<ToolTypes> ALL_MINING_TOOLS = EnumSet.of(HATCHET, KNIFE, AXE, HOE, PICKAXE, SHOVEL, SWORD, SCYTHE, HAMMER, EXCAVATOR);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.TIN, CraftingMaterialModule.ITEMS.get("tin_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.COPPER, Items.COPPER_INGOT, EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.COPPER, Items.COPPER_INGOT, ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.BRONZE, CraftingMaterialModule.ITEMS.get("bronze_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.BRONZE, CraftingMaterialModule.ITEMS.get("bronze_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.IRON, Items.IRON_INGOT, EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.IRON, Items.IRON_INGOT, ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.GOLD, Items.GOLD_INGOT, EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.GOLD, Items.GOLD_INGOT, ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.ANTANIUM, CraftingMaterialModule.ITEMS.get("antanium_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.ANTANIUM, CraftingMaterialModule.ITEMS.get("antanium_ingot"), ALL_MINING_TOOLS, exporter);

		makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.AMETHYST, Items.AMETHYST_SHARD, ALL_MINING_TOOLS, exporter);

		makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.EMERALD, Items.EMERALD, ALL_MINING_TOOLS, exporter);

		makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.DIAMOND, Items.DIAMOND, ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.STEEL, CraftingMaterialModule.ITEMS.get("steel_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.STEEL, CraftingMaterialModule.ITEMS.get("steel_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.NETHERITE, Items.NETHERITE_INGOT, EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.NETHERITE, Items.NETHERITE_INGOT, ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.NICKEL, CraftingMaterialModule.ITEMS.get("nickel_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.NICKEL, CraftingMaterialModule.ITEMS.get("nickel_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.INVAR, CraftingMaterialModule.ITEMS.get("invar_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.INVAR, CraftingMaterialModule.ITEMS.get("invar_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.CONSTANTAN, CraftingMaterialModule.ITEMS.get("constantan_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.CONSTANTAN, CraftingMaterialModule.ITEMS.get("constantan_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.TITANIUM, CraftingMaterialModule.ITEMS.get("titanium_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.TITANIUM, CraftingMaterialModule.ITEMS.get("titanium_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.TITANIUM_GOLD, CraftingMaterialModule.ITEMS.get("titanium_gold_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.TITANIUM_GOLD, CraftingMaterialModule.ITEMS.get("titanium_gold_ingot"), ALL_MINING_TOOLS, exporter);

        makePartSmithingAnvilRecipes(AlloygeryDefaultMaterials.NITINOL, CraftingMaterialModule.ITEMS.get("nitinol_ingot"), EnumSet.of(TOOL_BINDING, TOOL_HANDLE, ARMOR_PLATE), exporter);
        makeToolHeadSmithingAnvilRecipes(AlloygeryDefaultMaterials.NITINOL, CraftingMaterialModule.ITEMS.get("nitinol_ingot"), ALL_MINING_TOOLS, exporter);
    }

    private void makeToolHeadSmithingAnvilRecipes(AlloygeryMaterial material, Item ingredient, EnumSet<ToolTypes> toolTypes, Consumer<FinishedRecipe> exporter)
    {
        if(toolTypes.contains(HATCHET))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_hatchet_head"), ingredient, exporter);

        if(toolTypes.contains(KNIFE))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_knife_blade"), ingredient, exporter);

        if (toolTypes.contains(AXE))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_axe_head"), ingredient, 2, 1, null, exporter);

        if (toolTypes.contains(HOE))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_hoe_head"), ingredient, exporter);

        if (toolTypes.contains(PICKAXE))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_pickaxe_head"), ingredient, 2, 1, null, exporter);

        if (toolTypes.contains(SHOVEL))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_shovel_head"), ingredient, exporter);

        if (toolTypes.contains(SWORD))
		{
			SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_sword_blade"), ingredient, 2, 1, null, exporter);
			SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_guard"), ingredient, 1, 1, null, exporter);
		}

        // scythe

        if (toolTypes.contains(HAMMER))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_hammer_head"), ingredient, 4, 1, null, exporter);

        if (toolTypes.contains(EXCAVATOR))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_excavator_head"), ingredient, 4, 1, null, exporter);
    }

    private void makePartSmithingAnvilRecipes(AlloygeryMaterial material, Item ingredient, EnumSet<PartTypes> partTypes, Consumer<FinishedRecipe> exporter)
    {
        if(partTypes.contains(TOOL_HANDLE))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_handle"), ingredient, exporter);

        if(partTypes.contains(TOOL_BINDING))
            SingleIngredientRecipeBuilder.smithingAnvil(GearModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_binding"), ingredient, exporter);

		if (partTypes.contains(ARMOR_PLATE))
			SingleIngredientRecipeBuilder.smithingAnvil(CraftingMaterialModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_panel"), ingredient, exporter);
    }

    private void makeTailoringRecipes(Consumer<FinishedRecipe> exporter)
    {
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_helmet"), Items.LEATHER, 2, 1, "from_leather", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_chestplate"), Items.LEATHER, 4, 1, "from_leather", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_leggings"), Items.LEATHER, 3, 1, "from_leather", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_boots"), Items.LEATHER, 2, 1, "from_leather", exporter);

		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_helmet"), CraftingMaterialModule.ITEMS.get("leather_panel"), 2, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_chestplate"), CraftingMaterialModule.ITEMS.get("leather_panel"), 4, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_leggings"), CraftingMaterialModule.ITEMS.get("leather_panel"), 3, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_leather_boots"), CraftingMaterialModule.ITEMS.get("leather_panel"), 2, 1, null, exporter);

		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_helmet"), Items.RABBIT_HIDE, 2, 1, "from_rabbit_hide", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_chestplate"), Items.RABBIT_HIDE, 4, 1, "from_rabbit_hide", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_leggings"), Items.RABBIT_HIDE, 3, 1, "from_rabbit_hide", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_boots"), Items.RABBIT_HIDE, 2, 1, "from_rabbit_hide", exporter);

		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_helmet"), CraftingMaterialModule.ITEMS.get("rabbit_hide_panel"), 2, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_chestplate"), CraftingMaterialModule.ITEMS.get("rabbit_hide_panel"), 4, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_leggings"), CraftingMaterialModule.ITEMS.get("rabbit_hide_panel"), 3, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_rabbit_hide_boots"), CraftingMaterialModule.ITEMS.get("rabbit_hide_panel"), 2, 1, null, exporter);

		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_helmet"), Items.WHITE_WOOL, 2, 1, "from_wool", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_chestplate"), Items.WHITE_WOOL, 4, 1, "from_wool", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_leggings"), Items.WHITE_WOOL, 3, 1, "from_wool", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_boots"), Items.WHITE_WOOL, 2, 1, "from_wool", exporter);

		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_helmet"), CraftingMaterialModule.ITEMS.get("wool_panel"), 2, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_chestplate"), CraftingMaterialModule.ITEMS.get("wool_panel"), 4, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_leggings"), CraftingMaterialModule.ITEMS.get("wool_panel"), 3, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("base_wool_boots"), CraftingMaterialModule.ITEMS.get("wool_panel"), 2, 1, null, exporter);

        SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("leather_binding"), Items.LEATHER, exporter);
        SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("rabbit_hide_binding"), Items.RABBIT_HIDE, exporter);
        SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("phantom_membrane_binding"), Items.PHANTOM_MEMBRANE, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("string_binding"), Items.STRING, 4, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("dried_kelp_binding"), Items.DRIED_KELP, 4, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("paper_binding"), Items.PAPER, 4, 1, null, exporter);

		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("tin_plate_armor_part"), CraftingMaterialModule.ITEMS.get("tin_ingot"), 2, 1, "from_ingot", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("copper_plate_armor_part"), Items.COPPER_INGOT, 2, 1, "from_ingot", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("gold_plate_armor_part"), Items.GOLD_INGOT, 2, 1, "from_ingot", exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get("bronze_plate_armor_part"), CraftingMaterialModule.ITEMS.get("bronze_ingot"), 2, 1, "from_ingot", exporter);

		SingleIngredientRecipeBuilder.tailoring(CraftingMaterialModule.ITEMS.get("wool_panel"), ItemTags.WOOL, 1, 2, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(CraftingMaterialModule.ITEMS.get("leather_panel"), Items.LEATHER, 1, 2, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(CraftingMaterialModule.ITEMS.get("rabbit_hide_panel"), Items.RABBIT_HIDE, 1, 2, null, exporter);

		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.TIN, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.COPPER, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.BRONZE, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.IRON, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.GOLD, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.ANTANIUM, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.STEEL, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.NETHERITE, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.NICKEL, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.INVAR, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.CONSTANTAN, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.TITANIUM, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.TITANIUM_GOLD, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.NITINOL, exporter);

		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.DIAMOND, Items.DIAMOND, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.LEATHER, Items.LEATHER, exporter);
		makeTailoringArmorPartRecipes(AlloygeryDefaultMaterials.RABBIT_HIDE, Items.RABBIT_HIDE, exporter);
    }

	private void makeTailoringArmorPartRecipes(AlloygeryMaterial material, Consumer<FinishedRecipe> exporter)
	{
		Item panelItem = CraftingMaterialModule.ITEMS.get(MaterialHelper.getSimpleName(material) + "_panel");
		makeTailoringArmorPartRecipes(material, panelItem, exporter);
	}

	private void makeTailoringArmorPartRecipes(AlloygeryMaterial material, Item panelItem, Consumer<FinishedRecipe> exporter)
	{
		String name = MaterialHelper.getSimpleName(material);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get(name + "_plate_armor_part"), panelItem, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get(name + "_scale_armor_part"), panelItem, 2, 1, null, exporter);
		SingleIngredientRecipeBuilder.tailoring(GearModule.ITEMS.get(name + "_heavy_plate_armor_part"), panelItem, 4, 1, null, exporter);
	}

    private void makeWoodcutterRecipes(Consumer<FinishedRecipe> exporter)
    {
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("acacia_handle"), Items.ACACIA_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("bamboo_handle"), Items.BAMBOO_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("birch_handle"), Items.BIRCH_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("cherry_handle"), Items.CHERRY_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("crimson_handle"), Items.CRIMSON_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("dark_oak_handle"), Items.DARK_OAK_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("jungle_handle"), Items.JUNGLE_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("mangrove_handle"), Items.MANGROVE_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("mushroom_stem_handle"), Items.MUSHROOM_STEM, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("oak_handle"), Items.OAK_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("spruce_handle"), Items.SPRUCE_PLANKS, exporter);
        SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("warped_handle"), Items.WARPED_PLANKS, exporter);
		SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("bone_handle"), Items.BONE, 2, 1, null, exporter);
		SingleIngredientRecipeBuilder.woodcutting(GearModule.ITEMS.get("stick_handle"), Items.STICK, 2, 1, null, exporter);
    }

	public static void generateArmorPlatingRecipe(Item result, Ingredient plate, Consumer<FinishedRecipe> exporter)
	{
		ArmorPlatingShapelessRecipeBuilder.armorPlating(result, Ingredient.of(ArmorBaseItem.ARMOR_ITEMS_TAG), plate).unlockedBy("has_plate", RecipeProvider.has(ArmorBaseItem.BASE_ARMOR_ITEMS_TAG)).save(exporter);
	}

    public static void generateToolShapedRecipe(Item item, Consumer<FinishedRecipe> exporter)
    {
        final Item toolHeadItem = BuiltInRegistries.ITEM.get(NBTHelper.getItemIdentifierFromTag(NBTHelper.getPartTagFromItemStack(item.getDefaultInstance(), TOOL_HEAD)));
        final Item resultItem = item.getDefaultInstance().getItem();
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, resultItem)
                .pattern("  h")
                .pattern(" b ")
                .pattern("l  ")
                .define('h', toolHeadItem)
                .define('b', Ingredient.of(ToolBinding.TOOL_BINDING_ITEMS_TAG))
                .define('l', Ingredient.of(ToolHandle.TOOL_HANDLE_ITEMS_TAG))
                .unlockedBy("has_tool_head_item", RecipeProvider.has(toolHeadItem))
                .save(exporter);
    }

    public static void generateBasicToolShapedRecipe(Item item, Consumer<FinishedRecipe> exporter)
    {
        final Item toolHeadItem = BuiltInRegistries.ITEM.get(NBTHelper.getItemIdentifierFromTag(NBTHelper.getPartTagFromItemStack(item.getDefaultInstance(), TOOL_HEAD)));
        final Item resultItem = item.getDefaultInstance().getItem();
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, resultItem)
                .pattern(" h")
                .pattern("l ")
                .define('h', toolHeadItem)
                .define('l', Ingredient.of(ToolHandle.TOOL_HANDLE_ITEMS_TAG))
                .unlockedBy("has_tool_head_item", RecipeProvider.has(toolHeadItem))
                .save(exporter);
    }

    public static void generateSwordShapedRecipe(Item item, Consumer<FinishedRecipe> exporter)
    {
        final Item swordBladeItem = BuiltInRegistries.ITEM.get(NBTHelper.getItemIdentifierFromTag(NBTHelper.getPartTagFromItemStack(item.getDefaultInstance(), TOOL_HEAD)));
        final Item resultItem = item.getDefaultInstance().getItem();
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, resultItem)
                .pattern("  h")
                .pattern(" b ")
                .pattern("l  ")
                .define('h', swordBladeItem)
                .define('b', Ingredient.of(HandGuard.HAND_GUARD_ITEMS_TAG))
                .define('l', Ingredient.of(ToolHandle.TOOL_HANDLE_ITEMS_TAG))
                .unlockedBy("has_tool_head_item", RecipeProvider.has(swordBladeItem))
                .save(exporter);
    }

    private static void generatePrimitiveToolSets(Consumer<FinishedRecipe> exporter)
    {
        // flint hatchet
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource("flint_hatchet")))
                .pattern("ff")
                .pattern("h ")
                .define('f', Ingredient.of(Items.FLINT))
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource("flint_hatchet_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_flint", RecipeProvider.has(Items.FLINT))
                .save(exporter, Alloygery.asResource("flint_hatchet_primitive"));

        // flint knife
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource("flint_knife")))
                .pattern(" f")
                .pattern("h ")
                .define('f', Ingredient.of(Items.FLINT))
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource("flint_knife_blade")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_flint", RecipeProvider.has(Items.FLINT))
                .save(exporter, Alloygery.asResource("flint_knife_primitive"));

        // flint pickaxe
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource("flint_pickaxe")))
                .pattern("fff")
                .pattern(" h ")
                .pattern(" h ")
                .define('f', Ingredient.of(Items.FLINT))
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource("flint_pickaxe_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_flint", RecipeProvider.has(Items.FLINT))
                .save(exporter, Alloygery.asResource("flint_pickaxe_primitive"));

        generatePrimitiveBasicToolSet("stone", ItemTags.STONE_TOOL_MATERIALS, exporter);

        generatePrimitiveBasicToolSet("copper", TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_ingots")), exporter);
        generatePrimitiveVanillaToolSet("copper", TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_ingots")), exporter);

        generatePrimitiveBasicToolSet("gold", TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_ingots")), exporter);

        generatePrimitiveBasicToolSet("bronze", TagKey.create(Registries.ITEM, Alloygery.asCommonResource("bronze_ingots")), exporter);
        generatePrimitiveVanillaToolSet("bronze", TagKey.create(Registries.ITEM, Alloygery.asCommonResource("bronze_ingots")), exporter);
    }

    private static void generatePrimitiveBasicToolSet(String materialName, TagKey<Item> materialTag, Consumer<FinishedRecipe> exporter)
    {
        final Ingredient headIngredient = Ingredient.of(materialTag);
        final CriterionTriggerInstance unlockedBy = RecipeProvider.has(materialTag);

        // hatchet
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_hatchet")))
                .pattern("ss")
                .pattern("h ")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_hatchet_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_hatchet_primitive"));

        // knife
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_knife")))
                .pattern(" s")
                .pattern("h ")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_knife_blade")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_knife_primitive"));
    }

    public static void generatePrimitiveVanillaToolSet(String materialName, TagKey<Item> headMaterial, Consumer<FinishedRecipe> exporter)
    {
        final Ingredient headIngredient = Ingredient.of(headMaterial);
        final CriterionTriggerInstance unlockedBy = RecipeProvider.has(headMaterial);

        // axe
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_axe")))
                .pattern("ss")
                .pattern("sh")
                .pattern(" h")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_axe_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_axe_primitive"));

        // pickaxe
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_pickaxe")))
                .pattern("sss")
                .pattern(" h ")
                .pattern(" h ")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_pickaxe_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_pickaxe_primitive"));

        // hoe
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_hoe")))
                .pattern("ss")
                .pattern(" h")
                .pattern(" h")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_hoe_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_hoe_primitive"));

        // shovel
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_shovel")))
                .pattern("s")
                .pattern("h")
                .pattern("h")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_shovel_head")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_shovel_primitive"));

        // sword
        ToolRecipeShapedBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_sword")))
                .pattern("s")
                .pattern("s")
                .pattern("h")
                .define('s', headIngredient)
                .define('h', Ingredient.of(Items.STICK))
                .substitute(TOOL_HEAD, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_sword_blade")).getDefaultInstance())
                .substitute(TOOL_BINDING, BuiltInRegistries.ITEM.get(Alloygery.asResource(materialName + "_guard")).getDefaultInstance())
                .substitute(TOOL_HANDLE, BuiltInRegistries.ITEM.get(Alloygery.asResource("stick_handle")).getDefaultInstance())
                .property(PrimitiveProperty.of())
                .unlockedBy("has_material", unlockedBy)
                .save(exporter, Alloygery.asResource(materialName + "_sword_primitive"));
    }
}
