package amorphia.alloygery.gear.mixin;

import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.dynamicProviders.IDynamicAttributeModifiers;
import amorphia.alloygery.gear.dynamicProviders.IDynamicMaxDamage;
import amorphia.alloygery.gear.dynamicProviders.IDynamicMiningTool;
import amorphia.alloygery.gear.property.PrimitiveProperty;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackDynamicGearMixin
{
    @Shadow public abstract Item getItem();
    @Shadow public abstract boolean is(TagKey<Item> tag);
    @Shadow public abstract boolean hurt(int amount, RandomSource randomSource, ServerPlayer user);
    @Shadow public abstract void shrink(int decrement);
    @Shadow public abstract boolean isDamageableItem();
    @Shadow public abstract void setDamageValue(int damage);
    @Shadow public abstract int getMaxDamage();

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getDynamicMaxDamage(CallbackInfoReturnable<Integer> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
        {
            stack = VanillaItemConverter.convert(stack);
        }

        if (stack.getItem() instanceof IDynamicMaxDamage dynamicMaxDamage)
        {
            cir.setReturnValue(dynamicMaxDamage.getMaxDamage(stack));
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    public void getDynamicMiningSpeed(BlockState blockState, CallbackInfoReturnable<Float> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
        {
            stack = VanillaItemConverter.convert(stack);
        }

        if (stack.getItem() instanceof IDynamicMiningTool dynamicMiningTool)
        {
            cir.setReturnValue(dynamicMiningTool.getDynamicMiningSpeed(blockState, stack));
        }
    }

    @Inject(method = "isCorrectToolForDrops", at = @At("RETURN"), cancellable = true)
    public void isDynamicToolForDrops(BlockState blockState, CallbackInfoReturnable<Boolean> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
        {
            stack = VanillaItemConverter.convert(stack);
        }

        if (stack.getItem() instanceof IDynamicMiningTool dynamicMiningTool)
        {
            cir.setReturnValue(dynamicMiningTool.isCorrectToolForDrops(blockState, stack));
        }
    }

    @ModifyVariable(method = "getAttributeModifiers", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    public Multimap<Attribute, AttributeModifier> getDynamicAttributeModifiers(Multimap<Attribute, AttributeModifier> existingMap, EquipmentSlot slot)
    {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
        {
            stack = VanillaItemConverter.convert(stack);
        }

        return stack.getItem() instanceof IDynamicAttributeModifiers provider ? provider.modifyExistingModifiersMap(existingMap, slot, stack) : existingMap;
    }

    @Inject(method = "hurtAndBreak", at = @At(value = "HEAD"), cancellable = true)
    public <T extends LivingEntity> void hurtAndSetBrokenCondition(int amount, T entity, Consumer<T> onBroken, CallbackInfo ci)
    {
        ItemStack stack = (ItemStack) (Object) this;

        if(stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
        {
            stack = VanillaItemConverter.convert(stack);
        }

        if (stack.getItem() instanceof IDynamicMaxDamage dynamicMaxDamage)
        {
            if(!entity.level().isClientSide && (!(entity instanceof Player) || !((Player) entity).getAbilities().instabuild))
            {
                if (this.isDamageableItem())
                {
                    final boolean wasBroken = dynamicMaxDamage.isBroken(stack);
                    if(this.hurt(amount, entity.getRandom(), entity instanceof ServerPlayer ? (ServerPlayer) entity : null))
                    {
                        if (!wasBroken)
                        {
                            onBroken.accept(entity);
                            if(entity instanceof Player)
                            {
                                Item item = this.getItem();
                                ((Player) entity).awardStat(Stats.ITEM_BROKEN.get(item));
                            }
                        }

                        if(PrimitiveProperty.compute(stack))
                        {
                            this.shrink(1);
                            this.setDamageValue(0);
                        }
                        else
                        {
                            this.setDamageValue(this.getMaxDamage());
                        }
                    }
                }
            }

            ci.cancel();
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void passUseOnIfBroken(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;

        if(stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
        {
            stack = VanillaItemConverter.convert(stack);
        }

        if (stack.getItem() instanceof IDynamicMaxDamage dynamicMaxDamage && dynamicMaxDamage.isBroken(stack))
        {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
