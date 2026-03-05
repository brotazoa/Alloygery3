package amorphia.alloygery.machines.tailoring_table;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.machines.AbstractSingleIngredientMenu;
import amorphia.alloygery.machines.AbstractSingleIngredientRecipe;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class TailoringTableRecipe extends AbstractSingleIngredientRecipe
{
    public TailoringTableRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, int materialCost)
    {
        super(Type.INSTANCE, MachinesModule.TAILORING_TABLE_RECIPE_SERIALIZER, id, result, ingredient, materialCost);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess)
    {
        ItemStack result = super.assemble(container, registryAccess);
        if (result.getItem() instanceof DyeableLeatherItem dyeableResult)
        {
            ItemStack input = container.getItem(AbstractSingleIngredientMenu.INPUT_SLOT);
            if (input.getItem() instanceof DyeableLeatherItem dyeableInput)
            {
                dyeableResult.setColor(result, dyeableInput.getColor(input));
            }
        }
        return result;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(MachinesModule.TAILORING_TABLE);
    }

    public static class Type implements RecipeType<TailoringTableRecipe>
    {
        private Type() {}

        public static final ResourceLocation ID = Alloygery.asResource("tailoring");
        public static final TailoringTableRecipe.Type INSTANCE = new TailoringTableRecipe.Type();
    }

    public static class Serializer extends AbstractSingleIngredientRecipe.Serializer<TailoringTableRecipe>
    {
        public Serializer(SingleIngredientRecipeMaker<TailoringTableRecipe> factory)
        {
            super(factory);
        }
    }
}
