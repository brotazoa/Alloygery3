package amorphia.alloygery.gear.nbt;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.dynamicProviders.IMadeFromParts;
import amorphia.alloygery.gear.item.*;
import amorphia.alloygery.gear.item.armor.ArmorBaseItem;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.AlloygeryMaterialRegistry;
import amorphia.alloygery.gear.property.Property;
import com.google.common.collect.Lists;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static amorphia.alloygery.gear.nbt.AlloygeryNBTKeys.*;

public class NBTHelper
{
    public static CompoundTag createAlloygeryDataTag(Item gearItem, ListTag partsListTag, CompoundTag... partsTags)
    {
        CompoundTag alloygeryTag = new CompoundTag();
        alloygeryTag.putString(ITEM_IDENTIFIER, BuiltInRegistries.ITEM.getKey(gearItem).toString());
        alloygeryTag.put(PART_LIST, partsListTag);
        for(CompoundTag partTag : partsTags)
        {
            if (partTag.contains(PART_IDENTIFIER))
            {
                alloygeryTag.put(partTag.getString(PART_IDENTIFIER), partTag);
            }
        }
        return alloygeryTag;
    }

    public static ListTag createPartsListTag(ItemStack itemStack)
    {
		return itemStack.getItem() instanceof IMadeFromParts madeFromParts ? createPartsListTag(madeFromParts.getParts()) : createPartsListTag(new PartTypes[]{});
    }

    public static ListTag createPartsListTag(PartTypes[] parts)
    {
        ListTag listTag = new ListTag();
        for(PartTypes part : parts)
            listTag.add(StringTag.valueOf(part.getName()));

        return listTag;
    }

	public static ListTag createPartsListTag(List<PartTypes> parts)
	{
		ListTag listTag = new ListTag();
		for(PartTypes part : parts)
			listTag.add(StringTag.valueOf(part.getName()));

		return listTag;
	}

	public static ListTag recreatePartsListTag(ItemStack gearStack)
	{
		ListTag listTag = new ListTag();
		for(PartTypes part : PartTypes.VALUES_CACHE)
		{
			if (hasPartTag(gearStack, part))
			{
				listTag.add(StringTag.valueOf(part.getName()));
			}
		}
		return listTag;
	}

    public static CompoundTag createPartTag(GearPartItem partItem)
    {
        return createPartTag(partItem, partItem.getPartType(), partItem.getPartMaterial());
    }

    public static CompoundTag createPartTag(Item partItem, PartTypes partType, AlloygeryMaterial material)
    {
        return createPartTag(partItem.getDefaultInstance(), partType, material);
    }

    public static CompoundTag createPartTag(ItemStack partStack, PartTypes partType, AlloygeryMaterial material)
    {
        CompoundTag partTag = new CompoundTag();
        partTag.putString(TYPE, PART_IDENTIFIER);
        partTag.putString(PART_IDENTIFIER, partType.getName());
        partTag.putString(ITEM_IDENTIFIER, BuiltInRegistries.ITEM.getKey(partStack.getItem()).toString());
        partTag.putString(MATERIAL_IDENTIFIER, material.getIdentifierString());
        if (partStack.getItem() instanceof DyeableLeatherItem dyeableLeatherItem)
        {
            partTag.putInt(DYE_COLOR, dyeableLeatherItem.getColor(partStack));
        }
        return partTag;
    }

    public static CompoundTag createPartTag(PartTypes partType, AlloygeryMaterial material)
    {
        CompoundTag partTag = new CompoundTag();
        partTag.putString(TYPE, PART_IDENTIFIER);
        partTag.putString(PART_IDENTIFIER, partType.getName());
        partTag.putString(MATERIAL_IDENTIFIER, material.getIdentifierString());
        return partTag;
    }

    public static CompoundTag createArmorPartTagWithStyle(PartTypes partType, ArmorStyles armorStyle, AlloygeryMaterial material)
    {
        CompoundTag partTag = createPartTag(partType, material);
        partTag.putString(ARMOR_STYLE_IDENTIFIER, armorStyle.getName());
        return partTag;
    }

	public static CompoundTag createArmorPartTagWithStyle(Item partStack, PartTypes partType, ArmorStyles armorStyle, AlloygeryMaterial material)
	{
		CompoundTag partTag = createPartTag(partStack, partType, material);
		partTag.putString(ARMOR_STYLE_IDENTIFIER, armorStyle.getName());
		return partTag;
	}

    public static CompoundTag createImprovementPartTag(ItemStack improvementItem, PartTypes partType, ImprovementTypes improvementType, AlloygeryMaterial material)
    {
        CompoundTag partTag = createImprovementPartTag(partType, improvementType, material);
        if (improvementItem.getItem() instanceof DyeableLeatherItem dyeableLeatherItem)
        {
            partTag.putInt(DYE_COLOR, dyeableLeatherItem.getColor(improvementItem));
        }
        return partTag;
    }

    public static CompoundTag createImprovementPartTag(PartTypes partType, ImprovementTypes improvementType, AlloygeryMaterial material)
    {
        CompoundTag partTag = createPartTag(partType, material);
        partTag.putString(IMPROVEMENT_TYPE_IDENTIFIER, improvementType.getName());
        return partTag;
    }

    public static CompoundTag createToolUpgradePartTag(AlloygeryMaterial material)
    {
        CompoundTag partTag = createPartTag(PartTypes.TOOL_UPGRADE, material);
        // TODO: hard coded netherite upgrade type
        partTag.putString(UPGRADE_TYPE_IDENTIFIER, UpgradeTypes.NETHERITE.getName());
		return partTag;
    }

    public static CompoundTag createArmorUpgradePartTag(AlloygeryMaterial material)
    {
        CompoundTag partTag = createPartTag(PartTypes.ARMOR_UPGRADE, material);
        // TODO: hard coded netherite upgrade type
        partTag.putString(UPGRADE_TYPE_IDENTIFIER, UpgradeTypes.NETHERITE.getName());
        return partTag;
    }

    public static CompoundTag getAlloygeryDataTag(CompoundTag rootTag)
    {
        return rootTag == null ? new CompoundTag() : rootTag.getCompound(NBT);
    }

    public static CompoundTag getAlloygeryDataTag(ItemStack itemStack)
    {
        return itemStack == null ? new CompoundTag() : getAlloygeryDataTag(itemStack.getTag());
    }

    public static ResourceLocation getItemIdentifierFromTag(CompoundTag tag)
    {
        if(tag == null)
            return BuiltInRegistries.ITEM.getDefaultKey();

        final ResourceLocation identifier = ResourceLocation.tryParse(tag.getString(ITEM_IDENTIFIER));

        return identifier == null ? BuiltInRegistries.ITEM.getDefaultKey() : identifier;
    }

    public static ResourceLocation getMaterialIdentifierFromTag(CompoundTag tag)
    {
        if(tag == null)
            return AlloygeryMaterialRegistry.getDefaultKey();

        final ResourceLocation identifier = ResourceLocation.tryParse(tag.getString(MATERIAL_IDENTIFIER));

        return identifier == null ? AlloygeryMaterialRegistry.getDefaultKey() : identifier;
    }

    public static List<PartTypes> getPartTypesListFromTag(CompoundTag tag)
    {
        if (tag != null && tag.contains(PART_LIST))
        {
            ListTag listTag = tag.getList(PART_LIST, Tag.TAG_STRING);
            List<PartTypes> partTypesList = Lists.newArrayList();
            for(int i = 0; i < listTag.size(); i++)
            {
                partTypesList.add(PartTypes.getByName(listTag.getString(i)));
            }
            return partTypesList;
        }
        return List.of();
    }

    public static List<PartTypes> getPartTypesListFromItemStack(ItemStack itemStack)
    {
		return getPartTypesListFromTag(getAlloygeryDataTag(itemStack));
    }

    public static CompoundTag getPartTagFromItemStack(ItemStack stack, PartTypes partType)
    {
        return hasPartTag(stack, partType) ? getPartTagFromTag(partType, getAlloygeryDataTag(stack)) : new CompoundTag();
    }

    public static CompoundTag getPartTagFromTag(PartTypes partType, CompoundTag tag)
    {
        return tag != null && tag.contains(partType.getName()) ? tag.getCompound(partType.getName()) : new CompoundTag();
    }

    public static PartTypes getPartTypeFromTag(CompoundTag tag)
    {
        return tag == null ? null : PartTypes.getByName(tag.getString(PART_IDENTIFIER));
    }

    public static UpgradeTypes getUpgradeType(ItemStack gearStack)
    {
        return getUpgradeTypeFromTag(
                gearStack.getItem() instanceof IDynamicTool ? getPartTagFromItemStack(gearStack, PartTypes.TOOL_UPGRADE)
                        : gearStack.getItem() instanceof IDynamicArmor ? getPartTagFromItemStack(gearStack, PartTypes.ARMOR_UPGRADE)
                                : null
        );
    }

    public static UpgradeTypes getUpgradeTypeFromTag(CompoundTag tag)
    {
        return tag == null ? null : UpgradeTypes.getByName(tag.getString(UPGRADE_TYPE_IDENTIFIER));
    }

	public static ImprovementTypes getImprovementType(ItemStack gearStack)
	{
		return getImprovementTypeFromTag(
				gearStack.getItem() instanceof IDynamicTool ? getPartTagFromItemStack(gearStack, PartTypes.TOOL_IMPROVEMENT)
						: gearStack.getItem() instanceof IDynamicArmor ? getPartTagFromItemStack(gearStack, PartTypes.ARMOR_IMPROVEMENT)
								: null
		);
	}

    public static ImprovementTypes getImprovementTypeFromTag(CompoundTag tag)
    {
        return tag == null ? null : ImprovementTypes.getByName(tag.getString(IMPROVEMENT_TYPE_IDENTIFIER));
    }

    public static List<Property> getNBTPropertiesFromItemStack(ItemStack itemStack)
    {
        return getNBTPropertiesFromTag(getAlloygeryDataTag(itemStack));
    }

    public static List<Property> getNBTPropertiesFromTag(CompoundTag tag)
    {
        if(tag != null && tag.contains(PROPERTY_LIST))
        {
            ListTag listTag = tag.getList(PROPERTY_LIST, Tag.TAG_COMPOUND);
            List<Property> properties = Lists.newArrayList();

            listTag.forEach(t -> {
                DataResult<Property> result = Property.CODEC.parse(NbtOps.INSTANCE, t);
                Property p = result.getOrThrow(false, Alloygery.LOGGER::error);
                properties.add(p);
            });

            return properties;
        }
        return List.of();
    }

    public static void setNBTProperties(ItemStack itemStack, List<Property> properties)
    {
        if(!hasAlloygeryTag(itemStack))
            return;

        CompoundTag dataTag = getAlloygeryDataTag(itemStack);

        ListTag propertiesListTag = new ListTag();
		if (properties != null)
		{
			for(Property p : properties)
			{
				DataResult<Tag> result = Property.CODEC.encodeStart(NbtOps.INSTANCE, p);
				Tag propertyTag = result.getOrThrow(false, Alloygery.LOGGER::error);
				propertiesListTag.add(propertyTag);
			}
		}

        dataTag.put(PROPERTY_LIST, propertiesListTag);
    }

    public static int getPartDyeColorFromStack(ItemStack stack, PartTypes partType)
    {
        return partHasDyeColor(stack, partType) ? getPartTagFromItemStack(stack, partType).getInt(DYE_COLOR) : -1;
    }

    public static ArmorStyles getArmorStyleFromStack(ItemStack stack, PartTypes partType)
    {
        return getArmorStyleFromTag(getPartTagFromItemStack(stack, partType));
    }

    public static ArmorStyles getArmorStyleFromTag(CompoundTag tag)
    {
        return ArmorStyles.getByName(tag.getString(ARMOR_STYLE_IDENTIFIER));
    }

    public static boolean hasAlloygeryTag(ItemStack stack)
    {
        return stack != null && stack.getTag() != null && stack.getTag().contains(NBT);
    }

    public static boolean hasPartTag(ItemStack stack, PartTypes partType)
    {
        return hasAlloygeryTag(stack) && getAlloygeryDataTag(stack).contains(partType.getName());
    }

    public static boolean hasNBTProperties(ItemStack stack)
    {
        return hasAlloygeryTag(stack) && getAlloygeryDataTag(stack).contains(PROPERTY_LIST);
    }

    public static boolean partHasDyeColor(ItemStack stack, PartTypes partType)
    {
        return hasPartTag(stack, partType) && getPartTagFromItemStack(stack, partType).contains(DYE_COLOR);
    }

    public static boolean partHasArmorStyle(ItemStack stack, PartTypes partType)
    {
        return hasPartTag(stack, partType) && getPartTagFromItemStack(stack, partType).contains(ARMOR_STYLE_IDENTIFIER);
    }

	public static boolean isImproved(ItemStack stack)
	{
		return hasPartTag(stack, PartTypes.TOOL_IMPROVEMENT) || hasPartTag(stack, PartTypes.ARMOR_IMPROVEMENT) || hasPartTag(stack, PartTypes.BOW_IMPROVEMENT) || hasPartTag(stack, PartTypes.SHIELD_IMPROVEMENT);
	}

    public static boolean isUpgraded(ItemStack stack)
    {
        return hasPartTag(stack, PartTypes.TOOL_UPGRADE) || hasPartTag(stack, PartTypes.ARMOR_UPGRADE) || hasPartTag(stack, PartTypes.BOW_UPGRADE) || hasPartTag(stack, PartTypes.SHIELD_UPGRADE);
    }
}
