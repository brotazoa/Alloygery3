package amorphia.alloygery.gear.convert;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.material.AlloygeryDefaultMaterials;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.item.PartTypes;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.Set;

public class VanillaItemConverter
{
    public static final TagKey<Item> VANILLA_ITEM_CONVERTIBLE = TagKey.create(Registries.ITEM, Alloygery.asResource("dynamic_gear_vanilla_convertible_items"));
    public static final Item[] VANILLA_CONVERTIBLE_ITEMS = new Item[] {
			// tools
            Items.WOODEN_AXE,
            Items.WOODEN_HOE,
            Items.WOODEN_PICKAXE,
            Items.WOODEN_SHOVEL,
            Items.WOODEN_SWORD,

            Items.STONE_AXE,
            Items.STONE_HOE,
            Items.STONE_PICKAXE,
            Items.STONE_SHOVEL,
            Items.STONE_SWORD,

            Items.IRON_AXE,
            Items.IRON_HOE,
            Items.IRON_PICKAXE,
            Items.IRON_SHOVEL,
            Items.IRON_SWORD,

            Items.GOLDEN_AXE,
            Items.GOLDEN_HOE,
            Items.GOLDEN_PICKAXE,
            Items.GOLDEN_SHOVEL,
            Items.GOLDEN_SWORD,

            Items.DIAMOND_AXE,
            Items.DIAMOND_HOE,
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_SWORD,

            Items.NETHERITE_AXE,
            Items.NETHERITE_HOE,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_SWORD,

			// armor
			Items.LEATHER_HELMET,
			Items.LEATHER_CHESTPLATE,
			Items.LEATHER_LEGGINGS,
			Items.LEATHER_BOOTS,

			Items.GOLDEN_HELMET,
			Items.GOLDEN_CHESTPLATE,
			Items.GOLDEN_LEGGINGS,
			Items.GOLDEN_BOOTS,

			Items.CHAINMAIL_HELMET,
			Items.CHAINMAIL_CHESTPLATE,
			Items.CHAINMAIL_LEGGINGS,
			Items.CHAINMAIL_BOOTS,

			Items.IRON_HELMET,
			Items.IRON_CHESTPLATE,
			Items.IRON_LEGGINGS,
			Items.IRON_BOOTS,

			Items.DIAMOND_HELMET,
			Items.DIAMOND_CHESTPLATE,
			Items.DIAMOND_LEGGINGS,
			Items.DIAMOND_BOOTS,

			Items.NETHERITE_HELMET,
			Items.NETHERITE_CHESTPLATE,
			Items.NETHERITE_LEGGINGS,
			Items.NETHERITE_BOOTS,
    };

    public static ItemStack convert(ItemStack stack)
    {
        return convert(stack, false);
    }

    public static ItemStack convert(ItemStack stack, boolean force)
    {
        if(stack.is(VANILLA_ITEM_CONVERTIBLE) || force)
        {
            String name = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

			if(name.startsWith("leather") || name.startsWith("chainmail"))
			{
				name = "base_" + name;
			}

            name = name.replace("golden", "gold");
			name = name.replace("chainmail", "iron_chain");

            Item alloygeryItem = BuiltInRegistries.ITEM.get(Alloygery.asResource(name));

            return alloygeryItem == Items.AIR ? stack : mergeNbt(alloygeryItem.getDefaultInstance(), stack);
        }
        else return stack;
    }

    private static ItemStack mergeNbt(ItemStack alloygeryStack, ItemStack vanillaStack)
    {
        ItemStack merged = alloygeryStack.copy();
        merged.setTag(vanillaStack.getTag());
        merged.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(alloygeryStack));
        return merged;
    }
}
