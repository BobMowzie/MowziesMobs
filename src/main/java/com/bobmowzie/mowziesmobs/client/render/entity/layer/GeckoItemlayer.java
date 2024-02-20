package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class GeckoItemlayer<T extends MowzieGeckoEntity> extends GeoLayerRenderer<T> {
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();


    private MowzieGeckoEntity entity;
    private String boneName;
    private ItemStack renderedItem;

    public GeckoItemlayer(IGeoRenderer<T> entityRendererIn, String boneName, ItemStack renderedItem) {
        super(entityRendererIn);
        this.boneName = boneName;
        this.renderedItem = renderedItem;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.entity = entity;
        GeoModel model = this.entityRenderer.getGeoModelProvider().getModel(this.entityRenderer.getGeoModelProvider().getModelLocation(entity));
        renderRecursively(entity, model.topLevelBones.get(0), poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
    }



    public void renderRecursively(MowzieGeckoEntity entity, GeoBone bone, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                  int packedOverlay) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);

        boolean rotOverride = bone.rotMat != null;

        if (rotOverride) {
            poseStack.last().pose().multiply(bone.rotMat);
            poseStack.last().normal().mul(new Matrix3f(bone.rotMat));
        }
        else {
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        }

        RenderUtils.scaleMatrixForBone(poseStack, bone);


        if(bone.getName().equals(this.boneName) && !bone.isHidden()){
            poseStack.scale(1.5f,1.5f,1.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(this.renderedItem, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    packedLight, packedOverlay, poseStack, buffer, entity.getId());
        }

        if (bone.isTrackingXform()) {
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(new Vector3f(getRenderOffset(this.entity, 1)));
            bone.setLocalSpaceXform(localMatrix);

            Matrix4f worldState = localMatrix.copy();

            worldState.translate(new Vector3f(this.entity.position()));
            bone.setWorldSpaceXform(worldState);
        }

        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

        if (!bone.isHidden) {
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(entity,childBone, poseStack, buffer, packedLight, packedOverlay);
            }
        }



        poseStack.popPose();
    }

    public Vec3 getRenderOffset(MowzieGeckoEntity p_114483_, float p_114484_) {
        return Vec3.ZERO;
    }

}
