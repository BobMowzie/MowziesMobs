package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelUmvuthi;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthiSunLayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUmvuthi extends MowzieGeoEntityRenderer<EntityUmvuthi> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/umvuthi.png");
    public static final ResourceLocation SUN = new ResourceLocation(MowziesMobs.MODID, "textures/effects/sun_effect.png");

    private static final float BURST_RADIUS = 3.5f;
    private static final int BURST_FRAME_COUNT = 10;
    private static final int BURST_START_FRAME = 12;
    private MultiBufferSource source;
    private EntityUmvuthi entity;


    public RenderUmvuthi(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelUmvuthi());
        this.addLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, mgr));
        this.addLayer(new GeckoSunblockLayer(this, mgr));
        this.addLayer(new UmvuthiSunLayer(this));

        this.shadowRadius = 1.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUmvuthi entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }

    @Override
    public void render(EntityUmvuthi umvuthi, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        source = bufferIn;
        this.entity = umvuthi;
        if (!umvuthi.isInvisible()) {
            if (umvuthi.getActiveAbilityType() == EntityUmvuthi.ATTACK_ABILITY && umvuthi.getActiveAbility().getTicksInUse() > BURST_START_FRAME && umvuthi.getActiveAbility().getTicksInUse() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
                matrixStackIn.pushPose();
                Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
                matrixStackIn.mulPose(quat);
                matrixStackIn.translate(0, 1, 0);
                matrixStackIn.scale(0.8f, 0.8f, 0.8f);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
                PoseStack.Pose matrixstack$entry = matrixStackIn.last();
                Matrix4f matrix4f = matrixstack$entry.pose();
                Matrix3f matrix3f = matrixstack$entry.normal();
                drawBurst(matrix4f, matrix3f, ivertexbuilder, umvuthi.getActiveAbility().getTicksInUse() - BURST_START_FRAME + delta, packedLightIn);
                matrixStackIn.popPose();
            }

        }
//        if (barako.getActiveAbilityType() == EntityBarako.SUPERNOVA_ABILITY && barako.betweenHandPos != null && barako.betweenHandPos.length > 0) {
//            MowzieGeoBone betweenHands = getMowzieAnimatedGeoModel().getMowzieBone("betweenHands");
//            Vector3d betweenHandPos = betweenHands.getWorldPosition();
//            animatable.betweenHandPos[0] = new Vec3(betweenHandPos.x, betweenHandPos.y, betweenHandPos.z);
//        }

//        matrixStackIn.pushPose();
//        VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare( new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_8.png")));
//        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
//        matrixStackIn.scale(1.2f,1.2f,1.2f);
//        Matrix4f matrix4f = matrixstack$entry.pose();
//        Matrix3f matrix3f = matrixstack$entry.normal();
//        drawSun(matrix4f, matrix3f, ivertexbuilder, packedLightIn);
//        matrixStackIn.popPose();
        super.render(umvuthi, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);

        MowzieGeoBone head = getMowzieAnimatedGeoModel().getMowzieBone("sun_render");
        Vector3d worldPos = head.getWorldPosition();
        umvuthi.headPos[0] = new Vec3(worldPos.x, worldPos.y, worldPos.z);
    }

    @Override
    public boolean shouldRender(EntityUmvuthi umvuthi, Frustum frustum, double p_114493_, double p_114494_, double p_114495_) {
        boolean result = super.shouldRender(umvuthi, frustum, p_114493_, p_114494_, p_114495_);
        if (!result) umvuthi.headPos[0] = umvuthi.position().add(0, umvuthi.getEyeHeight(), 0);
        return result;
    }

    //    @Override
//    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//        if (bone.getName().equals("sun")){
//            Vector3d pos = bone.getWorldPosition();
//            Vec3 vec = new Vec3(pos.x,pos.y,pos.z);
//            if(this.entity!=null){
//                if(this.entity.level.isClientSide && this.entity.getRandom().nextFloat() < 0.5f){
//                    this.entity.level.addParticle(ParticleTypes.FLAME, vec.x, vec.y, vec.z, 0d, 0d, 0d );
//                }
//            }
//        }
//    }

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
