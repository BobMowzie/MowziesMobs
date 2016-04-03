package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationAI<T extends MMEntityBase & IAnimatedEntity> extends net.ilexiconn.llibrary.server.animation.AnimationAI<T> {
    protected T animatingEntity;
    protected Animation animation;

    public AnimationAI(T entity, Animation animation) {
        super(entity);
        this.animation = animation;
        this.animatingEntity = entity;
    }

    public AnimationAI(T entity, Animation animation, boolean interruptsAI) {
        super(entity);
        this.animation = animation;
        this.animatingEntity = entity;
        if (!interruptsAI) {
            setMutexBits(8);
        }
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        animatingEntity.currentAnim = this;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        animatingEntity.currentAnim = null;
    }
}
