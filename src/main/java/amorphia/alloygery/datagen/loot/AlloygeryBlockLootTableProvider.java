package amorphia.alloygery.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import java.util.ArrayList;
import java.util.List;

public class AlloygeryBlockLootTableProvider extends FabricBlockLootTableProvider
{
    private static final List<IAlloygeryBlockLootTableProvider> providers = new ArrayList<>();

    public static void addProvider(IAlloygeryBlockLootTableProvider provider)
    {
        providers.add(provider);
    }

    public AlloygeryBlockLootTableProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput);
    }

    @Override
    public void generate()
    {
        providers.forEach(provider -> provider.generate(this));
    }

    public interface IAlloygeryBlockLootTableProvider
    {
        void generate(AlloygeryBlockLootTableProvider builder);
    }
}
