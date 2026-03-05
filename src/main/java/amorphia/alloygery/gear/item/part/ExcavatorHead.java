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

public class ExcavatorHead extends ToolHeadPartItem implements GearItemTagProvider.IItemTagGen
{
    public static final TagKey<Item> EXCAVATOR_HEAD_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("excavator_head_items"));

    public ExcavatorHead(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public ExcavatorHead(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, ToolTypes.EXCAVATOR);
    }

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(EXCAVATOR_HEAD_ITEMS_TAG).add(this);
    }
}
