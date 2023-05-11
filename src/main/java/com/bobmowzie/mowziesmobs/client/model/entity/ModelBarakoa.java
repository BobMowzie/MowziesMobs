package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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
    public void setLivingAnimations(EntityBarakoa animatable, Integer instanceId, AnimationEvent animationEvent) {
        super.setLivingAnimations(animatable, instanceId, animationEvent);
    }

    @Override
    public void codeAnimations(EntityBarakoa entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
//        IBone head = this.getBone("head");
//        IBone neck = this.getBone("neck");
//
//        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
//        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) / 2f);
//        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) / 2f);
//        neck.setRotationX(neck.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) / 2f);
//        neck.setRotationY(neck.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) / 2f);

        MowzieGeoBone hips = getMowzieBone("hips");
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone neck = getMowzieBone("neck");
        MowzieGeoBone head = getMowzieBone("head");
        MowzieGeoBone leftThigh = getMowzieBone("leftThigh");
        MowzieGeoBone leftShin = getMowzieBone("leftShin");
        MowzieGeoBone leftAnkle = getMowzieBone("leftAnkle");
        MowzieGeoBone leftFoot = getMowzieBone("leftFoot");
        MowzieGeoBone leftToesBack = getMowzieBone("leftToesBack");
        MowzieGeoBone rightThigh = getMowzieBone("rightThigh");
        MowzieGeoBone rightShin = getMowzieBone("rightShin");
        MowzieGeoBone rightAnkle = getMowzieBone("rightAnkle");
        MowzieGeoBone rightFoot = getMowzieBone("rightFoot");
        MowzieGeoBone rightToesBack = getMowzieBone("rightToesBack");

        float limbSwing = customPredicate.getLimbSwing();//0.5f * (entity.tickCount + customPredicate.getPartialTick());
        float limbSwingAmount = customPredicate.getLimbSwingAmount();//1f;
        float globalSpeed = 1f;
        float globalHeight = 1f;
        float globalDegree = 1f;

        hips.addPositionY((float) (Math.cos(limbSwing * globalSpeed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotationX(-0.18f * limbSwingAmount * globalHeight);
        stomach.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotationX((float) (Math.cos(limbSwing * globalSpeed + 0.175) * 0.175 * globalHeight + 0.18 * globalHeight) * limbSwingAmount);

        leftThigh.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        leftShin.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 0.5 + 2.5) * -1.3f * globalDegree - 0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);
        
        rightThigh.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        rightShin.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed * 0.5 + 2.5) * -1.3f * globalDegree + 0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(-(float) (Math.cos(limbSwing * globalSpeed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX((float) (Math.cos(limbSwing * globalSpeed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

    }
}