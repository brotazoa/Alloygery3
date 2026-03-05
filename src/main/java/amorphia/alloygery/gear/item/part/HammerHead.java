package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.item.ToolHeadPartItem;
import amorphia.alloygery.gear.item.ToolTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HammerHead extends ToolHeadPartItem implements GearItemTagProvider.IItemTagGen
{
    public static final TagKey<Item> HAMMER_HEAD_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("hammer_head_items"));

    public HammerHead(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public HammerHead(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, ToolTypes.HAMMER);
    }

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(HAMMER_HEAD_ITEMS_TAG).add(this);
    }
}
