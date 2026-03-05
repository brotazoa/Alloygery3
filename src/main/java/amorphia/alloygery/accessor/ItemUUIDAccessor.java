package amorphia.alloygery.accessor;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Item.class)
public interface ItemUUIDAccessor
{
    @Accessor("BASE_ATTACK_DAMAGE_UUID")
    static UUID getAttackDamageModifierUUID()
    {
        throw new AssertionError(); // you shouldn't be here
    }

    @Accessor("BASE_ATTACK_SPEED_UUID")
    static UUID getAttackSpeedModifierUUID()
    {
        throw new AssertionError(); // you shouldn't be here either
    }
}
