package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySolarBeam;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by jnad325 on 12/26/15.
 */
public class RenderSolarBeam extends Render {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/textureSolarBeam.png");

    private static final double TEXTURE_WIDTH = 256;

    private static final double TEXTURE_HEIGHT = 32;

    private static final double PIXEL_SCALE = 1 / 16D;

    private static final double START_RADIUS = 1.3;

    private static final double BEAM_RADIUS = 1;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        EntitySolarBeam solarBeam = (EntitySolarBeam) entity;
        double length = Math.sqrt(Math.pow(solarBeam.endPosX - solarBeam.posX, 2) + Math.pow(solarBeam.endPosY-solarBeam.posY, 2) + Math.pow(solarBeam.endPosZ - solarBeam.posZ, 2));
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        setupGL();
        bindEntityTexture(solarBeam);

        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
        renderStart();
        GL11.glRotatef(renderManager.playerViewX, -1, 0, 0);
        GL11.glRotatef(-renderManager.playerViewY, 0, -1, 0);

        renderBeam(length, 180/Math.PI * solarBeam.yaw, 180/Math.PI * solarBeam.pitch, 8);

        GL11.glTranslated(solarBeam.endPosX - solarBeam.posX, solarBeam.endPosY - solarBeam.posY, solarBeam.endPosZ - solarBeam.posZ);
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
        renderEnd();
        GL11.glRotatef(renderManager.playerViewX, -1, 0, 0);
        GL11.glRotatef(-renderManager.playerViewY, 0, -1, 0);

        revertGL();
        GL11.glPopMatrix();
    }

    private void renderStart() {
        double minU = 0;
        double minV = 0;
        double maxU = minU + 0.0625;
        double maxV = minV + 0.5;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-START_RADIUS, -START_RADIUS, 0, minU, minV);
        t.addVertexWithUV(-START_RADIUS, START_RADIUS, 0, minU, maxV);
        t.addVertexWithUV(START_RADIUS, START_RADIUS, 0, maxU, maxV);
        t.addVertexWithUV(START_RADIUS, -START_RADIUS, 0, maxU, minV);
        GL11.glDepthMask(false);
        t.draw();
        GL11.glDepthMask(true);
    }

    private void renderEnd() {
        double minU = 0;
        double minV = 0;
        double maxU = minU + 0.0625;
        double maxV = minV + 0.5;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-START_RADIUS, -START_RADIUS, 0, minU, minV);
        t.addVertexWithUV(-START_RADIUS, START_RADIUS, 0, minU, maxV);
        t.addVertexWithUV(START_RADIUS, START_RADIUS, 0, maxU, maxV);
        t.addVertexWithUV(START_RADIUS, -START_RADIUS, 0, maxU, minV);
        GL11.glDepthMask(false);
        t.draw();
        GL11.glDepthMask(true);
    }

    private void renderBeam(double length, double yaw, double pitch, int frame) {
        double minU = 0;
        double minV = 0.5 + 1/TEXTURE_HEIGHT * frame;
        double maxU = minU + 20/TEXTURE_WIDTH;
        double maxV = minV + 1/TEXTURE_HEIGHT;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-BEAM_RADIUS, 0, 0, minU, minV);
        t.addVertexWithUV(-BEAM_RADIUS, length, 0, minU, maxV);
        t.addVertexWithUV(BEAM_RADIUS, length, 0, maxU, maxV);
        t.addVertexWithUV(BEAM_RADIUS, 0, 0, maxU, minV);
        GL11.glDepthMask(false);
        GL11.glRotatef(-90, 0, 0, 1);
        GL11.glRotatef((float) pitch, 0, 0, 1);
        GL11.glRotatef((float) yaw, 1, 0, 0);
        GL11.glRotatef(renderManager.playerViewX, 0, 1, 0);
        t.draw();
        GL11.glRotatef(-renderManager.playerViewX, 0, 1, 0);

        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-BEAM_RADIUS, 0, 0, minU, minV);
        t.addVertexWithUV(-BEAM_RADIUS, length, 0, minU, maxV);
        t.addVertexWithUV(BEAM_RADIUS, length, 0, maxU, maxV);
        t.addVertexWithUV(BEAM_RADIUS, 0, 0, maxU, minV);
        GL11.glRotatef(-renderManager.playerViewX, 0, 1, 0);
        GL11.glRotatef(180, 0, 1, 0);
        t.draw();
        GL11.glRotatef(-180, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 0, 1, 0);
        GL11.glRotatef((float) -yaw, 1, 0, 0);
        GL11.glRotatef((float) -pitch, 0, 0, 1);
        GL11.glRotatef(90, 0, 0, 1);
        GL11.glDepthMask(true);
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
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return TEXTURE;
    }
}
