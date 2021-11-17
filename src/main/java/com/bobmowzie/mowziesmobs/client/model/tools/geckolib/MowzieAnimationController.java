package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;

public class MowzieAnimationController<T extends IAnimatable & IAnimationTickable> extends AnimationController<T> {
    private double tickOffset;

    public MowzieAnimationController(T animatable, String name, float transitionLengthTicks, IAnimationPredicate<T> animationPredicate) {
        super(animatable, name, transitionLengthTicks, animationPredicate);
        tickOffset = 0.0d;
    }

    public void playAnimation(T animatable, AnimationBuilder animationBuilder) {
        markNeedsReload();
        setAnimation(animationBuilder);
        currentAnimation = this.animationQueue.peek();
        adjustTick(animatable.tickTimer());
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
        return Math.max(tick - this.tickOffset, 0.0D);
    }
}
