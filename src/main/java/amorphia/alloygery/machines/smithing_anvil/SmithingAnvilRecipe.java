package amorphia.alloygery.machines.smithing_anvil;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientRecipe;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class SmithingAnvilRecipe extends AbstractSingleIngredientRecipe
{
    public SmithingAnvilRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, int materialCost)
    {
        super(Type.INSTANCE, MachinesModule.SMITHING_ANVIL_RECIPE_SERIALIZER, id, result, ingredient, materialCost);
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(MachinesModule.SMITHING_ANVIL);
    }

    public static class Type implements RecipeType<SmithingAnvilRecipe>
    {
        private Type() {}

        public static final ResourceLocation ID = Alloygery.asResource("smithing_anvil");
        public static final SmithingAnvilRecipe.Type INSTANCE = new SmithingAnvilRecipe.Type();
    }

    public static class Serializer extends AbstractSingleIngredientRecipe.Serializer<SmithingAnvilRecipe>
    {
        public Serializer(SingleIngredientRecipeMaker<SmithingAnvilRecipe> factory)
        {
            super(factory);
        }
    }
}
