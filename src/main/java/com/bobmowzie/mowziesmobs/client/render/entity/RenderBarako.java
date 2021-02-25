package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarako;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBarako extends MobRenderer<EntityBarako, ModelBarako<EntityBarako>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    private static final float BURST_RADIUS = 3.5f;
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
        if (!barako.isInvisible()) {
            if (barako.getAnimation() == EntityBarako.ATTACK_ANIMATION && barako.getAnimationTick() > BURST_START_FRAME && barako.getAnimationTick() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
                matrixStackIn.push();
                Quaternion quat = this.renderManager.getCameraOrientation();
                matrixStackIn.rotate(quat);
                matrixStackIn.translate(0, 1, 0);
                matrixStackIn.scale(0.8f, 0.8f, 0.8f);
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
                MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
                Matrix4f matrix4f = matrixstack$entry.getMatrix();
                Matrix3f matrix3f = matrixstack$entry.getNormal();
                drawBurst(matrix4f, matrix3f, ivertexbuilder, barako.getAnimationTick() - BURST_START_FRAME + delta, packedLightIn);
                matrixStackIn.pop();
            }
        }
        super.render(barako, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);
    }

    private void drawBurst(Matrix4f matrix4f, Matrix3f matrix3f, IVertexBuilder builder, float tick, int packedLightIn) {
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
        float opacity = (tick < 8) ? 0.8f : 0.4f;
        this.drawVertex(matrix4f, matrix3f, builder, -BURST_RADIUS + offset, -BURST_RADIUS + offset, 0, minU, minV, opacity, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -BURST_RADIUS + offset, BURST_RADIUS + offset, 0, minU, maxV, opacity, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, BURST_RADIUS + offset, BURST_RADIUS + offset, 0, maxU, maxV, opacity, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, BURST_RADIUS + offset, -BURST_RADIUS + offset, 0, maxU, minV, opacity, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, IVertexBuilder vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.pos(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).tex(textureX, textureY).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
