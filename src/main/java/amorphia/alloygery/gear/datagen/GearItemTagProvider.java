package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.gear.GearModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

public class GearItemTagProvider implements AlloygeryItemTagProvider.IAlloygeryItemTagProvider
{
    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        for(Item item : GearModule.ITEMS.values())
        {
            if (item instanceof IItemTagGen itemWithTagGen)
            {
                itemWithTagGen.addItemTags(provider, lookup);
            }
        }
    }

    public interface IItemTagGen
    {
        void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup);
    }
}
