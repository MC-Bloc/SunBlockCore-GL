package ca.milieux.sunblock.core.application.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ca.milieux.sunblock.core.SunBlockCore;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SunBlockCore.MODID);

    public static final RegistryObject<Item> SOLAR_SWORD = ITEMS.register("solar_sword",
            () -> new SolarSword(
                    Tiers.IRON,
                    3,
                    -2.4f,
                    new Item.Properties()));

    public static final RegistryObject<Item> SOLAR_PICKAXE = ITEMS.register("solar_pickaxe",
            () -> new SolarPickaxe(
                    Tiers.IRON,
                    3,
                    -2.4f,
                    new Item.Properties()));

    public static final RegistryObject<Item> SOLAR_AXE = ITEMS.register("solar_axe",
            () -> new SolarAxe(
                    Tiers.IRON,
                    3,
                    -2.4f,
                    new Item.Properties()));

    public static final RegistryObject<Item> SOLAR_HOE = ITEMS.register("solar_hoe",
            () -> new SolarHoe(
                    Tiers.IRON,
                    3,
                    -2.4f,
                    new Item.Properties()));

    public static final RegistryObject<Item> SOLAR_SHOVEL = ITEMS.register("solar_shovel",
            () -> new SolarShovel(
                    Tiers.IRON,
                    3,
                    -2.4f,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}

}
