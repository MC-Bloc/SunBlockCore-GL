package ca.milieux.sunblock.sunblockcore.application.client;

import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.LocalDateTime;
@Mod.EventBusSubscriber(modid = SunBlockCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        ClientEventHandler.statsIndex = LocalDateTime.now().toLocalTime().toSecondOfDay() % 15;
        if (event.phase == TickEvent.Phase.END) {
            if (KeyBindings.INSTANCE.showSolarStats.isDown()) {
                ClientEventHandler.showAllSolarStats = true;
            } else if (ClientEventHandler.showAllSolarStats) {
                ClientEventHandler.showAllSolarStats = false;
            }
        }
    }
}
