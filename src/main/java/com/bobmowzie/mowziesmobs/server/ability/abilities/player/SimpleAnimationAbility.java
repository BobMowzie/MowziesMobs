package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SimpleAnimationAbility<T extends LivingEntity> extends Ability<T> {
    private RawAnimation animation;
    private int duration;
    protected boolean hurtInterrupts;

    public SimpleAnimationAbility(AbilityType<T, ? extends SimpleAnimationAbility<T>> abilityType, T user, RawAnimation animation, int duration) {
        this(abilityType, user, animation, duration, false);
    }

    public SimpleAnimationAbility(AbilityType<T, ? extends SimpleAnimationAbility<T>> abilityType, T user, RawAnimation animation, int duration, boolean hurtInterrupts) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, duration)
        });
        this.animation = animation;
        this.duration = duration;
        this.hurtInterrupts = hurtInterrupts;
    }

    @Override
    public void start() {
        super.start();
        playAnimation(getAnimation());
    }

    public RawAnimation getAnimation() {
        return animation;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean damageInterrupts() {
        return hurtInterrupts;
    }
}
