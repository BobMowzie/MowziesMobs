package com.bobmowzie.mowziesmobs.common.ai.animation;

import com.bobmowzie.mowziesmobs.common.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class AnimationAttackAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    protected EntityLivingBase entityTarget;
    protected String attackSound;
    protected float knockback = 1;
    protected float range;
    protected float damageMultiplier;
    protected int damageFrame;
    protected String hitSound;

    public AnimationAttackAI(T entity, Animation animation, String attackSound, String hitSound, float knockback, float range, float damageMultiplier, int damageFrame) {
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

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = animatingEntity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (animatingEntity.getAnimationTick() < damageFrame && entityTarget != null) {
            animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
        }
        if (animatingEntity.getAnimationTick() == damageFrame) {
            float damage = (float) animatingEntity.getAttack();
            if (entityTarget != null && animatingEntity.targetDistance <= range) {
                entityTarget.attackEntityFrom(DamageSource.causeMobDamage(animatingEntity), damage * damageMultiplier);
                entityTarget.motionX *= knockback;
                entityTarget.motionZ *= knockback;
                animatingEntity.playSound(hitSound, 1, 1);
            }
            animatingEntity.playSound(attackSound, 1, 1);
        }
    }
}
