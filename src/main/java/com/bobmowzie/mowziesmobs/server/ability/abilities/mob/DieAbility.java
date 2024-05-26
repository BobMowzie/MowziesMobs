package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import software.bernie.geckolib.core.animation.RawAnimation;

public class DieAbility<T extends MowzieGeckoEntity> extends SimpleAnimationAbility<T> {
    public DieAbility(AbilityType<T, ? extends DieAbility<T>> abilityType, T user, RawAnimation animation, int duration) {
        super(abilityType, user, animation, duration);
    }

    @Override
    public boolean canCancelActiveAbility() {
        return true;
    }
}
