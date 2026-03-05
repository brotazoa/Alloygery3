package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.item.GearPartItem;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HandGuard extends GearPartItem implements GearItemTagProvider.IItemTagGen
{
    public HandGuard(AlloygeryMaterial material)
    {
        this(new Properties(), material);
    }

    public HandGuard(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material, PartTypes.TOOL_BINDING);
    }

    @Override
    public void generateItemModel(ItemModelGenerators itemModelGenerator)
    {
        ResourceLocation textureTemplate = Alloygery.asResource("template/part/tool_guard_template");
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(this), TextureMapping.layer0(textureTemplate), itemModelGenerator.output);
    }

    public static final TagKey<Item> HAND_GUARD_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("hand_guard_items"));

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(HAND_GUARD_ITEMS_TAG).add(this);
    }
}
