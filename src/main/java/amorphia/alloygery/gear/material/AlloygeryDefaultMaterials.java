package amorphia.alloygery.gear.material;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.property.*;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

import static amorphia.alloygery.gear.item.PartTypes.*;
import static amorphia.alloygery.gear.property.PropertyOperation.*;

public class AlloygeryDefaultMaterials
{
    // meta materials
    public static final AlloygeryMaterial UNKNOWN = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("unknown")).build());
    public static final AlloygeryMaterial HIDDEN = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("hidden")).build());

    // metals
    public static final AlloygeryMaterial TIN;
    public static final AlloygeryMaterial COPPER;
    public static final AlloygeryMaterial BRONZE;
    public static final AlloygeryMaterial IRON;
    public static final AlloygeryMaterial GOLD;
    public static final AlloygeryMaterial ANTANIUM;
    public static final AlloygeryMaterial STEEL;
    public static final AlloygeryMaterial NETHERITE;
    public static final AlloygeryMaterial NICKEL;
    public static final AlloygeryMaterial INVAR;
    public static final AlloygeryMaterial CONSTANTAN;
    public static final AlloygeryMaterial TITANIUM;
    public static final AlloygeryMaterial TITANIUM_GOLD;
    public static final AlloygeryMaterial NITINOL;

    // gems
    public static final AlloygeryMaterial AMETHYST;
    public static final AlloygeryMaterial EMERALD;
    public static final AlloygeryMaterial DIAMOND;
    public static final AlloygeryMaterial FLINT;
    public static final AlloygeryMaterial LAPIS;
    public static final AlloygeryMaterial REDSTONE;
    public static final AlloygeryMaterial QUARTZ;
    public static final AlloygeryMaterial PRISMARINE;

    // wood
    public static final AlloygeryMaterial ACACIA;
    public static final AlloygeryMaterial BAMBOO;
    public static final AlloygeryMaterial BIRCH;
    public static final AlloygeryMaterial CHERRY;
    public static final AlloygeryMaterial CRIMSON;
    public static final AlloygeryMaterial DARK_OAK;
    public static final AlloygeryMaterial JUNGLE;
    public static final AlloygeryMaterial MANGROVE;
    public static final AlloygeryMaterial MUSHROOM_STEM;
    public static final AlloygeryMaterial OAK;
    public static final AlloygeryMaterial SPRUCE;
    public static final AlloygeryMaterial STICK;
    public static final AlloygeryMaterial WARPED;

    // leather and pelts and such
    public static final AlloygeryMaterial LEATHER;
    public static final AlloygeryMaterial RABBIT_HIDE;
    public static final AlloygeryMaterial PHANTOM_MEMBRANE;
    public static final AlloygeryMaterial WOOL;

    // misc
    public static final AlloygeryMaterial STONE;
    public static final AlloygeryMaterial STRING;
    public static final AlloygeryMaterial PAPER;
    public static final AlloygeryMaterial BONE;
    public static final AlloygeryMaterial DRIED_KELP;

    private static AlloygeryMaterial register(ResourceLocation identifier, AlloygeryMaterial material)
    {
        return AlloygeryMaterialRegistry.register(identifier, material);
    }

    private static AlloygeryMaterial register(AlloygeryMaterial material)
    {
        return register(material.materialIdentifier, material);
    }

    private static ResourceLocation asAlloygeryMaterial(String name)
    {
        return Alloygery.asResource("alloygery_material/" + name);
    }

    static
    {
        // metals
        TIN = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("tin"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("tin_ingots")))
				.standardToolProperties(150, 1, 8, 4.0f, 0.0f, 1.0f)
				.standardToolBindingProperties(0, 0, 0, 0, 0, 0)
				.standardToolHandleProperties(0.6f, 0.6f, 0.8f, 0.4f, 1, 1)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 50))

				.standardArmorPlateProperties(5, 0, 2, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
                .build());

        COPPER = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("copper"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("copper_ingots")))
				.standardToolProperties(150, 1, 10, 4.0f, 0.0f, 1.0f)
				.standardToolBindingProperties(25, 0, 2, -1, 0, -1)
				.standardToolHandleProperties(0.7f, 0.6f, 0.8f, 0.4f, 1.0f, 1.0f)
				.toolAttributes(AdditionalEntityAttributes.DROPPED_EXPERIENCE, 10, 3, 1.3f)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 50))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, TOOL_IMPROVEMENT, ADDITION, 10))

				.standardArmorPlateProperties(10, 10, 4, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, ARMOR_PLATE, ADDITION, 10))
                .build());

        BRONZE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("bronze"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("bronze_ingots")))
				.standardToolProperties(250, 2, 15, 5.0f, 0.0f, 2.0f)
				.standardToolBindingProperties(75, 0, 5, 0, 0, 0)
				.standardToolHandleProperties(0.9f, 0.8f, 1.0f, 0.8f, 1.0f, 1.0f)
				.toolAttributes(AdditionalEntityAttributes.DROPPED_EXPERIENCE, 6, 2, 1.2f)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 75))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, TOOL_IMPROVEMENT, ADDITION, 6))

				.standardArmorPlateProperties(15, 15, 5, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, ARMOR_PLATE, ADDITION, 8))
                .build());

        IRON = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("iron"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("iron_ingots")))
				.standardToolProperties(700, 2, 14, 6.0f, 0.0f, 2.0f)
				.standardToolBindingProperties(100, 0, 5, 1, 0, 0)
				.standardToolHandleProperties(1.1f, 1.0f, 1.1f, 0.9f, 1.2f, 1.0f)
				.toolAttributes(AdditionalEntityAttributes.COLLECTION_RANGE, 2, 1, 1.1f)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 100))
				.property(AttributeProperty.of(AdditionalEntityAttributes.COLLECTION_RANGE, TOOL_IMPROVEMENT, ADDITION, 2))

				.standardBaseArmorProperties(15, 10, 10, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MOB_DETECTION_RANGE, ARMOR_BASE, BASE, 20))
				.standardArmorPlateProperties(15, 14, 5, 0, 0)
                .build());

        GOLD = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("gold"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_ingots")))
				.standardToolProperties(32, 0, 22, 15.0f, 0.0f, 0.0f)
				.standardToolBindingProperties(-25, -1, 10, 5, 0, -3)
				.standardToolHandleProperties(0.5f, 1.5f, 0.8f, 0.3f, 1, 1)
                .property(LovedByPiglinsProperty.of(TOOL_HEAD, BASE, 3))
				.property(LovedByPiglinsProperty.of(TOOL_BINDING, ADDITION, 1))
				.property(LovedByPiglinsProperty.of(TOOL_HANDLE, ADDITION, 1))

				.property(LovedByPiglinsProperty.of(TOOL_IMPROVEMENT, ADDITION, 3))
				.property(MiningSpeedProperty.of(TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.5f))
				.property(MiningLevelProperty.of(TOOL_IMPROVEMENT, ADDITION, -1))

				.standardArmorPlateProperties(7, 20, 4, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(LovedByPiglinsProperty.of(ARMOR_PLATE, BASE, 3))
                .build());

        ANTANIUM = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("antanium"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("antanium_ingots")))
				.standardToolProperties(1000, 1, 20, 10.0f, 0.8f, 1.0f)
				.standardToolBindingProperties(100, 0, 8, 4, 0, -1)
				.standardToolHandleProperties(1, 1.4f, 0.9f, 0.7f, 1.1f, 1)
				.toolAttributes(AdditionalEntityAttributes.DROPPED_EXPERIENCE, 4, 1, 1.1f)
				.toolAttributes(AdditionalEntityAttributes.COLLECTION_RANGE, 2, 1, 1.1f)
				.property(LovedByPiglinsProperty.of(TOOL_HEAD, BASE, 2))
				.property(LovedByPiglinsProperty.of(TOOL_BINDING, ADDITION, 1))
				.property(LovedByPiglinsProperty.of(TOOL_HANDLE, ADDITION, 1))

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 100))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, TOOL_IMPROVEMENT, ADDITION, 4))
				.property(AttributeProperty.of(AdditionalEntityAttributes.COLLECTION_RANGE, TOOL_IMPROVEMENT, ADDITION, 1))
				.property(MiningSpeedProperty.of(TOOL_IMPROVEMENT, ADDITION, 4))
				.property(LovedByPiglinsProperty.of(TOOL_IMPROVEMENT, ADDITION, 1))

				.standardArmorPlateProperties(20, 10, 6, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, ARMOR_PLATE, ADDITION, 5))
                .build());

        STEEL = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("steel"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("steel_ingots")))
				.standardToolProperties(2892, 3, 8, 8, -0.5f, 4)
				.standardToolBindingProperties(500, 0, -5, 0, 0, 1)
				.standardToolHandleProperties(1.5f, 1, 0.9f, 1.1f, 1.3f, 1.1f)
				.toolAttributes(AdditionalEntityAttributes.COLLECTION_RANGE, 4, 2, 1.3f)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.5f))
				.property(AttributeProperty.of(AdditionalEntityAttributes.COLLECTION_RANGE, TOOL_IMPROVEMENT, ADDITION, 4))

				.standardArmorPlateProperties(50, 8, 10, 4, 40)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MAGIC_PROTECTION, ARMOR_PLATE, ADDITION, 5))
				.property(EncumbranceProperty.of(ARMOR_PLATE, ADDITION, 8))
                .build());

		NETHERITE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("netherite"))
				.repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("netherite_ingots")))
				.standardToolProperties(2031, 4, 15, 9, 0.0f, 4)
				.standardToolBindingProperties(150, 0, -5, 0, 0, 1)
				.standardToolHandleProperties(1, 1, 1, 1, 1, 1)
				.toolAttributes(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, 2, 1, 1.1f)
				.property(FireproofProperty.of(TOOL_HEAD, BASE, 3))
				.property(FireproofProperty.of(TOOL_BINDING, ADDITION, 1))
				.property(FireproofProperty.of(TOOL_HANDLE, ADDITION, 1))
				
				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.1f))
				.property(FireproofProperty.of(TOOL_IMPROVEMENT, ADDITION, 3))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.2f))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, TOOL_IMPROVEMENT, ADDITION, 1))

				.property(DurabilityProperty.of(TOOL_UPGRADE, MULTIPLY_TOTAL, 1.1f))
				.property(FireproofProperty.of(TOOL_UPGRADE, ADDITION, 3))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_UPGRADE, MULTIPLY_TOTAL, 1.2f))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, TOOL_UPGRADE, ADDITION, 1))

				.standardArmorPlateProperties(40, 10, 12, 8, 80)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MAGIC_PROTECTION, ARMOR_PLATE, ADDITION, 10))
				.property(EncumbranceProperty.of(ARMOR_PLATE, ADDITION, 6))
				.property(FireProtectionProperty.of(ARMOR_PLATE, ADDITION, 200))
				.property(FireproofProperty.of(ARMOR_PLATE, ADDITION, 2))

				.property(FireproofProperty.of(ARMOR_UPGRADE, ADDITION, 3))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MAGIC_PROTECTION, ARMOR_UPGRADE, ADDITION, 10))
				.property(FireProtectionProperty.of(ARMOR_UPGRADE, ADDITION, 200))
				.build());

        NICKEL = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("nickel"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("nickel_ingots")))
				.standardToolProperties(1784, 4, 5, 8, -1.0f, 4)
				.standardToolBindingProperties(150, 0, -1, -2, -1, 2)
				.standardToolHandleProperties(0.9f, 0.7f, 0.7f, 1.2f, 1, 1)
				.toolAttributes(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, 2, 1, 1.1f)
				
				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 200))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_IMPROVEMENT, ADDITION, 1))

				.standardArmorPlateProperties(37, 5, 8, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MAGIC_PROTECTION, ARMOR_PLATE, ADDITION, 8))
				.property(EncumbranceProperty.of(ARMOR_PLATE, ADDITION, 4))
                .build());

        INVAR = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("invar"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("invar_ingots")))
				.standardToolProperties(3127, 4, 5, 7, -1, 7)
				.standardToolBindingProperties(400, 0, -5, -1, -1, 4)
				.standardToolHandleProperties(1.3f, 0.9f, 0.7f, 1.4f, 1, 1)
				.toolAttributes(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, 4, 2, 1.3f)
				.toolAttributes(AdditionalEntityAttributes.COLLECTION_RANGE, 2, 1, 1.1f)
				
				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 500))
				.property(AttributeProperty.of(AdditionalEntityAttributes.COLLECTION_RANGE, TOOL_IMPROVEMENT, ADDITION, 1))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.2f))

				.standardArmorPlateProperties(45, 5, 12, 4, 10)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MAGIC_PROTECTION, ARMOR_PLATE, ADDITION, 10))
				.property(EncumbranceProperty.of(ARMOR_PLATE, ADDITION, 8))
                .build());

        CONSTANTAN = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("constantan"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("constantan_ingots")))
				.standardToolProperties(2031, 3, 8, 5, -0.2f, 3)
				.standardToolBindingProperties(155, 0, 0, -1, -1, 2)
				.standardToolHandleProperties(1, 0.8f, 0.7f, 1, 1, 1)
				.toolAttributes(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, 2, 1, 1.1f)
				.toolAttributes(AdditionalEntityAttributes.DROPPED_EXPERIENCE, 4, 1, 1.1f)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 155))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_IMPROVEMENT, ADDITION, 1))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, TOOL_IMPROVEMENT, ADDITION, 4))

				.standardArmorPlateProperties(38, 8, 8, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MAGIC_PROTECTION, ARMOR_PLATE, ADDITION, 5))
				.property(EncumbranceProperty.of(ARMOR_PLATE, ADDITION, 4))
				.property(AttributeProperty.of(AdditionalEntityAttributes.DROPPED_EXPERIENCE, ARMOR_PLATE, ADDITION, 5))
				.property(FireProtectionProperty.of(ARMOR_PLATE, ADDITION, 100))
                .build());

        TITANIUM = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("titanium"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("titanium_ingots")))
				.standardToolProperties(2221, 5, 10, 9, 2, 3)
				.standardToolBindingProperties(100, 0, 5, 2.5f, 1.2f, 0)
				.standardToolHandleProperties(1, 1, 1.3f, 0.9f, 1, 1)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 100))
				.property(MiningSpeedProperty.of(TOOL_IMPROVEMENT, ADDITION, 2))

				.standardArmorPlateProperties(30, 10, 12, 0, 0)
				.property(AttributeProperty.of(PortingLibAttributes.ENTITY_GRAVITY, ARMOR_PLATE, ADDITION, -50.0f))
                .build());

        TITANIUM_GOLD = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("titanium_gold"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("titanium_gold_ingots")))
				.standardToolProperties(1600, 4, 25, 14, 2, 2)
				.standardToolBindingProperties(25, 0, 12, 2, 0, 0)
				.standardToolHandleProperties(0.9f, 1.4f, 1.2f, 0.7f, 1 ,1)
				.property(LovedByPiglinsProperty.of(TOOL_HEAD, BASE, 3))
				.property(LovedByPiglinsProperty.of(TOOL_BINDING, ADDITION, 1))
				.property(LovedByPiglinsProperty.of(TOOL_HANDLE, ADDITION, 1))

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 25))
				.property(LovedByPiglinsProperty.of(TOOL_IMPROVEMENT, ADDITION, 1))
				.property(MiningSpeedProperty.of(TOOL_IMPROVEMENT, ADDITION, 2))

				.standardArmorPlateProperties(28, 12, 10, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(LovedByPiglinsProperty.of(ARMOR_PLATE, BASE, 3))
				.property(AttributeProperty.of(PortingLibAttributes.ENTITY_GRAVITY, ARMOR_PLATE, ADDITION, -10.0f))
                .build());

        NITINOL = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("nitinol"))
                .repairIngredientFromTag(TagKey.create(Registries.ITEM, Alloygery.asCommonResource("nitinol_ingots")))
				.standardToolProperties(2471, 5, 10, 9, 1, 4)
				.standardToolBindingProperties(100, 0, 8, 1, 1, 1)
				.standardToolHandleProperties(1, 1, 1.1f, 1, 1, 1.1f)
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_HEAD, BASE, 2))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_BINDING, ADDITION, 1))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_HANDLE, MULTIPLY_BASE, 1.3f))

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, ADDITION, 100))
				.property(MiningSpeedProperty.of(TOOL_IMPROVEMENT, ADDITION, 1))
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_IMPROVEMENT, ADDITION, 1))

				.standardArmorPlateProperties(35, 10, 12, 0, 32)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(PortingLibAttributes.ENTITY_GRAVITY, ARMOR_PLATE, ADDITION, -20.0f))
                .build());

        // gems
        AMETHYST = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("amethyst"))
                .repairIngredientFromItem(Items.AMETHYST_SHARD)
				.standardToolProperties(1000, 2, 10, 5, 0, 2)
                .property(AttributeProperty.of(AdditionalEntityAttributes.BONUS_LOOT_COUNT_ROLLS, TOOL_HEAD, BASE, 20))

				.property(AttributeProperty.of(AdditionalEntityAttributes.BONUS_LOOT_COUNT_ROLLS, TOOL_IMPROVEMENT, ADDITION, 10))
                .build());

        EMERALD = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("emerald"))
                .repairIngredientFromItem(Items.EMERALD)
				.standardToolProperties(1200, 2, 10, 6, 0, 2)

				.property(DurabilityProperty.of(TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.5f))
                .build());

        DIAMOND = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("diamond"))
                .repairIngredientFromItem(Items.DIAMOND)
				.standardToolProperties(1561, 3, 10, 8, 0, 3)
				.standardArmorPlateProperties(35, 10, 8, 4, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))

				.property(MiningLevelProperty.of(TOOL_IMPROVEMENT, ADDITION, 1))
                .build());

        FLINT = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("flint"))
                .repairIngredientFromItem(Items.FLINT)
				.standardToolProperties(60, 0, 5, 0, 0.0f, 0)
                .build());

        LAPIS = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("lapis"))
				.property(AttributeProperty.of(AdditionalEntityAttributes.BONUS_RARE_LOOT_ROLLS, TOOL_IMPROVEMENT, ADDITION, 10))
				.build());

        REDSTONE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("redstone"))
				.property(MiningSpeedProperty.of(TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.3f))
				.property(AttributeProperty.of(Attributes.ATTACK_SPEED, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.3f))
				.build());

        QUARTZ = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("quartz"))
				.property(AttributeProperty.of(Attributes.ATTACK_DAMAGE, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.3f))
				.build());

        PRISMARINE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("prismarine")).build());

        // wood
        ACACIA = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("acacia"))
                .repairIngredientFromItem(Items.ACACIA_PLANKS)
				.standardToolHandleProperties(1.0f, 1.2f, 0.8f, 1.0f, 1.0f, 1.0f)
                .build());

        BAMBOO = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("bamboo"))
                .repairIngredientFromItem(Items.BAMBOO_PLANKS)
				.standardToolHandleProperties(0.8f, 1.0f, 1.0f, 0.8f, 1.2f, 1.2f)
                .build());

        BIRCH = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("birch"))
                .repairIngredientFromItem(Items.BIRCH_PLANKS)
				.standardToolHandleProperties(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
                .build());

        CHERRY = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("cherry"))
                .repairIngredientFromItem(Items.CHERRY_PLANKS)
				.standardToolHandleProperties(1.0f, 1.1f, 1.1f, 1.0f, 1.0f, 0.8f)
                .build());

        CRIMSON = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("crimson"))
                .repairIngredientFromItem(Items.CRIMSON_PLANKS)
				.standardToolHandleProperties(0.8f, 1.0f, 0.8f, 1.2f, 1.0f, 1.1f)
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_HANDLE, MULTIPLY_BASE, 1.1f))
				.property(FireproofProperty.of(TOOL_HANDLE, ADDITION, 1))
                .build());

        DARK_OAK = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("dark_oak"))
                .repairIngredientFromItem(Items.DARK_OAK_PLANKS)
				.standardToolHandleProperties(1.2f, 0.8f, 0.8f, 1.0f, 1.0f, 1.2f)
                .build());

        JUNGLE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("jungle"))
                .repairIngredientFromItem(Items.JUNGLE_PLANKS)
				.standardToolHandleProperties(1.0f, 1.1f, 1.0f, 1.0f, 1.1f, 0.8f)
                .build());

        MANGROVE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("mangrove"))
                .repairIngredientFromItem(Items.MANGROVE_PLANKS)
				.standardToolHandleProperties(1.0f, 0.8f, 1.2f, 1.0f, 1.0f, 1.0f)
                .build());

        MUSHROOM_STEM = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("mushroom_stem"))
				.standardToolHandleProperties(0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
                .build());

        OAK = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("oak"))
                .repairIngredientFromItem(Items.OAK_PLANKS)
				.standardToolHandleProperties(1.2f, 0.8f, 0.8f, 1.0f, 1.2f, 1.0f)
                .build());

        SPRUCE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("spruce"))
                .repairIngredientFromItem(Items.SPRUCE_PLANKS)
				.standardToolHandleProperties(1.0f, 1.0f, 1.0f, 1.1f, 0.8f, 1.1f)
                .build());

        STICK = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("stick"))
				.standardToolHandleProperties(0.8f, 1.0f, 1.0f, 1.0f, 0.8f, 0.8f)
                .build());

        WARPED = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("warped"))
                .repairIngredientFromItem(Items.WARPED_PLANKS)
				.standardToolHandleProperties(0.8f, 1.0f, 1.2f, 0.8f, 1.0f, 1.1f)
				.property(AttributeProperty.of(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, TOOL_HANDLE, MULTIPLY_BASE, 1.1f))
				.property(FireproofProperty.of(TOOL_HANDLE, ADDITION, 1))
                .build());

        // leather, hide, and other animal drops
        LEATHER = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("leather"))
				.repairIngredientFromItem(Items.LEATHER)
				.standardToolBindingProperties(10, 0, 0, 0, 0, 0)

				.property(AttributeProperty.of(ReachEntityAttributes.REACH, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.1f))
				.property(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.1f))

				.standardBaseArmorProperties(5, 15, 5, 0, 0)
				.property(FreezeProtectionProperty.of(ARMOR_BASE, BASE, 100))
				.property(WalkOnPowderedSnowProperty.of(ARMOR_BASE, BASE, 3))
				.standardArmorPlateProperties(5, 15, 5, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(FreezeProtectionProperty.of(ARMOR_PLATE, ADDITION, 100))
				.dyeable(true)
				.build());

        RABBIT_HIDE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("rabbit_hide"))
				.standardToolBindingProperties(10, 0, 0, 1, 0, 0)

				.property(AttributeProperty.of(ReachEntityAttributes.REACH, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.2f))
				.property(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, TOOL_IMPROVEMENT, MULTIPLY_TOTAL, 1.1f))

				.standardBaseArmorProperties(5, 15, 5, 0, 0)
				.property(FreezeProtectionProperty.of(ARMOR_BASE, BASE, 100))
				.property(WalkOnPowderedSnowProperty.of(ARMOR_BASE, BASE, 3))
				.standardArmorPlateProperties(5, 15, 5, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.JUMP_HEIGHT, ARMOR_BASE, BASE, 20))
				.property(FreezeProtectionProperty.of(ARMOR_PLATE, ADDITION, 100))
				.property(AttributeProperty.of(AdditionalEntityAttributes.JUMP_HEIGHT, ARMOR_PLATE, ADDITION, 20))
				.dyeable(true)
				.build());

        PHANTOM_MEMBRANE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("phantom_membrane"))
				.standardToolBindingProperties(10, 0, 0, 0, 1, 0)
				.build());

        WOOL = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("wool"))
				.property(OccludeVibrationsProperty.of(TOOL_IMPROVEMENT, ADDITION, 3))

				.standardBaseArmorProperties(5, 20, 0, 0, 0)
				.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
				.property(AttributeProperty.of(AdditionalEntityAttributes.MOB_DETECTION_RANGE, ARMOR_BASE, BASE, -20))
				.property(FreezeProtectionProperty.of(ARMOR_BASE, BASE, 200))
				.property(OccludeVibrationsProperty.of(ARMOR_BASE, BASE, 3))
				.build());

        // misc
        STONE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("stone"))
                .repairIngredientFromTag(ItemTags.STONE_TOOL_MATERIALS)
				.standardToolProperties(131, 1, 5, 4, 0, 1)
                .build());

        STRING = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("string")).build());
        PAPER = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("paper"))
				.standardToolBindingProperties(0, 0, 20, 0.0f, 0.0f, 0.0f)
				.build());
        BONE = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("bone"))
				.standardToolHandleProperties(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.1f)
				.build());
        DRIED_KELP = register(new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("dried_kelp")).build());
    }
}
