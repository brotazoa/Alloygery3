package amorphia.alloygery.machines.heatExchanger;

import amorphia.alloygery.Alloygery;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class HeatExchangerBlockRevertRecipe implements Recipe<Container>
{
	public static void applyRecipeInWorld(Level level, BlockPos inBlockPos)
	{
        if(level.isClientSide) return;

		BlockState inBlockState = level.getBlockState(inBlockPos);

		if(inBlockState.isAir()) return;

		Optional<HeatExchangerBlockRevertRecipe> recipe = level.getRecipeManager().getAllRecipesFor(Type.INSTANCE).stream().filter(r -> r.testBlock(inBlockState)).findFirst();

		if(recipe.isEmpty()) return;

		HeatExchangerBlockRevertRecipe revertRecipe = recipe.get();
		BlockState revertedBlockState = revertRecipe.revertBlock(inBlockState);

		if(revertedBlockState.isAir()) return;

		level.setBlock(inBlockPos, revertedBlockState, 3);
	}

	protected final ResourceLocation id;
	protected final Ingredient ingredient;
	protected final ItemStack output;

	public HeatExchangerBlockRevertRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.output = output;
	}

	@Override
	public boolean matches(Container container, Level level)
	{
		return ingredient.test(container.getItem(0));
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess)
	{
		return getResultItem(registryAccess).copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess)
	{
		return this.output;
	}

	public boolean testBlock(BlockState in)
	{
        return ingredient.test(new ItemStack(in.getBlock().asItem()));
	}

	public BlockState revertBlock(BlockState in)
	{
		ItemStack result = this.output.copy();
		if (result.getItem() instanceof BlockItem out)
		{
			return HeatExchangerBlockTransformRecipe.copyProperties(in, out.getBlock().defaultBlockState());
		}

		return Blocks.AIR.defaultBlockState();
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

	public static class Type implements RecipeType<HeatExchangerBlockRevertRecipe>
	{
		private Type() {}

		public static final ResourceLocation ID = Alloygery.asResource("heat_exchanger_block_revert");
		public static final Type INSTANCE = new Type();
	}

	public static class Serializer implements RecipeSerializer<HeatExchangerBlockRevertRecipe>
	{
		protected Serializer() {}

		public static final Serializer INSTANCE = new HeatExchangerBlockRevertRecipe.Serializer();

		@Override
		public void toNetwork(FriendlyByteBuf buffer, HeatExchangerBlockRevertRecipe recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}

		@Override
		public HeatExchangerBlockRevertRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			final Ingredient input = Ingredient.fromNetwork(buffer);
			final ItemStack output = buffer.readItem();

			return new HeatExchangerBlockRevertRecipe(recipeId, input, output);
		}

		@Override
		public HeatExchangerBlockRevertRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe)
		{
			final Ingredient input = Ingredient.fromJson(serializedRecipe.get("input"));
			final ItemStack output = getItemStack(GsonHelper.getAsJsonObject(serializedRecipe, "output"));

			return new HeatExchangerBlockRevertRecipe(recipeId, input, output);
		}

		protected ItemStack getItemStack(JsonObject jsonObject)
		{
			final var item = GsonHelper.getAsItem(jsonObject, "item");
			final var count = GsonHelper.getAsInt(jsonObject, "count", 1);
			return new ItemStack(item, count);
		}
	}
}
