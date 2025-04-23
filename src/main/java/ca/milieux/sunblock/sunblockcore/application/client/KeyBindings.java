package ca.milieux.sunblock.sunblockcore.application.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private int solarStatsKey = InputConstants.KEY_G;
    private int switchHUDKey = InputConstants.KEY_F8;

    public final KeyMapping showSolarStats = new KeyMapping(
            "key." + SunBlockCore.MODID + ".show_solar_stats",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(solarStatsKey, -1),
            KeyMapping.CATEGORY_MISC );

    public final KeyMapping switchHUD = new KeyMapping(
            "key." + SunBlockCore.MODID + ".switch_hud_type",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(switchHUDKey, -1),
            KeyMapping.CATEGORY_MISC );


    private KeyBindings() {}

}
