package amorphia.alloygery.gear.datagen.convert;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.IDynamicGear;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ConvertItemTagProvider extends FabricTagProvider.ItemTagProvider
{
    public ConvertItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture)
    {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup)
    {
        getOrCreateTagBuilder(VanillaItemConverter.VANILLA_ITEM_CONVERTIBLE).add(VanillaItemConverter.VANILLA_CONVERTIBLE_ITEMS);
		getOrCreateTagBuilder(IDynamicGear.UPGRADEABLE_EQUIPMENT).add(VanillaItemConverter.VANILLA_CONVERTIBLE_ITEMS);
		for(Item item : VanillaItemConverter.VANILLA_CONVERTIBLE_ITEMS)
		{
			if (item instanceof ArmorItem armorItem)
			{
				ArmorItem.Type type = armorItem.getType();
				getOrCreateTagBuilder(ArmorBaseItem.ARMOR_ITEMS_TAG).add(item);
				getOrCreateTagBuilder(IDynamicArmor.IMPROVABLE_ARMOR).add(item);
				switch (type)
				{
					case HELMET -> getOrCreateTagBuilder(ArmorBaseItem.HELMET_ITEMS).add(item);
					case CHESTPLATE -> getOrCreateTagBuilder(ArmorBaseItem.CHESTPLATE_ITEMS).add(item);
					case LEGGINGS -> getOrCreateTagBuilder(ArmorBaseItem.LEGGINGS_ITEMS).add(item);
					case BOOTS -> getOrCreateTagBuilder(ArmorBaseItem.BOOTS_ITEMS).add(item);
				}
			}
			else
			{
				getOrCreateTagBuilder(IDynamicTool.IMPROVABLE_TOOL).add(item);
			}
		}
    }
}
