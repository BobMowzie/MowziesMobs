package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;

import javax.annotation.Nullable;

public class MowzieGeoBone extends GeoBone {

    public Matrix4f rotMat;
    protected boolean forceMatrixTransform = false;

    public MowzieGeoBone(@Nullable GeoBone parent, String name, Boolean mirror, @Nullable Double inflate, @Nullable Boolean dontRender, @Nullable Boolean reset) {
        super(parent, name, mirror, inflate, dontRender, reset);
        rotMat = null;
    }

    public MowzieGeoBone getParent() {
        return (MowzieGeoBone) super.getParent();
    }

    // Position utils
    public void addPos(Vec3 vec) {
        addPos((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void addPos(float x, float y, float z) {
        addPosX(x);
        addPosY(y);
        addPosZ(z);
    }

    public void addPosX(float x) {
        setPosX(getPosX() + x);
    }

    public void addPosY(float y) {
        setPosY(getPosY() + y);
    }

    public void addPosZ(float z) {
        setPosZ(getPosZ() + z);
    }

    public void setPos(Vec3 vec) {
        setPos((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void setPos(float x, float y, float z) {
        setPosX(x);
        setPosY(y);
        setPosZ(z);
    }

    public Vector3d getPos() {
        return new Vector3d(getPosX(), getPosY(), getPosZ());
    }

    // Rotation utils
    public void addRot(Vec3 vec) {
        addRot((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void addRot(float x, float y, float z) {
        addRotX(x);
        addRotY(y);
        addRotZ(z);
    }

    public void addRotX(float x) {
        setRotX(getRotX() + x);
    }

    public void addRotY(float y) {
        setRotY(getRotY() + y);
    }

    public void addRotZ(float z) {
        setRotZ(getRotZ() + z);
    }

    public void setRot(Vector3d vec) {
        setRot((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void setRot(Vec3 vec) {
        setRot((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void setRot(float x, float y, float z) {
        setRotX(x);
        setRotY(y);
        setRotZ(z);
    }

    public Vector3d getRot() {
        return new Vector3d(getRotX(), getRotY(), getRotZ());
    }

    // Scale utils
    public void multiplyScale(Vec3 vec) {
        multiplyScale((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void multiplyScale(float x, float y, float z) {
        setScaleX(getScaleX() * x);
        setScaleY(getScaleY() * y);
        setScaleZ(getScaleZ() * z);
    }

    public void setScale(Vec3 vec) {
        setScale((float) vec.x(), (float) vec.y(), (float) vec.z());
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
        setRotX(getRotX() + source.getRotX() - source.getInitialSnapshot().getRotX());
        setRotY(getRotY() + source.getRotY() - source.getInitialSnapshot().getRotY());
        setRotZ(getRotZ() + source.getRotZ() - source.getInitialSnapshot().getRotZ());
    }

    public void setForceMatrixTransform(boolean forceMatrixTransform) {
        this.forceMatrixTransform = forceMatrixTransform;
    }

    public boolean isForceMatrixTransform() {
        return forceMatrixTransform;
    }


    public Matrix4f getModelRotationMat() {
        Matrix4f matrix = new Matrix4f(getModelSpaceMatrix());
        removeMatrixTranslation(matrix);
        return matrix;
    }

    public static void removeMatrixTranslation(Matrix4f matrix) {
        matrix.m03(0);
        matrix.m13(0);
        matrix.m23(0);
    }

    public void setModelRotationMat(Matrix4f mat) {
        rotMat = mat;
    }
}