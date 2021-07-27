package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class MowzieGeoBone extends GeoBone {
    private Matrix4f modelSpaceXform;
    private boolean trackXform;

    public MowzieGeoBone() {
        super();
        modelSpaceXform = new Matrix4f();
        modelSpaceXform.setIdentity();
        trackXform = false;
    }

    public MowzieGeoBone getParent() {
        return (MowzieGeoBone) parent;
    }

    public boolean isTrackingXform() {
        return trackXform;
    }

    public void setTrackXform(boolean trackXform) {
        this.trackXform = trackXform;
    }

    public Matrix4f getModelSpaceXform() {
        setTrackXform(true);
        return modelSpaceXform;
    }

    public Vector3d getModelPosition() {
        Matrix4f matrix = getModelSpaceXform();
        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix);
        return new Vector3d(-vec.getX() * 16f, vec.getY() * 16f, vec.getZ() * 16f);
    }

    public void setModelPosition(Vector3d pos) {
        // TODO: Doesn't work on bones with parent transforms
        MowzieGeoBone parent = getParent();
        Matrix4f identity = new Matrix4f();
        identity.setIdentity();
        Matrix4f matrix = parent == null ? identity : parent.getModelSpaceXform().copy();
        matrix.invert();
        Vector4f vec = new Vector4f(-(float) pos.getX() / 16f, (float) pos.getY() / 16f, (float) pos.getZ() / 16f, 1);
        vec.transform(matrix);
        setPosition(-vec.getX() * 16f, vec.getY() * 16f, vec.getZ() * 16f);
    }

    // Position utils
    public void addPosition(Vector3d vec) {
        addPosition((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addPosition(float x, float y, float z) {
        setPositionX(getPositionX() + x);
        setPositionY(getPositionY() + y);
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
        setRotationX(getRotationX() + x);
        setRotationY(getRotationY() + y);
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
}