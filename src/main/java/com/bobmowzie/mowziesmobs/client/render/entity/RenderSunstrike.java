package com.bobmowzie.mowziesmobs.client.render.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;

public class RenderSunstrike extends Render
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/textureSunstrike.png");

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

	private static final double SCORCH_MIN_U = 11 * RING_FRAME_SIZE / TEXTURE_WIDTH;

	private static final double SCORCH_MAX_U = SCORCH_MIN_U + RING_FRAME_SIZE / TEXTURE_WIDTH;

	private static final double SCORCH_MIN_V = RING_FRAME_SIZE / TEXTURE_HEIGHT;

	private static final double SCORCH_MAX_V = SCORCH_MIN_V + RING_FRAME_SIZE / TEXTURE_HEIGHT;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta)
    {
        EntitySunstrike sunstrike = (EntitySunstrike) entity;
        double maxY = MAX_HEIGHT - sunstrike.posY;
        if (maxY < 0)
        {
            return;
        }
        boolean isLingering = sunstrike.isLingering(delta);
        boolean isStriking = sunstrike.isStriking(delta);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        if (isLingering)
        {
            drawLingering(sunstrike, delta);
        }
        if (isStriking)
        {
            drawStrike(sunstrike, maxY, delta);
        }
        GL11.glPopMatrix();
    }

    private void drawLingering(EntitySunstrike sunstrike, float delta)
    {
        setupGL();
        bindEntityTexture(sunstrike);
        drawScorch(sunstrike, delta);
        revertGL();
    }

    private void drawScorch(EntitySunstrike sunstrike, float delta)
    {
        World world = renderManager.worldObj;
        double ex = sunstrike.lastTickPosX + (sunstrike.posX - sunstrike.lastTickPosX) * delta;
        double ey = sunstrike.lastTickPosY + (sunstrike.posY - sunstrike.lastTickPosY) * delta + sunstrike.getShadowSize();
        double ez = sunstrike.lastTickPosZ + (sunstrike.posZ - sunstrike.lastTickPosZ) * delta;
        int minX = MathHelper.floor_double(ex - LINGER_RADIUS);
        int maxX = MathHelper.floor_double(ex + LINGER_RADIUS);
        int minY = MathHelper.floor_double(ey - LINGER_RADIUS);
        int maxY = MathHelper.floor_double(ey);
        int minZ = MathHelper.floor_double(ez - LINGER_RADIUS);
        int maxZ = MathHelper.floor_double(ez + LINGER_RADIUS);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        for (int bx = minX; bx <= maxX; bx++)
        {
            for (int by = minY; by <= maxY; by++)
            {
                for (int bz = minZ; bz <= maxZ; bz++)
                {
                    Block block = world.getBlock(bx, by - 1, bz);

                    if (block.getMaterial() != Material.air && world.getBlockLightValue(bx, by, bz) > 3)
                    {
                        drawScorchBlock(block, bx, by, bz, ex, ey - sunstrike.getShadowSize(), ez);
                    }
                }
            }
        }
        GL11.glDepthMask(false);
        t.draw();
        GL11.glDepthMask(true);
    }

    private void drawScorchBlock(Block block, int bx, int by, int bz, double ex, double ey, double ez)
    {
        Tessellator t = Tessellator.instance;
        if (block.renderAsNormalBlock())
        {
            float opacity = (float) ((1 - (ey - by) / 2) / 2 * renderManager.worldObj.getLightBrightness(bx, by, bz));
            if (opacity >= 0)
            {
                if (opacity > 1)
                {
                    opacity = 1;
                }
                t.setColorRGBA_F(1, 1, 1, opacity);
                double minX = bx + block.getBlockBoundsMinX() - ex;
                double maxX = bx + block.getBlockBoundsMaxX() - ex;
                double y = by + block.getBlockBoundsMinY() - ey + 0.015625;
                double minZ = bz + block.getBlockBoundsMinZ() - ez;
                double maxZ = bz + block.getBlockBoundsMaxZ() - ez;
                double minU = (-minX / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                double maxU = (-maxX / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                double minV = (-minZ / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                double maxV = (-maxZ / 2 / LINGER_RADIUS + 0.5) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                t.addVertexWithUV(minX, y, minZ, minU, minV);
                t.addVertexWithUV(minX, y, maxZ, minU, maxV);
                t.addVertexWithUV(maxX, y, maxZ, maxU, maxV);
                t.addVertexWithUV(maxX, y, minZ, maxU, minV);
            }
        }
    }

    private void drawStrike(EntitySunstrike sunstrike, double maxY, float delta)
    {
        float drawTime = sunstrike.getStrikeDrawTime(delta);
        float strikeTime = sunstrike.getStrikeDamageTime(delta);
        boolean drawing = sunstrike.isStrikeDrawing(delta);
        float opacity = drawing && drawTime < DRAW_FADE_IN_POINT ? drawTime * DRAW_FADE_IN_RATE : 1;
        if (drawing)
        {
            opacity *= DRAW_OPACITY_MULTIPLER;
        }
        setupGL();
        bindEntityTexture(sunstrike);
        drawRing(drawing, drawTime, strikeTime, opacity);
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        drawBeam(drawing, drawTime, strikeTime, opacity, maxY);
        revertGL();
    }

    private void drawRing(boolean drawing, float drawTime, float strikeTime, float opacity)
    {
        int frame = (int) (((drawing ? drawTime : strikeTime) * (RING_FRAME_COUNT + 1)));
        if (frame > RING_FRAME_COUNT)
        {
            frame = RING_FRAME_COUNT;
        }
        double minU = frame * RING_FRAME_SIZE / TEXTURE_WIDTH;
        double maxU = minU + RING_FRAME_SIZE / TEXTURE_WIDTH;
        double minV = drawing ? 0 : RING_FRAME_SIZE / TEXTURE_HEIGHT;
        double maxV = minV + RING_FRAME_SIZE / TEXTURE_HEIGHT;
        double offset = PIXEL_SCALE * RING_RADIUS * (frame % 2);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, opacity);
        t.addVertexWithUV(-RING_RADIUS + offset, 0, -RING_RADIUS + offset, minU, minV);
        t.addVertexWithUV(-RING_RADIUS + offset, 0, RING_RADIUS + offset, minU, maxV);
        t.addVertexWithUV(RING_RADIUS + offset, 0, RING_RADIUS + offset, maxU, maxV);
        t.addVertexWithUV(RING_RADIUS + offset, 0, -RING_RADIUS + offset, maxU, minV);
        t.draw();
    }

    private void drawBeam(boolean drawing, float drawTime, float strikeTime, float opacity, double maxY)
    {
        int frame = drawing ? 0 : (int) (strikeTime * (BREAM_FRAME_COUNT + 1));
        if (frame > BREAM_FRAME_COUNT)
        {
            frame = BREAM_FRAME_COUNT;
        }
        double radius = BEAM_STRIKE_RADIUS;
        if (drawing)
        {
            radius = (BEAM_DRAW_END_RADIUS - BEAM_DRAW_START_RADIUS) * drawTime + BEAM_DRAW_START_RADIUS;
        }
        double minV = frame / TEXTURE_HEIGHT;
        double maxV = (frame + 1) / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, opacity);
        t.addVertexWithUV(-radius, 0, 0, BEAM_MIN_U, minV);
        t.addVertexWithUV(-radius, maxY, 0, BEAM_MIN_U, maxV);
        t.addVertexWithUV(radius, maxY, 0, BEAM_MAX_U, maxV);
        t.addVertexWithUV(radius, 0, 0, BEAM_MAX_U, minV);
        t.draw();
    }

    private void setupGL()
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
    }

    private void revertGL()
    {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TEXTURE;
    }
}
