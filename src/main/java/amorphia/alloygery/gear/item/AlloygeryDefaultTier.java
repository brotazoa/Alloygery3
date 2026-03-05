package amorphia.alloygery.gear.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class AlloygeryDefaultTier implements Tier
{
    public static final AlloygeryDefaultTier INSTANCE = new AlloygeryDefaultTier();

    private AlloygeryDefaultTier(){} // no op

    @Override
    public int getUses()
    {
        return 1;
    }

    @Override
    public float getSpeed()
    {
        return 1.0f;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return 0.0f;
    }

    @Override
    public int getLevel()
    {
        return -1;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 0;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient()
    {
        return Ingredient.EMPTY;
    }
}
