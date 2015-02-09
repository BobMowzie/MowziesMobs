package com.bobmowzie.mowziesmobs.client.model.animation.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * This is a buffer used to delay a chain of parented boxes by using the yaw and pitch
 * of the entity.
 *
 * @author RafaMv
 */
@SideOnly(Side.CLIENT)
public class ChainBuffer
{
    /**
     * Used to delay the tail animation when the entity rotates.
     */
    private int yawTimer;
    /**
     * Rotation amount (rotateY) of the tail buffer. Added when the entity rotates.
     */
    private float yawVariation;
    /**
     * Used to delay the tail animation when the entity rotates.
     */
    private int pitchTimer;
    /**
     * Rotation amount (rotateY) of the tail buffer. Added when the entity rotates.
     */
    private float pitchVariation;
    /**
     * Array that contains the right rotations to be applied in the Y axis.
     */
    private float[] yawArray;
    /**
     * Array that contains the right rotations to be applied in the X axis.
     */
    private float[] pitchArray;

    public ChainBuffer(int numberOfParentedBoxes)
    {
        this.yawTimer = 0;
        this.pitchTimer = 0;
        this.yawVariation = 0.0F;
        this.pitchVariation = 0.0F;
        this.yawArray = new float[numberOfParentedBoxes];
        this.pitchArray = new float[numberOfParentedBoxes];
    }

    /**
     * Sets both yaw and pitch variations to zero.
     */
    public void resetRotations()
    {
        this.yawVariation = 0.0F;
        this.pitchVariation = 0.0F;
    }

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleY).
     *
     * @param i              is the number of boxes to be animated;
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40.0F to 90.0F degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3.0F degrees;
     * @param divider        reduces the amount of angle added to the buffer.
     *                       Try values about 5.0F.
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
    @SideOnly(Side.CLIENT)
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divider, EntityLivingBase entity)
    {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(this.yawVariation) < maxAngle)
            this.yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset) / divider;

        if (this.yawVariation > 0.7F * angleDecrement)
        {
            if (this.yawTimer > bufferTime)
            {
                this.yawVariation -= angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement)
                {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            }
            else
            {
                this.yawTimer++;
            }
        }
        else if (this.yawVariation < -0.7F * angleDecrement)
        {
            if (this.yawTimer > bufferTime)
            {
                this.yawVariation += angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement)
                {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            }
            else
            {
                this.yawTimer++;
            }
        }

        for (int i = 0; i < this.yawArray.length; i++)
            this.yawArray[i] = 0.01745329251F * this.yawVariation / this.pitchArray.length;
    }

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleX).
     *
     * @param i              is the number of boxes to be animated;
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40.0F to 90.0F degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3.0F degrees;
     * @param divider        reduces the amount of angle added to the buffer.
     *                       Try values about 5.0F.
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
    @SideOnly(Side.CLIENT)
    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, float divider, EntityLivingBase entity)
    {
        if (entity.rotationPitch != entity.prevRotationPitch && MathHelper.abs(this.pitchVariation) < maxAngle)
            this.pitchVariation += (entity.prevRotationPitch - entity.rotationPitch) / divider;

        if (this.pitchVariation > 0.7F * angleDecrement)
        {
            if (this.pitchTimer > bufferTime)
            {
                this.pitchVariation -= angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement)
                {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            }
            else
            {
                this.pitchTimer++;
            }
        }
        else if (this.pitchVariation < -0.7F * angleDecrement)
        {
            if (this.pitchTimer > bufferTime)
            {
                this.pitchVariation += angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement)
                {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            }
            else
            {
                this.pitchTimer++;
            }
        }

        for (int i = 0; i < this.pitchArray.length; i++)
            this.pitchArray[i] = 0.01745329251F * this.pitchVariation / this.pitchArray.length;
    }

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleY).
     *
     * @param i              is the number of boxes to be animated;
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40.0F to 90.0F degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3.0F degrees;
     * @param divider        reduces the amount of angle added to the buffer.
     *                       Try values about 5.0F.
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
    @SideOnly(Side.CLIENT)
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity)
    {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(this.yawVariation) < maxAngle)
            this.yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset);

        if (this.yawVariation > 0.7F * angleDecrement)
        {
            if (this.yawTimer > bufferTime)
            {
                this.yawVariation -= angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement)
                {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            }
            else
            {
                this.yawTimer++;
            }
        }
        else if (this.yawVariation < -0.7F * angleDecrement)
        {
            if (this.yawTimer > bufferTime)
            {
                this.yawVariation += angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement)
                {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            }
            else
            {
                this.yawTimer++;
            }
        }

        for (int i = 0; i < this.yawArray.length; i++)
            this.yawArray[i] = 0.01745329251F * this.yawVariation / this.pitchArray.length;
    }

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleX).
     *
     * @param i              is the number of boxes to be animated;
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40.0F to 90.0F degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3.0F degrees;
     * @param divider        reduces the amount of angle added to the buffer.
     *                       Try values about 5.0F.
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
    @SideOnly(Side.CLIENT)
    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity)
    {
        if (entity.rotationPitch != entity.prevRotationPitch && MathHelper.abs(this.pitchVariation) < maxAngle)
            this.pitchVariation += (entity.prevRotationPitch - entity.rotationPitch);

        if (this.pitchVariation > 0.7F * angleDecrement)
        {
            if (this.pitchTimer > bufferTime)
            {
                this.pitchVariation -= angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement)
                {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            }
            else
            {
                this.pitchTimer++;
            }
        }
        else if (this.pitchVariation < -0.7F * angleDecrement)
        {
            if (this.pitchTimer > bufferTime)
            {
                this.pitchVariation += angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement)
                {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            }
            else
            {
                this.pitchTimer++;
            }
        }

        for (int i = 0; i < this.pitchArray.length; i++)
            this.pitchArray[i] = 0.01745329251F * this.pitchVariation / this.pitchArray.length;
    }

    /**
     * Adds a rotations in the Y axis depending on the entity rotation using a previous set array. (rotateAngleY).
     *
     * @param boxes are the chain of parented boxes to be animated;
     */
    @SideOnly(Side.CLIENT)
    public void applyChainSwingBuffer(MowzieModelRenderer[] boxes)
    {
        if (boxes.length == this.yawArray.length)
        {
            for (int i = 0; i < boxes.length; i++)
                boxes[i].rotateAngleY += this.yawArray[i];
        }
        else
        {
            System.out.println("Wrong array length being used in the buffer! Y axis.");
        }
    }

    /**
     * Adds a rotations in the X axis depending on the entity rotation using a previous set array. (rotateAngleX).
     *
     * @param boxes are the chain of parented boxes to be animated;
     */
    @SideOnly(Side.CLIENT)
    public void applyChainWaveBuffer(MowzieModelRenderer[] boxes)
    {
        if (boxes.length == this.pitchArray.length)
        {
            for (int i = 0; i < boxes.length; i++)
                boxes[i].rotateAngleX += this.pitchArray[i];
        }
        else
        {
            System.out.println("Wrong array length being used in the buffer! X axis.");
        }
    }
}
