package ca.milieux.sunblock.sunblockcore.application.client;

import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import ca.milieux.sunblock.sunblockcore.application.config.ConfigHandler;
import ca.milieux.sunblock.sunblockcore.application.util.RenderUtils;
import ca.milieux.sunblock.sunblockcore.services.setup.ClientSetup;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import org.apache.logging.log4j.Logger;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


// All of this HUD rendering code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {

	private static final Minecraft mc = Minecraft.getInstance();
	private static final Logger LOGGER = SunBlockCore.LOGGER;
	public static boolean showAllSolarStats = false;
	private static boolean isHudVisible = true;
	private static boolean hasLoggedRender = false;

	// if 0, display time remaining, if 1 display power details, if 2 display solar stats
	// set in ClientForgeHandler.
	public static int statsIndex = 0;
	public static int dashboard_version = 2;


//	@SubscribeEvent
//	public void onRenderTick(RenderGuiOverlayEvent.Post event) {
//		if (mc.player != null && mc.level != null && !mc.options.hideGui && (mc.screen == null || (ConfigHandler.CLIENT.displayWithChatOpen.get() && mc.screen instanceof ChatScreen))) {
//			final Player player = mc.player;
//			float powerConsumption = ClientSetup.serverData.getlPower();
//
//			String powerString = "Burning " + powerConsumption  + " Watts";
//			String solarStats = "Solar: " + ClientSetup.serverData.getPvVoltage() + "V | " + ClientSetup.serverData.getPvCurrent() + "A";
//			String timeRemainingString = ClientSetup.serverData.getTimeRemaining() + " Hours remaining";
//
//			int solarStatsColor = mc.level.isNight() ? 0xCAE34B : 0xCAE34B; // same for now.
//
//			// red if high, orange if mid, white if low
//			int powerConsumptionColor = powerConsumption > 20 ? 0xD6520B : powerConsumption <= 15 ? 0xFFFFFF : 0xCAE34B;
//
//			if (showAllSolarStats) {
//				SolarStats(event);
//			} else {
//				if (statsIndex < 5) {
//					RenderUtils.drawStringTopLeft(event.getGuiGraphics(), timeRemainingString, 0xFFFFFF, 0);
//				} else if (statsIndex < 10) {
//					RenderUtils.drawStringTopLeft(event.getGuiGraphics(), powerString, powerConsumptionColor, 0);
//				} else if (statsIndex < 15) {
//					RenderUtils.drawStringTopLeft(event.getGuiGraphics(), solarStats, solarStatsColor, 0);
//				}
//			}
//
//		}
//	}


	void SolarStats(RenderGuiOverlayEvent.Post event) {

		float CPUTemp = ClientSetup.serverData.getCpuTemp();
		float CPUPower = ClientSetup.serverData.getPower(); // CPU power consumption
		float powerConsumption = ClientSetup.serverData.getlPower();

		float solarVolts = ClientSetup.serverData.getPvVoltage();
		float solarCurrent = ClientSetup.serverData.getPvCurrent();
		float solarPower = ClientSetup.serverData.getPvPower();

		float batteryVoltage = ClientSetup.serverData.getBattVoltage();
		float batteryPercentage = ClientSetup.serverData.getBattRemaining();
		float batteryChargeCurrent = ClientSetup.serverData.getBattChargeCurrent();

		String cpuPowerString = "CPU State: " + CPUPower  + "Watts | " + CPUTemp + "ºC";
		String systemPowerString = "System Power Consumption: " + powerConsumption  + " Watts";
		String solarPowerString = "Solar Power Generation: " + solarVolts + "V | " + solarCurrent + "A | " + solarPower + "W";
		String batteryPowerString = "Battery: " + batteryVoltage + "V | " + batteryPercentage + "%";

		String chargingState = batteryChargeCurrent > 0 ? "Charging" : "Discharging";
		int chargingStateColor = batteryChargeCurrent > 0 ? 0xFFFFFF : 0xD6520B;

		String powerState = powerConsumption >= solarPower ? "draining. Consumption is higher than solar regeneration" : "Storing Power";
		int powerStateColor = powerConsumption >= solarPower ? 0xD6520B : 0xFFFFFF;

		if (dashboard_version == 1) {
			RenderUtils.drawStringTopLeft(event.getGuiGraphics(), cpuPowerString, 0xFFFFFF, 1);
			RenderUtils.drawStringTopLeft(event.getGuiGraphics(), systemPowerString, 0xFFFFFF, 3);
			RenderUtils.drawStringTopLeft(event.getGuiGraphics(), solarPowerString, 0xFFFFFF, 5);
			RenderUtils.drawStringTopLeft(event.getGuiGraphics(), batteryPowerString, 0xFFFFFF, 7);

			RenderUtils.drawStringTopLeft(event.getGuiGraphics(), "Battery is " + chargingState, chargingStateColor, 9);
			RenderUtils.drawStringTopLeft(event.getGuiGraphics(), "Overall System is " + powerState, powerStateColor, 11);
		}
	}

	// Andrew's HUD

	@SubscribeEvent
	public void onRenderGui(RenderGuiOverlayEvent.Post event) {
		if (mc.player != null && mc.level != null && !mc.options.hideGui && (mc.screen == null || (ConfigHandler.CLIENT.displayWithChatOpen.get() && mc.screen instanceof ChatScreen))) {

			GuiGraphics guiGraphics = event.getGuiGraphics();

//		double configScale = SolarHudConfig.HUD_SCALE.get();
//		double configOpacity = SolarHudConfig.HUD_OPACITY.get();
//		float op = (float) configOpacity;

			// Calculate text alpha with 0.32 threshold
//		int originalAlpha = (int)(configOpacity * 255) & 0xFF;
			int originalAlpha = (int) (0.75f * 255) & 0xFF;

			int textAlpha;
			if (originalAlpha < 82) {
				textAlpha = 0;
			} else {
				textAlpha = (int) (((originalAlpha - 82) / 173.0) * 255.0); // 173 = 255 - 82
			}
			int textColor = (textAlpha << 24) | 0xFFFFFF;

			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
//		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, op);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

			guiGraphics.pose().pushPose();

//		guiGraphics.pose().translate((float)SolarHudConfig.HUD_X_POSITION.get(), (float)SolarHudConfig.HUD_Y_POSITION.get(), 0);
			guiGraphics.pose().translate(0, 0, 0);

//		guiGraphics.pose().scale((float)configScale, (float)configScale, 1.0F);
			guiGraphics.pose().scale(1.0F, 1.0F, 1.0F);


//		String texturePath = getTexturePath(DataHandler.getTimestamp());
			String texturePath = getTexturePath(null);

			RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, texturePath));
			guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, texturePath),
					0, 0, 0, 0, 168, 108, 168, 108);

//		String timestamp = DataHandler.getTimestamp();
//		String[] timeParts = (timestamp != null && !timestamp.isEmpty()) ? timestamp.split(" ")[1].split(":") : new String[]{"--","--"};
//		String timeString = timeParts[0] + ":" + timeParts[1];
			String timeString = "69:69";

			// Draw text with modified alpha scaling.
			guiGraphics.drawString(mc.font, "Montreal, QC " + timeString, 60, 19, textColor);
			guiGraphics.drawString(mc.font, "CPU: " + ClientSetup.serverData.getPower() + "w", 25, 40, textColor);
			guiGraphics.drawString(mc.font, "CONSUMPTION: " + ClientSetup.serverData.getlPower() + "w", 25, 58, textColor);
			guiGraphics.drawString(mc.font, "GENERATION: " + ClientSetup.serverData.getPvVoltage() + "v | " + ClientSetup.serverData.getPvPower() + "w", 25, 76, textColor);
			guiGraphics.drawString(mc.font, "BATTERY: " + ClientSetup.serverData.getBattVoltage() + "v | " + ClientSetup.serverData.getBattRemaining() + "%", 25, 94, textColor);

			// Icons.
			RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, getCpuIcon()));
			guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, getCpuIcon()), 3, 36, 0, 0, 16, 16, 16, 16);

			RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, getLoadIcon()));
			guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, getLoadIcon()), 3, 54, 0, 0, 16, 16, 16, 16);

			RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, getGenerationIcon()));
			guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, getGenerationIcon()), 3, 72, 0, 0, 16, 16, 16, 16);

			RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, getBatteryIcon()));
			guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, getBatteryIcon()), 3, 90, 0, 0, 16, 16, 16, 16);

			RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, getBatteryArrowIcon(ClientSetup.serverData.getBattOverallCurrent())));
			guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, getBatteryArrowIcon(ClientSetup.serverData.getBattOverallCurrent())),
					12, 90, 0, 0, 16, 16, 16, 16);

			guiGraphics.pose().popPose();

			// Reset shader color.
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		}
	}



	private String getTexturePath(String timestamp) {
		int DAWN = 6;
		int DAY = 9;
		int EVENING = 18;
		int NIGHT = 21;

		if (timestamp == null || timestamp.isEmpty()) {
			return "textures/gui/mc_sb_hud_day.png";
		}

		try {
			int hour = Integer.parseInt(timestamp.split(" ")[1].split(":")[0]);
			if (hour >= DAWN && hour < DAY) {
				return "textures/gui/mc_sb_hud_dawn.png";
			} else if (hour >= DAY && hour < EVENING) {
				return "textures/gui/mc_sb_hud_day.png";
			} else if (hour >= EVENING && hour < NIGHT) {
				return "textures/gui/mc_sb_hud_dusk.png";
			} else {
				return "textures/gui/mc_sb_hud_night.png";
			}
		} catch (Exception e) {
			LOGGER.error("Error parsing timestamp '{}': {}", timestamp, e.getMessage());
			return "textures/gui/mc_sb_hud_day.png";
		}
	}



	private String getCpuIcon() {
		int LOW_THRESH_CPUPOWER = 5;
		int HIGH_THRESH_CPUPOWER = 20;
		float cpuPowerDraw = ClientSetup.serverData.getPower();

		if (cpuPowerDraw < LOW_THRESH_CPUPOWER) {
			return "textures/gui/mc_sb_icons_iso_cpu_g.png";
		} else if (cpuPowerDraw < HIGH_THRESH_CPUPOWER) {
			return "textures/gui/mc_sb_icons_iso_cpu_y.png";
		} else {
			return "textures/gui/mc_sb_icons_iso_cpu_r.png";
		}
	}



	private String getLoadIcon() {
		int MIN_THRESH_LPOWER = 10;
		int MAX_THRESH_LPOWER = 22;
		float loadPower = ClientSetup.serverData.getlPower();

		if (loadPower < MIN_THRESH_LPOWER) {
			return "textures/gui/mc_sb_icons_iso_globe_g.png";
		} else if (loadPower < MAX_THRESH_LPOWER) {
			return "textures/gui/mc_sb_icons_iso_globe_y.png";
		} else {
			return "textures/gui/mc_sb_icons_iso_globe_r.png";
		}
	}

	private String getGenerationIcon() {
		int MIN_THRESH_PVPOWER = 10;
		int MAX_THRESH_PVPOWER = 22;
		float pvPower = ClientSetup.serverData.getPvPower();

		if (pvPower < MIN_THRESH_PVPOWER) {
			return "textures/gui/mc_sb_icons_iso_sun_r.png";
		} else if (pvPower < MAX_THRESH_PVPOWER) {
			return "textures/gui/mc_sb_icons_iso_sun_o.png";
		} else {
			return "textures/gui/mc_sb_icons_iso_sun.png";
		}
	}


	private String getBatteryIcon() {
		int HIGH_THRESH_BATTERY = 75;
		int MED_THRESH_BATTERY = 50;
		int LOW_THRESH_BATTERY = 25;
		float battPercentage = ClientSetup.serverData.getBattRemaining();

		if (battPercentage > HIGH_THRESH_BATTERY) {
			return "textures/gui/mc_sb_icons_iso_battery_g.png";
		} else if (battPercentage > MED_THRESH_BATTERY) {
			return "textures/gui/mc_sb_icons_iso_battery_y.png";
		} else if (battPercentage > LOW_THRESH_BATTERY) {
			return "textures/gui/mc_sb_icons_iso_battery_o.png";
		} else {
			return "textures/gui/mc_sb_icons_iso_battery_r.png";
		}
	}

	private static String getBatteryArrowIcon(float battOverallCurrent) {
		if (battOverallCurrent > 0) {
			return "textures/gui/mc_sb_icons_iso_arrow_up.png";
		} else {
			return "textures/gui/mc_sb_icons_iso_arrow_down.png";
		}
	}

}