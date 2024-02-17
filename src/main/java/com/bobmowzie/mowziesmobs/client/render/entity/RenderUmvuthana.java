package com.bobmowzie.mowziesmobs.client.render.entity;

import org.jetbrains.annotations.Nullable;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelUmvuthana;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthanaArmorLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthanaSunLayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoModel;

@OnlyIn(Dist.CLIENT)
public class RenderUmvuthana extends MowzieGeoEntityRenderer<EntityUmvuthana> {
    public RenderUmvuthana(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelUmvuthana());
        this.addLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addLayer(new GeckoSunblockLayer<>(this, renderManager));
        this.addLayer(new UmvuthanaArmorLayer(this, renderManager));
        this.addLayer(new UmvuthanaSunLayer(this, renderManager));
        this.shadowRadius = 0.6f;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUmvuthana entity) {
        return this.getGeoModelProvider().getTextureResource(entity);
    }

    @Override
    public void render(GeoModel model, EntityUmvuthana animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        MowzieGeoBone head = getMowzieAnimatedGeoModel().getMowzieBone("head");
        Vector3d worldPos = head.getWorldPosition();
        if (animatable.headPos != null && animatable.headPos.length > 0)
        animatable.headPos[0] = new Vec3(worldPos.x, worldPos.y, worldPos.z);

        if (!Minecraft.getInstance().isPaused()) {
            MowzieGeoBone mask = getMowzieAnimatedGeoModel().getMowzieBone("maskTwitcher");
            animatable.updateRattleSound(mask.getRotationZ());
        }
    }

    @Override
    public boolean shouldRender(EntityUmvuthana entity, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        boolean result = super.shouldRender(entity, p_114492_, p_114493_, p_114494_, p_114495_);
        if (!result) entity.headPos[0] = entity.position().add(0, entity.getEyeHeight(), 0);
        return result;
    }
}
