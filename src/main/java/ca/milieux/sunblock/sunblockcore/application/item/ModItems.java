package ca.milieux.sunblock.sunblockcore.application.item;

import ca.milieux.sunblock.sunblockcore.domain.SolarServerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import ca.milieux.sunblock.sunblockcore.services.setup.ClientSetup;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SunBlockCore.MODID);
    public static final RegistryObject<Item> SOLAR_SWORD = ITEMS.register("solar_sword",
            () -> new SolarSword(Tiers.IRON, 1, 2, new Item.Properties()));

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}

}
