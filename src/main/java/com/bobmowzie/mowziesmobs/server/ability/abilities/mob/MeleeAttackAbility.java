package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

public class MeleeAttackAbility<T extends MowzieGeckoEntity> extends Ability<T> {
    protected SoundEvent attackSound;
    protected float applyKnockbackMultiplier = 1;
    protected float range;
    protected float damageMultiplier;
    protected SoundEvent hitSound;
    protected boolean hurtInterrupts;
    protected String animationName;

    public MeleeAttackAbility(AbilityType<T, MeleeAttackAbility<T>> abilityType, T user, String animationName, SoundEvent attackSound, SoundEvent hitSound, float applyKnockbackMultiplier, float range, float damageMultiplier, int startup, int recovery, boolean hurtInterrupts) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, startup),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, recovery)
        }, 0);
        this.attackSound = attackSound;
        this.hitSound = hitSound;
        this.applyKnockbackMultiplier = applyKnockbackMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.range = range;
        this.hurtInterrupts = hurtInterrupts;
        this.animationName = animationName;
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getUser().getTarget() != null) getUser().lookAt(getUser().getTarget(), 30F, 30F);
        getUser().getNavigation().stop();
    }

    @Override
    public void start() {
        super.start();
        playAnimation(animationName, false);
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            LivingEntity entityTarget = getUser().getTarget();
            if (entityTarget != null && getUser().targetDistance <= range) {
                getUser().doHurtTarget(entityTarget, damageMultiplier, applyKnockbackMultiplier);
                onAttack(entityTarget, damageMultiplier, applyKnockbackMultiplier);
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
