package amorphia.alloygery.datagen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AlloygeryItemTagProvider extends FabricTagProvider.ItemTagProvider
{
    private static final List<IAlloygeryItemTagProvider> ITEM_TAG_PROVIDERS = Lists.newArrayList();

    public AlloygeryItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture)
    {
        super(output, completableFuture);
    }

    public static void addProvider(IAlloygeryItemTagProvider provider)
    {
        AlloygeryItemTagProvider.ITEM_TAG_PROVIDERS.add(provider);
    }

        @Override
    protected void addTags(HolderLookup.Provider lookup)
    {
        for(IAlloygeryItemTagProvider itemTagProvider : ITEM_TAG_PROVIDERS)
        {
            itemTagProvider.addItemTags(AlloygeryItemTagProvider.this, lookup);
        }
    }

    public FabricTagBuilder tagBuilderOf(TagKey<Item> tagKey)
    {
        return getOrCreateTagBuilder(tagKey);
    }

    public interface IAlloygeryItemTagProvider
    {
        void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup);
    }
}
