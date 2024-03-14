package amorphia.alloygery.machines.block.heatExchanger;

public interface IHeatConsumer
{
	void consumeHeat(int amount);

	int giveHeat(int amount);
}
