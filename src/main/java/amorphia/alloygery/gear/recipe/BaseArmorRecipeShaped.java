package amorphia.alloygery.gear.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;

public class BaseArmorRecipeShaped extends ShapedRecipe
{
	protected final List<Property> properties;

	public BaseArmorRecipeShaped(ShapedRecipe recipe, List<Property> properties)
	{
		super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY));
		this.properties = properties;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess)
	{
		ItemStack armorStack = super.getResultItem(registryAccess).copy();

		if (armorStack.getItem() instanceof ArmorBaseItem)
		{
			if (!NBTHelper.hasAlloygeryTag(armorStack))
			{
				armorStack = armorStack.getItem().getDefaultInstance();
			}

			NBTHelper.setNBTProperties(armorStack, properties);
		}

		return armorStack;
	}

	public static class Type implements RecipeType<BaseArmorRecipeShaped>
	{
		public static final ResourceLocation ID = Alloygery.asResource("armor_shaped");
		public static final Type INSTANCE = new Type();
		private Type() {} // no op
	}

	public static class Serializer extends ShapedRecipe.Serializer
	{
		public static final Serializer INSTANCE = new Serializer();

		protected Serializer() {} // protected op

		@Override
		public ShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			JsonArray propertiesArray = json.has("properties_on_result") ? json.getAsJsonArray("properties_on_result") : null;
			List<Property> properties = Lists.newArrayList();
			if (propertiesArray != null)
			{
				DataResult<List<Property>> result = Property.CODEC.listOf().parse(JsonOps.INSTANCE, propertiesArray);
				result.result().ifPresent(properties::addAll);
			}

			return new BaseArmorRecipeShaped(super.fromJson(recipeId, json), properties);
		}

		@Override
		public ShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			final ShapedRecipe shapedRecipe = super.fromNetwork(recipeId, buffer);
			final List<CompoundTag> propertiesTagList = buffer.readList(FriendlyByteBuf::readNbt);

			List<Property> propertiesList = Lists.newArrayList();
			for(CompoundTag ptag : propertiesTagList)
			{
				DataResult<Property> result = Property.CODEC.parse(NbtOps.INSTANCE, ptag);
				Property p = result.getOrThrow(false, Alloygery.LOGGER::error);
				propertiesList.add(p);
			}

			return new BaseArmorRecipeShaped(shapedRecipe, propertiesList);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapedRecipe recipe)
		{
			super.toNetwork(buffer, recipe);

			List<CompoundTag> propertiesTagList = Lists.newArrayList();
			for(Property p : ((ToolRecipeShaped) recipe).properties)
			{
				DataResult<Tag> result = Property.CODEC.encodeStart(NbtOps.INSTANCE, p);
				Tag propertyTag = result.getOrThrow(false, Alloygery.LOGGER::error);
				CompoundTag compoundTag = (CompoundTag) propertyTag;
				propertiesTagList.add(compoundTag);
			}

			buffer.writeCollection(propertiesTagList, FriendlyByteBuf::writeNbt);
		}
	}
}
