package ca.milieux.sunblock.sunblockcore.application.item;

import ca.milieux.sunblock.sunblockcore.services.DataQueryProcess;
import ca.milieux.sunblock.sunblockcore.services.SolarDataTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

import java.util.function.Consumer;

public class SolarSword extends SwordItem {
    public SolarSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {

        System.out.println("SolarSword::hurtEnemy -- target Health" + pTarget.getHealth());

        float solar_voltage = DataQueryProcess.GetServerData(SolarDataTypes.PVVOLTAGE);
//        int solar_power = (int) SolarServerData.getPvPower();

        System.out.println("SolarSword::hurtEnemy -- Solar Voltage" + solar_voltage);

        pTarget.setHealth(pTarget.getHealth() - solar_voltage);

        System.out.println("SolarSword::hurtEnemy -- target Health" + pTarget.getHealth());

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, amount, entity, onBroken);


    }
}
