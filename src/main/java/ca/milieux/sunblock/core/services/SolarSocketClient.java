package ca.milieux.sunblock.core.services;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.config.ConfigHandlerServer;
import ca.milieux.sunblock.core.services.setup.ServerManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.function.Consumer;

/**
 * Maintains a Socket.IO connection to the SunBlockCore-LL API and converts
 * incoming "solar_data" push events into {@link SolarSnapshot}s.
 *
 * This replaces the old approach of polling the local solar-data JSON file
 * once per second: the API now pushes a new reading the moment one is
 * available, and the mod simply reacts to it.
 *
 * Note: CPU temperature is read locally (sysfs) rather than over the wire,
 * since it describes the machine the server itself is running on — see
 * {@link ServerManager#readCpuTemp()}.
 */
public final class SolarSocketClient {

    private static Socket socket;

    private SolarSocketClient() {}

    /**
     * Connect to {@code ConfigHandlerServer.SUNBLOCK_API_URL} and invoke
     * {@code onData} every time a "solar_data" event is received. Safe to call
     * multiple times — a second call is a no-op while already connected.
     */
    public static synchronized void connect(Consumer<SolarSnapshot> onData) {
        if (socket != null) return;

        String apiUrl = ConfigHandlerServer.SUNBLOCK_API_URL.get();
        if (apiUrl.isEmpty()) {
            SunBlockCore.LOGGER.warn("SunBlockCore: SUNBLOCK_API_URL is not configured — skipping solar_data socket connection.");
            return;
        }

        try {
            socket = IO.socket(apiUrl);

            socket.on(Socket.EVENT_CONNECT, args ->
                    SunBlockCore.LOGGER.info("SunBlockCore: connected to SunBlockCore-LL at '{}'", apiUrl));

            socket.on(Socket.EVENT_DISCONNECT, args ->
                    SunBlockCore.LOGGER.warn("SunBlockCore: disconnected from SunBlockCore-LL"));

            socket.on(Socket.EVENT_CONNECT_ERROR, args ->
                    SunBlockCore.LOGGER.error("SunBlockCore: socket connection error: {}",
                            args.length > 0 ? String.valueOf(args[0]) : "unknown"));

            socket.on("solar_data", args -> {
                // Guard against events delivered in the brief window around
                // server shutdown/restart — the socket I/O thread is async and
                // can race the server lifecycle, so `socket == null` alone
                // isn't reliable (a callback may already be mid-flight when
                // disconnect() runs). toSnapshot() reads several
                // ConfigHandlerServer values (CPU_TEMP_PATH, BATTERY_CAPACITY,
                // etc.) which throw "Cannot get config value before config is
                // loaded" once the server config unloads — checking
                // SPEC.isLoaded() up front avoids that race entirely rather
                // than relying on catching it after the fact.
                if (socket == null || !ConfigHandlerServer.SPEC.isLoaded()) return;
                if (args.length == 0 || !(args[0] instanceof JSONObject payload)) return;
                try {
                    onData.accept(toSnapshot(payload));
                } catch (Exception e) {
                    SunBlockCore.LOGGER.error("SunBlockCore: failed to parse 'solar_data' payload: {}", e.getMessage());
                }
            });

            socket.connect();
        } catch (URISyntaxException e) {
            SunBlockCore.LOGGER.error("SunBlockCore: invalid SUNBLOCK_API_URL '{}': {}", apiUrl, e.getMessage());
            socket = null;
        }
    }

    /** Disconnect and release the socket, if connected. Safe to call repeatedly. */
    public static synchronized void disconnect() {
        if (socket == null) return;
        socket.off();
        socket.disconnect();
        socket = null;
    }

    /**
     * Convert a "solar_data" JSON payload into a SolarSnapshot, reusing the
     * same field names and normalisation logic as the old file-based reads
     * (the API payload mirrors the JSON file's shape) and merging in the
     * locally-read CPU temperature.
     */
    private static SolarSnapshot toSnapshot(JSONObject obj) {
        return new SolarSnapshot(
                (float) obj.optDouble("PVVoltage", 0),
                (float) obj.optDouble("PVCurrent", 0),
                (float) obj.optDouble("PVPower", 0),
                (float) obj.optDouble("BattVoltage", 0),
                (float) obj.optDouble("BattChargeCurrent", 0),
                (float) obj.optDouble("BattChargePower", 0),
                (float) obj.optDouble("LoadPower", 0),
                (float) obj.optDouble("BattPercentage", 0),
                (float) obj.optDouble("BattOverallCurrent", 0),
                (float) obj.optDouble("CPUPowerDraw", 0),
                ServerManager.readCpuTemp(),
                ServerManager.normalizePowerProfile(obj.optString("PowerProfile", "")),
                ServerManager.normalizeTimestamp(obj.optString("Timestamp", ""))
        );
    }
}
