package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationDieAI<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationDieAI(T entity) {
        super(entity, entity.getDeathAnimation(), false);
    }
}
