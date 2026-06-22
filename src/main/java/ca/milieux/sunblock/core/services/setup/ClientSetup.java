package ca.milieux.sunblock.core.services.setup;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.item.ModItems;
import ca.milieux.sunblock.core.application.client.SolarServerData;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        // ClientEventHandler is registered automatically via @Mod.EventBusSubscriber on the mod bus.

        //Defer property registration until thread‑safe time
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.SOLAR_SWORD.get(),
                    ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = SolarServerData.pvPower;
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });
            ItemProperties.register(ModItems.SOLAR_PICKAXE.get(),
                    ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = SolarServerData.pvPower;
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });

            ItemProperties.register(ModItems.SOLAR_AXE.get(),
                    ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = SolarServerData.pvPower;
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });

            ItemProperties.register(ModItems.SOLAR_SHOVEL.get(),
                    ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = SolarServerData.pvPower;
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });

            ItemProperties.register(ModItems.SOLAR_HOE.get(),
                    ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "power"),
                    (stack, level, entity, seed) -> {
                        //Use live cached PV power even when level == null (inventory)
                        float pv = SolarServerData.pvPower;
                        if (pv >= 20f) return 1.0f;   //high
                        if (pv >= 10f) return 0.5f;   //medium
                        return 0.0f;                  //low
                    });
        });
    }
}
