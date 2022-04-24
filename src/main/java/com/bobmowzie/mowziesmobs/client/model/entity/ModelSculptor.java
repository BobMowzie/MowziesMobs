package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Matrix4f;
import net.minecraft.world.phys.Quaternion;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class ModelSculptor extends MowzieAnimatedGeoModel<EntitySculptor> {
    public ModelSculptor() {
        super();
    }

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
        return new ResourceLocation(MowziesMobs.MODID, "animations/sculptor.animation.json");
    }

    @Override
    public void codeAnimations(EntitySculptor entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
        IBone head = this.getBone("head");
        IBone handClosedL = this.getBone("handClosedL");
        IBone handClosedR = this.getBone("handClosedR");
        IBone handOpenL = this.getBone("handOpenL");
        IBone handOpenR = this.getBone("handOpenR");
        IBone backCloth = this.getBone("clothBack");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F));

        handClosedL.setHidden(entity.handLOpen);
        handClosedR.setHidden(entity.handROpen);
        handOpenL.setHidden(!entity.handLOpen);
        handOpenR.setHidden(!entity.handROpen);

        backCloth.setHidden(false);

        beadsCorrections(entity);
        skirtCorrections(entity);
    }

    private void beadsCorrections(EntitySculptor entity) {
        Map<MowzieGeoBone, Vec3> beadsDirections = new HashMap<>();
        beadsDirections.put(this.getMowzieBone("bead1"), new Vec3(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead2"), new Vec3(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead3"), new Vec3(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead4"), new Vec3(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead5"), new Vec3(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead6"), new Vec3(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead7"), new Vec3(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead8"), new Vec3(0, -0.25, 0.75));
        beadsDirections.put(this.getMowzieBone("bead9"), new Vec3(0, -0.25, 0.75));

        IBone headJoint = this.getBone("head_joint");
        Vec3 headPos = new Vec3(headJoint.getPivotX(), headJoint.getPivotY(), headJoint.getPivotZ());
        IBone head = this.getBone("head");
        Vec3 headDir = new Vec3(0, 0, -1).normalize();
        headDir = headDir.rotateYaw(head.getRotationY()).rotatePitch(head.getRotationX());
        for (Map.Entry<MowzieGeoBone, Vec3> beadDir : beadsDirections.entrySet()) {
            MowzieGeoBone bead = beadDir.getKey();
            Vec3 beadPos = new Vec3(bead.getPivotX(), bead.getPivotY(), bead.getPivotZ());
            Vec3 dir = beadPos.subtract(headPos).normalize().mul(1, -1, 1);
            double dot = dir.dotProduct(headDir);
            dot = Math.pow(Math.max(dot, 0), 3.5);
            Vec3 moveDir = beadDir.getValue().normalize();
            bead.addPosition(moveDir.scale(dot * 3));
        }
    }

    private void skirtCorrections(EntitySculptor entity) {
        MowzieGeoBone headJoint = this.getMowzieBone("head_joint");
        MowzieGeoBone thighR = this.getMowzieBone("thighR");
        MowzieGeoBone thighJointR = this.getMowzieBone("thighJointR");
        MowzieGeoBone thighJointL = this.getMowzieBone("thighJointL");
        MowzieGeoBone calfR = this.getMowzieBone("calfR");
        MowzieGeoBone thighL = this.getMowzieBone("thighL");
        MowzieGeoBone calfL = this.getMowzieBone("calfL");
        MowzieGeoBone footR = this.getMowzieBone("footR");

        MowzieGeoBone skirtBack = this.getMowzieBone("skirtBack");
        MowzieGeoBone skirtFront = this.getMowzieBone("skirtFront");
        MowzieGeoBone skirtL = this.getMowzieBone("skirtL");
        MowzieGeoBone skirtR = this.getMowzieBone("skirtR");
        MowzieGeoBone skirtJointL = this.getMowzieBone("skirtJointL");
        MowzieGeoBone skirtJointR = this.getMowzieBone("skirtJointR");
        MowzieGeoBone skirtJoint2L = this.getMowzieBone("skirtJoint2L");
        MowzieGeoBone skirtJoint2R = this.getMowzieBone("skirtJoint2R");
        MowzieGeoBone skirtEndR = this.getMowzieBone("skirtEndR");
        MowzieGeoBone skirtEndL = this.getMowzieBone("skirtEndL");

        MowzieGeoBone skirtLocFrontR = this.getMowzieBone("skirtFrontLocR");
        MowzieGeoBone skirtLocFrontL = this.getMowzieBone("skirtFrontLocL");
        MowzieGeoBone skirtLocBackR = this.getMowzieBone("skirtBackLocR");
        MowzieGeoBone skirtLocBackL = this.getMowzieBone("skirtBackLocL");

        headJoint.setHidden(false);

        Vec3 thighToKneeR = calfR.getModelPosition().subtract(thighR.getModelPosition()).normalize();
        Vec3 thighToKneeL = calfL.getModelPosition().subtract(thighL.getModelPosition()).normalize();

        skirtEndL.addPosition(-0.2f, -1.5f, 0);
        skirtEndR.addPosition( 0.2f, -1.5f, 0);
        Vec3 thighToSkirtEndR = skirtEndR.getModelPosition().subtract(thighR.getModelPosition()).normalize();
        Vec3 thighToSkirtEndL = skirtEndL.getModelPosition().subtract(thighL.getModelPosition()).normalize();
        float rightDot = (float) thighToKneeR.dotProduct(new Vec3(0, -1, 0));
        rightDot = (float) Math.pow(Math.max(rightDot, 0), 3);
        float leftDot = (float) thighToKneeL.dotProduct(new Vec3(0, -1, 0));
        leftDot = (float) Math.pow(Math.max(leftDot, 0), 3);
        skirtJointR.addPosition(Math.max(-0.9f * rightDot, -0.7f), 0, Math.max(-0.7f * rightDot, -0.5f));
        skirtJointL.addPosition(-Math.max(-0.9f * leftDot, -0.7f), 0, Math.max(-0.7f * leftDot, -0.5f));

        MowzieGeoBone test = this.getMowzieBone("test");
        MowzieGeoBone test2 = this.getMowzieBone("test2");
        MowzieGeoBone test3 = this.getMowzieBone("test3");

        Quaternion rightRot = RigUtils.betweenVectors(thighToSkirtEndR, thighToKneeR);
        Quaternion leftRot = RigUtils.betweenVectors(thighToSkirtEndL, thighToKneeL);
        Matrix4f rightMat = new Matrix4f(rightRot);
        Matrix4f leftMat = new Matrix4f(leftRot);
        skirtJoint2R.setModelRotationMat(rightMat);
        skirtJoint2L.setModelRotationMat(leftMat);

        Vec3 average = thighToKneeL.add(thighToKneeR).scale(2).mul(0, 1, 1).normalize();
        float angleAv = (float) Mth.atan2(average.y(), average.z());
        skirtBack.setRotationX(skirtBack.getRotationX() - angleAv + 3.48f);
        skirtFront.setRotationX(skirtFront.getRotationX() - Math.min(angleAv, -2) + 3.48f);
        Vec3 skirtFrontDiff = skirtLocFrontL.getModelPosition().subtract(skirtLocFrontR.getModelPosition());
        skirtFront.setScaleX(Math.max((float) (0.3f + 0.15f * skirtFrontDiff.length()), 0.4f));
        Vec3 skirtBackDiff = skirtLocBackL.getModelPosition().subtract(skirtLocBackR.getModelPosition());
        skirtBack.setScaleX((float) (0.15f + 0.1f * skirtBackDiff.length()));
        float angleF = (float) Mth.atan2(skirtFrontDiff.normalize().z(), skirtFrontDiff.normalize().x());
        if (angleF < 0.001 || angleF > 3.141) angleF = 0;
        skirtFront.setRotationY(angleF * 0.6f);
        skirtFront.addPosition(0.5f * (float) skirtLocFrontR.getModelPosition().add(skirtFrontDiff.scale(0.5)).x(), 0, 0);
        float angleB = (float) Mth.atan2(skirtBackDiff.normalize().z(), skirtBackDiff.normalize().x());
        skirtBack.setRotationY(angleB * 0.5f);
        skirtBack.addPosition(0.5f * (float) skirtLocBackR.getModelPosition().add(skirtBackDiff.scale(0.5)).x(), 0, 0);
        float bothDots = (float) Math.pow(rightDot * leftDot, 1);
        float f = Math.min(1, bothDots * 2);
        skirtR.addRotation(0, Mth.clamp(angleF, -0.5f, 0.5f) * (1 - f) - bothDots * 0.4f, 0);
        skirtL.addRotation(0, Mth.clamp(angleF, -0.5f, 0.5f) * (1 - f) + bothDots * 0.4f, 0);

        MowzieGeoBone frontCloth = getMowzieBone("clothFront");
        MowzieGeoBone frontCloth2 = getMowzieBone("clothFront2");

        frontCloth.setRotation(skirtFront.getRotation());
        Matrix4f mat = frontCloth.getModelRotationMat().copy();
        mat.invert();
        frontCloth2.setModelRotationMat(mat);

    }
}