package amorphia.alloygery.gear.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.gear.material.AlloygeryMaterial;
import amorphia.alloygery.gear.material.AlloygeryMaterialRegistry;
import amorphia.alloygery.gear.material.MaterialHelper;
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

public class AlloygeryMaterialNetworkPackets
{
	public static final ResourceLocation SYNC_MATERIALS_ON_CONNECT_PACKET = Alloygery.asResource("sync_alloygery_materials_on_connect");

	public static void initialize()
	{
		ServerPlayConnectionEvents.JOIN.register((serverGamePacketListener, packetSender, minecraftServer) -> {
			FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
			CompoundTag tag = new CompoundTag();
			CompoundTag materials = new CompoundTag();
			AlloygeryMaterialRegistry.forEach((id, material) -> {
				DataResult<Tag> dataResult = AlloygeryMaterial.CODEC.encodeStart(NbtOps.INSTANCE, material);
				Tag materialTag = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
				materials.put(id.toString(), materialTag);
			});

			tag.put("alloygery_materials", materials);
			packet.writeNbt(tag);
			packetSender.sendPacket(SYNC_MATERIALS_ON_CONNECT_PACKET, packet);
		});
	}

	public static void initializeClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(SYNC_MATERIALS_ON_CONNECT_PACKET, ((minecraft, clientPacketListener, friendlyByteBuf, packetSender) -> {
			if(minecraft.isSingleplayer())
				return;

			CompoundTag tagFromPacket = friendlyByteBuf.readNbt();
			if(tagFromPacket == null || tagFromPacket.isEmpty())
				return;

			AlloygeryMaterialRegistry.resetMaterialRegistryToInitialRegisteredValues();
			AtomicInteger numberRead = new AtomicInteger();
			AtomicInteger numberModified = new AtomicInteger();

			Optional.of(tagFromPacket.getCompound("alloygery_materials")).ifPresent(materialsTag -> {
				for(String materialName : materialsTag.getAllKeys())
				{
					ResourceLocation materialLocation = ResourceLocation.tryParse(materialName);
					DataResult<AlloygeryMaterial> dataResult = AlloygeryMaterial.CODEC.parse(NbtOps.INSTANCE, materialsTag.get(materialName));
					AlloygeryMaterial material = dataResult.getOrThrow(false, Alloygery.LOGGER::error);
					numberRead.getAndIncrement();
					if(AlloygeryMaterialRegistry.contains(materialLocation))
						numberModified.getAndIncrement();

					AlloygeryMaterialRegistry.load(materialLocation, MaterialHelper.createMaterialDataFromMaterial(material));
				}
			});

			final int total = numberRead.get();
			final int modified = numberModified.get();
			Alloygery.LOGGER.info("Read {} materials from {}. Loaded {} as new, and modified {} existing materials.", total, SYNC_MATERIALS_ON_CONNECT_PACKET.toString(), total - modified, modified);
		}));
	}
}
