package ca.milieux.sunblock.core.application.item;

import ca.milieux.sunblock.core.services.DataQueryProcess;
import ca.milieux.sunblock.core.services.SolarDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class SolarPickaxe extends PickaxeItem {
    public SolarPickaxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

//    @Override
//    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker){
//
//        //Additional Damage to target when struck with solar sword
//        float inc_damage = DataQueryProcess.GetServerData(SolarDataTypes.PVPOWER) / 10;
//        pTarget.setHealth(pTarget.getHealth() - inc_damage);
//
//        return super.hurtEnemy(pStack, pTarget, pAttacker);
//    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        float inc_damage = DataQueryProcess.GetServerData(SolarDataTypes.PVPOWER) / 5;

        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak((int) 20, pEntityLiving, (p) -> {
                p.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }
    //    Healing
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player && player.getMainHandItem() == pStack  && player.isShiftKeyDown()) {
            int recover = pStack.getDamageValue() - (int) Math.ceil(DataQueryProcess.GetServerData(SolarDataTypes.PVPOWER) / 20);
            pStack.setDamageValue(recover);
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return super.isEnchantable(pStack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }
}
