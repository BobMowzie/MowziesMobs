package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySolarBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSolarBeam extends Render {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/textureSolarBeam.png");

    private static final double TEXTURE_WIDTH = 256;

    private static final double TEXTURE_HEIGHT = 32;

    private static final double PIXEL_SCALE = 1 / 16D;

    private static final double START_RADIUS = 1.3;

    private static final double BEAM_RADIUS = 1;

    private boolean clearerView = false;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        EntitySolarBeam solarBeam = (EntitySolarBeam) entity;

        clearerView = solarBeam.caster instanceof EntityPlayer && Minecraft.getMinecraft().thePlayer == solarBeam.caster && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;

        double length = Math.sqrt(Math.pow(solarBeam.collidePosX - solarBeam.posX, 2) + Math.pow(solarBeam.collidePosY - solarBeam.posY, 2) + Math.pow(solarBeam.collidePosZ - solarBeam.posZ, 2));
        int frame = MathHelper.floor_double((solarBeam.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        setupGL();
        bindEntityTexture(solarBeam);

        GL11.glDepthMask(false);
        renderStart(frame);
        renderBeam(length, 180 / Math.PI * solarBeam.getYaw(), 180 / Math.PI * solarBeam.getPitch(), frame);
        GL11.glTranslated(solarBeam.collidePosX - solarBeam.posX, solarBeam.collidePosY - solarBeam.posY, solarBeam.collidePosZ - solarBeam.posZ);
        renderEnd(frame, solarBeam.blockSide);
        GL11.glDepthMask(true);
        GL11.glTranslated(solarBeam.posX - solarBeam.collidePosX, solarBeam.posY - solarBeam.collidePosY, solarBeam.posZ - solarBeam.collidePosZ);

        GL11.glColorMask(false, false, false, true);
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            renderStart(frame);
        }
        renderBeam(length, 180 / Math.PI * solarBeam.getYaw(), 180 / Math.PI * solarBeam.getPitch(), frame);
        GL11.glTranslated(solarBeam.collidePosX - solarBeam.posX, solarBeam.collidePosY - solarBeam.posY, solarBeam.collidePosZ - solarBeam.posZ);
        renderEnd(frame, -1);
        GL11.glColorMask(true, true, true, true);

        revertGL();
        GL11.glPopMatrix();
    }

    private void renderStart(int frame) {
        if (clearerView) {
            return;
        }
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
        double minU = 0 + 16D / TEXTURE_WIDTH * frame;
        double minV = 0;
        double maxU = minU + 16D / TEXTURE_WIDTH;
        double maxV = minV + 16D / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-START_RADIUS, -START_RADIUS, 0, minU, minV);
        t.addVertexWithUV(-START_RADIUS, START_RADIUS, 0, minU, maxV);
        t.addVertexWithUV(START_RADIUS, START_RADIUS, 0, maxU, maxV);
        t.addVertexWithUV(START_RADIUS, -START_RADIUS, 0, maxU, minV);
        t.draw();
        GL11.glRotatef(renderManager.playerViewX, -1, 0, 0);
        GL11.glRotatef(-renderManager.playerViewY, 0, -1, 0);
    }

    private void renderEnd(int frame, int side) {
        GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
        double minU = 0 + 16D / TEXTURE_WIDTH * frame;
        double minV = 0;
        double maxU = minU + 16D / TEXTURE_WIDTH;
        double maxV = minV + 16D / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-START_RADIUS, -START_RADIUS, 0, minU, minV);
        t.addVertexWithUV(-START_RADIUS, START_RADIUS, 0, minU, maxV);
        t.addVertexWithUV(START_RADIUS, START_RADIUS, 0, maxU, maxV);
        t.addVertexWithUV(START_RADIUS, -START_RADIUS, 0, maxU, minV);
        t.draw();
        GL11.glRotatef(renderManager.playerViewX, -1, 0, 0);
        GL11.glRotatef(-renderManager.playerViewY, 0, -1, 0);
        if (side == -1) {
            return;
        }
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-START_RADIUS, -START_RADIUS, 0, minU, minV);
        t.addVertexWithUV(-START_RADIUS, START_RADIUS, 0, minU, maxV);
        t.addVertexWithUV(START_RADIUS, START_RADIUS, 0, maxU, maxV);
        t.addVertexWithUV(START_RADIUS, -START_RADIUS, 0, maxU, minV);
        if (side == 5) {
            GL11.glRotatef(270, 0, 1, 0);
            GL11.glTranslatef(0, 0, -0.01f);
            t.draw();
            GL11.glTranslatef(0, 0, 0.01f);
            GL11.glRotatef(-270, 0, 1, 0);
        }
        if (side == 4) {
            GL11.glRotatef(90, 0, 1, 0);
            GL11.glTranslatef(0, 0, -0.01f);
            t.draw();
            GL11.glTranslatef(0, 0, 0.01f);
            GL11.glRotatef(-90, 0, 1, 0);
        }
        if (side == 3) {
            GL11.glRotatef(180, 0, 1, 0);
            GL11.glTranslatef(0, 0, -0.01f);
            t.draw();
            GL11.glTranslatef(0, 0, 0.01f);
            GL11.glRotatef(-180, 0, 1, 0);
        }
        if (side == 2) {
            GL11.glTranslatef(0, 0, -0.01f);
            t.draw();
            GL11.glTranslatef(0, 0, 0.01f);
        }
        if (side == 0) {
            GL11.glRotatef(-90, 1, 0, 0);
            GL11.glTranslatef(0, 0, -0.01f);
            t.draw();
            GL11.glTranslatef(0, 0, 0.01f);
            GL11.glRotatef(90, 1, 0, 0);
        }
        if (side == 1) {
            GL11.glRotatef(90, 1, 0, 0);
            GL11.glTranslatef(0, 0, -0.01f);
            t.draw();
            GL11.glTranslatef(0, 0, 0.01f);
            GL11.glRotatef(-90, 1, 0, 0);
        }
    }

    private void renderBeam(double length, double yaw, double pitch, int frame) {
        double minU = 0;
        double minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        double maxU = minU + 20 / TEXTURE_WIDTH;
        double maxV = minV + 1 / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(-BEAM_RADIUS, 0, 0, minU, minV);
        t.addVertexWithUV(-BEAM_RADIUS, length, 0, minU, maxV);
        t.addVertexWithUV(BEAM_RADIUS, length, 0, maxU, maxV);
        t.addVertexWithUV(BEAM_RADIUS, 0, 0, maxU, minV);
        GL11.glRotatef(-90, 0, 0, 1);
        GL11.glRotatef((float) yaw, 1, 0, 0);
        GL11.glRotatef((float) pitch, 0, 0, 1);
        if (clearerView) {
            GL11.glRotatef(90, 0, 1, 0);
        } else {
            GL11.glRotatef(renderManager.playerViewX, 0, 1, 0);
        }
        t.draw();
        if (clearerView) {
            GL11.glRotatef(-90, 0, 1, 0);
        } else {
            GL11.glRotatef(-renderManager.playerViewX, 0, 1, 0);
        }

        if (!clearerView) {
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
        }
        GL11.glRotatef((float) -pitch, 0, 0, 1);
        GL11.glRotatef((float) -yaw, 1, 0, 0);
        GL11.glRotatef(90, 0, 0, 1);
    }

    private void setupGL() {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
    }

    private void revertGL() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return TEXTURE;
    }
}
