package amorphia.alloygery.gear.datagen.encumbrancePack;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.EncumbranceProperty;
import amorphia.alloygery.gear.property.MobilityProperty;
import amorphia.alloygery.gear.property.PropertyOperation;
import amorphia.alloygery.gear.toolType.ToolType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Locale;
import java.util.function.BiConsumer;

import static amorphia.alloygery.gear.item.PartTypes.*;
import static amorphia.alloygery.gear.property.PropertyOperation.*;

public class EncumbranceDataProviders
{
	public static class PackMetadataProvider extends PackMetadataGenerator
	{
		public PackMetadataProvider(PackOutput output)
		{
			super(output);
			Component description = Component.translatable("pack.alloygery.encumbrance.description");
			add(PackMetadataSection.TYPE, new PackMetadataSection(description, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA)));
		}
	}

	public static class AlloygeryMaterialProvider extends FabricCodecDataProvider<AlloygeryMaterial>
	{
		public AlloygeryMaterialProvider(FabricDataOutput dataOutput)
		{
			super(dataOutput, PackOutput.Target.DATA_PACK, "alloygery_material", AlloygeryMaterial.CODEC);
		}

		@Override
		protected void configure(BiConsumer<ResourceLocation, AlloygeryMaterial> provider)
		{
			provider.accept(Alloygery.asResource("leather_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("leather"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.BASE, 2.0f))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("rabbit_hide_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("rabbit_hide"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
					.property(MobilityProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.BASE, 2.0f))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -3.0f))
					.property(MobilityProperty.of(TOOL_BINDING, ADDITION, 2.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("wool_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("wool"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
					.property(MobilityProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.BASE, 5.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("iron_chain_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("iron"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, ARMOR_BASE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.ARMOR_BASE, PropertyOperation.BASE, 5.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("nickel_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("nickel"))
					.property(EncumbranceProperty.of(PartTypes.TOOL_HEAD, BASE, 5.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("invar_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("invar"))
					.property(EncumbranceProperty.of(PartTypes.TOOL_HEAD, BASE, 10.0f))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, 2.0f))
					.property(EncumbranceProperty.of(TOOL_HANDLE, MULTIPLY_BASE, 1.1f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("constantan_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("constantan"))
					.property(EncumbranceProperty.of(PartTypes.TOOL_HEAD, BASE, 3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("netherite_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("netherite"))
					.property(EncumbranceProperty.of(PartTypes.TOOL_HEAD, BASE, 3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("titanium_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("titanium"))
					.property(MobilityProperty.of(PartTypes.TOOL_HEAD, BASE, 10.0f))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -4.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("titanium_gold_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("titanium_gold"))
					.property(MobilityProperty.of(PartTypes.TOOL_HEAD, BASE, 10.0f))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -2.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("nitinol_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("nitinol"))
					.property(MobilityProperty.of(PartTypes.TOOL_HEAD, BASE, 10.0f))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("string_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("string"))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -2.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("paper_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("paper"))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -2.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("dried_kelp_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("dried_kelp"))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -2.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("phantom_membrane_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("phantom_membrane"))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -4.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("steel_encumbrance"), new MaterialHelper.MaterialBuilder(asAlloygeryMaterial("steel"))
					.property(EncumbranceProperty.of(TOOL_BINDING, ADDITION, -1.0f))
					.setMergeFlag(true)
					.build()
			);
		}

		@Override
		public String getName()
		{
			return "encumbrance_alloygery_material_datagen";
		}

		private static ResourceLocation asAlloygeryMaterial(String name)
		{
			return Alloygery.asResource("alloygery_material/" + name.replace(" ", "_").toLowerCase(Locale.ROOT));
		}
	}

	public static class ToolTypeProvider extends FabricCodecDataProvider<ToolType>
	{
		public ToolTypeProvider(FabricDataOutput dataOutput)
		{
			super(dataOutput, PackOutput.Target.DATA_PACK, "alloygery_tool_types", ToolType.CODEC);
		}

		@Override
		protected void configure(BiConsumer<ResourceLocation, ToolType> provider)
		{
			provider.accept(Alloygery.asResource("hatchet_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("hatchet"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 0.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("knife_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("knife"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 0.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("axe_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("axe"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("hoe_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("hoe"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("pickaxe_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("pickaxe"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("shovel_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("shovel"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("sword_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("sword"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 2.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("excavator_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("excavator"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 5.0f))
					.setMergeFlag(true)
					.build()
			);

			provider.accept(Alloygery.asResource("hammer_encumbrance"), new ToolType.ToolTypeBuilder(asToolType("hammer"))
					.property(AttributeProperty.of(Attributes.MOVEMENT_SPEED, PartTypes.TOOL_TYPE, BASE, 0))
					.property(EncumbranceProperty.of(PartTypes.TOOL_TYPE, PropertyOperation.BASE, 5.0f))
					.setMergeFlag(true)
					.build()
			);
		}

		@Override
		public String getName()
		{
			return "encumbrance_tool_types_datagen";
		}

		private static ResourceLocation asToolType(String name)
		{
			return Alloygery.asResource("alloygery_tool_types/" + name);
		}
	}
}
