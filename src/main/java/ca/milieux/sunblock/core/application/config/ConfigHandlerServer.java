package ca.milieux.sunblock.core.application.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandlerServer {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> SUNBLOCK_DATA_PATH;
    public static final ForgeConfigSpec.ConfigValue<String> SUNBLOCK_API_URL;
    public static final ForgeConfigSpec.ConfigValue<Float> BATTERY_CAPACITY;

    static {
        BUILDER.push("Server");

        // HERE DEFINE YOUR CONFIGS
        SUNBLOCK_DATA_PATH = BUILDER.comment("SunBlockCore Data Path")
                .define("Data Path", "/home/pc/SunblockData/solar_data.json");

        SUNBLOCK_API_URL = BUILDER.comment("SunBlockExpress API Endpoint")
                .define("API URL", "https://photon.sunblockone.milieux.ca");

        BATTERY_CAPACITY = BUILDER.comment("Server Battery Capacity in Watts (voltage x Ah)")
                .define("Battery Capacity", 1200f);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
