package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.item.ModItems;
import ca.milieux.sunblock.core.application.loot.AddItemModifier;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, SunBlockCore.MODID);
    }

    @Override
    protected void start() {

        float _probability = 0.2f;
        add("spruce_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SPRUCE_LEAVES).build(),
                LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.SPRUCE_SAPLING.asItem())
        );
        add("acacia_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ACACIA_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.ACACIA_SAPLING.asItem())
        );

        add("birch_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BIRCH_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.BIRCH_SAPLING.asItem())
        );
        add("cherry_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CHERRY_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.CHERRY_SAPLING.asItem())
        );

        add("oak_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.OAK_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.OAK_SAPLING.asItem())
        );

        add("dark_oak_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DARK_OAK_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.DARK_OAK_SAPLING.asItem())
        );
        add("dark_oak_sapling_chance_with_solar_hoe", new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.JUNGLE_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(_probability).build(),
                }, Blocks.JUNGLE_SAPLING.asItem())
        );
    }
}
