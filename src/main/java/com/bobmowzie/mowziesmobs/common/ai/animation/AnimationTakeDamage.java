package com.bobmowzie.mowziesmobs.common.ai.animation;

import com.bobmowzie.mowziesmobs.common.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationTakeDamage<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationTakeDamage(T entity) {
        super(entity, entity.getHurtAnimation());
        setMutexBits(8);
    }

    @Override
    public Animation getAnimation() {
        return entity.getHurtAnimation();
    }
}
