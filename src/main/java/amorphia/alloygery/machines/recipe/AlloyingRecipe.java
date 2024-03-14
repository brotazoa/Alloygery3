package amorphia.alloygery.machines.recipe;

import amorphia.alloygery.Alloygery;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlloyingRecipe implements Recipe<Container>
{
	protected final ResourceLocation id;
	protected final String group;
	protected final List<Ingredient> ingredients;
	protected final ItemStack output;
	protected final float experience;
	protected final int smeltingTime;

	public AlloyingRecipe(ResourceLocation id, String group, List<Ingredient> ingredients, ItemStack output, int smeltingTime, float experience)
	{
		this.id = id;
		this.group = group;
		this.ingredients = ingredients;
		this.output = output;
		this.smeltingTime = smeltingTime;
		this.experience = experience;
	}

	@Override
	public boolean matches(@NotNull Container container, @NotNull Level level)
	{
		// create a flat list of inputs, where partial stacks will be merged
		List<ItemStack> inputs = IntStream.range(0, 4).mapToObj(i -> container.getItem(i).copy())
				.filter(itemStack -> !itemStack.isEmpty())
				.collect(Collectors.groupingBy(ItemStack::getItem, Collectors.summingInt(ItemStack::getCount)))
				.entrySet().stream().map(pair -> new ItemStack(pair.getKey(), pair.getValue())).toList();

		if(inputs.isEmpty()) return false;

		//iterate through ingredients and see if there are enough matches in the inputs list
		List<Ingredient> recipeIngredients = List.copyOf(this.ingredients);
		Iterator<Ingredient> iterator = recipeIngredients.iterator();
		while(iterator.hasNext())
		{
			Ingredient ingredient = iterator.next();
			for(ItemStack stack : inputs)
			{
				if(stack.isEmpty() || !ingredient.test(stack)) continue;

				stack.shrink(1);
				iterator.remove();
				break;
			}
		}

		// if the iterator still has ingredients then a match was not found
		if(!recipeIngredients.isEmpty()) return false;

		// test to see if there are any extra inputs that are not part of the recipe
		for(ItemStack stack : inputs)
		{
			if(!stack.isEmpty() && this.ingredients.stream().noneMatch(ingredient -> ingredient.test(stack))) return false;
		}

		// inputs match recipe
		return true;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess)
	{
		return this.getResultItem(registryAccess);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return width * height >= this.ingredients.size();
	}

	@Override
	public @NotNull NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0]));
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess)
	{
		return this.output.copy();
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

	@Override
	public RecipeType<?> getType()
	{
		return Type.INSTANCE;
	}

	@Override
	public String getGroup()
	{
		return this.group;
	}

	public int getSmeltingTime()
	{
		return smeltingTime;
	}

	public float getExperience()
	{
		return experience;
	}

	public static class Type implements RecipeType<AlloyingRecipe>
	{
		private Type() {}

		public static final ResourceLocation ID = Alloygery.asResource("alloying");
		public static final Type INSTANCE = new Type();
	}

	public static class Serializer implements RecipeSerializer<AlloyingRecipe>
	{
		protected Serializer() {}

		public static final Serializer INSTANCE = new Serializer();

		@Override
		public void toNetwork(FriendlyByteBuf buffer, AlloyingRecipe recipe)
		{
			buffer.writeUtf(recipe.group);
			buffer.writeCollection(recipe.getIngredients(), ((packetByteBuff, ingredient) -> ingredient.toNetwork(packetByteBuff)));
			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.smeltingTime);
			buffer.writeFloat(recipe.experience);
		}

		@Override
		public AlloyingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			final String group = buffer.readUtf(32767);
			final var inputs = buffer.readCollection(value -> new ArrayList<>(), Ingredient::fromNetwork);
			final var output = buffer.readItem();
			final int smeltingTime = buffer.readVarInt();
			final float exp = buffer.readFloat();

			return new AlloyingRecipe(recipeId, group, inputs, output, smeltingTime, exp);
		}

		@Override
		public AlloyingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe)
		{
			final String group = GsonHelper.getAsString(serializedRecipe, "group", "");
			final var inputs = new ArrayList<Ingredient>();

			GsonHelper.getAsJsonArray(serializedRecipe, "inputs").forEach(jsonElement -> inputs.add(Ingredient.fromJson(jsonElement)));
			if(inputs.isEmpty()) throw new JsonSyntaxException("Inputs can not be empty");

			final var output = getItemStack(GsonHelper.getAsJsonObject(serializedRecipe, "output"));
			final int smeltingTime = GsonHelper.getAsInt(serializedRecipe, "smelting_time");
			final float exp = GsonHelper.getAsFloat(serializedRecipe, "experience", 0.0f);

			return new AlloyingRecipe(recipeId, group, inputs, output, smeltingTime, exp);
		}

		protected ItemStack getItemStack(JsonObject json)
		{
			final var item = GsonHelper.getAsItem(json, "id");
			final var count = GsonHelper.getAsInt(json, "count", 1);
			return new ItemStack(item, count);
		}
	}
}
