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

public class ReplaceItemLootModifier extends LootModifier
{
	public static final Codec<ReplaceItemLootModifier> CODEC = RecordCodecBuilder.create(instance -> LootModifier.codecStart(instance).and(
			instance.group(
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("removed_item").forGetter(m -> m.removedItem),
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("added_item").forGetter(m -> m.addedItem),
					Codec.INT.optionalFieldOf("count", 1).forGetter(m -> m.addedCount)
			)
	).apply(instance, ReplaceItemLootModifier::new));

	private final Item removedItem;
	private final Item addedItem;
	private final int addedCount;

	public ReplaceItemLootModifier(LootItemCondition[] conditionsIn, Item removeItem, Item additionItem, int additionCount)
	{
		super(conditionsIn);
		this.removedItem = removeItem;
		this.addedItem = additionItem;
		this.addedCount = additionCount;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		ItemStack addedStack = new ItemStack(addedItem, addedCount);

		generatedLoot.forEach(item -> {
			if(item.is(removedItem))
				generatedLoot.remove(item);
		});

		if(addedStack.getCount() < addedStack.getMaxStackSize())
		{
			generatedLoot.add(addedStack);
		}
		else
		{
			int i = addedStack.getCount();
			while(i > 0)
			{
				ItemStack subStack = addedStack.copy();
				subStack.setCount(Math.min(addedStack.getMaxStackSize(), i));
				i -= subStack.getCount();
				generatedLoot.add(subStack);
			}
		}

		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec()
	{
		return ReplaceItemLootModifier.CODEC;
	}
}
