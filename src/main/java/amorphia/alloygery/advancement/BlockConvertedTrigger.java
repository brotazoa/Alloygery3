package amorphia.alloygery.advancement;

import amorphia.alloygery.Alloygery;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class BlockConvertedTrigger extends SimpleCriterionTrigger<BlockConvertedTrigger.Conditions>
{
	public static final ResourceLocation ID = Alloygery.asResource("block_convert");

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	protected Conditions createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext)
	{
		BlockPredicate outPredicate = BlockPredicate.fromJson(json.getAsJsonObject("converted_block"));

		return new Conditions(predicate, outPredicate);
	}

	public void trigger(ServerLevel level, BlockPos convert)
	{
		Player player = level.getNearestPlayer(convert.getX(), convert.getY(), convert.getZ(), 10.0, false);
		if (player instanceof ServerPlayer serverPlayer)
		{
			this.trigger(serverPlayer, conditions -> conditions.matches(level, convert));
		}
	}

	public static class Conditions extends AbstractCriterionTriggerInstance
	{
		private final BlockPredicate convertedPredicate;

		public Conditions(ContextAwarePredicate player, BlockPredicate convertedPredicate)
		{
			super(BlockConvertedTrigger.ID, player);
			this.convertedPredicate = convertedPredicate;
		}

		public static Conditions blockConvert(BlockPredicate.Builder converted)
		{
			return new Conditions(ContextAwarePredicate.ANY, converted.build());
		}

		public boolean matches(ServerLevel level, BlockPos convertedPos)
		{
			return this.convertedPredicate.matches(level, convertedPos);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject jsonObject = super.serializeToJson(context);
			jsonObject.add("converted_block", this.convertedPredicate.serializeToJson());
			return jsonObject;
		}
	}
}
