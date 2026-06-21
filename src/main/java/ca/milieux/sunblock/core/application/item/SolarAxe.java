package ca.milieux.sunblock.core.application.item;

import ca.milieux.sunblock.core.services.setup.ServerManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class SolarAxe extends AxeItem {
    public SolarAxe(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {

        //Additional Damage to target when struck with solar axe
        float inc_damage = ServerManager.cachedPvPower / 10;

        DamageSource source =
                pAttacker instanceof Player player ?
                        pAttacker.level().damageSources().playerAttack(player) :
                        pAttacker.level().damageSources().mobAttack(pAttacker);

        pTarget.hurt(source, inc_damage);

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    //    Healing
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player && player.getMainHandItem() == pStack && player.isShiftKeyDown()) {
            int recover = pStack.getDamageValue() - (int) Math.ceil(ServerManager.cachedPvPower / 20);
            pStack.setDamageValue(recover);
        }
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
