package ca.milieux.sunblock.sunblockcore.application.item;

import ca.milieux.sunblock.sunblockcore.services.DataQueryProcess;
import ca.milieux.sunblock.sunblockcore.services.SolarDataTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Consumer;

public class SolarSword extends SwordItem  {
    public SolarSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker){
        System.out.println("SunBlockCore::SolarSword -- Health before: " + pTarget.getHealth());

        //Additional Damage to target when struck with solar sword
        float inc_damage = DataQueryProcess.GetServerData(SolarDataTypes.PVPOWER) / 10;
        pTarget.setHealth(pTarget.getHealth() - inc_damage);

        return super.hurtEnemy(pStack, pTarget, pAttacker);
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
