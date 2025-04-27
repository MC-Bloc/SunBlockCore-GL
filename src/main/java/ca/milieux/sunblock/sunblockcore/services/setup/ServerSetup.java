package ca.milieux.sunblock.sunblockcore.services.setup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ca.milieux.sunblock.sunblockcore.services.SolarDataTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import ca.milieux.sunblock.sunblockcore.application.network.ModPackets;
import ca.milieux.sunblock.sunblockcore.application.network.packets.ServerDataS2CPacket;
import ca.milieux.sunblock.sunblockcore.services.DataQueryProcess;

public class ServerSetup {

    public static MinecraftServer server;
    public static ExecutorService executor = Executors.newSingleThreadExecutor();

    @Mod.EventBusSubscriber(modid = SunBlockCore.MODID)
    public class ServerEvents {

        static int THREAD_SLEEP = 1000; //in milliseconds

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            // The server starting initiates a thread which calls DataQueryProcess methods every second

            ServerSetup.server = ServerLifecycleHooks.getCurrentServer();
            Future<?> future = ServerSetup.executor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    float currentTemp = DataQueryProcess.GetCPUTemp();
                    float currentPower = DataQueryProcess.GetServerData(SolarDataTypes.SYSTEMPOWERDRAW);

                    float currentPVVoltage = DataQueryProcess.GetServerData(SolarDataTypes.PVVOLTAGE);
                    float currentPVCurrent = DataQueryProcess.GetServerData(SolarDataTypes.PVCURRENT);
                    float currentPVPower = DataQueryProcess.GetServerData(SolarDataTypes.PVPOWER);

                    float currentbattVoltage = DataQueryProcess.GetServerData(SolarDataTypes.BATTVOLTAGE);
                    float currentbattChargeCurrent = DataQueryProcess.GetServerData(SolarDataTypes.BATTCHARGECURRENT);
                    float currentbattChargePower = DataQueryProcess.GetServerData(SolarDataTypes.BATTCHARGEPOWER);

                    float currentlPower = DataQueryProcess.GetServerData(SolarDataTypes.LPOWER);

                    float currentbattRemaining = DataQueryProcess.GetServerData(SolarDataTypes.BATTREMAINING);
                    float currentbattOverallCurrent = DataQueryProcess.GetServerData(SolarDataTypes.BATTOVERALLCURRENT);
                    String timeRemaining = DataQueryProcess.GetTimeRemaining();
                    String timestamp = DataQueryProcess.GetTimestamp();


                    ServerDataS2CPacket _packet = new ServerDataS2CPacket(
                            currentTemp, currentPower, currentPVVoltage, currentPVCurrent, currentPVPower,
                            currentbattVoltage, currentbattChargeCurrent, currentbattChargePower, currentlPower,
                            currentbattRemaining, currentbattOverallCurrent, timeRemaining, timestamp );


                    ModPackets.sendToClients(_packet);
                    try {
                        Thread.sleep(THREAD_SLEEP);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Preserve interrupt status
                        System.out.println("SunBlockCore::ServerSetup.java -- Thread Interrupted!");
                        break;
                    }
                }
            });
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    ServerPlayer eventPlayer = (ServerPlayer) event.getEntity();
                    ServerDataS2CPacket _packet = new ServerDataS2CPacket(
                            0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                            0.0F, 0.0F, 0.0F, 0.0F,
                            0.0F, 0.0F, "", "");

                    ModPackets.sendToPlayer(_packet, player);
                }
            }
        }

        // When world is unloaded, thread is shut down
        @SubscribeEvent
        public void onServerShutdown(LevelEvent.Unload event) {
            ServerSetup.executor.shutdownNow();
        }
    }
}