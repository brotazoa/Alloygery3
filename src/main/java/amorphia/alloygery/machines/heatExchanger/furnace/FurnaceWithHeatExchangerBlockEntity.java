package amorphia.alloygery.machines.heatExchanger.furnace;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.AbstractFurnaceWithHeatExchangerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceWithHeatExchangerBlockEntity extends AbstractFurnaceWithHeatExchangerBlockEntity<SmeltingRecipe>
{
	public FurnaceWithHeatExchangerBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY, pos, blockState, RecipeType.SMELTING);
	}

	@Override
	protected void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit)
	{
		if (wasLit != isLit())
		{
			state = state.setValue(FurnaceWithHeatExchangerBlock.LIT, isLit());
			level.setBlock(pos, state, 3);
			setChanged();
		}
	}

	@Override
	protected int getSmeltingSpeed()
	{
		return 2;
	}

	@Override
	protected Component getDefaultName()
	{
		return Component.translatable("container.alloygery.furnace_with_heat_exchanger");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return new FurnaceWithHeatExchangerMenu(containerId, inventory, this, this.dataAccess);
	}
}
