package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationBlockAI<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationBlockAI(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void tick() {
        super.tick();
        if (entity != null && entity.blockingEntity != null) {
            entity.lookAt(entity.blockingEntity, 100, 100);
            entity.getLookControl().setLookAt(entity.blockingEntity, 200F, 30F);
        }
    }
}