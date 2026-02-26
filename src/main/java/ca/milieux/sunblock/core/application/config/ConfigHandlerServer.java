package ca.milieux.sunblock.core.application.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandlerServer {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> SUNBLOCK_DATA_PATH;
    public static final ForgeConfigSpec.ConfigValue<String> SUNBLOCK_API_URL;

    static {
        BUILDER.push("Configs for SunBlockCore Mod");

        // HERE DEFINE YOUR CONFIGS
        SUNBLOCK_DATA_PATH = BUILDER.comment("SunBlockCore Data Path")
                .define("Data Path", "/home/pc/SunblockData/solar_data.json");

        SUNBLOCK_API_URL = BUILDER.comment("SunBlockExpress API Endpoint")
                .define("API URL", "https://photon.sunblockone.milieux.ca");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
