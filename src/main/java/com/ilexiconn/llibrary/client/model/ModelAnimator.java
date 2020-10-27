package com.ilexiconn.llibrary.client.model;

import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelAnimator {
    private int tempTick;
    private int prevTempTick;
    private boolean correctAnimation;
    private IAnimatedEntity entity;
    private HashMap<RendererModel, Transform> transformMap;
    private HashMap<RendererModel, Transform> prevTransformMap;

    private ModelAnimator() {
        this.tempTick = 0;
        this.correctAnimation = false;
        this.transformMap = new HashMap<>();
        this.prevTransformMap = new HashMap<>();
    }

    /**
     * @return a new ModelAnimator instance
     */
    public static ModelAnimator create() {
        return new ModelAnimator();
    }

    /**
     * @return the {@link IAnimatedEntity} instance. Null if {@link ModelAnimator#update} has never been called.
     */
    public IAnimatedEntity getEntity() {
        return this.entity;
    }

    /**
     * Update the animations of this model.
     *
     * @param entity the entity instance
     */
    public void update(IAnimatedEntity entity) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
    }

    /**
     * Start an animation
     *
     * @param animation the animation instance
     * @return true if it's the current animation
     */
    public boolean setAnimation(Animation animation) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = this.entity.getAnimation() == animation;
        return this.correctAnimation;
    }

    /**
     * Start a keyframe for the current animation.
     *
     * @param duration the keyframe duration
     */
    public void startKeyframe(int duration) {
        if (!this.correctAnimation) {
            return;
        }
        this.prevTempTick = this.tempTick;
        this.tempTick += duration;
    }

    /**
     * Add a static keyframe with a specific duration to the animation.
     *
     * @param duration the keyframe duration
     */
    public void setStaticKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe(true);
    }

    /**
     * Reset this keyframe to its original state
     *
     * @param duration the keyframe duration
     */
    public void resetKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe();
    }

    /**
     * Rotate a box in the current keyframe. All the values are relative.
     *
     * @param box the box to rotate
     * @param x   the x rotation
     * @param y   the y rotation
     * @param z   the z rotation
     */
    public void rotate(RendererModel box, float x, float y, float z) {
        if (!this.correctAnimation) {
            return;
        }
        this.getTransform(box).addRotation(x, y, z);
    }

    /**
     * Move a box in the current keyframe. All the values are relative.
     *
     * @param box the box to move
     * @param x   the x offset
     * @param y   the y offset
     * @param z   the z offset
     */
    public void move(RendererModel box, float x, float y, float z) {
        if (!this.correctAnimation) {
            return;
        }
        this.getTransform(box).addOffset(x, y, z);
    }

    private Transform getTransform(RendererModel box) {
        return this.transformMap.computeIfAbsent(box, b -> new Transform());
    }

    /**
     * End the current keyframe. this will reset all box transformations to their original state.
     */
    public void endKeyframe() {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary) {
        if (!this.correctAnimation) {
            return;
        }
        int animationTick = this.entity.getAnimationTick();

        if (animationTick >= this.prevTempTick && animationTick < this.tempTick) {
            if (stationary) {
                for (RendererModel box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.rotateAngleX += transform.getRotationX();
                    box.rotateAngleY += transform.getRotationY();
                    box.rotateAngleZ += transform.getRotationZ();
                    box.rotationPointX += transform.getOffsetX();
                    box.rotationPointY += transform.getOffsetY();
                    box.rotationPointZ += transform.getOffsetZ();
                }
            } else {
                float tick = (animationTick - this.prevTempTick + LLibrary.PROXY.getPartialTicks()) / (this.tempTick - this.prevTempTick);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2.0F)), dec = 1.0F - inc;
                for (RendererModel box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.rotateAngleX += dec * transform.getRotationX();
                    box.rotateAngleY += dec * transform.getRotationY();
                    box.rotateAngleZ += dec * transform.getRotationZ();
                    box.rotationPointX += dec * transform.getOffsetX();
                    box.rotationPointY += dec * transform.getOffsetY();
                    box.rotationPointZ += dec * transform.getOffsetZ();
                }
                for (RendererModel box : this.transformMap.keySet()) {
                    Transform transform = this.transformMap.get(box);
                    box.rotateAngleX += inc * transform.getRotationX();
                    box.rotateAngleY += inc * transform.getRotationY();
                    box.rotateAngleZ += inc * transform.getRotationZ();
                    box.rotationPointX += inc * transform.getOffsetX();
                    box.rotationPointY += inc * transform.getOffsetY();
                    box.rotationPointZ += inc * transform.getOffsetZ();
                }
            }
        }

        if (!stationary) {
            this.prevTransformMap.clear();
            this.prevTransformMap.putAll(this.transformMap);
            this.transformMap.clear();
        }
    }
}
