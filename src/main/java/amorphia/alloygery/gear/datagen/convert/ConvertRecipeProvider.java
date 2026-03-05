package amorphia.alloygery.gear.datagen.convert;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.datagen.recipe.GearRecipeProvider;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ConvertRecipeProvider extends FabricRecipeProvider
{
    public ConvertRecipeProvider(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter)
    {
        for(Item item : VanillaItemConverter.VANILLA_CONVERTIBLE_ITEMS)
        {
            makeEmptyRecipeFor(item, exporter);
        }

        // stone
        GearRecipeProvider.generatePrimitiveVanillaToolSet("stone", ItemTags.STONE_TOOL_MATERIALS, exporter);

        // golden
        GearRecipeProvider.generatePrimitiveVanillaToolSet("gold", TagKey.create(Registries.ITEM, Alloygery.asCommonResource("gold_ingots")), exporter);

		// override netherite upgrade recipes
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_AXE).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_BOOTS).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_CHESTPLATE).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_HELMET).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_HOE).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_LEGGINGS).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_PICKAXE).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_SHOVEL).withSuffix("_smithing")));
		exporter.accept(new EmptyRecipeResult(RecipeBuilder.getDefaultRecipeId(Items.NETHERITE_SWORD).withSuffix("_smithing")));
    }

    @Override
    protected ResourceLocation getRecipeIdentifier(ResourceLocation identifier)
    {
        return identifier;
    }

    private static void makeEmptyRecipeFor(Item item, Consumer<FinishedRecipe> exporter)
    {
        exporter.accept(new EmptyRecipeResult(BuiltInRegistries.ITEM.getKey(item)));
    }

	public record EmptyRecipe(ResourceLocation id) implements Recipe<Container>
	{
		@Override
		public boolean matches(@NotNull Container container, @NotNull Level level)
		{
			return false;
		}

		@Override
		public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess)
		{
			return ItemStack.EMPTY;
		}

		@Override
		public boolean canCraftInDimensions(int width, int height)
		{
			return false;
		}

		@Override
		public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess)
		{
			return ItemStack.EMPTY;
		}

		@Override
		public @NotNull ResourceLocation getId()
		{
			return this.id;
		}

		@Override
		public @NotNull RecipeSerializer<?> getSerializer()
		{
			return EmptySerializer.INSTANCE;
		}

		@Override
		public @NotNull RecipeType<?> getType()
		{
			return EmptyType.INSTANCE;
		}
	}

	public static class EmptyType implements RecipeType<EmptyRecipe>
	{
		public static final ResourceLocation ID = Alloygery.asResource("empty");
		public static final EmptyType INSTANCE = new EmptyType();
	}

	public static class EmptySerializer implements RecipeSerializer<EmptyRecipe>
	{
		public static final EmptySerializer INSTANCE = new EmptySerializer();

		@Override
		public @NotNull EmptyRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe)
		{
			return new EmptyRecipe(recipeId);
		}

		@Override
		public @NotNull EmptyRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer)
		{
			return new EmptyRecipe(recipeId);
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull EmptyRecipe recipe)
		{
		}
	}

    private record EmptyRecipeResult(ResourceLocation id) implements FinishedRecipe
    {
        @Override
        public void serializeRecipeData(@NotNull JsonObject json)
        {
            // do nothing
        }

        @Override
        public @NotNull ResourceLocation getId()
        {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType()
        {
            return EmptySerializer.INSTANCE;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement()
        {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId()
        {
            return null;
        }
    }
}
