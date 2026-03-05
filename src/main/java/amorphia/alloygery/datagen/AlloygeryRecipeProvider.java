package amorphia.alloygery.datagen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.List;
import java.util.function.Consumer;

public class AlloygeryRecipeProvider extends FabricRecipeProvider
{
    private static final List<IAlloygeryRecipeProvider> recipeProviders = Lists.newArrayList();

    public AlloygeryRecipeProvider(FabricDataOutput output)
    {
        super(output);
    }

    public static void addProvider(IAlloygeryRecipeProvider provider)
    {
        recipeProviders.add(provider);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter)
    {
        for(IAlloygeryRecipeProvider provider : recipeProviders)
        {
            provider.buildRecipes(this, exporter);
        }
    }

    public interface IAlloygeryRecipeProvider
    {
        void buildRecipes(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter);
    }
}
