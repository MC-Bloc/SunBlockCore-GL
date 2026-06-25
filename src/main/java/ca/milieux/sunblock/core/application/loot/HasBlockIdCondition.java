package ca.milieux.sunblock.core.application.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

// Matches a block by registry id instead of a resolved Block instance, so loot
// modifiers can target blocks from mods that aren't a build dependency of this
// project — only the runtime registry (populated by the modpack) is consulted.
public class HasBlockIdCondition implements LootItemCondition {
    public static final Serializer<HasBlockIdCondition> SERIALIZER = new Serializer<>() {
        @Override
        public void serialize(JsonObject json, HasBlockIdCondition value, JsonSerializationContext context) {
            json.addProperty("block", value.blockId.toString());
        }

        @Override
        public HasBlockIdCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new HasBlockIdCondition(new ResourceLocation(json.get("block").getAsString()));
        }
    };

    private final ResourceLocation blockId;

    public HasBlockIdCondition(ResourceLocation blockId) {
        this.blockId = blockId;
    }

    public static LootItemCondition.Builder hasBlockId(ResourceLocation blockId) {
        return () -> new HasBlockIdCondition(blockId);
    }

    @Override
    public boolean test(LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        return state != null && state.is(ForgeRegistries.BLOCKS.getValue(blockId));
    }

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootModifiers.HAS_BLOCK_ID.get();
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }
}
