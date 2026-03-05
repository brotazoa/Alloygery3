package amorphia.alloygery.gear.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.item.ArmorStyles;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.item.part.ArmorPart;
import amorphia.alloygery.gear.item.part.DyeableArmorPart;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;
import java.util.Map;

public class ArmorPlatingRecipeShapeless extends ShapelessRecipe
{
	public ArmorPlatingRecipeShapeless(ShapelessRecipe recipe)
	{
		super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients());
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess)
	{
		return super.getResultItem(registryAccess).copy().getItem().getDefaultInstance();
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess)
	{
		ItemStack baseStack = null;
		ItemStack partStack = null;

		for(ItemStack stack : container.getItems())
		{
			if(stack == null || stack.isEmpty())
				continue;

			if (stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
			{
				stack = VanillaItemConverter.convert(stack);
			}

			if (stack.getItem() instanceof ArmorBaseItem)
			{
				baseStack = stack;
			}

			if(stack.getItem() instanceof ArmorPart)
				partStack = stack;
		}

		if(baseStack == null || partStack == null || baseStack.isEmpty() || partStack.isEmpty())
			return ItemStack.EMPTY;

		if (!NBTHelper.hasAlloygeryTag(baseStack))
		{
			baseStack = baseStack.copy();
			baseStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(baseStack.getItem().getDefaultInstance()));
		}

		ItemStack armorStack = super.getResultItem(registryAccess).copy();
		armorStack.setTag(baseStack.getTag());

		if (armorStack.getItem() instanceof ArmorBaseItem armorItem && baseStack.getItem() instanceof ArmorBaseItem armorBaseItem && partStack.getItem() instanceof ArmorPart armorPart)
		{
			AlloygeryMaterial baseMaterial = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_BASE, baseStack);
			ArmorStyles baseStyle = NBTHelper.getArmorStyleFromStack(baseStack, PartTypes.ARMOR_BASE);
			AlloygeryMaterial plateMaterial = armorPart.getPartMaterial();
			ArmorStyles plateStyle = armorPart.getStyle();

			List<Property> nbtProperties = NBTHelper.getNBTPropertiesFromItemStack(baseStack);
			ListTag partsListTag = NBTHelper.createPartsListTag(new PartTypes[] { PartTypes.ARMOR_BASE, PartTypes.ARMOR_PLATE });
			Item baseItem = BuiltInRegistries.ITEM.get(NBTHelper.getItemIdentifierFromTag(NBTHelper.getPartTagFromItemStack(baseStack, PartTypes.ARMOR_BASE)));
			CompoundTag baseTag = NBTHelper.createArmorPartTagWithStyle(baseItem == Items.AIR ? armorBaseItem : baseItem, PartTypes.ARMOR_BASE, baseStyle, baseMaterial);
			if(NBTHelper.partHasDyeColor(baseStack, PartTypes.ARMOR_BASE))
				baseTag.putInt(AlloygeryNBTKeys.DYE_COLOR, NBTHelper.getPartDyeColorFromStack(baseStack, PartTypes.ARMOR_BASE));

			CompoundTag plateTag = NBTHelper.createArmorPartTagWithStyle(armorPart, PartTypes.ARMOR_PLATE, plateStyle, plateMaterial);
			if(partStack.getItem() instanceof DyeableArmorPart dyeableArmorPart)
				plateTag.putInt(AlloygeryNBTKeys.DYE_COLOR, dyeableArmorPart.getColor(partStack));

			CompoundTag alloygeryTag = NBTHelper.createAlloygeryDataTag(armorItem, partsListTag, baseTag, plateTag);
			armorStack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);
			NBTHelper.setNBTProperties(armorStack, nbtProperties);

			final float damage = baseStack.getMaxDamage() - baseStack.getDamageValue();
			final float damage_as_percent_of_max = damage/baseStack.getMaxDamage();
			final float armor_damage = (armorStack.getMaxDamage() * damage_as_percent_of_max) - armorStack.getMaxDamage();
			armorStack.setDamageValue(Math.round(armor_damage));

			EnchantmentHelper.setEnchantments(mergeEnchantments(baseStack, partStack), armorStack);
		}

		return armorStack;
	}

	protected static Map<Enchantment, Integer> mergeEnchantments(ItemStack base, ItemStack addition)
	{
		Map<Enchantment, Integer> baseEnchantmentsMap = EnchantmentHelper.getEnchantments(base);
		Map<Enchantment, Integer> additionEnchantmentsMap = EnchantmentHelper.getEnchantments(addition);

		Map<Enchantment, Integer> mergedEnchantmentsMap = Maps.newHashMap(baseEnchantmentsMap);

		for(Enchantment enchantment : additionEnchantmentsMap.keySet())
		{
			if (baseEnchantmentsMap.containsKey(enchantment))
			{
				int baseLevel = baseEnchantmentsMap.get(enchantment);
				int mergeLevel = additionEnchantmentsMap.get(enchantment);
				if (baseLevel == mergeLevel)
				{
					mergedEnchantmentsMap.put(enchantment, baseLevel + 1);
				}
				else
				{
					mergedEnchantmentsMap.put(enchantment, Math.max(baseLevel, mergeLevel));
				}
			}
			else if(EnchantmentHelper.isEnchantmentCompatible(mergedEnchantmentsMap.keySet(), enchantment))
			{
				mergedEnchantmentsMap.put(enchantment, additionEnchantmentsMap.get(enchantment));
			}
		}

		return mergedEnchantmentsMap;
	}

	public static class Type implements RecipeType<ArmorPlatingRecipeShapeless>
	{
		public static final ResourceLocation ID = Alloygery.asResource("armor_plating");
		public static final Type INSTANCE = new Type();

		private Type() {} // no op
	}

	public static class Serializer extends ShapelessRecipe.Serializer
	{
		public static final Serializer INSTANCE = new Serializer();

		protected Serializer() {}

		@Override
		public ArmorPlatingRecipeShapeless fromJson(ResourceLocation recipeId, JsonObject json)
		{
			return new ArmorPlatingRecipeShapeless(super.fromJson(recipeId, json));
		}

		@Override
		public ArmorPlatingRecipeShapeless fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			return new ArmorPlatingRecipeShapeless(super.fromNetwork(recipeId, buffer));
		}
	}
}
