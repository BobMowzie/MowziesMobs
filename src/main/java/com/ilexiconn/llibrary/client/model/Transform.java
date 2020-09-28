package com.ilexiconn.llibrary.client.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class Transform {
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float offsetX;
    private float offsetY;
    private float offsetZ;

    /**
     * @return the x rotation
     */
    public float getRotationX() {
        return this.rotationX;
    }

    /**
     * @return the y rotation
     */
    public float getRotationY() {
        return this.rotationY;
    }

    /**
     * @return the z rotation
     */
    public float getRotationZ() {
        return this.rotationZ;
    }

    /**
     * @return the x offset
     */
    public float getOffsetX() {
        return this.offsetX;
    }

    /**
     * @return the y offset
     */
    public float getOffsetY() {
        return this.offsetY;
    }

    /**
     * @return the z offset
     */
    public float getOffsetZ() {
        return this.offsetZ;
    }

    /**
     * Add rotation to this transformation
     *
     * @param x the x rotation
     * @param y the y rotation
     * @param z the z rotation
     */
    public void addRotation(float x, float y, float z) {
        this.rotationX += x;
        this.rotationY += y;
        this.rotationZ += z;
    }

    /**
     * Add offset to this transformation
     *
     * @param x the x offset
     * @param y the y offset
     * @param z the z offset
     */
    public void addOffset(float x, float y, float z) {
        this.offsetX += x;
        this.offsetY += y;
        this.offsetZ += z;
    }

    /**
     * Reset the rotation of this transformation
     */
    public void resetRotation() {
        this.rotationX = 0.0F;
        this.rotationY = 0.0F;
        this.rotationZ = 0.0F;
    }

    /**
     * Reset the offset of this transformation
     */
    public void resetOffset() {
        this.offsetX = 0.0F;
        this.offsetY = 0.0F;
        this.offsetZ = 0.0F;
    }

    /**
     * Set the rotation of this transformation
     *
     * @param x the x rotation
     * @param y the y rotation
     * @param z the z rotation
     */
    public void setRotation(float x, float y, float z) {
        this.resetRotation();
        this.addRotation(x, y, z);
    }

    /**
     * Set the offset of this transformation
     *
     * @param x the x offset
     * @param y the y offset
     * @param z the z offset
     */
    public void setOffset(float x, float y, float z) {
        this.resetOffset();
        this.addOffset(x, y, z);
    }
}
