package ca.milieux.sunblock.core.application.client;

import ca.milieux.sunblock.core.SunBlockCore;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private static final String CATEGORY = "key.categories." + SunBlockCore.MODID;

    public final KeyMapping HUDDetailsKey = new KeyMapping(
            "key." + SunBlockCore.MODID + ".hud_settings",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_U,
            CATEGORY
    );

    public final KeyMapping HUDCycleForward = new KeyMapping(
            "key." + SunBlockCore.MODID + ".cycle_hud",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_F8,
            CATEGORY
    );

    private KeyBindings() {}
}