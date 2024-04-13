package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.Optional;

public class UmvuthanaSunLayer extends GeoLayerRenderer<EntityUmvuthana> {
    protected final EntityRenderDispatcher entityRenderDispatcher;

    public UmvuthanaSunLayer(IGeoRenderer<EntityUmvuthana> entityRendererIn, EntityRendererProvider.Context context) {
        super(entityRendererIn);
        entityRenderDispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityUmvuthana entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityLivingBaseIn.deathTime < 27 && entityLivingBaseIn.active && !(entityLivingBaseIn.getActiveAbilityType() == EntityUmvuthana.TELEPORT_ABILITY && entityLivingBaseIn.getActiveAbility().getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY)) {
            poseStack.pushPose();
            GeoModel model = this.entityRenderer.getGeoModelProvider().getModel(this.entityRenderer.getGeoModelProvider().getModelResource(entityLivingBaseIn));
            String boneName = "head";
            Optional<GeoBone> bone = model.getBone(boneName);
            if (bone.isPresent() && !bone.get().isHidden()) {
                Matrix4f boneMatrix = bone.get().getModelSpaceXform();
                poseStack.mulPoseMatrix(boneMatrix);
                PoseStack.Pose matrixstack$entry = poseStack.last();
                Matrix4f matrix4f = matrixstack$entry.pose();
                Vector4f vecTranslation = new Vector4f(0, 0, 0, 1);
                vecTranslation.transform(matrix4f);
                PoseStack newPoseStack = new PoseStack();
                newPoseStack.translate(vecTranslation.x(), vecTranslation.y(), vecTranslation.z());
                Vector4f vecScale = new Vector4f(1, 0, 0, 1);
                vecScale.transform(matrix4f);
                float scale = (float) new Vec3(vecScale.x() - vecTranslation.x(), vecScale.y() - vecTranslation.y(), vecScale.z() - vecTranslation.z()).length();
                newPoseStack.scale(scale, scale, scale);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(new ResourceLocation(MowziesMobs.MODID, "textures/particle/sun_no_glow.png"),true));
                PoseStack.Pose matrixstack$entry2 = newPoseStack.last();
                Matrix4f matrix4f2 = matrixstack$entry2.pose();
                Matrix3f matrix3f = matrixstack$entry.normal();
                drawSun(matrix4f2, matrix3f, ivertexbuilder, packedLightIn, entityLivingBaseIn.tickCount + partialTicks);
            }
            poseStack.popPose();
        }
    }

    private void drawSun(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, int packedLightIn, float time) {
        float sunRadius = 1.2f + (float) Math.sin(time * 4) * 0.085f;
        this.drawVertex(matrix4f, matrix3f, builder, -sunRadius, -sunRadius, 0, 0, 0, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -sunRadius, sunRadius, 0, 0, 1, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, sunRadius, sunRadius, 0, 1, 1, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, sunRadius, -sunRadius, 0, 1, 0, 1, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1f, 1f, 1f, 1.0f).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normals, 1.0F, 1.0F, 1.0F).endVertex();
    }

}
