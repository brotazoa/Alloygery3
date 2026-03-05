package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class DyeableToolBinding extends ToolBinding implements DyeableLeatherItem, GearItemTagProvider.IItemTagGen
{
    public DyeableToolBinding(Properties properties, AlloygeryMaterial material)
    {
        super(properties, material);
    }

    public DyeableToolBinding(AlloygeryMaterial material)
    {
        super(material);
    }

    @Override
    public int getMaterialColor(ItemStack stack, int tintIndex)
    {
        return tintIndex == 0 ? getColor(stack) : -1;
    }
}
