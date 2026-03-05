package amorphia.alloygery.datagen.loot;

import amorphia.alloygery.Alloygery;
import io.github.fabricators_of_create.porting_lib.loot.GlobalLootModifierProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;

public class AlloygeryGlobalLootModifierDataProvider extends GlobalLootModifierProvider
{
	private static final List<IGlobalLootModifierDataProvider> providers = new ArrayList<>();

	public static void addProvider(IGlobalLootModifierDataProvider provider)
	{
		providers.add(provider);
	}

	public AlloygeryGlobalLootModifierDataProvider(PackOutput output)
	{
		super(output, Alloygery.MODID);
	}

	@Override
	protected void start()
	{
		providers.forEach(provider -> provider.generate(this));
	}

	public interface IGlobalLootModifierDataProvider
	{
		void generate(AlloygeryGlobalLootModifierDataProvider provider);
	}
}
