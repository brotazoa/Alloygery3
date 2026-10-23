package amorphia.alloygery.machines.smithing_vice;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.item.IDynamicGear;
import amorphia.alloygery.gear.item.PartTypes;
import amorphia.alloygery.gear.nbt.NBTHelper;
import amorphia.alloygery.machines.AbstractSingleIngredientMenu;
import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class SmithingViceMenu extends AbstractSingleIngredientMenu<SmithingViceRecipe>
{
	public SmithingViceMenu(int containerId, Inventory playerInventory)
	{
		super(MachinesModule.SMITHING_VICE_MENU_TYPE, containerId, playerInventory, SmithingViceRecipe.Type.INSTANCE);
		this.uiItemTakeSoundEvent = SoundEvents.ITEM_BREAK;
	}

	public SmithingViceMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access)
	{
		super(MachinesModule.SMITHING_VICE_MENU_TYPE, containerId, playerInventory, SmithingViceRecipe.Type.INSTANCE, access);
		this.uiItemTakeSoundEvent = SoundEvents.ITEM_BREAK;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return stillValid(this.access, player, MachinesModule.SMITHING_VICE);
	}

	@Override
	protected boolean hasRecipesForInput(ItemStack stack)
	{
		return super.hasRecipesForInput(stack) || stack.getItem() instanceof IDynamicGear;
	}

	@Override
	protected void setupRecipeList(Container container, ItemStack stack)
	{
		super.setupRecipeList(container, stack);

		if (stack.getItem() instanceof IDynamicGear)
		{
			for (PartTypes partType : NBTHelper.getPartTypesListFromItemStack(NBTHelper.hasAlloygeryTag(stack) ? stack : stack.getItem().getDefaultInstance()))
			{
				Item partItem = BuiltInRegistries.ITEM.get(NBTHelper.getItemIdentifierFromTag(NBTHelper.getPartTagFromItemStack(stack, partType)));
				if(partItem != Items.AIR)
				{
					this.recipes.add(new SmithingViceRecipe(Alloygery.asResource(partType.getName() + "_from_smithing_vice"), partItem.getDefaultInstance(), Ingredient.of(stack.getItem()), 1));
				}
			}
		}
	}
}
