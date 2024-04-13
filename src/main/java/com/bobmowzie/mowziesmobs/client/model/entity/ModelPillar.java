package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;

import net.minecraft.resources.ResourceLocation;

public class ModelPillar extends MowzieGeoModel<EntityPillar> {
    public ModelPillar() {
        super();
    }

    @Override
    public ResourceLocation getModelResource(EntityPillar object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/geomancy_pillar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityPillar object) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(EntityPillar object) {
        return null;
    }
}