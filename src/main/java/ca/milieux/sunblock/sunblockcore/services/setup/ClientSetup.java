package ca.milieux.sunblock.sunblockcore.services.setup;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ca.milieux.sunblock.sunblockcore.application.client.ClientEventHandler;
import ca.milieux.sunblock.sunblockcore.domain.SolarServerData;

public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
