package amorphia.alloygery.gear.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.toolType.ToolType;
import amorphia.alloygery.gear.toolType.ToolTypeRegistry;
import com.mojang.serialization.DataResult;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolTypeNetworkPackets
{
	public static final ResourceLocation SYNC_TOOL_TYPES_ON_CONNECT_PACKET = Alloygery.asResource("sync_tool_types_on_connect");

	public static void initialize()
	{
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
			CompoundTag tag = new CompoundTag();
			CompoundTag types = new CompoundTag();
			ToolTypeRegistry.forEach((resourceLocation, toolType) -> {
				DataResult<Tag> dataResult = ToolType.CODEC.encodeStart(NbtOps.INSTANCE, toolType);
				Tag typeTag = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
				types.put(resourceLocation.toString(), typeTag);
			});

			tag.put("alloygery_tool_types", types);
			packet.writeNbt(tag);
			sender.sendPacket(SYNC_TOOL_TYPES_ON_CONNECT_PACKET, packet);
		});
	}

	public static void initializeClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(SYNC_TOOL_TYPES_ON_CONNECT_PACKET, (client, handler, buf, responseSender) -> {
			if(client.isSingleplayer())
				return;

			CompoundTag tagFromPacket = buf.readNbt();
			if(tagFromPacket == null || tagFromPacket.isEmpty())
				return;

			ToolTypeRegistry.resetRegistryToInitialValues();
			AtomicInteger numberRead = new AtomicInteger();
			AtomicInteger numberModified = new AtomicInteger();

			Optional.of(tagFromPacket.getCompound("alloygery_tool_types")).ifPresent(typesTag -> {
				for(String typeName : typesTag.getAllKeys())
				{
					ResourceLocation location = ResourceLocation.tryParse(typeName);
					DataResult<ToolType> dataResult = ToolType.CODEC.parse(NbtOps.INSTANCE, typesTag.get(typeName));
					ToolType toolType = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
					numberRead.getAndIncrement();
					if(ToolTypeRegistry.contains(location))
						numberModified.getAndIncrement();

					ToolTypeRegistry.load(location, ToolType.createData(toolType));
				}
			});

			final int total = numberRead.get();
			final int modified = numberModified.get();
			Alloygery.LOGGER.info("Read {} Tool Types from {}. Loaded {} as new, and modified {} existing.", total, SYNC_TOOL_TYPES_ON_CONNECT_PACKET.toString(), total - modified, modified);
		});
	}
}
