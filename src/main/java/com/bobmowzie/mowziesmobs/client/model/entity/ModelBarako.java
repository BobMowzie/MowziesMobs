package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class ModelBarako extends MowzieAnimatedGeoModel<EntityUmvuthi> {
    public ModelBarako() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/barako.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/barako.animation.json");
    }

    @Override
    public void codeAnimations(EntityUmvuthi entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
        MowzieGeoBone rightThigh = getMowzieBone("rightThigh");
        MowzieGeoBone leftThigh = getMowzieBone("leftThigh");

        float liftLegs = entity.legsUp.getAnimationProgressSinSqrt(customPredicate.getPartialTick());
        leftThigh.addRotationX(1f * liftLegs);
        rightThigh.addRotationX(1f * liftLegs);
        leftThigh.addRotationZ(-1.5f * liftLegs);
        rightThigh.addRotationZ(1.5f * liftLegs);
        leftThigh.addRotationY(0.5f * liftLegs);
        rightThigh.addRotationY(-0.5f * liftLegs);
    }
}