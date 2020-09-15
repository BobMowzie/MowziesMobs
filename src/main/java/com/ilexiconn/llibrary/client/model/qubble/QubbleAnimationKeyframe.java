package com.ilexiconn.llibrary.client.model.qubble;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class QubbleAnimationKeyframe implements INBTSerializable<NBTTagCompound> {
    private int duration;
    private Type type;
    private List<QubbleAnimationAction> actions = new ArrayList<>();

    private QubbleAnimationKeyframe() {

    }

    public static QubbleAnimationKeyframe create(int duration, Type type) {
        QubbleAnimationKeyframe keyframe = new QubbleAnimationKeyframe();
        keyframe.setDuration(duration);
        keyframe.setType(type);
        return keyframe;
    }

    public static QubbleAnimationKeyframe deserialize(NBTTagCompound compound) {
        QubbleAnimationKeyframe keyframe = new QubbleAnimationKeyframe();
        keyframe.deserializeNBT(compound);
        return keyframe;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("duration", this.duration);
        compound.setString("type", this.type.name());
        NBTTagList actionsTag = new NBTTagList();
        for (QubbleAnimationAction action : this.actions) {
            actionsTag.appendTag(action.serializeNBT());
        }
        compound.setTag("actions", actionsTag);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.duration = compound.getInteger("duration");
        this.type = Type.valueOf(compound.getString("type"));
        NBTTagList actionsTag = compound.getTagList("actions", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < actionsTag.tagCount(); i++) {
            this.actions.add(QubbleAnimationAction.deserialize(actionsTag.getCompoundTagAt(i)));
        }
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<QubbleAnimationAction> getActions() {
        return this.actions;
    }

    public QubbleAnimationKeyframe copy() {
        QubbleAnimationKeyframe keyframe = QubbleAnimationKeyframe.create(this.getDuration(), this.getType());
        keyframe.getActions().addAll(this.getActions().stream().map(QubbleAnimationAction::copy).collect(Collectors.toList()));
        return keyframe;
    }

    public enum Type {
        DYNAMIC,
        STATIC,
        RESET
    }
}
