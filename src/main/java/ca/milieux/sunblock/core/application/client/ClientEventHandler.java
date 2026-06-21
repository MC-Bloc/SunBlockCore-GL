package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.SunBlockCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// All of this HUD rendering code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
@Mod.EventBusSubscriber(modid = SunBlockCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final ResourceLocation SUNBLOCK_HUD =
            ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "hud");

    @SubscribeEvent
    public static void onAddOverlayLayers(AddGuiOverlayLayersEvent event) {
        event.getLayeredDraw().add(SUNBLOCK_HUD, (guiGraphics, deltaTracker) -> {
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
