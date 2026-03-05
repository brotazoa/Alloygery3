package amorphia.alloygery.gear.item.tool;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.datagen.AlloygeryItemTagProvider;
import amorphia.alloygery.datagen.AlloygeryRecipeProvider;
import amorphia.alloygery.gear.client.DynamicGearDescriptionHelper;
import amorphia.alloygery.gear.datagen.GearItemTagProvider;
import amorphia.alloygery.gear.datagen.GearModelProvider;
import amorphia.alloygery.gear.datagen.recipe.GearRecipeProvider;
import amorphia.alloygery.gear.item.*;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.EnchantabilityProperty;
import amorphia.alloygery.gear.property.Property;
import amorphia.alloygery.gear.property.PropertyOperation;
import dev.draylar.magna.api.MagnaTool;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DynamicExcavatorItem extends ShovelItem implements IDynamicTool, MagnaTool, GearModelProvider.GearItemModelGenerator, GearItemTagProvider.IItemTagGen, GearRecipeProvider.IRecipeGen
{
    public static final TagKey<Item> DYNAMIC_EXCAVATOR_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("dynamic_excavator_items"));

    public DynamicExcavatorItem()
    {
        this(new Properties());
    }

    public DynamicExcavatorItem(Properties properties)
    {
        super(AlloygeryDefaultTier.INSTANCE, 0.0f, 0.0f, properties);
    }

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(DYNAMIC_EXCAVATOR_ITEMS_TAG).add(this);
		provider.tagBuilderOf(IDynamicTool.UPGRADEABLE_EQUIPMENT).add(this);
        provider.tagBuilderOf(IDynamicTool.IMPROVABLE_TOOL).add(this);
    }

    @Override
    public void generateItemModel(ItemModelGenerators itemModelGenerator)
    {
        GearModelProvider.generateToolModel(this.getDefaultInstance(), itemModelGenerator);
    }

    @Override
    public void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
    {
        GearRecipeProvider.generateToolShapedRecipe(this, exporter);
    }

    @Override
    public void addBaseToolProperties(List<Property> properties)
    {
        properties.add(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 2.0f));
        properties.add(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -3.0f));
    }

    @Override
    public ToolTypes getToolType()
    {
        return ToolTypes.EXCAVATOR;
    }

    @Override
    public boolean isEffectiveOn(BlockState targetBlockState, ItemStack dynamicGearStack)
    {
        return !isBroken(dynamicGearStack) && targetBlockState.is(BlockTags.MINEABLE_WITH_SHOVEL);
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
    public int getRadius(ItemStack stack)
    {
        return 1;
    }

    @Override
    public boolean playBreakEffects()
    {
        return false;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate)
    {
        return MaterialHelper.getRepairIngredientForMaterial(MaterialHelper.getMaterialForPartType(this.getPrimaryPart(), stack)).test(repairCandidate);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced)
    {
        DynamicGearDescriptionHelper.writeDescription(stack, tooltipComponents, isAdvanced);
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack)
    {
        return getItemBarStep(stack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack)
    {
        return getItemBarColor(stack);
    }
}
