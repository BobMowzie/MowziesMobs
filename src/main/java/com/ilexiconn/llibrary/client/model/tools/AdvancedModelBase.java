package com.ilexiconn.llibrary.client.model.tools;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.resources.model.ModelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.MathUtil;

import java.util.List;
import java.util.function.Function;

/**
 * An enhanced ModelBase
 *
 * @author gegy1000
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public abstract class AdvancedModelBase<T extends Entity> extends EntityModel<T> {
    public final List<ModelRenderer> boxList = Lists.newArrayList();

    private float movementScale = 1.0F;

    protected AdvancedModelBase() {
        super();
    }

    protected AdvancedModelBase(Function<ResourceLocation, RenderType> renderTypeFunction) {
        super(renderTypeFunction);
    }

    /**
     * Sets the default pose to the current pose of this model
     */
    public void updateDefaultPose() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer advancedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            advancedModelRenderer.updateDefaultPose();
        });
    }

    /**
     * Sets the current pose to the previously set default pose
     */
    public void resetToDefaultPose() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer advancedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            advancedModelRenderer.resetToDefaultPose();
        });
    }

    @Override
    public void accept(ModelRenderer modelRenderer) {
        boxList.add(modelRenderer);
    }

    /**
     * Rotates the given boxes to face a given target
     *
     * @param yaw             the yaw to face
     * @param pitch           the pitch to face
     * @param rotationDivisor the amount to divide the rotation angles by
     * @param boxes           the boxes to face the given target
     */
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelRenderer... boxes) {
        float actualRotationDivisor = rotationDivisor * boxes.length;
        float yawAmount = (float) (Math.toRadians(yaw) / actualRotationDivisor);
        float pitchAmount = (float) (Math.toRadians(pitch) / actualRotationDivisor);
        for (AdvancedModelRenderer box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }

    /**
     * Swings (rotates on the Y axis) the given model parts in a chain-like manner.
     *
     * @param boxes       the boxes to swing
     * @param speed       the speed to swing this at
     * @param degree      the amount to rotate this by
     * @param rootOffset  the root rotation offset
     * @param swing       the swing rotation
     * @param swingAmount the swing amount
     */
    public void chainSwing(AdvancedModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleY += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * Waves (rotates on the X axis) the given model parts in a chain-like manner.
     *
     * @param boxes       the boxes to wave
     * @param speed       the speed to wave this at
     * @param degree      the amount to rotate this by
     * @param rootOffset  the root rotation offset
     * @param swing       the swing rotation
     * @param swingAmount the swing amount
     */
    public void chainWave(AdvancedModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleX += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * Flaps (rotates on the Z axis) the given model parts in a chain-like manner.
     *
     * @param boxes       the boxes to flap
     * @param speed       the speed to flap this at
     * @param degree      the amount to rotate this by
     * @param rootOffset  the root rotation offset
     * @param swing       the swing rotation
     * @param swingAmount the swing amount
     */
    public void chainFlap(AdvancedModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleZ += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
        return MathHelper.cos(swing * (speed * this.movementScale) + offset * boxIndex) * swingAmount * (degree * this.movementScale);
    }

    private float calculateChainOffset(double rootOffset, AdvancedModelRenderer... boxes) {
        return (float) ((rootOffset * Math.PI) / (2 * boxes.length));
    }

    /**
     * @return the current movement scale
     */
    public float getMovementScale() {
        return this.movementScale;
    }

    /**
     * Multiplies all rotation and position changes by this value
     *
     * @param movementScale the movement scale
     */
    public void setMovementScale(float movementScale) {
        this.movementScale = movementScale;
    }

    /**
     * Rotates this box back and forth (rotateAngleX). Useful for arms and legs.
     *
     * @param box        the box to animate
     * @param speed      is how fast the animation runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the animation
     * @param weight     will make the animation favor one direction more based on how fast the mob is moving
     * @param walk       is the walked distance
     * @param walkAmount is the walk speed
     */
    public void walk(AdvancedModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        box.walk(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * Rotates this box up and down (rotateAngleZ). Useful for wing and ears.
     *
     * @param box        the box to animate
     * @param speed      is how fast the animation runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the animation
     * @param weight     will make the animation favor one direction more based on how fast the mob is moving
     * @param flap       is the flapped distance
     * @param flapAmount is the flap speed
     */
    public void flap(AdvancedModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        box.flap(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /**
     * Rotates this box side to side (rotateAngleY).
     *
     * @param box         the box to animate
     * @param speed       is how fast the animation runs
     * @param degree      is how far the box will rotate;
     * @param invert      will invert the rotation
     * @param offset      will offset the timing of the animation
     * @param weight      will make the animation favor one direction more based on how fast the mob is moving
     * @param swing       is the swung distance
     * @param swingAmount is the swing speed
     */
    public void swing(AdvancedModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        box.swing(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    /**
     * Moves this box up and down (rotationPointY). Useful for bodies.
     *
     * @param box    the box to animate
     * @param speed  is how fast the animation runs;
     * @param degree is how far the box will move;
     * @param bounce will make the box bounce;
     * @param f      is the walked distance;
     * @param f1     is the walk speed.
     */
    public void bob(AdvancedModelRenderer box, float speed, float degree, boolean bounce, float f, float f1) {
        box.bob(speed, degree, bounce, f, f1);
    }

    /**
     * Returns a float that can be used to move boxes.
     *
     * @param speed  is how fast the animation runs;
     * @param degree is how far the box will move;
     * @param bounce will make the box bounce;
     * @param f      is the walked distance;
     * @param f1     is the walk speed.
     */
    public float moveBox(float speed, float degree, boolean bounce, float f, float f1) {
        if (bounce) {
            return -MathHelper.abs((MathHelper.sin(f * speed) * f1 * degree));
        } else {
            return MathHelper.sin(f * speed) * f1 * degree - f1 * degree;
        }
    }
}
