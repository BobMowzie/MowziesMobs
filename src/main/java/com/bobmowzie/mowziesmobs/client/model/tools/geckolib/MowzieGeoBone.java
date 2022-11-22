package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.util.math.vector.*;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class MowzieGeoBone extends GeoBone {

    public MowzieGeoBone() {
        super();
    }

    public MowzieGeoBone getParent() {
        return (MowzieGeoBone) parent;
    }


    // Position utils
    public void addPosition(Vector3d vec) {
        addPosition((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addPosition(float x, float y, float z) {
        addPositionX(x);
        addPositionY(y);
        addPositionZ(z);
    }

    public void addPositionX(float x) {
        setPositionX(getPositionX() + x);
    }

    public void addPositionY(float y) {
        setPositionY(getPositionY() + y);
    }

    public void addPositionZ(float z) {
        setPositionZ(getPositionZ() + z);
    }

    public void setPosition(Vector3d vec) {
        setPosition((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setPosition(float x, float y, float z) {
        setPositionX(x);
        setPositionY(y);
        setPositionZ(z);
    }

    public Vector3d getPosition() {
        return new Vector3d(getPositionX(), getPositionY(), getPositionZ());
    }

    // Rotation utils
    public void addRotation(Vector3d vec) {
        addRotation((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addRotation(float x, float y, float z) {
        addRotationX(x);
        addRotationY(y);
        addRotationZ(z);
    }

    public void addRotationX(float x) {
        setRotationX(getRotationX() + x);
    }

    public void addRotationY(float y) {
        setRotationY(getRotationY() + y);
    }

    public void addRotationZ(float z) {
        setRotationZ(getRotationZ() + z);
    }

    public void setRotation(Vector3d vec) {
        setRotation((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setRotation(float x, float y, float z) {
        setRotationX(x);
        setRotationY(y);
        setRotationZ(z);
    }

    public Vector3d getRotation() {
        return new Vector3d(getRotationX(), getRotationY(), getRotationZ());
    }

    // Scale utils
    public void multiplyScale(Vector3d vec) {
        multiplyScale((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void multiplyScale(float x, float y, float z) {
        setScaleX(getScaleX() * x);
        setScaleY(getScaleY() * y);
        setScaleZ(getScaleZ() * z);
    }

    public void setScale(Vector3d vec) {
        setScale((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setScale(float x, float y, float z) {
        setScaleX(x);
        setScaleY(y);
        setScaleZ(z);
    }

    public Vector3d getScale() {
        return new Vector3d(getScaleX(), getScaleY(), getScaleZ());
    }

    public void addRotationOffsetFromBone(MowzieGeoBone source) {
        setRotationX(getRotationX() + source.getRotationX() - source.getInitialSnapshot().rotationValueX);
        setRotationY(getRotationY() + source.getRotationY() - source.getInitialSnapshot().rotationValueY);
        setRotationZ(getRotationZ() + source.getRotationZ() - source.getInitialSnapshot().rotationValueZ);
    }
}