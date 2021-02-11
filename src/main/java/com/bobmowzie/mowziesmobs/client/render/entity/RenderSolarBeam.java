package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderSolarBeam extends EntityRenderer<EntitySolarBeam> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/solar_beam.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.3f;
    private static final float BEAM_RADIUS = 1;
    private boolean clearerView = false;

    public RenderSolarBeam(EntityRendererManager mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySolarBeam entity) {
        return RenderSolarBeam.TEXTURE;
    }

    @Override
    public void render(EntitySolarBeam solarBeam, float entityYaw, float delta, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        clearerView = solarBeam.caster instanceof PlayerEntity && Minecraft.getInstance().player == solarBeam.caster && Minecraft.getInstance().gameSettings.thirdPersonView == 0;

        double length = Math.sqrt(Math.pow(solarBeam.collidePosX - solarBeam.getPosX(), 2) + Math.pow(solarBeam.collidePosY - solarBeam.getPosY(), 2) + Math.pow(solarBeam.collidePosZ - solarBeam.getPosZ(), 2));
        int frame = MathHelper.floor((solarBeam.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        RenderSystem.pushMatrix();
        RenderSystem.translated(solarBeam.getPosX(), solarBeam.getPosY(), solarBeam.getPosZ());
        setupGL();
//        bindEntityTexture(solarBeam);

        RenderSystem.depthMask(false);
        renderStart(frame);
        renderBeam(length, 180 / Math.PI * solarBeam.getYaw(), 180 / Math.PI * solarBeam.getPitch(), frame);
        RenderSystem.translated(solarBeam.collidePosX - solarBeam.getPosX(), solarBeam.collidePosY - solarBeam.getPosY(), solarBeam.collidePosZ - solarBeam.getPosZ());
        renderEnd(frame, solarBeam.blockSide);
        RenderSystem.depthMask(true);
        RenderSystem.translated(solarBeam.getPosX() - solarBeam.collidePosX, solarBeam.getPosY() - solarBeam.collidePosY, solarBeam.getPosZ() - solarBeam.collidePosZ);

        RenderSystem.colorMask(false, false, false, true);
        if (Minecraft.getInstance().gameSettings.thirdPersonView != 0) {
            renderStart(frame);
        }
        renderBeam(length, 180 / Math.PI * solarBeam.getYaw(), 180 / Math.PI * solarBeam.getPitch(), frame);
        RenderSystem.translated(solarBeam.collidePosX - solarBeam.getPosX(), solarBeam.collidePosY - solarBeam.getPosY(), solarBeam.collidePosZ - solarBeam.getPosZ());
        renderEnd(frame, null);
        RenderSystem.colorMask(true, true, true, true);

        revertGL();
        RenderSystem.popMatrix();
    }

    private void renderStart(int frame) {
        if (clearerView) {
            return;
        }
//        RenderSystem.rotatef(-renderManager.playerViewY, 0, 1, 0); TODO
//        RenderSystem.rotatef(renderManager.playerViewX, 1, 0, 0);
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-START_RADIUS, -START_RADIUS, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-START_RADIUS, START_RADIUS, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, START_RADIUS, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, -START_RADIUS, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        t.draw();
//        RenderSystem.rotatef(renderManager.playerViewX, -1, 0, 0); TODO
//        RenderSystem.rotatef(-renderManager.playerViewY, 0, -1, 0);
    }

    public static final VertexFormat POSITION_TEX_LMAP = new VertexFormat(ImmutableList.of(
            DefaultVertexFormats.POSITION_3F,
            DefaultVertexFormats.TEX_2F,
            DefaultVertexFormats.TEX_2S
    ));

    private void renderEnd(int frame, Direction side) {
//        GlStateManager.rotatef(-renderManager.playerViewY, 0, 1, 0); TODO
//        GlStateManager.rotatef(renderManager.playerViewX, 1, 0, 0);
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-START_RADIUS, -START_RADIUS, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-START_RADIUS, START_RADIUS, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, START_RADIUS, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, -START_RADIUS, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        t.draw();
//        RenderSystem.rotatef(renderManager.playerViewX, -1, 0, 0); TODO
//        RenderSystem.rotatef(-renderManager.playerViewY, 0, -1, 0);
        if (side == null) {
            return;
        }
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-START_RADIUS, -START_RADIUS, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-START_RADIUS, START_RADIUS, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, START_RADIUS, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(START_RADIUS, -START_RADIUS, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        RenderSystem.pushMatrix();
        switch (side) {
        case EAST:
            RenderSystem.rotatef(270, 0, 1, 0);
            RenderSystem.translatef(0, 0, -0.01f);
            break;
        case WEST:
            RenderSystem.rotatef(90, 0, 1, 0);
            RenderSystem.translatef(0, 0, -0.01f);
            break;
        case SOUTH:
            RenderSystem.rotatef(180, 0, 1, 0);
            RenderSystem.translatef(0, 0, -0.01f);
            break;
        case NORTH:
            RenderSystem.translatef(0, 0, -0.01f);
            break;
        case DOWN:
            RenderSystem.rotatef(-90, 1, 0, 0);
            RenderSystem.translatef(0, 0, -0.01f);
            break;
        case UP:
            RenderSystem.rotatef(90, 1, 0, 0);
            RenderSystem.translatef(0, 0, -0.01f);
        }
        t.draw();
        RenderSystem.popMatrix();
    }

    private void renderBeam(double length, double yaw, double pitch, int frame) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
        buf.pos(-BEAM_RADIUS, 0, 0).tex(minU, minV).lightmap(0, 240).endVertex();
        buf.pos(-BEAM_RADIUS, length, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
        buf.pos(BEAM_RADIUS, length, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
        buf.pos(BEAM_RADIUS, 0, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
        RenderSystem.rotatef(-90, 0, 0, 1);
        RenderSystem.rotatef((float) yaw, 1, 0, 0);
        RenderSystem.rotatef((float) pitch, 0, 0, 1);
        if (clearerView) {
            RenderSystem.rotatef(90, 0, 1, 0);
        } else {
//            RenderSystem.rotatef(renderManager.playerViewX, 0, 1, 0); TODO
        }
        t.draw();
        if (clearerView) {
            RenderSystem.rotatef(-90, 0, 1, 0);
        } else {
//            RenderSystem.rotatef(-renderManager.playerViewX, 0, 1, 0); TODO
        }

        if (!clearerView) {
            buf.begin(GL11.GL_QUADS, POSITION_TEX_LMAP);
            buf.pos(-BEAM_RADIUS, 0, 0).tex(minU, minV).lightmap(0, 240).endVertex();
            buf.pos(-BEAM_RADIUS, length, 0).tex(minU, maxV).lightmap(0, 240).endVertex();
            buf.pos(BEAM_RADIUS, length, 0).tex(maxU, maxV).lightmap(0, 240).endVertex();
            buf.pos(BEAM_RADIUS, 0, 0).tex(maxU, minV).lightmap(0, 240).endVertex();
//            RenderSystem.rotatef(-renderManager.playerViewX, 0, 1, 0); TODO
            RenderSystem.rotatef(180, 0, 1, 0);
            t.draw();
            RenderSystem.rotatef(-180, 0, 1, 0);
//            RenderSystem.rotatef(renderManager.playerViewX, 0, 1, 0); TODO
        }
        RenderSystem.rotatef((float) -pitch, 0, 0, 1);
        RenderSystem.rotatef((float) -yaw, 1, 0, 0);
        RenderSystem.rotatef(90, 0, 0, 1);
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
