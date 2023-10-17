package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarako;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUmvuthi extends MowzieGeoEntityRenderer<EntityUmvuthi> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    private static final ResourceLocation TEXTURE_OLD = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako_old.png");
    private static final float BURST_RADIUS = 3.5f;
    private static final int BURST_FRAME_COUNT = 10;
    private static final int BURST_START_FRAME = 12;

    public RenderUmvuthi(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelBarako());
//        addLayer(new SunblockLayer<>(this));
        this.shadowRadius = 1.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUmvuthi entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }

    @Override
    public void render(EntityUmvuthi barako, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!barako.isInvisible()) {
            if (barako.getActiveAbilityType() == EntityUmvuthi.ATTACK_ABILITY && barako.getActiveAbility().getTicksInUse() > BURST_START_FRAME && barako.getActiveAbility().getTicksInUse() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
                matrixStackIn.pushPose();
                Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
                matrixStackIn.mulPose(quat);
                matrixStackIn.translate(0, 1, 0);
                matrixStackIn.scale(0.8f, 0.8f, 0.8f);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
                PoseStack.Pose matrixstack$entry = matrixStackIn.last();
                Matrix4f matrix4f = matrixstack$entry.pose();
                Matrix3f matrix3f = matrixstack$entry.normal();
                drawBurst(matrix4f, matrix3f, ivertexbuilder, barako.getActiveAbility().getTicksInUse() - BURST_START_FRAME + delta, packedLightIn);
                matrixStackIn.popPose();
            }
        }
        super.render(barako, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);
        /*if (barako.getActiveAbilityType() == EntityBarako.SUPERNOVA_ABILITY && barako.betweenHandPos != null && barako.betweenHandPos.length > 0) {
            MowzieGeoBone betweenHands = getMowzieAnimatedGeoModel().getMowzieBone("betweenHands");
            Vector3d betweenHandPos = betweenHands.getWorldPosition();
            barako.betweenHandPos[0] = new Vec3(betweenHandPos.x, betweenHandPos.y, betweenHandPos.z);
        } TODO */
    }

    private void drawBurst(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, float tick, int packedLightIn) {
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

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
