package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationBlockAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationBlockAI(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entity != null && entity.blockingEntity != null) {
            entity.faceEntity(entity.blockingEntity, 100, 100);
            entity.getLookHelper().setLookPositionWithEntity(entity.blockingEntity, 200F, 30F);
        }
    }
}