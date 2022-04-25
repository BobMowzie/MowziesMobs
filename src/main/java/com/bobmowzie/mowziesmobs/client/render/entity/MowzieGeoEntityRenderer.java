package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.Iterator;

public abstract class MowzieGeoEntityRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {
    private Matrix4f renderEarlyMat = new Matrix4f();

    protected MowzieGeoEntityRenderer(EntityRenderDispatcher renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderEarly(T animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        renderEarlyMat = stackIn.last().pose().copy();
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.pushPose();
        boolean rotOverride = bone instanceof MowzieGeoBone && ((MowzieGeoBone)bone).rotMat != null;
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        if (rotOverride) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone) bone;
            stack.last().pose().multiply(mowzieBone.rotMat);
            stack.last().normal().mul(new Matrix3f(mowzieBone.rotMat));
        }
        else {
            RenderUtils.rotate(bone, stack);
        }
        RenderUtils.scale(bone, stack);
        if (bone instanceof MowzieGeoBone) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone) bone;
            if (mowzieBone.isTrackingXform()) {
                Matrix4f matBone = stack.last().pose().copy();
                Matrix4f renderEarlyMatInvert = renderEarlyMat.copy();
                renderEarlyMatInvert.invert();
                matBone.multiplyBackward(renderEarlyMatInvert);
                mowzieBone.getModelSpaceXform().set(matBone);
            }
        }
        RenderUtils.moveBackFromPivot(bone, stack);

        if (!bone.isHidden) {
            Iterator var10 = bone.childCubes.iterator();

            while(var10.hasNext()) {
                GeoCube cube = (GeoCube)var10.next();
                stack.pushPose();
                this.renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                stack.popPose();
            }

            var10 = bone.childBones.iterator();

            while(var10.hasNext()) {
                GeoBone childBone = (GeoBone)var10.next();
                this.renderRecursively(childBone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }

        stack.popPose();
    }
}
