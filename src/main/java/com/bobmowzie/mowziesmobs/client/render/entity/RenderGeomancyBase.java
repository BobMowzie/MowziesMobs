package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderGeomancyBase<T extends EntityGeomancyBase & GeoEntity> extends GeoEntityRenderer<T> {
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/blocks/stone.png");
    private T entity;

    protected RenderGeomancyBase(EntityRendererProvider.Context context, MowzieGeoModel<T> modelProvider) {
        super(context, modelProvider);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGeomancyBase entity) {
        return TEXTURE_STONE;
    }
}
