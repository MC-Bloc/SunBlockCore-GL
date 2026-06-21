package ca.milieux.sunblock.core.application.block;

import ca.milieux.sunblock.core.SunBlockCore;

import ca.milieux.sunblock.core.application.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


// Code from KaupenJoe's brilliant 1.20.1 tutorial series! Thanks!

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SunBlockCore.MODID);

    public static final RegistryObject<Block> POWER_BUTTON = registerBlock("power_button",
            () -> new PowerButton(BlockSetType.IRON, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON).strength(-1.0F, 3600000.0F)));

    public static final RegistryObject<Block> SOLAR_SWITCH = registerBlock("solar_switch",
            () -> new SolarSwitchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .strength(-1.0F, 3600000.0F)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(SolarSwitchBlock.SWITCH_STATE) == SolarSwitchBlock.SolarSwitchState.ON ? 14 : 0)));

    public static final RegistryObject<Block> SOLAR_LIGHT_SWITCH = registerBlock("solar_light_switch",
            () -> new SolarLightSwitchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .strength(1.5F)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(SolarLightSwitchBlock.SWITCH_STATE) == SolarLightSwitchBlock.SolarLightSwitchState.ON ? 14 : 0)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
