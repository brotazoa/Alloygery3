package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryBlockTagProvider;
import amorphia.alloygery.gear.item.tool.DynamicKnifeItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;

public class GearBlockTagProvider implements AlloygeryBlockTagProvider.IAlloygeryBlockTagProvider
{
    @Override
    public void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(DynamicKnifeItem.MINEABLE_WITH_KNIFE).add(
                Blocks.HAY_BLOCK,
                Blocks.DRIED_KELP_BLOCK
        ).forceAddTag(BlockTags.SWORD_EFFICIENT).addOptionalTag(TagKey.create(Registries.BLOCK, Alloygery.asCommonResource("mineable/knife")));
    }

    public interface IBlockTagGen
    {
        void addBlockTags(AlloygeryBlockTagProvider provider, HolderLookup.Provider lookup);
    }
}
