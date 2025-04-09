package ca.milieux.sunblock.sunblockcore.services.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import ca.milieux.sunblock.sunblockcore.application.network.ModPackets;

public class CommonSetup {
    public static void init(FMLCommonSetupEvent event) {
        ModPackets.register();
    }
}
