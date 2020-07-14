package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationAI<T extends MowzieEntity & IAnimatedEntity> extends net.ilexiconn.llibrary.server.animation.AnimationAI<T> {
    protected Animation animation;

    public AnimationAI(T entity, Animation animation) {
        super(entity);
        this.animation = animation;
    }

    public AnimationAI(T entity, Animation animation, boolean interruptsAI) {
        super(entity);
        this.animation = animation;
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
        entity.currentAnim = this;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.entity.getAnimation() == this.getAnimation() && this.entity.getAnimationTick() < this.getAnimation().getDuration();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        entity.currentAnim = null;
    }
}
