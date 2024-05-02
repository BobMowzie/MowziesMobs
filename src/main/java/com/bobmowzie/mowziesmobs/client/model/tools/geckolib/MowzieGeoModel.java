package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.state.BoneSnapshot;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public abstract class MowzieGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    public MowzieGeoModel() {
    }

    public MowzieGeoBone getMowzieBone(String boneName) {
        Optional<GeoBone> bone = this.getBone(boneName);
        return (MowzieGeoBone) bone.orElse(null);
    }

    public boolean isInitialized() {
        return !this.getAnimationProcessor().getRegisteredBones().isEmpty();
    }

    public void resetBoneToSnapshot(CoreGeoBone bone) {
        BoneSnapshot initialSnapshot = bone.getInitialSnapshot();

        bone.setRotX(initialSnapshot.getRotX());
        bone.setRotY(initialSnapshot.getRotY());
        bone.setRotZ(initialSnapshot.getRotZ());

        bone.setPosX(initialSnapshot.getOffsetX());
        bone.setPosY(initialSnapshot.getOffsetY());
        bone.setPosZ(initialSnapshot.getOffsetZ());

        bone.setScaleX(initialSnapshot.getScaleX());
        bone.setScaleY(initialSnapshot.getScaleY());
        bone.setScaleZ(initialSnapshot.getScaleZ());
    }

    @Override
    public void applyMolangQueries(T animatable, double animTime) {
        getAnimationProcessor().getRegisteredBones().forEach(this::resetBoneToSnapshot);
        super.applyMolangQueries(animatable, animTime);
    }

    /* TODO: New function is called handleAnimations, but was made final
    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        if (animatable instanceof MowzieEntity && ((MowzieEntity) animatable).renderingInGUI) return;

        Minecraft mc = Minecraft.getInstance();
        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        AnimationEvent<T> predicate;
        double currentTick = animatable.tickTimer();

        if (manager.startTick == -1)
            manager.startTick = currentTick + mc.getFrameTime();

        if (!Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused) {
            manager.tick = currentTick + mc.getFrameTime();
            double gameTick = manager.tick;
            double deltaTicks = gameTick - this.lastGameTickTime;
            this.seekTime += deltaTicks;
            this.lastGameTickTime = gameTick;
        }

        predicate = animationEvent == null ? new AnimationEvent<T>(animatable, 0, 0, (float)(manager.tick - this.lastGameTickTime), false, Collections.emptyList()) : animationEvent;
        predicate.animationTick = this.seekTime;

        getAnimationProcessor().preAnimationSetup(predicate.getAnimatable(), this.seekTime);

        if (!getAnimationProcessor().getModelRendererList().isEmpty())
            getAnimationProcessor().tickAnimation(animatable, instanceId, this.seekTime, predicate, GeckoLibCache.getInstance().parser, this.shouldCrashOnMissing);

        if (!Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused) {
            codeAnimations(animatable, instanceId, animationEvent);
        }
    }*/

    public void codeAnimations(T entity, Integer uniqueID, AnimationState<?> customPredicate) {

    }

    public float getControllerValueInverted(String controllerName) {
        if (!isInitialized()) return 1.0f;
        Optional<GeoBone> bone = getBone(controllerName);
        if (bone.isEmpty()) return 1.0f;
        return 1.0f - bone.get().getPosX();
    }

    public float getControllerValue(String controllerName) {
        if (!isInitialized()) return 0.0f;
        Optional<GeoBone> bone = getBone(controllerName);
        if (bone.isEmpty()) return 0.0f;
        return bone.get().getPosX();
    }
}
