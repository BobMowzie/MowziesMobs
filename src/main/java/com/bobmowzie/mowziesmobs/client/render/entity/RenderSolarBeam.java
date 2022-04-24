package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSolarBeam extends EntityRenderer<EntitySolarBeam> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/solar_beam.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.3f;
    private static final float BEAM_RADIUS = 1;
    private boolean clearerView = false;

    public RenderSolarBeam(EntityRendererManager mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySolarBeam entity) {
        return RenderSolarBeam.TEXTURE;
    }

    @Override
    public void render(EntitySolarBeam solarBeam, float entityYaw, float delta, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        clearerView = solarBeam.caster instanceof PlayerEntity && Minecraft.getInstance().player == solarBeam.caster && Minecraft.getInstance().gameSettings.getPointOfView() == PointOfView.FIRST_PERSON;

        double collidePosX = solarBeam.prevCollidePosX + (solarBeam.collidePosX - solarBeam.prevCollidePosX) * delta;
        double collidePosY = solarBeam.prevCollidePosY + (solarBeam.collidePosY - solarBeam.prevCollidePosY) * delta;
        double collidePosZ = solarBeam.prevCollidePosZ + (solarBeam.collidePosZ - solarBeam.prevCollidePosZ) * delta;
        double posX = solarBeam.prevPosX + (solarBeam.getPosX() - solarBeam.prevPosX) * delta;
        double posY = solarBeam.prevPosY + (solarBeam.getPosY() - solarBeam.prevPosY) * delta;
        double posZ = solarBeam.prevPosZ + (solarBeam.getPosZ() - solarBeam.prevPosZ) * delta;
        float yaw = solarBeam.prevYaw + (solarBeam.renderYaw - solarBeam.prevYaw) * delta;
        float pitch = solarBeam.prevPitch + (solarBeam.renderPitch - solarBeam.prevPitch) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = MathHelper.floor((solarBeam.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(MMRenderType.getGlowingEffect(getEntityTexture(solarBeam)));

        renderStart(frame, matrixStackIn, ivertexbuilder, packedLightIn);
        renderBeam(length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, matrixStackIn, ivertexbuilder, packedLightIn);

        matrixStackIn.push();
        matrixStackIn.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, solarBeam.blockSide, matrixStackIn, ivertexbuilder, packedLightIn);
        matrixStackIn.pop();
    }

    private void renderFlatQuad(int frame, MatrixStack matrixStackIn, IVertexBuilder builder, int packedLightIn) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderStart(int frame, MatrixStack matrixStackIn, IVertexBuilder builder, int packedLightIn) {
        if (clearerView) {
            return;
        }
        matrixStackIn.push();
        Quaternion quat = this.renderManager.getCameraOrientation();
        matrixStackIn.rotate(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();
    }

    private void renderEnd(int frame, Direction side, MatrixStack matrixStackIn, IVertexBuilder builder, int packedLightIn) {
        matrixStackIn.push();
        Quaternion quat = this.renderManager.getCameraOrientation();
        matrixStackIn.rotate(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();
        if (side == null) {
            return;
        }
        matrixStackIn.push();
        Quaternion sideQuat = side.getRotation();
        sideQuat.multiply(new Quaternion(90, 0, 0, true));
        matrixStackIn.rotate(sideQuat);
        matrixStackIn.translate(0, 0, -0.01f);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();
    }

    private void drawBeam(float length, int frame, MatrixStack matrixStackIn, IVertexBuilder builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        drawVertex(matrix4f, matrix3f, builder, -BEAM_RADIUS, -1, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -BEAM_RADIUS, length, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, BEAM_RADIUS, length, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, BEAM_RADIUS, -1, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame,  MatrixStack matrixStackIn, IVertexBuilder builder, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(90, 0, 0, true));
        matrixStackIn.rotate(new Quaternion(0, 0, yaw - 90f, true));
        matrixStackIn.rotate(new Quaternion(-pitch, 0, 0, true));
        matrixStackIn.push();
        if (!clearerView) {
            matrixStackIn.rotate(new Quaternion(0, Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getPitch() + 90, 0, true));
        }
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();

        if (!clearerView) {
            matrixStackIn.push();
            matrixStackIn.rotate(new Quaternion(0, -Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getPitch() - 90, 0, true));
            drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, IVertexBuilder vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.pos(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).tex(textureX, textureY).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
