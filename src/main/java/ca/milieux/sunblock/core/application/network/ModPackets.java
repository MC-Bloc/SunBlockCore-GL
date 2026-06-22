package ca.milieux.sunblock.core.application.network;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.network.packets.ServerDataS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers and dispatches this mod's custom network payloads via NeoForge's
 * payload-handler-registrar system (replaces Forge's SimpleChannel/ChannelBuilder API).
 */
@EventBusSubscriber(modid = SunBlockCore.MODID)
public class ModPackets {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ServerDataS2CPacket.TYPE, ServerDataS2CPacket.STREAM_CODEC, ServerDataS2CPacket::handle);
    }

    // Send packet to player
    public static <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    // Send packet to all clients on server
    public static <MSG extends CustomPacketPayload> void sendToClients(MSG message) {
        PacketDistributor.sendToAllPlayers(message);
    }
}
