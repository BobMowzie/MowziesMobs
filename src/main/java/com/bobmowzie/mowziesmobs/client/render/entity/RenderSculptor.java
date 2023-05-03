package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoItemlayer;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public final ResourceLocation staff_geo_location = new ResourceLocation(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    public final ResourceLocation staff_tex_location = new ResourceLocation(MowziesMobs.MODID, "textures/item/sculptor_staff.png");
    public int staffController = 0;
    public EntitySculptor animatable;

    public RenderSculptor(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSculptor());
        this.addLayer(new GeckoItemlayer<>(this,"backItem", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.addLayer(new GeckoItemlayer<>(this,"itemHandLeft", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.addLayer(new GeckoItemlayer<>(this,"itemHandRight", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.shadowRadius = 0.7f;

    }

    @Override
    public ResourceLocation getTextureLocation(EntitySculptor entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }

    @Override
    public void render(GeoModel model, EntitySculptor animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        staffController = (int) model.getBone("staffController").get().getPositionX();

    }

    @Override
    protected void renderLayer(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntitySculptor animatable, float limbSwing, float limbSwingAmount, float partialTick, float rotFloat, float netHeadYaw, float headPitch, MultiBufferSource bufferSource2, GeoLayerRenderer<EntitySculptor> layerRenderer) {
        super.renderLayer(poseStack, bufferSource, packedLight, animatable, limbSwing, limbSwingAmount, partialTick, rotFloat, netHeadYaw, headPitch, bufferSource2, layerRenderer);


    }

    @Override
    public void renderEarly(EntitySculptor animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        this.animatable = animatable;
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

    }
}
