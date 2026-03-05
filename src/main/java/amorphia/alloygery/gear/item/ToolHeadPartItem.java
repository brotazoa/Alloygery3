package amorphia.alloygery.gear.item;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.dynamicProviders.IDynamicMaxDamage;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.property.DurabilityProperty;
import amorphia.alloygery.gear.property.Property;
import amorphia.alloygery.gear.property.PropertyHelper;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class ToolHeadPartItem extends GearPartItem implements IDynamicMaxDamage
{
    private final ToolTypes toolType;

    public ToolHeadPartItem(Properties properties, AlloygeryMaterial material, ToolTypes toolType)
    {
        super(properties, material, PartTypes.TOOL_HEAD);
        this.toolType = toolType;
    }

    public ToolHeadPartItem(AlloygeryMaterial material, ToolTypes toolType)
    {
        this(new Properties(), material, toolType);
    }

    public ToolTypes getToolType()
    {
        return toolType;
    }

    @Override
    public void generateItemModel(ItemModelGenerators itemModelGenerator)
    {
        ResourceLocation textureTemplate = Alloygery.asResource("template/part/tool_head/" + toolType.getName() + "_template_" + MaterialHelper.getSimpleName(getPartMaterial()));
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(this), TextureMapping.layer0(textureTemplate), itemModelGenerator.output);
    }

    @Override
    public int getMaterialColor(ItemStack stack, int tintIndex)
    {
        return -1;
    }

    @Override
    public int getMaxDamage(ItemStack dynamicGearStack)
    {
        return PropertyHelper.computeIntegerPropertyValueRoundUp(PropertyHelper.getPropertiesOfType(DurabilityProperty.class, getMaterialProperties()), 0, Integer.MAX_VALUE);
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        return Math.round(13.0f - (float) stack.getDamageValue() * 13.0f / (float) getMaxDamage(stack));
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        final float durability = (float) getMaxDamage(stack);
        final float f = Math.max(0.0f, (durability - stack.getDamageValue()) / durability);
        return Mth.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate)
    {
        return MaterialHelper.getRepairIngredientForMaterial(getPartMaterial()).test(repairCandidate);
    }
}
