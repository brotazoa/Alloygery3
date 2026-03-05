package amorphia.alloygery.gear.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class AlloygeryDefaultArmorMaterial implements ArmorMaterial
{
    public static final AlloygeryDefaultArmorMaterial INSTANCE = new AlloygeryDefaultArmorMaterial();

    private AlloygeryDefaultArmorMaterial(){} // no op

    @Override
    public int getDurabilityForType(ArmorItem.@NotNull Type type)
    {
        return 1;
    }

    @Override
    public int getDefenseForType(ArmorItem.@NotNull Type type)
    {
        return 0;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 0;
    }

    @Override
    public @NotNull SoundEvent getEquipSound()
    {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient()
    {
        return Ingredient.EMPTY;
    }

    @Override
    public @NotNull String getName()
    {
        return "alloygery_default";
    }

    @Override
    public float getToughness()
    {
        return 0.0f;
    }

    @Override
    public float getKnockbackResistance()
    {
        return 0.0f;
    }
}
