package ca.milieux.sunblock.core.application.datagen;

import ca.milieux.sunblock.core.SunBlockCore;
import ca.milieux.sunblock.core.application.item.ModItems;
import ca.milieux.sunblock.core.application.loot.AddItemModifier;
import ca.milieux.sunblock.core.application.loot.HasBlockIdCondition;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, SunBlockCore.MODID);
    }

    @Override
    protected void start() {

        float _probability = 0.37f;
        // Every sapling drop modifier must only trigger when broken with the solar hoe -
        // without this condition any tool would have a chance to drop the extra sapling.
        LootItemCondition matchSolarHoe = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModItems.SOLAR_HOE.get())).build();

        // Vanilla
        sapling("spruce_sapling_chance_with_solar_hoe", rl("minecraft", "spruce_leaves"), rl("minecraft", "spruce_sapling"), matchSolarHoe, _probability);
        sapling("acacia_sapling_chance_with_solar_hoe", rl("minecraft", "acacia_leaves"), rl("minecraft", "acacia_sapling"), matchSolarHoe, _probability);
        sapling("birch_sapling_chance_with_solar_hoe", rl("minecraft", "birch_leaves"), rl("minecraft", "birch_sapling"), matchSolarHoe, _probability);
        sapling("cherry_sapling_chance_with_solar_hoe", rl("minecraft", "cherry_leaves"), rl("minecraft", "cherry_sapling"), matchSolarHoe, _probability);
        sapling("oak_sapling_chance_with_solar_hoe", rl("minecraft", "oak_leaves"), rl("minecraft", "oak_sapling"), matchSolarHoe, _probability);
        sapling("dark_oak_sapling_chance_with_solar_hoe", rl("minecraft", "dark_oak_leaves"), rl("minecraft", "dark_oak_sapling"), matchSolarHoe, _probability);
        sapling("jungle_sapling_chance_with_solar_hoe", rl("minecraft", "jungle_leaves"), rl("minecraft", "jungle_sapling"), matchSolarHoe, _probability);
        sapling("mangrove_sapling_chance_with_solar_hoe", rl("minecraft", "mangrove_leaves"), rl("minecraft", "mangrove_propagule"), matchSolarHoe, _probability);

        // Enchanted
        sapling("enchanted_alder_sapling_chance_with_solar_hoe", rl("enchanted", "alder_leaves"), rl("enchanted", "alder_sapling"), matchSolarHoe, _probability);
        sapling("enchanted_hawthorn_sapling_chance_with_solar_hoe", rl("enchanted", "hawthorn_leaves"), rl("enchanted", "hawthorn_sapling"), matchSolarHoe, _probability);
        sapling("enchanted_rowan_sapling_chance_with_solar_hoe", rl("enchanted", "rowan_leaves"), rl("enchanted", "rowan_sapling"), matchSolarHoe, _probability);

        // Integrated Dynamics
        sapling("integrateddynamics_menril_sapling_chance_with_solar_hoe", rl("integrateddynamics", "menril_leaves"), rl("integrateddynamics", "menril_sapling"), matchSolarHoe, _probability);

        // Natural Decor
        sapling("naturaldecormod_juniper_sapling_chance_with_solar_hoe", rl("naturaldecormod", "juniper_leaves"), rl("naturaldecormod", "juniper_sapling"), matchSolarHoe, _probability);
        sapling("naturaldecormod_monkey_puzzle_cone_chance_with_solar_hoe", rl("naturaldecormod", "monkey_puzzle_leaves_ndm"), rl("naturaldecormod", "monkey_puzzle_cone"), matchSolarHoe, _probability);

        // Nature's Spirit
        sapling("natures_spirit_aspen_sapling_chance_with_solar_hoe", rl("natures_spirit", "aspen_leaves"), rl("natures_spirit", "aspen_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_blue_wisteria_sapling_chance_with_solar_hoe", rl("natures_spirit", "blue_wisteria_leaves"), rl("natures_spirit", "blue_wisteria_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_cedar_sapling_chance_with_solar_hoe", rl("natures_spirit", "cedar_leaves"), rl("natures_spirit", "cedar_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_coconut_sprout_chance_with_solar_hoe", rl("natures_spirit", "coconut_leaves"), rl("natures_spirit", "coconut_sprout"), matchSolarHoe, _probability);
        sapling("natures_spirit_cypress_sapling_chance_with_solar_hoe", rl("natures_spirit", "cypress_leaves"), rl("natures_spirit", "cypress_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_fir_sapling_chance_with_solar_hoe", rl("natures_spirit", "fir_leaves"), rl("natures_spirit", "fir_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_ghaf_sapling_chance_with_solar_hoe", rl("natures_spirit", "ghaf_leaves"), rl("natures_spirit", "ghaf_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_joshua_sapling_chance_with_solar_hoe", rl("natures_spirit", "joshua_leaves"), rl("natures_spirit", "joshua_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_larch_sapling_chance_with_solar_hoe", rl("natures_spirit", "larch_leaves"), rl("natures_spirit", "larch_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_mahogany_sapling_chance_with_solar_hoe", rl("natures_spirit", "mahogany_leaves"), rl("natures_spirit", "mahogany_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_olive_sapling_chance_with_solar_hoe", rl("natures_spirit", "olive_leaves"), rl("natures_spirit", "olive_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_orange_maple_sapling_chance_with_solar_hoe", rl("natures_spirit", "orange_maple_leaves"), rl("natures_spirit", "orange_maple_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_palo_verde_sapling_chance_with_solar_hoe", rl("natures_spirit", "palo_verde_leaves"), rl("natures_spirit", "palo_verde_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_pink_wisteria_sapling_chance_with_solar_hoe", rl("natures_spirit", "pink_wisteria_leaves"), rl("natures_spirit", "pink_wisteria_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_purple_wisteria_sapling_chance_with_solar_hoe", rl("natures_spirit", "purple_wisteria_leaves"), rl("natures_spirit", "purple_wisteria_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_red_maple_sapling_chance_with_solar_hoe", rl("natures_spirit", "red_maple_leaves"), rl("natures_spirit", "red_maple_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_redwood_sapling_chance_with_solar_hoe", rl("natures_spirit", "redwood_leaves"), rl("natures_spirit", "redwood_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_saxal_sapling_chance_with_solar_hoe", rl("natures_spirit", "saxaul_leaves"), rl("natures_spirit", "saxal_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_sugi_sapling_chance_with_solar_hoe", rl("natures_spirit", "sugi_leaves"), rl("natures_spirit", "sugi_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_white_wisteria_sapling_chance_with_solar_hoe", rl("natures_spirit", "white_wisteria_leaves"), rl("natures_spirit", "white_wisteria_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_willow_sapling_chance_with_solar_hoe", rl("natures_spirit", "willow_leaves"), rl("natures_spirit", "willow_sapling"), matchSolarHoe, _probability);
        sapling("natures_spirit_yellow_maple_sapling_chance_with_solar_hoe", rl("natures_spirit", "yellow_maple_leaves"), rl("natures_spirit", "yellow_maple_sapling"), matchSolarHoe, _probability);

        // Nature's Aura
        sapling("naturesaura_ancient_sapling_chance_with_solar_hoe", rl("naturesaura", "ancient_leaves"), rl("naturesaura", "ancient_sapling"), matchSolarHoe, _probability);

        // Primal Magick
        sapling("primalmagick_hallowood_sapling_chance_with_solar_hoe", rl("primalmagick", "hallowood_leaves"), rl("primalmagick", "hallowood_sapling"), matchSolarHoe, _probability);
        sapling("primalmagick_moonwood_sapling_chance_with_solar_hoe", rl("primalmagick", "moonwood_leaves"), rl("primalmagick", "moonwood_sapling"), matchSolarHoe, _probability);
        sapling("primalmagick_sunwood_sapling_chance_with_solar_hoe", rl("primalmagick", "sunwood_leaves"), rl("primalmagick", "sunwood_sapling"), matchSolarHoe, _probability);

        // Quark
        sapling("quark_ancient_sapling_chance_with_solar_hoe", rl("quark", "ancient_leaves"), rl("quark", "ancient_sapling"), matchSolarHoe, _probability);
        sapling("quark_blue_blossom_sapling_chance_with_solar_hoe", rl("quark", "blue_blossom_leaves"), rl("quark", "blue_blossom_sapling"), matchSolarHoe, _probability);
        sapling("quark_lavender_blossom_sapling_chance_with_solar_hoe", rl("quark", "lavender_blossom_leaves"), rl("quark", "lavender_blossom_sapling"), matchSolarHoe, _probability);
        sapling("quark_orange_blossom_sapling_chance_with_solar_hoe", rl("quark", "orange_blossom_leaves"), rl("quark", "orange_blossom_sapling"), matchSolarHoe, _probability);
        sapling("quark_red_blossom_sapling_chance_with_solar_hoe", rl("quark", "red_blossom_leaves"), rl("quark", "red_blossom_sapling"), matchSolarHoe, _probability);
        sapling("quark_yellow_blossom_sapling_chance_with_solar_hoe", rl("quark", "yellow_blossom_leaves"), rl("quark", "yellow_blossom_sapling"), matchSolarHoe, _probability);

        // Regions Unexplored
        sapling("regions_unexplored_alpha_sapling_chance_with_solar_hoe", rl("regions_unexplored", "alpha_leaves"), rl("regions_unexplored", "alpha_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_apple_oak_sapling_chance_with_solar_hoe", rl("regions_unexplored", "apple_oak_leaves"), rl("regions_unexplored", "apple_oak_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_ashen_sapling_chance_with_solar_hoe", rl("regions_unexplored", "ashen_leaves"), rl("regions_unexplored", "ashen_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_bamboo_sapling_chance_with_solar_hoe", rl("regions_unexplored", "bamboo_leaves"), rl("regions_unexplored", "bamboo_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_baobab_sapling_chance_with_solar_hoe", rl("regions_unexplored", "baobab_leaves"), rl("regions_unexplored", "baobab_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_blackwood_sapling_chance_with_solar_hoe", rl("regions_unexplored", "blackwood_leaves"), rl("regions_unexplored", "blackwood_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_blue_magnolia_sapling_chance_with_solar_hoe", rl("regions_unexplored", "blue_magnolia_leaves"), rl("regions_unexplored", "blue_magnolia_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_brimwood_sapling_chance_with_solar_hoe", rl("regions_unexplored", "brimwood_leaves"), rl("regions_unexplored", "brimwood_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_cobalt_sapling_chance_with_solar_hoe", rl("regions_unexplored", "cobalt_webbing"), rl("regions_unexplored", "cobalt_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_cypress_sapling_chance_with_solar_hoe", rl("regions_unexplored", "cypress_leaves"), rl("regions_unexplored", "cypress_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_dead_pine_sapling_chance_with_solar_hoe", rl("regions_unexplored", "dead_pine_leaves"), rl("regions_unexplored", "dead_pine_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_dead_sapling_chance_with_solar_hoe", rl("regions_unexplored", "dead_leaves"), rl("regions_unexplored", "dead_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_enchanted_birch_sapling_chance_with_solar_hoe", rl("regions_unexplored", "enchanted_birch_leaves"), rl("regions_unexplored", "enchanted_birch_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_eucalyptus_sapling_chance_with_solar_hoe", rl("regions_unexplored", "eucalyptus_leaves"), rl("regions_unexplored", "eucalyptus_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_flowering_sapling_chance_with_solar_hoe", rl("regions_unexplored", "flowering_leaves"), rl("regions_unexplored", "flowering_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_golden_larch_sapling_chance_with_solar_hoe", rl("regions_unexplored", "golden_larch_leaves"), rl("regions_unexplored", "golden_larch_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_joshua_sapling_chance_with_solar_hoe", rl("regions_unexplored", "joshua_leaves"), rl("regions_unexplored", "joshua_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_kapok_sapling_chance_with_solar_hoe", rl("regions_unexplored", "kapok_leaves"), rl("regions_unexplored", "kapok_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_larch_sapling_chance_with_solar_hoe", rl("regions_unexplored", "larch_leaves"), rl("regions_unexplored", "larch_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_magnolia_sapling_chance_with_solar_hoe", rl("regions_unexplored", "magnolia_leaves"), rl("regions_unexplored", "magnolia_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_maple_sapling_chance_with_solar_hoe", rl("regions_unexplored", "maple_leaves"), rl("regions_unexplored", "maple_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_mauve_sapling_chance_with_solar_hoe", rl("regions_unexplored", "mauve_leaves"), rl("regions_unexplored", "mauve_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_orange_maple_sapling_chance_with_solar_hoe", rl("regions_unexplored", "orange_maple_leaves"), rl("regions_unexplored", "orange_maple_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_palm_sapling_chance_with_solar_hoe", rl("regions_unexplored", "palm_leaves"), rl("regions_unexplored", "palm_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_pine_sapling_chance_with_solar_hoe", rl("regions_unexplored", "pine_leaves"), rl("regions_unexplored", "pine_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_pink_magnolia_sapling_chance_with_solar_hoe", rl("regions_unexplored", "pink_magnolia_leaves"), rl("regions_unexplored", "pink_magnolia_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_red_maple_sapling_chance_with_solar_hoe", rl("regions_unexplored", "red_maple_leaves"), rl("regions_unexplored", "red_maple_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_redwood_sapling_chance_with_solar_hoe", rl("regions_unexplored", "redwood_leaves"), rl("regions_unexplored", "redwood_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_silver_birch_sapling_chance_with_solar_hoe", rl("regions_unexplored", "silver_birch_leaves"), rl("regions_unexplored", "silver_birch_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_small_oak_sapling_chance_with_solar_hoe", rl("regions_unexplored", "small_oak_leaves"), rl("regions_unexplored", "small_oak_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_socotra_sapling_chance_with_solar_hoe", rl("regions_unexplored", "socotra_leaves"), rl("regions_unexplored", "socotra_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_white_magnolia_sapling_chance_with_solar_hoe", rl("regions_unexplored", "white_magnolia_leaves"), rl("regions_unexplored", "white_magnolia_sapling"), matchSolarHoe, _probability);
        sapling("regions_unexplored_willow_sapling_chance_with_solar_hoe", rl("regions_unexplored", "willow_leaves"), rl("regions_unexplored", "willow_sapling"), matchSolarHoe, _probability);
    }

    private void sapling(String key, ResourceLocation block, ResourceLocation item, LootItemCondition matchSolarHoe, float probability) {
        add(key, new AddItemModifier(new LootItemCondition[]{
                HasBlockIdCondition.hasBlockId(block).build(),
                LootItemRandomChanceCondition.randomChance(probability).build(),
                matchSolarHoe,
        }, item));
    }

    private static ResourceLocation rl(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
}
