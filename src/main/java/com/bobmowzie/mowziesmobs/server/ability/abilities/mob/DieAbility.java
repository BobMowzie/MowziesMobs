package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;

public class DieAbility<T extends MowzieGeckoEntity> extends SimpleAnimationAbility<T> {
    public DieAbility(AbilityType<T, ? extends DieAbility<T>> abilityType, T user, String animationName, int duration) {
        super(abilityType, user, animationName, duration);
    }

    @Override
    public boolean canCancelActiveAbility() {
        return true;
    }
}
