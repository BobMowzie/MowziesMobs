package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public RenderSculptor(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSculptor());
        this.shadowRadius = 0.7f;
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySculptor entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }
}
