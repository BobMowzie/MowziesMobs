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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import java.util.LinkedList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RenderUmvuthi extends MowzieGeoEntityRenderer<EntityUmvuthi> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    private static final ResourceLocation TEXTURE_OLD = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako_old.png");
    public static final ResourceLocation SUN = new ResourceLocation(MowziesMobs.MODID, "textures/effects/sun_effect.png");

    private static final float BURST_RADIUS = 3.5f;
    private static final int BURST_FRAME_COUNT = 10;
    private static final int BURST_START_FRAME = 12;
    private MultiBufferSource source;
    private final Vec3[] X_FACES = {
            // FRONT FACE
            new Vec3(-2, -2, 2),
            new Vec3(2, -2, 2),
            new Vec3(2, 2, 2),
            new Vec3(-2, 2, 2),
            // BACK FACE
            new Vec3(-2, -2, -2),
            new Vec3(2, -2, -2),
            new Vec3(2, 2, -2),
            new Vec3(-2, 2, -2),
    };
    private final Vec3[] Y_FACES = {
            // TOP FACE
            new Vec3(-2,2,-2),
            new Vec3(2,2,-2),
            new Vec3(2,2,2),
            new Vec3(-2,2,2),
            // BOTTOM FACE
            new Vec3(-2,-2,-2),
            new Vec3(2,-2,-2),
            new Vec3(2,-2,2),
            new Vec3(-2,-2,2)
    };
    private final Vec3[] Z_FACES = {
            // RIGHT FACE
            new Vec3(2, 2, -2),
            new Vec3(2, -2, -2),
            new Vec3(2, -2, 2),
            new Vec3(2, 2, 2),
            // LEFT_FACE
            new Vec3(-2, 2, -2),
            new Vec3(-2, -2, -2),
            new Vec3(-2, -2, 2),
            new Vec3(-2, 2, 2)
    };


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
        source = bufferIn;
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
//        matrixStackIn.pushPose();
//        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(SUN,false));
//        matrixStackIn.translate(0d,1.2d,0d);
//        matrixStackIn.scale(0.5F,0.5F,0.5F);
//        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
//        Matrix4f matrix4f = matrixstack$entry.pose();
//        Matrix3f matrix3f = matrixstack$entry.normal();
//        //drawSun(barako, matrix4f, matrix3f, ivertexbuilder, barako.tickCount + delta, packedLightIn);
//        matrixStackIn.popPose();
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, matrixStackIn, bufferIn, packedLightIn, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("sun") && source != null){
            matrixStackIn.pushPose();
            super.renderRecursively(bone, matrixStackIn, bufferIn, packedLightIn, packedOverlay, red, green, blue, alpha);

            VertexConsumer ivertexbuilder = source.getBuffer(RenderType.entityTranslucent(SUN,false));
            matrixStackIn.scale(0.5F,0.5F,0.5F);
            matrixStackIn.translate(-0.5d,-0.5d,0d);
            PoseStack.Pose matrixstack$entry = matrixStackIn.last();
            Matrix4f matrix4f = matrixstack$entry.pose();
            Matrix3f matrix3f = matrixstack$entry.normal();
            drawSun(matrix4f, matrix3f, bufferIn,  packedLightIn);
            matrixStackIn.popPose();
        }
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

    private void drawSun(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, int packedLightIn) {
        float start_opacity = 2.3F;
        float opacity_step = 0.09F;
        float size_start = 0.002F;
        float size_step = 0.02F;
        float uv_scale =0.1F;
        int uv_offset = 192;
        List<Vec3> x_list = new LinkedList<Vec3>();
        for(Vec3 v : X_FACES){
            x_list.add(v);
        }
        List<Vec3> y_list = new LinkedList<Vec3>();
        for(Vec3 v : Y_FACES){
            y_list.add(v);
        }
        List<Vec3> z_list = new LinkedList<Vec3>();
        for(Vec3 v : Z_FACES){
            z_list.add(v);
        }

        for(int i = 0; i < 25; i++) {
            for (Vec3 vec : x_list) {
                vec = vec.multiply(size_start + (size_step* i), size_start + (size_step * i), size_start + (size_step * i));
                float x = vec.x < 0 ? 0: uv_scale;
                float y = vec.y < 0 ? 0: uv_scale;
                builder.vertex(matrix4f, (float) vec.x, (float) vec.y + 5F, (float) vec.z)
                        .color((float) 1.0F, (float) 1.0F, (float) 1.0F, (float) (start_opacity - (i * opacity_step)))
                        .uv(x,y)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                        .endVertex();
            }
            for (Vec3 vec : y_list) {
                vec = vec.multiply(size_start + (size_step* i), size_start + (size_step * i), size_start + (size_step * i));
                int index = y_list.indexOf(vec);
                float x = vec.x < 0 ? 0: uv_scale;
                float z = vec.z < 0 ? 0: uv_scale;
                builder.vertex(matrix4f, (float) vec.x, (float) vec.y + 5F, (float) vec.z)
                        .color((float) 1.0F, (float) 1.0F, (float) 1.0F, (float) (start_opacity - (i * opacity_step)))
                        .uv(x,z)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                        .endVertex();
            }
            for (Vec3 vec : z_list) {
                vec = vec.multiply(size_start + (size_step* i), size_start + (size_step * i), size_start + (size_step * i));
                float z = vec.z < 0 ? 0: uv_scale;
                float y = vec.y < 0 ? 0: uv_scale;
                builder.vertex(matrix4f, (float) vec.x, (float) vec.y + 5F, (float) vec.z)
                        .color((float) 1.0F, (float) 1.0F, (float) 1.0F, (float) (start_opacity - (i * opacity_step)))
                        .uv(z,y)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                        .endVertex();
            }
        }

    }
}
