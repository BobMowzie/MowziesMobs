package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ArmorItem;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class MowzieGeoArmorRenderer<T extends ArmorItem & GeoItem> extends GeoArmorRenderer<T> {
    public boolean usingCustomPlayerAnimations = false;

    public MowzieGeoArmorRenderer(GeoModel<T> modelProvider) {
        super(modelProvider);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        usingCustomPlayerAnimations = false;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferIn, VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        if (usingCustomPlayerAnimations && bone instanceof MowzieGeoBone && ((MowzieGeoBone) bone).isForceMatrixTransform()) {
            PoseStack.Pose last = poseStack.last();
            last.pose().identity();
            last.normal().identity();
            Matrix4f matrix4f = bone.getWorldSpaceMatrix();
            last.pose().mul(matrix4f);
            last.normal().mul(bone.getWorldSpaceNormal());
            poseStack.mulPose(MathUtils.quatFromRotationXYZ(0, 0, 180, true));
            poseStack.translate(0, -1.5, 0);
        }
        else {
            RenderUtils.prepMatrixForBone(poseStack, bone);
        }
        renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildBones(poseStack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    public void copyFrom(ModelPart modelPart, GeoBone geoBone, float offsetX, float offsetY, float offsetZ) {
        if (usingCustomPlayerAnimations && modelPart instanceof ModelPartMatrix && geoBone instanceof MowzieGeoBone) {
            MowzieGeoBone thisBone = (MowzieGeoBone) geoBone;
            ModelPartMatrix other = (ModelPartMatrix) modelPart;
            thisBone.setWorldSpaceNormal(other.getWorldNormal());
            thisBone.setWorldSpaceMatrix(other.getWorldXform());
            thisBone.setForceMatrixTransform(true);
        }
        else {
            RenderUtils.matchModelPartRot(modelPart, geoBone);
            this.head.updatePosition(modelPart.x + offsetX, -modelPart.y + offsetY, modelPart.z + offsetZ);
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;
            copyFrom(headPart, head, 0, 0, 0);
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;
            copyFrom(bodyPart, body, 0, 0, 0);
        }

        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;
            copyFrom(rightArmPart, rightArm, 5, 2, 0);
        }

        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;
            copyFrom(leftArmPart, leftArm,  -5, 2, 0);
        }

        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;
            copyFrom(rightLegPart, rightLeg, 2, 12, 0);

            if (this.rightBoot != null) {
                copyFrom(rightLegPart, rightBoot, 2, 12, 0);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;
            copyFrom(leftLegPart, leftLeg, -2, 12, 0);

            if (this.leftBoot != null) {
                copyFrom(leftLegPart, leftBoot, -2, 12, 0);
            }
        }
    }
}
