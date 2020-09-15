package com.ilexiconn.llibrary.client.model.qubble;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class QubbleAnimationAction implements INBTSerializable<NBTTagCompound> {
    private String cuboid;
    private Action action;
    private float valueX;
    private float valueY;
    private float valueZ;

    private QubbleAnimationAction() {

    }

    public static QubbleAnimationAction create(String cuboid, Action action) {
        QubbleAnimationAction animation = new QubbleAnimationAction();
        animation.setCuboid(cuboid);
        animation.setAction(action);
        return animation;
    }

    public static QubbleAnimationAction deserialize(NBTTagCompound compound) {
        QubbleAnimationAction animation = new QubbleAnimationAction();
        animation.deserializeNBT(compound);
        return animation;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("cuboid", this.cuboid);
        compound.setString("action", this.action.name());
        NBTTagCompound valueTag = new NBTTagCompound();
        valueTag.setFloat("x", this.valueX);
        valueTag.setFloat("y", this.valueY);
        valueTag.setFloat("z", this.valueZ);
        compound.setTag("value", valueTag);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.cuboid = compound.getString("cuboid");
        this.action = Action.valueOf(compound.getString("action"));
        NBTTagCompound valueTag = compound.getCompoundTag("value");
        this.valueX = valueTag.getFloat("x");
        this.valueY = valueTag.getFloat("y");
        this.valueZ = valueTag.getFloat("z");
    }

    public String getCuboid() {
        return this.cuboid;
    }

    public void setCuboid(String cuboid) {
        this.cuboid = cuboid;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public float getValueX() {
        return this.valueX;
    }

    public float getValueY() {
        return this.valueY;
    }

    public float getValueZ() {
        return this.valueZ;
    }

    public void setValue(float x, float y, float z) {
        this.valueX = x;
        this.valueY = y;
        this.valueZ = z;
    }

    public QubbleAnimationAction copy() {
        QubbleAnimationAction animation = QubbleAnimationAction.create(this.getCuboid(), this.getAction());
        animation.setValue(this.getValueX(), this.getValueY(), this.getValueZ());
        return animation;
    }

    public enum Action {
        ROTATE,
        MOVE
    }
}
