package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.item.*;
import amorphia.alloygery.gear.material.AlloygeryMaterialRegistry;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.property.PropertyType;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class GearEnglishLanguageProvider implements AlloygeryEnglishLanguageProvider.IAlloygeryEnglishLanguageProvider
{
    @Override
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder)
    {
		GearModule.ITEMS.forEach((path, item) -> {
			if (item instanceof ITranslationGen itemWithTranslation)
			{
				itemWithTranslation.provideTranslation(translationBuilder);
			}
			else translationBuilder.add(Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(item)), AlloygeryEnglishLanguageProvider.englishNameFromPath(path));
		});

        PropertyType.REGISTRY.forEach(propertyType -> addPropertyTranslations(PropertyType.REGISTRY.getKey(propertyType), translationBuilder));
		AlloygeryMaterialRegistry.forEach((resourceLocation, material) -> translationBuilder.add(resourceLocation.toLanguageKey(), AlloygeryEnglishLanguageProvider.englishNameFromPath(MaterialHelper.getSimpleName(material))));

        for(PartTypes type : PartTypes.VALUES_CACHE)
        {
            translationBuilder.add("part_type.alloygery." + type.getName() + ".tooltip", AlloygeryEnglishLanguageProvider.englishNameFromPath(type.getName()));
        }

        for(ToolTypes type : ToolTypes.VALUES_CACHE)
        {
            translationBuilder.add("tool_type.alloygery." + type.getName() + ".tooltip", AlloygeryEnglishLanguageProvider.englishNameFromPath(type.getName()));
        }

        for(ImprovementTypes improvement : ImprovementTypes.VALUES_CACHE)
        {
            translationBuilder.add("improvement_type.alloygery." + improvement.getName() + ".tooltip", AlloygeryEnglishLanguageProvider.englishNameFromPath(improvement.getName()));
			translationBuilder.add(Util.makeDescriptionId("improvement", Alloygery.asResource(improvement.getName())), AlloygeryEnglishLanguageProvider.englishNameFromPath(improvement.getName() + "_improvement"));
        }

        for(ArmorStyles style : ArmorStyles.VALUES_CACHE)
        {
            translationBuilder.add("armor_style.alloygery." + style.getName() + ".tooltip", AlloygeryEnglishLanguageProvider.englishNameFromPath(style.getName()));
        }

		for(UpgradeTypes upgrade : UpgradeTypes.VALUES_CACHE)
		{
			translationBuilder.add("upgrade_type.alloygery." + upgrade.getName() + ".tooltip", AlloygeryEnglishLanguageProvider.englishNameFromPath(upgrade.getName()));
			translationBuilder.add(Util.makeDescriptionId("upgrade", Alloygery.asResource(upgrade.getName())), AlloygeryEnglishLanguageProvider.englishNameFromPath(upgrade.getName() + "_upgrade"));
		}

        translationBuilder.add("tooltip.alloygery.shift_prompt", "Hold Shift To View Properties");
        translationBuilder.add("tooltip.alloygery.parts_list", "Parts:");
        translationBuilder.add("tooltip.alloygery.properties", "Properties:");
        translationBuilder.add("tooltip.alloygery.dye_color", "Dye Color");
        translationBuilder.add("tooltip.alloygery.armor_style", "Style");
		translationBuilder.add("tooltip.alloygery.upgrade", "Upgrade Material");
		translationBuilder.add("tooltip.alloygery.improvement", "Improvement Material");

        translationBuilder.add("property.alloygery.broken.tooltip", "Broken");
        translationBuilder.add("property.alloygery.broken.description", "This item needs to be repaired to use.");
        translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.FIREPROOF).toLanguageKey("property", "description"), "The item will not be destroyed by fire or lava.");
        translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.FIRE_PROTECTION).toLanguageKey("property", "description"), "Extra seconds before taking damage from being on fire.");
        translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.FREEZE_PROTECTION).toLanguageKey("property", "description"), "Extra seconds before taking damage from being frozen.");
        translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.LOVED_BY_PIGLINS).toLanguageKey("property", "description"), "Equipping this item will passify nearby Piglins.");
        translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.PRIMITIVE).toLanguageKey("property", "description"), "This item is destroyed when it's durability reaches zero.");
        translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.WALK_ON_POWDERED_SNOW).toLanguageKey("property", "description"), "Equipping this item will allow walking on top of powdered snow.");
		translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.OCCLUDE_VIBRATIONS).toLanguageKey("property", "armor_description"), "Wearing this item will dampen vibrations caused by stepping and falling.");
		translationBuilder.add(PropertyType.REGISTRY.getKey(PropertyType.OCCLUDE_VIBRATIONS).toLanguageKey("property", "tool_description"), "Breaking blocks with this tool won't cause vibrations");

		// generic attribute tooltips
		translationBuilder.add("property.alloygery.generic_attribute.base", "Base ");
		translationBuilder.add("property.alloygery.generic_attribute.total", "Total ");
		translationBuilder.add("property.alloygery.generic_attribute.part", " From Part");

		// smithing improvement tooltips
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.applies_to_armor")), "Armor");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.applies_to_tool")), "Tools");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.gem_ingredients")), "Crystals");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.ingot_ingredients")), "Ingots");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.panel_ingredients")), "Fabric & Hide Panels");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.base_tool_slot_description")), "Add a weapon, or tool");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.base_armor_slot_description")), "Add a piece of armor");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.addition_gem_slot_description")), "Add a Crystal");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.addition_ingot_slot_description")), "Add an Ingot");
		translationBuilder.add(Util.makeDescriptionId("item", Alloygery.asResource("smithing_improvement.addition_panel_slot_description")), "Add a Fabric, or Hide Panel");

		// tags
		translateAlloygeryItemTag("armor_items", translationBuilder);
		translateAlloygeryItemTag("armor_plate_items", translationBuilder);
		translateAlloygeryItemTag("axe_head_items", translationBuilder);
		translateAlloygeryItemTag("base_armor_items", translationBuilder);
		translateAlloygeryItemTag("boots_items", translationBuilder);
		translateAlloygeryItemTag("chestplate_items", translationBuilder);
		translateAlloygeryItemTag("excavator_head_items", translationBuilder);
		translateAlloygeryItemTag("hammer_head_items", translationBuilder);
		translateAlloygeryItemTag("hand_guard_items", translationBuilder);
		translateAlloygeryItemTag("hatchet_head_items", translationBuilder);
		translateAlloygeryItemTag("helmet_items", translationBuilder);
		translateAlloygeryItemTag("hoe_head_items", translationBuilder);
		translateAlloygeryItemTag("knife_blade_items", translationBuilder);
		translateAlloygeryItemTag("leggings_items", translationBuilder);
		translateAlloygeryItemTag("pickaxe_head_items", translationBuilder);
		translateAlloygeryItemTag("plated_armor_items", translationBuilder);
		translateAlloygeryItemTag("shovel_head_items", translationBuilder);
		translateAlloygeryItemTag("sword_blade_items", translationBuilder);
		translateAlloygeryItemTag("tool_binding_items", translationBuilder);
		translateAlloygeryItemTag("tool_handle_items", translationBuilder);
		translateAlloygeryItemTag("upgradeable_equipment", translationBuilder);
		translateAlloygeryItemTag("improvable_tool", translationBuilder);
		translateAlloygeryItemTag("improvable_armor", translationBuilder);
    }

    private void addPropertyTranslations(ResourceLocation location, FabricLanguageProvider.TranslationBuilder translationBuilder)
    {
        final String englishFromLocation = AlloygeryEnglishLanguageProvider.englishNameFromResourceLocation(location);
        translationBuilder.add(location.toLanguageKey("property", "tooltip"), englishFromLocation);
        translationBuilder.add(location.toLanguageKey("property", "base"), "Base " + englishFromLocation);
        translationBuilder.add(location.toLanguageKey("property", "total"), "Total " + englishFromLocation);
		translationBuilder.add(location.toLanguageKey("property", "part"), englishFromLocation + " from Part");
    }

	private void translateAlloygeryItemTag(String tagPath, FabricLanguageProvider.TranslationBuilder translationBuilder)
	{
		translationBuilder.add("tag.item.alloygery." + tagPath, AlloygeryEnglishLanguageProvider.englishNameFromPath(tagPath.replace("_items", "")));
	}

	public interface ITranslationGen
	{
		void provideTranslation(FabricLanguageProvider.TranslationBuilder translationBuilder);
	}
}
