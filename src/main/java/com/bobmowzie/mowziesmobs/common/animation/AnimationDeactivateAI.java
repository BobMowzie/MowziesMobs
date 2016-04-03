package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationDeactivateAI<T extends MMEntityBase & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationDeactivateAI(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        animatingEntity.active = false;
    }
}
