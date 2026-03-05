package amorphia.alloygery.datagen.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class AddItemLootModifier extends LootModifier
{
	public static final Codec<AddItemLootModifier> CODEC = RecordCodecBuilder.create(instance -> LootModifier.codecStart(instance).and(
			instance.group(
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.addedItem),
					Codec.INT.optionalFieldOf("count", 1).forGetter(m -> m.count)
			)
	).apply(instance, AddItemLootModifier::new));

	private final Item addedItem;
	private final int count;

	protected AddItemLootModifier(LootItemCondition[] conditionsIn, Item addedItem, int count)
	{
		super(conditionsIn);
		this.addedItem = addedItem;
		this.count = count;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext)
	{
		ItemStack addedStack = new ItemStack(addedItem, count);
		if(addedStack.getCount() < addedStack.getMaxStackSize())
		{
			generatedLoot.add(addedStack);
		}
		else
		{
			int remaining = addedStack.getCount();
			while(remaining > 0)
			{
				ItemStack remainingStack = addedStack.copy();
				remainingStack.setCount(Math.min(addedStack.getMaxStackSize(), remaining));
				remaining -= remainingStack.getCount();
				generatedLoot.add(remainingStack);
			}
		}

		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec()
	{
		return CODEC;
	}
}
