package amorphia.alloygery.gear.item.armor;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.client.DynamicGearDescriptionHelper;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.datagen.GearModelProvider;
import amorphia.alloygery.gear.datagen.recipe.ArmorPlatingShapelessRecipeBuilder;
import amorphia.alloygery.gear.datagen.recipe.GearRecipeProvider;
import amorphia.alloygery.gear.item.AlloygeryDefaultArmorMaterial;
import amorphia.alloygery.gear.item.IDynamicArmor;
import amorphia.alloygery.gear.item.IDynamicGear;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ArmorBaseItem extends ArmorItem implements IDynamicArmor, GearModelProvider.GearItemModelGenerator, GearItemTagProvider.IItemTagGen, GearRecipeProvider.IRecipeGen
{
    public static final TagKey<Item> BASE_ARMOR_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("base_armor_items"));
	public static final TagKey<Item> ARMOR_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("armor_items"));
	public static final TagKey<Item> PLATED_ARMOR_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("plated_armor_items"));
	public static final TagKey<Item> HELMET_ITEMS = TagKey.create(Registries.ITEM, Alloygery.asResource("helmet_items"));
	public static final TagKey<Item> CHESTPLATE_ITEMS = TagKey.create(Registries.ITEM, Alloygery.asResource("chestplate_items"));
	public static final TagKey<Item> LEGGINGS_ITEMS = TagKey.create(Registries.ITEM, Alloygery.asResource("leggings_items"));
	public static final TagKey<Item> BOOTS_ITEMS = TagKey.create(Registries.ITEM, Alloygery.asResource("boots_items"));

    public ArmorBaseItem(Type type)
    {
        super(AlloygeryDefaultArmorMaterial.INSTANCE, type, new Properties());
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate)
    {
        return MaterialHelper.getRepairIngredientForMaterial(NBTHelper.hasPartTag(stack, PartTypes.ARMOR_PLATE)
                ? MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_PLATE, stack)
                : MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_BASE, stack)
        ).test(repairCandidate);
    }

    private int calculatedEnchantability = 0;

    @Override
    public void calculateAndCacheEnchantability(ItemStack dynamicGearStack)
    {
        this.calculatedEnchantability = EnchantabilityProperty.compute(dynamicGearStack);
    }

    @Override
    public int getEnchantmentValue()
    {
        return this.calculatedEnchantability;
    }

    @Override
    public Component getName(ItemStack stack)
    {
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced)
    {
        DynamicGearDescriptionHelper.writeDescription(stack, tooltipComponents, isAdvanced);
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        return getItemBarStep(stack);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return getItemBarColor(stack);
    }

    @Override
    public void generateItemModel(ItemModelGenerators itemModelGenerator)
    {
        GearModelProvider.generateArmorModel(this.getDefaultInstance(), itemModelGenerator);
    }

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(ItemTags.TRIMMABLE_ARMOR).add(this);
		provider.tagBuilderOf(IDynamicGear.UPGRADEABLE_EQUIPMENT).add(this);
		provider.tagBuilderOf(IDynamicArmor.IMPROVABLE_ARMOR).add(this);

        if (!NBTHelper.hasPartTag(this.getDefaultInstance(), PartTypes.ARMOR_PLATE))
        {
            provider.tagBuilderOf(BASE_ARMOR_ITEMS_TAG).add(this);
        }

		if (NBTHelper.hasPartTag(this.getDefaultInstance(), PartTypes.ARMOR_PLATE))
		{
			provider.tagBuilderOf(PLATED_ARMOR_ITEMS_TAG).add(this);
		}

		provider.tagBuilderOf(ARMOR_ITEMS_TAG).add(this);

		switch (type)
		{
			case HELMET -> provider.tagBuilderOf(HELMET_ITEMS).add(this);
			case CHESTPLATE -> provider.tagBuilderOf(CHESTPLATE_ITEMS).add(this);
			case LEGGINGS -> provider.tagBuilderOf(LEGGINGS_ITEMS).add(this);
			case BOOTS -> provider.tagBuilderOf(BOOTS_ITEMS).add(this);
		}
    }

	@Override
	public void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
	{
		if (NBTHelper.hasPartTag(this.getDefaultInstance(), PartTypes.ARMOR_PLATE))
		{
			AlloygeryMaterial plateMaterial = MaterialHelper.getMaterialForPartType(PartTypes.ARMOR_PLATE, this.getDefaultInstance());
			String materialName = MaterialHelper.getSimpleName(plateMaterial);
			Ingredient armor = null;
			switch (type)
			{
				case HELMET -> armor = Ingredient.of(HELMET_ITEMS);
				case CHESTPLATE -> armor = Ingredient.of(CHESTPLATE_ITEMS);
				case LEGGINGS -> armor = Ingredient.of(LEGGINGS_ITEMS);
				case BOOTS -> armor = Ingredient.of(BOOTS_ITEMS);
			}
			Ingredient plates = Ingredient.of(
					GearModule.ITEMS.get(materialName + "_plate_armor_part"),
					GearModule.ITEMS.get(materialName + "_scale_armor_part"),
					GearModule.ITEMS.get(materialName + "_heavy_plate_armor_part")
			);

			ArmorPlatingShapelessRecipeBuilder.armorPlating(this, armor, plates)
					.unlockedBy("has_plate", RecipeProvider.has(GearModule.ITEMS.get(materialName + "_plate_armor_part")))
					.unlockedBy("has_scale", RecipeProvider.has(GearModule.ITEMS.get(materialName + "_scale_armor_part")))
					.unlockedBy("has_heavy_plate", RecipeProvider.has(GearModule.ITEMS.get(materialName + "_heavy_plate_armor_part")))
					.save(exporter);
		}
	}
}
