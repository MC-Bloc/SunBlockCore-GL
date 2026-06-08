package ca.milieux.sunblock.core.services.setup;

import ca.milieux.sunblock.core.application.config.ConfigHandlerServer;
import ca.milieux.sunblock.core.services.SolarSnapshot;
import ca.milieux.sunblock.core.services.SolarSocketClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.network.ModPackets;
import ca.milieux.sunblock.core.application.network.packets.ServerDataS2CPacket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class ServerManager {

    public static MinecraftServer server;

    private static final int MAX_MEMORY = 10;
    private static final Queue<Float> powerConsumptionHistory = new LinkedList<>();

    /** Cooldown counter for power profile switch  */
    public static int LAST_PROFILE_SWITCH = 0;

    /** Latest PV power from the most recent poll — read by break-speed and item handlers. */
    public static volatile float cachedPvPower = 0f;

    /** Latest power profile string from the most recent poll — read by PowerButton. */
    public static volatile String cachedPowerProfile = "";

    // -------------------------------------------------------------------------
    // Power profile API calls
    // -------------------------------------------------------------------------

    public static void PowerProfileSwitch() {

        String apiUrl = cachedPowerProfile.equals("Power Saver") ?
                ConfigHandlerServer.SUNBLOCK_API_PERFORMANCE.get() :
                ConfigHandlerServer.SUNBLOCK_API_POWER_SAVER.get();

        if (LAST_PROFILE_SWITCH > 0 || apiUrl.isEmpty()) return;

        try {
            URL url = new URL(apiUrl);
            URLConnection conn = url.openConnection();
            try (BufferedReader ignored = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {}
            LAST_PROFILE_SWITCH = ConfigHandlerServer.POWER_PROFILE_COOLDOWN.get();
        } catch (Exception e) {
            SunBlockCore.LOGGER.error("SunBlockCore: ServerManager::PowerProfileSwitch() error: {}", e.getMessage());
        }
    }

    /**
     * Calculate estimated hours of battery runtime remaining using a rolling
     * 10-sample average of load power. Returns "Calculating..." until the
     * window is full.
     */
    public static float getTimeRemaining(float battRemaining, float lPower) {
        if (powerConsumptionHistory.size() < MAX_MEMORY) {
            powerConsumptionHistory.add(lPower);
            return -1f;
        }
        powerConsumptionHistory.remove();
        powerConsumptionHistory.add(lPower);

        float avg = sumOfQueue(powerConsumptionHistory) / powerConsumptionHistory.size();
        if (avg <= 0f) return -1f;

        float maxCapacity = ConfigHandlerServer.BATTERY_CAPACITY.get().floatValue();
        return Math.round((maxCapacity / avg) * battRemaining) / 100f;

    }

    private static float sumOfQueue(Queue<Float> q) {
        float sum = 0f;
        for (float v : q) sum += v;
        return sum;
    }

    // -------------------------------------------------------------------------
    // Shared helpers
    //
    // Public so SolarSocketClient can reuse them when converting "solar_data"
    // websocket payloads into SolarSnapshots — the payload shape mirrors the
    // old JSON file's, so the same normalisation applies.
    // -------------------------------------------------------------------------

    public static float readCpuTemp() {
        try {
            String raw = Files.readString(Path.of(ConfigHandlerServer.CPU_TEMP_PATH.get())).trim();
            return Integer.parseInt(raw) / 1000f;
        } catch (Exception e) {
            return -1f;
        }
    }

    public static String normalizePowerProfile(String raw) {
        String lower = raw == null ? "" : raw.toLowerCase();
        if (lower.contains("performance")) return "Performance";
        if (lower.contains("balanced"))    return "Balanced";
        if (lower.contains("saver"))       return "Power Saver";
        return "";
    }

    /**
     * Normalise a timestamp that may arrive as "HH:MM:SS" or "YYYY-MM-DD HH:MM:SS"
     * down to "HH:MM".
     */
    public static String normalizeTimestamp(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        String[] spaceParts = raw.strip().split(" ");
        String timePart = spaceParts[spaceParts.length - 1];
        String[] colonParts = timePart.split(":");
        if (colonParts.length >= 2) return colonParts[0] + ":" + colonParts[1];
        return raw;
    }

    @Mod.EventBusSubscriber(modid = SunBlockCore.MODID)
    public class ServerEvents {

        // Decrements LAST_PROFILE_SWITCH once every 20 ticks (~1s).
        // This used to be driven by the 1-second polling loop; now that telemetry
        // arrives as push events from the websocket (at whatever cadence the API
        // chooses to send them), the cooldown is timed off the server tick instead
        // so it stays tied to real elapsed time rather than event arrival rate.
        static int cooldownTickCounter = 0;

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            ServerManager.server = ServerLifecycleHooks.getCurrentServer();

            // Live telemetry now arrives via a Socket.IO push ("solar_data") instead
            // of polling the solar-data JSON file once per second.
            SolarSocketClient.connect(ServerEvents::onSolarData);
        }

        // Invoked (off the server thread, on the socket's I/O callback thread) every
        // time a "solar_data" event is received from the SunBlockExpress API.
        private static void onSolarData(SolarSnapshot snap) {
            cachedPvPower = snap.pvPower();
            cachedPowerProfile = snap.powerProfile();

            float timeRemaining = getTimeRemaining(snap.battRemaining(), snap.lPower());

            ServerDataS2CPacket _packet = new ServerDataS2CPacket(
                    snap.cpuTemp(), snap.cpuPowerDraw(),
                    snap.pvVoltage(), snap.pvCurrent(), snap.pvPower(),
                    snap.battVoltage(), snap.battChargeCurrent(), snap.battChargePower(),
                    snap.lPower(), snap.battRemaining(), snap.battOverallCurrent(),
                    timeRemaining, snap.timestamp(), snap.powerProfile(),
                    LAST_PROFILE_SWITCH);

            ModPackets.sendToClients(_packet);
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            if (++cooldownTickCounter < 20) return;
            cooldownTickCounter = 0;
            if (LAST_PROFILE_SWITCH > 0) LAST_PROFILE_SWITCH--;
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    ServerDataS2CPacket _packet = ServerDataS2CPacket.EMPTY;
                    ModPackets.sendToPlayer(_packet, player);
                }
            }
        }

        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            String item = event.getEntity().getMainHandItem().toString();
            if (item.contains("solar_pickaxe") || item.contains("solar_shovel")) {
                float boostedSpeed = Math.max(cachedPvPower, event.getOriginalSpeed());
                event.setNewSpeed(boostedSpeed);
            }
        }

        // When the server stops (world unload / quit to title / dedicated server
        // shutdown), close the websocket connection BEFORE the server config
        // unloads. This must be `static` — `@Mod.EventBusSubscriber` only
        // auto-registers static @SubscribeEvent methods (an instance method here
        // would silently never fire, leaving the socket connected after shutdown
        // and causing "Cannot get config value before config is loaded" once a
        // late "solar_data" push tries to read CPU_TEMP_PATH from the unloaded
        // config). ServerStoppingEvent also fires exactly once per server
        // lifecycle, unlike LevelEvent.Unload which fires per-dimension.
        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            SolarSocketClient.disconnect();
        }
    }
}
