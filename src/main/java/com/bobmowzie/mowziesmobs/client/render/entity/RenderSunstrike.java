package com.bobmowzie.mowziesmobs.client.render.entity;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;

@SideOnly(Side.CLIENT)
public class RenderSunstrike extends Render<EntitySunstrike> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/sunstrike.png");
    private static final Random RANDOMIZER = new Random(0);
    private static final double TEXTURE_WIDTH = 256;
    private static final double TEXTURE_HEIGHT = 32;
    private static final double BEAM_MIN_U = 224 / TEXTURE_WIDTH;
    private static final double BEAM_MAX_U = 1;
    private static final double PIXEL_SCALE = 1 / 16D;
    private static final int MAX_HEIGHT = 256;
    private static final float DRAW_FADE_IN_RATE = 2;
    private static final float DRAW_FADE_IN_POINT = 1 / DRAW_FADE_IN_RATE;
    private static final float DRAW_OPACITY_MULTIPLER = 0.7F;
    private static final double RING_RADIUS = 1.6;
    private static final int RING_FRAME_SIZE = 16;
    private static final int RING_FRAME_COUNT = 10;
    private static final int BREAM_FRAME_COUNT = 31;
    private static final double BEAM_DRAW_START_RADIUS = 2;
    private static final double BEAM_DRAW_END_RADIUS = 0.25;
    private static final double BEAM_STRIKE_RADIUS = 1;
    private static final double LINGER_RADIUS = 1.2;
    private static final double SCORCH_MIN_U = 192 / TEXTURE_WIDTH;
    private static final double SCORCH_MAX_U = SCORCH_MIN_U + RING_FRAME_SIZE / TEXTURE_WIDTH;
    private static final double SCORCH_MIN_V = 16 / TEXTURE_HEIGHT;
    private static final double SCORCH_MAX_V = SCORCH_MIN_V + RING_FRAME_SIZE / TEXTURE_HEIGHT;

    public RenderSunstrike(RenderManager mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySunstrike entity) {
        return RenderSunstrike.TEXTURE;
    }

    @Override
    public void doRender(EntitySunstrike sunstrike, double x, double y, double z, float yaw, float delta) {
        double maxY = MAX_HEIGHT - sunstrike.posY;
        if (maxY < 0) {
            return;
        }
        RANDOMIZER.setSeed(sunstrike.getVariant());
        boolean isLingering = sunstrike.isLingering(delta);
        boolean isStriking = sunstrike.isStriking(delta);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        setupGL();
        bindEntityTexture(sunstrike);
        if (isLingering) {
            drawScorch(sunstrike, delta);
        } else if (isStriking) {
            drawStrike(sunstrike, maxY, delta);
        }
        revertGL();
        GlStateManager.popMatrix();
    }

    private void drawScorch(EntitySunstrike sunstrike, float delta) {
        World world = renderManager.worldObj;
        double ex = sunstrike.lastTickPosX + (sunstrike.posX - sunstrike.lastTickPosX) * delta;
        double ey = sunstrike.lastTickPosY + (sunstrike.posY - sunstrike.lastTickPosY) * delta;
        double ez = sunstrike.lastTickPosZ + (sunstrike.posZ - sunstrike.lastTickPosZ) * delta;
        int minX = MathHelper.floor_double(ex - LINGER_RADIUS);
        int maxX = MathHelper.floor_double(ex + LINGER_RADIUS);
        int minY = MathHelper.floor_double(ey - LINGER_RADIUS);
        int maxY = MathHelper.floor_double(ey);
        int minZ = MathHelper.floor_double(ez - LINGER_RADIUS);
        int maxZ = MathHelper.floor_double(ez + LINGER_RADIUS);
        Tessellator t = Tessellator.getInstance();
        VertexBuffer buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        float opacityMultiplier = (0.6F + RANDOMIZER.nextFloat() * 0.2F) * renderManager.worldObj.getLightBrightness(new BlockPos(ex, ey, ez));
        byte mirrorX = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        byte mirrorZ = (byte) (RANDOMIZER.nextBoolean() ? -1 : 1);
        for (BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
            IBlockState block = world.getBlockState(pos.down());
            if (block.getMaterial() != Material.AIR && world.getLight(pos) > 3) {
                drawScorchBlock(world, block, pos, ex, ey, ez, opacityMultiplier, mirrorX, mirrorZ);
            }
        }
        GlStateManager.depthMask(false);
        t.draw();
        GlStateManager.depthMask(true);
    }

    private void drawScorchBlock(World world, IBlockState block, BlockPos pos, double ex, double ey, double ez, float opacityMultiplier, byte mirrorX, byte mirrorZ) {
        Tessellator t = Tessellator.getInstance();
        VertexBuffer buf = t.getBuffer();
        if (block.isBlockNormalCube()) {
            int bx = pos.getX(), by = pos.getY(), bz = pos.getZ();
            float opacity = (float) ((1 - (ey - by) / 2) * opacityMultiplier);
            if (opacity >= 0) {
                if (opacity > 1) {
                    opacity = 1;
                }
                AxisAlignedBB aabb = block.getBoundingBox(world, pos);
                double minX = bx + aabb.minX - ex;
                double maxX = bx + aabb.maxX - ex;
                double y = by + aabb.minY - ey + 0.015625;
                double minZ = bz + aabb.minZ - ez;
                double maxZ = bz + aabb.maxZ - ez;
                double minU = (mirrorX * minX / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                double maxU = (mirrorX * maxX / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                double minV = (mirrorZ * minZ / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                double maxV = (mirrorZ * maxZ / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
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
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        drawBeam(drawing, drawTime, strikeTime, opacity, maxY);
    }

    private void drawRing(boolean drawing, float drawTime, float strikeTime, float opacity) {
        int frame = (int) (((drawing ? drawTime : strikeTime) * (RING_FRAME_COUNT + 1)));
        if (frame > RING_FRAME_COUNT) {
            frame = RING_FRAME_COUNT;
        }
        double minU = frame * RING_FRAME_SIZE / TEXTURE_WIDTH;
        double maxU = minU + RING_FRAME_SIZE / TEXTURE_WIDTH;
        double minV = drawing ? 0 : RING_FRAME_SIZE / TEXTURE_HEIGHT;
        double maxV = minV + RING_FRAME_SIZE / TEXTURE_HEIGHT;
        double offset = PIXEL_SCALE * RING_RADIUS * (frame % 2);
        Tessellator t = Tessellator.getInstance();
        VertexBuffer buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
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
        double minV = frame / TEXTURE_HEIGHT;
        double maxV = (frame + 1) / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        VertexBuffer buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        buf.pos(-radius, 0, 0).tex(BEAM_MIN_U, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(-radius, maxY, 0).tex(BEAM_MIN_U, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(radius, maxY, 0).tex(BEAM_MAX_U, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(radius, 0, 0).tex(BEAM_MAX_U, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        t.draw();
    }

    private void setupGL() {
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0);
    }

    private void revertGL() {
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
    }
}
