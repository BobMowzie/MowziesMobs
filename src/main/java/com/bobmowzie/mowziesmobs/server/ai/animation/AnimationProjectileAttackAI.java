package com.bobmowzie.mowziesmobs.server.ai.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.SoundEvent;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;

public class AnimationProjectileAttackAI<T extends MowzieEntity & IAnimatedEntity & IRangedAttackMob> extends AnimationAI<T> {
    private EntityLivingBase entityTarget;
    private int attackFrame;
    private SoundEvent attackSound;

    public AnimationProjectileAttackAI(T entity, Animation animation, int attackFrame, SoundEvent attackSound) {
        super(entity, animation);
        this.entityTarget = null;
        this.attackFrame = attackFrame;
        this.attackSound = attackSound;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = animatingEntity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entityTarget != null) {
            animatingEntity.faceEntity(entityTarget, 100, 100);
            animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
            if (animatingEntity.getAnimationTick() == attackFrame) {
                animatingEntity.attackEntityWithRangedAttack(entityTarget, 0);
                animatingEntity.playSound(attackSound, 1, 1);
            }
        }
    }
}
