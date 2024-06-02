package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

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

    public RenderSunstrike(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySunstrike entity) {
        return RenderSunstrike.TEXTURE;
    }

    @Override
    public void render(EntitySunstrike sunstrike, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float maxY = (float) (MAX_HEIGHT - sunstrike.getY());
        if (maxY < 0) {
            return;
        }
        RANDOMIZER.setSeed(sunstrike.getVariant());
        boolean isLingering = sunstrike.isLingering(delta);
        boolean isStriking = sunstrike.isStriking(delta);
        matrixStackIn.pushPose();
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getGlowingEffect(RenderSunstrike.TEXTURE));
        if (isLingering) {
            drawScorch(sunstrike, delta, matrixStackIn, ivertexbuilder, packedLightIn);
        } else if (isStriking) {
            drawStrike(sunstrike, maxY, delta, matrixStackIn, ivertexbuilder, packedLightIn);
        }
        matrixStackIn.popPose();
    }

    private void drawScorch(EntitySunstrike sunstrike, float delta, PoseStack matrixStack, VertexConsumer builder, int packedLightIn) {
        Level world = sunstrike.getCommandSenderWorld();
        double ex = sunstrike.xOld + (sunstrike.getX() - sunstrike.xOld) * delta;
        double ey = sunstrike.yOld + (sunstrike.getY() - sunstrike.yOld) * delta;
        double ez = sunstrike.zOld + (sunstrike.getZ() - sunstrike.zOld) * delta;
        int minX = Mth.floor(ex - LINGER_RADIUS);
        int maxX = Mth.floor(ex + LINGER_RADIUS);
        int minY = Mth.floor(ey - LINGER_RADIUS);
        int maxY = Mth.floor(ey);
        int minZ = Mth.floor(ez - LINGER_RADIUS);
        int maxZ = Mth.floor(ez + LINGER_RADIUS);
        float opacityMultiplier = (0.6F + RANDOMIZER.nextFloat() * 0.2F) * world.getMaxLocalRawBrightness(new BlockPos((int) ex, (int) ey, (int) ez));
        byte mirrorX = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        byte mirrorZ = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
            BlockState block = world.getBlockState(pos.below());
            if (!block.isAir() && world.getMaxLocalRawBrightness(pos) > 3) {
                drawScorchBlock(world, block, pos, ex, ey, ez, opacityMultiplier, mirrorX, mirrorZ, matrixStack, builder, packedLightIn);
            }
        }
    }

    private void drawScorchBlock(Level world, BlockState block, BlockPos pos, double ex, double ey, double ez, float opacityMultiplier, byte mirrorX, byte mirrorZ, PoseStack matrixStack, VertexConsumer builder, int packedLightIn) {
        PoseStack.Pose matrixstack$entry = matrixStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        if (block.isRedstoneConductor(world, pos)) {
            int bx = pos.getX(), by = pos.getY(), bz = pos.getZ();
            float opacity = (float) ((1 - (ey - by) / 2) * opacityMultiplier);
            if (opacity >= 0) {
                if (opacity > 1) {
                    opacity = 1;
                }
                VoxelShape shape = block.getBlockSupportShape(world, pos);
                if (!shape.isEmpty()) {
                    AABB aabb = shape.bounds();
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
    }

    private void drawStrike(EntitySunstrike sunstrike, float maxY, float delta, PoseStack matrixStack, VertexConsumer builder, int packedLightIn) {
        float drawTime = sunstrike.getStrikeDrawTime(delta);
        float strikeTime = sunstrike.getStrikeDamageTime(delta);
        boolean drawing = sunstrike.isStrikeDrawing(delta);
        float opacity = drawing && drawTime < DRAW_FADE_IN_POINT ? drawTime * DRAW_FADE_IN_RATE : 1;
        if (drawing) {
            opacity *= DRAW_OPACITY_MULTIPLER;
        }
        drawRing(drawing, drawTime, strikeTime, opacity, matrixStack, builder, packedLightIn);
        matrixStack.mulPose(MathUtils.quatFromRotationXYZ(0, -Minecraft.getInstance().gameRenderer.getMainCamera().getYRot(), 0, true));
        drawBeam(drawing, drawTime, strikeTime, opacity, maxY, matrixStack, builder, packedLightIn);
    }

    private void drawRing(boolean drawing, float drawTime, float strikeTime, float opacity, PoseStack matrixStack, VertexConsumer builder, int packedLightIn) {
        int frame = (int) (((drawing ? drawTime : strikeTime) * (RING_FRAME_COUNT + 1)));
        if (frame > RING_FRAME_COUNT) {
            frame = RING_FRAME_COUNT;
        }
        float minU = frame * RING_FRAME_SIZE / TEXTURE_WIDTH;
        float maxU = minU + RING_FRAME_SIZE / TEXTURE_WIDTH;
        float minV = drawing ? 0 : RING_FRAME_SIZE / TEXTURE_HEIGHT;
        float maxV = minV + RING_FRAME_SIZE / TEXTURE_HEIGHT;
        float offset = PIXEL_SCALE * RING_RADIUS * (frame % 2);
        PoseStack.Pose matrixstack$entry = matrixStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -RING_RADIUS + offset, 0, -RING_RADIUS + offset, minU, minV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -RING_RADIUS + offset, 0, RING_RADIUS + offset, minU, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, RING_RADIUS + offset, 0, RING_RADIUS + offset, maxU, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, RING_RADIUS + offset, 0, -RING_RADIUS + offset, maxU, minV, opacity, packedLightIn);
    }

    private void drawBeam(boolean drawing, float drawTime, float strikeTime, float opacity, float maxY, PoseStack matrixStack, VertexConsumer builder, int packedLightIn) {
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
        PoseStack.Pose matrixstack$entry = matrixStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -radius, 0, 0, BEAM_MIN_U, minV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -radius, maxY, 0, BEAM_MIN_U, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, radius, maxY, 0, BEAM_MAX_U, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, radius, 0, 0, BEAM_MAX_U, minV, opacity, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
