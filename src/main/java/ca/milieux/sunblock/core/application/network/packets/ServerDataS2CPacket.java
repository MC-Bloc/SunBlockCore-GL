package ca.milieux.sunblock.core.application.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import ca.milieux.sunblock.core.application.client.SolarServerData;

import java.util.function.Supplier;

/* Server data packets will be sent from the server to clients every second.
(See thread in ServerManager.onServerStarting)
 */

public class ServerDataS2CPacket {
    private float cpuTemp;
    private float power;
    private float pvVoltage;
    private float pvCurrent;
    private float pvPower;
    private float battVoltage;
    private float battChargeCurrent;
    private float battChargePower;
    private float lPower;
    private float battRemaining;
    private float battOverallCurrent;
    private float timeRemaining;
    private String timestamp;
    private String powerProfile;
    private int cooldownSecondsRemaining;

    public static ServerDataS2CPacket EMPTY = new ServerDataS2CPacket(
            0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 0.0F, 0.0f, "", "", 0);

    public ServerDataS2CPacket( float cpuTemp, float power,
                                float pvVoltage, float pvCurrent, float pvPower, 
                                float battVoltage, float battChargeCurrent, float battChargePower, 
                                float lPower, 
                                float battRemaining, float battOverallCurrent, float timeRemaining, String timestamp, String powerProfile, int cooldownSecondsRemaining) {
        this.cpuTemp = cpuTemp;
        this.power = power;

        this.pvVoltage = pvVoltage;
        this.pvCurrent = pvCurrent;
        this.pvPower = pvPower;
        
        this.battVoltage = battVoltage;
        this.battChargeCurrent = battChargeCurrent;
        this.battChargePower = battChargePower;
        
        this.lPower = lPower;
        
        this.battRemaining = battRemaining;
        this.battOverallCurrent = battOverallCurrent;
        this.timeRemaining = timeRemaining;
        this.timestamp = timestamp;
        this.powerProfile = powerProfile;
        this.cooldownSecondsRemaining = cooldownSecondsRemaining;

    }

    public ServerDataS2CPacket(FriendlyByteBuf buf) {
        cpuTemp = buf.readFloat();
        power = buf.readFloat();

        pvVoltage = buf.readFloat();
        pvCurrent = buf.readFloat();
        pvPower = buf.readFloat();

        battVoltage = buf.readFloat();
        battChargeCurrent = buf.readFloat();
        battChargePower = buf.readFloat();

        lPower = buf.readFloat();

        battRemaining = buf.readFloat();
        battOverallCurrent = buf.readFloat();
        timeRemaining = buf.readFloat();
        timestamp = buf.readUtf();

        powerProfile = buf.readUtf();
        cooldownSecondsRemaining = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(cpuTemp);
        buf.writeFloat(power);

        buf.writeFloat(pvVoltage);
        buf.writeFloat(pvCurrent);
        buf.writeFloat(pvPower);

        buf.writeFloat(battVoltage);
        buf.writeFloat(battChargeCurrent);
        buf.writeFloat(battChargePower);

        buf.writeFloat(lPower);

        buf.writeFloat(battRemaining);
        buf.writeFloat(battOverallCurrent);
        buf.writeFloat(timeRemaining);

        buf.writeUtf(timestamp);
        buf.writeUtf(powerProfile);

        buf.writeInt(cooldownSecondsRemaining);
    }

    public static boolean handle(ServerDataS2CPacket packet, Supplier<NetworkEvent.ClientCustomPayloadEvent.Context> ctx) {
        ctx.get().enqueueWork( () ->{
                // On client side

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
        });
        return true;
    }
}
