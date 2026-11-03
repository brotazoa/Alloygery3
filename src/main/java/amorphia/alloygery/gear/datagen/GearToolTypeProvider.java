package amorphia.alloygery.gear.datagen;

import amorphia.alloygery.gear.toolType.ToolType;
import amorphia.alloygery.gear.toolType.ToolTypeRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class GearToolTypeProvider extends FabricCodecDataProvider<ToolType>
{
	public GearToolTypeProvider(FabricDataOutput dataOutput)
	{
		super(dataOutput, PackOutput.Target.DATA_PACK, "alloygery_tool_types", ToolType.CODEC);
	}

	@Override
	protected void configure(BiConsumer<ResourceLocation, ToolType> provider)
	{
		ToolTypeRegistry.forEach((resourceLocation, toolType) -> {
			ResourceLocation trimmed = new ResourceLocation(resourceLocation.getNamespace(), toolType.getSimpleName());
			provider.accept(trimmed, toolType);
		});
	}

	@Override
	public String getName()
	{
		return "alloygery_tool_types_datagen";
	}
}
