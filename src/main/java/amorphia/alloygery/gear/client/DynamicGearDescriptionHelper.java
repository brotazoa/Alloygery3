package amorphia.alloygery.gear.client;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.attribute.SlotMultipliers;
import amorphia.alloygery.gear.dynamicProviders.IOccludeVibrations;
import amorphia.alloygery.gear.item.*;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DynamicGearDescriptionHelper
{
    private static final Map<PropertyType<?>, TooltipPropertyWriter> TOOLTIP_PROPERTY_WRITER_MAP = Maps.newHashMap();
	private static final Map<PropertyType<?>, TooltipPartPropertyWriter> TOOLTIP_PART_PROPERTY_WRITER_MAP = Maps.newHashMap();
    private static final TooltipPropertyWriter DEFAULT_PROPERTY_WRITER = ((gearStack, tooltip, isAdvanced) -> {
        // do nothing
    });

    public static void register(PropertyType<?> propertyType, TooltipPropertyWriter writer)
    {
        TOOLTIP_PROPERTY_WRITER_MAP.put(propertyType, writer);
    }

	public static void register(PropertyType<?> propertyType, TooltipPartPropertyWriter writer)
	{
		TOOLTIP_PART_PROPERTY_WRITER_MAP.put(propertyType, writer);
	}

    public static void writeDescription(ItemStack gearStack, List<Component> tooltip, TooltipFlag isAdvanced)
    {
        if(gearStack == null || gearStack.isEmpty())
            return;

        if(!NBTHelper.hasAlloygeryTag(gearStack))
        {
            gearStack = gearStack.copy();
            gearStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(gearStack.getItem().getDefaultInstance()));
        }

        writeShiftPrompt(tooltip);
        writePartsList(gearStack, tooltip, isAdvanced);
        writeProperties(gearStack, tooltip, isAdvanced);

        if(PropertyHelper.isBroken(gearStack))
            writeBrokenStatus(tooltip);
    }

    public static void writePartDescription(ItemStack partStack, List<Component> tooltip, TooltipFlag isAdvanced)
    {
        if(partStack == null || partStack.isEmpty())
            return;

        if (partStack.getItem() instanceof IDynamicGearPart gearPart)
        {
            if (Screen.hasShiftDown())
            {
                List<Property> properties = gearPart.getMaterialProperties().stream().sorted(Comparator.comparing(Property::getOperation)).toList();
				for(Property property : properties)
				{
					TOOLTIP_PART_PROPERTY_WRITER_MAP.getOrDefault(property.getPropertyType(), DynamicGearDescriptionHelper::default_part_property_writer).append(partStack, tooltip, property, isAdvanced);
				}
            }
            else
            {
                writeShiftPrompt(tooltip);
            }
        }
    }

    private static void writeProperties(ItemStack gearStack, List<Component> tooltip, TooltipFlag isAdvanced)
    {
        List<PropertyType<?>> propertiesOnStack = Lists.newArrayList();
        for(Property property : PropertyHelper.getPropertiesFromItemStack(gearStack))
        {
            if(!propertiesOnStack.contains(property.getPropertyType()))
                propertiesOnStack.add(property.getPropertyType());
        }
        if(propertiesOnStack.isEmpty())
            return;

        propertiesOnStack = propertiesOnStack.stream().sorted(Comparator.comparing(PropertyType.REGISTRY::getKey)).toList();
        propertiesOnStack.forEach(propertyType -> TOOLTIP_PROPERTY_WRITER_MAP.getOrDefault(propertyType, DEFAULT_PROPERTY_WRITER).append(gearStack, tooltip, isAdvanced));
    }

    private static void writePartsList(ItemStack gearStack, List<Component> tooltip, TooltipFlag isAdvanced)
    {
        if(gearStack == null || gearStack.isEmpty())
            return;

        if(!NBTHelper.hasAlloygeryTag(gearStack) || !isAdvanced.isAdvanced())
        {
            gearStack = gearStack.copy();
            gearStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(gearStack.getItem().getDefaultInstance()));
        }

        List<PartTypes> parts = NBTHelper.getPartTypesListFromItemStack(gearStack);
        if(parts.isEmpty())
            return;

        if (Screen.hasShiftDown() && gearStack.getItem() instanceof IDynamicTool)
        {
            parts.add(PartTypes.TOOL_TYPE);
        }

        parts = parts.stream().sorted().toList();

        tooltip.add(Component.translatable("tooltip.alloygery.parts_list").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        for(PartTypes part : parts)
        {
            if (part == PartTypes.TOOL_TYPE && gearStack.getItem() instanceof IDynamicTool dynamicTool)
            {
                tooltip.add(CommonComponents.space().append(Component.translatable("tool_type.alloygery." + dynamicTool.getToolType().getName() + ".tooltip")).withStyle(ChatFormatting.WHITE));
            }
			else if (PartTypes.isImprovement(part))
			{
				ImprovementTypes improvement = NBTHelper.getImprovementType(gearStack);
				tooltip.add(CommonComponents.space().append(Component.translatable(Util.makeDescriptionId("improvement", Alloygery.asResource(improvement.getName())))).withStyle(ChatFormatting.WHITE));
			}
			else if (PartTypes.isUpgrade(part))
			{
				UpgradeTypes upgrade = NBTHelper.getUpgradeType(gearStack);
				tooltip.add(CommonComponents.space().append(Component.translatable(Util.makeDescriptionId("upgrade", Alloygery.asResource(upgrade.getName())))).withStyle(ChatFormatting.WHITE));
			}
			else
			{
				ResourceLocation partLocation = NBTHelper.getItemIdentifierFromTag(NBTHelper.getPartTagFromItemStack(gearStack, part));
				Item partItem = BuiltInRegistries.ITEM.get(partLocation);
				tooltip.add(CommonComponents.space().append(Component.translatable(
						partItem == Items.AIR
								? "part_type.alloygery." + part.getName() + ".tooltip"
								: partLocation.toLanguageKey("item")
				)).withStyle(ChatFormatting.WHITE));
			}

            if (Screen.hasShiftDown())
            {
				if (PartTypes.isImprovement(part))
				{
					AlloygeryMaterial improvementMaterial = MaterialHelper.getMaterialForPartType(part, gearStack);
					tooltip.add(Component.literal("  ").append(Component.translatable("tooltip.alloygery.improvement")).append(": ").append(Component.translatable(improvementMaterial.getMaterialIdentifier().toLanguageKey())));
				}
				if (PartTypes.isUpgrade(part))
				{
					AlloygeryMaterial upgradeMaterial = MaterialHelper.getMaterialForPartType(part, gearStack);
					tooltip.add(Component.literal("  ").append(Component.translatable("tooltip.alloygery.upgrade").append(": ").append(Component.translatable(upgradeMaterial.getMaterialIdentifier().toLanguageKey()))));
				}
				if (NBTHelper.partHasArmorStyle(gearStack, part))
				{
					ArmorStyles style = NBTHelper.getArmorStyleFromStack(gearStack, part);
					tooltip.add(Component.literal("  ")
							.append(Component.translatable("tooltip.alloygery.armor_style").append(": ").append(Component.translatable("armor_style.alloygery." + style.getName() + ".tooltip"))));
				}
                List<Property> properties = PropertyHelper.getPropertiesByPartTypeFromItemStack(part, gearStack).stream().sorted(Comparator.comparing(Property::getOperation)).toList();
				for(Property property : properties)
				{
					TOOLTIP_PART_PROPERTY_WRITER_MAP.getOrDefault(property.getPropertyType(), DynamicGearDescriptionHelper::default_part_property_writer).append(gearStack, tooltip, property, isAdvanced);
				}
                if(NBTHelper.partHasDyeColor(gearStack, part))
                    tooltip.add(Component.literal("  ").append(Component.translatable("tooltip.alloygery.dye_color").append(Component.literal(": " + NBTHelper.getPartDyeColorFromStack(gearStack, part)))));
            }
        }
    }

    private static void writeShiftPrompt(List<Component> tooltip)
    {
        if(!Screen.hasShiftDown())
            tooltip.add(Component.translatable("tooltip.alloygery.shift_prompt").withStyle(ChatFormatting.DARK_GRAY));
    }

    private static void writeBrokenStatus(List<Component> tooltip)
    {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("property.alloygery.broken.tooltip").withStyle(ChatFormatting.DARK_RED));
        if(Screen.hasShiftDown())
            tooltip.add(CommonComponents.space().append(Component.translatable("property.alloygery.broken.description").withStyle(ChatFormatting.GRAY)));
    }

	private static boolean invertValueColors(Property property)
	{
		return property.getPropertyType() == PropertyType.ENCUMBRANCE;
	}

	private static void default_part_property_writer(ItemStack gearStack, List<Component> tooltip, Property property, TooltipFlag isAdvanced)
	{
		ResourceLocation propertyTypeLocation = PropertyType.REGISTRY.getKey(property.getPropertyType());
		if(propertyTypeLocation == null)
			return;

		if(PropertyOperation.isMultiplication(property.getOperation()) && property.getValue() == 1.0f)
			return;

		if(PropertyOperation.isAddition(property.getOperation()) && property.getValue() == 0.0f)
			return;

		MutableComponent propertyComponent = property.getOperation() == PropertyOperation.BASE || property.getOperation() == PropertyOperation.MULTIPLY_BASE
				? Component.translatable(propertyTypeLocation.toLanguageKey("property", "base"))
				: property.getOperation() == PropertyOperation.MULTIPLY_PART
						? Component.translatable(propertyTypeLocation.toLanguageKey("property", "part"))
						: Component.translatable(propertyTypeLocation.toLanguageKey("property", "total"));

		MutableComponent tooltipComponent = Component.literal("  ").append(propertyComponent).append(PropertyOperation.isMultiplication(property.getOperation()) ? " x " : " + ").withStyle(ChatFormatting.GRAY);

		final float value = property.getValue();
		ChatFormatting valueStyle = value < (PropertyOperation.isMultiplication(property.getOperation()) ? 1.0f : 0.0f) ? invertValueColors(property) ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED : invertValueColors(property) ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
		Component valueComponent = Component.literal(String.format("%.2f", value)).withStyle(valueStyle);

		tooltip.add(tooltipComponent.append(valueComponent));
	}

	private static void default_part_property_writer_with_slot_multiplier(ItemStack gearStack, List<Component> tooltip, Property property, TooltipFlag isAdvanced)
	{
		ResourceLocation propertyTypeLocation = PropertyType.REGISTRY.getKey(property.getPropertyType());
		if(propertyTypeLocation == null)
			return;

		if(PropertyOperation.isMultiplication(property.getOperation()) && property.getValue() == 1.0f)
			return;

		if(PropertyOperation.isAddition(property.getOperation()) && property.getValue() == 0.0f)
			return;

		MutableComponent propertyComponent = property.getOperation() == PropertyOperation.BASE || property.getOperation() == PropertyOperation.MULTIPLY_BASE
				? Component.translatable(propertyTypeLocation.toLanguageKey("property", "base"))
				: property.getOperation() == PropertyOperation.MULTIPLY_PART
				? Component.translatable(propertyTypeLocation.toLanguageKey("property", "part"))
						: Component.translatable(propertyTypeLocation.toLanguageKey("property", "total"));

		MutableComponent tooltipComponent = Component.literal("  ").append(propertyComponent).append(PropertyOperation.isMultiplication(property.getOperation()) ? " x " : " + ").withStyle(ChatFormatting.GRAY);

		final float value = PropertyOperation.isAddition(property.getOperation()) ? property.getValue() * SlotMultipliers.getSlotMultiplier(gearStack) : property.getValue();
		ChatFormatting valueStyle = value < (PropertyOperation.isMultiplication(property.getOperation()) ? 1.0f : 0.0f) ? invertValueColors(property) ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED : invertValueColors(property) ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
		Component valueComponent = Component.literal(String.format("%.2f", value)).withStyle(valueStyle);

		tooltip.add(tooltipComponent.append(valueComponent));
	}

    public interface TooltipPropertyWriter
    {
        void append(ItemStack gearStack, List<Component> tooltip, TooltipFlag isAdvanced);
    }

	public interface TooltipPartPropertyWriter
	{
		void append(ItemStack gearStack, List<Component> tooltip, Property property, TooltipFlag isAdvanced);
	}

    static
    {
		// property writers
        register(PropertyType.ATTRIBUTE, (gearStack, tooltip, isAdvanced) -> {
            if (Screen.hasShiftDown())
            {
                List<Attribute> attributesOnStack = Lists.newArrayList();
                for(AttributeProperty property : PropertyHelper.getPropertiesOfTypeFromItemStack(AttributeProperty.class, gearStack))
                {
                    if(!attributesOnStack.contains(property.getTargetAttribute()))
                        attributesOnStack.add(property.getTargetAttribute());
                }

                if(attributesOnStack.isEmpty())
                    return;

                attributesOnStack = attributesOnStack.stream().sorted(Comparator.comparing(BuiltInRegistries.ATTRIBUTE::getKey)).toList();
                for(Attribute attribute : attributesOnStack)
                {
					final float value = AttributeProperty.compute(attribute, gearStack);
					if(value == 0.0f)
						continue;

					tooltip.add(Component.translatable(attribute.getDescriptionId()).append(": " + String.format("%.2f", value)).withStyle(ChatFormatting.GRAY));
                }
            }
        });

        register(PropertyType.DURABILITY, (gearStack, tooltip, isAdvanced) -> {
            if (Screen.hasShiftDown())
            {
                final int durability = DurabilityProperty.compute(gearStack);
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.DURABILITY);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + durability).withStyle(ChatFormatting.GRAY));
            }
        });

        register(PropertyType.ENCHANTABILITY, (gearStack, tooltip, isAdvanced) -> {
            if (Screen.hasShiftDown())
            {
                final int enchantability = EnchantabilityProperty.compute(gearStack);
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.ENCHANTABILITY);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + enchantability).withStyle(ChatFormatting.GRAY));
            }
        });

		register(PropertyType.ENCUMBRANCE, (gearStack, tooltip, isAdvanced) -> {
			if (Screen.hasShiftDown())
			{
				final float encumbrance = EncumbranceProperty.compute(gearStack);
				ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.ENCUMBRANCE);
				if(location == null)
					return;

				tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + String.format("%.2f", encumbrance)).withStyle(ChatFormatting.GRAY));
			}
		});

        register(PropertyType.MINING_LEVEL, (gearStack, tooltip, isAdvanced) -> {
            if (Screen.hasShiftDown())
            {
                final int level = MiningLevelProperty.compute(gearStack);
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.MINING_LEVEL);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + level).withStyle(ChatFormatting.GRAY));
            }
        });

        register(PropertyType.MINING_SPEED, (gearStack, tooltip, isAdvanced) -> {
            if (Screen.hasShiftDown())
            {
                final float speed = MiningSpeedProperty.compute(gearStack);
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.MINING_SPEED);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + String.format("%.2f", speed)).withStyle(ChatFormatting.GRAY));
            }
        });

		register(PropertyType.MOBILITY, (gearStack, tooltip, isAdvanced) -> {
			if (Screen.hasShiftDown())
			{
				final float mobility = MobilityProperty.compute(gearStack);
				ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.MOBILITY);
				if(location == null)
					return;

				tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + String.format("%.2f", mobility)).withStyle(ChatFormatting.GRAY));
			}
		});

        register(PropertyType.HONING, (gearStack, tooltip, isAdvanced) -> {
            if (Screen.hasShiftDown())
            {
                final float honing = HoningProperty.compute(gearStack);
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.HONING);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + String.format("%.2f", honing)).withStyle(ChatFormatting.GRAY));
            }
        });

        register(PropertyType.FIREPROOF, (gearStack, tooltip, isAdvanced) -> {
            if (FireproofProperty.compute(gearStack))
            {
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.FIREPROOF);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).withStyle(ChatFormatting.DARK_RED));
                if (Screen.hasShiftDown())
                {
                    tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "description")).withStyle(ChatFormatting.GRAY)));
                }
            }
        });

        register(PropertyType.FIRE_PROTECTION, (gearStack, tooltip, isAdvanced) -> {
            final int protection = FireProtectionProperty.compute(gearStack);
            if (protection != 0)
            {
                final double protection_in_seconds = protection / 20.0;
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.FIRE_PROTECTION);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + String.format("%.2f", protection_in_seconds) + "s").withStyle(ChatFormatting.DARK_RED));
                if (Screen.hasShiftDown())
                {
                    tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "description")).withStyle(ChatFormatting.GRAY)));
                }
            }
        });

        register(PropertyType.FREEZE_PROTECTION, (gearStack, tooltip, isAdvanced) -> {
            final int protection = FreezeProtectionProperty.compute(gearStack);
            if (protection != 0)
            {
                final double protection_in_seconds = protection / 20.0;
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.FREEZE_PROTECTION);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).append(": " + String.format("%.2f", protection_in_seconds) + "s").withStyle(ChatFormatting.AQUA));
                if (Screen.hasShiftDown())
                {
                    tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "description")).withStyle(ChatFormatting.GRAY)));
                }
            }
        });

        register(PropertyType.LOVED_BY_PIGLINS, (gearStack, tooltip, isAdvanced) -> {
            if (LovedByPiglinsProperty.compute(gearStack))
            {
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.LOVED_BY_PIGLINS);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).withStyle(ChatFormatting.GOLD));
                if (Screen.hasShiftDown())
                {
                    tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "description")).withStyle(ChatFormatting.GRAY)));
                }
            }
        });

        register(PropertyType.PRIMITIVE, ((gearStack, tooltip, isAdvanced) -> {
            if (PrimitiveProperty.compute(gearStack))
            {
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.PRIMITIVE);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).withStyle(ChatFormatting.RED));

                if (Screen.hasShiftDown())
                {
                    tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "description")).withStyle(ChatFormatting.GRAY)));
                }
            }
        }));

        register(PropertyType.WALK_ON_POWDERED_SNOW, (gearStack, tooltip, isAdvanced) -> {
            if (WalkOnPowderedSnowProperty.compute(gearStack))
            {
                ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.WALK_ON_POWDERED_SNOW);
                if(location == null)
                    return;

                tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).withStyle(ChatFormatting.WHITE));

                if (Screen.hasShiftDown())
                {
                    tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "description")).withStyle(ChatFormatting.GRAY)));
                }
            }
        });

		register(PropertyType.OCCLUDE_VIBRATIONS, (gearStack, tooltip, isAdvanced) -> {
			if (OccludeVibrationsProperty.compute(gearStack))
			{
				ResourceLocation location = PropertyType.REGISTRY.getKey(PropertyType.OCCLUDE_VIBRATIONS);
				if(location == null)
					return;

				if (gearStack.getItem() instanceof ArmorItem armorItem)
				{
					if (armorItem.getType() == ArmorItem.Type.BOOTS)
					{
						tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).withStyle(ChatFormatting.WHITE));
						if (Screen.hasShiftDown())
						{
							tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "armor_description")).withStyle(ChatFormatting.GRAY)));
						}
					}
				}
				else
				{
					tooltip.add(Component.translatable(location.toLanguageKey("property", "tooltip")).withStyle(ChatFormatting.WHITE));
					if (Screen.hasShiftDown())
					{
						tooltip.add(CommonComponents.space().append(Component.translatable(location.toLanguageKey("property", "tool_description")).withStyle(ChatFormatting.GRAY)));
					}
				}
			}
		});

		// part property writers
		register(PropertyType.ATTRIBUTE, (gearStack, tooltip, property, isAdvanced) -> {
			if(PropertyOperation.isMultiplication(property.getOperation()) && property.getValue() == 1.0f)
				return;

			if(PropertyOperation.isAddition(property.getOperation()) && property.getValue() == 0.0f)
				return;

			MutableComponent propertyComponent = property.getOperation() == PropertyOperation.BASE || property.getOperation() == PropertyOperation.MULTIPLY_BASE
					? Component.translatable("property.alloygery.generic_attribute.base").append(Component.translatable(((AttributeProperty) property).getTargetAttribute().getDescriptionId()))
					: property.getOperation() == PropertyOperation.MULTIPLY_PART
					? Component.translatable(((AttributeProperty) property).getTargetAttribute().getDescriptionId()).append(Component.translatable("property.alloygery.generic_attribute.part"))
							: Component.translatable("property.alloygery.generic_attribute.total").append(Component.translatable(((AttributeProperty) property).getTargetAttribute().getDescriptionId()));

			MutableComponent tooltipComponent = Component.literal("  ").append(propertyComponent).append(PropertyOperation.isMultiplication(property.getOperation()) ? " x ": " + ").withStyle(ChatFormatting.GRAY);

			final float value = PropertyOperation.isAddition(property.getOperation()) ? property.getValue() * SlotMultipliers.getSlotMultiplier(gearStack) : property.getValue();
			ChatFormatting valueStyle = value < (PropertyOperation.isMultiplication(property.getOperation()) ? 1.0f : 0.0f) ? invertValueColors(property) ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED : invertValueColors(property) ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
			Component valueComponent = Component.literal(String.format("%.2f", value)).withStyle(valueStyle);

			tooltip.add(tooltipComponent.append(valueComponent));
		});

		register(PropertyType.DURABILITY, (gearStack, tooltip, property, isAdvanced) -> {
			ResourceLocation propertyTypeLocation = PropertyType.REGISTRY.getKey(property.getPropertyType());
			if(propertyTypeLocation == null)
				return;

			if(PropertyOperation.isMultiplication(property.getOperation()) && property.getValue() == 1.0f)
				return;

			if(PropertyOperation.isAddition(property.getOperation()) && property.getValue() == 0.0f)
				return;

			MutableComponent propertyComponent = property.getOperation() == PropertyOperation.BASE || property.getOperation() == PropertyOperation.MULTIPLY_BASE
					? Component.translatable(propertyTypeLocation.toLanguageKey("property", "base"))
					: property.getOperation() == PropertyOperation.MULTIPLY_PART
							? Component.translatable(propertyTypeLocation.toLanguageKey("property", "part"))
							: Component.translatable(propertyTypeLocation.toLanguageKey("property", "total"));

			MutableComponent tooltipComponent = Component.literal("  ").append(propertyComponent).append(PropertyOperation.isMultiplication(property.getOperation()) ? " x " : " + ").withStyle(ChatFormatting.GRAY);

			final float value = PropertyOperation.isAddition(property.getOperation()) ? property.getValue() * SlotMultipliers.getDurabilitySlotMultiplier(gearStack) : property.getValue();
			ChatFormatting valueStyle = value < (PropertyOperation.isMultiplication(property.getOperation()) ? 1.0f : 0.0f) ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
			Component valueComponent = Component.literal(String.format("%.2f", value)).withStyle(valueStyle);

			tooltip.add(tooltipComponent.append(valueComponent));
		});

		register(PropertyType.ENCUMBRANCE, DynamicGearDescriptionHelper::default_part_property_writer_with_slot_multiplier);
		register(PropertyType.MOBILITY, DynamicGearDescriptionHelper::default_part_property_writer_with_slot_multiplier);
    }
}
