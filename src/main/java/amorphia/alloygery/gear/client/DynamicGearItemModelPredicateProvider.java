package amorphia.alloygery.gear.client;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.PropertyHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DynamicGearItemModelPredicateProvider
{
    public static void registerItemModelPredicateProviders()
    {
        for (Item item : GearModule.ITEMS.values())
        {
            if(item instanceof ICanBeBroken breakableItem)
            {
                ItemProperties.register(item, ICanBeBroken.IDENTIFIER, breakableItem::calculateBrokenPredicate);
            }

            if (item instanceof ICanBeImproved itemWithImprovement)
            {
                ItemProperties.register(item, ICanBeImproved.IDENTIFIER, itemWithImprovement::calculateImprovementTypePredicate);
            }

            if (item instanceof ICanHaveToolBinding toolWithBinding)
            {
                ItemProperties.register(item, ICanHaveToolBinding.IDENTIFIER, toolWithBinding::calculateToolBindingPredicate);
            }

            if (item instanceof ICanHaveArmorStyling armorWithStyle)
            {
                ItemProperties.register(item, ICanHaveArmorStyling.IDENTIFIER, armorWithStyle::calculateArmorStylePredicate);
            }

            if (item instanceof ICanBeUpgraded itemWithUpgrade)
            {
                ItemProperties.register(item, ICanBeUpgraded.IDENTIFIER, itemWithUpgrade::calculateUpgradedPredicate);
            }
        }
    }

    public interface ICanBeBroken
    {
        ResourceLocation IDENTIFIER = Alloygery.asResource("broken");

        default float calculateBrokenPredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed)
        {
            return PropertyHelper.isBroken(stack) ? 1.0f : 0.0f;
        }
    }

    public interface ICanBeImproved
    {
        ResourceLocation IDENTIFIER = Alloygery.asResource("improvement_type");

        float calculateImprovementTypePredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed);
    }

    public interface ICanBeUpgraded
    {
        ResourceLocation IDENTIFIER = Alloygery.asResource("upgraded");

        default float calculateUpgradedPredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed)
        {
            return NBTHelper.isUpgraded(stack) ? 1.0f : 0.0f;
        }
    }

    public interface ICanHaveToolBinding
    {
        ResourceLocation IDENTIFIER = Alloygery.asResource("tool_has_binding");

        default float calculateToolBindingPredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed)
        {
            return NBTHelper.hasPartTag(stack, PartTypes.TOOL_BINDING) ? 1.0f : 0.0f;
        }
    }

    public interface ICanHaveArmorStyling
    {
        ResourceLocation IDENTIFIER = Alloygery.asResource("armor_style");

        default float calculateArmorStylePredicate(ItemStack stack, ClientLevel clientLevel, LivingEntity user, int seed)
        {
            if(stack == null || stack.isEmpty() || !(stack.getItem() instanceof IDynamicArmor))
                return 0.0f;

            if (!NBTHelper.hasAlloygeryTag(stack))
            {
                stack = stack.copy();
                stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(stack.getItem().getDefaultInstance()));
            }

            return NBTHelper.partHasArmorStyle(stack, PartTypes.ARMOR_BASE) ? NBTHelper.getArmorStyleFromStack(stack, PartTypes.ARMOR_BASE).getTypeFloat() : 0.0f;
        }
    }
}
