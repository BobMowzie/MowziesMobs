package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class AnimationAI<T extends MowzieEntity & IAnimatedEntity> extends EntityAIBase {
    protected final T entity;

    protected final boolean hurtInterruptsAnimation;

    protected AnimationAI(T entity) {
        this(entity, true, false);
    }

    protected AnimationAI(T entity, boolean interruptsAI) {
        this(entity, interruptsAI, false);
    }

    protected AnimationAI(T entity, boolean interruptsAI, boolean hurtInterruptsAnimation) {
        this.entity = entity;
        this.setMutexBits(interruptsAI ? 7 : 8);
        this.hurtInterruptsAnimation = hurtInterruptsAnimation;
    }

    @Override
    public boolean shouldExecute() {
        return this.test(this.entity.getAnimation());
    }

    @Override
    public void startExecuting() {
        this.entity.currentAnim = this;
        this.entity.hurtInterruptsAnimation = this.hurtInterruptsAnimation;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.test(this.entity.getAnimation()) && this.entity.getAnimationTick() < this.entity.getAnimation().getDuration();
    }

    @Override
    public void resetTask() {
        if (this.test(this.entity.getAnimation())) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
        }
        if (this.entity.currentAnim == this) {
            this.entity.currentAnim = null;
        }
    }

    protected abstract boolean test(Animation animation);
}
