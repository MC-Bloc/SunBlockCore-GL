package ca.milieux.sunblock.core.application.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigHandlerServer {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<String> CPU_TEMP_PATH;
    public static final ModConfigSpec.ConfigValue<String> SUNBLOCK_API_URL;
    public static final ModConfigSpec.ConfigValue<String> SUNBLOCK_API_PERFORMANCE;
    public static final ModConfigSpec.ConfigValue<String> SUNBLOCK_API_POWER_SAVER;
    public static final ModConfigSpec.ConfigValue<String> SUNBLOCK_API_TOKEN;
    public static final ModConfigSpec.ConfigValue<Double> BATTERY_CAPACITY;
    public static final ModConfigSpec.ConfigValue<Integer> POWER_PROFILE_COOLDOWN;

    static {
        BUILDER.push("Server");

        // HERE DEFINE YOUR CONFIGS
        CPU_TEMP_PATH = BUILDER.comment("CPU Temperature Sysfs Path")
                .define("CPU Temp Path", "/sys/class/thermal/thermal_zone1/temp");

        SUNBLOCK_API_URL = BUILDER.comment("SunBlockCore API Endpoint")
                .define("API URL", "");

        SUNBLOCK_API_PERFORMANCE = BUILDER.comment("SunBlockCore Performance Mode API Endpoint")
                .define("PerformanceEndpoint", "");

        SUNBLOCK_API_POWER_SAVER = BUILDER.comment("SunBlockCore Power Saver Mode API Endpoint")
                .define("PowerSaverEndpoint", "");

        SUNBLOCK_API_TOKEN = BUILDER.comment("SunBlockCore API Authorization Token")
                .define("TOKEN", "");

        BATTERY_CAPACITY = BUILDER.comment("Server Battery Capacity in Watts (voltage x Ah)")
                .define("Battery Capacity", 1200.0);

        POWER_PROFILE_COOLDOWN = BUILDER.comment("How long before power profile can be changed again - in seconds")
                .define("Cooldown", 60);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
