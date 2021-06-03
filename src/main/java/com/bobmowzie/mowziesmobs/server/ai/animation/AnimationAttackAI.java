package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;

import java.util.EnumSet;

public class AnimationAttackAI<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    protected LivingEntity entityTarget;
    protected SoundEvent attackSound;
    protected float applyKnockbackMultiplier = 1;
    protected float range;
    protected float damageMultiplier;
    protected int damageFrame;
    protected SoundEvent hitSound;

    public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float applyKnockback, float range, float damageMultiplier, int damageFrame) {
        this(entity, animation, attackSound, hitSound, applyKnockback, range, damageMultiplier, damageFrame, false);
    }

    public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float applyKnockbackMultiplier, float range, float damageMultiplier, int damageFrame, boolean hurtInterrupts) {
        super(entity, animation, false, hurtInterrupts);
        this.entityTarget = null;
        this.attackSound = attackSound;
        this.applyKnockbackMultiplier = applyKnockbackMultiplier;
        this.range = range;
        this.damageMultiplier = damageMultiplier;
        this.damageFrame = damageFrame;
        this.hitSound = hitSound;
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = entity.getAttackTarget();
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() < damageFrame && entityTarget != null) {
            entity.faceEntity(entityTarget, 30F, 30F);
        }
        if (entity.getAnimationTick() == damageFrame) {
            if (entityTarget != null && entity.targetDistance <= range) {
                entity.attackEntityAsMob(entityTarget, damageMultiplier, applyKnockbackMultiplier);
                onAttack(entityTarget, damageMultiplier, applyKnockbackMultiplier);
                if (hitSound != null) {
                    entity.playSound(hitSound, 1, 1);   
                }
            }
            if (attackSound != null) {
                entity.playSound(attackSound, 1, 1);   
            }
        }
    }

    protected void onAttack(LivingEntity entityTarget, float damageMultiplier, float applyKnockbackMultiplier) {

    }
}
