package amorphia.alloygery.advancement;

import amorphia.alloygery.Alloygery;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class WrongToolForDropsTrigger extends SimpleCriterionTrigger<WrongToolForDropsTrigger.Conditions>
{
	public static final ResourceLocation ID = Alloygery.asResource("wrong_tool_for_drops");

	@Override
	protected Conditions createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext)
	{
		BlockPredicate blockPredicate = BlockPredicate.fromJson(json.getAsJsonObject("block_predicate"));
		if(blockPredicate == null)
			throw new JsonSyntaxException("Failed to parse block predicate");

		return new Conditions(predicate, blockPredicate);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public void trigger(ServerPlayer player, ServerLevel level, BlockPos blockPos)
	{
		this.trigger(player, conditions -> conditions.matches(level, blockPos));
	}

	public static class Conditions extends AbstractCriterionTriggerInstance
	{
		private final BlockPredicate blockPredicate;

		public Conditions(ContextAwarePredicate player, BlockPredicate blockPredicate)
		{
			super(WrongToolForDropsTrigger.ID, player);
			this.blockPredicate = blockPredicate;
		}

		public static Conditions wrongToolForDrops(BlockPredicate.Builder blockPredicateBuilder)
		{
			return new Conditions(ContextAwarePredicate.ANY, blockPredicateBuilder.build());
		}

		public boolean matches(ServerLevel level, BlockPos blockPos)
		{
			return this.blockPredicate.matches(level, blockPos);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject jsonObject = super.serializeToJson(context);
			jsonObject.add("block_predicate", this.blockPredicate.serializeToJson());
			return jsonObject;
		}
	}
}
