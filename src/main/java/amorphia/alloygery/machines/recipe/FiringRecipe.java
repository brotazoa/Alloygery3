package amorphia.alloygery.machines.recipe;

import amorphia.alloygery.Alloygery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class FiringRecipe extends AbstractCookingRecipe
{
	public FiringRecipe(ResourceLocation resourceLocation, String s, CookingBookCategory cookingBookCategory, Ingredient ingredient,
			ItemStack itemStack, float v, int i)
	{
		super(Type.INSTANCE, resourceLocation, s, cookingBookCategory, ingredient, itemStack, v, i);
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return Serializer.INSTANCE;
	}

	public static class Type implements RecipeType<FiringRecipe>
	{
		private Type() {} // no op

		public static final ResourceLocation ID = Alloygery.asResource("firing");
		public static final Type INSTANCE = new Type();
	}

	public static class Serializer extends SimpleCookingSerializer<FiringRecipe>
	{
		public static final Serializer INSTANCE = new Serializer(FiringRecipe::new, 100);

		public Serializer(CookieBaker<FiringRecipe> factory, int defaultCookingTime)
		{
			super(factory, defaultCookingTime);
		}
	}

}
