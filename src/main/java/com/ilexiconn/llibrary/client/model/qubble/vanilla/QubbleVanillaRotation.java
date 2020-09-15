package com.ilexiconn.llibrary.client.model.qubble.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author gegy1000
 * @since 1.7.5
 */
public class QubbleVanillaRotation implements INBTSerializable<NBTTagCompound> {
    private EnumFacing.Axis axis;
    private float originX = 8.0F;
    private float originY = 8.0F;
    private float originZ = 8.0F;
    private float angle;

    private QubbleVanillaRotation() {
    }

    public static QubbleVanillaRotation create(EnumFacing.Axis axis, float originX, float originY, float originZ, float angle) {
        QubbleVanillaRotation rotation = new QubbleVanillaRotation();
        rotation.setAxis(axis);
        rotation.setOrigin(originX, originY, originZ);
        rotation.setAngle(angle);
        return rotation;
    }

    public static QubbleVanillaRotation deserialize(NBTTagCompound compound) {
        QubbleVanillaRotation rotation = new QubbleVanillaRotation();
        rotation.deserializeNBT(compound);
        return rotation;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("axis", (byte) this.axis.ordinal());
        compound.setFloat("originX", this.originX);
        compound.setFloat("originY", this.originY);
        compound.setFloat("originZ", this.originZ);
        compound.setFloat("angle", this.angle);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.setAxis(EnumFacing.Axis.values()[compound.getByte("axis")]);
        this.setOrigin(compound.getFloat("originX"), compound.getFloat("originY"), compound.getFloat("originZ"));
        this.setAngle(compound.getFloat("angle"));
    }

    public void setAxis(EnumFacing.Axis axis) {
        this.axis = axis;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public void setOriginZ(float originZ) {
        this.originZ = originZ;
    }

    public void setOrigin(float originX, float originY, float originZ) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
    }

    public void setAngle(float angle) {
        this.angle = this.limitAngle(angle);
    }

    protected float limitAngle(float angle) {
        return MathHelper.floor(angle / 22.5) * 22.5F;
    }

    public Axis getAxis() {
        return this.axis;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getOriginX() {
        return this.originX;
    }

    public float getOriginY() {
        return this.originY;
    }

    public float getOriginZ() {
        return this.originZ;
    }

    public QubbleVanillaRotation copy() {
        return QubbleVanillaRotation.create(this.axis, this.originX, this.originY, this.originZ, this.angle);
    }
}
