package com.bobmowzie.mowziesmobs.server.ai.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;

public class AnimationProjectileAttackAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    private EntityLivingBase entityTarget;
    private int attackFrame;
    private String attackSound;

    public AnimationProjectileAttackAI(T entity, Animation animation, int attackFrame, String attackSound) {
        super(entity, animation);
        this.entityTarget = null;
        this.attackFrame = attackFrame;
        this.attackSound = attackSound;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entityTarget = animatingEntity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.entityTarget != null) {
            animatingEntity.faceEntity(entityTarget, 100, 100);
            animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
            if (animatingEntity.getAnimationTick() == attackFrame) {
                ((IRangedAttackMob) animatingEntity).attackEntityWithRangedAttack(entityTarget, 0);
                animatingEntity.playSound(attackSound, 1, 1);
            }
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }
}

