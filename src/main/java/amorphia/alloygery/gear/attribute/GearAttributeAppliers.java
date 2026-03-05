package amorphia.alloygery.gear.attribute;

import amorphia.alloygery.accessor.ArmorUUIDAccessor;
import amorphia.alloygery.accessor.ItemUUIDAccessor;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.EncumbranceProperty;
import amorphia.alloygery.gear.property.MobilityProperty;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GearAttributeAppliers
{
    private static final UUID NON_SLOT_DEPENDANT_EQUIPMENT_MODIFIER = UUID.fromString("4022dfe0-142b-46a9-ac9c-fc8074c31c81");

    private static final Map<Attribute, AttributeApplier> ATTRIBUTE_APPLIER_MAP = Maps.newHashMap();

    public static void applyAttribute(Attribute attribute, ItemStack gearStack, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder)
    {
        Optional.ofNullable(ATTRIBUTE_APPLIER_MAP.get(attribute)).ifPresentOrElse(applier -> applier.apply(gearStack, builder),
                () -> apply_addition_attribute(attribute, getUUIDByArmorType(gearStack), gearStack, builder));
    }

    public static void register(Attribute attribute, AttributeApplier applier)
    {
        if(attribute == null || applier == null)
            return;

        ATTRIBUTE_APPLIER_MAP.put(attribute, applier);
    }

    public interface AttributeApplier
    {
        void apply(ItemStack gearStack, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder);
    }

    private static UUID getUUIDByArmorType(ItemStack gearStack)
    {
        if (gearStack.getItem() instanceof ArmorItem armorItem)
        {
            return ArmorUUIDAccessor.getArmorModifierUUIDPerType().get(armorItem.getType());
        }
        else return NON_SLOT_DEPENDANT_EQUIPMENT_MODIFIER;
    }

    private static void apply_addition_attribute(Attribute attribute, UUID modifierID, ItemStack gearStack, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder)
    {
        builder.put(attribute, new AttributeModifier(modifierID, "Gear Modifier", AttributeProperty.compute(attribute, gearStack), AttributeModifier.Operation.ADDITION));
    }

    static
    {
        // vanilla
        register(Attributes.ATTACK_DAMAGE, (gearStack, builder) -> {
            final float damage = AttributeProperty.compute(Attributes.ATTACK_DAMAGE, gearStack);

            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ItemUUIDAccessor.getAttackDamageModifierUUID(), "Gear Modifier", damage, AttributeModifier.Operation.ADDITION));
        });

        register(Attributes.ATTACK_SPEED, (gearStack, builder) -> {
            final float speed = AttributeProperty.compute(Attributes.ATTACK_SPEED, gearStack);
            final float speed_clamped = Math.max(-3.99f, speed);

            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ItemUUIDAccessor.getAttackSpeedModifierUUID(), "Gear Modifier", speed_clamped, AttributeModifier.Operation.ADDITION));
        });

        register(Attributes.ARMOR, (gearStack, builder) -> {
            final float armor = AttributeProperty.compute(Attributes.ARMOR, gearStack);
            final float armor_by_slot = armor * SlotMultipliers.getSlotMultiplier(gearStack);
            final int armor_rounded_up = Math.max(0, (int) Math.ceil(armor_by_slot));

            builder.put(Attributes.ARMOR, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", armor_rounded_up, AttributeModifier.Operation.ADDITION));
        });

        register(Attributes.KNOCKBACK_RESISTANCE, (gearStack, builder) -> {
            final float knockback = AttributeProperty.compute(Attributes.KNOCKBACK_RESISTANCE, gearStack);
            final float knockback_by_slot = knockback * SlotMultipliers.getSlotMultiplier(gearStack);
            final float knockback_as_percent = knockback_by_slot * 0.01f;
            final float knockback_clamped = Math.max(0.0f, Math.min(1.0f, knockback_as_percent));

            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", knockback_clamped, AttributeModifier.Operation.ADDITION));
        });

        register(Attributes.MOVEMENT_SPEED, (gearStack, builder) -> {
			final float mobility = MobilityProperty.compute(gearStack);
			final float encumbrance = EncumbranceProperty.compute(gearStack);
			final float movement_from_properties = mobility - encumbrance;

            final float movement_from_attribute = AttributeProperty.compute(Attributes.MOVEMENT_SPEED, gearStack);
			final float movement = movement_from_properties + movement_from_attribute;
            final float movement_by_slot = movement * SlotMultipliers.getSlotMultiplier(gearStack);
            final float movement_as_percent = movement_by_slot * 0.01f;

            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", movement_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
        });

        // additional entity attributes
		register(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, (gearStack, builder) -> {
			final float damage = AttributeProperty.compute(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, gearStack);
			final float damage_as_percent = damage * 0.01f;

			builder.put(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", damage_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
		});

		register(AdditionalEntityAttributes.JUMP_HEIGHT, (gearStack, builder) -> {
			final float jump = AttributeProperty.compute(AdditionalEntityAttributes.JUMP_HEIGHT, gearStack);
			final float jump_by_slot = jump * SlotMultipliers.getSlotMultiplier(gearStack);
			final float jump_as_percent = jump_by_slot * 0.01f;

			builder.put(AdditionalEntityAttributes.JUMP_HEIGHT, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", jump_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
		});

        register(AdditionalEntityAttributes.MAGIC_PROTECTION, (gearStack, builder) -> {
            final float protection = AttributeProperty.compute(AdditionalEntityAttributes.MAGIC_PROTECTION, gearStack);
            final float protection_by_slot = protection * SlotMultipliers.getSlotMultiplier(gearStack);
            final int protection_rounded = Math.max(0, (int) Math.ceil(protection_by_slot));

            builder.put(AdditionalEntityAttributes.MAGIC_PROTECTION, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", protection_rounded, AttributeModifier.Operation.ADDITION));
        });

        register(AdditionalEntityAttributes.WATER_SPEED, (gearStack, builder) -> {
            final float speed = AttributeProperty.compute(AdditionalEntityAttributes.WATER_SPEED, gearStack);
            final float speed_by_slot = speed * SlotMultipliers.getSlotMultiplier(gearStack);
            final float speed_as_percent = speed_by_slot * 0.01f;

            builder.put(AdditionalEntityAttributes.WATER_SPEED, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", speed_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
        });

        register(AdditionalEntityAttributes.LAVA_SPEED, (gearStack, builder) -> {
            final float speed = AttributeProperty.compute(AdditionalEntityAttributes.LAVA_SPEED, gearStack);
            final float speed_by_slot = speed * SlotMultipliers.getSlotMultiplier(gearStack);
            final float speed_as_percent = speed_by_slot * 0.01f;

            builder.put(AdditionalEntityAttributes.LAVA_SPEED, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", speed_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
        });

		register(AdditionalEntityAttributes.MOB_DETECTION_RANGE, (gearStack, builder) -> {
			final float range = AttributeProperty.compute(AdditionalEntityAttributes.MOB_DETECTION_RANGE, gearStack);
			final float range_by_slot = range * SlotMultipliers.getSlotMultiplier(gearStack);
			final float range_as_percent = range_by_slot * 0.01f;

			builder.put(AdditionalEntityAttributes.MOB_DETECTION_RANGE, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", range_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
		});

        // porting lib
        register(PortingLibAttributes.SWIM_SPEED, (gearStack, builder) -> {
            final float speed = AttributeProperty.compute(PortingLibAttributes.SWIM_SPEED, gearStack);
            final float speed_by_slot = speed * SlotMultipliers.getSlotMultiplier(gearStack);
            final float speed_as_percent = speed_by_slot * 0.01f;

            builder.put(PortingLibAttributes.SWIM_SPEED, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", speed_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
        });

        register(PortingLibAttributes.ENTITY_GRAVITY, (gearStack, builder) -> {
            final float gravity = AttributeProperty.compute(PortingLibAttributes.ENTITY_GRAVITY, gearStack);
            final float gravity_by_slot = gravity * SlotMultipliers.getSlotMultiplier(gearStack);
            final float gravity_as_percent = gravity_by_slot * 0.01f;

            builder.put(PortingLibAttributes.ENTITY_GRAVITY, new AttributeModifier(getUUIDByArmorType(gearStack), "Gear Modifier", gravity_as_percent, AttributeModifier.Operation.MULTIPLY_BASE));
        });
    }
}
