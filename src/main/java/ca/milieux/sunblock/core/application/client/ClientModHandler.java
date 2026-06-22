package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.services.setup.ClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = SunBlockCore.MODID, value = Dist.CLIENT)
public class ClientModHandler {

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.INSTANCE.HUDDetailsKey);
        event.register(KeyBindings.INSTANCE.HUDCycleForward);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientSetup.init(event);
    }
}