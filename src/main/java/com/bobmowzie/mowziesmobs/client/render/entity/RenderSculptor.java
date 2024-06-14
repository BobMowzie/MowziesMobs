package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoItemlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public final ResourceLocation staff_geo_location = new ResourceLocation(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    public final ResourceLocation staff_tex_location = new ResourceLocation(MowziesMobs.MODID, "textures/item/sculptor_staff.png");
    public int staffController = 0;

    public RenderSculptor(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSculptor());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addRenderLayer(new GeckoSunblockLayer<>(this, renderManager));
        this.addRenderLayer(new GeckoItemlayer<>(this, "backItem", new ItemStack(ItemHandler.SCULPTOR_STAFF.get(), 1)));
        this.addRenderLayer(new GeckoItemlayer<>(this,"itemHandLeft", new ItemStack(ItemHandler.SCULPTOR_STAFF.get(), 1)));
        this.addRenderLayer(new GeckoItemlayer<>(this,"itemHandRight", new ItemStack(ItemHandler.SCULPTOR_STAFF.get(), 1)));
        this.shadowRadius = 0.7f;

    }

    @Override
    public void preRender(PoseStack poseStack, EntitySculptor animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        model.getBone("gauntletRotator").get().setHidden(true);
        model.getBone("gauntletUnparented").get().setHidden(true);
    }

    @Override
    public void render(EntitySculptor entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        staffController = (int) model.getBone("staffController").get().getPosX();
    }
}
