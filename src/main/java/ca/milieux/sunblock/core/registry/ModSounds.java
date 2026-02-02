package ca.milieux.sunblock.core.registry;

import ca.milieux.sunblock.core.SunBlockCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SunBlockCore.MODID);

    public static final RegistryObject<SoundEvent> POWER_ON  = register("power_on");
    public static final RegistryObject<SoundEvent> POWER_OFF = register("power_off");

    private static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(SunBlockCore.MODID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init(IEventBus bus) {
        SOUNDS.register(bus);
    }

    private ModSounds() {}
}