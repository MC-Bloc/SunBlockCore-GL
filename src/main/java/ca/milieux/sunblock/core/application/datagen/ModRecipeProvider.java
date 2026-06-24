package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.application.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

/*Datagen provider – generates crafting recipes for the solar tools. All five share
the same ingredient set (gray stained glass pane, copper block, lightning rod) in
different patterns, and unlock once the player has a copper block.*/
public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        solarTool(writer, RecipeCategory.COMBAT, ModItems.SOLAR_SWORD.get(), "GCG", "GCG", "GLG");
        solarTool(writer, RecipeCategory.TOOLS, ModItems.SOLAR_PICKAXE.get(), "CCC", "GLG", "GLG");
        solarTool(writer, RecipeCategory.TOOLS, ModItems.SOLAR_AXE.get(), "CCG", "CLG", "GLG");
        solarTool(writer, RecipeCategory.TOOLS, ModItems.SOLAR_SHOVEL.get(), "GCG", "GLG", "GLG");
        solarTool(writer, RecipeCategory.TOOLS, ModItems.SOLAR_HOE.get(), "CCG", "GLG", "GLG");
    }

    private void solarTool(Consumer<FinishedRecipe> writer, RecipeCategory category, ItemLike result, String row0, String row1, String row2) {
        ShapedRecipeBuilder.shaped(category, result)
                .pattern(row0)
                .pattern(row1)
                .pattern(row2)
                .define('G', Items.GRAY_STAINED_GLASS_PANE)
                .define('C', Items.COPPER_BLOCK)
                .define('L', Items.LIGHTNING_ROD)
                .unlockedBy("has_copper_block", has(Items.COPPER_BLOCK))
                .showNotification(true)
                .save(writer);
    }
}
