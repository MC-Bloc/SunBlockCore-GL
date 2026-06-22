package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SunBlockCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        buttonBlock((ButtonBlock) ModBlocks.POWER_BUTTON.get(), ResourceLocation.fromNamespaceAndPath(SunBlockCore.MODID, "block/power_button"));

    }

    private void blockWithItem (DeferredHolder<Block, Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}

