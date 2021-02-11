package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderBarako extends MobRenderer<EntityBarako, ModelBarako<EntityBarako>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    private static final double BURST_RADIUS = 3.5;
    private static final int BURST_FRAME_COUNT = 10;
    private static final int BURST_START_FRAME = 12;

    public RenderBarako(EntityRendererManager mgr) {
        super(mgr, new ModelBarako<>(), 1.0F);
    }

    @Override
    protected float getDeathMaxRotation(EntityBarako entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBarako entity) {
        return RenderBarako.TEXTURE;
    }

    @Override
    public void render(EntityBarako barako, float entityYaw, float delta, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (barako.getAnimation() == EntityBarako.ATTACK_ANIMATION && barako.getAnimationTick() > BURST_START_FRAME && barako.getAnimationTick() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
            GlStateManager.pushMatrix();
//            GlStateManager.translated(x, y + 1.1, z);
            setupGL();
            Minecraft.getInstance().getTextureManager().bindTexture(RenderSunstrike.TEXTURE);
            RenderSystem.rotatef(-renderManager.getCameraOrientation().getY(), 0, 1, 0);
            RenderSystem.rotatef(renderManager.getCameraOrientation().getX(), 1, 0, 0);
            RenderSystem.disableDepthTest();
            drawBurst(barako.getAnimationTick() - BURST_START_FRAME + delta);
            RenderSystem.enableDepthTest();
            revertGL();
            RenderSystem.popMatrix();
        }
        super.render(barako, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);
    }

    private void drawBurst(float tick) {
        int dissapateFrame = 6;
        float firstSpeed = 2f;
        float secondSpeed = 1f;
        int frame = ((int) (tick * firstSpeed) <= dissapateFrame) ? (int) (tick * firstSpeed) : (int) (dissapateFrame + (tick - dissapateFrame / firstSpeed) * secondSpeed);
        if (frame > BURST_FRAME_COUNT) {
            frame = BURST_FRAME_COUNT;
        }
        float minU = 0.0625f * frame;
        float maxU = minU + 0.0625f;
        float minV = 0.5f;
        float maxV = minV + 0.5f;
        float offset = 0.219f * (frame % 2);
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        float opacity = (tick < 8) ? 0.8f : 0.4f;
        buf.pos(-BURST_RADIUS + offset, -BURST_RADIUS + offset, 0).tex(minU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(-BURST_RADIUS + offset, BURST_RADIUS + offset, 0).tex(minU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(BURST_RADIUS + offset, BURST_RADIUS + offset, 0).tex(maxU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(BURST_RADIUS + offset, -BURST_RADIUS + offset, 0).tex(maxU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        RenderSystem.disableDepthTest();
        t.draw();
        RenderSystem.enableDepthTest();
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
