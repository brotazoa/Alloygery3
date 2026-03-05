package amorphia.alloygery.gear.item;

import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;

import java.util.List;

public interface IDynamicGearPart
{
    AlloygeryMaterial getPartMaterial();

    PartTypes getPartType();

    default List<Property> getMaterialProperties()
    {
        return getPartMaterial().getPropertiesByPart(getPartType());
    }
}
