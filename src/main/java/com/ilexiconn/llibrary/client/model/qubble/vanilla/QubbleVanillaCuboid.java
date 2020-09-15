package com.ilexiconn.llibrary.client.model.qubble.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author gegy1000
 * @since 1.7.5
 */
public class QubbleVanillaCuboid implements INBTSerializable<NBTTagCompound> {
    private String name;
    private float fromX;
    private float fromY;
    private float fromZ;
    private float toX;
    private float toY;
    private float toZ;
    private QubbleVanillaRotation rotation;
    private QubbleVanillaFace[] faces = new QubbleVanillaFace[EnumFacing.values().length];
    private boolean shade = true;

    private QubbleVanillaCuboid(String name, String texture) {
        this.name = name;
        for (int i = 0; i < this.faces.length; i++) {
            this.faces[i] = QubbleVanillaFace.create(EnumFacing.values()[i], texture, 0.0F, 0.0F, 1.0F, 1.0F);
        }
    }

    private QubbleVanillaCuboid() {
    }

    public static QubbleVanillaCuboid create(String name, String texture, float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
        QubbleVanillaCuboid cuboid = new QubbleVanillaCuboid(name, texture);
        cuboid.setFrom(fromX, fromY, fromZ);
        cuboid.setTo(toX, toY, toZ);
        return cuboid;
    }

    public static QubbleVanillaCuboid deserialize(NBTTagCompound compound) {
        QubbleVanillaCuboid cuboid = new QubbleVanillaCuboid();
        cuboid.deserializeNBT(compound);
        return cuboid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFace(QubbleVanillaFace face) {
        if (face == null) {
            throw new IllegalArgumentException("Face cannot be null!");
        }
        this.faces[face.getFacing().ordinal()] = face;
    }

    public void setFromX(float fromX) {
        this.fromX = fromX;
    }

    public void setFromY(float fromY) {
        this.fromY = fromY;
    }

    public void setFromZ(float fromZ) {
        this.fromZ = fromZ;
    }

    public void setFrom(float fromX, float fromY, float fromZ) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
    }

    public void setToX(float toX) {
        this.toX = toX;
    }

    public void setToY(float toY) {
        this.toY = toY;
    }

    public void setToZ(float toZ) {
        this.toZ = toZ;
    }

    public void setTo(float toX, float toY, float toZ) {
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
    }

    public void setRotation(QubbleVanillaRotation rotation) {
        this.rotation = rotation;
    }

    public void setDimension(float dimensionX, float dimensionY, float dimensionZ) {
        this.toX = this.fromX + dimensionX;
        this.toY = this.fromY + dimensionY;
        this.toZ = this.fromZ + dimensionZ;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public String getName() {
        return this.name;
    }

    public float getFromX() {
        return this.fromX;
    }

    public float getFromY() {
        return this.fromY;
    }

    public float getFromZ() {
        return this.fromZ;
    }

    public float getToX() {
        return this.toX;
    }

    public float getToY() {
        return this.toY;
    }

    public float getToZ() {
        return this.toZ;
    }

    public QubbleVanillaRotation getRotation() {
        return this.rotation;
    }

    public QubbleVanillaFace getFace(EnumFacing facing) {
        return this.faces[facing.ordinal()];
    }

    public boolean hasShade() {
        return this.shade;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("name", this.name);
        compound.setFloat("fromX", this.fromX);
        compound.setFloat("fromY", this.fromY);
        compound.setFloat("fromZ", this.fromZ);
        compound.setFloat("toX", this.toX);
        compound.setFloat("toY", this.toY);
        compound.setFloat("toZ", this.toZ);
        compound.setBoolean("shade", this.shade);
        if (this.rotation != null) {
            compound.setTag("rotation", this.rotation.serializeNBT());
        }
        NBTTagList faces = new NBTTagList();
        for (QubbleVanillaFace face : this.faces) {
            faces.appendTag(face.serializeNBT());
        }
        compound.setTag("faces", faces);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.name = compound.getString("name");
        this.setFrom(compound.getFloat("fromX"), compound.getFloat("fromY"), compound.getFloat("fromZ"));
        this.setTo(compound.getFloat("toX"), compound.getFloat("toY"), compound.getFloat("toZ"));
        if (compound.hasKey("rotation")) {
            this.rotation = QubbleVanillaRotation.deserialize(compound.getCompoundTag("rotation"));
        }
        if (compound.hasKey("shade")) {
            this.shade = compound.getBoolean("shade");
        }
        NBTTagList faces = compound.getTagList("faces", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < faces.tagCount(); i++) {
            this.setFace(QubbleVanillaFace.deserialize(faces.getCompoundTagAt(i)));
        }
    }

    public QubbleVanillaCuboid copy() {
        QubbleVanillaCuboid cuboid = QubbleVanillaCuboid.create(this.name, "none", this.fromX, this.fromY, this.fromZ, this.toX, this.toY, this.toZ);
        if (this.rotation != null) {
            cuboid.setRotation(this.rotation.copy());
        }
        for (QubbleVanillaFace face : this.faces) {
            cuboid.setFace(face.copy());
        }
        cuboid.setShade(this.shade);
        return cuboid;
    }
}
