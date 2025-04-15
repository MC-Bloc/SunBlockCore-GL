package ca.milieux.sunblock.sunblockcore.application.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private int solarStatsKey = InputConstants.KEY_G;

    public final KeyMapping showSolarStats = new KeyMapping(
            "key." + SunBlockCore.MODID + ".show_solar_stats",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(solarStatsKey, -1),
            KeyMapping.CATEGORY_MISC );

    public static final KeyMapping SUNBLOCK_SOLAR_HUD_KEY = new KeyMapping(
            "key.solarhud.solar_hud",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.solarhud"
    );

    public static final KeyMapping SOLAR_HUD_SETTINGS_KEY = new KeyMapping(
            "key.solarhud.solar_hud_settings",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.solarhud"
    );

    private KeyBindings() {}

}
