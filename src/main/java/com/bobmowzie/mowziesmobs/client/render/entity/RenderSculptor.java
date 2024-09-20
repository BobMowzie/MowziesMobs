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
    public int staffController = 0;
    public float disappearController = 0;

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

        Optional<GeoBone> staffControllerBone = model.getBone("staffController");
        staffControllerBone.ifPresent(geoBone -> staffController = (int) geoBone.getPosX());

        Optional<GeoBone> disappearControllerBone = model.getBone("disappearController");
        disappearControllerBone.ifPresent(geoBone -> disappearController = geoBone.getPosX());

        this.shadowRadius = 0.7f * (1.0f - disappearController);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, EntitySculptor animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (model instanceof ModelSculptor modelSculptor && modelSculptor.beardOriginal != null) {
            for (int i = 0; i < modelSculptor.beardOriginal.length; i++) {
                modelSculptor.beardOriginal[i].setHidden(animatable.dc != null);
//                modelSculptor.beardOriginal[i].setHidden(false);
                modelSculptor.beardOriginal[i].setTrackingMatrices(true);
            }
        }
        super.actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (model instanceof ModelSculptor modelSculptor) {
            if (animatable.dc != null) {
                if (!isReRender) {
                    animatable.dc.setChain(modelSculptor.beardOriginal, modelSculptor.beardDynamic);
                    animatable.dc.updateChain(Minecraft.getInstance().getFrameTime(), modelSculptor.beardOriginal, modelSculptor.beardDynamic, 0.1f, 0.1f, 0.5f, 0.02f, 10, true);
                }
                poseStack.pushPose();
                for (GeoBone group : modelSculptor.beardDynamic) {
                    renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
                            packedOverlay, red, green, blue, alpha);
                }
                poseStack.popPose();
            }
        }
    }

    @Override
    public Color getRenderColor(EntitySculptor animatable, float partialTick, int packedLight) {
        return Color.ofRGBA(1, 1, 1, 1.0f - disappearController);
    }

    @Override
    public RenderType getRenderType(EntitySculptor animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (disappearController > 0) {
            return RenderType.entityTranslucent(texture);
        }
        else {
            return super.getRenderType(animatable, texture, bufferSource, partialTick);
        }
    }
}
