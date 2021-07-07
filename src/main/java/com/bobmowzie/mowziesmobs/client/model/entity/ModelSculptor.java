package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSculptor extends AnimatedGeoModel<EntitySculptor> {
    @Override
    public ResourceLocation getModelLocation(EntitySculptor object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/sculptor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySculptor object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/sculptor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySculptor object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/sculptor/idle.animation.json");
    }
}