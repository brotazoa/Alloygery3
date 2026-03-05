package amorphia.alloygery.craftingMaterials.client;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.craftingMaterials.CraftingMaterial;
import amorphia.alloygery.craftingMaterials.CraftingMaterialModule;
import amorphia.alloygery.gear.client.AlloygeryMaterialColors;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftingMaterialClientReloadListener implements SimpleSynchronousResourceReloadListener
{
    public static final CraftingMaterialClientReloadListener INSTANCE = new CraftingMaterialClientReloadListener();
    public static final ResourceLocation ID = Alloygery.asResource("crafting_material_client_reload_listener");

    private CraftingMaterialClientReloadListener() {} // no op

    @Override
    public ResourceLocation getFabricId()
    {
        return CraftingMaterialClientReloadListener.ID;
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager)
    {
        for(Item item : CraftingMaterialModule.ITEMS.values())
        {
            if (item instanceof IClientTintColor itemWithTintColor)
            {
                ColorProviderRegistry.ITEM.register(itemWithTintColor::getMaterialColor, item);
            }

            if (item instanceof DyeableLeatherItem dyeableLeatherItem)
            {
                ColorProviderRegistry.ITEM.register((itemStack, i) -> i > 0 ? -1 : dyeableLeatherItem.getColor(itemStack), item);
            }
        }
    }

    public interface IClientTintColor
    {
        int getMaterialColor(ItemStack stack, int tintIndex);

        default int getMaterialColorFromMaterial(CraftingMaterial material)
        {
            return material == null ? AlloygeryMaterialColors.UNKNOWN_COLOR.color() : AlloygeryMaterialColors.get(material.getName()).color();
        }
    }
}
