package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.services.DataQueryProcess;
import ca.milieux.sunblock.core.services.SolarDataTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


// All of this HUD rendering code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void onRenderTick(RenderGuiOverlayEvent.Post event) {
		if (HUD.type == HUDType.TextV0) {
			HUD.TextHUD(event);
		} else if (HUD.type == HUDType.TextV1) {
			HUD.TextHUDDetailed(event);
		} else if (HUD.type == HUDType.GraphicalV0) {
			HUD.GraphicHUD(event);
		}
	}

	@SubscribeEvent
	public void onBlockStartBreak(PlayerEvent.BreakSpeed event){
		String item = event.getEntity().getMainHandItem().toString();
		float inc_speed = Math.max(DataQueryProcess.GetServerData(SolarDataTypes.PVPOWER), event.getOriginalSpeed());
		if (item.contains("solar_pickaxe") || item.contains("solar_shovel")){
			event.setNewSpeed(inc_speed);
		}
	}
}