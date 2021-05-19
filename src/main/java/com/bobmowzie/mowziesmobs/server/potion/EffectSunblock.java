package com.bobmowzie.mowziesmobs.server.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class EffectSunblock extends MowzieEffect {
    public EffectSunblock() {
        super(EffectType.BENEFICIAL, 0xFFDF42);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);
        int k = 50 >> amplifier;
        if (k > 0 && entityLivingBaseIn.ticksExisted % k == 0) {
            if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
                entityLivingBaseIn.heal(1.0F);
            }
        }
    }
}
