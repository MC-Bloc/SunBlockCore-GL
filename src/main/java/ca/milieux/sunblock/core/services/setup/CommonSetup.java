package ca.milieux.sunblock.core.services.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import ca.milieux.sunblock.core.application.network.ModPackets;

public class CommonSetup {
    public static void init(FMLCommonSetupEvent event) {
        ModPackets.register();
    }
}
