package amorphia.alloygery.gear.dynamicProviders;

import amorphia.alloygery.gear.item.PartTypes;

public interface IMadeFromParts
{
    PartTypes[] getParts();

    PartTypes getPrimaryPart();
}
