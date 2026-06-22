package ca.milieux.sunblock.core.application.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ca.milieux.sunblock.core.SunBlockCore;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(SunBlockCore.MODID);

    public static final DeferredItem<Item> SOLAR_SWORD = ITEMS.register("solar_sword",
            () -> new SolarSword(
                    Tiers.IRON,
                    new Item.Properties().durability(600)
                            .attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4f))));

    public static final DeferredItem<Item> SOLAR_PICKAXE = ITEMS.register("solar_pickaxe",
            () -> new SolarPickaxe(
                    Tiers.IRON,
                    new Item.Properties().durability(600)
                            .attributes(DiggerItem.createAttributes(Tiers.IRON, 3.0f, -2.4f))));

    public static final DeferredItem<Item> SOLAR_AXE = ITEMS.register("solar_axe",
            () -> new SolarAxe(
                    Tiers.IRON,
                    new Item.Properties().durability(600)
                            .attributes(DiggerItem.createAttributes(Tiers.IRON, 3.0f, -2.4f))));

    public static final DeferredItem<Item> SOLAR_HOE = ITEMS.register("solar_hoe",
            () -> new SolarHoe(
                    Tiers.IRON,
                    new Item.Properties().durability(600)
                            .attributes(DiggerItem.createAttributes(Tiers.IRON, 3.0f, -2.4f))));

    public static final DeferredItem<Item> SOLAR_SHOVEL = ITEMS.register("solar_shovel",
            () -> new SolarShovel(
                    Tiers.IRON,
                    new Item.Properties().durability(600)
                            .attributes(DiggerItem.createAttributes(Tiers.IRON, 3.0f, -2.4f))));

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}

}
