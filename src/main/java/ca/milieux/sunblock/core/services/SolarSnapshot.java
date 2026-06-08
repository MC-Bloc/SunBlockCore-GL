package ca.milieux.sunblock.core.services;

/**
 * Immutable snapshot of one full solar-data reading, produced by SolarSocketClient
 * when a "solar_data" websocket event arrives, and consumed by ServerManager to
 * update caches and broadcast a ServerDataS2CPacket.
 */
public record SolarSnapshot(
        float pvVoltage,
        float pvCurrent,
        float pvPower,
        float battVoltage,
        float battChargeCurrent,
        float battChargePower,
        float lPower,
        float battRemaining,
        float battOverallCurrent,
        float cpuPowerDraw,
        float cpuTemp,
        String powerProfile,
        String timestamp
) {
}
