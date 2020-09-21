package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;

import java.util.EnumSet;

public class AnimationTakeDamage<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationTakeDamage(T entity) {
        super(entity, entity.getHurtAnimation());
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }
}
