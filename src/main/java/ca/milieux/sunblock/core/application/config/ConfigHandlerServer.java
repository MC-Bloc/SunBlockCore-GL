package ca.milieux.sunblock.core.application.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandlerServer {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> CPU_TEMP_PATH;
    public static final ForgeConfigSpec.ConfigValue<String> SUNBLOCK_API_URL;
    public static final ForgeConfigSpec.ConfigValue<Float> BATTERY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> POWER_PROFILE_COOLDOWN;

    static {
        BUILDER.push("Server");

        // HERE DEFINE YOUR CONFIGS
        CPU_TEMP_PATH = BUILDER.comment("CPU Temperature Sysfs Path")
                .define("CPU Temp Path", "/sys/class/thermal/thermal_zone1/temp");

        SUNBLOCK_API_URL = BUILDER.comment("SunBlockCore API Endpoint")
                .define("API URL", "");

        BATTERY_CAPACITY = BUILDER.comment("Server Battery Capacity in Watts (voltage x Ah)")
                .define("Battery Capacity", 1200f);

        POWER_PROFILE_COOLDOWN = BUILDER.comment("How long before power profile can be changed again - in seconds")
                .define("Cooldown", 60);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
