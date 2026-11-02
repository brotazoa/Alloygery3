package amorphia.alloygery.gear.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.armorStyle.ArmorStyle;
import amorphia.alloygery.gear.armorStyle.ArmorStyleRegistry;
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

public class ArmorStyleNetworkPackets
{
	public static final ResourceLocation SYNC_ARMOR_STYLES_ON_CONNECT_PACKET = Alloygery.asResource("sync_armor_styles_on_connect");

	public static void initialize()
	{
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
			CompoundTag tag = new CompoundTag();
			CompoundTag styles = new CompoundTag();
			ArmorStyleRegistry.forEach((resourceLocation, style) -> {
				DataResult<Tag> dataResult = ArmorStyle.CODEC.encodeStart(NbtOps.INSTANCE, style);
				Tag styleTag = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
				styles.put(resourceLocation.toString(), styleTag);
			});

			tag.put("alloygery_armor_styles", styles);
			packet.writeNbt(tag);
			sender.sendPacket(SYNC_ARMOR_STYLES_ON_CONNECT_PACKET, packet);
		});
	}

	public static void initializeClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(SYNC_ARMOR_STYLES_ON_CONNECT_PACKET, (client, handler, buf, responseSender) -> {
			if(client.isSingleplayer())
				return;

			CompoundTag tagFromPacket = buf.readNbt();
			if(tagFromPacket == null || tagFromPacket.isEmpty())
				return;

			ArmorStyleRegistry.resetRegistryToInitialValues();
			AtomicInteger numberRead = new AtomicInteger();
			AtomicInteger numberModified = new AtomicInteger();

			Optional.of(tagFromPacket.getCompound("alloygery_armor_styles")).ifPresent(stylesTag -> {
				for(String styleName : stylesTag.getAllKeys())
				{
					ResourceLocation location = ResourceLocation.tryParse(styleName);
					DataResult<ArmorStyle> dataResult = ArmorStyle.CODEC.parse(NbtOps.INSTANCE, stylesTag.get(styleName));
					ArmorStyle style = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
					numberRead.getAndIncrement();
					if(ArmorStyleRegistry.contains(location))
						numberModified.getAndIncrement();

					ArmorStyleRegistry.load(location, ArmorStyle.createData(style));
				}
			});

			final int total = numberRead.get();
			final int modified = numberModified.get();
			Alloygery.LOGGER.info("Read {} Armor Styles from {}. Loaded {} as new, and modified {} existing.", total, SYNC_ARMOR_STYLES_ON_CONNECT_PACKET.toString(), total - modified, modified);
		});
	}
}
