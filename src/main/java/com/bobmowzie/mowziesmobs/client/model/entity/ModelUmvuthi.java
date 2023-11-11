package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelUmvuthi extends MowzieAnimatedGeoModel<EntityUmvuthi> {
    public ModelUmvuthi() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/umvuthi.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/umvuthi_2.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/umvuthi.animation.json");
    }

    @Override
    public void codeAnimations(EntityUmvuthi entity, Integer uniqueID, AnimationEvent<?> customPredicate) {

        if (entity.isAlive()) {
            MowzieGeoBone neck1 = getMowzieBone("neck");
            MowzieGeoBone neck2 = getMowzieBone("neck2");
            MowzieGeoBone headJoint = getMowzieBone("headJoint");
            MowzieGeoBone head = getMowzieBone("head");
            MowzieGeoBone[] lookPieces = new MowzieGeoBone[] { neck1, neck2, headJoint, head };
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            float headYaw = Mth.wrapDegrees(extraData.netHeadYaw);
            float headPitch = Mth.wrapDegrees(extraData.headPitch);
            for (MowzieGeoBone bone : lookPieces) {
                bone.addRotationX(headPitch * ((float) Math.PI / 180F) / (float) lookPieces.length);
                bone.addRotationY(headYaw * ((float) Math.PI / 180F) / (float) lookPieces.length);
            }
        }

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