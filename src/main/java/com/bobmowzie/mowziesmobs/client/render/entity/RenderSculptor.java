package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public RenderSculptor(EntityRendererManager renderManager) {
        super(renderManager, new ModelSculptor());
        this.shadowSize = 0.7f;
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySculptor entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }
}
