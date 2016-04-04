package com.bobmowzie.mowziesmobs.common.ai.animation;

import com.bobmowzie.mowziesmobs.common.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

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
