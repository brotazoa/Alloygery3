package amorphia.alloygery.datagen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AlloygeryBlockTagProvider extends FabricTagProvider.BlockTagProvider
{
    private static final List<IAlloygeryBlockTagProvider> BLOCK_TAG_PROVIDERS = Lists.newArrayList();

    public AlloygeryBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture)
    {
        super(output, registriesFuture);
    }

    public static void addProvider(IAlloygeryBlockTagProvider provider)
    {
        AlloygeryBlockTagProvider.BLOCK_TAG_PROVIDERS.add(provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup)
    {
        for(IAlloygeryBlockTagProvider provider : BLOCK_TAG_PROVIDERS)
        {
            provider.addBlockTags(AlloygeryBlockTagProvider.this, lookup);
        }
    }

    public FabricTagBuilder tagBuilderOf(TagKey<Block> tagKey)
    {
        return getOrCreateTagBuilder(tagKey);
    }

    public interface IAlloygeryBlockTagProvider
    {
        void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup);
    }
}
