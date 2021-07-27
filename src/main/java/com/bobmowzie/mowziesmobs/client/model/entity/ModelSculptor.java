package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils.*;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.BoneInfo;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class ModelSculptor extends MowzieAnimatedGeoModel<EntitySculptor> {
    public ModelSculptor() {
        super();
        trackBone("body");
        trackBone("chest");
        trackBone("thighR");
        trackBone("calfR");
        trackBone("thighL");
        trackBone("calfL");
        trackBone("footR");
        trackBone("skirtL");
        trackBone("skirtR");
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
    public void setLivingAnimations(EntitySculptor entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone handClosedL = this.getBone("handClosedL");
        IBone handClosedR = this.getBone("handClosedR");
        IBone handOpenL = this.getBone("handOpenL");
        IBone handOpenR = this.getBone("handOpenR");
        IBone frontCloth = this.getBone("clothFront");
        IBone frontCloth2 = this.getBone("clothFront2");
        IBone backCloth = this.getBone("clothBack");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F));

        handClosedL.setHidden(entity.handLOpen);
        handClosedR.setHidden(entity.handROpen);
        handOpenL.setHidden(!entity.handLOpen);
        handOpenR.setHidden(!entity.handROpen);

//        frontCloth.setHidden(true);
//        frontCloth2.setHidden(true);
//        backCloth.setHidden(true);

        beadsCorrections(entity);
        skirtCorrections(entity);
    }

    private void beadsCorrections(EntitySculptor entity) {
        Map<MowzieGeoBone, Vector3d> beadsDirections = new HashMap<>();
        beadsDirections.put(this.getMowzieBone("bead1"), new Vector3d(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead2"), new Vector3d(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead3"), new Vector3d(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead4"), new Vector3d(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead5"), new Vector3d(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead6"), new Vector3d(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead7"), new Vector3d(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead8"), new Vector3d(0, -0.25, 0.75));
        beadsDirections.put(this.getMowzieBone("bead9"), new Vector3d(0, -0.25, 0.75));

        IBone headJoint = this.getBone("head_joint");
        Vector3d headPos = new Vector3d(headJoint.getPivotX(), headJoint.getPivotY(), headJoint.getPivotZ());
        IBone head = this.getBone("head");
        Vector3d headDir = new Vector3d(0, 0, -1).normalize();
        headDir = headDir.rotateYaw(head.getRotationY()).rotatePitch(head.getRotationX());
        for (Map.Entry<MowzieGeoBone, Vector3d> beadDir : beadsDirections.entrySet()) {
            MowzieGeoBone bead = beadDir.getKey();
            Vector3d beadPos = new Vector3d(bead.getPivotX(), bead.getPivotY(), bead.getPivotZ());
            Vector3d dir = beadPos.subtract(headPos).normalize().mul(1, -1, 1);
            double dot = dir.dotProduct(headDir);
            dot = Math.pow(Math.max(dot, 0), 3.5);
            Vector3d moveDir = beadDir.getValue().normalize();
            bead.addPosition(moveDir.scale(dot * 3));
        }
//        head.setHidden(false);
    }

    private void skirtCorrections(EntitySculptor entity) {
        MowzieGeoBone headJoint = this.getMowzieBone("head_joint");
        MowzieGeoBone skirtBack = this.getMowzieBone("skirtBack");
        MowzieGeoBone skirtFront = this.getMowzieBone("skirtFront");
        MowzieGeoBone skirtL = this.getMowzieBone("skirtL");
        MowzieGeoBone skirtR = this.getMowzieBone("skirtR");
        MowzieGeoBone skirtLRot = this.getMowzieBone("skirtLRot");
        MowzieGeoBone skirtRRot = this.getMowzieBone("skirtRRot");
        MowzieGeoBone thighR = this.getMowzieBone("thighR");
        MowzieGeoBone thighJointR = this.getMowzieBone("thighJointR");
        MowzieGeoBone calfR = this.getMowzieBone("calfR");
        MowzieGeoBone thighL = this.getMowzieBone("thighL");
        MowzieGeoBone calfL = this.getMowzieBone("calfL");
        MowzieGeoBone footR = this.getMowzieBone("footR");
//        headJoint.setHidden(true);

//        thighJointR.setRotationX(0);
//        thighJointR.setRotationY((float) (Math.PI / 2.0));
//        thighJointR.setRotationZ(0);
//        thighR.setRotationX(0);
//        thighR.setRotationY(-0.9f);
//        thighR.setRotationZ(-0.8f);

        Vector3d thighToKneeR = calfR.getModelPosition().subtract(thighR.getModelPosition()).normalize();
        Vector3d thighToKneeL = calfL.getModelPosition().subtract(thighL.getModelPosition()).normalize();
        Vector3d average = thighToKneeL.add(thighToKneeR).scale(2).mul(0, 1, 1).normalize();
        float angleAv = (float) MathHelper.atan2(average.getY(), average.getZ());
        skirtBack.setRotationX(skirtBack.getRotationX() - angleAv + 3.48f);
        skirtFront.setRotationX(skirtFront.getRotationX() - Math.min(angleAv, -2) + 3.48f);

        Vector3d neutralDir = new Vector3d(-0.461, -0.297, -0.836);
        Vector3d outwardDir = new Vector3d(-0.997, 0.014, -0.079);
        Vector3d forwardDir = new Vector3d(0.081, 0.173, -0.982);
        Vector3d downwardDir = new Vector3d(0.081, -0.982, -0.173);
        Vector3d forwardDownDir = new Vector3d(0.08054550975390348, -0.318712495954773, -0.9444230175545759);
        Vector3d diagonalUpDir = new Vector3d(-0.49633081828428827, 0.3175958029975034, -0.8079533990032902);
        Vector3d diagonalOutDir = new Vector3d(-0.7307140013921798, -0.3996947712758693, -0.5534447538006492);
        BlendShape3D bshapeSkirtRot = new BlendShape3D(new BlendShape3DEntry[]{
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 0,
                        0, 0, 0,
                        1, 1, 1
                ), neutralDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 1.1,
                        0, -0.7, 0.5,
                        1, 1, 1
                ), outwardDir, 0.4f),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 0,
                        0, 0.9, 0,
                        1, 1, 1
                ), forwardDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0.8, 0, 1.1,
                        0, 0, 0,
                        1, 1, 1
                ), downwardDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, -1,
                        0, 0.9, 0,
                        1, 1, 1
                ), forwardDownDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 0,
                        0.3, 0.2, 0.3,
                        1, 1, 1
                ), diagonalUpDir, 0.4f),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 1,
                        0.1, -0.4, 0.3,
                        1, 1, 1
                ), diagonalOutDir, 0.4f)
        });
        bshapeSkirtRot.evaluate(skirtRRot, thighToKneeR);
        bshapeSkirtRot.evaluate(skirtLRot, thighToKneeL, true);

        BlendShape3D bshapeSkirt = new BlendShape3D(new BlendShape3DEntry[]{
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 0,
                        0, 0, 0,
                        1, 1, 1
                ), neutralDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0.5, 0,
                        -0.1, 0.2, 0.3,
                        1, 1, 1
                ), outwardDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0.8, -0.6,
                        0, 0, 0.9,
                        1, 1, 0.8
                ), forwardDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 0,
                        -0.65, 0, -0.6,
                        1, 1, 1
                ), downwardDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0.1, -0.6,
                        0, 0.4, 0.3,
                        1, 1, 0.8
                ), forwardDownDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0.8, 0,
                        0, 0, 0.6,
                        1, 1, 1
                ), diagonalUpDir, 1),
                new BlendShape3DEntry(new BoneTransform(
                        0, 0, 0,
                        -0.3, 0, -0.3,
                        1, 1, 1
                ), diagonalOutDir, 1)
        });
        bshapeSkirt.evaluate(skirtR, thighToKneeR);
        bshapeSkirt.evaluate(skirtL, thighToKneeL, true);


//        System.out.println(thighToKneeR.normalize());

//        MowzieGeoBone test = this.getMowzieBone("test");
//        test.setModelPosition(thighR.getModelPosition());
//        MowzieGeoBone test2 = this.getMowzieBone("test2");
//        test2.setModelPosition(calfR.getModelPosition());
//        MowzieGeoBone test3 = this.getMowzieBone("test3");
//        test3.setModelPosition(footR.getModelPosition());
    }
}