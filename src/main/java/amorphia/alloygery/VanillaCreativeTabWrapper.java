package amorphia.alloygery;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class VanillaCreativeTabWrapper implements IAlloygeryCreativeTab
{
	private final ResourceKey<CreativeModeTab> vanillaTab;
	private final List<Item> items = new ArrayList<>();

	VanillaCreativeTabWrapper(ResourceKey<CreativeModeTab> tab)
	{
		this.vanillaTab = tab;
	}

	@Override
	public void add(Item... items)
	{
		this.items.addAll(List.of(items));
	}

	@Override
	public void build()
	{
		ItemGroupEvents.modifyEntriesEvent(vanillaTab).register(entries -> {
			for(Item item : items)
			{
				entries.accept(item);
			}
		});
	}
}
