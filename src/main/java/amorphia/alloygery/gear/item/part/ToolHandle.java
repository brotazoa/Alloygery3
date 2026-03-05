package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.item.GearPartItem;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ToolHandle extends GearPartItem implements GearItemTagProvider.IItemTagGen
{
    public ToolHandle(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public ToolHandle(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, PartTypes.TOOL_HANDLE);
    }

    public static final TagKey<Item> TOOL_HANDLE_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("tool_handle_items"));

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(TOOL_HANDLE_ITEMS_TAG).add(this);
    }
}
