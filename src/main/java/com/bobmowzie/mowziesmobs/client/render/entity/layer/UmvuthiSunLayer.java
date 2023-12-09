package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class UmvuthiSunLayer extends GeoLayerRenderer<EntityUmvuthi> {
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();
    private final Vec3 v1 = new Vec3(-2,1,-1);
    private final Vec3 v2 = new Vec3(0,1,-1);
    private final Vec3 v3 = new Vec3(-1,0,1);
    private final Vec3 v4 = new Vec3(-1,0,-3);
    private final Vec3 v5 = new Vec3(-3,-1,0);
    private final Vec3 v6 = new Vec3(-3,-1,-2);
    private final Vec3 v7 = new Vec3(1,-1,0);
    private final Vec3 v8 = new Vec3(1,-1,-2);
    private final Vec3 v9 = new Vec3(0,-3,-1);
    private final Vec3 v10 = new Vec3(-2,-3,-1);
    private final Vec3 v11 = new Vec3(-1,-2,1);
    private final Vec3 v12 = new Vec3(-1,-2,-3);
    private final Vec3[] POS = {
            // Face 1
            v1,
            v2,
            v3,
            v1,
            // Face 2
            v1,
            v2,
            v4,
            v1,
            // Face 3
            v1,
            v5,
            v6,
            v1,
            // Face 4
            v2,
            v7,
            v8,
            v2,
            // Face 5
            v2,
            v8,
            v4,
            v2,
            // Face 6
            v1,
            v4,
            v6,
            v1,
            // Face 7
            v1,
            v5,
            v3,
            v1,
            // Face 8
            v2,
            v3,
            v7,
            v2,
            // Face 9
            v9,
            v7,
            v8,
            v9,
            // Face 10
            v5,
            v6,
            v10,
            v5,
            // Face 11
            v9,
            v10,
            v11,
            v9,
            // Face 12
            v9,
            v10,
            v12,
            v9,
            // Face 13
            v4,
            v6,
            v12,
            v4,
            // Face 14
            v4,
            v8,
            v12,
            v4,
            // Face 15
            v3,
            v5,
            v11,
            v3,
            // Face 16
            v3,
            v7,
            v11,
            v3,
            // Face 17
            v5,
            v10,
            v11,
            v5,
            // Face 18
            v7,
            v9,
            v11,
            v7,
            // Face 19
            v8,
            v9,
            v12,
            v8,
            // Face 20
            v6,
            v10,
            v12,
            v6,
    };

    public UmvuthiSunLayer(IGeoRenderer<EntityUmvuthi> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityUmvuthi entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        GeoModel model = this.entityRenderer.getGeoModelProvider().getModel(this.entityRenderer.getGeoModelProvider().getModelLocation(entityLivingBaseIn));
        renderRecursively(entityLivingBaseIn, model.topLevelBones.get(2), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, partialTicks);
    }

    public void renderRecursively(MowzieGeckoEntity entityLivingBaseIn, GeoBone bone, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                  int packedOverlay, float partialTicks) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);

        boolean rotOverride = bone.rotMat != null;

        if (rotOverride) {
            poseStack.last().pose().multiply(bone.rotMat);
            poseStack.last().normal().mul(new Matrix3f(bone.rotMat));
        }
        else {
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        }

        RenderUtils.scaleMatrixForBone(poseStack, bone);


        if(bone.getName().equals("sun_render") && !bone.isHidden()){
            VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(MowziesMobs.MODID, "textures/effects/sun_effect.png"),true));
            PoseStack.Pose matrixstack$entry = poseStack.last();
            poseStack.translate(0.02d,0d,-0.0d);
            poseStack.scale(0.06f,0.06f,0.06f);
            Matrix4f matrix4f = matrixstack$entry.pose();
            Matrix3f matrix3f = matrixstack$entry.normal();
            drawSun(matrix4f, matrix3f, ivertexbuilder, packedLight, entityLivingBaseIn.tickCount + partialTicks);
        }
        if (bone.isTrackingXform()) {
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(new Vector3f(getRenderOffset(entityLivingBaseIn, 1)));
            bone.setLocalSpaceXform(localMatrix);

            Matrix4f worldState = localMatrix.copy();

            worldState.translate(new Vector3f(entityLivingBaseIn.position()));
            bone.setWorldSpaceXform(worldState);
        }

        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

        if (!bone.isHidden) {
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(entityLivingBaseIn, childBone, poseStack, buffer, packedLight, packedOverlay, partialTicks);
            }
        }



        poseStack.popPose();
    }

    public Vec3 getRenderOffset(MowzieGeckoEntity p_114483_, float p_114484_) {
        return Vec3.ZERO;
    }

    private void drawSun(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, int packedLightIn, float time) {
        float scale = 0.9f + (float) Math.sin(time * 4) * 0.07f;
        for(int i = 0; i < 4; i++) {
            for (Vec3 vec : POS) {
                vec = vec.multiply(1f + (scale * i), 1f + (scale * i), 1f + (scale * i));
                builder.vertex(matrix4f, (float) ((float) vec.x + (scale * i)), (float) ((float) vec.y+ (scale * i)), (float) ((float) vec.z+ (scale * i)))
                        .color( 1f, 1f, .5f, 0.2f)
                        .uv(0.0f, 0.5f)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(matrix3f, 1f, 1f, 1f)
                        .endVertex();
            }
        }
        for (Vec3 vec : POS) {
            builder.vertex(matrix4f, (float) ((float) vec.x * 1.2f), (float) ((float) vec.y * 1.2f), (float) ((float) vec.z * 1.2f))
                    .color(1f, 1f, 1f, 1f)
                    .uv(0.0f, 0.5f)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(15728880)
                    .normal(matrix3f, 1f, 1f, 1f)
                    .endVertex();
        }
//.normal(matrix3f, (float) normal.x, (float) normal.y, (float) normal.z)
    }

}
