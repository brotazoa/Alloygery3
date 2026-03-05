package amorphia.alloygery.advancement;

import amorphia.alloygery.datagen.AlloygeryEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class AdvancementModule
{
	public static final WrongToolForDropsTrigger WRONG_TOOL_FOR_DROPS_TRIGGER = CriteriaTriggers.register(new WrongToolForDropsTrigger());
	public static final BlockConvertedTrigger BLOCK_CONVERTED_TRIGGER = CriteriaTriggers.register(new BlockConvertedTrigger());

	public static void initialize()
	{
		PlayerBlockBreakEvents.BEFORE.register(((level, player, blockPos, blockState, blockEntity) -> {
			if(player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel)
			{
				ItemStack mainHandStack = player.getMainHandItem();
				if(!mainHandStack.isCorrectToolForDrops(blockState))
				{
					WRONG_TOOL_FOR_DROPS_TRIGGER.trigger(serverPlayer, serverLevel, blockPos);
				}
			}
			return true;
		}));
	}

	public static void initializeClient()
	{

	}

	public static void initializeDataGen(FabricDataGenerator dataGenerator, FabricDataGenerator.Pack pack)
	{
		AlloygeryEnglishLanguageProvider.addProvider(new AdvancementEnglishLanguageProvider());
		pack.addProvider(GameChangesAdvancementsProvider::new);
	}
}
