package ca.milieux.sunblock.sunblockcore.application.client;

import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import ca.milieux.sunblock.sunblockcore.application.config.ConfigHandler;
import ca.milieux.sunblock.sunblockcore.application.util.RenderUtils;
import ca.milieux.sunblock.sunblockcore.domain.SolarServerData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.apache.logging.log4j.Logger;

public class HUD {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final Logger LOGGER = SunBlockCore.LOGGER;

    public static int statsIndex = 0;
    public static HUDType type = HUDType.GraphicalV0;

    public static void TextHUD(RenderGuiOverlayEvent.Post event) {
        if (mc.player != null && mc.level != null && !mc.options.hideGui && (mc.screen == null || (ConfigHandler.CLIENT.displayWithChatOpen.get() && mc.screen instanceof ChatScreen))) {

            float powerConsumption = SolarServerData.getlPower();

            String powerString = "Burning " + powerConsumption  + " Watts";
            String solarStats = "Solar: " + SolarServerData.getPvVoltage() + "V | " + SolarServerData.getPvCurrent() + "A";
            String timeRemainingString = SolarServerData.getTimeRemaining() + " Hours remaining";

            int solarStatsColor = mc.level.isNight() ? 0xCAE34B : 0xCAE34B; // same for now.

            // red if high, orange if mid, white if low
            int powerConsumptionColor = powerConsumption > 20 ? 0xD6520B : powerConsumption <= 15 ? 0xFFFFFF : 0xCAE34B;

            if (statsIndex < 5) {
                RenderUtils.drawStringTopLeft(event.getGuiGraphics(), timeRemainingString, 0xFFFFFF, 0);
            } else if (statsIndex < 10) {
                RenderUtils.drawStringTopLeft(event.getGuiGraphics(), powerString, powerConsumptionColor, 0);
            } else if (statsIndex < 15) {
                RenderUtils.drawStringTopLeft(event.getGuiGraphics(), solarStats, solarStatsColor, 0);
            }


        }
    }

    static void TextHUDDetailed(RenderGuiOverlayEvent.Post event) {

        float CPUTemp = SolarServerData.getCpuTemp();
        float CPUPower = SolarServerData.getPower(); // CPU power consumption
        float powerConsumption = SolarServerData.getlPower();

        float solarVolts = SolarServerData.getPvVoltage();
        float solarCurrent = SolarServerData.getPvCurrent();
        float solarPower = SolarServerData.getPvPower();

        float batteryVoltage = SolarServerData.getBattVoltage();
        float batteryPercentage = SolarServerData.getBattRemaining();
        float batteryChargeCurrent = SolarServerData.getBattChargeCurrent();

        String cpuPowerString = "CPU State: " + CPUPower  + "Watts | " + CPUTemp + "ºC";
        String systemPowerString = "System Power Consumption: " + powerConsumption  + " Watts";
        String solarPowerString = "Solar Power Generation: " + solarVolts + "V | " + solarCurrent + "A | " + solarPower + "W";
        String batteryPowerString = "Battery: " + batteryVoltage + "V | " + batteryPercentage + "%";

        String chargingState = batteryChargeCurrent > 0 ? "Charging" : "Discharging";
        int chargingStateColor = batteryChargeCurrent > 0 ? 0xFFFFFF : 0xD6520B;

        String powerState = powerConsumption >= solarPower ? "draining. Consumption is higher than solar regeneration" : "Storing Power";
        int powerStateColor = powerConsumption >= solarPower ? 0xD6520B : 0xFFFFFF;

        RenderUtils.drawStringTopLeft(event.getGuiGraphics(), cpuPowerString, 0xFFFFFF, 1);
        RenderUtils.drawStringTopLeft(event.getGuiGraphics(), systemPowerString, 0xFFFFFF, 3);
        RenderUtils.drawStringTopLeft(event.getGuiGraphics(), solarPowerString, 0xFFFFFF, 5);
        RenderUtils.drawStringTopLeft(event.getGuiGraphics(), batteryPowerString, 0xFFFFFF, 7);

        RenderUtils.drawStringTopLeft(event.getGuiGraphics(), "Battery is " + chargingState, chargingStateColor, 9);
        RenderUtils.drawStringTopLeft(event.getGuiGraphics(), "Overall System is " + powerState, powerStateColor, 11);
}

    public static void GraphicHUD(RenderGuiOverlayEvent.Post event) {
        if (mc.player != null && mc.level != null && !mc.options.hideGui && (mc.screen == null || (ConfigHandler.CLIENT.displayWithChatOpen.get() && mc.screen instanceof ChatScreen))) {

            GuiGraphics guiGraphics = event.getGuiGraphics();

            String timeString = SolarServerData.getTimestamp();



            double configScale = ConfigHandler.CLIENT.HUD_SCALE.get();
            double configOpacity = ConfigHandler.CLIENT.HUD_OPACITY.get();
            float op = (float) configOpacity / 10;

            int textColor = 0xDDDDDD;
            String texturePath = GetTexturePath(timeString);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
    		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, op);

            guiGraphics.pose().pushPose();
    		guiGraphics.pose().translate((float)ConfigHandler.CLIENT.HUD_X_POSITION.get(), (float)ConfigHandler.CLIENT.HUD_Y_POSITION.get(), 0);
    		guiGraphics.pose().scale((float)configScale, (float)configScale, 1.0F);


            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, texturePath));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, texturePath),
                    0, 0, 0, 0, 168, 126, 168, 126);

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            // Draw text with modified alpha scaling.
            guiGraphics.drawString(mc.font, "Montreal, QC " + timeString, 60, 19, textColor);

            int _px = 25;
            //There is a difference of 4 pixels between the line and the graphic.
            int diff = 4;

            int _py_line1 = 40;
            guiGraphics.drawString(mc.font, "SOLAR: " + SolarServerData.getPvVoltage() + "v | " + SolarServerData.getPvPower() + "w", _px, _py_line1, textColor);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, GetGenerationIcon()));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, GetGenerationIcon()), 3, _py_line1 - diff, 0, 0, 16, 16, 16, 16);

            int _py_line2 = 58;
            guiGraphics.drawString(mc.font, "BATTERY: " + SolarServerData.getBattVoltage() + "v | " + SolarServerData.getBattRemaining() + "%", _px, _py_line2, textColor);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, GetBatteryIcon()));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, GetBatteryIcon()), 3, _py_line2 - diff, 0, 0, 16, 16, 16, 16);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, GetBatteryArrowIcon(SolarServerData.getBattOverallCurrent())));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, GetBatteryArrowIcon(SolarServerData.getBattOverallCurrent())),
                    12, _py_line2 - diff, 0, 0, 16, 16, 16, 16);

            int _py_line3 = 76;
            guiGraphics.drawString(mc.font, "USAGE: " + SolarServerData.getlPower() + "w", _px, _py_line3, textColor);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, GetLoadIcon()));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, GetLoadIcon()), 3, _py_line3 - diff, 0, 0, 16, 16, 16, 16);

            int _py_line4 = 94;
            guiGraphics.drawString(mc.font, "TIME LEFT: " + SolarServerData.getTimeRemaining() + " Hours", _px, _py_line4, textColor);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, GetTimeRemainingIcon()));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, GetTimeRemainingIcon()), 3, _py_line4 - 4, 0, 0, 16, 16, 16, 16);

            int _py_line5 = 112;
            guiGraphics.drawString(mc.font, "POWER MODE:" + SolarServerData.getPowerProfile(), _px, _py_line5, textColor);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SunBlockCore.MODID, GetCPUIcon()));
            guiGraphics.blit(new ResourceLocation(SunBlockCore.MODID, GetCPUIcon()), 3, _py_line5 - 4, 0, 0, 16, 16, 16, 16);


            guiGraphics.pose().popPose();

            // Reset shader color.
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    static String GetTexturePath(String timestamp) {
        int DAWN = 6;
        int DAY = 9;
        int EVENING = 18;
        int NIGHT = 21;

        if (timestamp == null || timestamp.isEmpty() || timestamp == "") {
            return "textures/gui/mc_sb_hud_day.png";
        }

        try {
            int hour = Integer.parseInt(timestamp.split(":")[0]);
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

    static String GetCPUIcon() {
        int LOW_THRESH_CPUPOWER = 5;
        int HIGH_THRESH_CPUPOWER = 20;
        float cpuPowerDraw = SolarServerData.getPower();

        if (cpuPowerDraw < LOW_THRESH_CPUPOWER) {
            return "textures/gui/mc_sb_icons_iso_cpu_g.png";
        } else if (cpuPowerDraw < HIGH_THRESH_CPUPOWER) {
            return "textures/gui/mc_sb_icons_iso_cpu_y.png";
        } else {
            return "textures/gui/mc_sb_icons_iso_cpu_r.png";
        }
    }

    static String GetLoadIcon() {
        int MIN_THRESH_LPOWER = 10;
        int MAX_THRESH_LPOWER = 22;
        float loadPower = SolarServerData.getlPower();

        if (loadPower < MIN_THRESH_LPOWER) {
            return "textures/gui/mc_sb_icons_iso_globe_g.png";
        } else if (loadPower < MAX_THRESH_LPOWER) {
            return "textures/gui/mc_sb_icons_iso_globe_y.png";
        } else {
            return "textures/gui/mc_sb_icons_iso_globe_r.png";
        }
    }

    static String GetGenerationIcon() {
        int MIN_THRESH_PVPOWER = 10;
        int MAX_THRESH_PVPOWER = 22;
        float pvPower = SolarServerData.getPvPower();

        if (pvPower < MIN_THRESH_PVPOWER) {
            return "textures/gui/mc_sb_icons_iso_sun_r.png";
        } else if (pvPower < MAX_THRESH_PVPOWER) {
            return "textures/gui/mc_sb_icons_iso_sun_o.png";
        } else {
            return "textures/gui/mc_sb_icons_iso_sun.png";
        }
    }

    static String GetBatteryIcon() {
        int HIGH_THRESH_BATTERY = 75;
        int MED_THRESH_BATTERY = 50;
        int LOW_THRESH_BATTERY = 25;
        float battPercentage = SolarServerData.getBattRemaining();

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

    static String GetTimeRemainingIcon() {
        int HIGH_THRESH_BATTERY = 40;
        int MED_THRESH_BATTERY = 12;
        float hours_rem = Float.parseFloat(SolarServerData.getTimeRemaining());

        if (hours_rem > HIGH_THRESH_BATTERY) {
            return "textures/gui/mc_sb_icons_iso_clock_g.png";
        } else if (hours_rem > MED_THRESH_BATTERY) {
            return "textures/gui/mc_sb_icons_iso_clock_y.png";
        } else {
            return "textures/gui/mc_sb_icons_iso_clock_r.png";
        }
    }

    static String GetBatteryArrowIcon(float battOverallCurrent) {
        if (battOverallCurrent > 0) {
            return "textures/gui/mc_sb_icons_iso_arrow_up.png";
        } else {
            return "textures/gui/mc_sb_icons_iso_arrow_down.png";
        }
    }

}