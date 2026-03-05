package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.AlloygeryMaterialRegistry;
import amorphia.alloygery.gear.material.MaterialHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class GearAlloygeryMaterialProvider extends FabricCodecDataProvider<AlloygeryMaterial>
{
    public GearAlloygeryMaterialProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput, PackOutput.Target.DATA_PACK, "alloygery_material", AlloygeryMaterial.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, AlloygeryMaterial> provider)
    {
        AlloygeryMaterialRegistry.forEach((location, material) -> {
            ResourceLocation trimmed = new ResourceLocation(location.getNamespace(), MaterialHelper.getSimpleName(material));
            provider.accept(trimmed, material);
        });
    }

    @Override
    public String getName()
    {
        return "alloygery_material_datagen";
    }
}
