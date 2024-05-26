package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animation.RawAnimation;

public class MeleeAttackAbility<T extends MowzieGeckoEntity> extends Ability<T> {
    protected SoundEvent attackSound;
    protected float knockBackMultiplier = 0;
    protected float range;
    protected float damageMultiplier;
    protected SoundEvent hitSound;
    protected boolean hurtInterrupts;
    protected RawAnimation[] animations;

    public MeleeAttackAbility(AbilityType<T, ? extends MeleeAttackAbility<T>> abilityType, T user, RawAnimation[] animations, SoundEvent attackSound, SoundEvent hitSound, float knockBackMultiplier, float range, float damageMultiplier, int startup, int recovery, boolean hurtInterrupts) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, startup),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, recovery)
        }, 0);
        this.attackSound = attackSound;
        this.hitSound = hitSound;
        this.knockBackMultiplier = knockBackMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.range = range;
        this.hurtInterrupts = hurtInterrupts;
        this.animations = animations;
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getUser().getTarget() != null) {
            getUser().lookAt(getUser().getTarget(), 30F, 30F);
            getUser().getLookControl().setLookAt(getUser().getTarget(), 30F, 30F);
        }
        getUser().getNavigation().stop();
        getUser().getMoveControl().strafe(0, 0);
        getUser().setStrafing(false);
    }

    @Override
    public void start() {
        super.start();
        RawAnimation animation = this.animations[getUser().getRandom().nextInt(animations.length)];
        playAnimation(animation);
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            LivingEntity entityTarget = getUser().getTarget();
            if (entityTarget != null && getUser().targetDistance <= range) {
                getUser().doHurtTarget(entityTarget, damageMultiplier, knockBackMultiplier);
                onAttack(entityTarget, damageMultiplier, knockBackMultiplier);
                if (hitSound != null) {
                    getUser().playSound(hitSound, 1, 1);
                }
            }
            if (attackSound != null) {
                getUser().playSound(attackSound, 1, 1);
            }
        }
    }

    protected void onAttack(LivingEntity entityTarget, float damageMultiplier, float applyKnockbackMultiplier) {

    }

    @Override
    public boolean damageInterrupts() {
        return hurtInterrupts;
    }
}
