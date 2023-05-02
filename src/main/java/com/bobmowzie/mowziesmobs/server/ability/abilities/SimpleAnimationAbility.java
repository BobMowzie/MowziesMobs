package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;

public class SimpleAnimationAbility extends Ability<LivingEntity> {
    private String animationName;

    public SimpleAnimationAbility(AbilityType<LivingEntity, SimpleAnimationAbility> abilityType, LivingEntity user, String animationName, int duration) {
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
