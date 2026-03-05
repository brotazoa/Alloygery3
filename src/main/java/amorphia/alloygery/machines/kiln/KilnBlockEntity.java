package amorphia.alloygery.machines.kiln;

import amorphia.alloygery.machines.MachinesModule;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity
{
	public KilnBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.KILN_BLOCK_ENTITY, pos, blockState, FiringRecipe.Type.INSTANCE);
	}

	@Override
	protected int getBurnDuration(ItemStack fuel)
	{
		return super.getBurnDuration(fuel) / 2;
	}

	@Override
	protected Component getDefaultName()
	{
		return Component.translatable("container.alloygery.kiln");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return new KilnMenu(containerId, inventory, this, this.dataAccess);
	}
}
