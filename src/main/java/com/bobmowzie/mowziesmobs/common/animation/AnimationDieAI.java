package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationDieAI<T extends MMEntityBase & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationDieAI(T entity) {
        super(entity, MMEntityBase.DIE_ANIMAION);
        setMutexBits(8);
    }

    @Override
    public Animation getAnimation() {
        return MMEntityBase.DIE_ANIMAION;
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
