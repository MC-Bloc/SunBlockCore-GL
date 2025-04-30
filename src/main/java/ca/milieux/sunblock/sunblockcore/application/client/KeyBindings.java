package ca.milieux.sunblock.sunblockcore.application.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private int _solarStatsKey = InputConstants.KEY_G;
    private int _HUDCycleForward = InputConstants.KEY_F8;
    private int _HUDCycleBackward = InputConstants.KEY_F7;

    public final KeyMapping showSolarStats = new KeyMapping(
            "key." + SunBlockCore.MODID + ".show_solar_stats",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(_solarStatsKey, -1),
            KeyMapping.CATEGORY_MISC );

    public final KeyMapping HUDCycleForward = new KeyMapping(
            "key." + SunBlockCore.MODID + ".switch_hud_forward",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(_HUDCycleForward, -1),
            KeyMapping.CATEGORY_MISC );

    public final KeyMapping HUDCycleBackwards = new KeyMapping(
            "key." + SunBlockCore.MODID + ".switch_hud_backwards",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(_HUDCycleBackward, -1),
            KeyMapping.CATEGORY_MISC );


    private KeyBindings() {}

}
