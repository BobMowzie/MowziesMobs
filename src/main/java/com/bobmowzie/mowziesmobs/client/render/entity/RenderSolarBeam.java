package com.bobmowzie.mowziesmobs.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;

@SideOnly(Side.CLIENT)
public class RenderSolarBeam extends Render<EntitySolarBeam> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/solar_beam.png");
    private static final double TEXTURE_WIDTH = 256;
    private static final double TEXTURE_HEIGHT = 32;
    private static final double START_RADIUS = 1.3;
    private static final double BEAM_RADIUS = 1;
    private boolean clearerView = false;

    public RenderSolarBeam(RenderManager mgr) {
        super(mgr);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySolarBeam entity) {
        return RenderSolarBeam.TEXTURE;
    }

    @Override
    public void doRender(EntitySolarBeam solarBeam, double x, double y, double z, float yaw, float delta) {
        clearerView = solarBeam.caster instanceof EntityPlayer && Minecraft.getMinecraft().player == solarBeam.caster && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;

        double length = Math.sqrt(Math.pow(solarBeam.collidePosX - solarBeam.posX, 2) + Math.pow(solarBeam.collidePosY - solarBeam.posY, 2) + Math.pow(solarBeam.collidePosZ - solarBeam.posZ, 2));
        int frame = MathHelper.floor((solarBeam.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        setupGL();
        bindEntityTexture(solarBeam);

        GlStateManager.depthMask(false);
        renderStart(frame);
        renderBeam(length, 180 / Math.PI * solarBeam.getYaw(), 180 / Math.PI * solarBeam.getPitch(), frame);
        GlStateManager.translate(solarBeam.collidePosX - solarBeam.posX, solarBeam.collidePosY - solarBeam.posY, solarBeam.collidePosZ - solarBeam.posZ);
        renderEnd(frame, solarBeam.blockSide);
        GlStateManager.depthMask(true);
        GlStateManager.translate(solarBeam.posX - solarBeam.collidePosX, solarBeam.posY - solarBeam.collidePosY, solarBeam.posZ - solarBeam.collidePosZ);

        GlStateManager.colorMask(false, false, false, true);
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            renderStart(frame);
        }
        renderBeam(length, 180 / Math.PI * solarBeam.getYaw(), 180 / Math.PI * solarBeam.getPitch(), frame);
        GlStateManager.translate(solarBeam.collidePosX - solarBeam.posX, solarBeam.collidePosY - solarBeam.posY, solarBeam.collidePosZ - solarBeam.posZ);
        renderEnd(frame, null);
        GlStateManager.colorMask(true, true, true, true);

        revertGL();
        GlStateManager.popMatrix();
    }

    private void renderStart(int frame) {
        if (clearerView) {
            return;
        }
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
        double minU = 0 + 16D / TEXTURE_WIDTH * frame;
        double minV = 0;
        double maxU = minU + 16D / TEXTURE_WIDTH;
        double maxV = minV + 16D / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-START_RADIUS, -START_RADIUS, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-START_RADIUS, START_RADIUS, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, START_RADIUS, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, -START_RADIUS, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        t.draw();
        GlStateManager.rotate(renderManager.playerViewX, -1, 0, 0);
        GlStateManager.rotate(-renderManager.playerViewY, 0, -1, 0);
    }

    public static final VertexFormat POSITION_TEX_LMAP = new VertexFormat()
        .addElement(DefaultVertexFormats.POSITION_3F)
        .addElement(DefaultVertexFormats.TEX_2F)
        .addElement(DefaultVertexFormats.TEX_2S);

    private void renderEnd(int frame, EnumFacing side) {
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
        double minU = 0 + 16D / TEXTURE_WIDTH * frame;
        double minV = 0;
        double maxU = minU + 16D / TEXTURE_WIDTH;
        double maxV = minV + 16D / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-START_RADIUS, -START_RADIUS, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-START_RADIUS, START_RADIUS, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, START_RADIUS, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, -START_RADIUS, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        t.draw();
        GlStateManager.rotate(renderManager.playerViewX, -1, 0, 0);
        GlStateManager.rotate(-renderManager.playerViewY, 0, -1, 0);
        if (side == null) {
            return;
        }
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-START_RADIUS, -START_RADIUS, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-START_RADIUS, START_RADIUS, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, START_RADIUS, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, -START_RADIUS, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        GlStateManager.pushMatrix();
        switch (side) {
        case EAST:
            GlStateManager.rotate(270, 0, 1, 0);
            GlStateManager.translate(0, 0, -0.01f);
            break;
        case WEST:
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.translate(0, 0, -0.01f);
            break;
        case SOUTH:
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.translate(0, 0, -0.01f);
            break;
        case NORTH:
            GlStateManager.translate(0, 0, -0.01f);
            break;
        case DOWN:
            GlStateManager.rotate(-90, 1, 0, 0);
            GlStateManager.translate(0, 0, -0.01f);
            break;
        case UP:
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.translate(0, 0, -0.01f);
        }
        t.draw();
        GlStateManager.popMatrix();
    }

    private void renderBeam(double length, double yaw, double pitch, int frame) {
        double minU = 0;
        double minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        double maxU = minU + 20 / TEXTURE_WIDTH;
        double maxV = minV + 1 / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-BEAM_RADIUS, 0, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-BEAM_RADIUS, length, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(BEAM_RADIUS, length, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(BEAM_RADIUS, 0, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        GlStateManager.rotate(-90, 0, 0, 1);
        GlStateManager.rotate((float) yaw, 1, 0, 0);
        GlStateManager.rotate((float) pitch, 0, 0, 1);
        if (clearerView) {
            GlStateManager.rotate(90, 0, 1, 0);
        } else {
            GlStateManager.rotate(renderManager.playerViewX, 0, 1, 0);
        }
        t.draw();
        if (clearerView) {
            GlStateManager.rotate(-90, 0, 1, 0);
        } else {
            GlStateManager.rotate(-renderManager.playerViewX, 0, 1, 0);
        }

        if (!clearerView) {
            buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
            buf.pos(-BEAM_RADIUS, 0, 0).tex(minU, minV).lightmap(0, 240).endVertex();
            buf.pos(-BEAM_RADIUS, length, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
            buf.pos(BEAM_RADIUS, length, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
            buf.pos(BEAM_RADIUS, 0, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
            GlStateManager.rotate(-renderManager.playerViewX, 0, 1, 0);
            GlStateManager.rotate(180, 0, 1, 0);
            t.draw();
            GlStateManager.rotate(-180, 0, 1, 0);
            GlStateManager.rotate(renderManager.playerViewX, 0, 1, 0);
        }
        GlStateManager.rotate((float) -pitch, 0, 0, 1);
        GlStateManager.rotate((float) -yaw, 1, 0, 0);
        GlStateManager.rotate(90, 0, 0, 1);
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
