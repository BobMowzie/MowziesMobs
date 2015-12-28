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

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        EntitySolarBeam solarBeam = (EntitySolarBeam) entity;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        setupGL();
        bindEntityTexture(solarBeam);

        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
        renderStart();
        GL11.glRotatef(renderManager.playerViewX, -1, 0, 0);
        GL11.glRotatef(-renderManager.playerViewY, 0, -1, 0);

        GL11.glTranslated(solarBeam.endPosX - solarBeam.posX, solarBeam.endPosY - solarBeam.posY, solarBeam.endPosZ - solarBeam.posZ);
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
        renderEnd();
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
