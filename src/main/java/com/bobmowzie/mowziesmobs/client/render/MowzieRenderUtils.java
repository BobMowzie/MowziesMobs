package com.bobmowzie.mowziesmobs.client.render;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;

public class MowzieRenderUtils {
    public static void matrixStackFromModel(PoseStack matrixStack, AdvancedModelRenderer modelRenderer) {
        AdvancedModelRenderer parent = modelRenderer.getParent();
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        modelRenderer.translateRotate(matrixStack);
    }

    public static Vec3 getWorldPosFromModel(Entity entity, float entityYaw, AdvancedModelRenderer modelRenderer) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(entity.getX(), entity.getY(), entity.getZ());
        matrixStack.mulPose(new Quaternion(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        MowzieRenderUtils.matrixStackFromModel(matrixStack, modelRenderer);
        PoseStack.Pose matrixEntry = matrixStack.last();
        Matrix4f matrix4f = matrixEntry.pose();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix4f);
        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    public static void translateRotateGeckolib(GeoBone bone, PoseStack matrixStackIn) {
        matrixStackIn.translate((double)(bone.rotationPointX / 16.0F), (double)(bone.rotationPointY / 16.0F), (double)(bone.rotationPointZ / 16.0F));
        if (bone.getRotationZ() != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
        }

        matrixStackIn.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static void matrixStackFromModel(PoseStack matrixStack, GeoBone geoBone) {
        GeoBone parent = geoBone.parent;
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        translateRotateGeckolib(geoBone, matrixStack);
    }

    public static Vec3 getWorldPosFromModel(Entity entity, float entityYaw, GeoBone geoBone) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(entity.getX(), entity.getY(), entity.getZ());
        matrixStack.mulPose(new Quaternion(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        MowzieRenderUtils.matrixStackFromModel(matrixStack, geoBone);
        PoseStack.Pose matrixEntry = matrixStack.last();
        Matrix4f matrix4f = matrixEntry.pose();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix4f);
        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    // Mirrored render utils
    public static void moveToPivotMirror(GeoCube cube, PoseStack stack) {
        Vector3f pivot = cube.pivot;
        stack.translate((double)(-pivot.x() / 16.0F), (double)(pivot.y() / 16.0F), (double)(pivot.z() / 16.0F));
    }

    public static void moveBackFromPivotMirror(GeoCube cube, PoseStack stack) {
        Vector3f pivot = cube.pivot;
        stack.translate((double)(pivot.x() / 16.0F), (double)(-pivot.y() / 16.0F), (double)(-pivot.z() / 16.0F));
    }

    public static void moveToPivotMirror(GeoBone bone, PoseStack stack) {
        stack.translate((double)(-bone.rotationPointX / 16.0F), (double)(bone.rotationPointY / 16.0F), (double)(bone.rotationPointZ / 16.0F));
    }

    public static void moveBackFromPivotMirror(GeoBone bone, PoseStack stack) {
        stack.translate((double)(bone.rotationPointX / 16.0F), (double)(-bone.rotationPointY / 16.0F), (double)(-bone.rotationPointZ / 16.0F));
    }

    public static void translateMirror(GeoBone bone, PoseStack stack) {
        stack.translate((double)(bone.getPositionX() / 16.0F), (double)(bone.getPositionY() / 16.0F), (double)(bone.getPositionZ() / 16.0F));
    }

    public static void rotateMirror(GeoBone bone, PoseStack stack) {
        if (bone.getRotationZ() != 0.0F) {
            stack.mulPose(Vector3f.ZP.rotation(-bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            stack.mulPose(Vector3f.YP.rotation(-bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            stack.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
        }

    }

    public static void rotateMirror(GeoCube bone, PoseStack stack) {
        Vector3f rotation = bone.rotation;
        stack.mulPose(new Quaternion(0.0F, 0.0F, -rotation.z(), false));
        stack.mulPose(new Quaternion(0.0F, -rotation.y(), 0.0F, false));
        stack.mulPose(new Quaternion(rotation.x(), 0.0F, 0.0F, false));
    }

    // Used for elytra layer, parrot layer, cape layer
    public static void transformStackToModelPart(PoseStack stack, ModelPartMatrix part) {
        stack.last().pose().setIdentity();
        stack.last().normal().setIdentity();
        stack.pushPose();
        stack.last().pose().multiply(part.getWorldXform());
        stack.last().normal().mul(part.getWorldNormal());
    }

    /* TODO: See if minecraft's new math library actually inverts matrices correctly
    public static Matrix4f invertAndMultiplyMatrices(Matrix4f baseMatrix, Matrix4f inputMatrix) {
        inputMatrix = new Matrix4f(inputMatrix);

        invertFixed(inputMatrix);
        inputMatrix.mul(baseMatrix);

        return inputMatrix;
    }

    public static boolean invertFixed(Matrix4f mat) {
        float f = mat.adjugateAndDet();
        if (Math.abs(f) > 1.0E-6F) {
            mat.multiply(1.0f / f);
            return true;
        } else {
            return false;
        }
    }*/
}
