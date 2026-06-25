package ca.milieux.sunblock.core.application.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public class AddItemModifier extends LootModifier {

    // Stored as a raw id rather than a resolved Item so the item's owning mod doesn't
    // need to be present at datagen time — only at actual gameplay time, when it's
    // resolved against the live registry (see doApply).
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ResourceLocation.CODEC
            .fieldOf("item").forGetter(m -> m.itemId)).apply(inst, AddItemModifier::new)));
    private final ResourceLocation itemId;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        this(conditionsIn, ForgeRegistries.ITEMS.getKey(item));
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, ResourceLocation itemId) {
        super(conditionsIn);
        this.itemId = itemId;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for(LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        Item resolvedItem = ForgeRegistries.ITEMS.getValue(this.itemId);
        if (resolvedItem != null) {
            generatedLoot.add(new ItemStack(resolvedItem));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
