package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryModelProvider;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.client.DynamicGearItemModelPredicateProvider;
import amorphia.alloygery.gear.item.*;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.NBTHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

public class GearModelProvider implements AlloygeryModelProvider.IAlloygeryModelProvider
{
    public static final TextureSlot LAYER3 = TextureSlot.create("layer3");
    public static final TextureSlot LAYER4 = TextureSlot.create("layer4");
	public static final TextureSlot LAYER5 = TextureSlot.create("layer5");

    public static final EnumSet<ImprovementTypes> TOOL_IMPROVEMENT_TYPES = EnumSet.of(ImprovementTypes.TIPPED, ImprovementTypes.PLATED, ImprovementTypes.WRAPPED);
    public static final EnumSet<ImprovementTypes> ARMOR_IMPROVEMENT_TYPES = EnumSet.of(ImprovementTypes.PADDED, ImprovementTypes.ENGRAVED, ImprovementTypes.REINFORCED);
    public static final EnumSet<ArmorStyles> ARMOR_BASE_STYLES = EnumSet.of(ArmorStyles.CHAIN, ArmorStyles.LEATHER, ArmorStyles.WOOL);

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators)
    {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators)
    {
        for(Item item : GearModule.ITEMS.values())
        {
            if (item instanceof GearItemModelGenerator itemWithModelGen)
            {
                itemWithModelGen.generateItemModel(itemModelGenerators);
            }
            else
            {
                itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        }
    }

    public interface GearItemModelGenerator
    {
        void generateItemModel(ItemModelGenerators itemModelGenerator);
    }

    public static void generateArmorModel(ItemStack armorStack, ItemModelGenerators itemModelGenerator)
    {
        if (armorStack.getItem() instanceof ArmorItem armorItem)
        {
            final ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
            final ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(armorStack.getItem());
            final ArmorItem.Type armorType = armorItem.getType();
            final ResourceLocation baseTexture = Alloygery.asResource("template/render_part/armor/base_" + armorType.getName());
			final ResourceLocation upgradeTexture = Alloygery.asResource("template/render_part/armor/upgrade_" + armorType.getName());
            final ResourceLocation trimmedTexture = Alloygery.asResource("template/render_part/armor/trim_" + armorType.getName());

            if (NBTHelper.hasPartTag(armorStack, PartTypes.ARMOR_PLATE))
            {
                final AlloygeryMaterial plateMaterial = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_PLATE, armorStack);
                final ResourceLocation plateTexture = Alloygery.asResource("template/render_part/armor/plate_" + armorType.getName() + "_" + MaterialHelper.getSimpleName(plateMaterial));

                template.create(
                        modelLocation,
                        new TextureMapping().putForced(TextureSlot.LAYER0, Alloygery.asResource("template/render_part/this_should_never_render")),
                        itemModelGenerator.output,
                        (resourceLocation, map) -> GearModelProvider.makeDynamicArmorOverrides(resourceLocation, map, template,  ARMOR_BASE_STYLES, ARMOR_IMPROVEMENT_TYPES)
                );

                for(ArmorStyles style : ARMOR_BASE_STYLES)
                {
                    final ResourceLocation baseStyleTexture = Alloygery.asResource("template/render_part/armor/base_" + armorType.getName() + "_" + style.getName());
                    template.create(
                            modelLocation.withSuffix("_" + style.getName()),
                            new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, plateTexture),
                            itemModelGenerator.output
                    );
					template.create(
							modelLocation.withSuffix("_" + style.getName() + "_upgraded"),
							new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, upgradeTexture).putForced(LAYER3, plateTexture),
							itemModelGenerator.output
					);
                    template.create(
                            modelLocation.withSuffix("_" + style.getName() + "_trimmed"),
                            new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, plateTexture).putForced(LAYER3, trimmedTexture),
                            itemModelGenerator.output
                    );
					template.create(
							modelLocation.withSuffix("_" + style.getName() + "_upgraded_trimmed"),
							new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, upgradeTexture).putForced(LAYER3, plateTexture).putForced(LAYER4, trimmedTexture),
							itemModelGenerator.output
					);

                    for(ImprovementTypes improvement : ARMOR_IMPROVEMENT_TYPES)
                    {
                        final ResourceLocation improvementTexture = Alloygery.asResource("template/render_part/armor/improvement_" + armorType.getName() + "_" + improvement.getName());
                        template.create(
                                modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName()),
                                new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, plateTexture).putForced(LAYER3, improvementTexture),
                                itemModelGenerator.output
                        );
						template.create(
								modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName() + "_upgraded"),
								new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, plateTexture).putForced(LAYER3, improvementTexture).putForced(LAYER4, upgradeTexture),
								itemModelGenerator.output
						);
                        template.create(
                                modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName() + "_trimmed"),
                                new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, plateTexture).putForced(LAYER3, improvementTexture).putForced(LAYER4, trimmedTexture),
                                itemModelGenerator.output
                        );
						template.create(
								modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName() + "_upgraded_trimmed"),
								new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, baseStyleTexture).putForced(TextureSlot.LAYER2, plateTexture).putForced(LAYER3, improvementTexture).putForced(LAYER4, upgradeTexture).putForced(LAYER5, trimmedTexture),
								itemModelGenerator.output
						);
                    }
                }
            }
            else
            {
                final ArmorStyles baseStyle = NBTHelper.getArmorStyleFromStack(armorStack, PartTypes.ARMOR_BASE);
                final ResourceLocation styleTexture = Alloygery.asResource("template/render_part/armor/base_" + armorType.getName() + "_" + baseStyle.getName());
                template.create(
                        modelLocation,
                        new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, styleTexture),
                        itemModelGenerator.output,
                        (resourceLocation, map) -> GearModelProvider.makeDynamicArmorOverrides(resourceLocation, map, template, EnumSet.noneOf(ArmorStyles.class), EnumSet.noneOf(ImprovementTypes.class))
                );

                template.create(
                        modelLocation.withSuffix("_trimmed"),
                        new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, styleTexture).putForced(TextureSlot.LAYER2, trimmedTexture),
                        itemModelGenerator.output
                );
				template.create(
						modelLocation.withSuffix("_upgraded"),
						new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, styleTexture).putForced(TextureSlot.LAYER2, upgradeTexture),
						itemModelGenerator.output
				);
				template.create(
						modelLocation.withSuffix("_upgraded_trimmed"),
						new TextureMapping().putForced(TextureSlot.LAYER0, baseTexture).putForced(TextureSlot.LAYER1, styleTexture).putForced(TextureSlot.LAYER2, upgradeTexture).putForced(LAYER3, trimmedTexture),
						itemModelGenerator.output
				);
            }
        }
    }

    private static JsonObject makeDynamicArmorOverrides(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> textureMap, ModelTemplate baseTemplate, EnumSet<ArmorStyles> baseStyles, EnumSet<ImprovementTypes> upgrades)
    {
        JsonObject base = baseTemplate.createBaseTemplate(modelLocation, textureMap);
        JsonArray overridesArray = new JsonArray();

		// upgrade
		JsonObject upgradeOverrideObject = new JsonObject();
		JsonObject upgradePredicateObject = new JsonObject();
		upgradePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
		upgradeOverrideObject.add("predicate", upgradePredicateObject);
		upgradeOverrideObject.addProperty("model", modelLocation.withSuffix("_upgraded").toString());
		overridesArray.add(upgradeOverrideObject);

		// trim
        JsonObject trimOverrideObject = new JsonObject();
        JsonObject trimPredicateObject = new JsonObject();
        trimPredicateObject.addProperty(Alloygery.asVanillaResource("trim_type").toString(), 0.1f);
        trimOverrideObject.add("predicate", trimPredicateObject);
        trimOverrideObject.addProperty("model", modelLocation.withSuffix("_trimmed").toString());
        overridesArray.add(trimOverrideObject);

		// upgrade and trim
		JsonObject upgradedTrimmedOverrideObject = new JsonObject();
		JsonObject upgradedTrimmedPredicateObject = new JsonObject();
		upgradedTrimmedPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
		upgradedTrimmedPredicateObject.addProperty(Alloygery.asVanillaResource("trim_type").toString(), 0.1f);
		upgradedTrimmedOverrideObject.add("predicate", upgradedTrimmedPredicateObject);
		upgradedTrimmedOverrideObject.addProperty("model", modelLocation.withSuffix("_upgraded_trimmed").toString());
		overridesArray.add(upgradedTrimmedOverrideObject);

        for(ArmorStyles style : baseStyles)
        {
			// style
            JsonObject styleOverrideObject = new JsonObject();
            JsonObject stylePredicateObject = new JsonObject();
            stylePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
            styleOverrideObject.add("predicate", stylePredicateObject);
            styleOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName()).toString());
            overridesArray.add(styleOverrideObject);

			// style and upgrade
			JsonObject upgradedStyleOverrideObject = new JsonObject();
			JsonObject upgradedStylePredicateObject = new JsonObject();
			upgradedStylePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
			upgradedStylePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
			upgradedStyleOverrideObject.add("predicate", upgradedStylePredicateObject);
			upgradedStyleOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_upgraded").toString());

			// style and trim
            JsonObject trimmedStyleOverrideObject = new JsonObject();
            JsonObject trimmedStylePredicateObject = new JsonObject();
            trimmedStylePredicateObject.addProperty(Alloygery.asVanillaResource("trim_type").toString(), 0.1f);
            trimmedStylePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
            trimmedStyleOverrideObject.add("predicate", trimmedStylePredicateObject);
            trimmedStyleOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_trimmed").toString());
            overridesArray.add(trimmedStyleOverrideObject);

			// style, upgrade, and trim
			JsonObject upgradedTrimmedStyleOverrideObject = new JsonObject();
			JsonObject upgradedTrimmedStylePredicateObject = new JsonObject();
			upgradedTrimmedStylePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
			upgradedTrimmedStylePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
			upgradedTrimmedStylePredicateObject.addProperty(Alloygery.asVanillaResource("trim_type").toString(), 0.1f);
			upgradedTrimmedStyleOverrideObject.add("predicate", upgradedTrimmedStylePredicateObject);
			upgradedTrimmedStyleOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_upgraded_trimmed").toString());
			overridesArray.add(upgradedTrimmedStyleOverrideObject);

            for(ImprovementTypes improvement : upgrades)
            {
				// style and improvement
                JsonObject improvementOverrideObject = new JsonObject();
                JsonObject improvementPredicateObject = new JsonObject();
                improvementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
                improvementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
                improvementOverrideObject.add("predicate", improvementPredicateObject);
                improvementOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName()).toString());
                overridesArray.add(improvementOverrideObject);

				// style, improvement, and trim
                JsonObject trimmedImprovementOverrideObject = new JsonObject();
                JsonObject trimmedImprovementPredicateObject = new JsonObject();
                trimmedImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
                trimmedImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
                trimmedImprovementPredicateObject.addProperty(Alloygery.asVanillaResource("trim_type").toString(), 0.1f);
                trimmedImprovementOverrideObject.add("predicate", trimmedImprovementPredicateObject);
                trimmedImprovementOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName() + "_trimmed").toString());
                overridesArray.add(trimmedImprovementOverrideObject);

				// style, upgrade, and improvement
				JsonObject upgradeImprovementOverrideObject = new JsonObject();
				JsonObject upgradeImprovementPredicateObject = new JsonObject();
				upgradeImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
				upgradeImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
				upgradeImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
				upgradeImprovementOverrideObject.add("predicate", upgradeImprovementPredicateObject);
				upgradeImprovementOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName() + "_upgraded").toString());
				overridesArray.add(upgradeImprovementOverrideObject);

				// style, upgrade, improvement, and trim
				JsonObject trimmedUpgradeImprovementOverrideObject = new JsonObject();
				JsonObject trimmedUpgradeImprovementPredicateObject = new JsonObject();
				trimmedUpgradeImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveArmorStyling.IDENTIFIER.toString(), style.getTypeFloat());
				trimmedUpgradeImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
				trimmedUpgradeImprovementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
				trimmedUpgradeImprovementPredicateObject.addProperty(Alloygery.asVanillaResource("trim_type").toString(), 0.1f);
				trimmedUpgradeImprovementOverrideObject.add("predicate", trimmedUpgradeImprovementPredicateObject);
				trimmedUpgradeImprovementOverrideObject.addProperty("model", modelLocation.withSuffix("_" + style.getName() + "_" + improvement.getName() + "_upgraded_trimmed").toString());
				overridesArray.add(trimmedUpgradeImprovementOverrideObject);
            }
        }

        base.add("overrides", overridesArray);
        return base;
    }

    public static void generateToolModel(ItemStack toolStack, ItemModelGenerators itemModelGenerator)
    {
        if (toolStack.getItem() instanceof IDynamicTool dynamicTool)
        {
            AlloygeryMaterial headMaterial = MaterialHelper.getMaterialForPartType(PartTypes.TOOL_HEAD, toolStack);
            ResourceLocation headTexture = Alloygery.asResource("template/render_part/tool_head/" + dynamicTool.getToolType().getName() + "_template_" + MaterialHelper.getSimpleName(headMaterial));
//            ResourceLocation brokenHeadTexture = Alloygery.asResource("template/render_part/tool_head/broken_" + dynamicTool.getToolType().getName() + "_template_" + MaterialHelper.getSimpleName(headMaterial));
            ResourceLocation handleTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_handle_template");
            ResourceLocation bindingTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_binding_template");
            ResourceLocation upgradeTexture = Alloygery.asResource("template/render_part/tool_upgrade/upgrade_" + dynamicTool.getToolType().getName());
            ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
            ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(toolStack.getItem());

            template.create(
                    modelLocation,
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture),
                    itemModelGenerator.output,
                    (resourceLocation, map) -> GearModelProvider.makeDynamicToolOverrides(resourceLocation, map, template)
            );

            template.create(
                    modelLocation.withSuffix("_with_binding"),
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture),
                    itemModelGenerator.output
            );

            template.create(
                    modelLocation.withSuffix("_upgraded"),
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, upgradeTexture),
                    itemModelGenerator.output
            );

			template.create(
					modelLocation.withSuffix("_with_binding_upgraded"),
					new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture).putForced(LAYER3, upgradeTexture),
					itemModelGenerator.output
			);

//            template.create(
//                    modelLocation.withSuffix("_broken"),
//                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, brokenHeadTexture),
//                    itemModelGenerator.output
//            );

            for(ImprovementTypes improvement : TOOL_IMPROVEMENT_TYPES)
            {
                ResourceLocation improvementTexture = Alloygery.asResource("template/render_part/tool_improvement/" + improvement.getName() + "_" + dynamicTool.getToolType().getName() + "_template");
                template.create(
                        modelLocation.withSuffix("_" + improvement.getName()),
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture).putForced(LAYER3, improvementTexture),
                        itemModelGenerator.output
                );

                template.create(
                        modelLocation.withSuffix("_" + improvement.getName() + "_upgraded"),
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture).putForced(LAYER3, upgradeTexture).putForced(LAYER4, improvementTexture),
                        itemModelGenerator.output
                );
            }
        }
    }

    public static void generateSwordLikeToolModel(ItemStack swordStack, ItemModelGenerators itemModelGenerator)
    {
        if (swordStack.getItem() instanceof IDynamicTool dynamicTool)
        {
            AlloygeryMaterial headMaterial = MaterialHelper.getMaterialForPartType(PartTypes.TOOL_HEAD, swordStack);
            ResourceLocation headTexture = Alloygery.asResource("template/render_part/tool_head/" + dynamicTool.getToolType().getName() + "_template_" + MaterialHelper.getSimpleName(headMaterial));
            ResourceLocation handleTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_handle_template");
            ResourceLocation bindingTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_guard_template");
            ResourceLocation upgradeTexture = Alloygery.asResource("template/render_part/tool_upgrade/upgrade_" + dynamicTool.getToolType().getName());
            ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
            ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(swordStack.getItem());

            template.create(
                    modelLocation,
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture),
                    itemModelGenerator.output,
                    (resourceLocation, map) -> GearModelProvider.makeDynamicToolOverridesNoBinding(resourceLocation, map, template)
            );

            template.create(
                    modelLocation.withSuffix("_upgraded"),
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture).putForced(LAYER3, upgradeTexture),
                    itemModelGenerator.output
            );

            for(ImprovementTypes improvement : TOOL_IMPROVEMENT_TYPES)
            {
                ResourceLocation improvementTexture = Alloygery.asResource("template/render_part/tool_improvement/" + improvement.getName() + "_" + dynamicTool.getToolType().getName() + "_template");
                template.create(
                        modelLocation.withSuffix("_" + improvement.getName()),
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture).putForced(LAYER3, improvementTexture),
                        itemModelGenerator.output
                );

                template.create(
                        modelLocation.withSuffix("_" + improvement.getName() + "_upgraded"),
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture).putForced(LAYER3, upgradeTexture).putForced(LAYER4, improvementTexture),
                        itemModelGenerator.output
                );
            }
        }
    }
    
    public static void generateBasicToolModel(ItemStack basicToolStack, ItemModelGenerators itemModelGenerator)
    {
        if (basicToolStack.getItem() instanceof IDynamicTool dynamicTool)
        {
            AlloygeryMaterial headMaterial = MaterialHelper.getMaterialForPartType(PartTypes.TOOL_HEAD, basicToolStack);
            ResourceLocation headTexture = Alloygery.asResource("template/render_part/tool_head/" + dynamicTool.getToolType().getName() + "_template_" + MaterialHelper.getSimpleName(headMaterial));
            ResourceLocation handleTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_handle_template");
            ResourceLocation upgradeTexture = Alloygery.asResource("template/render_part/tool_upgrade/upgrade_" + dynamicTool.getToolType().getName());
            ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
            ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(basicToolStack.getItem());

            template.create(
                    modelLocation,
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture),
                    itemModelGenerator.output,
                    (resourceLocation, map) -> GearModelProvider.makeDynamicToolOverridesNoBinding(resourceLocation, map, template)
            );

            template.create(
                    modelLocation.withSuffix("_upgraded"),
                    new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, upgradeTexture),
                    itemModelGenerator.output
            );

            for(ImprovementTypes improvement : TOOL_IMPROVEMENT_TYPES)
            {
                ResourceLocation improvementTexture = Alloygery.asResource("template/render_part/tool_improvement/" + improvement.getName() + "_" + dynamicTool.getToolType().getName() + "_template");
                template.create(
                        modelLocation.withSuffix("_" + improvement.getName()),
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, improvementTexture),
                        itemModelGenerator.output
                );

                template.create(
                        modelLocation.withSuffix("_" + improvement.getName() + "_upgraded"),
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, upgradeTexture).putForced(LAYER3, improvementTexture),
                        itemModelGenerator.output
                );
            }
        }
    }

    private static JsonObject makeDynamicToolOverrides(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> textureMap, ModelTemplate baseTemplate)
    {
        JsonObject base = baseTemplate.createBaseTemplate(modelLocation, textureMap);
        JsonArray overridesArray = new JsonArray();

        // binding
        JsonObject bindingObject = new JsonObject();
        JsonObject bindingPredicate = new JsonObject();
        bindingPredicate.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveToolBinding.IDENTIFIER.toString(), 1.0f);
        bindingObject.add("predicate", bindingPredicate);
        bindingObject.addProperty("model", modelLocation.withSuffix("_with_binding").toString());
        overridesArray.add(bindingObject);

        // upgrade
        JsonObject netheriteObject = new JsonObject();
        JsonObject netheritePredicateObject = new JsonObject();
        netheritePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
        netheriteObject.add("predicate", netheritePredicateObject);
        netheriteObject.addProperty("model", modelLocation.withSuffix("_upgraded").toString());
        overridesArray.add(netheriteObject);

		// upgrade and binding
        JsonObject bindingNetheriteObject = new JsonObject();
        JsonObject bindingNetheritePredicateObject = new JsonObject();
        bindingNetheritePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveToolBinding.IDENTIFIER.toString(), 1.0f);
        bindingNetheritePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
        bindingNetheriteObject.add("predicate", bindingNetheritePredicateObject);
        bindingNetheriteObject.addProperty("model", modelLocation.withSuffix("_with_binding_upgraded").toString());
        overridesArray.add(bindingNetheriteObject);

        // upgrades
        for(ImprovementTypes improvement : GearModelProvider.TOOL_IMPROVEMENT_TYPES)
        {
			// improvement
            JsonObject improvementOverrideObject = new JsonObject();
            JsonObject improvementPredicateObject = new JsonObject();
            improvementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveToolBinding.IDENTIFIER.toString(), 1);
            improvementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
            improvementOverrideObject.add("predicate", improvementPredicateObject);
            improvementOverrideObject.addProperty("model", modelLocation.withSuffix("_" + improvement.getName()).toString());
            overridesArray.add(improvementOverrideObject);

			// improvement and upgrade
            JsonObject improvementUpgradedObject = new JsonObject();
            JsonObject improvementUpgradedPredicateObject = new JsonObject();
            improvementUpgradedPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanHaveToolBinding.IDENTIFIER.toString(), 1.0f);
            improvementUpgradedPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
            improvementUpgradedPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
            improvementUpgradedObject.add("predicate", improvementUpgradedPredicateObject);
            improvementUpgradedObject.addProperty("model", modelLocation.withSuffix("_" + improvement.getName() + "_upgraded").toString());
            overridesArray.add(improvementUpgradedObject);
        }

        base.add("overrides", overridesArray);
        return base;
    }

    private static JsonObject makeDynamicToolOverridesNoBinding(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> textureMap, ModelTemplate baseTemplate)
    {
        JsonObject base = baseTemplate.createBaseTemplate(modelLocation, textureMap);
        JsonArray overridesArray = new JsonArray();

        JsonObject upgradeObject = new JsonObject();
        JsonObject upgradePredicateObject = new JsonObject();
        upgradePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
        upgradeObject.add("predicate", upgradePredicateObject);
        upgradeObject.addProperty("model", modelLocation.withSuffix("_upgraded").toString());
        overridesArray.add(upgradeObject);

        // upgrades
        for(ImprovementTypes improvement : GearModelProvider.TOOL_IMPROVEMENT_TYPES)
        {
            JsonObject improvementOverrideObject = new JsonObject();
            JsonObject improvementPredicateObject = new JsonObject();
            improvementPredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
            improvementOverrideObject.add("predicate", improvementPredicateObject);
            improvementOverrideObject.addProperty("model", modelLocation.withSuffix("_" + improvement.getName()).toString());
            overridesArray.add(improvementOverrideObject);

            JsonObject improvementUpgradeObject = new JsonObject();
            JsonObject improvementUpgradePredicateObject = new JsonObject();
            improvementUpgradePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeImproved.IDENTIFIER.toString(), improvement.getTypeFloat());
            improvementUpgradePredicateObject.addProperty(DynamicGearItemModelPredicateProvider.ICanBeUpgraded.IDENTIFIER.toString(), 1.0f);
            improvementUpgradeObject.add("predicate", improvementUpgradePredicateObject);
            improvementUpgradeObject.addProperty("model", modelLocation.withSuffix("_" + improvement.getName() + "_upgraded").toString());
            overridesArray.add(improvementUpgradeObject);
        }

        base.add("overrides", overridesArray);
        return base;
    }
}
