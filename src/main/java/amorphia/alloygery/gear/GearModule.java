package amorphia.alloygery.gear;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.AlloygeryCreativeTabs;
import amorphia.alloygery.IAlloygeryCreativeTab;
import amorphia.alloygery.datagen.*;
import amorphia.alloygery.datagen.loot.AlloygeryChestLootTableProvider;
import amorphia.alloygery.datagen.loot.AlloygeryGlobalLootModifierDataProvider;
import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import amorphia.alloygery.gear.client.DynamicGearClientReloadListener;
import amorphia.alloygery.gear.client.DynamicGearItemModelPredicateProvider;
import amorphia.alloygery.gear.data.AlloygeryMaterialDataReloadListener;
import amorphia.alloygery.gear.data.AlloygeryMaterialNetworkPackets;
import amorphia.alloygery.gear.datagen.*;
import amorphia.alloygery.gear.datagen.convert.ConvertItemTagProvider;
import amorphia.alloygery.gear.datagen.convert.ConvertModelProvider;
import amorphia.alloygery.gear.datagen.convert.ConvertPackMetadataProvider;
import amorphia.alloygery.gear.datagen.convert.ConvertRecipeProvider;
import amorphia.alloygery.gear.datagen.recipe.GearRecipeProvider;
import amorphia.alloygery.gear.item.ArmorStyles;
import amorphia.alloygery.gear.item.SmithingImprovementItem;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.item.armor.DyeableArmorBaseItem;
import amorphia.alloygery.gear.item.part.*;
import amorphia.alloygery.gear.item.tool.*;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.ToolTypes;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.recipe.*;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Map;

import static amorphia.alloygery.gear.material.AlloygeryDefaultMaterials.*;
import static amorphia.alloygery.gear.item.PartTypes.*;
import static amorphia.alloygery.gear.item.ToolTypes.*;

public class GearModule
{
    private static final Map<PartTypes, GearItemMaker> PART_MAKERS = Maps.newHashMap();
    private static final Map<ToolTypes, GearItemMaker> HEAD_PART_MAKERS = Maps.newHashMap();

    public static final Map<String, Item> ITEMS = Maps.newLinkedHashMap();

    public static void initialize()
    {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(AlloygeryMaterialDataReloadListener.INSTANCE);

        Registry.register(BuiltInRegistries.RECIPE_TYPE, ToolRecipeShaped.Type.ID, ToolRecipeShaped.Type.INSTANCE);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ToolRecipeShaped.Type.ID, ToolRecipeShaped.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, BaseArmorRecipeShaped.Type.ID, BaseArmorRecipeShaped.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, BaseArmorRecipeShaped.Type.ID, BaseArmorRecipeShaped.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, ArmorPlatingRecipeShapeless.Type.ID, ArmorPlatingRecipeShapeless.Type.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ArmorPlatingRecipeShapeless.Type.ID, ArmorPlatingRecipeShapeless.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Alloygery.asResource("smithing_upgrade"), SmithingUpgradeRecipe.Serializer.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Alloygery.asResource("smithing_improvement"), SmithingImprovementRecipe.Serializer.INSTANCE);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, ConvertRecipeProvider.EmptyType.ID, ConvertRecipeProvider.EmptyType.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ConvertRecipeProvider.EmptyType.ID, ConvertRecipeProvider.EmptySerializer.INSTANCE);

		AlloygeryMaterialNetworkPackets.initialize();

        makeItems();
    }

    public static void initializeClient()
    {
        DynamicGearItemModelPredicateProvider.registerItemModelPredicateProviders();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(DynamicGearClientReloadListener.INSTANCE);

		AlloygeryMaterialNetworkPackets.initializeClient();
    }

    public static void initializeDataGen(FabricDataGenerator dataGenerator, FabricDataGenerator.Pack pack)
    {
        AlloygeryModelProvider.addProvider(new GearModelProvider());
        AlloygeryItemTagProvider.addProvider(new GearItemTagProvider());
        AlloygeryBlockTagProvider.addProvider(new GearBlockTagProvider());
        AlloygeryEnglishLanguageProvider.addProvider(new GearEnglishLanguageProvider());
        AlloygeryRecipeProvider.addProvider(new GearRecipeProvider());
		AlloygeryChestLootTableProvider.addProvider(new GearLootProvider.ChestLootProvider());
		AlloygeryGlobalLootModifierDataProvider.addProvider(new GearLootProvider.LootModifiers());

        pack.addProvider(GearAlloygeryMaterialProvider::new);

        FabricDataGenerator.Pack convertVanillaGearPack = dataGenerator.createBuiltinResourcePack(Alloygery.asResource("convert_vanilla_gear_to_alloygery_gear"));
        convertVanillaGearPack.addProvider(ConvertModelProvider::new);
        convertVanillaGearPack.addProvider(ConvertItemTagProvider::new);
        convertVanillaGearPack.addProvider(ConvertRecipeProvider::new);
		convertVanillaGearPack.addProvider((FabricDataGenerator.Pack.Factory<ConvertPackMetadataProvider>) ConvertPackMetadataProvider::new);
    }

    private static void makeItems()
    {
        EnumSet<ToolTypes> ALL_MINING_TOOLS = EnumSet.of(HATCHET, KNIFE, AXE, HOE, PICKAXE, SHOVEL, SWORD, SCYTHE, HAMMER, EXCAVATOR);
        EnumSet<ToolTypes> VANILLA_TOOL_TYPES = EnumSet.of(AXE, HOE, PICKAXE, SHOVEL, SWORD);
        EnumSet<ToolTypes> ALL_RANGED_WEAPONS = EnumSet.of(BOW, LONGBOW, RECURVE, CROSSBOW);
        EnumSet<ToolTypes> EMPTY_TOOL_TYPES = EnumSet.noneOf(ToolTypes.class);

        EnumSet<PartTypes> STANDARD_TOOL_PART_TYPES = EnumSet.of(TOOL_HEAD, TOOL_BINDING, TOOL_HANDLE);
        EnumSet<PartTypes> RANGED_WEAPON_TOOL_PART_TYPES = EnumSet.of(BOW_LIMB, BOW_BINDING, BOW_STRING);
        EnumSet<PartTypes> TOOL_HEADS = EnumSet.of(TOOL_HEAD);

        EnumSet<PartTypes> EMPTY_PART_TYPES = EnumSet.noneOf(PartTypes.class);

        // ###### Tools and Tool Parts ######

        // primitive
        makeGearItems(FLINT, TOOL_HEADS, EnumSet.of(HATCHET, KNIFE, PICKAXE));
        makeGearItems(STONE, TOOL_HEADS, EnumSet.of(HATCHET, KNIFE, AXE, HOE, PICKAXE, SHOVEL, SWORD));

        // metals
        makeGearItems(TIN, EnumSet.of(TOOL_BINDING, TOOL_HANDLE));
        makeGearItems(COPPER, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(BRONZE, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(IRON, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(GOLD, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(ANTANIUM, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(STEEL, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(NETHERITE, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(NICKEL, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(INVAR, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(CONSTANTAN, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(TITANIUM, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(TITANIUM_GOLD, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);
        makeGearItems(NITINOL, STANDARD_TOOL_PART_TYPES, ALL_MINING_TOOLS);

        // gems
        makeGearItems(DIAMOND, TOOL_HEADS, ALL_MINING_TOOLS);
        makeGearItems(AMETHYST, TOOL_HEADS, ALL_MINING_TOOLS);
        makeGearItems(EMERALD, TOOL_HEADS, ALL_MINING_TOOLS);

        // wood
        makeGearItems(ACACIA, EnumSet.of(TOOL_HANDLE));
        makeGearItems(BAMBOO, EnumSet.of(TOOL_HANDLE));
        makeGearItems(BIRCH, EnumSet.of(TOOL_HANDLE));
        makeGearItems(CHERRY, EnumSet.of(TOOL_HANDLE));
        makeGearItems(CRIMSON, EnumSet.of(TOOL_HANDLE));
        makeGearItems(DARK_OAK, EnumSet.of(TOOL_HANDLE));
        makeGearItems(JUNGLE, EnumSet.of(TOOL_HANDLE));
        makeGearItems(MANGROVE, EnumSet.of(TOOL_HANDLE));
        makeGearItems(MUSHROOM_STEM, EnumSet.of(TOOL_HANDLE));
        makeGearItems(OAK, EnumSet.of(TOOL_HANDLE));
        makeGearItems(SPRUCE, EnumSet.of(TOOL_HANDLE));
        // makeGearPartItems(STICK, EnumSet.of(TOOL_HANDLE)); // don't make this item because we make it in the static block
        makeGearItems(WARPED, EnumSet.of(TOOL_HANDLE));

        // leather and such
        makeGearItems(LEATHER, EnumSet.of(TOOL_BINDING));
        makeGearItems(RABBIT_HIDE, EnumSet.of(TOOL_BINDING));
        makeGearItems(PHANTOM_MEMBRANE, EnumSet.of(TOOL_BINDING));

        // misc
        makeGearItems(STRING, EnumSet.of(TOOL_BINDING));
        makeGearItems(PAPER, EnumSet.of(TOOL_BINDING));
        makeGearItems(BONE, EnumSet.of(TOOL_HANDLE));
        makeGearItems(DRIED_KELP, EnumSet.of(TOOL_BINDING));

        // ###### Armor and Armor Parts ######

        makeGearItems(WOOL, EnumSet.of(ARMOR_BASE));
        makeGearItems(LEATHER, EnumSet.of(ARMOR_BASE));
		makeGearItems(RABBIT_HIDE, EnumSet.of(ARMOR_BASE));
		makeGearItems(IRON, EnumSet.of(ARMOR_BASE));

        makeGearItems(TIN, EnumSet.of(ARMOR_PLATE));
        makeGearItems(COPPER, EnumSet.of(ARMOR_PLATE));
        makeGearItems(BRONZE, EnumSet.of(ARMOR_PLATE));
        makeGearItems(IRON, EnumSet.of(ARMOR_PLATE));
        makeGearItems(GOLD, EnumSet.of(ARMOR_PLATE));
        makeGearItems(ANTANIUM, EnumSet.of(ARMOR_PLATE));
        makeGearItems(DIAMOND, EnumSet.of(ARMOR_PLATE));
        makeGearItems(STEEL, EnumSet.of(ARMOR_PLATE));
        makeGearItems(NETHERITE, EnumSet.of(ARMOR_PLATE));
        makeGearItems(NICKEL, EnumSet.of(ARMOR_PLATE));
        makeGearItems(INVAR, EnumSet.of(ARMOR_PLATE));
        makeGearItems(CONSTANTAN, EnumSet.of(ARMOR_PLATE));
        makeGearItems(TITANIUM, EnumSet.of(ARMOR_PLATE));
        makeGearItems(TITANIUM_GOLD, EnumSet.of(ARMOR_PLATE));
        makeGearItems(NITINOL, EnumSet.of(ARMOR_PLATE));
        makeGearItems(LEATHER, EnumSet.of(ARMOR_PLATE));
		makeGearItems(RABBIT_HIDE, EnumSet.of(ARMOR_PLATE));

		// ##### Smithing Templates #####

		registerItem("tipped_template", SmithingImprovementItem.tipped(), AlloygeryCreativeTabs.VANILLA_INGREDIENTS);
		registerItem("plated_template", SmithingImprovementItem.plated(), AlloygeryCreativeTabs.VANILLA_INGREDIENTS);
		registerItem("wrapped_template", SmithingImprovementItem.wrapped(), AlloygeryCreativeTabs.VANILLA_INGREDIENTS);
    }

    private static void makeGearItems(AlloygeryMaterial material, EnumSet<PartTypes> partTypes)
    {
        makeGearItems(material, partTypes, EnumSet.noneOf(ToolTypes.class));
    }

    private static void makeGearItems(AlloygeryMaterial material, EnumSet<PartTypes> partTypes, EnumSet<ToolTypes> headTypes)
    {
        if (partTypes.contains(TOOL_HEAD))
        {
            for(ToolTypes toolType : headTypes)
            {
                if(HEAD_PART_MAKERS.containsKey(toolType))
                    HEAD_PART_MAKERS.get(toolType).makePartItem(material);
            }
        }

        for(PartTypes partType : partTypes)
        {
            if(PART_MAKERS.containsKey(partType))
                PART_MAKERS.get(partType).makePartItem(material);
        }
    }

    private static Item registerItem(String name, Item item)
    {
        ITEMS.put(name, item);
        return Registry.register(BuiltInRegistries.ITEM, Alloygery.asResource(name), item);
    }

	private static Item registerItem(String name, Item item, IAlloygeryCreativeTab tab)
	{
		tab.add(item);
		return registerItem(name, item);
	}

	private static void makeBaseArmorItem(String path, AlloygeryMaterial alloygeryMaterial, ArmorItem.Type type, ArmorStyles style)
	{
		registerItem(
				path,
				style != ArmorStyles.CHAIN
						? new DyeableArmorBaseItem(type)  {
							@Override
							public @NotNull ItemStack getDefaultInstance()
							{
								ItemStack stack = super.getDefaultInstance();
								final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE});
								final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(ARMOR_BASE, style, alloygeryMaterial);
								baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(alloygeryMaterial.getMaterialIdentifier()).color());
								final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag);
								stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
								return stack;
							}
						}
						: new ArmorBaseItem(type) {
							@Override
							public @NotNull ItemStack getDefaultInstance()
							{
								ItemStack stack = super.getDefaultInstance();
								final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE});
								final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(ARMOR_BASE, style, alloygeryMaterial);
								final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag);
								stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
								return stack;
							}
						},
				AlloygeryCreativeTabs.VANILLA_COMBAT
		);
	}

	private static void makePlatedArmorItem(String path, AlloygeryMaterial alloygeryMaterial, ArmorItem.Type type, Item armorPlatePart)
	{
		registerItem(
				path,
				new ArmorBaseItem(type) {
					@Override
					public @NotNull ItemStack getDefaultInstance()
					{
						ItemStack stack = super.getDefaultInstance();
						final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE, ARMOR_PLATE});
						final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(GearModule.ITEMS.get("base_leather_" + type.getName()), ARMOR_BASE, ArmorStyles.LEATHER, LEATHER);
						baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(LEATHER.getMaterialIdentifier()).color());
						final CompoundTag plateTag = NBTHelper.createArmorPartTagWithStyle(armorPlatePart, ARMOR_PLATE, ArmorStyles.SCALE, alloygeryMaterial);
						if(alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE)
						{
							plateTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(alloygeryMaterial.getMaterialIdentifier()).color());
						}
						final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag, plateTag);
						stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
						return stack;
					}
				},
				AlloygeryCreativeTabs.VANILLA_COMBAT
		);
	}

    interface GearItemMaker
    {
        void makePartItem(AlloygeryMaterial material);
    }

    static
    {
        Item stickHandleItem = registerItem(MaterialHelper.getSimpleName(STICK) + "_handle", new ToolHandle(STICK));
        AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(stickHandleItem);

        PART_MAKERS.put(TOOL_BINDING, material -> {
            Item binding;
            if (material.isDyeable())
            {
                binding = new DyeableToolBinding(material);
            }
            else
            {
                binding = new ToolBinding(material);
            }
            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(binding);
            registerItem(MaterialHelper.getSimpleName(material) + "_binding", binding);
        });

        PART_MAKERS.put(TOOL_HANDLE, material -> AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(registerItem(MaterialHelper.getSimpleName(material) + "_handle", new ToolHandle(material))));

        HEAD_PART_MAKERS.put(AXE, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item axeHeadItem = registerItem(materialName + "_axe_head", new AxeHead(material));
            Item axeItem = registerItem(materialName + "_axe", new DynamicAxeItem(){
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{TOOL_HEAD, TOOL_HANDLE});
                    final CompoundTag headTag = NBTHelper.createPartTag(axeHeadItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(axeHeadItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(axeItem);
            AlloygeryCreativeTabs.VANILLA_COMBAT.add(axeItem);
        });

        HEAD_PART_MAKERS.put(HOE, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item hoeHeadItem = registerItem(materialName + "_hoe_head", new HoeHead(material));
            Item hoeItem = registerItem(materialName + "_hoe", new DynamicHoeItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{TOOL_HEAD, TOOL_HANDLE});
                    final CompoundTag headTag = NBTHelper.createPartTag(hoeHeadItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(hoeHeadItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(hoeItem);
        });

        HEAD_PART_MAKERS.put(PICKAXE, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item pickaxeHeadItem = registerItem(materialName + "_pickaxe_head", new PickaxeHead(material));
            Item pickaxeItem = registerItem(materialName + "_pickaxe", new DynamicPickaxeItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{TOOL_HEAD, TOOL_HANDLE});
                    final CompoundTag headTag = NBTHelper.createPartTag(pickaxeHeadItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(pickaxeHeadItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(pickaxeItem);
        });

        HEAD_PART_MAKERS.put(SHOVEL, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item headItem = registerItem(materialName + "_shovel_head", new ShovelHead(material));
            Item toolItem = registerItem(materialName + "_shovel", new DynamicShovelItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{TOOL_HEAD, TOOL_HANDLE});
                    final CompoundTag headTag = NBTHelper.createPartTag(headItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(headItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(toolItem);
        });

        HEAD_PART_MAKERS.put(SWORD, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item headItem = registerItem(materialName + "_sword_blade", new SwordBlade(material));
            Item guardItem = registerItem(materialName + "_guard", new HandGuard(material));
            Item toolItem = registerItem(materialName + "_sword", new DynamicSwordItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{TOOL_HEAD, TOOL_HANDLE, TOOL_BINDING});
                    final CompoundTag headTag = NBTHelper.createPartTag(headItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag guardTag = NBTHelper.createPartTag(guardItem, TOOL_BINDING, material);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag, guardTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(headItem);
            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(guardItem);
            AlloygeryCreativeTabs.VANILLA_COMBAT.add(toolItem);
        });

        HEAD_PART_MAKERS.put(HATCHET, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item headItem = registerItem(materialName + "_hatchet_head", new HatchetHead(material));
            Item toolItem = registerItem(materialName + "_hatchet", new DynamicHatchetItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[] { TOOL_HEAD, TOOL_HANDLE });
                    final CompoundTag headTag = NBTHelper.createPartTag(headItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(headItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(toolItem);
            AlloygeryCreativeTabs.VANILLA_COMBAT.add(toolItem);
        });

        HEAD_PART_MAKERS.put(KNIFE, material ->
        {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item headItem = registerItem(materialName + "_knife_blade", new KnifeBlade(material));
            Item toolItem = registerItem(materialName + "_knife", new DynamicKnifeItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[] { TOOL_HEAD, TOOL_HANDLE });
                    final CompoundTag headTag = NBTHelper.createPartTag(headItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(headItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(toolItem);
            AlloygeryCreativeTabs.VANILLA_COMBAT.add(toolItem);
        });

        HEAD_PART_MAKERS.put(HAMMER, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item headItem = registerItem(materialName + "_hammer_head", new HammerHead(material));
            Item toolItem = registerItem(materialName + "_hammer", new DynamicHammerItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[] { TOOL_HEAD, TOOL_HANDLE });
                    final CompoundTag headTag = NBTHelper.createPartTag(headItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(headItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(toolItem);
        });

        HEAD_PART_MAKERS.put(EXCAVATOR, material -> {
            final String materialName = MaterialHelper.getSimpleName(material);
            Item headItem = registerItem(materialName + "_excavator_head", new ExcavatorHead(material));
            Item toolItem = registerItem(materialName + "_excavator", new DynamicExcavatorItem()
            {
                @Override
                public @NotNull ItemStack getDefaultInstance()
                {
                    ItemStack stack = super.getDefaultInstance();
                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[] { TOOL_HEAD, TOOL_HANDLE });
                    final CompoundTag headTag = NBTHelper.createPartTag(headItem, TOOL_HEAD, material);
                    final CompoundTag handleTag = NBTHelper.createPartTag(stickHandleItem, TOOL_HANDLE, STICK);
                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, headTag, handleTag);
                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
                    return stack;
                }
            });

            AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS.add(headItem);
            AlloygeryCreativeTabs.VANILLA_TOOLS.add(toolItem);
        });

        PART_MAKERS.put(ARMOR_BASE, alloygeryMaterial -> {
            final String materialName = MaterialHelper.getSimpleName(alloygeryMaterial);
            final ArmorStyles style = materialName.equals("leather") || materialName.endsWith("hide") ? ArmorStyles.LEATHER : materialName.endsWith("wool") ? ArmorStyles.WOOL : ArmorStyles.CHAIN;

			makeBaseArmorItem("base_" + MaterialHelper.getSimpleName(alloygeryMaterial) + (style == ArmorStyles.CHAIN ? "_chain_helmet" : "_helmet"), alloygeryMaterial, ArmorItem.Type.HELMET, style);
			makeBaseArmorItem("base_" + MaterialHelper.getSimpleName(alloygeryMaterial) + (style == ArmorStyles.CHAIN ? "_chain_chestplate" : "_chestplate"), alloygeryMaterial, ArmorItem.Type.CHESTPLATE, style);
			makeBaseArmorItem("base_" + MaterialHelper.getSimpleName(alloygeryMaterial) + (style == ArmorStyles.CHAIN ? "_chain_leggings" : "_leggings"), alloygeryMaterial, ArmorItem.Type.LEGGINGS, style);
			makeBaseArmorItem("base_" + MaterialHelper.getSimpleName(alloygeryMaterial) + (style == ArmorStyles.CHAIN ? "_chain_boots" : "_boots"), alloygeryMaterial, ArmorItem.Type.BOOTS, style);
        });

        PART_MAKERS.put(ARMOR_PLATE, alloygeryMaterial -> {
            final String materialName = MaterialHelper.getSimpleName(alloygeryMaterial);

			registerItem(materialName + "_plate_armor_part", alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE ? new DyeableArmorPart(alloygeryMaterial, ArmorStyles.PLATE) : new ArmorPart(alloygeryMaterial, ArmorStyles.PLATE), AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS);
			registerItem(materialName + "_heavy_plate_armor_part", alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE ? new DyeableArmorPart(alloygeryMaterial, ArmorStyles.HEAVY_PLATE) : new ArmorPart(alloygeryMaterial, ArmorStyles.HEAVY_PLATE), AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS);
			Item armorPlatePart = registerItem(materialName + "_scale_armor_part", alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE ? new DyeableArmorPart(alloygeryMaterial, ArmorStyles.SCALE) : new ArmorPart(alloygeryMaterial, ArmorStyles.SCALE), AlloygeryCreativeTabs.ALLOYGERY_PART_ITEMS);

			makePlatedArmorItem(materialName + "_helmet", alloygeryMaterial, ArmorItem.Type.HELMET, armorPlatePart);
			makePlatedArmorItem(materialName + "_chestplate", alloygeryMaterial, ArmorItem.Type.CHESTPLATE, armorPlatePart);
			makePlatedArmorItem(materialName + "_leggings", alloygeryMaterial, ArmorItem.Type.LEGGINGS, armorPlatePart);
			makePlatedArmorItem(materialName + "_boots", alloygeryMaterial, ArmorItem.Type.BOOTS, armorPlatePart);

//            // helmet
//            Item helmet = registerItem(materialName + "_helmet", new ArmorBaseItem(ArmorItem.Type.HELMET) {
//                @Override
//                public @NotNull ItemStack getDefaultInstance()
//                {
//                    ItemStack stack = super.getDefaultInstance();
//                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE, ARMOR_PLATE});
//                    final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(GearModule.ITEMS.get("base_leather_helmet"), ARMOR_BASE, ArmorStyles.LEATHER, LEATHER);
//                    baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(LEATHER.getMaterialIdentifier()).color());
//                    final CompoundTag plateTag = NBTHelper.createArmorPartTagWithStyle(armorPlatePart, ARMOR_PLATE, ArmorStyles.PLATE, alloygeryMaterial);
//                    if(alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE)
//                    {
//                        plateTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(alloygeryMaterial.getMaterialIdentifier()).color());
//                    }
//                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag, plateTag);
//                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
//                    return stack;
//                }
//            });
//
//            // chestplate
//            Item chestplate = registerItem(materialName + "_chestplate", new ArmorBaseItem(ArmorItem.Type.CHESTPLATE) {
//                @Override
//                public @NotNull ItemStack getDefaultInstance()
//                {
//                    ItemStack stack = super.getDefaultInstance();
//                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE, ARMOR_PLATE});
//                    final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(GearModule.ITEMS.get("base_leather_chestplate"), ARMOR_BASE, ArmorStyles.LEATHER, LEATHER);
//                    baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(LEATHER.getMaterialIdentifier()).color());
//                    final CompoundTag plateTag = NBTHelper.createArmorPartTagWithStyle(armorPlatePart, ARMOR_PLATE, ArmorStyles.PLATE, alloygeryMaterial);
//					if(alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE)
//					{
//						plateTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(alloygeryMaterial.getMaterialIdentifier()).color());
//					}
//                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag, plateTag);
//                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
//                    return stack;
//                }
//            });
//
//            // leggings
//            Item leggings = registerItem(materialName + "_leggings", new ArmorBaseItem(ArmorItem.Type.LEGGINGS) {
//                @Override
//                public @NotNull ItemStack getDefaultInstance()
//                {
//                    ItemStack stack = super.getDefaultInstance();
//                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE, ARMOR_PLATE});
//                    final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(GearModule.ITEMS.get("base_leather_leggings"), ARMOR_BASE, ArmorStyles.LEATHER, LEATHER);
//                    baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(LEATHER.getMaterialIdentifier()).color());
//                    final CompoundTag plateTag = NBTHelper.createArmorPartTagWithStyle(armorPlatePart, ARMOR_PLATE, ArmorStyles.PLATE, alloygeryMaterial);
//					if(alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE)
//					{
//						plateTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(alloygeryMaterial.getMaterialIdentifier()).color());
//					}
//                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag, plateTag);
//                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
//                    return stack;
//                }
//            });
//
//            // boots
//            Item boots = registerItem(materialName + "_boots", new ArmorBaseItem(ArmorItem.Type.BOOTS) {
//                @Override
//                public @NotNull ItemStack getDefaultInstance()
//                {
//                    ItemStack stack = super.getDefaultInstance();
//                    final ListTag partsTag = NBTHelper.createPartsListTag(new PartTypes[]{ARMOR_BASE, ARMOR_PLATE});
//                    final CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(GearModule.ITEMS.get("base_leather_boots"), ARMOR_BASE, ArmorStyles.LEATHER, LEATHER);
//                    baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(LEATHER.getMaterialIdentifier()).color());
//                    final CompoundTag plateTag = NBTHelper.createArmorPartTagWithStyle(armorPlatePart, ARMOR_PLATE, ArmorStyles.PLATE, alloygeryMaterial);
//					if(alloygeryMaterial == LEATHER || alloygeryMaterial == RABBIT_HIDE)
//					{
//						plateTag.putInt(AlloygeryNBTKeys.DYE_COLOR, AlloygeryMaterialColors.get(alloygeryMaterial.getMaterialIdentifier()).color());
//					}
//                    final CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(this, partsTag, baseTag, plateTag);
//                    stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
//                    return stack;
//                }
//            });
//
//            AlloygeryCreativeTabs.VANILLA_COMBAT.add(helmet, chestplate, leggings, boots);
        });
    }
}
