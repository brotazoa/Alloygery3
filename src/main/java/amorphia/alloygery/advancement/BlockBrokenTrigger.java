package amorphia.alloygery.advancement;

import amorphia.alloygery.Alloygery;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class BlockBrokenTrigger extends SimpleCriterionTrigger<BlockBrokenTrigger.Conditions>
{
	public static final ResourceLocation ID = Alloygery.asResource("block_broken");

	@Override
	protected BlockBrokenTrigger.Conditions createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext)
	{
		BlockPredicate blockPredicate = BlockPredicate.fromJson(json.getAsJsonObject("block_predicate"));
		if(blockPredicate == null)
			throw new JsonSyntaxException("Failed to parse block predicate");
		boolean needsCorrectTool = json.get("needs_correct_tool").getAsBoolean();

		return new BlockBrokenTrigger.Conditions(predicate, blockPredicate, needsCorrectTool);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public void trigger(ServerPlayer player, ServerLevel level, BlockPos blockPos, boolean correctTool)
	{
		this.trigger(player, conditions -> conditions.matches(level, blockPos, correctTool));
	}

	public static class Conditions extends AbstractCriterionTriggerInstance
	{
		private final BlockPredicate blockPredicate;
		private final boolean needsCorrectTool;

		public Conditions(ContextAwarePredicate player, BlockPredicate blockPredicate, boolean needsCorrectTool)
		{
			super(BlockBrokenTrigger.ID, player);
			this.blockPredicate = blockPredicate;
			this.needsCorrectTool = needsCorrectTool;
		}

		public static BlockBrokenTrigger.Conditions blockBroken(BlockPredicate.Builder blockPredicateBuilder, boolean needsCorrectTool)
		{
			return new BlockBrokenTrigger.Conditions(ContextAwarePredicate.ANY, blockPredicateBuilder.build(), needsCorrectTool);
		}

		public boolean matches(ServerLevel level, BlockPos blockPos, boolean correctTool)
		{
			return this.blockPredicate.matches(level, blockPos) && this.needsCorrectTool == correctTool;
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject jsonObject = super.serializeToJson(context);
			jsonObject.add("block_predicate", this.blockPredicate.serializeToJson());
			jsonObject.addProperty("needs_correct_tool", this.needsCorrectTool);
			return jsonObject;
		}
	}
}
