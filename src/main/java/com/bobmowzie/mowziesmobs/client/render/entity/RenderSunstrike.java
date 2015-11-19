package com.bobmowzie.mowziesmobs.client.render.entity;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;

public class RenderSunstrike extends Render
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/sunstrike.png");

    private static final double TEXTURE_WIDTH = 256;

    private static final double TEXTURE_HEIGHT = 32;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta)
    {
        EntitySunstrike sunstrike = (EntitySunstrike) entity;
        float strikeTime = sunstrike.getStrikeDamageTime(delta);
        float drawTime = sunstrike.getStrikeDrawTime(delta);
        boolean drawing = sunstrike.isStrikeDrawing(delta);
        int bFrameCount = 23;
        int bFrame = drawing ? 0 : (int) (strikeTime * (bFrameCount + 1));
        if (bFrame > bFrameCount)
        {
            bFrame = bFrameCount;
        }
        double maxY = 256 - sunstrike.posY;
        if (maxY < 0)
        {
            return;
        }
        double bDrawStartRadius = 2;
        double bDrawEndRadius = 0.25;
        double bRadius = 0.95;
        double rRadius = 1.3;
        float drawFadeInRate = 4;
        float drawFadeInPoint = 1 / drawFadeInRate;
        if (drawing)
        {
            bRadius = (bDrawEndRadius - bDrawStartRadius) * drawTime + bDrawStartRadius;
        }
        double bMinU = 224 / TEXTURE_WIDTH;
        double bMaxU = 1;
        double bMinV = bFrame / TEXTURE_HEIGHT;
        double bMaxV = (bFrame + 1) / TEXTURE_HEIGHT;
        int rFrameSize = 16;
        int rFrameCount = 11;
        int ringFrame = (int) (((drawing ? drawTime : strikeTime) * (rFrameCount + 1)));
        if (ringFrame > rFrameCount)
        {
            ringFrame = rFrameCount;
        }
        double rMinU = (drawing ? ringFrame : (rFrameCount - ringFrame)) * rFrameSize / TEXTURE_WIDTH;
        double rMaxU = rMinU + rFrameSize / TEXTURE_WIDTH;
        double rMinV = drawing ? 0 : rFrameSize / TEXTURE_HEIGHT;
        double rMaxV = rMinV + rFrameSize / TEXTURE_HEIGHT;
        double rOffset = 0.0625F * rRadius * ((drawing ? ringFrame : ringFrame + 1) % 2);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, drawing && drawTime < drawFadeInPoint ? drawTime * drawFadeInRate : 1);
        // ring
        t.addVertexWithUV(-rRadius + rOffset, 0, -rRadius + rOffset, rMinU, rMinV);
        t.addVertexWithUV(-rRadius + rOffset, 0, rRadius + rOffset, rMinU, rMaxV);
        t.addVertexWithUV(rRadius + rOffset, 0, rRadius + rOffset, rMaxU, rMaxV);
        t.addVertexWithUV(rRadius + rOffset, 0, -rRadius + rOffset, rMaxU, rMinV);
        // beam
        t.addVertexWithUV(-bRadius, 0, 0, bMinU, bMinV);
        t.addVertexWithUV(-bRadius, maxY, 0, bMinU, bMaxV);
        t.addVertexWithUV(bRadius, maxY, 0, bMaxU, bMaxV);
        t.addVertexWithUV(bRadius, 0, 0, bMaxU, bMinV);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        bindEntityTexture(sunstrike);
        setupGL();
        t.draw();
        revertGL();
        GL11.glPopMatrix();
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
