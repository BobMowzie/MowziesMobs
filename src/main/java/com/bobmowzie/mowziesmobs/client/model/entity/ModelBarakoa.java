package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
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
        MowzieGeoBone head = getMowzieBone("head");
        MowzieGeoBone neck = getMowzieBone("neck");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.addRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 2f);
        head.addRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 2f);
        neck.addRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 2f);
        neck.addRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 2f);

        float limbSwing = customPredicate.getLimbSwing();
        float limbSwingAmount = customPredicate.getLimbSwingAmount();
//        limbSwing = 0.5f * (entity.tickCount + customPredicate.getPartialTick());
//        limbSwingAmount = 0.7f;
        float animSpeed = 1.4f;

//        float angle = 0.03f * (entity.tickCount + customPredicate.getPartialTick());
//        Vec3 moveVec = new Vec3(1.0, 0, 0);
//        moveVec = moveVec.yRot(angle);
        Vec3 moveVec = entity.getDeltaMovement().normalize().yRot((float) Math.toRadians(entity.getYRot() + 90.0));
        float forward = (float) Math.max(0, new Vec3(1.0, 0, 0).dot(moveVec));
        float backward = (float) Math.max(0, new Vec3(-1.0, 0, 0).dot(moveVec));
        float left = (float) Math.max(0, new Vec3(0, 0, 1.0).dot(moveVec));
        float right = (float) Math.max(0, new Vec3(0, 0, -1.0).dot(moveVec));
        walkForwardAnim(forward, limbSwing, limbSwingAmount, animSpeed);
        walkBackwardAnim(backward, limbSwing, limbSwingAmount, animSpeed);
        walkLeftAnim(left, limbSwing, limbSwingAmount, animSpeed);
        walkRightAnim(right, limbSwing, limbSwingAmount, animSpeed);
    }

    private void walkForwardAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = getMowzieBone("head");
        MowzieGeoBone neck = getMowzieBone("neck");
        MowzieGeoBone hips = getMowzieBone("hips");
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone chest = getMowzieBone("chest");
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
        MowzieGeoBone leftShoulder = getMowzieBone("leftShoulder");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightShoulder = getMowzieBone("rightShoulder");
        MowzieGeoBone rightForeArm = getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPositionY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotationX(blend * -0.18f * limbSwingAmount * globalHeight);
        hips.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        chest.addRotationY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotationX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotationX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight + 0.18 * globalHeight) * limbSwingAmount);

        leftThigh.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        leftThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.15f) * limbSwingAmount);
        leftShin.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree - 0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        rightThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.15f) * limbSwingAmount);
        rightShin.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree + 0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftShoulder.addRotationY(blend * -0.85f * limbSwingAmount * globalHeight);
        leftShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        leftHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);

        rightShoulder.addRotationY(blend * 0.85f * limbSwingAmount * globalHeight);
        rightShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        rightHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);
    }

    private void walkBackwardAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = getMowzieBone("head");
        MowzieGeoBone neck = getMowzieBone("neck");
        MowzieGeoBone hips = getMowzieBone("hips");
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone chest = getMowzieBone("chest");
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
        MowzieGeoBone leftShoulder = getMowzieBone("leftShoulder");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightShoulder = getMowzieBone("rightShoulder");
        MowzieGeoBone rightForeArm = getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPositionY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotationX(blend * 0.18f * limbSwingAmount * globalHeight);
        hips.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * 0.1f * globalHeight) * limbSwingAmount);
        chest.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotationX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * 0.1f * globalHeight) * limbSwingAmount);
        neck.addRotationX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight - 0.18 * globalHeight) * limbSwingAmount);

        leftThigh.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.55f * globalDegree - 0.3 * globalDegree) * limbSwingAmount);
        leftThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.1f * globalDegree - 0.15f) * limbSwingAmount);
        leftShin.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 2.5) * -1.3f * globalDegree - 0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.55f * globalDegree + 0.3 * globalDegree) * limbSwingAmount);
        rightThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.1f * globalDegree + 0.15f) * limbSwingAmount);
        rightShin.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 2.5) * -1.3f * globalDegree + 0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftShoulder.addRotationY(blend * -1.0f * limbSwingAmount * globalHeight);
        leftShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        leftHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);

        rightShoulder.addRotationY(blend * 1.0f * limbSwingAmount * globalHeight);
        rightShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        rightHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);
    }

    private void walkLeftAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = getMowzieBone("head");
        MowzieGeoBone neck = getMowzieBone("neck");
        MowzieGeoBone hips = getMowzieBone("hips");
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone chest = getMowzieBone("chest");
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
        MowzieGeoBone leftShoulder = getMowzieBone("leftShoulder");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightShoulder = getMowzieBone("rightShoulder");
        MowzieGeoBone rightForeArm = getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPositionY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotationX(blend * -0.1f * limbSwingAmount * globalHeight);
        hips.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        hips.addRotationZ(blend * 0.08f * limbSwingAmount * globalHeight);
        chest.addRotationY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotationX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        stomach.addRotationZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        stomach.addRotationZ(blend * -(float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        neck.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotationX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight + 0.1 * globalHeight) * limbSwingAmount);
        head.addRotationZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotationZ(blend * (float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotationZ(blend * -0.03f * limbSwingAmount * globalHeight);

        leftThigh.addRotationX(blend * -0.05f * limbSwingAmount * globalHeight);
        leftThigh.addRotationZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree + 0.05 * globalDegree) * limbSwingAmount);
        leftThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.15) * limbSwingAmount);
        leftShin.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree - 0.6f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotationX(blend * 0.05f * limbSwingAmount * globalHeight);
        rightThigh.addRotationZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree - 0.05 * globalDegree) * limbSwingAmount);
        rightThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.15) * limbSwingAmount);
        rightShin.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree + 0.6f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationY(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftShin.addRotationX(blend * -(float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6), 12) * 0.6f * globalHeight) * limbSwingAmount);
        leftAnkle.addRotationX(blend * (float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6), 12) * 0.6f * globalHeight) * limbSwingAmount);

        leftShoulder.addRotationY(blend * -0.85f * limbSwingAmount * globalHeight);
        leftShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        leftHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);

        rightShoulder.addRotationY(blend * 0.85f * limbSwingAmount * globalHeight);
        rightShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        rightHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);
    }

    private void walkRightAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = getMowzieBone("head");
        MowzieGeoBone neck = getMowzieBone("neck");
        MowzieGeoBone hips = getMowzieBone("hips");
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone chest = getMowzieBone("chest");
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
        MowzieGeoBone leftShoulder = getMowzieBone("leftShoulder");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightShoulder = getMowzieBone("rightShoulder");
        MowzieGeoBone rightForeArm = getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPositionY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotationX(blend * -0.1f * limbSwingAmount * globalHeight);
        hips.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        hips.addRotationZ(blend * -0.08f * limbSwingAmount * globalHeight);
        chest.addRotationY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotationX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        stomach.addRotationZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        stomach.addRotationZ(blend * (float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        neck.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotationX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight + 0.1 * globalHeight) * limbSwingAmount);
        head.addRotationZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotationZ(blend * -(float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotationZ(blend * 0.03f * limbSwingAmount * globalHeight);

        leftThigh.addRotationX(blend * 0.05f * limbSwingAmount * globalHeight);
        leftThigh.addRotationZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree + 0.05 * globalDegree) * limbSwingAmount);
        leftThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.15) * limbSwingAmount);
        leftShin.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree - 0.6f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationY(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationY(blend * -(float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotationX(blend * -0.05f * limbSwingAmount * globalHeight);
        rightThigh.addRotationZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree - 0.05 * globalDegree) * limbSwingAmount);
        rightThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.15) * limbSwingAmount);
        rightShin.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree + 0.6f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationY(blend * -(float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightShin.addRotationX(blend * -(float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6 + Math.PI/2.0), 12) * 0.6f * globalHeight) * limbSwingAmount);
        rightAnkle.addRotationX(blend * (float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6 + Math.PI/2.0), 12) * 0.6f * globalHeight) * limbSwingAmount);

        leftShoulder.addRotationY(blend * -0.85f * limbSwingAmount * globalHeight);
        leftShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        leftHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);

        rightShoulder.addRotationY(blend * 0.85f * limbSwingAmount * globalHeight);
        rightShoulder.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight + -0.75f * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight + 0.2f * globalHeight) * limbSwingAmount);
        rightHand.addRotationX(blend * 0.5f * limbSwingAmount * globalHeight);
    }

}