package amorphia.alloygery;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlloygeryCreativeTab implements IAlloygeryCreativeTab
{
	private final ResourceLocation icon;
	private final List<Item> entries;

	AlloygeryCreativeTab(ResourceLocation icon)
	{
		this.icon = icon;
		this.entries = new ArrayList<>();
	}

	@Override
	public void build()
	{
		final Item iconItem = Registry.register(BuiltInRegistries.ITEM, icon, new Item(new Item.Properties()));
		final CreativeModeTab GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(iconItem)).title(Component.translatable(icon.toLanguageKey("creative_tab"))).displayItems((context, items) -> {
			for(Item item : entries)
				items.accept(item);
		}).build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, icon, GROUP);
	}

	@Override
	public void add(Item... items)
	{
		entries.addAll(List.of(items));
	}

	public ResourceLocation getIcon()
	{
		return this.icon;
	}
}
