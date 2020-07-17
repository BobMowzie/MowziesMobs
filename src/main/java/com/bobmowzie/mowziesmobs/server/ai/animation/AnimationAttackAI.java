package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

public class AnimationAttackAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    protected EntityLivingBase entityTarget;
    protected SoundEvent attackSound;
    protected float knockback = 1;
    protected float range;
    protected float damageMultiplier;
    protected int damageFrame;
    protected SoundEvent hitSound;

    public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float knockback, float range, float damageMultiplier, int damageFrame) {
        super(entity, animation);
        setMutexBits(8);
        this.entity = entity;
        this.entityTarget = null;
        this.attackSound = attackSound;
        this.knockback = knockback;
        this.range = range;
        this.damageMultiplier = damageMultiplier;
        this.damageFrame = damageFrame;
        this.hitSound = hitSound;
    }

    public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float knockback, float range, float damageMultiplier, int damageFrame, boolean hurtInterrupts) {
        this(entity, animation, attackSound, hitSound, knockback, range, damageMultiplier, damageFrame);
        this.hurtInterruptsAnimation = hurtInterrupts;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = entity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entity.getAnimationTick() < damageFrame && entityTarget != null) {
            entity.faceEntity(entityTarget, 30F, 30F);
        }
        if (entity.getAnimationTick() == damageFrame) {
            float damage = (float) entity.getAttack();
            if (entityTarget != null && entity.targetDistance <= range) {
                entityTarget.attackEntityFrom(DamageSource.causeMobDamage(entity), damage * damageMultiplier);
                entityTarget.motionX *= knockback;
                entityTarget.motionZ *= knockback;
                if (hitSound != null) {
                    entity.playSound(hitSound, 1, 1);   
                }
            }
            if (attackSound != null) {
                entity.playSound(attackSound, 1, 1);   
            }
        }
    }
}
