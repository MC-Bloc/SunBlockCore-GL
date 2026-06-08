package ca.milieux.sunblock.core.domain;

public class SolarServerData {
    public static float power;
    public static float cpuTemp;
    public static float pvVoltage;
    public static float pvCurrent;
    public static float pvPower;
    public static float battVoltage;
    public static float battChargeCurrent;
    public static float battChargePower;
    public static float lPower;
    public static float battRemaining;
    public static float battOverallCurrent;
    public static String timeRemaining;
    public static String timestamp;
    public static String powerProfile;
    public static int cooldownSecondsRemaining;

    public SolarServerData(float power, float cpuTemp) {
        SolarServerData.power = power;
        SolarServerData.cpuTemp = cpuTemp;
        SolarServerData.pvVoltage = 0.0f;
        SolarServerData.pvCurrent = 0.0f;
        SolarServerData.pvPower = 0.0f;
        SolarServerData.battVoltage = 0.0f;
        SolarServerData.battChargeCurrent = 0.0f;
        SolarServerData.battChargePower = 0.0f;
        SolarServerData.lPower = 0.0f;
        SolarServerData.battRemaining = 0.0f;
        SolarServerData.battOverallCurrent = 0.0f;
        SolarServerData.timeRemaining = "Calculating Time Remaining...";
        SolarServerData.timestamp = "00:00";
        SolarServerData.powerProfile = "";
        SolarServerData.cooldownSecondsRemaining = 0;
    }
}
