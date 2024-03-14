package amorphia.alloygery.machines.block.heatExchanger.blastFurnace;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.block.heatExchanger.AbstractFurnaceWithHeatExchangerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceWithHeatExchangerBlockEntity extends AbstractFurnaceWithHeatExchangerBlockEntity<BlastingRecipe>
{
	public BlastFurnaceWithHeatExchangerBlockEntity(BlockPos pos, BlockState blockState)
	{
		super(MachinesModule.BLAST_FURNACE_WITH_HEAT_EXCHANGER_BLOCK_ENTITY, pos, blockState, RecipeType.BLASTING);
	}

	@Override
	protected void updateLitProperty(Level level, BlockPos pos, BlockState state, boolean wasLit)
	{
		if (wasLit != isLit())
		{
			state = state.setValue(BlastFurnaceWithHeatExchangerBlock.LIT, isLit());
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
		return Component.translatable("container.alloygery.blast_furnace_with_heat_exchanger");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory)
	{
		return null;
	}
}
