package ca.milieux.sunblock.core.services.setup;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.client.ClientEventHandler;
import ca.milieux.sunblock.core.application.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        //Register general client‑side events
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        //Defer property registration until thread‑safe time
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.SOLAR_SWORD.get(),
                    new ResourceLocation(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = ca.milieux.sunblock.core.domain.SolarServerData.getPvPower();
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });
            ItemProperties.register(ModItems.SOLAR_PICKAXE.get(),
                    new ResourceLocation(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = ca.milieux.sunblock.core.domain.SolarServerData.getPvPower();
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });

            ItemProperties.register(ModItems.SOLAR_AXE.get(),
                    new ResourceLocation(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = ca.milieux.sunblock.core.domain.SolarServerData.getPvPower();
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });

            ItemProperties.register(ModItems.SOLAR_SHOVEL.get(),
                    new ResourceLocation(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = ca.milieux.sunblock.core.domain.SolarServerData.getPvPower();
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });

            ItemProperties.register(ModItems.SOLAR_HOE.get(),
                    new ResourceLocation(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = ca.milieux.sunblock.core.domain.SolarServerData.getPvPower();
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });
        });
    }
}
