package amorphia.alloygery.machines.alloyKiln;

import amorphia.alloygery.Alloygery;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SimpleAlloyingRecipe extends AlloyingRecipe
{
	public SimpleAlloyingRecipe(AlloyingRecipe recipe)
	{
		super(recipe.id, recipe.group, recipe.ingredients, recipe.output, recipe.smeltingTime, recipe.experience);
	}

	@Override
	public RecipeType<?> getType()
	{
		return Type.INSTANCE;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return Serializer.INSTANCE;
	}

	public static class Type implements RecipeType<SimpleAlloyingRecipe>
	{
		public static final ResourceLocation ID = Alloygery.asResource("simple_alloying");
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static class Serializer extends AlloyingRecipe.Serializer
	{
		public static final Serializer INSTANCE = new Serializer();

		protected Serializer() {}

		@Override
		public AlloyingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe)
		{
			return new SimpleAlloyingRecipe(super.fromJson(recipeId, serializedRecipe));
		}

		@Override
		public AlloyingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			return new SimpleAlloyingRecipe(super.fromNetwork(recipeId, buffer));
		}
	}
}
