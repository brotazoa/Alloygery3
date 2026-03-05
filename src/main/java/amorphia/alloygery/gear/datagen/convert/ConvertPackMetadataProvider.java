package amorphia.alloygery.gear.datagen.convert;

import net.minecraft.DetectedVersion;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

public class ConvertPackMetadataProvider extends PackMetadataGenerator
{
	public ConvertPackMetadataProvider(PackOutput output)
	{
		super(output);
		Component description = Component.translatable("pack.alloygery.convert_vanilla_gear.description");
		add(PackMetadataSection.TYPE, new PackMetadataSection(description, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA)));
	}
}
