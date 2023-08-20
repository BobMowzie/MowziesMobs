package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.easing.EasingType;

public class MowzieAnimationController<T extends IAnimatable & IAnimationTickable> extends AnimationController<T> {
    private double tickOffset;
    private double timingOffset;

    public MowzieAnimationController(T animatable, String name, float transitionLengthTicks, IAnimationPredicate<T> animationPredicate, double timingOffset) {
        super(animatable, name, transitionLengthTicks, animationPredicate);
        tickOffset = 0.0d;
        this.timingOffset = timingOffset;
    }

    public MowzieAnimationController(T animatable, String name, float transitionLengthTicks, EasingType easingType, IAnimationPredicate<T> animationPredicate, double timingOffset) {
        super(animatable, name, transitionLengthTicks, easingType, animationPredicate);
        tickOffset = 0.0d;
        this.timingOffset = timingOffset;
    }

    public void playAnimation(T animatable, AnimationBuilder animationBuilder) {
        markNeedsReload();
        setAnimation(animationBuilder);
        currentAnimation = this.animationQueue.poll();
        isJustStarting = true;
        adjustTick(animatable.tickTimer());
        transitionLengthTicks = 0;
    }

    @Override
    protected double adjustTick(double tick) {
        if (this.shouldResetTick) {
            if (getAnimationState() == AnimationState.Transitioning) {
                this.tickOffset = tick;
            }
            else if (getAnimationState() == AnimationState.Running) {
                this.tickOffset += transitionLengthTicks;
            }
            this.shouldResetTick = false;
        }
        double adjustedTick = Math.max(tick - this.tickOffset, 0.0D) + timingOffset;
        if (this.currentAnimation != null && this.currentAnimation.loop.isRepeatingAfterEnd()) adjustedTick = adjustedTick % this.currentAnimation.animationLength;
        if (adjustedTick == timingOffset) isJustStarting = true;
        return adjustedTick;
    }
}
