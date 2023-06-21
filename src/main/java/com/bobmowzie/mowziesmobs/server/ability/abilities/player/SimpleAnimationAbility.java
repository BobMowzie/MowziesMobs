package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;

public class SimpleAnimationAbility<T extends LivingEntity> extends Ability<T> {
    private String animationName;

    public SimpleAnimationAbility(AbilityType<T, SimpleAnimationAbility<T>> abilityType, T user, String animationName, int duration) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, duration)
        });
        this.animationName = animationName;
    }

    @Override
    public void start() {
        super.start();
        playAnimation(animationName, false);
    }
}
