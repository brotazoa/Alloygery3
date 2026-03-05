package amorphia.alloygery.datagen.loot;

import amorphia.alloygery.accessor.LootContextAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class AddLootTableLootModifier extends LootModifier
{
	public static final Codec<AddLootTableLootModifier> CODEC = RecordCodecBuilder.create(instance -> LootModifier.codecStart(instance).and(
			ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.lootTable)
	).apply(instance, AddLootTableLootModifier::new));

	private final ResourceLocation lootTable;

	public AddLootTableLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable)
	{
		super(conditionsIn);
		this.lootTable = lootTable;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext)
	{
		LootTable addTable = lootContext.getResolver().getLootTable(this.lootTable);
		LootContext newContext = new LootContext.Builder(((LootContextAccessor)lootContext).getParams()).create(this.lootTable);
		addTable.getRandomItemsRaw(newContext, LootTable.createStackSplitter(lootContext.getLevel(), generatedLoot::add));
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec()
	{
		return CODEC;
	}
}
