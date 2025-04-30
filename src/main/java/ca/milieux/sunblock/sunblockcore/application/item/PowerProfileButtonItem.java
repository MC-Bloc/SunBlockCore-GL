package ca.milieux.sunblock.sunblockcore.application.item;

import net.minecraft.world.level.block.Block;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PowerProfileButtonItem extends Block {

    public PowerProfileButtonItem(Properties pProperties) {
        super(pProperties);
    }

    public static void PerformanceMode(){
        try {
            URL switch_performance = new URL("");
            URLConnection yc = switch_performance.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        } catch (Exception e) {
            System.err.println("SunBlockCore::DataQueryProcess -- PerformanceMode() Error " + e.getMessage());
        }
    }

    public static void PowerSaverMode(){
        try {
            URL switch_powersaver = new URL("");
            URLConnection yc = switch_powersaver.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        } catch (Exception e) {
            System.err.println("SunBlockCore::DataQueryProcess -- PowerSaverMode() Error " + e.getMessage());
        }
    }
}
