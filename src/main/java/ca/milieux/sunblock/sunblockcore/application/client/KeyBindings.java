package ca.milieux.sunblock.sunblockcore.application.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private int _HUDDetailsKey = InputConstants.KEY_U;
    private int _HUDCycleForward = InputConstants.KEY_F8;

    public final KeyMapping HUDDetailsKey = new KeyMapping(
            "key." + SunBlockCore.MODID + ".hud_details_key",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(_HUDDetailsKey, -1),
            KeyMapping.CATEGORY_MISC );

    public final KeyMapping HUDCycleForward = new KeyMapping(
            "key." + SunBlockCore.MODID + ".switch_hud_forward",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(_HUDCycleForward, -1),
            KeyMapping.CATEGORY_MISC );


    private KeyBindings() {}

}
