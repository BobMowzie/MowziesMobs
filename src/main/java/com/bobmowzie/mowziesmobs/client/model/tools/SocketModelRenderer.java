package com.bobmowzie.mowziesmobs.client.model.tools;


import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;


/**
 * Created by Josh on 5/8/2017.
 */
public class SocketModelRenderer extends AdvancedModelRenderer {
    public SocketModelRenderer(AdvancedModelBase model) {
        super(model);
    }

    public SocketModelRenderer(AdvancedModelBase model, int textureOffsetX, int textureOffsetY) {
        this(model);
        this.setTextureOffset(textureOffsetX, textureOffsetY);
    }

    public SocketModelRenderer(SocketModelRenderer modelRenderer) {
        super(modelRenderer.getModel());
        this.rotationPointX = modelRenderer.rotationPointX;
        this.rotationPointY = modelRenderer.rotationPointY;
        this.rotationPointZ = modelRenderer.rotationPointZ;
        this.rotateAngleX = modelRenderer.rotateAngleX;
        this.rotateAngleY = modelRenderer.rotateAngleY;
        this.rotateAngleZ = modelRenderer.rotateAngleZ;
        this.scaleX = modelRenderer.scaleX;
        this.scaleY = modelRenderer.scaleY;
        this.scaleZ = modelRenderer.scaleZ;
        this.defaultPositionX = modelRenderer.defaultPositionX;
        this.defaultPositionY = modelRenderer.defaultPositionY;
        this.defaultPositionZ = modelRenderer.defaultPositionZ;
        this.defaultRotationX = modelRenderer.defaultRotationX;
        this.defaultRotationY = modelRenderer.defaultRotationY;
        this.defaultRotationZ = modelRenderer.defaultRotationZ;

        this.scaleChildren = modelRenderer.scaleChildren;
    }

    public Vec3d getWorldPos(Entity entity, float delta) {
//        Vec3d modelPos = getModelPos(this, new Vec3d(rotationPointX/16, -rotationPointY/16, -rotationPointZ/16));
//        double x = modelPos.x;
//        double y = modelPos.y + 1.5f;
//        double z = modelPos.z;
//        Matrix4d entityTranslate = new Matrix4d();
//        Matrix4d entityRotate = new Matrix4d();
//        float dx = (float) (entity.prevPosX + (entity.posX - entity.prevPosX) * delta);
//        float dy = (float) (entity.prevPosY + (entity.posY - entity.prevPosY) * delta);
//        float dz = (float) (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * delta);
//        entityTranslate.set(new Vector3d(dx, dy, dz));
//        entityRotate.rotY(-Math.toRadians(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta));
//        Point3d rendererPos = new Point3d(x, y, z);
//        entityRotate.transform(rendererPos);
//        entityTranslate.transform(rendererPos);
//        return new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ());
        return new Vec3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());
    }

    public Vec3d getWorldPos(Entity entity) {
        return getWorldPos(entity, 0);
    }

    public Vec3d getModelPos(AdvancedModelRenderer modelRenderer, Vec3d recurseValue) {
//        double x = recurseValue.x;
//        double y = recurseValue.y;
//        double z = recurseValue.z;
//        Point3d rendererPos = new Point3d(x, y, z);
//
//        AdvancedModelRenderer parent = modelRenderer.getParent();
//        if (parent != null) {
//            Matrix4d boxTranslate = new Matrix4d();
//            Matrix4d boxRotateX = new Matrix4d();
//            Matrix4d boxRotateY = new Matrix4d();
//            Matrix4d boxRotateZ = new Matrix4d();
//            boxTranslate.set(new Vector3d(parent.rotationPointX / 16, -parent.rotationPointY / 16, -parent.rotationPointZ / 16));
//            boxRotateX.rotX(parent.rotateAngleX);
//            boxRotateY.rotY(-parent.rotateAngleY);
//            boxRotateZ.rotZ(-parent.rotateAngleZ);
//
//            boxRotateX.transform(rendererPos);
//            boxRotateY.transform(rendererPos);
//            boxRotateZ.transform(rendererPos);
//            boxTranslate.transform(rendererPos);
//
//            return getModelPos(parent, new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ()));
//        }
//        return new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ());
        return new Vec3d(0, 0, 0);
    }

    public Vec3d getWorldRotation(Entity entity, float delta) {
        Vec3d modelRot = getModelRotation(this, new Vec3d(rotateAngleX, -rotateAngleY, -rotateAngleZ));
        double x = modelRot.x;
        double y = modelRot.y;
        double z = modelRot.z;
        y += -Math.toRadians(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta);
        return new Vec3d(x, y, z);
    }

    public Vec3d getModelRotation(AdvancedModelRenderer modelRenderer, Vec3d recurseValue) {
        double x = recurseValue.x;
        double y = recurseValue.y;
        double z = recurseValue.z;

        AdvancedModelRenderer parent = modelRenderer.getParent();
        if (parent != null) {
            x += parent.rotateAngleX;
            y += -parent.rotateAngleY;
            z += -parent.rotateAngleZ;

            return getModelRotation(parent, new Vec3d(x, y, z));
        }
        return new Vec3d(x, y, z);
    }

    public void setWorldPos(Entity entity, Vec3d pos, float delta) {
//        Matrix4d entityTranslate = new Matrix4d();
//        Matrix4d entityRotate = new Matrix4d();
//        float dx = (float) (entity.prevPosX + (entity.posX - entity.prevPosX) * delta);
//        float dy = (float) (entity.prevPosY + (entity.posY - entity.prevPosY) * delta);
//        float dz = (float) (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * delta);
//        entityTranslate.set(new Vector3d(dx, dy, dz));
//        entityRotate.rotY(-Math.toRadians(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta));
//        entityTranslate.invert();
//        entityRotate.invert();
//        Point3d rendererPos = new Point3d(pos.x, pos.y, pos.z);
//        entityTranslate.transform(rendererPos);
//        entityRotate.transform(rendererPos);
//        rendererPos.y -= 1.5f;
//        rendererPos.scale(16);
//        setRotationPoint((float)rendererPos.x, -(float)rendererPos.y, -(float)rendererPos.z);
    }
}
