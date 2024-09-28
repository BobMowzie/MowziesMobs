package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayerSculptorStaff;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;

import java.util.Optional;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public final ResourceLocation staff_geo_location = new ResourceLocation(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    public final ResourceLocation staff_tex_location = new ResourceLocation(MowziesMobs.MODID, "textures/item/sculptor_staff.png");

    public RenderSculptor(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSculptor());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addRenderLayer(new GeckoSunblockLayer<>(this, renderManager));
        this.addRenderLayer(new ItemLayerSculptorStaff(this, "backItem"));
        this.addRenderLayer(new ItemLayerSculptorStaff(this,"itemHandLeft"));
        this.addRenderLayer(new ItemLayerSculptorStaff(this,"itemHandRight"));
        this.shadowRadius = 0.7f;

    }

    @Override
    public void preRender(PoseStack poseStack, EntitySculptor animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void render(EntitySculptor entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        Optional<GeoBone> disappearControllerBone = model.getBone("disappearController");
        disappearControllerBone.ifPresent(geoBone -> entity.disappearController = geoBone.getPosX());

        Optional<GeoBone> calfRight = model.getBone("calfRight");
        Optional<GeoBone> calfLeft = model.getBone("calfLeft");
        Optional<GeoBone> thighRight = model.getBone("thighRight");
        Optional<GeoBone> thighLeft = model.getBone("thighLeft");
        Optional<GeoBone> skirtEndRight = model.getBone("skirtEndRight");
        Optional<GeoBone> skirtEndLeft = model.getBone("skirtEndLeft");
        Optional<GeoBone> skirtFrontLocRight = model.getBone("skirtFrontLocRight");
        Optional<GeoBone> skirtFrontLocLeft = model.getBone("skirtFrontLocLeft");
        Optional<GeoBone> skirtBackLocRight = model.getBone("skirtBackLocRight");
        Optional<GeoBone> skirtBackLocLeft = model.getBone("skirtBackLocLeft");
        calfRight.ifPresent(geoBone -> entity.calfRPos = geoBone.getModelPosition());
        calfLeft.ifPresent(geoBone -> entity.calfLPos = geoBone.getModelPosition());
        thighRight.ifPresent(geoBone -> entity.thighRPos = geoBone.getModelPosition());
        thighLeft.ifPresent(geoBone -> entity.thighLPos = geoBone.getModelPosition());
        skirtEndRight.ifPresent(geoBone -> entity.skirtEndRPos = geoBone.getModelPosition());
        skirtEndLeft.ifPresent(geoBone -> entity.skirtEndLPos = geoBone.getModelPosition());
        skirtFrontLocRight.ifPresent(geoBone -> entity.skirtLocFrontRPos = geoBone.getModelPosition());
        skirtFrontLocLeft.ifPresent(geoBone -> entity.skirtLocFrontLPos = geoBone.getModelPosition());
        skirtBackLocRight.ifPresent(geoBone -> entity.skirtLocBackRPos = geoBone.getModelPosition());
        skirtBackLocLeft.ifPresent(geoBone -> entity.skirtLocBackLPos = geoBone.getModelPosition());

        this.shadowRadius = 0.7f * (1.0f - entity.disappearController);
    }

    @Override
    public Color getRenderColor(EntitySculptor animatable, float partialTick, int packedLight) {
        return Color.ofRGBA(1, 1, 1, 1.0f - animatable.disappearController);
    }

    @Override
    public RenderType getRenderType(EntitySculptor animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.disappearController > 0) {
            return RenderType.entityTranslucent(texture);
        }
        else {
            return super.getRenderType(animatable, texture, bufferSource, partialTick);
        }
    }
}
