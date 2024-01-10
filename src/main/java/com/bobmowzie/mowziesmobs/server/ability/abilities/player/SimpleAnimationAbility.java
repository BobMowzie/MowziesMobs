package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;

public class SimpleAnimationAbility<T extends LivingEntity> extends Ability<T> {
    private String animationName;
    private int duration;
    protected boolean hurtInterrupts;

    public SimpleAnimationAbility(AbilityType<T, ? extends SimpleAnimationAbility<T>> abilityType, T user, String animationName, int duration) {
        this(abilityType, user, animationName, duration, false);
    }

    public SimpleAnimationAbility(AbilityType<T, ? extends SimpleAnimationAbility<T>> abilityType, T user, String animationName, int duration, boolean hurtInterrupts) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, duration)
        });
        this.animationName = animationName;
        this.duration = duration;
        this.hurtInterrupts = hurtInterrupts;
    }

    @Override
    public void start() {
        super.start();
        playAnimation(animationName, false);
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean damageInterrupts() {
        return hurtInterrupts;
    }
}
