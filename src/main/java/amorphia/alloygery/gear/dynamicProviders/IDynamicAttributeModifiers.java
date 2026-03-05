package amorphia.alloygery.gear.dynamicProviders;

import com.google.common.collect.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public interface IDynamicAttributeModifiers
{
    List<Attribute> DEFAULT_ATTRIBUTES_TO_REPLACE = List.of(
            Attributes.ATTACK_DAMAGE,
            Attributes.ATTACK_SPEED,
            Attributes.ARMOR,
            Attributes.ARMOR_TOUGHNESS,
            Attributes.KNOCKBACK_RESISTANCE
            );

    Multimap<Attribute, AttributeModifier> EMPTY = ImmutableSetMultimap.of();

    default Multimap<Attribute, AttributeModifier> getDynamicAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        return EMPTY;
    }

    default Multimap<Attribute, AttributeModifier> modifyExistingModifiersMap(Multimap<Attribute, AttributeModifier> existing, EquipmentSlot slot, ItemStack stack)
    {
        if(stack.getItem() instanceof IDynamicAttributeModifiers dynamicModifiers)
        {
            Multimap<Attribute, AttributeModifier> old = LinkedListMultimap.create();
            old.putAll(existing);

            Multimap<Attribute, AttributeModifier> dynamic = dynamicModifiers.getDynamicAttributeModifiers(slot, stack);
            for(Attribute replace : DEFAULT_ATTRIBUTES_TO_REPLACE)
            {
                for(AttributeModifier modifier : old.get(replace))
                {
                    Optional.of(dynamic.get(replace).stream().filter(m -> m.getId() == modifier.getId()).findFirst()).ifPresent(m -> old.remove(replace, modifier));
                }
            }

            LinkedListMultimap<Attribute, AttributeModifier> ordered = LinkedListMultimap.create();
            ordered.putAll(dynamic);
            ordered.putAll(old);

            return ordered;
        }

        return existing;
    }
}
