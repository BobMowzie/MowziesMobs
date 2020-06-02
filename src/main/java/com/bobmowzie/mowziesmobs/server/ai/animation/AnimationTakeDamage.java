package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
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
