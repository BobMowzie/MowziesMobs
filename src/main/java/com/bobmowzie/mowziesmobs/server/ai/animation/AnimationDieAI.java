package com.bobmowzie.mowziesmobs.server.ai.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;

public class AnimationDieAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationDieAI(T entity) {
        super(entity, entity.getDeathAnimation());
        setMutexBits(8);
    }

    @Override
    public Animation getAnimation() {
        return entity.getDeathAnimation();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        animatingEntity.setAnimationTick(0);
    }

    @Override
    public void updateTask() {
        super.updateTask();
    }
}
