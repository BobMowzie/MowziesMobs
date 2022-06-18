package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.Iterator;

public abstract class MowzieGeoEntityRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {
    private Matrix4f renderEarlyMat = new Matrix4f();

    protected MowzieGeoEntityRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderEarly(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        renderEarlyMat = stackIn.getLast().getMatrix().copy();
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.push();
        boolean rotOverride = bone instanceof MowzieGeoBone && ((MowzieGeoBone)bone).rotMat != null;
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        if (rotOverride) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone) bone;
            stack.getLast().getMatrix().mul(mowzieBone.rotMat);
            stack.getLast().getNormal().mul(new Matrix3f(mowzieBone.rotMat));
        }
        else {
            RenderUtils.rotate(bone, stack);
        }
        RenderUtils.scale(bone, stack);
        if (bone instanceof MowzieGeoBone) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone) bone;
            if (mowzieBone.isTrackingXform()) {
                MatrixStack.Entry entry = stack.getLast();
                Matrix4f matBone = entry.getMatrix().copy();
                mowzieBone.getWorldSpaceXform().set(matBone.copy());

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
                stack.push();
                this.renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                stack.pop();
            }

            var10 = bone.childBones.iterator();

            while(var10.hasNext()) {
                GeoBone childBone = (GeoBone)var10.next();
                this.renderRecursively(childBone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }

        stack.pop();
    }
}
