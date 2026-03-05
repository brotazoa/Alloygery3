package amorphia.alloygery.machines.woodcutter;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientRecipe;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class WoodcutterRecipe extends AbstractSingleIngredientRecipe
{
    public WoodcutterRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, int materialCost)
    {
        super(Type.INSTANCE, MachinesModule.WOODCUTTING_RECIPE_SERIALIZER, id, result, ingredient, materialCost);
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(MachinesModule.WOODCUTTER);
    }

    public static class Type implements RecipeType<WoodcutterRecipe>
    {
        private Type() {}

        public static final ResourceLocation ID = Alloygery.asResource("woodcutting");
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer extends AbstractSingleIngredientRecipe.Serializer<WoodcutterRecipe>
    {
        public Serializer(SingleIngredientRecipeMaker<WoodcutterRecipe> factory)
        {
            super(factory);
        }
    }
}
