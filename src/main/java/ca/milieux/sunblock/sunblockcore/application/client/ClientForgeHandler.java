package ca.milieux.sunblock.sunblockcore.application.client;

import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.LocalDateTime;
@Mod.EventBusSubscriber(modid = SunBlockCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        HUD.statsIndex = LocalDateTime.now().toLocalTime().toSecondOfDay() % 15;
        if (event.phase == TickEvent.Phase.END) {

            if (HUD.type == HUDType.GraphicalV0 && KeyBindings.INSTANCE.showSolarStats.consumeClick())
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

            if (KeyBindings.INSTANCE.HUDCycleBackwards.consumeClick()) {
                if (HUD.type == HUDType.GraphicalV0) {
                    HUD.type = HUDType.None;
                } else if (HUD.type == HUDType.None) {
                    HUD.type = HUDType.TextV1;
                } else if (HUD.type == HUDType.TextV1) {
                    HUD.type = HUDType.TextV0;
                } else if (HUD.type == HUDType.TextV0) {
                    HUD.type = HUDType.GraphicalV0;
                }
            }
        }
    }
}
