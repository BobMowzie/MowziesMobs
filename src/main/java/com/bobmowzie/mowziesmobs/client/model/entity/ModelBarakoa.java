package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class ModelBarakoa extends MowzieAnimatedGeoModel<EntityBarakoa> {
    public ModelBarakoa() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityBarakoa object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/barakoa.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBarakoa object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBarakoa object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/barakoa.animation.json");
    }

    @Override
    public void codeAnimations(EntityBarakoa entity, Integer uniqueID, AnimationEvent<?> customPredicate) {

    }
}