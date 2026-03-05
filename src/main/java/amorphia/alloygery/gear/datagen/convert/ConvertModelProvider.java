package amorphia.alloygery.gear.datagen.convert;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.item.ArmorStyles;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.IDynamicTool;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.NBTHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.Optional;

public class ConvertModelProvider extends FabricModelProvider
{
    public ConvertModelProvider(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator)
    {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator)
    {
		generateModelsFor(itemModelGenerator, VanillaItemConverter.VANILLA_CONVERTIBLE_ITEMS);
    }

	private void generateModelsFor(ItemModelGenerators itemModelGenerators, Item... items)
	{
		for (Item item : items)
		{
			if(item instanceof ArmorItem)
			{
				generateArmorModelsFor(itemModelGenerators, item);
			}
			else if (item instanceof SwordItem)
			{
				generateSwordModelsFor(itemModelGenerators, item);
			}
			else
			{
				generateToolModelsFor(itemModelGenerators, item);
			}
		}
	}

	private void generateArmorModelsFor(ItemModelGenerators itemModelGenerators, Item... items)
	{
		for(Item item : items)
		{
			ItemStack convert = VanillaItemConverter.convert(item.getDefaultInstance(), true);
			if (convert.getItem() instanceof ArmorBaseItem armorItem)
			{
				ArmorItem.Type armorType = armorItem.getType();
				ResourceLocation typeTexture = Alloygery.asResource("template/render_part/armor/base_" + armorType.getName());
				ArmorStyles baseStyle = NBTHelper.getArmorStyleFromStack(convert, PartTypes.ARMOR_BASE);
				ResourceLocation baseTexture = Alloygery.asResource("template/render_part/armor/base_" + armorType.getName() + "_" + baseStyle.getName());

				if(NBTHelper.hasPartTag(convert, PartTypes.ARMOR_PLATE))
				{
					AlloygeryMaterial plateMaterial = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_PLATE, convert);
					ResourceLocation plateTexture = Alloygery.asResource("template/render_part/armor/plate_" + armorType.getName() + "_" + MaterialHelper.getSimpleName(plateMaterial));
					ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
					ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);

					template.create(
							modelLocation,
							new TextureMapping().putForced(TextureSlot.LAYER0, typeTexture).putForced(TextureSlot.LAYER1, baseTexture).putForced(TextureSlot.LAYER2, plateTexture),
							itemModelGenerators.output
					);
				}
				else
				{
					ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
					ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);

					template.create(
							modelLocation,
							new TextureMapping().putForced(TextureSlot.LAYER0, typeTexture).putForced(TextureSlot.LAYER1, baseTexture),
							itemModelGenerators.output
					);
				}
			}
		}
	}

    private void generateToolModelsFor(ItemModelGenerators itemModelGenerator, Item... items)
    {
        for(Item item : items)
        {
            ItemStack convert = VanillaItemConverter.convert(item.getDefaultInstance(), true);
            if (convert.getItem() instanceof IDynamicTool dynamicTool)
            {
                AlloygeryMaterial headMaterial = MaterialHelper.getMaterialForPartType(PartTypes.TOOL_HEAD, convert);
                ResourceLocation headTexture = Alloygery.asResource("template/render_part/tool_head/" + dynamicTool.getToolType().getName() + "_template_" + MaterialHelper.getSimpleName(headMaterial));
                ResourceLocation handleTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_handle_template");
                ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
                ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);

                template.create(
                        modelLocation,
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture),
                        itemModelGenerator.output
                );
            }
        }
    }

    private void generateSwordModelsFor(ItemModelGenerators itemModelGenerator, Item... items)
    {
        for(Item item : items)
        {
            ItemStack convert = VanillaItemConverter.convert(item.getDefaultInstance(), true);
            if (convert.getItem() instanceof IDynamicTool dynamicTool)
            {
                AlloygeryMaterial headMaterial = MaterialHelper.getMaterialForPartType(PartTypes.TOOL_HEAD, convert);
                ResourceLocation headTexture = Alloygery.asResource("template/render_part/tool_head/" + dynamicTool.getToolType().getName() + "_template_" + MaterialHelper.getSimpleName(headMaterial));
                ResourceLocation handleTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_handle_template");
                ResourceLocation bindingTexture = Alloygery.asResource("template/render_part/tool_part/" + dynamicTool.getToolType().getName() + "_guard_template");
                ModelTemplate template = new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/handheld")), Optional.empty());
                ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);

                template.create(
                        modelLocation,
                        new TextureMapping().putForced(TextureSlot.LAYER0, handleTexture).putForced(TextureSlot.LAYER1, headTexture).putForced(TextureSlot.LAYER2, bindingTexture),
                        itemModelGenerator.output
                );
            }
        }
    }
}
