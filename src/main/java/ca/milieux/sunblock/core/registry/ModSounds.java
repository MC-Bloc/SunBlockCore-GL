package ca.milieux.sunblock.core.registry;

import ca.milieux.sunblock.core.SunBlockCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import net.neoforged.bus.api.IEventBus;

public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SunBlockCore.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> POWER_ON  = register("power_on");
    public static final DeferredHolder<SoundEvent, SoundEvent> POWER_OFF = register("power_off");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init(IEventBus bus) {
        SOUNDS.register(bus);
    }

    private ModSounds() {}
}