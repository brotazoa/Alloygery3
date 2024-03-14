package amorphia.alloygery.craftingMaterials.tinted;

import amorphia.alloygery.Alloygery;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

public class TintedItemReloadListener implements SimpleSynchronousResourceReloadListener
{
	public static final TintedItemReloadListener INSTANCE = new TintedItemReloadListener();
	public static final ResourceLocation ID = Alloygery.asResource("tinted_item_reload_listener");

	private TintedItemReloadListener() {} // no op

	@Override
	public ResourceLocation getFabricId()
	{
		return ID;
	}

	@Override
	public void onResourceManagerReload(@NotNull ResourceManager resourceManager)
	{
		amorphia.alloygery.craftingMaterials.tinted.TintedItem.ITEMS.forEach(tintedItem -> {
			ColorProviderRegistry.ITEM.register((itemStack, tintIndex) -> {
				return tintIndex == 0 ? tintedItem.getCraftingMaterial().getColor() : -1;
			}, tintedItem);
		});
	}
}
