package ca.milieux.sunblock.sunblockcore.application.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private int solarStatsKey = InputConstants.KEY_G;

    public final KeyMapping showSolarStats = new KeyMapping(
            "key." + SunBlockCore.MODID + ".show_solar_stats",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(solarStatsKey, -1),
            KeyMapping.CATEGORY_MISC );

    private KeyBindings() {}

}
