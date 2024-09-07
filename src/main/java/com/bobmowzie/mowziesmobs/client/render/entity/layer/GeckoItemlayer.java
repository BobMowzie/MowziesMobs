package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class GeckoItemlayer<T extends MowzieGeckoEntity> extends BlockAndItemGeoLayer<T> {
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();


    private MowzieGeckoEntity entity;
    private String boneName;
    protected ItemStack renderedItem;

    public GeckoItemlayer(GeoRenderer<T> rendererIn, String boneName, ItemStack renderedItem) {
        super(rendererIn);
        this.boneName = boneName;
        this.renderedItem = renderedItem;
    }

    @Override
    protected ItemStack getStackForBone(GeoBone bone, T animatable) {
        if (!bone.isHidden() && bone.getName().equals(boneName)) {
            return renderedItem;
        }
        return null;
    }

    @Override
    protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, MowzieGeckoEntity animatable) {
        return switch (bone.getName()) {
            default -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
        };
    }

    @Override
    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource,
                              VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack stack = getStackForBone(bone, animatable);
        BlockState blockState = getBlockForBone(bone, animatable);

        if (stack == null && blockState == null)
            return;

        poseStack.pushPose();
        RenderUtils.translateToPivotPoint(poseStack, bone);

        if (stack != null)
            renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        if (blockState != null)
            renderBlockForBone(poseStack, bone, blockState, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        buffer = bufferSource.getBuffer(renderType);

        poseStack.popPose();
    }


//    @Override
//    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
//        for (GeoBone group : bakedModel.topLevelBones()) {
//            renderRecursively(animatable, group, poseStack, bufferSource, packedLight, packedOverlay);
//        }
//    }
//
//    public void renderRecursively(MowzieGeckoEntity entity, GeoBone bone, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
//                                  int packedOverlay) {
//        poseStack.pushPose();
//        RenderUtils.translateMatrixToBone(poseStack, bone);
//        RenderUtils.translateToPivotPoint(poseStack, bone);
//
//        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
//
//        RenderUtils.scaleMatrixForBone(poseStack, bone);
//
//
//        if(bone.getName().equals(this.boneName) && !bone.isHidden()){
//            poseStack.scale(1.5f,1.5f,1.5f);
//            Minecraft.getInstance().getItemRenderer().renderStatic(this.renderedItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
//                    packedLight, packedOverlay, poseStack, buffer, entity.level(), entity.getId());
//        }
//
//        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
//
//        if (!bone.isHidden()) {
//            for (GeoBone childBone : bone.getChildBones()) {
//                renderRecursively(entity,childBone, poseStack, buffer, packedLight, packedOverlay);
//            }
//        }
//
//
//
//        poseStack.popPose();
//    }

    public Vec3 getRenderOffset(MowzieGeckoEntity p_114483_, float p_114484_) {
        return Vec3.ZERO;
    }

}
