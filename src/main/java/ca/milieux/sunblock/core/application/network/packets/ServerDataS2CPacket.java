package ca.milieux.sunblock.core.application.network.packets;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.client.SolarServerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/* Server data packets are sent from the server to clients whenever a new
   "solar_data" reading arrives over the websocket (see ServerManager.onSolarData).

   Rewritten for the 1.21 Payload-based networking API — replaces the old
   SimpleChannel/NetworkEvent message that this packet used under 1.20.1.
 */
public record ServerDataS2CPacket(
        float cpuTemp,
        float power,
        float pvVoltage,
        float pvCurrent,
        float pvPower,
        float battVoltage,
        float battChargeCurrent,
        float battChargePower,
        float lPower,
        float battRemaining,
        float battOverallCurrent,
        float timeRemaining,
        String timestamp,
        String powerProfile,
        int cooldownSecondsRemaining
) implements CustomPacketPayload {

    public static final Type<ServerDataS2CPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "server_data"));

    public static final ServerDataS2CPacket EMPTY = new ServerDataS2CPacket(
            0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 0.0F, 0.0F, "", "", 0);

    public static final StreamCodec<FriendlyByteBuf, ServerDataS2CPacket> STREAM_CODEC = StreamCodec.of(
            ServerDataS2CPacket::write,
            ServerDataS2CPacket::read
    );

    private static void write(FriendlyByteBuf buf, ServerDataS2CPacket packet) {
        buf.writeFloat(packet.cpuTemp);
        buf.writeFloat(packet.power);

        buf.writeFloat(packet.pvVoltage);
        buf.writeFloat(packet.pvCurrent);
        buf.writeFloat(packet.pvPower);

        buf.writeFloat(packet.battVoltage);
        buf.writeFloat(packet.battChargeCurrent);
        buf.writeFloat(packet.battChargePower);

        buf.writeFloat(packet.lPower);

        buf.writeFloat(packet.battRemaining);
        buf.writeFloat(packet.battOverallCurrent);
        buf.writeFloat(packet.timeRemaining);

        buf.writeUtf(packet.timestamp);
        buf.writeUtf(packet.powerProfile);

        buf.writeInt(packet.cooldownSecondsRemaining);
    }

    private static ServerDataS2CPacket read(FriendlyByteBuf buf) {
        float cpuTemp = buf.readFloat();
        float power = buf.readFloat();

        float pvVoltage = buf.readFloat();
        float pvCurrent = buf.readFloat();
        float pvPower = buf.readFloat();

        float battVoltage = buf.readFloat();
        float battChargeCurrent = buf.readFloat();
        float battChargePower = buf.readFloat();

        float lPower = buf.readFloat();

        float battRemaining = buf.readFloat();
        float battOverallCurrent = buf.readFloat();
        float timeRemaining = buf.readFloat();

        String timestamp = buf.readUtf();
        String powerProfile = buf.readUtf();

        int cooldownSecondsRemaining = buf.readInt();

        return new ServerDataS2CPacket(cpuTemp, power, pvVoltage, pvCurrent, pvPower,
                battVoltage, battChargeCurrent, battChargePower, lPower,
                battRemaining, battOverallCurrent, timeRemaining,
                timestamp, powerProfile, cooldownSecondsRemaining);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** Invoked on the client's main thread when a packet of this type arrives (NeoForge runs payload handlers on the main thread by default). */
    public static void handle(ServerDataS2CPacket packet, IPayloadContext context) {
        SolarServerData.cpuTemp = packet.cpuTemp;
        SolarServerData.power = packet.power;

        SolarServerData.pvVoltage = packet.pvVoltage;
        SolarServerData.pvCurrent = packet.pvCurrent;
        SolarServerData.pvPower = packet.pvPower;

        SolarServerData.battVoltage = packet.battVoltage;
        SolarServerData.battChargeCurrent = packet.battChargeCurrent;
        SolarServerData.battChargePower = packet.battChargePower;

        SolarServerData.lPower = packet.lPower;

        SolarServerData.battRemaining = packet.battRemaining;
        SolarServerData.battOverallCurrent = packet.battOverallCurrent;
        SolarServerData.timeRemaining = packet.timeRemaining;
        SolarServerData.timestamp = packet.timestamp;
        SolarServerData.powerProfile = packet.powerProfile;

        SolarServerData.cooldownSecondsRemaining = packet.cooldownSecondsRemaining;
    }
}
