package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationAI<T extends MowzieEntity & IAnimatedEntity> extends net.ilexiconn.llibrary.server.animation.AnimationAI<T> {
    protected Animation animation;
    protected boolean hurtInterruptsAnimation = false;

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

    public AnimationAI(T entity, Animation animation, boolean interruptsAI, boolean hurtInterruptsAnimation) {
        super(entity);
        this.animation = animation;
        if (!interruptsAI) {
            setMutexBits(8);
        }
        this.hurtInterruptsAnimation = hurtInterruptsAnimation;
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
        entity.hurtInterruptsAnimation = hurtInterruptsAnimation;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.entity.getAnimation() == this.getAnimation() && this.entity.getAnimationTick() < this.getAnimation().getDuration();
    }

    @Override
    public void resetTask() {
        if (this.entity.getAnimation() == this.getAnimation()) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
        }
        if (entity.currentAnim == this) {
            entity.currentAnim = null;
        }
    }
}
