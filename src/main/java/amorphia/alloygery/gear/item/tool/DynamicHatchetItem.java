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
import amorphia.alloygery.gear.nbt.AlloygeryNBTKeys;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.gear.property.AttributeProperty;
import amorphia.alloygery.gear.property.EnchantabilityProperty;
import amorphia.alloygery.gear.property.Property;
import amorphia.alloygery.gear.property.PropertyOperation;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DynamicHatchetItem extends DiggerItem implements IDynamicTool, GearModelProvider.GearItemModelGenerator, GearItemTagProvider.IItemTagGen, GearRecipeProvider.IRecipeGen
{
    public static final TagKey<Item> DYNAMIC_HATCHET_ITEMS_TAG = TagKey.create(Registries.ITEM, Alloygery.asResource("dynamic_hatchet_items"));

    public DynamicHatchetItem()
    {
        this(new Properties());
    }

    public DynamicHatchetItem(Properties properties)
    {
        super(0.0f, 0.0f, AlloygeryDefaultTier.INSTANCE, BlockTags.MINEABLE_WITH_AXE, properties);
    }

    @Override
    public void addItemTags(AlloygeryItemTagProvider provider, HolderLookup.Provider lookup)
    {
        provider.tagBuilderOf(DYNAMIC_HATCHET_ITEMS_TAG).add(this);
		provider.tagBuilderOf(IDynamicTool.UPGRADEABLE_EQUIPMENT).add(this);
        provider.tagBuilderOf(IDynamicTool.IMPROVABLE_TOOL).add(this);
    }

    @Override
    public void generateItemModel(ItemModelGenerators itemModelGenerator)
    {
        GearModelProvider.generateBasicToolModel(this.getDefaultInstance(), itemModelGenerator);
    }

    @Override
    public void addBaseToolProperties(List<Property> properties)
    {
        properties.add(AttributeProperty.of(Attributes.ATTACK_DAMAGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, 3.0f));
        properties.add(AttributeProperty.of(Attributes.ATTACK_SPEED, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.6f));
        properties.add(AttributeProperty.of(ReachEntityAttributes.ATTACK_RANGE, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.0f, true));
        properties.add(AttributeProperty.of(ReachEntityAttributes.REACH, PartTypes.TOOL_TYPE, PropertyOperation.BASE, -2.0f, true));
    }

    @Override
    public ToolTypes getToolType()
    {
        return ToolTypes.HATCHET;
    }

    @Override
    public PartTypes[] getParts()
    {
        return new PartTypes[]{
                PartTypes.TOOL_HEAD,
                PartTypes.TOOL_HANDLE,
                PartTypes.TOOL_IMPROVEMENT
        };
    }

    @Override
    public boolean isEffectiveOn(BlockState targetBlockState, ItemStack dynamicGearStack)
    {
        return !isBroken(dynamicGearStack) && targetBlockState.is(BlockTags.MINEABLE_WITH_AXE);
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
    public int getBarWidth(@NotNull ItemStack stack)
    {
        return getItemBarStep(stack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack)
    {
        return getItemBarColor(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced)
    {
        DynamicGearDescriptionHelper.writeDescription(stack, tooltipComponents, isAdvanced);
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate)
    {
        return MaterialHelper.getRepairIngredientForMaterial(MaterialHelper.getMaterialForPartType(PartTypes.TOOL_HEAD, stack)).test(repairCandidate);
    }

    @Override
    public void generateRecipe(AlloygeryRecipeProvider provider, Consumer<FinishedRecipe> exporter)
    {
        GearRecipeProvider.generateBasicToolShapedRecipe(this, exporter);
    }

	@Override
	public int getMaterialColor(ItemStack stack, int tintIndex)
	{
		if(stack == null || stack.isEmpty() || !(stack.getItem() instanceof IDynamicTool))
			return -1;

		if(!NBTHelper.hasAlloygeryTag(stack))
		{
			stack = stack.copy();
			stack.getOrCreateTag().put(AlloygeryNBTKeys.NBT, NBTHelper.getAlloygeryDataTag(stack.getItem().getDefaultInstance()));
		}

		return switch (tintIndex)
		{
			case 0 -> getColorForPart(stack, PartTypes.TOOL_HANDLE);
			// case 1 -> -1;
			case 2 -> NBTHelper.isUpgraded(stack) ? getColorForPart(stack, PartTypes.TOOL_UPGRADE) : getColorForPart(stack, PartTypes.TOOL_IMPROVEMENT);
			case 3 -> getColorForPart(stack, PartTypes.TOOL_IMPROVEMENT);
			default -> -1;
		};
	}
}
