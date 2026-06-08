package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.config.ConfigHandler;
import ca.milieux.sunblock.core.application.util.RenderUtils;
import ca.milieux.sunblock.core.domain.SolarServerData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.apache.logging.log4j.Logger;

public class HUD {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final Logger LOGGER = SunBlockCore.LOGGER;

    public static int statsIndex = 0;
    public static HUDType type = HUDType.GraphicalV0;

    private static final ResourceLocation BG_DAWN       = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_hud_dawn.png");
    private static final ResourceLocation BG_DAY        = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_hud_day.png");
    private static final ResourceLocation BG_DUSK       = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_hud_dusk.png");
    private static final ResourceLocation BG_NIGHT      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_hud_night.png");

    private static final ResourceLocation CPU_LOW       = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_cpu_g.png");
    private static final ResourceLocation CPU_MED       = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_cpu_y.png");
    private static final ResourceLocation CPU_HIGH      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_cpu_r.png");

    private static final ResourceLocation LOAD_LOW      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_globe_g.png");
    private static final ResourceLocation LOAD_MED      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_globe_y.png");
    private static final ResourceLocation LOAD_HIGH     = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_globe_r.png");

    private static final ResourceLocation GEN_LOW       = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_sun_r.png");
    private static final ResourceLocation GEN_MED       = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_sun_o.png");
    private static final ResourceLocation GEN_HIGH      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_sun.png");

    private static final ResourceLocation BATT_HIGH     = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_battery_g.png");
    private static final ResourceLocation BATT_MED      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_battery_y.png");
    private static final ResourceLocation BATT_LOW      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_battery_o.png");
    private static final ResourceLocation BATT_DAM      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_battery_r.png");

    private static final ResourceLocation TIME_HIGH     = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_clock_g.png");
    private static final ResourceLocation TIME_MED      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_clock_y.png");
    private static final ResourceLocation TIME_LOW      = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_clock_r.png");

    private static final ResourceLocation BATT_CHARGING = new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_arrow_up.png");
    private static final ResourceLocation BATT_DISCHARGE= new ResourceLocation(SunBlockCore.MODID, "textures/gui/mc_sb_icons_iso_arrow_down.png");


    public static void TextHUD(RenderGuiOverlayEvent.Post event) {
        if (mc.player != null && mc.level != null && !mc.options.hideGui && (mc.screen == null || (ConfigHandler.CLIENT.displayWithChatOpen.get() && mc.screen instanceof ChatScreen))) {

            float powerConsumption = SolarServerData.lPower;

            String powerString = "Burning " + powerConsumption  + " Watts";
            String solarStats = "Solar: " + SolarServerData.pvVoltage + "V | " + SolarServerData.pvCurrent + "A";
            String timeRemainingString = SolarServerData.timeRemaining + " Hours remaining";

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

        float CPUTemp = SolarServerData.cpuTemp;
        float CPUPower = SolarServerData.power; // CPU power consumption
        float powerConsumption = SolarServerData.lPower;

        float solarVolts = SolarServerData.pvVoltage;
        float solarCurrent = SolarServerData.pvCurrent;
        float solarPower = SolarServerData.pvPower;

        float batteryVoltage = SolarServerData.battVoltage;
        float batteryPercentage = SolarServerData.battRemaining;
        float batteryChargeCurrent = SolarServerData.battChargeCurrent;

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

            String timeString = SolarServerData.timestamp;

            double configScale = ConfigHandler.CLIENT.HUD_SCALE.get();
            double configOpacity = ConfigHandler.CLIENT.HUD_OPACITY.get();
            float op = (float) configOpacity / 10;

            int textColor = 0xDDDDDD;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
    		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, op);

            guiGraphics.pose().pushPose();
    		guiGraphics.pose().translate((float)ConfigHandler.CLIENT.HUD_X_POSITION.get(), (float)ConfigHandler.CLIENT.HUD_Y_POSITION.get(), 0);
    		guiGraphics.pose().scale((float)configScale, (float)configScale, 1.0F);


            //
            ResourceLocation bg_texture = GetTexture(timeString);
            RenderSystem.setShaderTexture(0, bg_texture);
            guiGraphics.blit(bg_texture,0, 0, 0, 0, 168, 126, 168, 126);

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            // Draw text with modified alpha scaling.
            guiGraphics.drawString(mc.font, "Montreal, QC " + timeString, 60, 19, textColor);


            int _px = 25, _py_line1 = 40, diff = 4; // There is a difference of 4 pixels between the line and the graphic.
            String delim = "\n\n";


            String solar_stats = "SOLAR: " + SolarServerData.pvVoltage + "v | " + SolarServerData.pvPower + "w";
            String battery_stats = "BATTERY: " + SolarServerData.battVoltage + "v | " + SolarServerData.battRemaining + "%";
            String usage_stats = "USAGE: " + SolarServerData.lPower + "w";
            String time_left = SolarServerData.cooldownSecondsRemaining > 0 ?
                    "COOLDOWN: " + SolarServerData.cooldownSecondsRemaining + " Secs" :
                    "TIME LEFT: " + SolarServerData.timeRemaining + " Hours";
            String power_profile = "POWER MODE:" + SolarServerData.powerProfile;


            FormattedText t = FormattedText.of(solar_stats + delim + battery_stats + delim + usage_stats + delim + time_left + delim + power_profile);
            guiGraphics.drawWordWrap(mc.font, t, _px, _py_line1,168, textColor);

            ResourceLocation genIcon = GetGenerationIcon();
            RenderSystem.setShaderTexture(0, genIcon);
            guiGraphics.blit(genIcon, 3, _py_line1 - diff, 0, 0, 16, 16, 16, 16);

            int _py_line2 = 58;
            ResourceLocation batt_icon = GetBatteryIcon(), batt_arrow = GetBatteryArrowIcon();
            RenderSystem.setShaderTexture(0, batt_icon);
            guiGraphics.blit(batt_icon, 3, _py_line2 - diff, 0, 0, 16, 16, 16, 16);
            RenderSystem.setShaderTexture(0, batt_arrow);
            guiGraphics.blit(batt_arrow, 12, _py_line2 - diff, 0, 0, 16, 16, 16, 16);

            int _py_line3 = 76;
            ResourceLocation load_icon = GetLoadIcon();
            RenderSystem.setShaderTexture(0, load_icon);
            guiGraphics.blit(load_icon, 3, _py_line3 - diff, 0, 0, 16, 16, 16, 16);

            int _py_line4 = 94;
            ResourceLocation time_icon = GetTimeRemainingIcon();
            RenderSystem.setShaderTexture(0, time_icon);
            guiGraphics.blit(time_icon, 3, _py_line4 - 4, 0, 0, 16, 16, 16, 16);

            int _py_line5 = 112;
            ResourceLocation cpu_icon = GetCPUIcon();
            RenderSystem.setShaderTexture(0, cpu_icon);
            guiGraphics.blit(cpu_icon, 3, _py_line5 - 4, 0, 0, 16, 16, 16, 16);


            guiGraphics.pose().popPose();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f); // Reset shader color.
        }
    }


    static ResourceLocation GetTexture(String timestamp) {
        int DAWN = 6, DAY = 9, EVENING = 18, NIGHT = 21;

        // base case
        if (timestamp == null || timestamp.isEmpty()) {
            return BG_DAY;
        }

        try {
            int hour = Integer.parseInt(timestamp.split(":")[0]);
            if (hour >= NIGHT) return BG_NIGHT;
            else if (hour >= EVENING) return BG_DUSK;
            else if (hour >= DAY) return BG_DAY;
            else return BG_DAWN;
        } catch (Exception e) {
            LOGGER.error("Error parsing timestamp '{}': {}", timestamp, e.getMessage());
            return BG_DAY;
        }
    }

    static ResourceLocation GetCPUIcon() {
        int LOW_THRESH_CPUPOWER = 5, HIGH_THRESH_CPUPOWER = 20;
        float cpuPowerDraw = SolarServerData.power;

        if (cpuPowerDraw < LOW_THRESH_CPUPOWER) return CPU_LOW;
        else if (cpuPowerDraw < HIGH_THRESH_CPUPOWER) return CPU_MED;
        else return CPU_HIGH;
    }

    static ResourceLocation GetLoadIcon() {
        int MIN_THRESH_LPOWER = 10;
        int MAX_THRESH_LPOWER = 22;
        float loadPower = SolarServerData.lPower;

        if (loadPower < MIN_THRESH_LPOWER) return LOAD_LOW;
        else if (loadPower < MAX_THRESH_LPOWER) return LOAD_MED;
        else return LOAD_HIGH;
    }

    static ResourceLocation GetGenerationIcon() {
        int MIN_THRESH_PVPOWER = 10;
        int MAX_THRESH_PVPOWER = 22;
        float pvPower = SolarServerData.pvPower;

        if (pvPower < MIN_THRESH_PVPOWER) return GEN_LOW;
        else if (pvPower < MAX_THRESH_PVPOWER) return GEN_MED;
        else return GEN_HIGH;
    }

    static ResourceLocation GetBatteryIcon() {
        int HIGH_THRESH_BATTERY = 75;
        int MED_THRESH_BATTERY = 50;
        int LOW_THRESH_BATTERY = 25;
        float battPercentage = SolarServerData.battRemaining;

        if (battPercentage > HIGH_THRESH_BATTERY) return BATT_HIGH;
        else if (battPercentage > MED_THRESH_BATTERY) return BATT_MED;
        else if (battPercentage > LOW_THRESH_BATTERY) return BATT_LOW;
        else return BATT_DAM;
    }

    static ResourceLocation GetTimeRemainingIcon() {
        int HIGH_THRESH_BATTERY = 40;
        int MED_THRESH_BATTERY = 12;
        float hours_rem = Float.parseFloat(SolarServerData.timeRemaining);

        if (hours_rem > HIGH_THRESH_BATTERY) return TIME_HIGH;
        else if (hours_rem > MED_THRESH_BATTERY) return TIME_MED;
        else return TIME_LOW;
    }

    static ResourceLocation GetBatteryArrowIcon() {
        if (SolarServerData.battOverallCurrent > 0) return BATT_CHARGING;
        else return BATT_DISCHARGE;
    }

}