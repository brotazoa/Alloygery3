package amorphia.alloygery.gear.item;

import amorphia.alloygery.Alloygery;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class SmithingImprovementItem extends SmithingTemplateItem
{
	public static final Component APPLIES_TO_ARMOR;
	public static final Component APPLIES_TO_TOOL;

	public static final Component GEM_INGREDIENTS;
	public static final Component INGOT_INGREDIENTS;
	public static final Component PANEL_INGREDIENTS;

	public static final Component BASE_TOOL_DESCRIPTION;
	public static final Component BASE_ARMOR_DESCRIPTION;

	public static final Component ADDITION_GEM_DESCRIPTION;
	public static final Component ADDITION_INGOT_DESCRIPTION;
	public static final Component ADDITION_PANEL_DESCRIPTION;

	public static final ResourceLocation EMPTY_SLOT_HELMET;
	public static final ResourceLocation EMPTY_SLOT_CHESTPLATE;
	public static final ResourceLocation EMPTY_SLOT_LEGGINGS;
	public static final ResourceLocation EMPTY_SLOT_BOOTS;
	public static final ResourceLocation EMPTY_SLOT_HOE;
	public static final ResourceLocation EMPTY_SLOT_AXE;
	public static final ResourceLocation EMPTY_SLOT_SWORD;
	public static final ResourceLocation EMPTY_SLOT_SHOVEL;
	public static final ResourceLocation EMPTY_SLOT_PICKAXE;
	public static final ResourceLocation EMPTY_SLOT_EXCAVATOR;
	public static final ResourceLocation EMPTY_SLOT_HAMMER;
	public static final ResourceLocation EMPTY_SLOT_HATCHET;
	public static final ResourceLocation EMPTY_SLOT_KNIFE;
	public static final ResourceLocation EMPTY_SLOT_INGOT;
	public static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST;
	public static final ResourceLocation EMPTY_SLOT_QUARTZ;
	public static final ResourceLocation EMPTY_SLOT_EMERALD;
	public static final ResourceLocation EMPTY_SLOT_DIAMOND;
	public static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI;
	public static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD;
	public static final ResourceLocation EMPTY_SLOT_PANEL;

	private final ImprovementTypes improvementType;

	public static SmithingImprovementItem tipped()
	{
		return new SmithingImprovementItem(
				ImprovementTypes.TIPPED,
				APPLIES_TO_TOOL,
				GEM_INGREDIENTS,
				Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource("tipped"))),
				BASE_TOOL_DESCRIPTION,
				ADDITION_GEM_DESCRIPTION,
				createImprovableToolIconList(),
				createGemMaterialIconList()
		);
	}

	public static SmithingImprovementItem plated()
	{
		return new SmithingImprovementItem(
				ImprovementTypes.PLATED,
				APPLIES_TO_TOOL,
				INGOT_INGREDIENTS,
				Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource("plated"))),
				BASE_TOOL_DESCRIPTION,
				ADDITION_INGOT_DESCRIPTION,
				createImprovableToolIconList(),
				createIngotMaterialIconList()
		);
	}

	public static SmithingImprovementItem wrapped()
	{
		return new SmithingImprovementItem(
				ImprovementTypes.WRAPPED,
				APPLIES_TO_TOOL,
				PANEL_INGREDIENTS,
				Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource("wrapped"))),
				BASE_TOOL_DESCRIPTION,
				ADDITION_PANEL_DESCRIPTION,
				createImprovableToolIconList(),
				createPanelMaterialIconList()
		);
	}

	public static SmithingImprovementItem engraved()
	{
		return new SmithingImprovementItem(
				ImprovementTypes.ENGRAVED,
				APPLIES_TO_ARMOR,
				GEM_INGREDIENTS,
				Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource("engraved"))),
				BASE_ARMOR_DESCRIPTION,
				ADDITION_GEM_DESCRIPTION,
				createImprovableArmorIconList(),
				createGemMaterialIconList()
		);
	}

	public static SmithingImprovementItem reinforced()
	{
		return new SmithingImprovementItem(
				ImprovementTypes.REINFORCED,
				APPLIES_TO_ARMOR,
				INGOT_INGREDIENTS,
				Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource("reinforced"))),
				BASE_ARMOR_DESCRIPTION,
				ADDITION_INGOT_DESCRIPTION,
				createImprovableArmorIconList(),
				createIngotMaterialIconList()
		);
	}

	public static SmithingImprovementItem padded()
	{
		return new SmithingImprovementItem(
				ImprovementTypes.PADDED,
				APPLIES_TO_ARMOR,
				PANEL_INGREDIENTS,
				Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource("padded"))),
				BASE_ARMOR_DESCRIPTION,
				ADDITION_PANEL_DESCRIPTION,
				createImprovableArmorIconList(),
				createPanelMaterialIconList()
		);
	}

	public SmithingImprovementItem(ImprovementTypes improvementType, Component appliesTo, Component ingredients, Component upgradeDescription,
			Component baseSlotDescription, Component additionsSlotDescription,
			List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons)
	{
		super(appliesTo, ingredients, upgradeDescription, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons);
		this.improvementType = improvementType;
	}

	public ImprovementTypes getImprovementType()
	{
		return improvementType;
	}

	private static List<ResourceLocation> createImprovableArmorIconList()
	{
		return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS);
	}

	private static List<ResourceLocation> createImprovableToolIconList()
	{
		return List.of(EMPTY_SLOT_AXE, EMPTY_SLOT_EXCAVATOR, EMPTY_SLOT_HAMMER, EMPTY_SLOT_HATCHET, EMPTY_SLOT_HOE, EMPTY_SLOT_KNIFE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_SHOVEL, EMPTY_SLOT_SWORD);
	}

	private static List<ResourceLocation> createGemMaterialIconList()
	{
		return List.of(EMPTY_SLOT_AMETHYST_SHARD, EMPTY_SLOT_EMERALD, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_QUARTZ);
	}

	private static List<ResourceLocation> createPanelMaterialIconList()
	{
		return List.of(EMPTY_SLOT_PANEL);
	}

	private static List<ResourceLocation> createIngotMaterialIconList()
	{
		return List.of(EMPTY_SLOT_INGOT);
	}

	static
	{
		APPLIES_TO_ARMOR = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.applies_to_armor"))).withStyle(ChatFormatting.BLUE);
		APPLIES_TO_TOOL = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.applies_to_tool"))).withStyle(ChatFormatting.BLUE);

		GEM_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.gem_ingredients"))).withStyle(ChatFormatting.BLUE);
		INGOT_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.ingot_ingredients"))).withStyle(ChatFormatting.BLUE);
		PANEL_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.panel_ingredients"))).withStyle(ChatFormatting.BLUE);

		BASE_TOOL_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.base_tool_slot_description")));
		BASE_ARMOR_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.base_armor_slot_description")));

		ADDITION_GEM_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.addition_gem_slot_description")));
		ADDITION_INGOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.addition_ingot_slot_description")));
		ADDITION_PANEL_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.addition_panel_slot_description")));

		EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
		EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
		EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
		EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
		EMPTY_SLOT_HOE = new ResourceLocation("item/empty_slot_hoe");
		EMPTY_SLOT_AXE = new ResourceLocation("item/empty_slot_axe");
		EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
		EMPTY_SLOT_SHOVEL = new ResourceLocation("item/empty_slot_shovel");
		EMPTY_SLOT_PICKAXE = new ResourceLocation("item/empty_slot_pickaxe");
		EMPTY_SLOT_EXCAVATOR = Alloygery.asResource("item/empty_slot_excavator");
		EMPTY_SLOT_HAMMER = Alloygery.asResource("item/empty_slot_hammer");
		EMPTY_SLOT_HATCHET = Alloygery.asResource("item/empty_slot_hatchet");
		EMPTY_SLOT_KNIFE = Alloygery.asResource("item/empty_slot_knife");
		EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");
		EMPTY_SLOT_REDSTONE_DUST = new ResourceLocation("item/empty_slot_redstone_dust");
		EMPTY_SLOT_QUARTZ = new ResourceLocation("item/empty_slot_quartz");
		EMPTY_SLOT_EMERALD = new ResourceLocation("item/empty_slot_emerald");
		EMPTY_SLOT_DIAMOND = new ResourceLocation("item/empty_slot_diamond");
		EMPTY_SLOT_LAPIS_LAZULI = new ResourceLocation("item/empty_slot_lapis_lazuli");
		EMPTY_SLOT_AMETHYST_SHARD = new ResourceLocation("item/empty_slot_amethyst_shard");
		EMPTY_SLOT_PANEL = Alloygery.asResource("item/empty_slot_panel");
	}
}
