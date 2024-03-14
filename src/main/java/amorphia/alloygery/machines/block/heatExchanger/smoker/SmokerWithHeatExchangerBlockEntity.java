package amorphia.alloygery.machines.block.heatExchanger.smoker;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.block.heatExchanger.AbstractFurnaceWithHeatExchangerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SmokerWithHeatExchangerBlockEntity extends AbstractFurnaceWithHeatExchangerBlockEntity<SmokingRecipe>
{
	public SmokerWithHeatExchangerBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.SMOKER_WITH_HEAT_EXCHANGER_BLOCK_ENTITY, pos, blockState, RecipeType.SMOKING);
	}

	@Override
	protected void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit)
	{
		if (wasLit != isLit())
		{
			state = state.setValue(SmokerWithHeatExchangerBlock.LIT, isLit());
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
		return Component.translatable("container.alloygery.smoker_with_heat_exchanger");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return null;
	}
}
