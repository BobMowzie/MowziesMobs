package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationTakeDamage<T extends MMEntityBase & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationTakeDamage(T entity) {
        super(entity, MMEntityBase.DAMAGE_ANIMATION);
        setMutexBits(8);
    }

    @Override
    public Animation getAnimation() {
        return MMEntityBase.DAMAGE_ANIMATION;
    }
}
