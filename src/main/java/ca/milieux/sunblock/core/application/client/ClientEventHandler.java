package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.SunBlockCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

// All of this HUD rendering code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
@EventBusSubscriber(modid = SunBlockCore.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final ResourceLocation SUNBLOCK_HUD =
            ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "hud");

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(SUNBLOCK_HUD, (guiGraphics, deltaTracker) -> {
            if (HUD.type == HUDType.TextV0) {
                HUD.TextHUD(guiGraphics);
            } else if (HUD.type == HUDType.TextV1) {
                HUD.TextHUDDetailed(guiGraphics);
            } else if (HUD.type == HUDType.GraphicalV0) {
                HUD.GraphicHUD(guiGraphics);
            }
        });
    }
}
