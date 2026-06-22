package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.SunBlockCore;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.time.LocalDateTime;
@EventBusSubscriber(modid = SunBlockCore.MODID, value = Dist.CLIENT)
public class ClientForgeHandler {

    static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        HUD.statsIndex = LocalDateTime.now().toLocalTime().toSecondOfDay() % 15;
        if (HUD.type == HUDType.GraphicalV0 && KeyBindings.INSTANCE.HUDDetailsKey.consumeClick())
        {
            if (mc.screen == null) {
                mc.setScreen(new HUDSettings());
            }
        }

        if (KeyBindings.INSTANCE.HUDCycleForward.consumeClick()) {
            if (HUD.type == HUDType.GraphicalV0) {
                HUD.type = HUDType.TextV1;
            } else if (HUD.type == HUDType.TextV1) {
                HUD.type = HUDType.TextV0;
            } else if (HUD.type == HUDType.TextV0) {
                HUD.type = HUDType.None;
            } else if (HUD.type == HUDType.None) {
                HUD.type = HUDType.GraphicalV0;
            }
        }
    }
}
