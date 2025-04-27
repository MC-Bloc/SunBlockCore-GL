package ca.milieux.sunblock.sunblockcore.application.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


// All of this HUD rendering code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void onRenderTick(RenderGuiOverlayEvent.Post event) {
		if (HUD.type == HUDType.TextV0) {
			HUD.TextHUD(event);
		} else if (HUD.type == HUDType.GraphicalV0) {
			HUD.GraphicHUD(event);
		}
	}
}