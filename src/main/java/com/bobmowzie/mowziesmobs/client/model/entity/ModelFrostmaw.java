package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.LegArticulator;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class ModelFrostmaw<T extends EntityFrostmaw> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer root;
    public AdvancedModelRenderer waist;
    public AdvancedModelRenderer chest;
    public AdvancedModelRenderer legLeftJoint;
    public AdvancedModelRenderer legRightJoint;
    public AdvancedModelRenderer headJoint;
    public AdvancedModelRenderer armLeftJoint;
    public AdvancedModelRenderer armRightJoint;
    public AdvancedModelRenderer headRotator;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer jawJoint;
    public AdvancedModelRenderer teethUpper;
    public AdvancedModelRenderer headBack;
    public AdvancedModelRenderer eyeLidRight;
    public AdvancedModelRenderer eyeLidLeft;
    public AdvancedModelRenderer jawRotator;
    public AdvancedModelRenderer jaw;
    public AdvancedModelRenderer tuskLeftJoint;
    public AdvancedModelRenderer tuskRightJoint;
    public AdvancedModelRenderer jawSpikes1;
    public AdvancedModelRenderer jawSpikes2;
    public AdvancedModelRenderer teethLower;
    public AdvancedModelRenderer tuskLeft1;
    public AdvancedModelRenderer tuskLeft2;
    public AdvancedModelRenderer tuskRight1;
    public AdvancedModelRenderer tuskRight2;
    public AdvancedModelRenderer armLeft1;
    public AdvancedModelRenderer armLeftJoint2;
    public AdvancedModelRenderer armLeft2;
    public AdvancedModelRenderer leftHandJoint;
    public AdvancedModelRenderer armLeft2Fur;
    public AdvancedModelRenderer leftHand;
    public AdvancedModelRenderer leftFingersJoint;
    public AdvancedModelRenderer leftThumb;
    public AdvancedModelRenderer leftFingers;
    public AdvancedModelRenderer armRight1;
    public AdvancedModelRenderer armRightJoint2;
    public AdvancedModelRenderer armRight2;
    public AdvancedModelRenderer leftRightJoint;
    public AdvancedModelRenderer armRight2Fur;
    public AdvancedModelRenderer rightHand;
    public AdvancedModelRenderer rightFingersJoint;
    public AdvancedModelRenderer rightThumb;
    public AdvancedModelRenderer rightFingers;
    public AdvancedModelRenderer legLeft1;
    public AdvancedModelRenderer legLeft2;
    public AdvancedModelRenderer leftFoot;
    public AdvancedModelRenderer legLeftFur;
    public AdvancedModelRenderer legRight1;
    public AdvancedModelRenderer legRight2;
    public AdvancedModelRenderer rightFoot;
    public AdvancedModelRenderer legRightFur;
    public AdvancedModelRenderer chestJoint;
    public AdvancedModelRenderer handController;
    public AdvancedModelRenderer swingOffsetController;
    public AdvancedModelRenderer roarController;
    public AdvancedModelRenderer rightHandSocket;
    public AdvancedModelRenderer leftHandSocket;
    public AdvancedModelRenderer mouthSocket;
    public AdvancedModelRenderer iceCrystal;
    public AdvancedModelRenderer iceCrystalJoint;
    public AdvancedModelRenderer iceCrystalHand;
    public AdvancedModelRenderer standUpController;

    public AdvancedModelRenderer headHair;
    public AdvancedModelRenderer hornR1;
    public AdvancedModelRenderer hornR2;
    public AdvancedModelRenderer hornR6;
    public AdvancedModelRenderer hornR7;
    public AdvancedModelRenderer hornR3;
    public AdvancedModelRenderer hornR5;
    public AdvancedModelRenderer hornR4;
    public AdvancedModelRenderer hornL1;
    public AdvancedModelRenderer hornL2;
    public AdvancedModelRenderer hornL3;
    public AdvancedModelRenderer hornL4;
    public AdvancedModelRenderer hornL5;
    public AdvancedModelRenderer hornL6;
    public AdvancedModelRenderer hornL7;
    public AdvancedModelRenderer backHair;
    public AdvancedModelRenderer armRight2FurClump1;
    public AdvancedModelRenderer armRight2FurClump2;
    public AdvancedModelRenderer armLeft2FurClump1;
    public AdvancedModelRenderer armLeft2FurClump2;
    public AdvancedModelRenderer earL;
    public AdvancedModelRenderer earR;

    public ModelFrostmaw() {
        this.textureWidth = 512;
        this.textureHeight = 256;
        this.leftHand = new AdvancedModelRenderer(this, 240, 0);
        this.leftHand.mirror = true;
        this.leftHand.setRotationPoint(-2.0F, 1.0F, -4.5F);
        this.leftHand.addBox(-10.0F, -2.0F, -7.5F, 20, 20, 9, 0.0F);
        setRotateAngle(leftHand, 0.0F, 0.3490658503988659F, 0.0F);
        this.legLeft2 = new AdvancedModelRenderer(this, 81, 77);
        this.legLeft2.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.legLeft2.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 16, 0.0F);
        setRotateAngle(legLeft2, 1.2217304763960306F, 0.0F, 0.0F);
        this.rightThumb = new AdvancedModelRenderer(this, 63, 0);
        this.rightThumb.mirror = true;
        this.rightThumb.setRotationPoint(10.0F, 0.5F, -5.0F);
        this.rightThumb.addBox(0.0F, -2.5F, -2.5F, 12, 6, 6, 0.0F);
        this.legRight1 = new AdvancedModelRenderer(this, 37, 56);
        this.legRight1.mirror = true;
        this.legRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legRight1.addBox(-4.5F, -4.5F, -17.0F, 9, 11, 21, 0.0F);
        setRotateAngle(legRight1, 0.7853981633974483F, 0.6981317007977318F, 0.0F);
        this.tuskRight2 = new AdvancedModelRenderer(this, 0, 80);
        this.tuskRight2.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.tuskRight2.addBox(-10.0F, -2.0F, -2.0F, 11, 4, 4, 0.0F);
        setRotateAngle(tuskRight2, -0.8727F, 1.0908F, 0.5236F);
        this.jawRotator = new AdvancedModelRenderer(this, 0, 0);
        this.jawRotator.setRotationPoint(0.0F, 7.353768176172814F, -12.24856180331474F);
        this.jawRotator.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(jawRotator, 0.0F, 0.7853981633974483F, 0.0F);
        this.leftThumb = new AdvancedModelRenderer(this, 63, 0);
        this.leftThumb.setRotationPoint(-10.0F, 0.5F, -5.0F);
        this.leftThumb.addBox(-12.0F, -2.5F, -2.5F, 12, 6, 6, 0.0F);
        this.armRightJoint2 = new AdvancedModelRenderer(this, 67, 0);
        this.armRightJoint2.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.armRightJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(armRightJoint2, -0.1710422666954443F, -0.15079644737231007F, -0.7155849933176751F);
        this.legLeft1 = new AdvancedModelRenderer(this, 37, 56);
        this.legLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legLeft1.addBox(-4.5F, -4.5F, -17.0F, 9, 11, 21, 0.0F);
        setRotateAngle(legLeft1, 0.7853981633974483F, -0.6981317007977318F, 0.0F);
        this.teethUpper = new AdvancedModelRenderer(this, 376, 0);
        this.teethUpper.setRotationPoint(14.0F, 12.0F, -14.0F);
        this.teethUpper.addBox(-13.0F, 0.0F, -13.0F, 26, 6, 26, 0.0F);
        this.leftHandJoint = new AdvancedModelRenderer(this, 8, 0);
        this.leftHandJoint.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.leftHandJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(leftHandJoint, 0.8726646259971648F, -0.3490658503988659F, -0.2617993877991494F);
        this.jawSpikes2 = new AdvancedModelRenderer(this, 212, 104);
        this.jawSpikes2.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.jawSpikes2.addBox(-19.0F, 0.0F, -19.0F, 38, 9, 38, 0.0F);
        this.rightHand = new AdvancedModelRenderer(this, 240, 0);
        this.rightHand.setRotationPoint(2.0F, 1.0F, -4.5F);
        this.rightHand.addBox(-10.0F, -2.0F, -7.52F, 20, 20, 9, 0.0F);
        setRotateAngle(rightHand, 0.0F, -0.3490658503988659F, 0.0F);
        this.armLeft1 = new AdvancedModelRenderer(this, 0, 88);
        this.armLeft1.mirror = true;
        this.armLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLeft1.addBox(-8.5F, -10.0F, -8.5F, 17, 34, 17, 0.0F);
        setRotateAngle(armLeft1, 0.22759093446006054F, 0.0F, -0.7285004297824331F);
        this.leftFoot = new AdvancedModelRenderer(this, 80, 12);
        this.leftFoot.setRotationPoint(0.0F, 0.2F, -12.0F);
        this.leftFoot.mirror = true;
        this.leftFoot.addBox(-6.5F, -14.75F, -6.3F, 13, 20, 6, 0.0F);
        setRotateAngle(leftFoot, -0.4363323129985824F, 0.0F, 0.0F);
        this.rightFingers = new AdvancedModelRenderer(this, 0, 62);
        this.rightFingers.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.rightFingers.addBox(-10.0F, -7.5F, -2.5F, 20, 10, 5, 0.0F);
        this.jawJoint = new AdvancedModelRenderer(this, 0, 0);
        this.jawJoint.setRotationPoint(6.41F, 11.0F, -6.41F);
        this.jawJoint.addBox(0.0F, -5.65F, -4.25F, 0, 0, 0, 0.0F);
        setRotateAngle(jawJoint, -0.17453292519943295F, -0.7853981633974483F, 0.0F);
        this.leftFingers = new AdvancedModelRenderer(this, 0, 62);
        this.leftFingers.mirror = true;
        this.leftFingers.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.leftFingers.addBox(-10.0F, -7.5F, -2.5F, 20, 10, 5, 0.0F);
        this.headRotator = new AdvancedModelRenderer(this, 0, 0);
        this.headRotator.setRotationPoint(0.0F, -6.0F, -16.0F);
        this.headRotator.addBox(0.0F, 0.0F, -0.1F, 0, 0, 0, 0.0F);
        setRotateAngle(headRotator, 0.0F, 0.7853981633974483F, 0.0F);
        this.armLeft2Fur = new AdvancedModelRenderer(this, 326, 113);
        this.armLeft2Fur.setRotationPoint(0.0F, 28.0F, 0.0F);
        this.armLeft2Fur.addBox(-11.0F, -5.0F, -11.0F, 22, 7, 22, 0.0F);
        this.armRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.armRightJoint.setRotationPoint(-28.0F, 0.0F, -15.0F);
        this.armRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(armRightJoint, 0.08726646259971647F, 0.0F, 0.0F);
        this.jaw = new AdvancedModelRenderer(this, 242, 52);
        this.jaw.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.jaw.addBox(-19.0F, 0.0F, -19.0F, 38, 14, 38, 0.0F);
        this.eyeLidLeft = new AdvancedModelRenderer(this, 92, 107);
        this.eyeLidLeft.mirror = true;
        this.eyeLidLeft.setRotationPoint(30.01F, -5.0F, -12.5F);
        this.eyeLidLeft.addBox(-7.5F, 0.0F, 0.0F, 15, 14, 0, 0.0F);
        setRotateAngle(eyeLidLeft, 0.0F, -1.5707963267948966F, 0.0F);
        this.jawSpikes1 = new AdvancedModelRenderer(this, 380, 48);
        this.jawSpikes1.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.jawSpikes1.addBox(-14.0F, 0.0F, -14.0F, 28, 14, 28, 0.0F);
        this.tuskRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.tuskRightJoint.setRotationPoint(-12.0F, 0.0F, -19.0F);
        this.tuskRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(tuskRightJoint, 0.0F, 1.5707963267948966F, 0.0F);
        this.armRight2Fur = new AdvancedModelRenderer(this, 326, 113);
        this.armRight2Fur.setRotationPoint(0.0F, 28.0F, 0.0F);
        this.armRight2Fur.addBox(-11.0F, -5.0F, -11.0F, 22, 7, 22, 0.0F);
        this.legRightFur = new AdvancedModelRenderer(this, 144, 77);
        this.legRightFur.mirror = true;
        this.legRightFur.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.legRightFur.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 3, 0.0F);
        this.tuskLeft2 = new AdvancedModelRenderer(this, 0, 80);
        this.tuskLeft2.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.tuskLeft2.addBox(-10.0F, -2.0F, -2.0F, 11, 4, 4, 0.0F);
        setRotateAngle(tuskLeft2, 0.8727F, -1.0908F, 0.5236F);
        this.legRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.legRightJoint.setRotationPoint(-9.0F, 3.14F, 0.0F);
        this.legRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(legRightJoint, -0.6981317007977318F, 0.0F, 0.0F);
        this.tuskRight1 = new AdvancedModelRenderer(this, 68, 109);
        this.tuskRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tuskRight1.addBox(-3.0F, -8.0F, -3.0F, 6, 13, 6, 0.0F);
        setRotateAngle(tuskRight1, 0.0F, 0.0F, 2.0943951023931953F);
        this.rightFingersJoint = new AdvancedModelRenderer(this, 0, 47);
        this.rightFingersJoint.setRotationPoint(0.0F, 15.5F, -3.0F);
        this.rightFingersJoint.addBox(-10.0F, -2.5F, 0.0F, 20, 5, 7, 0.0F);
        this.teethLower = new AdvancedModelRenderer(this, 383, 120);
        this.teethLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.teethLower.addBox(-14.0F, -6.0F, -17.0F, 31, 6, 31, 0.0F);
        this.chest = new AdvancedModelRenderer(this, 80, 0);
        this.chest.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chest.addBox(-30.0F, -25.0F, -30.0F, 60, 37, 40, 0.0F);
        setRotateAngle(chest, 0.0F, 0.0F, 0.0F);
        this.chestJoint = new AdvancedModelRenderer(this, 80, 0);
        this.chestJoint.setRotationPoint(0.0F, -24.96F, -1.0F);
        setRotateAngle(chestJoint, -0.7853981633974483F, 0.0F, 0.0F);
        this.leftFingersJoint = new AdvancedModelRenderer(this, 0, 47);
        this.leftFingersJoint.mirror = true;
        this.leftFingersJoint.setRotationPoint(0.0F, 15.5F, -3.0F);
        this.leftFingersJoint.addBox(-10.0F, -2.5F, 0.0F, 20, 5, 7, 0.0F);
        this.headBack = new AdvancedModelRenderer(this, 0, 139);
        this.headBack.setRotationPoint(14.0F, 12.0F, -14.0F);
        this.headBack.addBox(-16.0F, 0.0F, -16.0F, 32, 6, 32, 0.0F);
        this.legRight2 = new AdvancedModelRenderer(this, 81, 77);
        this.legRight2.mirror = true;
        this.legRight2.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.legRight2.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 16, 0.0F);
        setRotateAngle(legRight2, 1.2217304763960306F, 0.0F, 0.0F);
        this.eyeLidRight = new AdvancedModelRenderer(this, 92, 107);
        this.eyeLidRight.setRotationPoint(12.5F, -5.0F, -30.01F);
        this.eyeLidRight.addBox(-7.5F, 0.0F, 0.0F, 15, 14, 0, 0.0F);
        this.armLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.armLeftJoint.setRotationPoint(28.0F, 0.0F, -15.0F);
        this.armLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(armLeftJoint, 0.08726646259971647F, 0.0F, 0.0F);
        this.armLeftJoint2 = new AdvancedModelRenderer(this, 4, 0);
        this.armLeftJoint2.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.armLeftJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(armLeftJoint2, -0.1710422666954443F, 0.15079644737231007F, 0.7155849933176751F);
        this.armLeft2 = new AdvancedModelRenderer(this, 112, 109);
        this.armLeft2.mirror = true;
        this.armLeft2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLeft2.addBox(-11.0F, -15.0F, -11.0F, 22, 38, 22, 0.0F);
        setRotateAngle(armLeft2, -0.8726646259971648F, 0.4363323129985824F, -0.08726646259971647F);
        this.armRight2 = new AdvancedModelRenderer(this, 112, 109);
        this.armRight2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRight2.addBox(-11.0F, -15.0F, -11.0F, 22, 38, 22, 0.0F);
        setRotateAngle(armRight2, -0.8726646259971648F, -0.4363323129985824F, 0.08726646259971647F);
        this.waist = new AdvancedModelRenderer(this, 0, 0);
        this.waist.setRotationPoint(0.0F, -30.0F, 5.0F);
        this.waist.addBox(-11.5F, -23.0F, -8.5F, 23, 30, 17, 0.0F);
        setRotateAngle(waist, 0.6981317007977318F, 0.0F, 0.0F);
        this.head = new AdvancedModelRenderer(this, 280, 0);
        this.head.setRotationPoint(-13.0F, 5.18F, 13.0F);
        this.head.addBox(-2.0F, -5.0F, -30.0F, 32, 17, 32, 0.0F);
        this.armRight1 = new AdvancedModelRenderer(this, 0, 88);
        this.armRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRight1.addBox(-8.5F, -10.0F, -8.5F, 17, 34, 17, 0.0F);
        setRotateAngle(armRight1, 0.22759093446006054F, 0.0F, 0.7285004297824331F);
        this.tuskLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.tuskLeftJoint.setRotationPoint(19.0F, 0.0F, 12.0F);
        this.tuskLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.headJoint = new AdvancedModelRenderer(this, 0, 0);
        this.headJoint.setRotationPoint(0.0F, -10.0F, -30.0F);
        this.headJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(headJoint, 0.3490658503988659F, 0.0F, 0.0F);
        this.tuskLeft1 = new AdvancedModelRenderer(this, 68, 109);
        this.tuskLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tuskLeft1.addBox(-3.0F, -8.0F, -3.0F, 6, 13, 6, 0.0F);
        setRotateAngle(tuskLeft1, 0.0F, 0.0F, 2.0943951023931953F);
        this.leftRightJoint = new AdvancedModelRenderer(this, 71, 0);
        this.leftRightJoint.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.leftRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(leftRightJoint, 0.8726646259971648F, 0.3490658503988659F, 0.2617993877991494F);
        this.rightFoot = new AdvancedModelRenderer(this, 80, 12);
        this.rightFoot.mirror = false;
        this.rightFoot.setRotationPoint(0.0F, 0.2F, -12.0F);
        this.rightFoot.addBox(-6.5F, -14.75F, -6.3F, 13, 20, 6, 0.0F);
        setRotateAngle(rightFoot, -0.4363323129985824F, 0.0F, 0.0F);
        this.root = new AdvancedModelRenderer(this, 0, 0);
        this.root.setRotationPoint(0.0F, 24.0F, 10.0F);
        this.root.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.legLeftFur = new AdvancedModelRenderer(this, 144, 77);
        this.legLeftFur.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.legLeftFur.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 3, 0.0F);
        this.legLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.legLeftJoint.setRotationPoint(9.0F, 3.14F, 0.0F);
        this.legLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(legLeftJoint, -0.6981317007977318F, 0.0F, 0.0F);
        this.iceCrystal = new AdvancedModelRenderer(this, 0, 0);
        this.iceCrystal.setRotationPoint(0, 0, 0);
//        this.iceCrystal.add3DTexture(-8, -8, 0, 16, 16);
        this.iceCrystalJoint = new AdvancedModelRenderer(this, 0, 0);
        this.iceCrystalJoint.setRotationPoint(0, 20, -20);
        this.iceCrystalHand = new AdvancedModelRenderer(this, 0, 0);
//        this.iceCrystalHand.add3DTexture(-8, -8, 0, 16, 16);
//        this.iceCrystalHand.setScale(0.5f, 0.5f, 0.5f);
        this.iceCrystalHand.setRotationPoint(-28.5f, 10, -25.5f);
        
        headHair = new AdvancedModelRenderer(this);
        headHair.setRotationPoint(0.0F, -3.0F, -24.0F);
        headJoint.addChild(headHair);
        setRotateAngle(headHair, 0.2182F, 0.0F, 0.0F);
        headHair.setTextureOffset(266, 192).addBox(-9.0F, -9.0F, -1.0F, 18, 23, 36, false);
        headHair.setScale(0.999f, 1, 1);

        hornR1 = new AdvancedModelRenderer(this);
        hornR1.setRotationPoint(8.5F, 0.0F, -10.5F);
        headJoint.addChild(hornR1);
        setRotateAngle(hornR1, -0.2327F, 0.7396F, 0.5849F);
        hornR1.setTextureOffset(72, 177).addBox(-7.5F, -21.0F, -9.5F, 17, 23, 17, false);

        hornR2 = new AdvancedModelRenderer(this);
        hornR2.setRotationPoint(0.0F, -14.0F, 2.0F);
        hornR1.addChild(hornR2);
        setRotateAngle(hornR2, -0.3286F, -0.0089F, -0.665F);
        hornR2.setTextureOffset(140, 169).addBox(-4.5F, -20.0F, -9.5F, 14, 22, 14, false);

        hornR6 = new AdvancedModelRenderer(this);
        hornR6.setRotationPoint(0.2003F, 0.5395F, 3.3145F);
        hornR2.addChild(hornR6);
        setRotateAngle(hornR6, -0.397F, 0.3402F, 0.428F);
        hornR6.setTextureOffset(0, 226).addBox(-0.5F, -16.0F, -9.5F, 10, 19, 10, false);

        hornR7 = new AdvancedModelRenderer(this);
        hornR7.setRotationPoint(6.0F, -13.0F, -5.0F);
        hornR6.addChild(hornR7);
        setRotateAngle(hornR7, 0.546F, 0.0303F, 0.2416F);
        hornR7.setTextureOffset(455, 0).addBox(-4.6263F, -15.3903F, -1.6013F, 6, 18, 6, false);

        hornR3 = new AdvancedModelRenderer(this);
        hornR3.setRotationPoint(-3.0F, -14.0F, 2.0F);
        hornR2.addChild(hornR3);
        setRotateAngle(hornR3, -0.3356F, -0.0462F, -0.1705F);
        hornR3.setTextureOffset(0, 226).addBox(-0.5F, -20.0F, -9.5F, 10, 20, 10, false);

        hornR5 = new AdvancedModelRenderer(this);
        hornR5.setRotationPoint(4.0F, -9.0F, -9.0F);
        hornR3.addChild(hornR5);
        setRotateAngle(hornR5, 0.8134F, 0.4571F, -0.1047F);
        hornR5.setTextureOffset(405, 193).addBox(-4.6263F, -12.3903F, -0.6013F, 5, 17, 5, false);

        hornR4 = new AdvancedModelRenderer(this);
        hornR4.setRotationPoint(6.0F, -15.0F, -5.0F);
        hornR3.addChild(hornR4);
        setRotateAngle(hornR4, 0.4363F, -0.3927F, 0.2618F);
        hornR4.setTextureOffset(455, 0).addBox(-4.6263F, -19.3903F, -0.6013F, 6, 18, 6, false);

        hornL1 = new AdvancedModelRenderer(this);
        hornL1.setRotationPoint(-8.5F, 0.0F, -10.5F);
        headJoint.addChild(hornL1);
        setRotateAngle(hornL1, -0.2327F, -0.7396F, -0.5849F);
        hornL1.setTextureOffset(72, 177).addBox(-9.5F, -21.0F, -9.5F, 17, 23, 17, true);

        hornL2 = new AdvancedModelRenderer(this);
        hornL2.setRotationPoint(0.0F, -14.0F, 2.0F);
        hornL1.addChild(hornL2);
        setRotateAngle(hornL2, -0.3286F, 0.0089F, 0.665F);
        hornL2.setTextureOffset(140, 169).addBox(-9.5F, -20.0F, -9.5F, 14, 22, 14, true);

        hornL3 = new AdvancedModelRenderer(this);
        hornL3.setRotationPoint(3.0F, -14.0F, 2.0F);
        hornL2.addChild(hornL3);
        setRotateAngle(hornL3, -0.3356F, 0.0462F, 0.1705F);
        hornL3.setTextureOffset(0, 226).addBox(-9.5F, -20.0F, -9.5F, 10, 20, 10, true);

        hornL4 = new AdvancedModelRenderer(this);
        hornL4.setRotationPoint(-6.0F, -15.0F, -5.0F);
        hornL3.addChild(hornL4);
        setRotateAngle(hornL4, 0.4363F, 0.3927F, -0.2618F);
        hornL4.setTextureOffset(455, 0).addBox(-1.3737F, -19.3903F, -0.6013F, 6, 18, 6, true);

        hornL5 = new AdvancedModelRenderer(this);
        hornL5.setRotationPoint(-4.0F, -9.0F, -9.0F);
        hornL3.addChild(hornL5);
        setRotateAngle(hornL5, 0.8134F, -0.4571F, 0.1047F);
        hornL5.setTextureOffset(405, 193).addBox(-0.3737F, -12.3903F, -0.6013F, 5, 17, 5, true);

        hornL6 = new AdvancedModelRenderer(this);
        hornL6.setRotationPoint(-0.2003F, 0.5395F, 3.3145F);
        hornL2.addChild(hornL6);
        setRotateAngle(hornL6, -0.397F, -0.3402F, -0.428F);
        hornL6.setTextureOffset(0, 226).addBox(-9.5F, -16.0F, -9.5F, 10, 19, 10, true);

        hornL7 = new AdvancedModelRenderer(this);
        hornL7.setRotationPoint(-6.0F, -13.0F, -5.0F);
        hornL6.addChild(hornL7);
        setRotateAngle(hornL7, 0.546F, -0.0303F, -0.2416F);
        hornL7.setTextureOffset(455, 0).addBox(-1.3737F, -15.3903F, -1.6013F, 6, 18, 6, true);

        earL = new AdvancedModelRenderer(this);
        earL.setRotationPoint(-19.0F, 0.0F, -14.0F);
        headJoint.addChild(earL);
        setRotateAngle(earL, -0.3831F, 0.4174F, -0.3721F);
        earL.setTextureOffset(396, 102).addBox(-23.0F, -2.0F, -1.0F, 23, 17, 0, false);

        earR = new AdvancedModelRenderer(this);
        earR.setRotationPoint(19.0F, 0.0F, -14.0F);
        headJoint.addChild(earR);
        setRotateAngle(earR, -0.3831F, -0.4174F, 0.3721F);
        earR.setTextureOffset(396, 102).addBox(0.0F, -2.0F, -1.0F, 23, 17, 0, true);

        armLeft2FurClump1 = new AdvancedModelRenderer(this);
        armLeft2FurClump1.setRotationPoint(10.0F, 23.96F, 10.0F);
        armLeft2.addChild(armLeft2FurClump1);
        setRotateAngle(armLeft2FurClump1, -0.1571F, 0.0F, 0.2269F);
        armLeft2FurClump1.setTextureOffset(0, 179).addBox(-18.0F, -26.0F, -18.0F, 18, 26, 18, false);

        armLeft2FurClump2 = new AdvancedModelRenderer(this);
        armLeft2FurClump2.setRotationPoint(-10.0F, 23.96F, 10.0F);
        armLeft2.addChild(armLeft2FurClump2);
        setRotateAngle(armLeft2FurClump2, -0.3054F, -0.0436F, -0.2531F);
        armLeft2FurClump2.setTextureOffset(40, 223).addBox(0.0F, -16.0F, -18.0F, 18, 15, 18, false);

        armRight2FurClump1 = new AdvancedModelRenderer(this);
        armRight2FurClump1.setRotationPoint(-10.0F, 23.96F, 10.0F);
        armRight2.addChild(armRight2FurClump1);
        setRotateAngle(armRight2FurClump1, -0.1571F, 0.0F, -0.2269F);
        armRight2FurClump1.setTextureOffset(0, 179).addBox(0.0F, -26.0F, -18.0F, 18, 26, 18, true);

        armRight2FurClump2 = new AdvancedModelRenderer(this);
        armRight2FurClump2.setRotationPoint(10.0F, 23.96F, 10.0F);
        armRight2.addChild(armRight2FurClump2);
        setRotateAngle(armRight2FurClump2, -0.3054F, 0.0436F, 0.2531F);
        armRight2FurClump2.setTextureOffset(40, 223).addBox(-18.0F, -16.0F, -18.0F, 18, 15, 18, true);

        backHair = new AdvancedModelRenderer(this);
        backHair.setRotationPoint(0.5F, -24.5373F, -33.3434F);
        chest.addChild(backHair);
        setRotateAngle(backHair, -0.1309F, 0.0F, 0.0F);
        backHair.setTextureOffset(374, 180).addBox(-9.5F, -8.2441F, -4.8728F, 18, 25, 51, false);

        handController = new AdvancedModelRenderer(this, 0, 0);
        swingOffsetController = new AdvancedModelRenderer(this, 0, 0);
        roarController = new AdvancedModelRenderer(this, 0, 0);
        standUpController = new AdvancedModelRenderer(this, 0, 0);
        rightHandSocket = new AdvancedModelRenderer(this);
        leftHandSocket = new AdvancedModelRenderer(this);
        mouthSocket = new AdvancedModelRenderer(this);

        this.leftHandJoint.addChild(this.leftHand);
        this.legLeft1.addChild(this.legLeft2);
        this.rightHand.addChild(this.rightThumb);
        this.legRightJoint.addChild(this.legRight1);
        this.tuskRight1.addChild(this.tuskRight2);
        this.jawJoint.addChild(this.jawRotator);
        this.leftHand.addChild(this.leftThumb);
        this.armRight1.addChild(this.armRightJoint2);
        this.legLeftJoint.addChild(this.legLeft1);
        this.head.addChild(this.teethUpper);
        this.armLeft2.addChild(this.leftHandJoint);
        this.jaw.addChild(this.jawSpikes2);
        this.leftRightJoint.addChild(this.rightHand);
        this.armLeftJoint.addChild(this.armLeft1);
        this.legLeft2.addChild(this.leftFoot);
        this.rightFingersJoint.addChild(this.rightFingers);
        this.head.addChild(this.jawJoint);
        this.leftFingersJoint.addChild(this.leftFingers);
        this.headJoint.addChild(this.headRotator);
        this.armLeft2.addChild(this.armLeft2Fur);
        this.chest.addChild(this.armRightJoint);
        this.jawRotator.addChild(this.jaw);
        this.head.addChild(this.eyeLidLeft);
        this.jaw.addChild(this.jawSpikes1);
        this.jaw.addChild(this.tuskRightJoint);
        this.armRight2.addChild(this.armRight2Fur);
        this.legRight2.addChild(this.legRightFur);
        this.tuskLeft1.addChild(this.tuskLeft2);
        this.waist.addChild(this.legRightJoint);
        this.tuskRightJoint.addChild(this.tuskRight1);
        this.rightHand.addChild(this.rightFingersJoint);
        this.jaw.addChild(this.teethLower);
        this.waist.addChild(this.chestJoint);
        this.chestJoint.addChild(this.chest);
        this.leftHand.addChild(this.leftFingersJoint);
        this.head.addChild(this.headBack);
        this.legRight1.addChild(this.legRight2);
        this.head.addChild(this.eyeLidRight);
        this.chest.addChild(this.armLeftJoint);
        this.armLeft1.addChild(this.armLeftJoint2);
        this.armLeftJoint2.addChild(this.armLeft2);
        this.armRightJoint2.addChild(this.armRight2);
        this.root.addChild(this.waist);
        this.headRotator.addChild(this.head);
        this.armRightJoint.addChild(this.armRight1);
        this.jaw.addChild(this.tuskLeftJoint);
        this.chest.addChild(this.headJoint);
        this.tuskLeftJoint.addChild(this.tuskLeft1);
        this.armRight2.addChild(this.leftRightJoint);
        this.legRight2.addChild(this.rightFoot);
        this.legLeft2.addChild(this.legLeftFur);
        this.waist.addChild(this.legLeftJoint);
        this.headJoint.addChild(iceCrystalJoint);
        this.iceCrystalJoint.addChild(iceCrystal);
        rightHand.addChild(rightHandSocket);
        leftHand.addChild(leftHandSocket);
        headJoint.addChild(mouthSocket);

        eyeLidLeft.showModel = false;
        eyeLidRight.showModel = false;
        leftHand.setScale(1.001f, 1.001f, 1.001f);
        rightHand.setScale(1.001f, 1.001f, 1.001f);
        leftFingersJoint.setScale(1.002f, 1.002f, 1.002f);
        rightFingersJoint.setScale(1.002f, 1.002f, 1.002f);
        leftThumb.rotateAngleY = (float) (Math.PI);
        leftThumb.rotationPointZ = 4;
        rightThumb.rotateAngleY = (float) (Math.PI);
        rightThumb.rotationPointZ = 4;
        iceCrystal.showModel = false;

        updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.iceCrystalHand.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        LegArticulator.articulateQuadruped(entity, entity.legSolver, waist, headJoint,
                legLeft1, legLeft2, legRight1, legRight2, armLeftJoint, armLeftJoint2, armRightJoint, armRightJoint2,
                0.6f, 0.6f, -0.65f, -0.65f,
                ageInTicks - entity.ticksExisted
        );
        legLeftJoint.rotateAngleX -= waist.rotateAngleX - waist.defaultRotationX;
        legRightJoint.rotateAngleX -= waist.rotateAngleX - waist.defaultRotationX;

        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        mouthSocket.setRotationPoint(0, -10, 8);
        mouthSocket.rotationPointZ -= 28;
        mouthSocket.rotationPointY += 26;
    }

    @Override
    protected void animate(EntityFrostmaw entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        float frame = entity.ticksExisted + delta;

        if (entity.getAnimation() == EntityFrostmaw.SWIPE_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.SWIPE_ANIMATION);
            if (entity.swingWhichArm) {
                animator.startKeyframe(7);
                animator.rotate(waist, -0.3f, 0.08f, 0);
                animator.rotate(legRightJoint, 0.3f, 0, 0);
                animator.rotate(legLeftJoint, 0.3f, 0, 0);
                animator.rotate(chest, 0, 0.08f, 0.5f);
                animator.rotate(headJoint, 0.2f, 0, -0.35f);
                animator.rotate(armLeftJoint, 0.3f, 0, -0.5f);
                animator.rotate(armLeftJoint, 0.2f, 0, -0.25f);
                animator.rotate(armLeftJoint2, 0f, 0, 0.18f);
                animator.rotate(leftHand, -0.1f, 0, 0.05f);

                animator.rotate(armRightJoint, -1.5f, 0.2f, 0f);
                animator.rotate(armRight1, 0, 0, 0.5f);
                animator.rotate(armRight2, 0.4f, 0.4f, 0f);
                animator.rotate(rightHand, -0.8f, 0.2f, -0.8f);
                animator.move(handController, 0, 1, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(2);

                animator.startKeyframe(5);
                animator.rotate(waist, 0.15f, -0.2f, 0);
                animator.rotate(chest, 0, -0.2f, -0.5f);
                animator.rotate(headJoint, -0.15f, 0.35f, 0.4f);
                animator.rotate(legRightJoint, -0.15f, 0, 0);
                animator.rotate(legLeftJoint, -0.15f, 0, 0);

                animator.rotate(armLeftJoint, -0.8f, 0.4f, 0.8f);
                animator.move(armLeftJoint, 0, 0, 1f);
                animator.rotate(armLeftJoint, 0.2f, 0, -0.25f);
                animator.rotate(armLeftJoint2, 1f, 0, -0.2f);
                animator.rotate(armLeft2, 0, 0.2f, 0);
                animator.rotate(leftHand, -0.5f, 0, -0.1f);

                animator.rotate(armRightJoint, -0.8f, -0.4f, 0f);
                animator.move(armRightJoint, 0, 0, -13);
                animator.rotate(armRight1, 0, 0, -0.5f);
                animator.rotate(armRight2, 0.5f, 0.4f, 0f);
                animator.rotate(rightHand, -0.4f, 0.6f, -0.8f);
                animator.move(handController, 0, 1, 0);
                animator.move(swingOffsetController, 7, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(4);
                animator.resetKeyframe(10);

                float swingFrame = swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 14) {
                    waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeftJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeftJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    armRightJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRight2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRight2.rotateAngleY += 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            }
            else {
                animator.startKeyframe(7);
                animator.rotate(waist, -0.3f, -0.08f, 0);
                animator.rotate(legLeftJoint, 0.3f, 0, 0);
                animator.rotate(legRightJoint, 0.3f, 0, 0);
                animator.rotate(chest, 0, -0.08f, -0.5f);
                animator.rotate(headJoint, 0.2f, 0, 0.35f);
                animator.rotate(armRightJoint, 0.3f, 0, 0.5f);
                animator.rotate(armRightJoint, 0.2f, 0, 0.25f);
                animator.rotate(armRightJoint2, 0f, 0, -0.18f);
                animator.rotate(rightHand, -0.1f, 0, -0.05f);

                animator.rotate(armLeftJoint, -1.5f, -0.2f, 0f);
                animator.rotate(armLeft1, 0, 0, -0.5f);
                animator.rotate(armLeft2, 0.4f, -0.4f, 0f);
                animator.rotate(leftHand, -0.8f, -0.2f, 0.8f);
                animator.move(handController, 1, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(2);

                animator.startKeyframe(5);
                animator.rotate(waist, 0.15f, 0.2f, 0);
                animator.rotate(chest, 0, 0.2f, 0.5f);
                animator.rotate(headJoint, -0.15f, -0.35f, -0.4f);
                animator.rotate(legLeftJoint, -0.15f, 0, 0);
                animator.rotate(legRightJoint, -0.15f, 0, 0);

                animator.rotate(armRightJoint, -0.8f, -0.4f, -0.8f);
                animator.move(armRightJoint, 0, 0, -1f);
                animator.rotate(armRightJoint, 0.2f, 0, 0.25f);
                animator.rotate(armRightJoint2, 1f, 0, 0.2f);
                animator.rotate(armRight2, 0, -0.2f, 0);
                animator.rotate(rightHand, -0.5f, 0, 0.1f);

                animator.rotate(armLeftJoint, -0.8f, 0.4f, 0f);
                animator.move(armLeftJoint, 0, 0, -13);
                animator.rotate(armLeft1, 0, 0, 0.5f);
                animator.rotate(armLeft2, 0.5f, -0.4f, 0f);
                animator.rotate(leftHand, -0.4f, -0.6f, 0.8f);
                animator.move(handController, 1, 0, 0);
                animator.move(swingOffsetController, 7, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(4);
                animator.resetKeyframe(10);

                float swingFrame = swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 14) {
                    waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRightJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRightJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    armLeftJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeft2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeft2.rotateAngleY -= 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            }
        }

        if (entity.getAnimation() == EntityFrostmaw.SWIPE_TWICE_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.SWIPE_TWICE_ANIMATION);
            if (entity.swingWhichArm) {
                animator.startKeyframe(7);
                animator.rotate(waist, -0.3f, 0.08f, 0);
                animator.rotate(legRightJoint, 0.3f, 0, 0);
                animator.rotate(legLeftJoint, 0.3f, 0, 0);
                animator.rotate(chest, 0, 0.08f, 0.5f);
                animator.rotate(headJoint, 0.2f, 0, -0.35f);
                animator.rotate(armLeftJoint, 0.3f, 0, -0.5f);
                animator.rotate(armLeftJoint, 0.2f, 0, -0.25f);
                animator.rotate(armLeftJoint2, 0f, 0, 0.18f);
                animator.rotate(leftHand, -0.1f, 0, 0.05f);

                animator.rotate(armRightJoint, -1.5f, 0.2f, 0f);
                animator.rotate(armRight1, 0, 0, 0.5f);
                animator.rotate(armRight2, 0.4f, 0.4f, 0f);
                animator.rotate(rightHand, -0.8f, 0.2f, -0.8f);
                animator.move(handController, 0, 1, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(2);

                animator.startKeyframe(6);
                animator.rotate(waist, -0.15f, -0.2f, 0);
                animator.rotate(chest, 0, -0.2f, -0.5f);
                animator.rotate(headJoint, 0.15f, 0.35f, 0.4f);
                animator.rotate(legRightJoint, 0.15f, 0, 0);
                animator.rotate(legLeftJoint, 0.15f, 0, 0);

                animator.rotate(armLeftJoint, -1.5f, -0.2f, 0f);
                animator.rotate(armLeft1, 0, 0, -0.8f);
                animator.rotate(armLeft2, 0.4f, -0.4f, 0f);
                animator.rotate(leftHand, -0.8f, -0.2f, 0.8f);
                animator.move(handController, 1, 0, 0);

                animator.rotate(armRightJoint, -0.5f, -0.4f, 0f);
                animator.move(armRightJoint, 0, 0, -13);
                animator.rotate(armRight1, 0, 0, -0.5f);
                animator.rotate(armRight2, 0.5f, 0.4f, 0f);
                animator.rotate(rightHand, -0.4f, 0.6f, -0.8f);
                animator.move(handController, 0, 1, 0);
                animator.move(swingOffsetController, 7, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(4);

                animator.startKeyframe(6);
                animator.rotate(waist, 0.15f, 0.2f, 0);
                animator.rotate(chest, 0, 0.2f, 0.5f);
                animator.rotate(headJoint, -0.15f, -0.35f, -0.4f);
                animator.rotate(legLeftJoint, -0.15f, 0, 0);
                animator.rotate(legRightJoint, -0.15f, 0, 0);

                animator.rotate(armRightJoint, -0.8f, -0.4f, -0.8f);
                animator.move(armRightJoint, 0, 0, 1f);
                animator.rotate(armRightJoint, 0.2f, 0, 0.25f);
                animator.rotate(armRightJoint2, 1f, 0, 0.2f);
                animator.rotate(armRight2, 0, -0.2f, 0);
                animator.rotate(rightHand, -0.5f, 0, 0.1f);

                animator.rotate(armLeftJoint, -0.8f, 0.4f, 0f);
                animator.move(armLeftJoint, 0, 0, -13);
                animator.rotate(armLeft1, 0, 0, 0.5f);
                animator.rotate(armLeft2, 0.5f, -0.4f, 0f);
                animator.rotate(leftHand, -0.4f, -0.6f, 0.8f);
                animator.move(handController, 1, 0, 0);
                animator.move(swingOffsetController, 0, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(4);
                animator.resetKeyframe(10);

                float swingFrame = swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 15) {
                    waist.rotateAngleX += 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    headJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legRightJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legLeftJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeftJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeftJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    armRightJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRight2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRight2.rotateAngleY += 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
                if (entity.getAnimationTick() > 15 && entity.getAnimationTick() <= 25) {
                    waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRightJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRightJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    armLeftJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeft2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeft2.rotateAngleY -= 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            }
            else {
                animator.startKeyframe(7);
                animator.rotate(waist, -0.3f, -0.08f, 0);
                animator.rotate(legLeftJoint, 0.3f, 0, 0);
                animator.rotate(legRightJoint, 0.3f, 0, 0);
                animator.rotate(chest, 0, -0.08f, -0.5f);
                animator.rotate(headJoint, 0.2f, 0, 0.35f);
                animator.rotate(armRightJoint, 0.3f, 0, 0.5f);
                animator.rotate(armRightJoint, 0.2f, 0, 0.25f);
                animator.rotate(armRightJoint2, 0f, 0, -0.18f);
                animator.rotate(rightHand, -0.1f, 0, -0.05f);

                animator.rotate(armLeftJoint, -1.5f, -0.2f, 0f);
                animator.rotate(armLeft1, 0, 0, -0.5f);
                animator.rotate(armLeft2, 0.4f, -0.4f, 0f);
                animator.rotate(leftHand, -0.8f, -0.2f, 0.8f);
                animator.move(handController, 1, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(2);

                animator.startKeyframe(6);
                animator.rotate(waist, -0.15f, 0.2f, 0);
                animator.rotate(chest, 0, 0.2f, 0.5f);
                animator.rotate(headJoint, 0.15f, -0.35f, -0.4f);
                animator.rotate(legLeftJoint, 0.15f, 0, 0);
                animator.rotate(legRightJoint, 0.15f, 0, 0);

                animator.rotate(armRightJoint, -1.5f, 0.2f, 0f);
                animator.rotate(armRight1, 0, 0, 0.8f);
                animator.rotate(armRight2, 0.4f, 0.4f, 0f);
                animator.rotate(rightHand, -0.8f, 0.2f, -0.8f);
                animator.move(handController, 0, 1, 0);

                animator.rotate(armLeftJoint, -0.5f, 0.4f, 0f);
                animator.move(armLeftJoint, 0, 0, -13);
                animator.rotate(armLeft1, 0, 0, 0.5f);
                animator.rotate(armLeft2, 0.5f, -0.4f, 0f);
                animator.rotate(leftHand, -0.4f, -0.6f, 0.8f);
                animator.move(handController, 1, 0, 0);
                animator.move(swingOffsetController, 7, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(4);

                animator.startKeyframe(6);
                animator.rotate(waist, 0.15f, -0.2f, 0);
                animator.rotate(chest, 0, -0.2f, -0.5f);
                animator.rotate(headJoint, -0.15f, 0.35f, 0.4f);
                animator.rotate(legRightJoint, -0.15f, 0, 0);
                animator.rotate(legLeftJoint, -0.15f, 0, 0);

                animator.rotate(armLeftJoint, -0.8f, 0.4f, 0.8f);
                animator.move(armLeftJoint, 0, 0, 1f);
                animator.rotate(armLeftJoint, 0.2f, 0, -0.25f);
                animator.rotate(armLeftJoint2, 1f, 0, -0.2f);
                animator.rotate(armLeft2, 0, 0.2f, 0);
                animator.rotate(leftHand, -0.5f, 0, -0.1f);

                animator.rotate(armRightJoint, -0.8f, -0.4f, 0f);
                animator.move(armRightJoint, 0, 0, -13);
                animator.rotate(armRight1, 0, 0, -0.5f);
                animator.rotate(armRight2, 0.5f, 0.4f, 0f);
                animator.rotate(rightHand, -0.4f, 0.6f, -0.8f);
                animator.move(handController, 0, 1, 0);
                animator.move(swingOffsetController, 0, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(4);
                animator.resetKeyframe(10);

                float swingFrame = swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 15) {
                    waist.rotateAngleX += 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    headJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legLeftJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legRightJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRightJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRightJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    armLeftJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeft2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeft2.rotateAngleY -= 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
                if (entity.getAnimationTick() > 15 && entity.getAnimationTick() <= 25) {
                    waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeftJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armLeftJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    armRightJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRight2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    armRight2.rotateAngleY += 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            }
        }

        if (entity.getAnimation() == EntityFrostmaw.ROAR_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.ROAR_ANIMATION);
            animator.startKeyframe(8);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, 0.3f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(4);

            animator.startKeyframe(5);
            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.3f, 0, 0);
            animator.rotate(jawJoint, 1.3f, 0, 0);
            animator.move(roarController, 1, 1, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(50);
            animator.resetKeyframe(8);
        }

        if (entity.getAnimation() == EntityFrostmaw.ICE_BREATH_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.ICE_BREATH_ANIMATION);
            animator.startKeyframe(10);
            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, 0.6f, 0, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(4);

            animator.startKeyframe(5);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, -0.9f, 0, 0);
            animator.rotate(jawJoint, 1.6f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.move(roarController, 1, 1, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(65);
            animator.resetKeyframe(7);
            iceCrystal.rotateAngleY += frame;
        }

        if (entity.getAnimation() == EntityFrostmaw.ICE_BALL_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.ICE_BALL_ANIMATION);

            animator.startKeyframe(20);
            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.1f, 0, 0);
            animator.move(headJoint, 0, 0, -4);
            animator.rotate(jawJoint, 1.4f, 0, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(8);

            animator.startKeyframe(4);
            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, 0.6f, 0, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);

            animator.startKeyframe(3);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, -0.9f, 0, 0);
            animator.rotate(jawJoint, 1.6f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(5);
            animator.resetKeyframe(7);
            iceCrystal.rotateAngleY += frame;
        }

        if (entity.getAnimation() == EntityFrostmaw.ACTIVATE_ANIMATION) {
            eyeLidLeft.showModel = false;
            eyeLidRight.showModel = false;
            animator.setAnimation(EntityFrostmaw.ACTIVATE_ANIMATION);

            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.7f, 0.8f, -3.3f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(5);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.7f, 0.8f, -3.3f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);

            animator.move(handController, 0, -0.8f, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(10);

            animator.startKeyframe(7);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.7f, 0.8f, -3.3f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);

            animator.move(handController, 0, -0.8f, 0);
            animator.rotate(armRightJoint, 0.1f, 0.2f, 0.8f);
            animator.move(armRightJoint, 0, -8, 0);
            animator.rotate(armRight1, -0.1f, 0.2f, 0.4f);
            animator.rotate(armRight2, 0f, 0.4f, 0f);
            animator.rotate(armRightJoint2, 0.3f, -0.5f, 0.6f);
            animator.rotate(rightHand, 0f, 0f, 0.6f);
            animator.rotate(waist, 0, 0.3f, 0);
            animator.rotate(legLeftJoint, 0, -0.3f, 0.2f);
            animator.rotate(legRightJoint, 0, -0.3f, 0.2f);
            animator.rotate(armLeftJoint, 0f, -0.7f, 0);
            animator.rotate(armLeft2, -0.15f, 0.35f, 0);
            animator.rotate(headJoint, -0.4f, 0.3f, 0);
            animator.move(roarController, 0, 1f, 0);
            animator.rotate(jawJoint, 1.5f, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(2);

            animator.startKeyframe(5);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.7f, 0.8f, -3.3f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);

            animator.move(handController, 0, 0f, 0);
            animator.rotate(armRightJoint, 0.1f, -0.1f, 0.1f);
            animator.move(armRightJoint, 0, 0, -6);
            animator.rotate(armRight1, -0.5f, 0.2f, 0.4f);
            animator.rotate(armRight2, -0.5f, 0.1f, 0.2f);
            animator.rotate(armRightJoint2, 0.3f, -0.5f, 0.6f);
            animator.rotate(rightHand, -0.2f, 0f, 0.6f);
            animator.rotate(waist, 0.2f, 0.3f, 0);
            animator.rotate(legLeftJoint, -0.1f, -0.2f, 0.4f);
            animator.rotate(legRightJoint, -0.1f, -0.3f, 0.4f);
            animator.rotate(armLeftJoint, 0f, -1.1f, -0.3f);
            animator.rotate(armLeft2, -0.35f, 0.35f, 0.3f);
            animator.rotate(headJoint, 0.1f, 0.5f, -0.1f);
            animator.rotate(jawJoint, 0f, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(7);

            animator.startKeyframe(15);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, 0.3f, 0, 0);
            animator.move(armLeftJoint, 0.15f, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(4);

            animator.startKeyframe(5);

            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.3f, 0, 0);
            animator.rotate(jawJoint, 1.3f, 0, 0);
            animator.move(roarController, 1, 1, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(50);

            animator.resetKeyframe(8);
        }
        if (entity.getAnimation() == EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION) {
            eyeLidLeft.showModel = false;
            eyeLidRight.showModel = false;
            animator.setAnimation(EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION);

            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.7f, 0.8f, -3.3f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(5);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.7f, 0.8f, -3.3f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);

            animator.move(handController, 0, -0.8f, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(10);

            animator.startKeyframe(15);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, 0.3f, 0, 0);
            animator.move(armLeftJoint, 0.15f, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(4);

            animator.startKeyframe(5);

            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.3f, 0, 0);
            animator.rotate(jawJoint, 1.3f, 0, 0);
            animator.move(roarController, 1, 1, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(50);

            animator.resetKeyframe(8);
        }
        if (entity.getAnimation() == EntityFrostmaw.DEACTIVATE_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.DEACTIVATE_ANIMATION);
            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, -0.9f);
            animator.move(root, 20, 0, 0);
            animator.rotate(chest, -0.2f, -0.2f, 0.1f);
            animator.rotate(headJoint, 0, 0, 0.3f);
            animator.rotate(armRightJoint, -0.2f, 0.5f, 0.8f);
            animator.move(armRightJoint, 0, -8, 0);
            animator.rotate(armRightJoint2, 0f, -0.5f, 0);
            animator.rotate(armRight2, 0, -0.2f, 0);
            animator.rotate(armLeftJoint, 1.3f, 0, 0);
            animator.rotate(armLeft1, 0.8f, 0, 0);
            animator.rotate(armLeftJoint2, -1.3f, 0, -0.3f);
            animator.rotate(leftHand, 0.65f, 0, 0);
            animator.move(handController, -0.8f, -0.8f, 0);
            animator.rotate(rightHand, 1.7f, -0.8f, 3.3f);
            animator.rotate(legLeftJoint, -0.7f, 0, 0);
            animator.rotate(legLeft1, 0.6f, 0, 0);
            animator.rotate(legLeft2, 0.6f, 0, -0.2f);
            animator.rotate(legLeftJoint, 0, -0.9f, -0.2f);
            animator.rotate(legLeft2, 0, -0.3f, 0);
            animator.rotate(legRightJoint, 0, 0.4f, 0);
            animator.rotate(legRight1, -1.1f, 0, -0.45f);
            animator.rotate(legRight2, 0.3f, 0, 0);
            animator.endKeyframe();

            animator.resetKeyframe(20);
        }
        if (entity.getAnimation() == EntityFrostmaw.LAND_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.LAND_ANIMATION);
            animator.startKeyframe(4);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();
            animator.resetKeyframe(8);
        }
        if (entity.getAnimation() == EntityFrostmaw.SLAM_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.SLAM_ANIMATION);
            animator.startKeyframe(6);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(2);
            animator.startKeyframe(16);
            animator.move(standUpController, 1, 0, 0);
            animator.rotate(waist, -0.6f, 0, 0);
            animator.rotate(headJoint, 1.2f, 0, 0);
            animator.move(headJoint, 0, 8, -12);
            animator.rotate(chest, -0.2f, 0, 0);
            animator.move(waist, 0, -3, 0);
            animator.rotate(legLeftJoint, 0.6f, 0, 0);
            animator.rotate(legRightJoint, 0.6f, 0, 0);
            animator.rotate(legLeft1, 0.3f, 0, 0);
            animator.rotate(legLeft2, -0.3f, 0, 0);
            animator.move(legLeftJoint, 2, 0, 0);
            animator.rotate(legRight1, 0.3f, 0, 0);
            animator.rotate(legRight2, -0.3f, 0, 0);
            animator.move(legRightJoint, -2, 0, 0);

            animator.rotate(armLeftJoint, 0.5f, 0, 0);
            animator.rotate(armRightJoint, 0.5f, 0, 0);
            animator.rotate(armLeft1, 0, 0, -0.1f);
            animator.rotate(armRight1, 0, 0, 0.1f);
            animator.rotate(armLeft2, 1f, -1.1f, 0);
            animator.rotate(armRight2, 1f, 1.1f, 0);
            animator.move(handController, 0.5f, 0.5f, 0);
            animator.rotate(leftHand, -0.3f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0);

            animator.endKeyframe();
            animator.setStaticKeyframe(50);

            animator.startKeyframe(7);
            animator.move(standUpController, 1, 0, 0);
            animator.rotate(waist, -0.6f, 0, 0);
            animator.rotate(headJoint, 1.2f, 0, 0);
            animator.move(headJoint, 0, 8, -12);
            animator.rotate(chest, -0.2f, 0, 0);
            animator.move(waist, 0, -3, 0);
            animator.rotate(legLeftJoint, 0.6f, 0, 0);
            animator.rotate(legRightJoint, 0.6f, 0, 0);
            animator.rotate(legLeft1, 0.3f, 0, 0);
            animator.rotate(legLeft2, -0.3f, 0, 0);
            animator.move(legLeftJoint, 2, 0, 0);
            animator.rotate(legRight1, 0.3f, 0, 0);
            animator.rotate(legRight2, -0.3f, 0, 0);
            animator.move(legRightJoint, -2, 0, 0);

            animator.rotate(armLeftJoint, 0.5f, 0, 0);
            animator.rotate(armRightJoint, 0.5f, 0, 0);
            animator.rotate(armLeft1, 0, 0, -0.1f);
            animator.rotate(armRight1, 0, 0, 0.1f);
            animator.rotate(armLeft2, 1f, -1.1f, 0);
            animator.rotate(armRight2, 1f, 1.1f, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(leftHand, -0.3f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0);

            animator.rotate(waist, -0.3f, 0, 0);
            animator.rotate(legLeftJoint, 0.3f, 0, 0);
            animator.rotate(legRightJoint, 0.3f, 0, 0);

            animator.rotate(armRightJoint, -2.2f, 0, 0);
            animator.rotate(armRight2, -0.6f, 0, -1f);
            animator.rotate(rightHand, -0.6f, 0, 0.4f);
            animator.rotate(armLeftJoint, -2.2f, 0, 0);
            animator.rotate(armLeft2, -0.6f, 0, 1f);
            animator.rotate(leftHand, -0.6f, 0, -0.4f);

            animator.endKeyframe();
            animator.setStaticKeyframe(4);
            animator.startKeyframe(4);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, -0.2f, 0, 0);
            animator.rotate(jawJoint, 0.5f, 0, 0);
            animator.move(handController, 0.9f, 0.9f, 0);
            animator.move(roarController, 0, 1, 0);

            animator.rotate(armRightJoint, -1f, 0, 0);
            animator.rotate(armRight1, 0, 1.2f, -0.3f);
            animator.rotate(armRight2, 2.2f, -0.3f, -0.8f);
            animator.rotate(rightHand, -1.6f, -0.15f, 0.3f);
            animator.move(armRightJoint, 0, 4, -16);
            animator.rotate(armLeftJoint, -1f, 0, 0);
            animator.rotate(armLeft1, 0, -1.2f, 0.3f);
            animator.rotate(armLeft2, 2.2f, 0.3f, 0.8f);
            animator.rotate(leftHand, -1.6f, 0.15f, -0.3f);
            animator.move(armLeftJoint, 0, 4, -16);
            animator.endKeyframe();
            animator.setStaticKeyframe(8);
            animator.resetKeyframe(16);
        }

        if (entity.getAnimation() == EntityFrostmaw.DODGE_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.DODGE_ANIMATION);
            animator.startKeyframe(5);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, -0.2f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(1);

            animator.startKeyframe(4);
            animator.rotate(waist, -0.1f, 0, 0);
            animator.rotate(legRightJoint, 0.1f, 0, 0);
            animator.rotate(legLeftJoint, 0.1f, 0, 0);
            animator.rotate(headJoint, 0.2f, 0, 0);
            animator.move(waist, 0, 0, 0);
            animator.rotate(legLeft1, 0.3f, 0, 0);
            animator.rotate(legLeft2, -0.3f, 0, 0);
            animator.move(legLeftJoint, 2, 0, 0);
            animator.rotate(legRight1, 0.3f, 0, 0);
            animator.rotate(legRight2, -0.3f, 0, 0);
            animator.move(legRightJoint, -2, 0, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }

        if (entity.getAnimation() == EntityFrostmaw.DIE_ANIMATION) {
            animator.setAnimation(EntityFrostmaw.DIE_ANIMATION);
            animator.startKeyframe(4);
            animator.rotate(waist, 0.2f, 0, 0);
            animator.rotate(legRightJoint, -0.2f, 0, 0);
            animator.rotate(legLeftJoint, -0.2f, 0, 0);
            animator.rotate(headJoint, 0.3f, 0, 0);

            animator.rotate(armLeftJoint, 0.15f, 0, 0);
            animator.move(armLeftJoint, 0, 2, 0);
            animator.rotate(armLeftJoint2, -0.6f, 0, 0);
            animator.rotate(leftHand, 0.3f, 0, 0.15f);
            animator.rotate(armRightJoint, 0.15f, 0, 0);
            animator.move(armRightJoint, 0, 2, 0);
            animator.rotate(armRightJoint2, -0.6f, 0, 0);
            animator.rotate(rightHand, 0.3f, 0, -0.15f);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);

            animator.startKeyframe(5);
            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.3f, 0, 0);
            animator.rotate(jawJoint, 1.3f, 0, 0);
            animator.move(roarController, 1, 1, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(22);

            animator.startKeyframe(3);
            animator.rotate(waist, -0.2f, 0, 0);
            animator.rotate(legRightJoint, 0.2f, 0, 0);
            animator.rotate(legLeftJoint, 0.2f, 0, 0);
            animator.rotate(headJoint, -0.3f, 0, 0);
            animator.rotate(jawJoint, 1.3f, 0, 0);
            animator.move(roarController, 0, 1, 0);

            animator.rotate(armLeftJoint, -0.4f, 0, 0);
            animator.rotate(armLeftJoint2, 0.9f, 0, 0);
            animator.rotate(leftHand, -0.3f, 0, -0.15f);
            animator.rotate(armRightJoint, -0.4f, 0, 0);
            animator.rotate(armRightJoint2, 0.9f, 0, 0);
            animator.rotate(rightHand, -0.3f, 0, 0.15f);
            animator.endKeyframe();
            animator.setStaticKeyframe(12);

            animator.startKeyframe(7);
            animator.rotate(root, 0, 0, 0.9f);
            animator.move(root, -20, 0, 0);
            animator.rotate(chest, 0.2f, 0.2f, -0.1f);
            animator.rotate(headJoint, 0, 0, -0.3f);
            animator.rotate(armRightJoint, 0.2f, -0.5f, -0.8f);
            animator.move(armRightJoint, 0, 8, 0);
            animator.rotate(armRightJoint2, 0f, 0.5f, 0);
            animator.rotate(armRight2, 0, 0.2f, 0);
            animator.rotate(armLeftJoint, -1.3f, 0, 0);
            animator.rotate(armLeft1, -0.8f, 0, 0);
            animator.rotate(armLeftJoint2, 1.3f, 0, 0.3f);
            animator.rotate(leftHand, -0.65f, 0, 0);
            animator.move(handController, 0.8f, 0.8f, 0);
            animator.rotate(rightHand, -1.5f, 0, -0.2f);
            animator.rotate(legLeftJoint, 0.7f, 0, 0);
            animator.rotate(legLeft1, -0.6f, 0, 0);
            animator.rotate(legLeft2, -0.6f, 0, 0.2f);
            animator.rotate(legLeftJoint, 0, 0.9f, 0.2f);
            animator.rotate(legLeft2, 0, 0.3f, 0);
            animator.rotate(legRightJoint, 0, -0.4f, 0);
            animator.rotate(legRight1, 1.1f, 0, 0.45f);
            animator.rotate(legRight2, -0.3f, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(40);

            if (entity.getAnimationTick() > 50) {
                eyeLidLeft.showModel = true;
                eyeLidRight.showModel = true;
            }
        }

//        limbSwing = 0.5f * (entity.ticksExisted + delta);
//        limbSwingAmount = 1f;
        float globalSpeed = 0.5f;
        float globalHeightQuad = 1.2f * (1 - standUpController.rotationPointX);
        float globalDegreeQuad = 0.8f * (1 - standUpController.rotationPointX);
        float frontDegree = 1.1f;
        float frontOffset = (float) (Math.PI/2);
        float globalHeightBi = 1f * standUpController.rotationPointX;
        float globalDegreeBi = 1f * standUpController.rotationPointX;

        float lookLimit = 50;
        if (headYaw > lookLimit) {
            headYaw = lookLimit;
        }
        if (headYaw < -lookLimit) {
            headYaw = -lookLimit;
        }

        if (entity.getAnimation() == EntityFrostmaw.ROAR_ANIMATION) {
            headYaw = headYaw / (frame);
            headPitch = headPitch / (frame);
        }
        waist.rotationPointZ += 10;

        if (entity.getActive()) {
            if (entity.getAnimation() != EntityFrostmaw.ACTIVATE_ANIMATION && entity.getAnimation() != EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION && entity.getAnimation() != EntityFrostmaw.DEACTIVATE_ANIMATION) {
                faceTarget(headYaw * (1 - standUpController.rotationPointX), headPitch, 1, headJoint);
                float yawAmount = headYaw / (180.0F / (float) Math.PI);
                headJoint.rotateAngleZ += yawAmount * standUpController.rotationPointX;

                //Walk
                //Quadrupedal
                bob(waist, globalSpeed, globalHeightQuad * 3f, false, limbSwing, limbSwingAmount);
                walk(waist, globalSpeed, globalHeightQuad * 0.12f, false, frontOffset, 0.08f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(headJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset + 0.4f, 0.08f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(legLeftJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(legRightJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(armLeftJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(armRightJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);

                swing(waist, 0.5f * globalSpeed, globalDegreeQuad * 0.2f, true, 0, 0, limbSwing, limbSwingAmount);
                swing(headJoint, 0.5f * globalSpeed, globalDegreeQuad * 0.2f, false, 0, 0, limbSwing, limbSwingAmount);

                flap(waist, 0.5f * globalSpeed, 0.15f * globalHeightQuad, false, 1, 0, limbSwing, limbSwingAmount);
                flap(legLeft1, 0.5f * globalSpeed, 0.15f * globalHeightQuad, true, 1, 0, limbSwing, limbSwingAmount);
                flap(legRight1, 0.5f * globalSpeed, 0.15f * globalHeightQuad, true, 1, 0, limbSwing, limbSwingAmount);
                flap(chest, 0.5f * globalSpeed, 0.15f * globalHeightQuad, true, 1, 0, limbSwing, limbSwingAmount);
                swing(legLeft1, 0.5F * globalSpeed, 0.2F * globalDegreeQuad, false, 0, 0.6F * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                swing(legRight1, 0.5F * globalSpeed, 0.2F * globalDegreeQuad, false, 0, -0.6F * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(legLeft1, 0.5f * globalSpeed, 0.7f * globalDegreeQuad, false, 0, 0.4f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(legRight1, 0.5f * globalSpeed, 0.7f * globalDegreeQuad, true, 0, -0.4f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(legLeft2, 0.5f * globalSpeed, 0.6f * globalDegreeQuad, false, -1.8f, 0.2f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(legRight2, 0.5f * globalSpeed, 0.6f * globalDegreeQuad, true, -1.8f, -0.2f * (1 - standUpController.rotationPointX), limbSwing, limbSwingAmount);
                walk(leftFoot, 0.5f * globalSpeed, 0.4f * globalDegreeQuad, false, -1.5f, 0.4f * globalDegreeQuad, limbSwing, limbSwingAmount);
                walk(rightFoot, 0.5f * globalSpeed, 0.4f * globalDegreeQuad, true, -1.5f, -0.4f * globalDegreeQuad, limbSwing, limbSwingAmount);

                swing(chest, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, false, frontOffset, 0, limbSwing, limbSwingAmount);
                swing(headJoint, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, true, frontOffset, 0, limbSwing, limbSwingAmount);
                swing(armLeft2, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, true, frontOffset, 0.4f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                swing(armRight2, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, true, frontOffset, -0.4f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                flap(chest, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, true, frontOffset + 1, 0, limbSwing, limbSwingAmount);
                flap(armLeftJoint, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, false, frontOffset + 1, 0, limbSwing, limbSwingAmount);
                flap(armRightJoint, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, false, frontOffset + 1, 0, limbSwing, limbSwingAmount);
                flap(headJoint, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, false, frontOffset + 1, 0, limbSwing, limbSwingAmount);

                walk(armLeftJoint, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, true, frontOffset, -0.7f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                walk(armRightJoint, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, false, frontOffset, 0.7f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                walk(armLeftJoint2, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, true, frontOffset + 2f, 0.6f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                walk(armRightJoint2, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, false, frontOffset + 2f, -0.6f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                walk(leftHand, 0.5f * globalSpeed, 0.5f * globalDegreeQuad * frontDegree, false, frontOffset + 1.5f, 0.05f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                walk(rightHand, 0.5f * globalSpeed, 0.5f * globalDegreeQuad * frontDegree, true, frontOffset + 1.5f, 0.05f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);

                //Bipedal
                flap(root, 0.5F * globalSpeed, 0.1f * globalHeightBi, true, -0.7f, 0, limbSwing, limbSwingAmount);
                bob(waist, globalSpeed, globalHeightBi * 3f, false, limbSwing, limbSwingAmount);
                walk(waist, globalSpeed, 0.05f * globalHeightBi, false, -1f, 0.1f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(legLeftJoint, globalSpeed, 0.05f * globalHeightBi, true, -1f, 0.1f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(legRightJoint, globalSpeed, 0.05f * globalHeightBi, true, -1f, 0.1f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(chest, globalSpeed, 0.05f * globalHeightBi, false, -2f, 0.05f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(headJoint, globalSpeed, 0.1f * globalHeightBi, true, -2f, -0.1f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                flap(chest, 0.5f * globalSpeed, 0.2f * globalHeightBi, false, -1, 0, limbSwing, limbSwingAmount);
                flap(headJoint, 0.5f * globalSpeed, -0.2f * globalHeightBi, false, -1, 0, limbSwing, limbSwingAmount);

                walk(armLeftJoint, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, false, frontOffset, 0.4f * globalDegreeBi, limbSwing, limbSwingAmount);
                walk(armLeftJoint2, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, true, frontOffset + 2f, 0.6f * globalDegreeBi, limbSwing, limbSwingAmount);
                walk(leftHand, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, true, frontOffset + 1.5f, 0.05f * globalDegreeBi, limbSwing, limbSwingAmount);

                walk(armRightJoint, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, true, frontOffset, -0.4f * globalDegreeBi, limbSwing, limbSwingAmount);
                walk(armRightJoint2, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, false, frontOffset + 2f, -0.6f * globalDegreeBi, limbSwing, limbSwingAmount);
                walk(rightHand, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, false, frontOffset + 1.5f, -0.05f * globalDegreeBi, limbSwing, limbSwingAmount);

                swing(legLeft1, 0.5F * globalSpeed, 0.2F * globalDegreeBi, false, 0, 0.6F * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                swing(legRight1, 0.5F * globalSpeed, 0.2F * globalDegreeBi, false, 0, -0.6F * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(legLeft1, 0.5f * globalSpeed, 0.7f * globalDegreeBi, false, 0, 0.4f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(legRight1, 0.5f * globalSpeed, 0.7f * globalDegreeBi, true, 0, -0.4f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(legLeft2, 0.5f * globalSpeed, 0.6f * globalDegreeBi, false, -1.8f, 0.2f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(legRight2, 0.5f * globalSpeed, 0.6f * globalDegreeBi, true, -1.8f, -0.2f * standUpController.rotationPointX, limbSwing, limbSwingAmount);
                walk(leftFoot, 0.5f * globalSpeed, 0.4f * globalDegreeBi, false, -1.5f, 0.4f * globalDegreeBi, limbSwing, limbSwingAmount);
                walk(rightFoot, 0.5f * globalSpeed, 0.4f * globalDegreeBi, true, -1.5f, -0.4f * globalDegreeBi, limbSwing, limbSwingAmount);

                //Idle
                if (!entity.isPotionActive(PotionHandler.FROZEN) && (entity.getAnimation() != EntityFrostmaw.SLAM_ANIMATION || entity.getAnimationTick() < 118) && entity.getAnimation() != EntityFrostmaw.DIE_ANIMATION) {
                    walk(waist, 0.08f, 0.05f, false, 0, 0, frame, 1);
                    walk(headJoint, 0.08f, 0.05f, true, 0.8f, 0, frame, 1);
                    walk(legRightJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    walk(legLeftJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    walk(armLeftJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    walk(armRightJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    walk(armLeftJoint2, 0.08f, 0.07f, true, 0, 0, frame, 1);
                    walk(armRightJoint2, 0.08f, 0.07f, true, 0, 0, frame, 1);
                    walk(leftHand, 0.08f, 0.07f, false, 0, 0, frame, 1);
                    walk(rightHand, 0.08f, 0.07f, false, 0, 0, frame, 1);
                    armLeftJoint.rotationPointZ += 1.8f * Math.cos(frame * 0.08f);
                    armRightJoint.rotationPointZ += 1.8f * Math.cos(frame * 0.08f);
                    armLeftJoint.rotationPointY -= 0.4f * Math.cos(frame * 0.08f);
                    armRightJoint.rotationPointY -= 0.4f * Math.cos(frame * 0.08f);
                }
            }
            if (entity.getAnimation() != EntityFrostmaw.DIE_ANIMATION) {
                eyeLidRight.showModel = false;
                eyeLidLeft.showModel = false;
            }
        }
        else if (entity.getAnimation() != EntityFrostmaw.ACTIVATE_ANIMATION && entity.getAnimation() != EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION) {
            eyeLidLeft.showModel = true;
            eyeLidRight.showModel = true;
            root.rotateAngleZ += 0.9f;
            root.rotationPointX -= 20;
            chest.rotateAngleZ -= 0.1f;
            chest.rotateAngleX += 0.2;
            chest.rotateAngleY += 0.2;
            headJoint.rotateAngleZ -= 0.3f;
            armRightJoint.rotateAngleZ -= 0.8f;
            armRightJoint.rotateAngleX += 0.2f;
            armRightJoint.rotateAngleY -= 0.5f;
            armRightJoint.rotationPointY += 8;
            armRight2.rotateAngleY += 0.2f;
            armLeftJoint.rotateAngleX -= 1.3f;
            armLeft1.rotateAngleX -= 0.8f;
            armLeftJoint2.rotateAngleX += 1.3f;
            armLeftJoint2.rotateAngleZ += 0.3f;
            armRightJoint2.rotateAngleY += 0.5f;
            leftHand.rotateAngleX -= 0.65f;
            handController.rotationPointX += 0.8f;
            rightHand.rotateAngleX -= 1.7f;
            rightHand.rotateAngleY += 0.8f;
            rightHand.rotateAngleZ -= 3.3f;
            handController.rotationPointY += 0.8f;
            legLeftJoint.rotateAngleX += 0.7f;
            legLeft1.rotateAngleX -= 0.6f;
            legLeft2.rotateAngleX -= 0.6f;
            legLeft2.rotateAngleZ += 0.2f;
            legLeftJoint.rotateAngleY += 0.9f;
            legLeftJoint.rotateAngleZ += 0.2f;
            legLeft2.rotateAngleY += 0.3f;
            legRightJoint.rotateAngleY -= 0.4f;
            legRight1.rotateAngleX += 1.1f;
            legRight1.rotateAngleZ += 0.45f;
            legRight2.rotateAngleX -= 0.3f;
//            legRightJoint.rotationPointZ -= 5f;
            chest.scaleChildren = false;
            float chestScale = (float) (1.05 + 0.05f * Math.cos(frame * 0.05f));
            chest.setScale(chestScale, chestScale, chestScale);
            backHair.setScale(chestScale, chestScale, chestScale);
            backHair.rotationPointZ -= (chestScale - 1.05) * 20f;
            headJoint.rotateAngleX += (float) (0.04f * Math.cos(frame * 0.05f - 1));
        }

        jawJoint.rotateAngleX += 0.08 * roarController.rotationPointX * Math.cos(2 * frame);
        headBack.setScale(1, 1 - roarController.rotationPointY, 1);

        rightFingersJoint.rotateAngleX -= handController.rotationPointY * Math.PI/2;
        rightFingers.rotateAngleX -= handController.rotationPointY * Math.PI/2;
        rightThumb.rotateAngleY += (float) (handController.rotationPointY * Math.PI);
        rightThumb.rotateAngleZ += handController.rotationPointY * 0.7f;
        rightThumb.rotationPointZ -= handController.rotationPointY * 6;

        leftFingersJoint.rotateAngleX -= handController.rotationPointX * Math.PI/2;
        leftFingers.rotateAngleX -= handController.rotationPointX * Math.PI/2;
        leftThumb.rotateAngleY -= (float) (handController.rotationPointX * Math.PI);
        leftThumb.rotateAngleZ -= handController.rotationPointX * 0.7f;
        leftThumb.rotationPointZ -= handController.rotationPointX * 6;

        iceCrystalJoint.rotateAngleX = -headJoint.rotateAngleX - chest.rotateAngleX - waist.rotateAngleX - root.rotateAngleX + 0.9f;
//        iceCrystal.rotateAngleY += (float)Math.PI/2;
        iceCrystal.rotationPointY += 2 * Math.cos(0.15f * frame) + 5;
        iceCrystal.setScale(2);
        iceCrystal.rotateAngleZ += (float)Math.PI;
        iceCrystal.rotationPointY -= 6 * (1 - Math.min(jawJoint.rotateAngleX - jawJoint.defaultRotationX, 1.0f));

        iceCrystalHand.rotationPointY -= 1.7f * Math.cos(0.1f * frame) - 1;
        iceCrystalHand.rotateAngleY -= frame * 0.05f;
        iceCrystalHand.rotateAngleZ += Math.PI;

        if (entity.getHasCrystal()) {
            iceCrystal.showModel = !(entity.getAnimation() == EntityFrostmaw.ACTIVATE_ANIMATION && entity.getAnimationTick() <= 28);

            iceCrystalHand.showModel = !entity.active;
        }
        else {
            iceCrystal.showModel = false;
            iceCrystalHand.showModel = false;
        }
    }
}