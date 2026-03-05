package amorphia.alloygery.gear.client;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.GearModule;
import amorphia.alloygery.gear.convert.VanillaItemConverter;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class DynamicGearClientReloadListener implements SimpleSynchronousResourceReloadListener
{
    public static final DynamicGearClientReloadListener INSTANCE = new DynamicGearClientReloadListener();
    public static final ResourceLocation ID = Alloygery.asResource("dynamic_gear_client_reload_listener");

    private DynamicGearClientReloadListener(){} // no op

    @Override
    public ResourceLocation getFabricId()
    {
        return DynamicGearClientReloadListener.ID;
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager)
    {
        for(Item item : GearModule.ITEMS.values())
        {
            if (item instanceof IDynamicClientTintColor itemWithTintColor)
            {
                ColorProviderRegistry.ITEM.register(itemWithTintColor::getMaterialColor, item);
            }
        }

        for(Item item : VanillaItemConverter.VANILLA_CONVERTIBLE_ITEMS)
        {
            ColorProviderRegistry.ITEM.register((itemStack, index) -> {
                ItemStack converted = VanillaItemConverter.convert(itemStack);
                return converted.getItem() instanceof IDynamicClientTintColor itemWithColor ? itemWithColor.getMaterialColor(converted, index) : -1;
            }, item);
        }
    }

    public interface IDynamicClientTintColor
    {
        int getMaterialColor(ItemStack stack, int tintIndex);

        default int getMaterialColorFromMaterial(AlloygeryMaterial material)
        {
            return material == null ? AlloygeryMaterialColors.UNKNOWN_COLOR.color() : AlloygeryMaterialColors.get(material.getMaterialIdentifier()).color();
        }
    }
}
