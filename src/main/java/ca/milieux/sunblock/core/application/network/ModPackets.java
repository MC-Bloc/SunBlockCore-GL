package ca.milieux.sunblock.core.application.network;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.network.packets.ServerDataS2CPacket;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

/**
 * Registers and dispatches this mod's custom network payloads over a
 * {@link SimpleChannel}.
 *
 * Forge kept the SimpleChannel/ChannelBuilder API intact through 1.21.1 (unlike
 * NeoForge, which moved to a Payload-handler-registrar system) — the only
 * change needed here versus the 1.20.1 version is that {@code messageBuilder}
 * now takes a {@link net.minecraft.network.codec.StreamCodec} instead of
 * separate encoder/decoder functions, matching how {@link ServerDataS2CPacket}
 * already serialises itself.
 */
public class ModPackets {
    private static SimpleChannel INSTANCE;

    public static void register() {
        INSTANCE = ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, SunBlockCore.MODID))
                .networkProtocolVersion(1)
                .acceptedVersions((status, version) -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(ServerDataS2CPacket.class, 0)
                .direction(PacketFlow.CLIENTBOUND)
                .codec(ServerDataS2CPacket.STREAM_CODEC)
                .consumerMainThread(ServerDataS2CPacket::handle)
                .add();
    }

    // Send packet to player
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }

    // Send packet to all clients on server
    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(message, PacketDistributor.ALL.noArg());
    }
}
