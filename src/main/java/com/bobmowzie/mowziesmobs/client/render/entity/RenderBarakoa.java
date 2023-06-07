package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarakoa;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.StrixArmorLayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3d;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;

@OnlyIn(Dist.CLIENT)
public class RenderBarakoa extends MowzieGeoEntityRenderer<EntityBarakoa> {
    public EntityBarakoa animatable;

    public RenderBarakoa(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelBarakoa());
        this.addLayer(new StrixArmorLayer(this, renderManager));
        this.shadowRadius = 0.6f;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBarakoa entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }

    @Override
    public void render(GeoModel model, EntityBarakoa animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        boolean healingAnim = animatable.getActiveAbilityType() == EntityBarakoa.HEAL_ABILITY;
        float f = Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
//        if (healingAnim && animatable.staffPos != null && animatable.staffPos.length > 0) animatable.staffPos[0] = MowzieRenderUtils.getWorldPosFromModel(animatable, f, getModel().staffEnd); TODO
        MowzieGeoBone head = getMowzieAnimatedGeoModel().getMowzieBone("head");
        Vector3d worldPos = head.getWorldPosition();
        animatable.headPos[0] = new Vec3(worldPos.x, worldPos.y, worldPos.z);
    }

    @Override
    public void renderEarly(EntityBarakoa animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        this.animatable = animatable;
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

    }

    @Override
    public boolean shouldRender(EntityBarakoa entity, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        boolean result = super.shouldRender(entity, p_114492_, p_114493_, p_114494_, p_114495_);
        if (!result) entity.headPos[0] = null;
        return result;
    }
}
