package amorphia.alloygery.machines;

import com.google.gson.JsonObject;
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

public abstract class AbstractSingleIngredientRecipe implements Recipe<Container>
{
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;

    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final int materialCost;
    protected final ItemStack result;

    protected AbstractSingleIngredientRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, ItemStack result, Ingredient ingredient, int materialCost)
    {
        this.type = type;
        this.serializer = serializer;

        this.id = id;
        this.ingredient = ingredient;
        this.materialCost = materialCost;
        this.result = result;
    }

    @Override
    public boolean matches(Container container, Level level)
    {
        return !container.getItem(AbstractSingleIngredientMenu.INPUT_SLOT).isEmpty() && this.ingredient.test(container.getItem(AbstractSingleIngredientMenu.INPUT_SLOT));
    }

    public boolean canAfford(Container container, Level level)
    {
        return this.matches(container, level) && container.getItem(AbstractSingleIngredientMenu.INPUT_SLOT).getCount() >= this.getMaterialCost();
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess)
    {
        return this.result.copy().getItem().getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 1;
    }

	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(getIngredient());
		return list;
	}

	public Ingredient getIngredient()
	{
		return ingredient;
	}

	@Override
    public ItemStack getResultItem(RegistryAccess registryAccess)
    {
        return this.result.copy();
    }

    @Override
    public ResourceLocation getId()
    {
        return this.id;
    }

    public int getMaterialCost()
    {
        return this.materialCost;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return this.serializer;
    }

    @Override
    public RecipeType<?> getType()
    {
        return this.type;
    }

    public static class Serializer<T extends AbstractSingleIngredientRecipe> implements RecipeSerializer<T>
    {
        final SingleIngredientRecipeMaker<T> factory;

        protected Serializer(SingleIngredientRecipeMaker<T> factory)
        {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject json)
        {
            Ingredient ingredient;
            if(GsonHelper.isArrayNode(json, "ingredient"))
            {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
            }
            else
            {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
            }
            int cost = GsonHelper.getAsInt(json, "material_cost", 1);
            ItemStack result = getItemStack(GsonHelper.getAsJsonObject(json, "result"));
            return this.factory.create(recipeId, result, ingredient, cost);
        }

        @Override
        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int cost = buffer.readInt();
            ItemStack result = buffer.readItem();
            return this.factory.create(recipeId, result, ingredient, cost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.materialCost);
            buffer.writeItem(recipe.result);
        }

        protected ItemStack getItemStack(JsonObject json)
        {
            final var item = GsonHelper.getAsItem(json, "item");
            final var count = GsonHelper.getAsInt(json, "count", 1);
            return new ItemStack(item, count);
        }

        public interface SingleIngredientRecipeMaker<T extends AbstractSingleIngredientRecipe>
        {
            T create(ResourceLocation id, ItemStack result, Ingredient ingredient, int materialCost);
        }
    }
}
