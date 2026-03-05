package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.item.ToolHeadPartItem;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.item.ToolTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HoeHead extends ToolHeadPartItem implements GearItemTagProvider.IItemTagGen
{
    public HoeHead(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public HoeHead(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, ToolTypes.HOE);
    }

    public static final TagKey<Item> HOE_HEAD_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("hoe_head_items"));

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(HOE_HEAD_ITEMS_TAG).add(this);
    }
}
