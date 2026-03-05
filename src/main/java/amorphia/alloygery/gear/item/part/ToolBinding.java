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

public class ToolBinding extends GearPartItem implements GearItemTagProvider.IItemTagGen
{
    public ToolBinding(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public ToolBinding(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, PartTypes.TOOL_BINDING);
    }

    public static final TagKey<Item> TOOL_BINDING_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("tool_binding_items"));

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(TOOL_BINDING_ITEMS_TAG).add(this);
    }
}
