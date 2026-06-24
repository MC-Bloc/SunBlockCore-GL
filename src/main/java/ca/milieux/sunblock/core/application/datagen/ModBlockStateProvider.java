package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.block.ModBlocks;
import ca.milieux.sunblock.core.application.block.SolarLightSwitchBlock;
import ca.milieux.sunblock.core.application.block.SolarSwitchBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SunBlockCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        buttonBlock((ButtonBlock) ModBlocks.POWER_BUTTON.get(), new ResourceLocation(SunBlockCore.MODID, "block/power_button"));

        solarSwitchBlock(ModBlocks.SOLAR_SWITCH.get());
        solarLightSwitchBlock(ModBlocks.SOLAR_LIGHT_SWITCH.get());
    }

    // FACING includes DOWN even though SolarSwitchBlock/SolarLightSwitchBlock placement always
    // rejects ceiling placement — the variant validator still requires a model for that
    // unreachable state, which a hand-authored blockstate JSON previously omitted.
    private void solarSwitchBlock(Block block) {
        ModelFile offModel = models().getExistingFile(modLoc("block/solar_switch_off"));
        ModelFile pressedModel = models().getExistingFile(modLoc("block/solar_switch_pressed"));
        ModelFile onModel = models().getExistingFile(modLoc("block/solar_switch_on"));

        getVariantBuilder(block).forAllStates(state -> {
            ModelFile model = switch (state.getValue(SolarSwitchBlock.SWITCH_STATE)) {
                case OFF -> offModel;
                case PRESSED -> pressedModel;
                case ON -> onModel;
            };
            return facingRotations(state.getValue(BlockStateProperties.FACING), model);
        });
    }

    private void solarLightSwitchBlock(Block block) {
        ModelFile offModel = models().getExistingFile(modLoc("block/solar_switch_off"));
        ModelFile pressedModel = models().getExistingFile(modLoc("block/solar_switch_pressed"));
        ModelFile onModel = models().getExistingFile(modLoc("block/solar_switch_on"));

        getVariantBuilder(block).forAllStates(state -> {
            ModelFile model = switch (state.getValue(SolarLightSwitchBlock.SWITCH_STATE)) {
                case OFF -> offModel;
                case PRESSED -> pressedModel;
                case ON -> onModel;
            };
            return facingRotations(state.getValue(BlockStateProperties.FACING), model);
        });
    }

    private static ConfiguredModel[] facingRotations(Direction facing, ModelFile model) {
        int x = switch (facing) {
            case UP -> 0;
            case DOWN -> 180;
            default -> 90;
        };
        int y = switch (facing) {
            case SOUTH -> 180;
            case EAST -> 90;
            case WEST -> 270;
            default -> 0; // NORTH, UP, DOWN
        };
        return new ConfiguredModel[]{ new ConfiguredModel(model, x, y, false) };
    }
}
