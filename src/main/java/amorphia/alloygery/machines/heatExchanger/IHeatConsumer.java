package amorphia.alloygery.machines.heatExchanger;

public interface IHeatConsumer
{
	void consumeHeat(int amount);

	int giveHeat(int amount);
}
