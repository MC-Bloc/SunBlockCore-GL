package ca.milieux.sunblock.sunblockcore.application.datagen;

import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import ca.milieux.sunblock.sunblockcore.application.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocksStateProvider extends BlockStateProvider {
    public ModBlocksStateProvider(PackOutput output,  ExistingFileHelper exFileHelper) {
        super(output, SunBlockCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.POWER_BUTTON);
    }

    private void blockWithItem (RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}

