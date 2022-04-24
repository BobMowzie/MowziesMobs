package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Matrix3f;
import net.minecraft.world.phys.Matrix4f;
import net.minecraft.world.phys.Quaternion;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderSunstrike extends EntityRenderer<EntitySunstrike> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/sunstrike.png");
    private static final Random RANDOMIZER = new Random(0);
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float BEAM_MIN_U = 224f / TEXTURE_WIDTH;
    private static final float BEAM_MAX_U = 1;
    private static final float PIXEL_SCALE = 1 / 16f;
    private static final int MAX_HEIGHT = 256;
    private static final float DRAW_FADE_IN_RATE = 2;
    private static final float DRAW_FADE_IN_POINT = 1 / DRAW_FADE_IN_RATE;
    private static final float DRAW_OPACITY_MULTIPLER = 0.7f;
    private static final float RING_RADIUS = 1.6f;
    private static final int RING_FRAME_SIZE = 16;
    private static final int RING_FRAME_COUNT = 10;
    private static final int BREAM_FRAME_COUNT = 31;
    private static final float BEAM_DRAW_START_RADIUS = 2f;
    private static final float BEAM_DRAW_END_RADIUS = 0.25f;
    private static final float BEAM_STRIKE_RADIUS = 1f;
    private static final float LINGER_RADIUS = 1.2f;
    private static final float SCORCH_MIN_U = 192f / TEXTURE_WIDTH;
    private static final float SCORCH_MAX_U = SCORCH_MIN_U + RING_FRAME_SIZE / TEXTURE_WIDTH;
    private static final float SCORCH_MIN_V = 16f / TEXTURE_HEIGHT;
    private static final float SCORCH_MAX_V = SCORCH_MIN_V + RING_FRAME_SIZE / TEXTURE_HEIGHT;

    public RenderSunstrike(EntityRendererManager mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySunstrike entity) {
        return RenderSunstrike.TEXTURE;
    }

    @Override
    public void render(EntitySunstrike sunstrike, float entityYaw, float delta, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float maxY = (float) (MAX_HEIGHT - sunstrike.getY());
        if (maxY < 0) {
            return;
        }
        RANDOMIZER.setSeed(sunstrike.getVariant());
        boolean isLingering = sunstrike.isLingering(delta);
        boolean isStriking = sunstrike.isStriking(delta);
        matrixStackIn.push();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(MMRenderType.getGlowingEffect(RenderSunstrike.TEXTURE));
        if (isLingering) {
            drawScorch(sunstrike, delta, matrixStackIn, ivertexbuilder, packedLightIn);
        } else if (isStriking) {
            drawStrike(sunstrike, maxY, delta, matrixStackIn, ivertexbuilder, packedLightIn);
        }
        matrixStackIn.pop();
    }

    private void drawScorch(EntitySunstrike sunstrike, float delta, MatrixStack matrixStack, IVertexBuilder builder, int packedLightIn) {
        Level world = sunstrike.getEntityWorld();
        double ex = sunstrike.lastTickPosX + (sunstrike.getX() - sunstrike.lastTickPosX) * delta;
        double ey = sunstrike.lastTickPosY + (sunstrike.getY() - sunstrike.lastTickPosY) * delta;
        double ez = sunstrike.lastTickPosZ + (sunstrike.getZ() - sunstrike.lastTickPosZ) * delta;
        int minX = Mth.floor(ex - LINGER_RADIUS);
        int maxX = Mth.floor(ex + LINGER_RADIUS);
        int minY = Mth.floor(ey - LINGER_RADIUS);
        int maxY = Mth.floor(ey);
        int minZ = Mth.floor(ez - LINGER_RADIUS);
        int maxZ = Mth.floor(ez + LINGER_RADIUS);
        float opacityMultiplier = (0.6F + RANDOMIZER.nextFloat() * 0.2F) * world.getLight(new BlockPos(ex, ey, ez));
        byte mirrorX = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        byte mirrorZ = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        for (BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
            BlockState block = level.getBlockState(pos.below());
            if (block.getMaterial() != Material.AIR && world.getLight(pos) > 3) {
                drawScorchBlock(world, block, pos, ex, ey, ez, opacityMultiplier, mirrorX, mirrorZ, matrixStack, builder, packedLightIn);
            }
        }
    }

    private void drawScorchBlock(Level world, BlockState block, BlockPos pos, double ex, double ey, double ez, float opacityMultiplier, byte mirrorX, byte mirrorZ, MatrixStack matrixStack, IVertexBuilder builder, int packedLightIn) {
        MatrixStack.Entry matrixstack$entry = matrixStack.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        if (block.isNormalCube(world, pos)) {
            int bx = pos.x(), by = pos.y(), bz = pos.z();
            float opacity = (float) ((1 - (ey - by) / 2) * opacityMultiplier);
            if (opacity >= 0) {
                if (opacity > 1) {
                    opacity = 1;
                }
                AxisAlignedBB aabb = block.getCollisionShape(world, pos).getBoundingBox();
                float minX = (float) (bx + aabb.minX - ex);
                float maxX = (float) (bx + aabb.maxX - ex);
                float y = (float) (by + aabb.minY - ey + 0.015625f);
                float minZ = (float) (bz + aabb.minZ - ez);
                float maxZ = (float) (bz + aabb.maxZ - ez);
                float minU = (mirrorX * minX / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                float maxU = (mirrorX * maxX / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                float minV = (mirrorZ * minZ / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                float maxV = (mirrorZ * maxZ / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                drawVertex(matrix4f, matrix3f, builder, minX, y, minZ, minU, minV, opacity, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, minX, y, maxZ, minU, maxV, opacity, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, maxX, y, maxZ, maxU, maxV, opacity, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, maxX, y, minZ, maxU, minV, opacity, packedLightIn);
            }
        }
    }

    private void drawStrike(EntitySunstrike sunstrike, float maxY, float delta, MatrixStack matrixStack, IVertexBuilder builder, int packedLightIn) {
        float drawTime = sunstrike.getStrikeDrawTime(delta);
        float strikeTime = sunstrike.getStrikeDamageTime(delta);
        boolean drawing = sunstrike.isStrikeDrawing(delta);
        float opacity = drawing && drawTime < DRAW_FADE_IN_POINT ? drawTime * DRAW_FADE_IN_RATE : 1;
        if (drawing) {
            opacity *= DRAW_OPACITY_MULTIPLER;
        }
        drawRing(drawing, drawTime, strikeTime, opacity, matrixStack, builder, packedLightIn);
        matrixStack.rotate(new Quaternion(0, -Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getYaw(), 0, true));
        drawBeam(drawing, drawTime, strikeTime, opacity, maxY, matrixStack, builder, packedLightIn);
    }

    private void drawRing(boolean drawing, float drawTime, float strikeTime, float opacity, MatrixStack matrixStack, IVertexBuilder builder, int packedLightIn) {
        int frame = (int) (((drawing ? drawTime : strikeTime) * (RING_FRAME_COUNT + 1)));
        if (frame > RING_FRAME_COUNT) {
            frame = RING_FRAME_COUNT;
        }
        float minU = frame * RING_FRAME_SIZE / TEXTURE_WIDTH;
        float maxU = minU + RING_FRAME_SIZE / TEXTURE_WIDTH;
        float minV = drawing ? 0 : RING_FRAME_SIZE / TEXTURE_HEIGHT;
        float maxV = minV + RING_FRAME_SIZE / TEXTURE_HEIGHT;
        float offset = PIXEL_SCALE * RING_RADIUS * (frame % 2);
        MatrixStack.Entry matrixstack$entry = matrixStack.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        drawVertex(matrix4f, matrix3f, builder, -RING_RADIUS + offset, 0, -RING_RADIUS + offset, minU, minV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -RING_RADIUS + offset, 0, RING_RADIUS + offset, minU, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, RING_RADIUS + offset, 0, RING_RADIUS + offset, maxU, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, RING_RADIUS + offset, 0, -RING_RADIUS + offset, maxU, minV, opacity, packedLightIn);
    }

    private void drawBeam(boolean drawing, float drawTime, float strikeTime, float opacity, float maxY, MatrixStack matrixStack, IVertexBuilder builder, int packedLightIn) {
        int frame = drawing ? 0 : (int) (strikeTime * (BREAM_FRAME_COUNT + 1));
        if (frame > BREAM_FRAME_COUNT) {
            frame = BREAM_FRAME_COUNT;
        }
        float radius = BEAM_STRIKE_RADIUS;
        if (drawing) {
            radius = (BEAM_DRAW_END_RADIUS - BEAM_DRAW_START_RADIUS) * drawTime + BEAM_DRAW_START_RADIUS;
        }
        float minV = frame / TEXTURE_HEIGHT;
        float maxV = (frame + 1) / TEXTURE_HEIGHT;
        MatrixStack.Entry matrixstack$entry = matrixStack.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        drawVertex(matrix4f, matrix3f, builder, -radius, 0, 0, BEAM_MIN_U, minV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -radius, maxY, 0, BEAM_MIN_U, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, radius, maxY, 0, BEAM_MAX_U, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, radius, 0, 0, BEAM_MAX_U, minV, opacity, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, IVertexBuilder vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.pos(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).tex(textureX, textureY).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
