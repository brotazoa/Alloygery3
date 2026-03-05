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

public class SwordBlade extends ToolHeadPartItem implements GearItemTagProvider.IItemTagGen
{
    public SwordBlade(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public SwordBlade(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, ToolTypes.SWORD);
    }

    public static final TagKey<Item> SWORD_BLADE_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("sword_blade_items"));

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(SWORD_BLADE_ITEMS_TAG).add(this);
    }
}
