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
        float frame = entity.frame + animationState.getPartialTick();

        MowzieGeoBone rightThigh = getMowzieBone("rightThigh");
        MowzieGeoBone leftThigh = getMowzieBone("leftThigh");

        float liftLegs = entity.legsUp.getAnimationProgressSinSqrt(animationState.getPartialTick());
        leftThigh.addRotX(1f * liftLegs);
        rightThigh.addRotX(1f * liftLegs);
        leftThigh.addRotZ(1.5f * liftLegs);
        rightThigh.addRotZ(-1.5f * liftLegs);
        leftThigh.addRotY(-0.5f * liftLegs);
        rightThigh.addRotY(0.5f * liftLegs);

        if (entity.isAlive() && entity.active) {
            MowzieGeoBone neck1 = getMowzieBone("neck");
            MowzieGeoBone neck2 = getMowzieBone("neck2");
            MowzieGeoBone head = getMowzieBone("head");
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

            MowzieGeoBone featherRaiseController = getMowzieBone("featherRaiseController");
            MowzieGeoBone rightFoot = getMowzieBone("rightFoot");
            MowzieGeoBone rightAnkle = getMowzieBone("rightAnkle");
            MowzieGeoBone rightCalf = getMowzieBone("rightCalf");
            MowzieGeoBone leftFoot = getMowzieBone("leftFoot");
            MowzieGeoBone leftAnkle = getMowzieBone("leftAnkle");
            MowzieGeoBone leftCalf = getMowzieBone("leftCalf");
            MowzieGeoBone rightHand = getMowzieBone("rightHand");
            MowzieGeoBone rightLowerArm = getMowzieBone("rightLowerArm");
            MowzieGeoBone rightArmJoint = getMowzieBone("rightArmJoint");
            MowzieGeoBone leftHand = getMowzieBone("leftHand");
            MowzieGeoBone leftLowerArm = getMowzieBone("leftLowerArm");
            MowzieGeoBone leftArmJoint = getMowzieBone("leftArmJoint");
            MowzieGeoBone chest = getMowzieBone("chest");
            MowzieGeoBone body = getMowzieBone("body");
            MowzieGeoBone headJoint = getMowzieBone("headJoint");
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
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone chest = getMowzieBone("chest");
        MowzieGeoBone tail = getMowzieBone("tail");
        stomach.setScale(stomach.getScaleX() + jiggleScale, stomach.getScaleY() + jiggleScale, stomach.getScaleZ() + jiggleScale);
        chest.addPosY(jiggleScale * 3);
        leftThigh.addPosX(-jiggleScale * 5);
        rightThigh.addPosX(jiggleScale * 5);
        tail.addPosZ(jiggleScale * 4);

        float featherShakeControl = getControllerValue("featherShakeController");
        float featherRaiseControl = getControllerValue("featherRaiseController");
        List<Triple<MowzieGeoBone, Direction.Axis, Boolean>> feathers = new ArrayList<>();
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront4"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack1"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack2"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack3"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack4"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack5"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft1"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft2"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft3"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft4"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft5"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight1"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight2"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight3"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight4"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight5"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersFront2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersLeft1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersLeft2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersLeft3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersRight1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersRight2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersRight3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersFront2"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight3"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight4"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight5"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft3"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft4"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft5"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestDraperyFront"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestDraperyRight"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("chestDraperyLeft"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("chestDraperyBack"), Direction.Axis.X, true));

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

        MowzieGeoBone mask = getMowzieBone("mask");
        MowzieGeoBone body = getMowzieBone("body");
        mask.setScale(1.0f / (float) body.getScale().x, 1.0f / (float) body.getScale().y, 1.0f / (float) body.getScale().z);

    }
}