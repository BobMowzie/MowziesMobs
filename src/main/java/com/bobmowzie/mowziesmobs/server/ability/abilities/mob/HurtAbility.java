package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import software.bernie.geckolib.core.animation.RawAnimation;

public class HurtAbility<T extends MowzieGeckoEntity> extends SimpleAnimationAbility<T> {
    public HurtAbility(AbilityType<T, ? extends HurtAbility<T>> abilityType, T user, RawAnimation animation, int duration) {
        super(abilityType, user, animation, duration);
    }

    public HurtAbility(AbilityType<T, ? extends HurtAbility<T>> abilityType, T user, RawAnimation animation, int duration, int cooldown) {
        this(abilityType, user, animation, duration);
        this.cooldownMax = cooldown;
    }

    @Override
    public boolean damageInterrupts() {
        return true;
    }

    @Override
    public boolean canCancelSelf() {
        return true;
    }
}
