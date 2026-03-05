package amorphia.alloygery.gear.item;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.client.DynamicGearClientReloadListener;
import amorphia.alloygery.gear.client.DynamicGearDescriptionHelper;
import amorphia.alloygery.gear.datagen.GearModelProvider;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.MaterialHelper;
import amorphia.alloygery.gear.property.Property;
import amorphia.alloygery.gear.property.PropertyHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public abstract class GearPartItem extends Item implements IDynamicGearPart, DynamicGearClientReloadListener.IDynamicClientTintColor, GearModelProvider.GearItemModelGenerator
{
    private final AlloygeryMaterial material;
    private final PartTypes partType;

    public GearPartItem(Properties properties, AlloygeryMaterial material, PartTypes partType)
    {
        super(properties);
        this.material = material;
        this.partType = partType;
    }

    public GearPartItem(AlloygeryMaterial material, PartTypes partType)
    {
        this(new Properties(), material, partType);
    }

    @Override
    public AlloygeryMaterial getPartMaterial()
    {
        return material;
    }

    @Override
    public PartTypes getPartType()
    {
        return partType;
    }

    @Override
    public int getMaterialColor(ItemStack stack, int tintIndex)
    {
        return tintIndex == 0 ? getMaterialColorFromMaterial(getPartMaterial()) : -1;
    }

    @Override
    public void generateItemModel(ItemModelGenerators itemModelGenerator)
    {
        ResourceLocation textureTemplate = Alloygery.asResource("template/part/" + partType.getName() + "_template");
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(this), TextureMapping.layer0(textureTemplate), itemModelGenerator.output);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced)
    {
        DynamicGearDescriptionHelper.writePartDescription(stack, tooltipComponents, isAdvanced);
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
