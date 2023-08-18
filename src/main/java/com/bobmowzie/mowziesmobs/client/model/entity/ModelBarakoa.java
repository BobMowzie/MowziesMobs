package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
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
    public void codeAnimations(EntityBarakoa entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
        boolean isBarakoana = entity instanceof EntityBarakoana;
        boolean isElite = entity instanceof EntityBarakoaya || isBarakoana;
        getMowzieBone("crestRight").isHidden = !isElite;
        getMowzieBone("crestLeft").isHidden = !isElite;
        getMowzieBone("crest1").isHidden = !isElite;
        getMowzieBone("leftPinkyTalon").isHidden = !isBarakoana;
        getMowzieBone("leftPinkyClaw").isHidden = isBarakoana;
        getMowzieBone("rightPinkyTalon").isHidden = !isBarakoana;
        getMowzieBone("rightPinkyClaw").isHidden = isBarakoana;
        MowzieGeoBone root = getMowzieBone("root");
        if (isElite) {
            root.multiplyScale(0.9f, 0.9f, 0.9f);
        }
        else {
            root.multiplyScale(0.83f, 0.83f, 0.83f);
        }

        MowzieGeoBone mask = getMowzieBone("mask");
        MowzieGeoBone hips = getMowzieBone("hips");
        mask.setScale(1.0f / (float) hips.getScale().x, 1.0f / (float) hips.getScale().y, 1.0f / (float) hips.getScale().z);

        if (entity.isAlive()) {
            MowzieGeoBone head = getMowzieBone("head");
            MowzieGeoBone neck = getMowzieBone("neck");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            float headYaw = Mth.wrapDegrees(extraData.netHeadYaw);
            float headPitch = Mth.wrapDegrees(extraData.headPitch);
            head.addRotationX(headPitch * ((float) Math.PI / 180F) / 2f);
            head.addRotationY(headYaw * ((float) Math.PI / 180F) / 2f);
            neck.addRotationX(headPitch * ((float) Math.PI / 180F) / 2f);
            neck.addRotationY(headYaw * ((float) Math.PI / 180F) / 2f);
        }

        float animSpeed = 1.4f;
        float limbSwing = customPredicate.getLimbSwing();
        float limbSwingAmount = customPredicate.getLimbSwingAmount();

//        limbSwing = 0.5f * (entity.tickCount + customPredicate.getPartialTick());
//        limbSwingAmount = 1f;
//        float angle = 0.03f * (entity.tickCount + customPredicate.getPartialTick());
//        Vec3 moveVec = new Vec3(1.0, 0, 0);
//        moveVec = moveVec.yRot(angle);

        Vec3 moveVec = entity.getDeltaMovement().normalize().yRot((float) Math.toRadians(entity.yBodyRot + 90.0));
        float forward = (float) Math.max(0, new Vec3(1.0, 0, 0).dot(moveVec));
        float backward = (float) Math.max(0, new Vec3(-1.0, 0, 0).dot(moveVec));
        float left = (float) Math.max(0, new Vec3(0, 0, -1.0).dot(moveVec));
        float right = (float) Math.max(0, new Vec3(0, 0, 1.0).dot(moveVec));
        limbSwingAmount *= 2;
        limbSwingAmount = Math.min(0.7f, limbSwingAmount);
        float locomotionAnimController = getControllerValue("locomotionAnimController");
        float runAnim = getControllerValue("walkRunSwitchController");
        float walkAnim = 1.0f - runAnim;
        walkForwardAnim(forward * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);
        walkBackwardAnim(backward * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);
        walkLeftAnim(left * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);
        walkRightAnim(right * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);

        runAnim(locomotionAnimController * runAnim, limbSwing, limbSwingAmount, animSpeed);
    }

    private void runAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
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
        MowzieGeoBone leftArm = getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightArm = getMowzieBone("rightArm");
        MowzieGeoBone rightForeArm = getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.7f;
        speed *= 0.8;

        hips.addPositionY(blend * (float) (Math.cos(limbSwing * speed - 1.7) * 2f * globalHeight + 4 * globalHeight) * limbSwingAmount);
        hips.addRotationX(blend * -0.4f * limbSwingAmount * globalHeight);
        hips.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        chest.addRotationY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotationX(blend * -(float) (Math.cos(limbSwing * speed + 1.4 - 1.7) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotationX(blend * -(float) (Math.cos(limbSwing * speed - 1.5) * 0.25 * globalHeight - 0.2 * globalHeight) * limbSwingAmount);
        head.addRotationX(blend * (float) (Math.cos(limbSwing * speed + 0.175 - 1.7) * 0.25 * globalHeight + 0.2 * globalHeight) * limbSwingAmount);

        leftThigh.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        leftThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.2f) * limbSwingAmount);
        leftShin.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.50) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.50) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.5) * -1f * globalDegree - 1.1f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.8f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotationX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotationX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        rightThigh.addRotationY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.2f) * limbSwingAmount);
        rightShin.addRotationX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 2.50) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotationX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 2.50) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotationX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 3.5) * -1f * globalDegree - 1.1f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.8f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotationX(blend * (float) (-Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftArm.addRotationY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        leftArm.addRotationZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.05 * globalHeight) * limbSwingAmount);

        rightArm.addRotationY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        rightArm.addRotationZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.05 * globalHeight) * limbSwingAmount);
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
        MowzieGeoBone leftArm = getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightArm = getMowzieBone("rightArm");
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

        leftArm.addRotationY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotationZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotationY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotationZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
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
        MowzieGeoBone leftArm = getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightArm = getMowzieBone("rightArm");
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

        leftArm.addRotationY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotationZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotationY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotationZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
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
        MowzieGeoBone leftArm = getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightArm = getMowzieBone("rightArm");
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

        leftArm.addRotationY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotationZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotationY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotationZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
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
        MowzieGeoBone leftArm = getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = getMowzieBone("leftHand");
        MowzieGeoBone rightArm = getMowzieBone("rightArm");
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

        leftArm.addRotationY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotationZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotationY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotationZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotationX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
    }

}