package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class MowzieAnimationController<T extends GeoAnimatable> extends AnimationController<T> {
    private double tickOffset;
    private double timingOffset;

    public MowzieAnimationController(T animatable, String name, int transitionLength, AnimationStateHandler<T> animationHandler, double timingOffset) {
        super(animatable, name, transitionLength, animationHandler);
        tickOffset = 0.0d;
        this.timingOffset = timingOffset;
    }

    public void playAnimation(T animatable, RawAnimation animation) {
        forceAnimationReset();
        setAnimation(animation);
        currentAnimation = this.animationQueue.poll();
        isJustStarting = true;
        adjustTick(animatable.getTick(animatable));
        transitionLength = 0;
    }

    @Override
    protected double adjustTick(double tick) {
        if (this.shouldResetTick) {
            if (getAnimationState() == State.TRANSITIONING) {
                this.tickOffset = tick;
            }
            else if (getAnimationState() == State.RUNNING) {
                this.tickOffset += transitionLength;
            }
            this.shouldResetTick = false;
        }
        double adjustedTick = Math.max(tick - this.tickOffset, 0.0D) + timingOffset;
        if (this.currentAnimation != null && this.currentAnimation.loopType().shouldPlayAgain(animatable, this, currentAnimation.animation())) adjustedTick = adjustedTick % this.currentAnimation.animation().length();
        if (adjustedTick == timingOffset) isJustStarting = true;
        return adjustedTick;
    }
}
