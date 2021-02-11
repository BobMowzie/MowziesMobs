package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

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
        double maxY = MAX_HEIGHT - sunstrike.getPosY();
        if (maxY < 0) {
            return;
        }
        RANDOMIZER.setSeed(sunstrike.getVariant());
        boolean isLingering = sunstrike.isLingering(delta);
        boolean isStriking = sunstrike.isStriking(delta);
        RenderSystem.pushMatrix();
        setupGL();
        Minecraft.getInstance().getTextureManager().bindTexture(getEntityTexture(sunstrike));
        if (isLingering) {
            drawScorch(sunstrike, delta);
        } else if (isStriking) {
            drawStrike(sunstrike, maxY, delta);
        }
        revertGL();
        RenderSystem.popMatrix();
    }

    private void drawScorch(EntitySunstrike sunstrike, float delta) {
        World world = renderManager.pointedEntity.getEntityWorld();
        double ex = sunstrike.lastTickPosX + (sunstrike.getPosX() - sunstrike.lastTickPosX) * delta;
        double ey = sunstrike.lastTickPosY + (sunstrike.getPosY() - sunstrike.lastTickPosY) * delta;
        double ez = sunstrike.lastTickPosZ + (sunstrike.getPosZ() - sunstrike.lastTickPosZ) * delta;
        int minX = MathHelper.floor(ex - LINGER_RADIUS);
        int maxX = MathHelper.floor(ex + LINGER_RADIUS);
        int minY = MathHelper.floor(ey - LINGER_RADIUS);
        int maxY = MathHelper.floor(ey);
        int minZ = MathHelper.floor(ez - LINGER_RADIUS);
        int maxZ = MathHelper.floor(ez + LINGER_RADIUS);
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        float opacityMultiplier = (0.6F + RANDOMIZER.nextFloat() * 0.2F) * world.getLight(new BlockPos(ex, ey, ez));
        byte mirrorX = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        byte mirrorZ = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        for (BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
            BlockState block = world.getBlockState(pos.down());
            if (block.getMaterial() != Material.AIR && world.getLight(pos) > 3) {
                drawScorchBlock(world, block, pos, ex, ey, ez, opacityMultiplier, mirrorX, mirrorZ);
            }
        }
        RenderSystem.depthMask(false);
        t.draw();
        RenderSystem.depthMask(true);
    }

    private void drawScorchBlock(World world, BlockState block, BlockPos pos, double ex, double ey, double ez, float opacityMultiplier, byte mirrorX, byte mirrorZ) {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        if (block.isNormalCube(world, pos)) {
            int bx = pos.getX(), by = pos.getY(), bz = pos.getZ();
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
                buf.pos(minX, y, minZ).tex(minU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
                buf.pos(minX, y, maxZ).tex(minU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
                buf.pos(maxX, y, maxZ).tex(maxU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
                buf.pos(maxX, y, minZ).tex(maxU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
            }
        }
    }

    private void drawStrike(EntitySunstrike sunstrike, double maxY, float delta) {
        float drawTime = sunstrike.getStrikeDrawTime(delta);
        float strikeTime = sunstrike.getStrikeDamageTime(delta);
        boolean drawing = sunstrike.isStrikeDrawing(delta);
        float opacity = drawing && drawTime < DRAW_FADE_IN_POINT ? drawTime * DRAW_FADE_IN_RATE : 1;
        if (drawing) {
            opacity *= DRAW_OPACITY_MULTIPLER;
        }
        drawRing(drawing, drawTime, strikeTime, opacity);
        GlStateManager.rotatef(-renderManager.getCameraOrientation().getY(), 0, 1, 0);
        drawBeam(drawing, drawTime, strikeTime, opacity, maxY);
    }

    private void drawRing(boolean drawing, float drawTime, float strikeTime, float opacity) {
        int frame = (int) (((drawing ? drawTime : strikeTime) * (RING_FRAME_COUNT + 1)));
        if (frame > RING_FRAME_COUNT) {
            frame = RING_FRAME_COUNT;
        }
        float minU = frame * RING_FRAME_SIZE / TEXTURE_WIDTH;
        float maxU = minU + RING_FRAME_SIZE / TEXTURE_WIDTH;
        float minV = drawing ? 0 : RING_FRAME_SIZE / TEXTURE_HEIGHT;
        float maxV = minV + RING_FRAME_SIZE / TEXTURE_HEIGHT;
        float offset = PIXEL_SCALE * RING_RADIUS * (frame % 2);
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        buf.pos(-RING_RADIUS + offset, 0, -RING_RADIUS + offset).tex(minU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(-RING_RADIUS + offset, 0, RING_RADIUS + offset).tex(minU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(RING_RADIUS + offset, 0, RING_RADIUS + offset).tex(maxU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(RING_RADIUS + offset, 0, -RING_RADIUS + offset).tex(maxU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        GlStateManager.depthMask(false);
        t.draw();
        GlStateManager.depthMask(true);
    }

    private void drawBeam(boolean drawing, float drawTime, float strikeTime, float opacity, double maxY) {
        int frame = drawing ? 0 : (int) (strikeTime * (BREAM_FRAME_COUNT + 1));
        if (frame > BREAM_FRAME_COUNT) {
            frame = BREAM_FRAME_COUNT;
        }
        double radius = BEAM_STRIKE_RADIUS;
        if (drawing) {
            radius = (BEAM_DRAW_END_RADIUS - BEAM_DRAW_START_RADIUS) * drawTime + BEAM_DRAW_START_RADIUS;
        }
        float minV = frame / TEXTURE_HEIGHT;
        float maxV = (frame + 1) / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        buf.pos(-radius, 0, 0).tex(BEAM_MIN_U, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(-radius, maxY, 0).tex(BEAM_MIN_U, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(radius, maxY, 0).tex(BEAM_MAX_U, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(radius, 0, 0).tex(BEAM_MAX_U, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        t.draw();
    }

    private void setupGL() {
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableBlend();
        RenderSystem.disableLighting();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0);
    }

    private void revertGL() {
        RenderSystem.disableBlend();
        RenderSystem.enableLighting();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
    }
}
