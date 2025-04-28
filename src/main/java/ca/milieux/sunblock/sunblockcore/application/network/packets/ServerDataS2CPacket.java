package ca.milieux.sunblock.sunblockcore.application.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import ca.milieux.sunblock.sunblockcore.domain.SolarServerData;

import java.util.function.Supplier;

/* Server data packets will be sent from the server to clients every second.
(See thread in ServerSetup.onServerStarting)
 */

public class ServerDataS2CPacket {
    private static float cpuTemp;
    private static float power;
    private static float pvVoltage;
    private static float pvCurrent;
    private static float pvPower;
    private static float battVoltage;
    private static float battChargeCurrent;
    private static float battChargePower;
    private static float lPower;
    private static float battRemaining;
    private static float battTemp;
    private static float battOverallCurrent;
    private static String timeRemaining;
    private static String timestamp;
    private static String powerProfile;

    public ServerDataS2CPacket( float cpuTemp, float power, 
                                float pvVoltage, float pvCurrent, float pvPower, 
                                float battVoltage, float battChargeCurrent, float battChargePower, 
                                float lPower, 
                                float battRemaining, float battOverallCurrent, String timeRemaining, String timestamp, String powerProfile) {
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
        timeRemaining = buf.readUtf();
        timestamp = buf.readUtf();

        powerProfile = buf.readUtf();
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

        buf.writeUtf(timeRemaining);
        buf.writeUtf(timestamp);
        buf.writeUtf(powerProfile);
    }

    public static boolean handle(ServerDataS2CPacket packet, Supplier<NetworkEvent.ClientCustomPayloadEvent.Context> ctx) {
        ctx.get().enqueueWork( () ->{
                // On client side
                SolarServerData.setCpuTemp(cpuTemp);
                SolarServerData.setPower(power);

                SolarServerData.setPvVoltage(pvVoltage);
                SolarServerData.setPvCurrent(pvCurrent);
                SolarServerData.setPvPower(pvPower);

                SolarServerData.setBattVoltage(battVoltage);
                SolarServerData.setBattChargeCurrent(battChargeCurrent);
                SolarServerData.setBattChargePower(battChargePower);

                SolarServerData.setlPower(lPower);

                SolarServerData.setBattRemaining(battRemaining);
                SolarServerData.setBattOverallCurrent(battOverallCurrent);
                SolarServerData.setTimeRemaining(timeRemaining);
                SolarServerData.setTimestamp(timestamp);
                SolarServerData.setPowerProfile(powerProfile);


        });
        return true;
    }
}
