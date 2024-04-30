package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Triple;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.ArrayList;
import java.util.List;

public class ModelUmvuthi extends MowzieGeoModel<EntityUmvuthi> {
    public ModelUmvuthi() {
        super();
    }

    @Override
    public ResourceLocation getModelResource(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/umvuthi.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/umvuthi.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/umvuthi.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityUmvuthi entity, long instanceId, AnimationState<EntityUmvuthi> animationState) {
        AnimatableManager<GeoAnimatable> manager = entity.getAnimatableInstanceCache().getManagerForId(instanceId);

        float frame = entity.frame + animationState.getPartialTick();

        MowzieGeoBone rightThigh = getAndResetMowzieBone("rightThigh", manager);
        MowzieGeoBone leftThigh = getAndResetMowzieBone("leftThigh", manager);
        MowzieGeoBone neck1 = getAndResetMowzieBone("neck", manager);
        MowzieGeoBone neck2 = getAndResetMowzieBone("neck2", manager);
        MowzieGeoBone head = getAndResetMowzieBone("head", manager);
        MowzieGeoBone featherRaiseController = getAndResetMowzieBone("featherRaiseController", manager);
        MowzieGeoBone rightFoot = getAndResetMowzieBone("rightFoot", manager);
        MowzieGeoBone rightAnkle = getAndResetMowzieBone("rightAnkle", manager);
        MowzieGeoBone rightCalf = getAndResetMowzieBone("rightCalf", manager);
        MowzieGeoBone leftFoot = getAndResetMowzieBone("leftFoot", manager);
        MowzieGeoBone leftAnkle = getAndResetMowzieBone("leftAnkle", manager);
        MowzieGeoBone leftCalf = getAndResetMowzieBone("leftCalf", manager);
        MowzieGeoBone rightHand = getAndResetMowzieBone("rightHand", manager);
        MowzieGeoBone rightLowerArm = getAndResetMowzieBone("rightLowerArm", manager);
        MowzieGeoBone rightArmJoint = getAndResetMowzieBone("rightArmJoint", manager);
        MowzieGeoBone leftHand = getAndResetMowzieBone("leftHand", manager);
        MowzieGeoBone leftLowerArm = getAndResetMowzieBone("leftLowerArm", manager);
        MowzieGeoBone leftArmJoint = getAndResetMowzieBone("leftArmJoint", manager);
        MowzieGeoBone chest = getAndResetMowzieBone("chest", manager);
        MowzieGeoBone body = getAndResetMowzieBone("body", manager);
        MowzieGeoBone headJoint = getAndResetMowzieBone("headJoint", manager);
        MowzieGeoBone stomach = getAndResetMowzieBone("stomach", manager);
        MowzieGeoBone tail = getAndResetMowzieBone("tail", manager);
        MowzieGeoBone mask = getAndResetMowzieBone("mask", manager);

        float liftLegs = entity.legsUp.getAnimationProgressSinSqrt(animationState.getPartialTick());
        leftThigh.addRotX(1f * liftLegs);
        rightThigh.addRotX(1f * liftLegs);
        leftThigh.addRotZ(1.5f * liftLegs);
        rightThigh.addRotZ(-1.5f * liftLegs);
        leftThigh.addRotY(-0.5f * liftLegs);
        rightThigh.addRotY(0.5f * liftLegs);

        if (entity.isAlive() && entity.active) {
            MowzieGeoBone[] lookPieces = new MowzieGeoBone[] { neck1, neck2, head };

            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            float headYaw = Mth.wrapDegrees(entityData.netHeadYaw());
            float headPitch = Mth.wrapDegrees(entityData.headPitch());
            float maxYaw = 140f;
            headYaw = Mth.clamp(headYaw, -maxYaw, maxYaw);
            for (MowzieGeoBone bone : lookPieces) {
                bone.addRotX(headPitch * ((float) Math.PI / 180F) / (float) lookPieces.length);
                bone.addRotY(headYaw * ((float) Math.PI / 180F) / (float) lookPieces.length);
            }

            float idleSpeed = 0.08f;
            featherRaiseController.addPosX((float) (Math.sin((frame - 1.2) * idleSpeed) * 0.1));
            body.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.035));
            chest.addPosY((float) (-0.7 + Math.sin((frame - 0.0) * idleSpeed) * 0.014));
            chest.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * -0.017));
            neck1.addRotX((float) (Math.cos((frame - 0.3) * idleSpeed) * -0.052));
            neck2.addRotX((float) (Math.cos((frame - 0.5) * idleSpeed) * -0.052));
            headJoint.addRotX((float) (Math.cos((frame - 0.6) * idleSpeed) * 0.1));
            leftArmJoint.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.052));
            leftLowerArm.addRotY((float) (-Math.cos((frame - 0.0) * idleSpeed) * 0.035));
            leftHand.addRotY((float) (-Math.sin((frame - 0.0) * idleSpeed) * 0.087));
            rightArmJoint.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.052));
            rightLowerArm.addRotY((float) (Math.cos((frame - 0.0) * idleSpeed) * 0.035));
            rightHand.addRotY((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.087));
            leftThigh.addRotY((float) (Math.cos((frame - 0.0) * idleSpeed) * -0.052) * (1.0f - liftLegs));
            leftThigh.addRotZ((float) (Math.sin((frame - 0.0) * idleSpeed) * -0.052) * (1.0f - liftLegs));
            leftCalf.addRotX((float) (Math.sin((frame - 0.2) * idleSpeed) * 0.087) * (1.0f - liftLegs));
            leftAnkle.addRotX((float) (Math.cos((frame - 0.2) * idleSpeed) * -0.087) * (1.0f - liftLegs));
            leftFoot.addRotX((float) (Math.cos((frame - 0.4) * idleSpeed) * -0.14));
            rightThigh.addRotY((float) (Math.cos((frame - 0.0) * idleSpeed) * 0.052) * (1.0f - liftLegs));
            rightThigh.addRotZ((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.052) * (1.0f - liftLegs));
            rightCalf.addRotX((float) (Math.sin((frame - 0.2) * idleSpeed) * 0.087) * (1.0f - liftLegs));
            rightAnkle.addRotX((float) (Math.cos((frame - 0.2) * idleSpeed) * -0.087) * (1.0f - liftLegs));
            rightFoot.addRotX((float) (Math.cos((frame - 0.4) * idleSpeed) * -0.14));
            leftThigh.addRotY((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.035) * liftLegs);
            rightThigh.addRotY((float) (-Math.sin((frame - 0.0) * idleSpeed) * 0.035) * liftLegs);

            float armAimControl = getControllerValue("armAimController");
            leftArmJoint.addRotX(headPitch * ((float) Math.PI / 180F) * armAimControl);
            leftArmJoint.addRotY(headYaw * ((float) Math.PI / 180F) * armAimControl);
        }

        float bellyBounceControl = getControllerValue("bellyBounceController");
        float jiggleSpeed = 2.5f;
        float jiggleScale = (float) (bellyBounceControl * 0.1 * Math.cos(jiggleSpeed * frame));
        stomach.setScale(stomach.getScaleX() + jiggleScale, stomach.getScaleY() + jiggleScale, stomach.getScaleZ() + jiggleScale);
        chest.addPosY(jiggleScale * 3);
        leftThigh.addPosX(-jiggleScale * 5);
        rightThigh.addPosX(jiggleScale * 5);
        tail.addPosZ(jiggleScale * 4);

        float featherShakeControl = getControllerValue("featherShakeController");
        float featherRaiseControl = getControllerValue("featherRaiseController");
        List<Triple<MowzieGeoBone, Direction.Axis, Boolean>> feathers = new ArrayList<>();
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersFront1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersFront2", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersFront3", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersFront4", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersBack1", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersBack2", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersBack3", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersBack4", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersBack5", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersLeft1", manager), Direction.Axis.Z, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersLeft2", manager), Direction.Axis.Z, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersLeft3", manager), Direction.Axis.Z, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersLeft4", manager), Direction.Axis.Z, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersLeft5", manager), Direction.Axis.Z, true));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersRight1", manager), Direction.Axis.Z, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersRight2", manager), Direction.Axis.Z, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersRight3", manager), Direction.Axis.Z, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersRight4", manager), Direction.Axis.Z, false));
        feathers.add(Triple.of(getAndResetMowzieBone("neckFeathersRight5", manager), Direction.Axis.Z, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersFront1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersFront2", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersFront3", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersLeft1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersLeft2", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersLeft3", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersRight1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersRight2", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestFeathersRight3", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersFront1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersFront2", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersFront3", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersRight1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersRight2", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersRight3", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersRight4", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersRight5", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersLeft1", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersLeft2", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersLeft3", manager), Direction.Axis.X, true));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersLeft4", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("bellyFeathersLeft5", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestDraperyFront", manager), Direction.Axis.X, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestDraperyRight", manager), Direction.Axis.Z, false));
        feathers.add(Triple.of(getAndResetMowzieBone("chestDraperyLeft", manager), Direction.Axis.Z, true));
        feathers.add(Triple.of(getAndResetMowzieBone("chestDraperyBack", manager), Direction.Axis.X, true));

        for (Triple<MowzieGeoBone, Direction.Axis, Boolean> feather : feathers) {
            MowzieGeoBone bone = feather.getLeft();
            float oscillation = (float) (featherShakeControl * 0.13 * Math.cos(1.4 * frame + bone.getPivotY() * -0.15 + bone.getPivotZ() * -0.1)) + featherRaiseControl;
            if (feather.getRight()) oscillation *= -1;
            Direction.Axis axis = feather.getMiddle();
            if (axis == Direction.Axis.X) {
                bone.addRotX(oscillation);
            }
            else if (axis == Direction.Axis.Y) {
                bone.addRotY(oscillation);
            }
            else {
                bone.addRotZ(oscillation);
            }
        }
        mask.setScale(1.0f / (float) body.getScale().x, 1.0f / (float) body.getScale().y, 1.0f / (float) body.getScale().z);

    }
}