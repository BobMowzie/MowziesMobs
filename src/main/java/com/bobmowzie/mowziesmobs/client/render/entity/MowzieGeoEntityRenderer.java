package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public abstract class MowzieGeoEntityRenderer<T extends LivingEntity & GeoEntity> extends GeoEntityRenderer<T> {

    protected MowzieGeoEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    public MowzieGeoModel<T> getMowzieGeoModel() {
        return (MowzieGeoModel<T>) super.getGeoModel();
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone == null) return;
        poseStack.pushPose();
        if (bone instanceof MowzieGeoBone mowzieGeoBone && mowzieGeoBone.isForceMatrixTransform()) {
            PoseStack.Pose last = poseStack.last();
            float lerpBodyRot = animatable == null ? 0 : Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
            double d0 = Mth.lerp(partialTick, animatable.xOld, animatable.getX());
            double d1 = Mth.lerp(partialTick, animatable.yOld, animatable.getY());
            double d2 = Mth.lerp(partialTick, animatable.zOld, animatable.getZ());
            Matrix4f matrix4f = new Matrix4f();
            matrix4f = matrix4f.translate(0, -0.01f, 0);
            matrix4f = matrix4f.translate((float) -d0, (float) -d1, (float) -d2);
            matrix4f = matrix4f.mul(bone.getWorldSpaceMatrix());
            matrix4f = matrix4f.rotate(Axis.YP.rotationDegrees(-180f - lerpBodyRot));
            last.pose().mul(matrix4f);
            last.normal().mul(bone.getWorldSpaceNormal());

            RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        }
        else {
            boolean rotOverride = false;
            if (bone instanceof MowzieGeoBone mowzieGeoBone) {
                rotOverride = mowzieGeoBone.rotationOverride != null;
            }

            RenderUtils.translateMatrixToBone(poseStack, bone);
            RenderUtils.translateToPivotPoint(poseStack, bone);

            if (bone instanceof MowzieGeoBone mowzieGeoBone) {
                if (!mowzieGeoBone.inheritRotation && !mowzieGeoBone.inheritTranslation) {
                    poseStack.last().pose().identity();
                    poseStack.last().pose().mul(this.entityRenderTranslations);
                } else if (!mowzieGeoBone.inheritRotation) {
                    Vector4f t = new Vector4f().mul(poseStack.last().pose());
                    poseStack.last().pose().identity();
                    poseStack.translate(t.x, t.y, t.z);
                } else if (!mowzieGeoBone.inheritTranslation) {
                    MowzieGeoBone.removeMatrixTranslation(poseStack.last().pose());
                    poseStack.last().pose().mul(this.entityRenderTranslations);
                }
            }

            if (rotOverride) {
                MowzieGeoBone mowzieGeoBone = (MowzieGeoBone) bone;
                poseStack.last().pose().mul(mowzieGeoBone.rotationOverride);
                poseStack.last().normal().mul(new Matrix3f(mowzieGeoBone.rotationOverride));
            } else {
                RenderUtils.rotateMatrixAroundBone(poseStack, bone);
            }

            RenderUtils.scaleMatrixForBone(poseStack, bone);

            if (bone.isTrackingMatrices()) {
                Matrix4f poseState = new Matrix4f(poseStack.last().pose());
                Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

                bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
                bone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, getRenderOffset(this.animatable, 1).toVector3f()));
                bone.setWorldSpaceMatrix(RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.animatable.position().toVector3f()));
            }

            RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        }

        renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (!isReRender)
            applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.popPose();
    }

    @Override
    public void renderChildBones(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (GeoBone childBone : bone.getChildBones()) {
            if (!bone.isHidingChildren() || (childBone instanceof MowzieGeoBone mowzieGeoBone && mowzieGeoBone.isDynamicJoint())) {
                renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }
}
