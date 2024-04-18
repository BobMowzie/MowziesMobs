package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

    /* TODO: Use new per-bone geckolib armor renderer
    public void copyFrom(GeoBone geoBone, ModelPart modelRendererIn) {
        if (usingCustomPlayerAnimations && modelRendererIn instanceof ModelPartMatrix && geoBone instanceof MowzieGeoBone) {
            MowzieGeoBone thisBone = (MowzieGeoBone) geoBone;
            ModelPartMatrix other = (ModelPartMatrix) modelRendererIn;
            thisBone.setWorldSpaceNormal(other.getWorldNormal());
            thisBone.setWorldSpaceMatrix(other.getWorldXform());
            thisBone.setForceMatrixTransform(true);
        }
        else {
            GeoUtils.copyRotations(modelRendererIn, geoBone);
            geoBone.setPosX(this.head.x);
            geoBone.setPosY(-this.head.y);
            geoBone.setPosZ(this.head.z);
        }
    }

    @Override
    protected void fitToBiped() {
        if (this.headBone != null) {
            IBone headBone = this.getGeoModelProvider().getBone(this.headBone);
            copyFrom(headBone, head);
        }

        if (this.bodyBone != null) {
            IBone bodyBone = this.getGeoModelProvider().getBone(this.bodyBone);
            copyFrom(bodyBone, body);
        }

        if (this.rightArmBone != null) {
            IBone rightArmBone = this.getGeoModelProvider().getBone(this.rightArmBone);
            copyFrom(rightArmBone, rightArm);
        }

        if (this.leftArmBone != null) {
            IBone leftArmBone = this.getGeoModelProvider().getBone(this.leftArmBone);
            copyFrom(leftArmBone, leftArm);
        }

        if (this.rightLegBone != null) {
            IBone rightLegBone = this.getGeoModelProvider().getBone(this.rightLegBone);
            copyFrom(rightLegBone, rightLeg);

            if (this.rightBootBone != null) {
                IBone rightBootBone = this.getGeoModelProvider().getBone(this.rightBootBone);
                copyFrom(rightBootBone, rightLeg);
            }
        }

        if (this.leftLegBone != null) {
            IBone leftLegBone = this.getGeoModelProvider().getBone(this.leftLegBone);
            copyFrom(leftLegBone, leftLeg);

            if (this.leftBootBone != null) {
                IBone leftBootBone = this.getGeoModelProvider().getBone(this.leftBootBone);
                copyFrom(leftBootBone, leftLeg);
            }
        }
    }*/
}
