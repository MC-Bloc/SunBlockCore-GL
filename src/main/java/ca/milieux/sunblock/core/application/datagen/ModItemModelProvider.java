package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

/*Datagen provider – generates item models. The Solar Sword now uses the base model
that contains overrides for power levels.*/
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SunBlockCore.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Use the custom base model that has predicate overrides
        withExistingParent("solar_sword", modLoc("item/solar_sword_base"));

        //Button inventory model
        buttonItem(ModBlocks.POWER_BUTTON);
    }

    //Utility for simple generated items (not used for Solar Sword anymore)
    private ItemModelBuilder simpleItem(DeferredHolder<Item, Item> item) {
        return withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }

    private void buttonItem(DeferredHolder<Block, ? extends Block> block) {
        withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", modLoc("block/power_button"));
    }
}
