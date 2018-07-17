package com.bobmowzie.mowziesmobs.server.ai.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;

public class AnimationBlockAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationBlockAI(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.faceEntity(entity.blockingEntity, 100, 100);
        entity.getLookHelper().setLookPositionWithEntity(entity.blockingEntity, 200F, 30F);
    }
}