package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.gear.armorStyle.ArmorStyle;
import amorphia.alloygery.gear.armorStyle.ArmorStyleRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class GearArmorStyleProvider extends FabricCodecDataProvider<ArmorStyle>
{
	public GearArmorStyleProvider(FabricDataOutput dataOutput)
	{
		super(dataOutput, PackOutput.Target.DATA_PACK, "alloygery_armor_styles", ArmorStyle.CODEC);
	}

	@Override
	protected void configure(BiConsumer<ResourceLocation, ArmorStyle> provider)
	{
		ArmorStyleRegistry.forEach((resourceLocation, style) -> {
			ResourceLocation trimmed = new ResourceLocation(resourceLocation.getNamespace(), style.getSimpleName());
			provider.accept(trimmed, style);
		});
	}

	@Override
	public String getName()
	{
		return "alloygery_armor_styles_datagen";
	}
}
