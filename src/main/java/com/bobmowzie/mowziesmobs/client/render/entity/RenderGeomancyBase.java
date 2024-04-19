package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderGeomancyBase<T extends EntityGeomancyBase & GeoEntity> extends GeoEntityRenderer<T> {
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/blocks/stone.png");
    private MultiBufferSource multiBufferSource;
    private T entity;

    protected RenderGeomancyBase(EntityRendererProvider.Context context, MowzieGeoModel<T> modelProvider) {
        super(context, modelProvider);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGeomancyBase entity) {
        return TEXTURE_STONE;
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.multiBufferSource = bufferSource;
        this.entity = animatable;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setCurrentMultiBufferSource(MultiBufferSource rtb) {
        this.multiBufferSource = rtb;
    }

    public MultiBufferSource getCurrentMultiBufferSource() {
        return this.multiBufferSource;
    }

    public T getEntity() {
        return entity;
    }
}
