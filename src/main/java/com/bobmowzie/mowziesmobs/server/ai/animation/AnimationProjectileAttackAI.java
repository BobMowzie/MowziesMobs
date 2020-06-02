package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.SoundEvent;

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
        entityTarget = entity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entityTarget != null) {
            entity.faceEntity(entityTarget, 100, 100);
            entity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
            if (entity.getAnimationTick() == attackFrame) {
                entity.attackEntityWithRangedAttack(entityTarget, 0);
                entity.playSound(attackSound, 1, 1);
            }
        }
    }
}
