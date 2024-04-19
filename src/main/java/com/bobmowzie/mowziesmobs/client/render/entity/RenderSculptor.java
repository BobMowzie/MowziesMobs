package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public final ResourceLocation staff_geo_location = new ResourceLocation(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    public final ResourceLocation staff_tex_location = new ResourceLocation(MowziesMobs.MODID, "textures/item/sculptor_staff.png");
    public int staffController = 0;

    public RenderSculptor(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSculptor());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addRenderLayer(new GeckoSunblockLayer<>(this, renderManager));
//        this.addRenderLayer(new GeckoItemlayer<>(this,"backItem", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
//        this.addRenderLayer(new GeckoItemlayer<>(this,"itemHandLeft", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
//        this.addRenderLayer(new GeckoItemlayer<>(this,"itemHandRight", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.shadowRadius = 0.7f;

    }

    @Override
    public void render(EntitySculptor entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        staffController = (int) model.getBone("staffController").get().getPosX();
    }
}
