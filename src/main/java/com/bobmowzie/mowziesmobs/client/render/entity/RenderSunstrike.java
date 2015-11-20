package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSunstrike extends Render
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/sunstrike.png");

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

    private static final int RING_FRAME_COUNT = 11;

    private static final int BREAM_FRAME_COUNT = 31;

    private static final double BEAM_DRAW_START_RADIUS = 2;

    private static final double BEAM_DRAW_END_RADIUS = 0.25;

    private static final double BEAM_STRIKE_RADIUS = 1;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta)
    {
        EntitySunstrike sunstrike = (EntitySunstrike) entity;
        double maxY = MAX_HEIGHT - sunstrike.posY;
        if (maxY < 0)
        {
            return;
        }
        float drawTime = sunstrike.getStrikeDrawTime(delta);
        float strikeTime = sunstrike.getStrikeDamageTime(delta);
        boolean drawing = sunstrike.isStrikeDrawing(delta);
        float opacity = drawing && drawTime < DRAW_FADE_IN_POINT ? drawTime * DRAW_FADE_IN_RATE : 1;
        if (drawing)
        {
            opacity *= DRAW_OPACITY_MULTIPLER;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        setupGL();
        bindEntityTexture(sunstrike);
        drawRing(drawing, drawTime, strikeTime, opacity);
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        drawBeam(drawing, drawTime, strikeTime, opacity, maxY);
        revertGL();
        GL11.glPopMatrix();
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
