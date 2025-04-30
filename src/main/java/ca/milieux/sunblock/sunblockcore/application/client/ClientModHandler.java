package ca.milieux.sunblock.sunblockcore.application.client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;

@Mod.EventBusSubscriber(modid = SunBlockCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.INSTANCE.HUDDetailsKey);
        event.register(KeyBindings.INSTANCE.HUDCycleForward);
    }
}
