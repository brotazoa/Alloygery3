package amorphia.alloygery.machines.heatExchanger.kiln;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerBlockEntity;
import amorphia.alloygery.machines.kiln.FiringRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class KilnWithHeatExchangerBlockEntity extends AbstractFurnaceWithHeatExchangerBlockEntity<FiringRecipe>
{
	public KilnWithHeatExchangerBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.KILN_WITH_HEAT_EXCHANGER_BLOCK_ENTITY, pos, blockState, FiringRecipe.Type.INSTANCE);
	}

	@Override
	protected void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit)
	{
		if (wasLit != isLit())
		{
			state = state.setValue(KilnWithHeatExchangerBlock.LIT, isLit());
			level.setBlock(pos, state, 3);
			setChanged();
		}
	}

	@Override
	protected int getSmeltingSpeed()
	{
		return 4;
	}

	@Override
	protected Component getDefaultName()
	{
		return Component.translatable("container.alloygery.kiln_with_heat_exchanger");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return new KilnWithHeatExchangerMenu(containerId, inventory, this, this.dataAccess);
	}
}
