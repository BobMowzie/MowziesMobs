package com.ilexiconn.llibrary.client.model.qubble.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author gegy1000
 * @since 1.7.5
 */
public class QubbleVanillaFace implements INBTSerializable<NBTTagCompound> {
    private Direction facing;
    private Direction cullface;
    private String texture;
    private float minU;
    private float minV;
    private float maxU;
    private float maxV;
    private boolean enabled = true;

    private QubbleVanillaFace(Direction facing) {
        this.facing = facing;
    }

    private QubbleVanillaFace() {
    }

    public static QubbleVanillaFace create(Direction facing, String texture, float minU, float minV, float maxU, float maxV) {
        QubbleVanillaFace face = new QubbleVanillaFace(facing);
        face.setTexture(texture);
        face.setUV(minU, minV, maxU, maxV);
        return face;
    }

    public static QubbleVanillaFace deserialize(NBTTagCompound compound) {
        QubbleVanillaFace face = new QubbleVanillaFace();
        face.deserializeNBT(compound);
        return face;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public void setCullface(Direction cullface) {
        this.cullface = cullface;
    }

    public void setMinU(float minU) {
        this.minU = minU;
    }

    public void setMinV(float minV) {
        this.minV = minV;
    }

    public void setMaxU(float maxU) {
        this.maxU = maxU;
    }

    public void setMaxV(float maxV) {
        this.maxV = maxV;
    }

    public void setUV(float minU, float minV, float maxU, float maxV) {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public String getTexture() {
        return this.texture;
    }

    public float getMinU() {
        return this.minU;
    }

    public float getMinV() {
        return this.minV;
    }

    public float getMaxU() {
        return this.maxU;
    }

    public float getMaxV() {
        return this.maxV;
    }

    public Direction getCullface() {
        return this.cullface;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("facing", (byte) this.facing.ordinal());
        if (this.cullface != null) {
            compound.setByte("cullface", (byte) this.cullface.ordinal());
        }
        if (this.texture != null) {
            compound.setString("texture", this.texture);
        }
        compound.setFloat("minU", this.minU);
        compound.setFloat("minV", this.minV);
        compound.setFloat("maxU", this.maxU);
        compound.setFloat("maxV", this.maxV);
        compound.setBoolean("enabled", this.enabled);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.facing = Direction.values()[compound.getByte("facing")];
        if (compound.hasKey("cullface")) {
            this.setCullface(Direction.values()[compound.getByte("cullface")]);
        }
        if (compound.hasKey("texture")) {
            this.setTexture(compound.getString("texture"));
        }
        this.setUV(compound.getFloat("minU"), compound.getFloat("minV"), compound.getFloat("maxU"), compound.getFloat("maxV"));
        if (compound.hasKey("enabled")) {
            this.setEnabled(compound.getBoolean("enabled"));
        } else {
            this.setEnabled(true);
        }
    }

    public QubbleVanillaFace copy() {
        QubbleVanillaFace face = QubbleVanillaFace.create(this.facing, this.texture, this.minU, this.minV, this.maxU, this.maxV);
        face.setEnabled(this.isEnabled());
        return face;
    }
}
