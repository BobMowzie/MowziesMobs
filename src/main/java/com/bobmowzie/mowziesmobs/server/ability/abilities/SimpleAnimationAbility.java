package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.entity.LivingEntity;

public class SimpleAnimationAbility extends Ability {
    private String animation;

    public SimpleAnimationAbility(AbilityType<SimpleAnimationAbility> abilityType, LivingEntity user, String animationName, int duration) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        animation = animationName;
    }

    @Override
    public void start() {
        super.start();
        playAnimation(animation, false);
    }
}
