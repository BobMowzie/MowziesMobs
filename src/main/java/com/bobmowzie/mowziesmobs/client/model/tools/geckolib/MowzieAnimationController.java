package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.TestOnly;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoModel;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Queue;

public class MowzieAnimationController<T extends GeoAnimatable> extends AnimationController<T> {
    private double timingOffset;

    public MowzieAnimationController(T animatable, String name, int transitionLength, AnimationStateHandler<T> animationHandler, double timingOffset) {
        super(animatable, name, transitionLength, animationHandler);
        this.timingOffset = timingOffset;
    }

    // Normally, mobs won't start playing an animation until they are rendering on screen, even if the behavior starts off-screen
    // This combination of playAnimation and the overridden adjustTick fixes this issue

    public void playAnimation(T animatable, RawAnimation animation) {
        forceAnimationReset();
        setAnimation(animation);
        currentAnimation = this.animationQueue.poll();
        isJustStarting = true;
        adjustTick(animatable.getTick(animatable) + Minecraft.getInstance().getPartialTick());
        transitionLength = 0;
    }

    @Override
    protected double adjustTick(double tick) {
        if (this.shouldResetTick) {
            if (getAnimationState() == State.TRANSITIONING) {
                this.tickOffset = tick;
            }
            else if (getAnimationState() != State.STOPPED) {
                this.tickOffset += transitionLength;
            }
            this.shouldResetTick = false;
        }

        double adjustedTick = this.animationSpeedModifier.apply(this.animatable) * Math.max(tick - this.tickOffset, 0) + timingOffset;
        if (this.currentAnimation != null && this.currentAnimation.loopType() == Animation.LoopType.LOOP) adjustedTick = adjustedTick % this.currentAnimation.animation().length();
        if (adjustedTick == timingOffset) isJustStarting = true;
        return adjustedTick;
//        // I did this in a fugue state and I have no idea why it works
    }

    public void setLastModel(CoreGeoModel<T> coreGeoModel) {
        this.lastModel = coreGeoModel;
    }

    @TestOnly
    public <E extends GeoEntity> void checkAndReloadAnims() {
        if (
                this.lastModel != null &&
                getCurrentAnimation() != null &&
                getCurrentAnimation().animation() != null
        ) {
            String currentAnimationName = getCurrentAnimation().animation().name();
            Animation animation = this.lastModel.getAnimation(this.animatable, currentAnimationName);
            if (!animation.equals(getCurrentAnimation().animation())) {
                forceAnimationReset();
                currentAnimation = this.animationQueue.poll();
                isJustStarting = true;
                adjustTick(animatable.getTick(animatable) + Minecraft.getInstance().getPartialTick());
                transitionLength = 0;
            }
        }
    }
}
