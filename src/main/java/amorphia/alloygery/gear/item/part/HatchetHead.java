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

public class HatchetHead extends ToolHeadPartItem implements GearItemTagProvider.IItemTagGen
{
    public HatchetHead(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public HatchetHead(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, ToolTypes.HATCHET);
    }

    public static final TagKey<Item> HATCHET_HEAD_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("hatchet_head_items"));

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(HATCHET_HEAD_ITEMS_TAG).add(this);
    }
}
