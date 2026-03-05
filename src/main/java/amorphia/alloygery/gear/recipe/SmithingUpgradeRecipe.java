package amorphia.alloygery.gear.recipe;

import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class SmithingUpgradeRecipe implements SmithingRecipe
{
	private final ResourceLocation id;
	final Ingredient template;
	final Ingredient base;
	final Ingredient addition;
	final ResourceLocation upgradeMaterial;

	public SmithingUpgradeRecipe(ResourceLocation id, Ingredient template, Ingredient base, Ingredient addition, ResourceLocation upgradeMaterial)
	{
		this.id = id;
		this.template = template;
		this.base = base;
		this.addition = addition;
		this.upgradeMaterial = upgradeMaterial;
	}

	@Override
	public boolean isTemplateIngredient(ItemStack stack)
	{
		return this.template.test(stack);
	}

	@Override
	public boolean isBaseIngredient(ItemStack stack)
	{
		if(stack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
			stack = VanillaItemConverter.convert(stack);

		return this.base.test(stack);
	}

	@Override
	public boolean isAdditionIngredient(ItemStack stack)
	{
		return this.addition.test(stack);
	}

	@Override
	public boolean matches(Container container, Level level)
	{
		ItemStack itemStack = container.getItem(1);
		if (itemStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
		{
			itemStack = VanillaItemConverter.convert(itemStack);
		}

		return this.template.test(container.getItem(0)) && this.base.test(itemStack) && this.addition.test(container.getItem(2));
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess)
	{
		ItemStack itemStack = container.getItem(1);
		if (itemStack.is(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE))
		{
			itemStack = VanillaItemConverter.convert(itemStack);
		}

		if(this.base.test(itemStack) && !NBTHelper.isUpgraded(itemStack))
		{
			ItemStack result = itemStack.copy();
			result.setCount(1);

			if (!NBTHelper.hasAlloygeryTag(result))
			{
				result.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(result.getItem().getDefaultInstance()));
			}

			CompoundTag alloygeryTag = NBTHelper.getAlloygeryDataTag(result).copy();
			List<PartTypes> partsList = NBTHelper.getPartTypesListFromItemStack(result);
			if (result.getItem() instanceof IDynamicTool)
			{
				alloygeryTag.put(PartTypes.TOOL_UPGRADE.getName(), NBTHelper.createToolUpgradePartTag(MaterialHelper.getMaterialFromIdentifier(upgradeMaterial)));
				partsList.add(PartTypes.TOOL_UPGRADE);
			}
			else if(result.getItem() instanceof IDynamicArmor)
			{
				alloygeryTag.put(PartTypes.ARMOR_UPGRADE.getName(), NBTHelper.createArmorUpgradePartTag(MaterialHelper.getMaterialFromIdentifier(upgradeMaterial)));
				partsList.add(PartTypes.ARMOR_UPGRADE);
			}
			ListTag partsListTag = NBTHelper.createPartsListTag(partsList);
			alloygeryTag.put(AlloygeryNBTKeys.PART_LIST, partsListTag);

			result.getOrCreateTag().put(AlloygeryNBTKeys.NBT, alloygeryTag);

			return result;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess)
	{
		return GearModule.ITEMS.get("base_leather_chestplate").getDefaultInstance();
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<SmithingUpgradeRecipe>
	{
		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public SmithingUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient template = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
			Ingredient base = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
			Ingredient addition = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
			ResourceLocation upgradeMaterial = ResourceLocation.tryParse(json.get("upgrade_material").getAsString());
			return new SmithingUpgradeRecipe(recipeId, template, base, addition, upgradeMaterial);
		}

		@Override
		public SmithingUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient template = Ingredient.fromNetwork(buffer);
			Ingredient base = Ingredient.fromNetwork(buffer);
			Ingredient addition = Ingredient.fromNetwork(buffer);
			ResourceLocation upgradeMaterial = buffer.readResourceLocation();
			return new SmithingUpgradeRecipe(recipeId, template, base, addition, upgradeMaterial);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SmithingUpgradeRecipe recipe)
		{
			recipe.template.toNetwork(buffer);
			recipe.base.toNetwork(buffer);
			recipe.addition.toNetwork(buffer);
			buffer.writeResourceLocation(recipe.upgradeMaterial);
		}
	}
}
