package amorphia.alloygery.gear.item.part;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.dynamicProviders.IDynamicMaxDamage;
import amorphia.alloygery.gear.item.ArmorStyles;
import amorphia.alloygery.gear.item.GearPartItem;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.property.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class ArmorPart extends GearPartItem implements IDynamicMaxDamage, GearItemTagProvider.IItemTagGen
{
	private final ArmorStyles style;

	public ArmorPart(AlloygeryMaterial material, ArmorStyles style)
	{
		this(new Properties(), material, style);
	}

	public ArmorPart(Properties properties, AlloygeryMaterial material, ArmorStyles style)
	{
		super(properties, material, PartTypes.ARMOR_PLATE);
		this.style = style;
	}

	public ArmorStyles getStyle()
	{
		return style;
	}

	@Override
	public List<Property> getMaterialProperties()
	{
		List<Property> properties = Lists.newArrayList();
		properties.addAll(super.getMaterialProperties());
		getStyle().addStyleProperties(properties);
		return properties;
	}

	@Override
	public int getMaxDamage(ItemStack dynamicGearStack)
	{
		return PropertyHelper.computeIntegerPropertyValueRoundUp(PropertyHelper.getPropertiesOfType(DurabilityProperty.class, getMaterialProperties()), 0, Integer.MAX_VALUE);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate)
	{
		return MaterialHelper.getRepairIngredientForMaterial(getPartMaterial()).test(repairCandidate);
	}

	@Override
	public int getBarColor(ItemStack stack)
	{
		final float durability = (float) getMaxDamage(stack);
		final float f = Math.max(0.0f, (durability - stack.getDamageValue()) / durability);
		return Mth.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
	}

	@Override
	public int getMaterialColor(ItemStack stack, int tintIndex)
	{
		return -1;
	}

	@Override
	public void generateItemModel(ItemModelGenerators itemModelGenerator)
	{
		ResourceLocation base = Alloygery.asResource("template/part/armor/base_armor_part");
		ResourceLocation texture = Alloygery.asResource("template/part/armor/plate_" + style.getName() + "_armor_part_" + MaterialHelper.getSimpleName(getPartMaterial()));
		ModelTemplates.TWO_LAYERED_ITEM.create(ModelLocationUtils.getModelLocation(this), TextureMapping.layered(base, texture), itemModelGenerator.output);
	}

	@Override
	public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
	{
		provider.tagBuilderOf(TagKey.create(Registries.ITEM, Alloygery.asResource("armor_plate_items"))).add(this);
	}
}
