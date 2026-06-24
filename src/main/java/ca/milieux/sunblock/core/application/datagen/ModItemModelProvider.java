package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*Datagen provider – generates item models. The solar tools' item models (including the
power-level predicate overrides driven by the "sunblockcore:power" item property
registered in ClientSetup) are entirely hand-authored in src/main/resources — nothing
generates them, so they aren't registered here. */
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SunBlockCore.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Button inventory model
        buttonItem(ModBlocks.POWER_BUTTON);
    }

    private void buttonItem(RegistryObject<Block> block) {
        withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", modLoc("block/power_button"));
    }
}
