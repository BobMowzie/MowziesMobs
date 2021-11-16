package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

import javax.annotation.Nullable;
import java.util.Collections;

public abstract class MowzieAnimatedGeoModel<T extends IAnimatable & IAnimationTickable> extends AnimatedGeoModel<T> {
    public MowzieAnimatedGeoModel() {
    }

    public MowzieGeoBone getMowzieBone(String boneName) {
        IBone bone = this.getBone(boneName);
        return (MowzieGeoBone) bone;
    }

    public boolean isInitialized() {
        return !this.getAnimationProcessor().getModelRendererList().isEmpty();
    }

    @Override
    public void setLivingAnimations(T entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        // Each animation has it's own collection of animations (called the
        // EntityAnimationManager), which allows for multiple independent animations
        AnimationData manager = entity.getFactory().getOrCreateAnimationData(uniqueID);
        if (manager.startTick == null) {
            manager.startTick = (double) (entity.tickTimer() + Minecraft.getInstance().getRenderPartialTicks());    // Set start ticks when animation starts playing
        }

        if (!Minecraft.getInstance().isGamePaused() || manager.shouldPlayWhilePaused) {
            manager.tick = (entity.tickTimer() + Minecraft.getInstance().getRenderPartialTicks());
            double gameTick = manager.tick;
            double deltaTicks = gameTick - lastGameTickTime;
            seekTime += deltaTicks;
            lastGameTickTime = gameTick;
        }

        AnimationEvent<T> predicate;
        if (customPredicate == null) {
            predicate = new AnimationEvent<T>(entity, 0, 0, 0, false, Collections.emptyList());
        } else {
            predicate = customPredicate;
        }

        predicate.animationTick = seekTime;
        getAnimationProcessor().preAnimationSetup(predicate.getAnimatable(), seekTime);
        if (!this.getAnimationProcessor().getModelRendererList().isEmpty()) {
            getAnimationProcessor().tickAnimation(entity, uniqueID, seekTime, predicate, GeckoLibCache.getInstance().parser,
                    shouldCrashOnMissing);
        }

        if (!Minecraft.getInstance().isGamePaused() || manager.shouldPlayWhilePaused) {
            codeAnimations(entity, uniqueID, customPredicate);
        }
    }

    public void codeAnimations(T entity, Integer uniqueID, AnimationEvent<?> customPredicate) {

    }

    public boolean resourceForModelId(AbstractClientPlayerEntity player) {
        return true;
    }

    public float getControllerValue(String controllerName) {
        if (!isInitialized()) return 1.0f;
        return 1.0f - getBone(controllerName).getPositionX();
    }
}
