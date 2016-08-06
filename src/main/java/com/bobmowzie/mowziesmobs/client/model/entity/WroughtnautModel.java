package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class WroughtnautModel extends AdvancedModelBase {
    public AdvancedModelRenderer waist;
    public AdvancedModelRenderer groin;
    public AdvancedModelRenderer stomachJoint;
    public AdvancedModelRenderer groinJoint;
    public AdvancedModelRenderer stomach;
    public AdvancedModelRenderer chestJoint;
    public AdvancedModelRenderer chest;
    public AdvancedModelRenderer neck;
    public AdvancedModelRenderer shoulderRightJoint;
    public AdvancedModelRenderer shoulderLeftJoint;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer helmet;
    public AdvancedModelRenderer tuskRight1;
    public AdvancedModelRenderer tuskLeft1;
    public AdvancedModelRenderer hornRight1;
    public AdvancedModelRenderer hornLeft1;
    public AdvancedModelRenderer tuskRight2;
    public AdvancedModelRenderer tuskLeft2;
    public AdvancedModelRenderer hornRight2;
    public AdvancedModelRenderer hornLeft2;
    public AdvancedModelRenderer shoulderRight;
    public AdvancedModelRenderer upperArmRightJoint;
    public AdvancedModelRenderer upperArmRight;
    public AdvancedModelRenderer lowerArmRightJoint;
    public AdvancedModelRenderer elbowRightJoint;
    public AdvancedModelRenderer lowerArmRight;
    public AdvancedModelRenderer handRightJoint;
    public AdvancedModelRenderer handRight;
    public AdvancedModelRenderer axeBase;
    public AdvancedModelRenderer axeHandle;
    public AdvancedModelRenderer axeBladeRight;
    public AdvancedModelRenderer axeBladeLeft;
    public AdvancedModelRenderer axeBladeRight1;
    public AdvancedModelRenderer axeBladeRight2;
    public AdvancedModelRenderer axeBladeRight3;
    public AdvancedModelRenderer axeBladeLeft1;
    public AdvancedModelRenderer axeBladeLeft2;
    public AdvancedModelRenderer axeBladeLeft3;
    public AdvancedModelRenderer elbowRight;
    public AdvancedModelRenderer shoulderLeft;
    public AdvancedModelRenderer upperArmLeftJoint;
    public AdvancedModelRenderer upperArmLeft;
    public AdvancedModelRenderer lowerArmLeftJoint;
    public AdvancedModelRenderer elbowLeftJoint;
    public AdvancedModelRenderer lowerArmLeft;
    public AdvancedModelRenderer handLeftJoint;
    public AdvancedModelRenderer handLeft;
    public AdvancedModelRenderer elbowLeft;
    public AdvancedModelRenderer groinFront;
    public AdvancedModelRenderer groinBack;
    public AdvancedModelRenderer thighRightJoint;
    public AdvancedModelRenderer thighLeftJoint;
    public AdvancedModelRenderer thighRightJoint2;
    public AdvancedModelRenderer thighRight;
    public AdvancedModelRenderer calfRightJoint;
    public AdvancedModelRenderer kneeRight;
    public AdvancedModelRenderer calfRight;
    public AdvancedModelRenderer footRightJoint;
    public AdvancedModelRenderer footRight;
    public AdvancedModelRenderer thighLeftJoint2;
    public AdvancedModelRenderer thighLeft;
    public AdvancedModelRenderer calfLeftJoint;
    public AdvancedModelRenderer kneeLeft;
    public AdvancedModelRenderer calfLeft;
    public AdvancedModelRenderer footLeftJoint;
    public AdvancedModelRenderer footLeft;
    public AdvancedModelRenderer sword;
    public AdvancedModelRenderer swordJoint;
    public AdvancedModelRenderer rootBox;
    public AdvancedModelRenderer waistBendController;
    private ModelAnimator animator;

    public WroughtnautModel() {
        animator = ModelAnimator.create();
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.axeHandle = new AdvancedModelRenderer(this, 0, 22);
        this.axeHandle.setRotationPoint(3.0F, 0.0F, 1.0F);
        this.axeHandle.addBox(-1.5F, -44.0F, -1.5F, 3, 50, 3, 0.0F);
        this.stomach = new AdvancedModelRenderer(this, 80, 63);
        this.stomach.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stomach.addBox(-6.0F, -13.7F, -6.0F, 12, 17, 12, 0.0F);
        this.setRotateAngle(stomach, 0.0F, 0.7853981633974483F, 0.0F);
        this.elbowRight = new AdvancedModelRenderer(this, 70, 24);
        this.elbowRight.setRotationPoint(0.0F, 0.0F, -1.4F);
        this.elbowRight.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 5, 0.0F);
        this.calfRight = new AdvancedModelRenderer(this, 0, 75);
        this.calfRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.calfRight.addBox(-4.5F, 0.0F, -0.5F, 5, 12, 5, 0.0F);
        this.setRotateAngle(calfRight, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerArmRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.lowerArmRightJoint.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.lowerArmRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(lowerArmRightJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.kneeLeft = new AdvancedModelRenderer(this, 24, 80);
        this.kneeLeft.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.kneeLeft.addBox(-3.0F, -1.7F, -3.0F, 6, 4, 6, 0.0F);
        this.setRotateAngle(kneeLeft, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        this.elbowLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.elbowLeftJoint.setRotationPoint(15.0F, -1.6F, -1.7F);
        this.elbowLeftJoint.addBox(-3.0F, -3.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(elbowLeftJoint, -0.7853981633974483F, 0.0F, 0.0F);
        this.upperArmLeft = new AdvancedModelRenderer(this, 24, 40);
        this.upperArmLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperArmLeft.addBox(-5.0F, -4.0F, -4.0F, 20, 8, 8, 0.0F);
        this.setRotateAngle(upperArmLeft, 0.7853981633974483F, 0.0F, 0.0F);
        this.handLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.handLeftJoint.setRotationPoint(16.0F, 1.0F, 1.0F);
        this.handLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(handLeftJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.lowerArmLeft = new AdvancedModelRenderer(this, 86, 29);
        this.lowerArmLeft.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.lowerArmLeft.addBox(0.0F, -2.0F, -2.0F, 15, 6, 6, 0.0F);
        this.setRotateAngle(lowerArmLeft, -0.7853981633974483F, 0.0F, 0.0F);
        this.shoulderRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.shoulderRightJoint.setRotationPoint(10.0F, 4.0F, 14.9F);
        this.shoulderRightJoint.addBox(-4.0F, -7.0F, -5.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(shoulderRightJoint, -1.0471975511965976F, 0.0F, 0.0F);
        this.groinBack = new AdvancedModelRenderer(this, 0, 92);
        this.groinBack.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.groinBack.addBox(-5.0F, 0.0F, -1.0F, 10, 12, 2, 0.0F);
        this.setRotateAngle(groinBack, 0.17453292519943295F, 0.0F, 0.0F);
        this.thighLeftJoint2 = new AdvancedModelRenderer(this, 0, 0);
        this.thighLeftJoint2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighLeftJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(thighLeftJoint2, -0.8726646259971648F, 0.0F, 0.0F);
        this.helmet = new AdvancedModelRenderer(this, 32, 20);
        this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet.addBox(-4.0F, -10.0F, -4.0F, 8, 12, 8, 0.0F);
        this.lowerArmRight = new AdvancedModelRenderer(this, 86, 29);
        this.lowerArmRight.setRotationPoint(0.0F, 1.5F, 0.0F);
        this.lowerArmRight.addBox(0.0F, -4.0F, -4.0F, 15, 6, 6, 0.0F);
        this.setRotateAngle(lowerArmRight, -0.7853981633974483F, 0.0F, 0.0F);
        this.handLeft = new AdvancedModelRenderer(this, 98, 14);
        this.handLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handLeft.addBox(-2.0F, -4.0F, -2.0F, 8, 8, 7, 0.0F);
        this.setRotateAngle(handLeft, 0.0F, 0.7853981633974483F, 0.0F);
        this.calfLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.calfLeftJoint.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.calfLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(calfLeftJoint, 1.5707963267948966F, -0.7853981633974483F, 0.0F);
        this.chestJoint = new AdvancedModelRenderer(this, 0, 0);
        this.chestJoint.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.chestJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(chestJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.tuskRight2 = new AdvancedModelRenderer(this, 110, 97);
        this.tuskRight2.setRotationPoint(6.0F, 1.5F, 0.0F);
        this.tuskRight2.addBox(0.0F, -2.0F, -1.0F, 7, 2, 2, 0.0F);
        this.setRotateAngle(tuskRight2, 0.0F, 0.0F, -0.8726646259971648F);
        this.groinJoint = new AdvancedModelRenderer(this, 0, 0);
        this.groinJoint.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.groinJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(groinJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.upperArmRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.upperArmRightJoint.setRotationPoint(5.0F, 1.0F, 1.0F);
        this.upperArmRightJoint.addBox(0.0F, -4.0F, -4.0F, 0, 0, 0, 0.0F);
        this.axeBladeRight3 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeRight3.setRotationPoint(17.7F, 2.3F, -0.01F);
        this.axeBladeRight3.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeRight3, 0.0F, 0.0F, 2.6179938779914944F);
        this.footRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.footRightJoint.setRotationPoint(-2.0F, 11.0F, 2.0F);
        this.footRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(footRightJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.head = new AdvancedModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.0F, -9.0F, -3.0F, 6, 10, 6, 0.0F);
        this.setRotateAngle(head, 0.0F, 0.7853981633974483F, 0.0F);
        this.hornLeft1 = new AdvancedModelRenderer(this, 12, 17);
        this.hornLeft1.setRotationPoint(-2.5F, -8.05F, -3.0F);
        this.hornLeft1.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        this.setRotateAngle(hornLeft1, -0.3490658503988659F, 0.0F, 0.0F);
        this.thighRightJoint2 = new AdvancedModelRenderer(this, 0, 0);
        this.thighRightJoint2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighRightJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(thighRightJoint2, -0.8726646259971648F, 0.0F, 0.0F);
        this.thighRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.thighRightJoint.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.thighRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(thighRightJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.tuskLeft1 = new AdvancedModelRenderer(this, 13, 60);
        this.tuskLeft1.setRotationPoint(-2.5F, 0.5F, -3.0F);
        this.tuskLeft1.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        this.setRotateAngle(tuskLeft1, 0.4363323129985824F, 0.0F, 0.0F);
        this.kneeRight = new AdvancedModelRenderer(this, 24, 80);
        this.kneeRight.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.kneeRight.addBox(-3.0F, -1.7F, -3.0F, 6, 4, 6, 0.0F);
        this.setRotateAngle(kneeRight, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        this.axeBladeLeft1 = new AdvancedModelRenderer(this, 84, 0);
        this.axeBladeLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeBladeLeft1.addBox(0.0F, -4.5F, -1.0F, 10, 8, 2, 0.0F);
        this.waist = new AdvancedModelRenderer(this, 64, 41);
        this.waist.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.waist.addBox(-8.0F, 0.0F, -8.0F, 16, 6, 16, 0.0F);
        this.setRotateAngle(waist, 0.0F, 0.7853981633974483F, 0.0F);
        this.hornLeft2 = new AdvancedModelRenderer(this, 30, 0);
        this.hornLeft2.setRotationPoint(-1.0F, 1.5F, -8.0F);
        this.hornLeft2.addBox(0.0F, -2.0F, -11.0F, 2, 2, 11, 0.0F);
        this.setRotateAngle(hornLeft2, -1.2217304763960306F, 0.0F, 0.0F);
        this.shoulderLeft = new AdvancedModelRenderer(this, 21, 56);
        this.shoulderLeft.mirror = true;
        this.shoulderLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.shoulderLeft.addBox(-4.0F, -7.0F, -7.5F, 15, 10, 13, 0.0F);
        this.setRotateAngle(shoulderLeft, 0.0F, 3.141592653589793F, 0.0F);
        this.axeBladeRight = new AdvancedModelRenderer(this, 0, 0);
        this.axeBladeRight.setRotationPoint(0.0F, -37.0F, 0.0F);
        this.axeBladeRight.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(axeBladeRight, 0.0F, -0.7853981633974483F, 0.0F);
        this.calfLeft = new AdvancedModelRenderer(this, 0, 75);
        this.calfLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.calfLeft.addBox(-4.5F, 0.0F, -0.5F, 5, 12, 5, 0.0F);
        this.setRotateAngle(calfLeft, 0.0F, 0.7853981633974483F, 0.0F);
        this.elbowRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.elbowRightJoint.setRotationPoint(15.0F, 1.6F, 1.7F);
        this.elbowRightJoint.addBox(-3.0F, -3.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(elbowRightJoint, -0.7853981633974483F, 0.0F, 0.0F);
        this.axeBladeLeft = new AdvancedModelRenderer(this, 0, 0);
        this.axeBladeLeft.setRotationPoint(0.0F, -37.0F, 0.0F);
        this.axeBladeLeft.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(axeBladeLeft, 0.0F, -3.9269908169872414F, 0.0F);
        this.thighRight = new AdvancedModelRenderer(this, 26, 90);
        this.thighRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighRight.addBox(-3.5F, 0.0F, -3.5F, 7, 13, 7, 0.0F);
        this.setRotateAngle(thighRight, 0.0F, 0.7853981633974483F, 0.0F);
        this.shoulderRight = new AdvancedModelRenderer(this, 21, 56);
        this.shoulderRight.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.shoulderRight.addBox(-4.0F, -7.0F, -5.5F, 15, 10, 13, 0.0F);
        this.axeBladeRight1 = new AdvancedModelRenderer(this, 84, 0);
        this.axeBladeRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeBladeRight1.addBox(0.0F, -4.5F, -1.0F, 10, 8, 2, 0.0F);
        this.hornRight2 = new AdvancedModelRenderer(this, 16, 44);
        this.hornRight2.setRotationPoint(8.0F, 1.5F, 0.0F);
        this.hornRight2.addBox(0.0F, -2.0F, -1.0F, 6, 2, 2, 0.0F);
        this.setRotateAngle(hornRight2, 0.0F, 0.0F, -1.2217304763960306F);
        this.thighLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.thighLeftJoint.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.thighLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(thighLeftJoint, 0.0F, 0.7853981633974483F, 0.0F);
        this.axeBladeLeft3 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeLeft3.setRotationPoint(17.7F, 2.3F, -0.01F);
        this.axeBladeLeft3.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.01F);
        this.setRotateAngle(axeBladeLeft3, 0.0F, 0.0F, 2.6179938779914944F);
        this.footLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.footLeftJoint.setRotationPoint(-2.0F, 11.0F, 2.0F);
        this.footLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(footLeftJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.chest = new AdvancedModelRenderer(this, 36, 92);
        this.chest.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.chest.addBox(-14.0F, 0.0F, 0.0F, 28, 18, 18, 0.0F);
        this.setRotateAngle(chest, 0.7853981633974483F, 0.0F, 0.0F);
        this.tuskLeft2 = new AdvancedModelRenderer(this, 110, 101);
        this.tuskLeft2.setRotationPoint(-1.0F, 1.5F, -6.0F);
        this.tuskLeft2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(tuskLeft2, -0.8726646259971648F, 0.0F, 0.0F);
        this.upperArmLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.upperArmLeftJoint.setRotationPoint(5.0F, 1.0F, -1.0F);
        this.upperArmLeftJoint.addBox(0.0F, -4.0F, -4.0F, 0, 0, 0, 0.0F);
        this.axeBase = new AdvancedModelRenderer(this, 0, 0);
        this.axeBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.handRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.handRightJoint.setRotationPoint(16.0F, -1.0F, -1.0F);
        this.handRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(handRightJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.footLeft = new AdvancedModelRenderer(this, 48, 79);
        this.footLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.footLeft.addBox(-3.0F, 0.0F, -8.0F, 6, 3, 10, 0.0F);
        this.setRotateAngle(footLeft, -0.6981317007977318F, 0.0F, 0.0F);
        this.handRight = new AdvancedModelRenderer(this, 98, 14);
        this.handRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handRight.addBox(-2.0F, -4.0F, -2.0F, 8, 8, 7, 0.0F);
        this.setRotateAngle(handRight, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerArmLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.lowerArmLeftJoint.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.lowerArmLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(lowerArmLeftJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.hornRight1 = new AdvancedModelRenderer(this, 34, 13);
        this.hornRight1.setRotationPoint(3.0F, -8.05F, 2.5F);
        this.hornRight1.addBox(0.0F, -1.5F, -1.5F, 8, 3, 3, 0.0F);
        this.setRotateAngle(hornRight1, 0.0F, 0.0F, -0.3490658503988659F);
        this.tuskRight1 = new AdvancedModelRenderer(this, 64, 63);
        this.tuskRight1.setRotationPoint(3.0F, 0.5F, 2.5F);
        this.tuskRight1.addBox(0.0F, -1.5F, -1.5F, 6, 3, 3, 0.0F);
        this.setRotateAngle(tuskRight1, 0.0F, 0.0F, 0.4363323129985824F);
        this.footRight = new AdvancedModelRenderer(this, 48, 79);
        this.footRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.footRight.addBox(-3.0F, 0.0F, -8.0F, 6, 3, 10, 0.0F);
        this.setRotateAngle(footRight, -0.6981317007977318F, 0.0F, 0.0F);
        this.thighLeft = new AdvancedModelRenderer(this, 26, 90);
        this.thighLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighLeft.addBox(-3.5F, 0.0F, -3.5F, 7, 13, 7, 0.0F);
        this.setRotateAngle(thighLeft, 0.0F, 0.7853981633974483F, 0.0F);
        this.groinFront = new AdvancedModelRenderer(this, 0, 92);
        this.groinFront.setRotationPoint(0.0F, 0.0F, -7.0F);
        this.groinFront.addBox(-5.0F, 0.0F, -1.0F, 10, 12, 2, 0.0F);
        this.setRotateAngle(groinFront, -0.17453292519943295F, 0.0F, 0.0F);
        this.elbowLeft = new AdvancedModelRenderer(this, 70, 24);
        this.elbowLeft.setRotationPoint(0.0F, 0.0F, -3.6F);
        this.elbowLeft.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 5, 0.0F);
        this.axeBladeRight2 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeRight2.mirror = true;
        this.axeBladeRight2.setRotationPoint(17.7F, -3.2F, 0.01F);
        this.axeBladeRight2.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeRight2, 0.0F, 0.0F, 0.5235987755982988F);
        this.shoulderLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.shoulderLeftJoint.setRotationPoint(-10.0F, 4.0F, 14.9F);
        this.shoulderLeftJoint.addBox(-4.0F, -7.0F, -5.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(shoulderLeftJoint, -1.0471975511965976F, 0.0F, 0.0F);
        this.stomachJoint = new AdvancedModelRenderer(this, 0, 0);
        this.stomachJoint.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stomachJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(stomachJoint, 0.2617993877991494F, -0.7853981633974483F, 0.0F);
        this.axeBladeLeft2 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeLeft2.mirror = true;
        this.axeBladeLeft2.setRotationPoint(17.7F, -3.2F, 0.01F);
        this.axeBladeLeft2.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeLeft2, 0.0F, 0.0F, 0.5235987755982988F);
        this.calfRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.calfRightJoint.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.calfRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(calfRightJoint, 1.5707963267948966F, -0.7853981633974483F, 0.0F);
        this.upperArmRight = new AdvancedModelRenderer(this, 24, 40);
        this.upperArmRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperArmRight.addBox(-5.0F, -4.0F, -4.0F, 20, 8, 8, 0.0F);
        this.setRotateAngle(upperArmRight, 0.7853981633974483F, 0.0F, 0.0F);
        this.groin = new AdvancedModelRenderer(this, 0, 106);
        this.groin.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.groin.addBox(-3.0F, -5.5F, -5.5F, 6, 11, 11, 0.0F);
        this.setRotateAngle(groin, -0.7853981633974483F, 0.0F, 0.0F);
        this.neck = new AdvancedModelRenderer(this, 0, 0);
        this.neck.setRotationPoint(0.0F, -1.4F, 15.1F);
        this.neck.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(neck, -1.0471975511965976F, 0.0F, 0.0F);
        this.waistBendController = new AdvancedModelRenderer(this, 0, 0);
        this.waistBendController.setRotationPoint(0.0F, 0F, 0F);
        this.waistBendController.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(waistBendController, 0.0F, 0.0F, 0.0F);
        this.swordJoint = new AdvancedModelRenderer(this, 0, 0);
        this.swordJoint.setRotationPoint(0F, -3F, 10F);
        this.swordJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(swordJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.sword = new AdvancedModelRenderer(this, 82, 10);
        this.sword.setRotationPoint(0F, 0F, 0F);
//        this.sword.addBox(-11F, 0F, -11F, 11, 11, 0, 0.0F);
        //sword.add3DTexture(-11f, 0, -11f, 11, 11); todo
        this.setRotateAngle(sword, 0.0F, 0F, 0.0F);
        this.rootBox = new AdvancedModelRenderer(this, 0, 0);
        this.rootBox.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.rootBox.addBox(0F, 0F, 0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(rootBox, 0.0F, 0F, 0.0F);

        rootBox.addChild(waist);
        this.waist.addChild(this.groinJoint);
        this.upperArmRightJoint.addChild(this.upperArmRight);
        this.helmet.addChild(this.tuskRight1);
        this.shoulderRight.addChild(this.upperArmRightJoint);
        this.axeBase.addChild(this.axeHandle);
        this.footRightJoint.addChild(this.footRight);
        this.head.addChild(this.helmet);
        this.calfRight.addChild(this.footRightJoint);
        this.axeBladeLeft.addChild(this.axeBladeLeft1);
        this.thighRightJoint.addChild(this.thighRightJoint2);
        this.axeBladeRight.addChild(this.axeBladeRight2);
        this.elbowLeftJoint.addChild(this.elbowLeft);
        this.lowerArmRight.addChild(this.handRightJoint);
        this.upperArmLeft.addChild(this.elbowLeftJoint);
        this.lowerArmLeft.addChild(this.handLeftJoint);
        this.axeBladeLeft.addChild(this.axeBladeLeft3);
        this.axeBladeLeft.addChild(this.axeBladeLeft2);
        this.axeHandle.addChild(this.axeBladeLeft);
        this.shoulderLeftJoint.addChild(this.shoulderLeft);
        this.thighLeftJoint.addChild(this.thighLeftJoint2);
        this.upperArmRight.addChild(this.lowerArmRightJoint);
        this.chest.addChild(this.shoulderRightJoint);
        this.upperArmLeftJoint.addChild(this.upperArmLeft);
        this.calfLeft.addChild(this.footLeftJoint);
        this.handLeftJoint.addChild(this.handLeft);
        this.footLeftJoint.addChild(this.footLeft);
        this.thighLeft.addChild(this.calfLeftJoint);
        this.tuskLeft1.addChild(this.tuskLeft2);
        this.stomach.addChild(this.chestJoint);
        this.groinJoint.addChild(this.thighLeftJoint);
        this.handRight.addChild(this.axeBase);
        this.chestJoint.addChild(this.chest);
        this.calfLeftJoint.addChild(this.calfLeft);
        this.chest.addChild(this.shoulderLeftJoint);
        this.elbowRightJoint.addChild(this.elbowRight);
        this.thighRightJoint2.addChild(this.thighRight);
        this.groinJoint.addChild(this.groinFront);
        this.tuskRight1.addChild(this.tuskRight2);
        this.handRightJoint.addChild(this.handRight);
        this.helmet.addChild(this.hornLeft1);
        this.shoulderRightJoint.addChild(this.shoulderRight);
        this.stomachJoint.addChild(this.stomach);
        this.axeBladeRight.addChild(this.axeBladeRight3);
        this.lowerArmLeftJoint.addChild(this.lowerArmLeft);
        this.calfRightJoint.addChild(this.calfRight);
        this.groinJoint.addChild(this.groinBack);
        this.hornLeft1.addChild(this.hornLeft2);
        this.thighRight.addChild(this.calfRightJoint);
        this.groinJoint.addChild(this.thighRightJoint);
        this.helmet.addChild(this.tuskLeft1);
        this.thighRight.addChild(this.kneeRight);
        this.thighLeft.addChild(this.kneeLeft);
        this.thighLeftJoint2.addChild(this.thighLeft);
        this.chest.addChild(this.neck);
        this.neck.addChild(this.head);
        this.upperArmRight.addChild(this.elbowRightJoint);
        this.hornRight1.addChild(this.hornRight2);
        this.lowerArmRightJoint.addChild(this.lowerArmRight);
        this.waist.addChild(this.stomachJoint);
        this.axeHandle.addChild(this.axeBladeRight);
        this.shoulderLeft.addChild(this.upperArmLeftJoint);
        this.helmet.addChild(this.hornRight1);
        this.axeBladeRight.addChild(this.axeBladeRight1);
        this.upperArmLeft.addChild(this.lowerArmLeftJoint);
        groin.addChild(groinJoint);
        stomach.addChild(swordJoint);
        swordJoint.addChild(sword);

        //Corrections
        groin.rotateAngleY -= 45 * Math.PI / 180;

        updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate(f, f1, f2, f3, f4, f5, entity);
        this.rootBox.render(f5);
    }

    public void setRotateAngle(AdvancedModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityWroughtnaut entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        resetToDefaultPose();
        if (entity.getAnimation() != EntityWroughtnaut.ACTIVATE_ANIMATION && entity.getAnimation() != EntityWroughtnaut.DEACTIVATE_ANIMATION) {
            if (entity.isActive()) {
                shoulderLeft.rotateAngleZ -= 0.4;
                shoulderRight.rotateAngleZ += 0.35;
                shoulderLeft.rotateAngleY -= 0.3;
                shoulderRight.rotateAngleY += 0.2;
                upperArmLeft.rotateAngleZ += 0.5;
                upperArmRight.rotateAngleZ += 0.5;
                upperArmLeft.rotateAngleY -= 0.5;
                upperArmRight.rotateAngleY += 0.3;
                lowerArmLeft.rotateAngleZ += 1.5;
                lowerArmRight.rotateAngleZ -= 1.3;
                lowerArmLeft.rotateAngleY += 0.3;
                lowerArmRight.rotateAngleY += 0.7;
                handRight.rotateAngleZ += 0.3;
                handRight.rotateAngleX -= 0.2;
                handLeftJoint.rotateAngleX += 1.6;
                handLeftJoint.rotateAngleZ -= 0.5;
                axeHandle.rotateAngleY += 0.8;

                head.rotateAngleY += f3 / (180f / (float) Math.PI);
                neck.rotateAngleX += f4 / (180f / (float) Math.PI);
            } else {
                shoulderLeft.rotateAngleZ -= 0.4;
                shoulderRight.rotateAngleZ += 0.4;
                shoulderLeft.rotateAngleY -= 0.4;
                shoulderRight.rotateAngleY += 0.4;
                upperArmLeft.rotateAngleZ += 0.5;
                upperArmRight.rotateAngleZ += 0.5;
                upperArmLeft.rotateAngleY -= 0.5;
                upperArmRight.rotateAngleY += 0.5;
                lowerArmLeft.rotateAngleZ += 1.5;
                lowerArmRight.rotateAngleZ -= 1.5;
                lowerArmLeft.rotateAngleY += 1;
                lowerArmRight.rotateAngleY += 1;
                axeBase.rotationPointY += 30;
                axeBase.rotationPointZ += 3.5;
                axeBase.rotateAngleY += 0.35;
                handRight.rotateAngleX -= 0.9;
                handRightJoint.rotateAngleZ -= 0.88;
                handRightJoint.rotateAngleX -= 0.2;
                handLeftJoint.rotateAngleY -= 0.3;
                handLeftJoint.rotateAngleX += 0.8;
                neck.rotateAngleX += 0.5;
                rootBox.rotationPointY -= 5;
                thighRightJoint.rotateAngleX += 0.35;
                thighRightJoint.rotateAngleY += 0.5;
                thighLeftJoint.rotateAngleX += 0.35;
                thighLeftJoint.rotateAngleY -= 0.5;
                calfRightJoint.rotateAngleX -= 0.6;
                calfLeftJoint.rotateAngleX -= 0.6;
                footLeft.rotateAngleX += 0.25;
                footRight.rotateAngleX += 0.25;
            }
        }

        float frame = entity.frame + LLibrary.PROXY.getPartialTicks();
        f = (float) ((4 * Math.PI * frame - 30 * MathHelper.sin((float) (0.1 * Math.PI * (frame - 9))) - 27 * Math.PI) / (4 * Math.PI)) + 5f;
        f1 = (float) Math.pow(MathHelper.sin((float) (entity.walkAnim.getTimer() * Math.PI * 0.05)), 2);

        float globalSpeed = (float) (Math.PI * 0.05);
        float globalDegree = 0.8F;
        float height = 2F;

        //groinJoint.rotationPointY -= 1 * f1;
        waist.rotationPointZ -= f1 * 3f * Math.pow(Math.sin(globalSpeed * (frame - 13)), 2);
        bob(waist, 2F * globalSpeed, 1 * height, false, f, f1);
        swing(waist, 1F * globalSpeed, 0.3F * globalDegree, false, 0, 0, f, f1);
        swing(stomachJoint, 1F * globalSpeed, 0.6F * globalDegree, true, 0, 0, f, f1);
        swing(head, 1F * globalSpeed, 0.3F * globalDegree, false, 0, 0, f, f1);

        swing(thighLeftJoint, 1F * globalSpeed, 0.4F * globalDegree, true, 0, 0.5F, f, f1);
        walk(thighLeftJoint, 1F * globalSpeed, 0.4F * globalDegree, false, 0, 0.3F * globalDegree, f, f1);
        walk(calfLeftJoint, 1F * globalSpeed, 0.5F * globalDegree, false, -2.2F, 0.1F * globalDegree, f, f1);
        walk(footLeftJoint, 1F * globalSpeed, 0.4F * globalDegree, false, -2.1F, 0.26F * globalDegree, f, f1);

        swing(thighRightJoint, 1F * globalSpeed, 0.4F * globalDegree, true, 0, -0.5F, f, f1);
        walk(thighRightJoint, 1F * globalSpeed, 0.4F * globalDegree, true, 0, -0.3F * globalDegree, f, f1);
        walk(calfRightJoint, 1F * globalSpeed, 0.5F * globalDegree, true, -2.2F, -0.1F * globalDegree, f, f1);
        walk(footRightJoint, 1F * globalSpeed, 0.4F * globalDegree, true, -2.1F, -0.26F * globalDegree, f, f1);

        walk(groinFront, 2F * globalSpeed, 0.2F * 0.8F, true, -0.5F, 0.1F, f, f1);
        walk(groinBack, 2F * globalSpeed, 0.2F * 0.8F, false, -0.5F, 0.1F, f, f1);
        walk(neck, 2F * globalSpeed, 0.1F * 0.8F, true, -0.5F, 0.1F, f, f1);

        flap(shoulderLeft, 2F * globalSpeed, 0.05F * 0.8F, false, -0.5F, 0F, f, f1);
        flap(shoulderRight, 2F * globalSpeed, 0.05F * 0.8F, true, -0.5F, 0F, f, f1);
        swing(shoulderLeft, 1F * globalSpeed, 0.1F * 0.8F, true, 0F, -0.3F, f, f1);
        swing(shoulderRight, 1F * globalSpeed, 0.1F * 0.8F, true, 0F, -0.3F, f, f1);
        flap(upperArmLeftJoint, 2F * globalSpeed, 0.05F * 0.8F, true, -1F, 0F, f, f1);
        flap(upperArmRightJoint, 2F * globalSpeed, 0.05F * 0.8F, true, -1F, 0F, f, f1);
        flap(handLeft, 2 * globalSpeed, 0.2F, false, -0.5F, 0.5F, f, f1);
        walk(lowerArmLeftJoint, 2 * globalSpeed, 0.1F, true, -0.5F, 0, f, f1);
        walk(handLeft, 2 * globalSpeed, 0.2F, false, -0.5F, 0.6F, f, f1);
        lowerArmLeftJoint.rotateAngleY -= 0.65F * f1;

        sword.rotationPointZ += 4;
        sword.rotationPointX -= 5;
        sword.rotationPointY -= 3;
        swordJoint.rotationPointX += 2;
        swordJoint.rotationPointY -= 5;
        swordJoint.rotateAngleY += 1.3;
        swordJoint.rotateAngleX -= 0.5;
        swordJoint.rotateAngleZ -= 0.5;

        sword.rotateAngleY += Math.PI / 2;
//        sword.rotationPointX += 10;
        sword.rotationPointZ -= 22;
        sword.rotationPointY += 10;
    }

    public void animate(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        animator.update((EntityWroughtnaut) entity);
        EntityWroughtnaut entityWroughtnaut = (EntityWroughtnaut) entity;
        setRotationAngles(f, f1, f2, f3, f4, f5, entityWroughtnaut);

        if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) {
            if (!entityWroughtnaut.swingDirection) {
                animator.setAnimation(EntityWroughtnaut.ATTACK_ANIMATION);
                animator.setStaticKeyframe(6);
                animator.startKeyframe(15);
                animator.rotate(stomachJoint, -0.2F, 0.5F, 0);
                animator.rotate(waist, 0, 0.5F, -0.2F);
                animator.move(waist, -3, 1F, 1);
                animator.rotate(head, 0F, -0.8F, 0);
                animator.rotate(neck, 0F, 0F, 0);

                animator.rotate(shoulderRight, 0, 0.1F, 0);
                animator.rotate(upperArmRightJoint, -0.5F, 0.2F, 0);
                animator.rotate(lowerArmRightJoint, 0.2F, 0.3F, 0);
                animator.rotate(handRight, -0.5F, 0.3F, 0.1F);
                animator.rotate(axeHandle, 0, 0.5F, 0);

                animator.rotate(shoulderLeft, 0, 0F, 0);
                animator.rotate(upperArmLeftJoint, 0.2F, 0F, 0);
                animator.rotate(lowerArmLeftJoint, 0.1F, -0.3F, 0);
                animator.rotate(handLeft, 0F, 0.5F, -0.3F);

                animator.rotate(thighRightJoint, -0.7F, 0, 0);
                animator.rotate(thighRightJoint2, 0F, 0.2F, 0.5F);
                animator.rotate(calfRightJoint, 0.3F, 0, 0);

                animator.rotate(thighLeftJoint, -0.2F, 0, 0);
                animator.rotate(thighLeftJoint2, 0F, -0.5F, -0.05F);
                animator.rotate(calfLeftJoint, 0.1F, 0, 0);
                animator.rotate(footLeftJoint, -0.1F, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(5);

                animator.startKeyframe(6);
                animator.rotate(stomachJoint, 0.3F, -1.3F, 0);
                animator.rotate(waist, 0, 0.5F, 0F);
                animator.move(waist, 2, 2, -7);
                animator.rotate(head, 0F, 0.8F, 0);
                animator.move(waistBendController, 7, 0, 0);

                animator.rotate(shoulderRight, 0, -0.6F, 0);
                animator.rotate(upperArmRightJoint, -0.8F, -0.9F, 0);
                animator.rotate(lowerArmRightJoint, 0.4F, 0.9F, 0F);
                animator.rotate(handRight, -0.8F, 0F, 0F);
                animator.rotate(axeHandle, 0, 1.2F, 0);

                animator.rotate(shoulderLeft, 0, -0.7F, 0);
                animator.move(shoulderLeft, 1, 0F, 0);
                animator.rotate(upperArmLeftJoint, 0.2F, -0.5F, 0);
                animator.move(upperArmLeft, 5F, 2F, 5F);
                animator.rotate(lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                animator.move(lowerArmLeft, 0F, 2F, 0F);
                animator.rotate(handLeft, 2F, -0.3F, 0.5F);
                animator.rotate(handLeftJoint, 0F, -0.5F, -0.5F);

                animator.rotate(thighRightJoint, -0.4F, 0, 0);
                animator.rotate(thighRightJoint2, 0F, 0.2F, 0);
                animator.rotate(calfRightJoint, -0.1F, 0, 0);
                animator.rotate(footRightJoint, 0.5F, 0, 0);

                animator.rotate(thighLeftJoint, 0F, 0, 0);
                animator.rotate(thighLeftJoint2, 0F, -0.3F, 0.5F);
                animator.rotate(calfLeftJoint, -0.1F, 0, 0);
                animator.rotate(footLeftJoint, 0F, 0, 0);

                animator.endKeyframe();
                animator.setStaticKeyframe(8);
                animator.resetKeyframe(10);
                float frame = waistBendController.rotationPointX;
                if (entityWroughtnaut.getAnimationTick() <= 33) {
                    stomachJoint.rotateAngleX += 0.06 * -frame * (frame - 7);
                    neck.rotateAngleX -= 0.06 * -frame * (frame - 7);
                }
            } else {
                animator.setAnimation(EntityWroughtnaut.ATTACK_ANIMATION);
                animator.setStaticKeyframe(6);
                animator.startKeyframe(15);
                animator.rotate(stomachJoint, 0.2F, -0.5F, 0);
                animator.rotate(waist, 0, -0.5F, 0.2F);
                animator.move(waist, 3, 1F, -1);
                animator.rotate(head, 0F, 0.8F, 0);
                animator.rotate(neck, 0F, 0F, 0);

                animator.rotate(shoulderRight, 0, -0.6F, 0);
                animator.rotate(upperArmRightJoint, -0.8F, -0.9F, 0);
                animator.rotate(lowerArmRightJoint, 0.4F, 0.9F, 0F);
                animator.rotate(handRight, -0.8F, 0F, 0F);
                animator.rotate(axeHandle, 0, 1.2F, 0);

                animator.rotate(shoulderLeft, 0, -0.7F, 0);
                animator.move(shoulderLeft, 1, 0F, 0);
                animator.rotate(upperArmLeftJoint, 0.2F, -0.5F, 0);
                animator.move(upperArmLeft, 5F, 2F, 5F);
                animator.rotate(lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                animator.move(lowerArmLeft, 0F, 3F, 0F);
                animator.rotate(handLeft, 2F, -0.3F, 0.5F);
                animator.rotate(handLeftJoint, 0F, -0.5F, -0.5F);

                animator.rotate(thighLeftJoint, -0.7F, 0, 0);
                animator.rotate(thighLeftJoint2, 0F, -0.2F, -0.5F);
                animator.rotate(calfLeftJoint, 0.3F, 0, 0);

                animator.rotate(thighRightJoint, -0.2F, 0, 0);
                animator.rotate(thighRightJoint2, 0F, 0.5F, 0.05F);
                animator.rotate(calfRightJoint, 0.1F, 0, 0);
                animator.rotate(footRightJoint, -0.1F, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(5);

                animator.startKeyframe(6);
                animator.rotate(stomachJoint, 0.3F, 1.3F, 0);
                animator.rotate(waist, 0, -0.5F, 0F);
                animator.move(waist, -2, 2, -7);
                animator.rotate(head, 0F, -0.8F, 0);
                animator.move(waistBendController, 7, 0, 0);

                animator.rotate(shoulderRight, 0, 0.1F, 0);
                animator.rotate(upperArmRightJoint, -0.5F, 0.2F, 0);
                animator.rotate(lowerArmRightJoint, 0.2F, 0.3F, 0);
                animator.rotate(handRight, -0.5F, 0.3F, 0.1F);
                animator.rotate(axeHandle, 0, 0.5F, 0);

                animator.rotate(shoulderLeft, 0, 0F, 0);
                animator.rotate(upperArmLeftJoint, 0.2F, 0F, 0);
                animator.rotate(lowerArmLeftJoint, 0.1F, -0.3F, 0);
                animator.rotate(handLeft, 0F, 0.5F, -0.3F);

                animator.rotate(thighLeftJoint, -0.4F, 0, 0);
                animator.rotate(thighLeftJoint2, 0F, -0.2F, 0);
                animator.rotate(calfLeftJoint, -0.1F, 0, 0);
                animator.rotate(footLeftJoint, 0.5F, 0, 0);

                animator.rotate(thighRightJoint, 0F, 0, 0);
                animator.rotate(thighRightJoint2, 0F, 0.3F, -0.5F);
                animator.rotate(calfRightJoint, -0.1F, 0, 0);
                animator.rotate(footRightJoint, 0F, 0, 0);

                animator.endKeyframe();
                animator.setStaticKeyframe(8);
                animator.resetKeyframe(10);
                float frame = waistBendController.rotationPointX;
                if (entityWroughtnaut.getAnimationTick() <= 33) {
                    stomachJoint.rotateAngleX += 0.06 * -frame * (frame - 7);
                    neck.rotateAngleX -= 0.06 * -frame * (frame - 7);
                }
            }
        } else if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.VERTICAL_ATTACK_ANIMATION) {
            animator.setAnimation(EntityWroughtnaut.VERTICAL_ATTACK_ANIMATION);
            animator.setStaticKeyframe(6);
            animator.startKeyframe(15);
            animator.rotate(stomachJoint, -0.65F, 0F, 0);
            animator.move(waist, 0F, 0F, 6);
            animator.rotate(neck, 0.65F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(lowerArmRightJoint, -1.5F, 0F, 0.3F);
            animator.rotate(handRight, 0F, 0.2F, -0.5F);
            animator.rotate(axeHandle, 0, -1.7F, 0);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, -0.5F, -0.3F, -1F);
            animator.rotate(lowerArmLeftJoint, 1.7F, 0F, -0.1F);
            animator.rotate(handLeft, -1.3F, -0.6F, -0.6F);

            animator.rotate(thighRightJoint, -0.7F, 0, 0);
            animator.rotate(thighRightJoint2, 0F, 0.2F, 0.5F);
            animator.rotate(calfRightJoint, 0.3F, 0, 0);

            animator.rotate(thighLeftJoint, -0.1F, -0.2F, 0);
            animator.rotate(calfLeftJoint, -0.3F, 0, 0);
            animator.rotate(footLeftJoint, 0.3F, 0, 0);

            animator.endKeyframe();

            animator.setStaticKeyframe(5);

            animator.startKeyframe(4);
            animator.rotate(stomachJoint, 0.65F, 0F, 0);
            animator.move(waist, 0F, 4F, -3);
            animator.rotate(neck, 0.3F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmRight, 0F, 0F, 1.5F);
            animator.rotate(lowerArmRightJoint, -1.5F, 0F, 0.3F);
            animator.rotate(lowerArmRight, 0F, 0F, 0.7F);
            animator.rotate(handRight, 0F, 0.2F, -0.5F);
            animator.rotate(axeHandle, 0, -1.7F, 0);

            animator.rotate(shoulderLeft, 0, 0.3F, -0.4F);
            animator.rotate(upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            animator.rotate(upperArmLeft, -0.2F, 0.3F, -0.5F);
            animator.rotate(lowerArmLeft, 0, -0.3F, -1.5F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(shoulderLeft, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1.5F, 1.3F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmLeft, 0F, 0F, 1.5F);
            animator.rotate(lowerArmLeftJoint, 1F, 0F, -0.5F);
            animator.rotate(lowerArmLeft, 0F, 0F, -0.7F);
            animator.rotate(handLeft, 0.7F, 0F, -0.5F);

            animator.rotate(thighRightJoint, -0.6F, 0.4F, 0);
            animator.rotate(thighRightJoint2, 0F, 0F, 0);
            animator.rotate(calfRightJoint, -0.1F, 0, 0);
            animator.rotate(footRightJoint, 0.7F, 0, 0);

            animator.rotate(thighLeftJoint, -0.2F, 0, 0);
            animator.rotate(calfLeftJoint, 0.3F, 0, 0);
            animator.rotate(footLeftJoint, -0.1F, 0, 0);

            animator.endKeyframe();
            animator.setStaticKeyframe(15);

            animator.startKeyframe(5);
            animator.rotate(stomachJoint, 0.2F, 0F, 0);
            animator.move(waist, 0F, 4F, -3);
            animator.rotate(neck, -0.5F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmRight, 0F, 0.15F, 1.7F);
            animator.move(upperArmRightJoint, 0F, 7F, 0F);
            animator.move(upperArmRight, 5F, 0F, 0F);
            animator.rotate(lowerArmRightJoint, -2F, 0F, 0.8F);
            animator.rotate(lowerArmRight, 0F, 0F, 0.7F);
            animator.rotate(handRight, 0F, 0.6F, -0.5F);
            animator.rotate(handRightJoint, 0.2F, 0F, 0F);
            animator.rotate(axeHandle, -0.14F, -1.72F, 0.12F);

            animator.rotate(shoulderLeft, 0, 0.3F, -0.4F);
            animator.rotate(upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            animator.rotate(upperArmLeft, -0.2F, 0.5F, -0.5F);
            animator.rotate(lowerArmLeft, 0, -0.3F, -1.5F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(shoulderLeft, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1.5F, 1.3F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, 0F, -0.3F, -1F);
            animator.rotate(upperArmLeft, 0F, -0.9F, 1.5F);
            animator.move(upperArmLeftJoint, 0F, 5F, 0F);
            animator.rotate(lowerArmLeftJoint, 1.2F, 0.3F, -0.8F);
            animator.rotate(lowerArmLeft, -0.5F, -0.7F, -1.5F);
            animator.rotate(handLeft, 0.7F, 0F, -0.5F);

            animator.rotate(thighRightJoint, -0.6F, 0.4F, 0);
            animator.rotate(thighRightJoint2, 0F, 0F, 0);
            animator.rotate(calfRightJoint, -0.1F, 0, 0);
            animator.rotate(footRightJoint, 0.7F, 0, 0);

            animator.rotate(thighLeftJoint, -0.2F, 0, 0);
            animator.rotate(calfLeftJoint, 0.3F, 0, 0);
            animator.rotate(footLeftJoint, -0.1F, 0, 0);

            animator.move(waistBendController, 1, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(15);

            animator.startKeyframe(7);
            animator.rotate(stomachJoint, 0.65F, 0F, 0);
            animator.move(waist, 0F, 4F, -3);
            animator.rotate(neck, 0.3F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmRight, 0F, 0F, 1.5F);
            animator.rotate(lowerArmRightJoint, -1.5F, 0F, 0.3F);
            animator.rotate(lowerArmRight, 0F, 0F, 0.7F);
            animator.rotate(handRight, 0F, 0.2F, -0.5F);
            animator.rotate(axeHandle, 0, -1.7F, 0);

            animator.rotate(shoulderLeft, 0, 0.3F, -0.4F);
            animator.rotate(upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            animator.rotate(upperArmLeft, -0.2F, 0.3F, -0.5F);
            animator.rotate(lowerArmLeft, 0, -0.3F, -1.5F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(shoulderLeft, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1.5F, 1.3F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmLeft, 0F, 0F, 1.5F);
            animator.rotate(lowerArmLeftJoint, 1F, 0F, -0.5F);
            animator.rotate(lowerArmLeft, 0F, 0F, -0.7F);
            animator.rotate(handLeft, 0.7F, 0F, -0.5F);

            animator.rotate(thighRightJoint, -0.6F, 0.4F, 0);
            animator.rotate(thighRightJoint2, 0F, 0F, 0);
            animator.rotate(calfRightJoint, -0.1F, 0, 0);
            animator.rotate(footRightJoint, 0.7F, 0, 0);

            animator.rotate(thighLeftJoint, -0.2F, 0, 0);
            animator.rotate(calfLeftJoint, 0.3F, 0, 0);
            animator.rotate(footLeftJoint, -0.1F, 0, 0);

            animator.endKeyframe();

            animator.setStaticKeyframe(5);

            animator.startKeyframe(5);
            animator.rotate(stomachJoint, 0.2F, 0F, 0);
            animator.move(waist, 0F, 4F, -3);
            animator.rotate(neck, -0.5F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmRight, 0F, 0.15F, 1.7F);
            animator.move(upperArmRightJoint, 0F, 7F, 0F);
            animator.move(upperArmRight, 5F, 0F, 0F);
            animator.rotate(lowerArmRightJoint, -2F, 0F, 0.8F);
            animator.rotate(lowerArmRight, 0F, 0F, 0.7F);
            animator.rotate(handRight, 0F, 0.6F, -0.5F);
            animator.rotate(handRightJoint, 0.2F, 0F, 0F);
            animator.rotate(axeHandle, -0.14F, -1.72F, 0.12F);

            animator.rotate(shoulderLeft, 0, 0.3F, -0.4F);
            animator.rotate(upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            animator.rotate(upperArmLeft, -0.2F, 0.5F, -0.5F);
            animator.rotate(lowerArmLeft, 0, -0.3F, -1.5F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(shoulderLeft, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1.5F, 1.3F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, 0F, -0.3F, -1F);
            animator.rotate(upperArmLeft, 0F, -0.9F, 1.5F);
            animator.move(upperArmLeftJoint, 0F, 5F, 0F);
            animator.rotate(lowerArmLeftJoint, 1.2F, 0.3F, -0.8F);
            animator.rotate(lowerArmLeft, -0.5F, -0.7F, -1.5F);
            animator.rotate(handLeft, 0.7F, 0F, -0.5F);

            animator.rotate(thighRightJoint, -0.6F, 0.4F, 0);
            animator.rotate(thighRightJoint2, 0F, 0F, 0);
            animator.rotate(calfRightJoint, -0.1F, 0, 0);
            animator.rotate(footRightJoint, 0.7F, 0, 0);

            animator.rotate(thighLeftJoint, -0.2F, 0, 0);
            animator.rotate(calfLeftJoint, 0.3F, 0, 0);
            animator.rotate(footLeftJoint, -0.1F, 0, 0);

            animator.move(waistBendController, 1, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(5);

            animator.startKeyframe(5);
            animator.rotate(stomachJoint, -0.5F, 0F, 0);
            animator.move(waist, 0F, 0F, 6);
            animator.rotate(neck, 0.65F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmRight, 0F, 0F, 1.3F);
            animator.rotate(lowerArmRightJoint, -1.5F, 0F, 0.3F);
            animator.rotate(handRight, 0F, 0.2F, -0.5F);
            animator.rotate(axeHandle, 0, -1.7F, 0);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, -0.5F, -0.3F, -1F);
            animator.rotate(upperArmLeft, -0.5F, 0F, 1.3F);
            animator.rotate(lowerArmLeftJoint, 1.7F, 0F, -0.1F);
            animator.rotate(handLeft, -1.3F, -0.6F, -0.6F);

            animator.rotate(thighRightJoint, -0.7F, 0, 0);
            animator.rotate(thighRightJoint2, 0F, 0.2F, 0.5F);
            animator.rotate(calfRightJoint, 0.3F, 0, 0);

            animator.rotate(thighLeftJoint, -0.1F, -0.2F, 0);
            animator.rotate(calfLeftJoint, -0.3F, 0, 0);
            animator.rotate(footLeftJoint, 0.3F, 0, 0);

            animator.endKeyframe();

            animator.setStaticKeyframe(3);

            animator.resetKeyframe(10);
            neck.rotateAngleX += Math.sin((entityWroughtnaut.frame + LLibrary.PROXY.getPartialTicks()) * 2) * waistBendController.rotationPointX * 0.1;
        } else if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.HURT_ANIMATION) {
            animator.setAnimation(EntityWroughtnaut.HURT_ANIMATION);
            animator.startKeyframe(0);
            animator.rotate(stomachJoint, 0.65F, 0F, 0);
            animator.move(waist, 0F, 4F, -3);
            animator.rotate(neck, 0.3F, 0F, 0);

            animator.rotate(shoulderRight, 0, 1F, 0);
            animator.rotate(upperArmRightJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmRight, 0F, 0F, 1.5F);
            animator.rotate(lowerArmRightJoint, -1.5F, 0F, 0.3F);
            animator.rotate(lowerArmRight, 0F, 0F, 0.7F);
            animator.rotate(handRight, 0F, 0.2F, -0.5F);
            animator.rotate(axeHandle, 0, -1.7F, 0);

            animator.rotate(shoulderLeft, 0, 0.3F, 0.4F);
            animator.rotate(upperArmLeft, 0, 0.5F, -0.5F);
            animator.rotate(lowerArmLeft, 0, -0.3F, -1.5F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(shoulderLeft, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1.5F, 1.3F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderLeft, 0, -1F, 0);
            animator.rotate(upperArmLeftJoint, 0F, 0.3F, -1F);
            animator.rotate(upperArmLeft, 0F, 0F, 1.5F);
            animator.rotate(lowerArmLeftJoint, 1F, 0F, -0.5F);
            animator.rotate(lowerArmLeft, 0F, 0F, -0.7F);
            animator.rotate(handLeft, 0.7F, 0F, -0.5F);

            animator.rotate(thighRightJoint, -0.6F, 0.4F, 0);
            animator.rotate(thighRightJoint2, 0F, 0F, 0);
            animator.rotate(calfRightJoint, -0.1F, 0, 0);
            animator.rotate(footRightJoint, 0.7F, 0, 0);

            animator.rotate(thighLeftJoint, -0.2F, 0, 0);
            animator.rotate(calfLeftJoint, 0.3F, 0, 0);
            animator.rotate(footLeftJoint, -0.1F, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(stomachJoint, -0.4F, 0, 0);
            animator.rotate(neck, -0.4F, 0, 0);

            animator.rotate(shoulderLeft, 0, 0.5F, 0.75F);
            animator.rotate(upperArmLeft, 0, 1.2F, -0.3F);
            animator.rotate(lowerArmLeft, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmLeftJoint, 0, 0F, -0.3F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderRight, 0, -0.5F, -0.75F);
            animator.rotate(upperArmRight, 0, -1F, -0.3F);
            animator.rotate(lowerArmRight, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmRightJoint, 0, 0F, -0.3F);
            animator.rotate(handRightJoint, -1F, 0, -0.5F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);

            animator.endKeyframe();
            animator.setStaticKeyframe(2);
            animator.resetKeyframe(9);
        } else if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.DIE_ANIMATION) {
            animator.setAnimation(EntityWroughtnaut.DIE_ANIMATION);
            animator.startKeyframe(5);
            animator.rotate(stomachJoint, -0.4F, 0, 0);
            animator.rotate(neck, -0.4F, 0, 0);

            animator.rotate(shoulderLeft, 0, 0.5F, 0.75F);
            animator.rotate(upperArmLeft, 0, 1.2F, -0.3F);
            animator.rotate(lowerArmLeft, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmLeftJoint, 0, 0F, -0.3F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderRight, 0, -0.5F, -0.75F);
            animator.rotate(upperArmRight, 0, -1F, -0.3F);
            animator.rotate(lowerArmRight, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmRightJoint, 0, 0F, -0.3F);
            animator.rotate(handRightJoint, -1F, 0, -0.5F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);
            animator.endKeyframe();

            animator.startKeyframe(8);
            animator.rotate(stomachJoint, -0.4F, 0, 0);
            animator.rotate(neck, -0.4F, 0, 0);

            animator.rotate(shoulderLeft, 0, 0.5F, 0.75F);
            animator.rotate(upperArmLeft, 0, 1.2F, -0.3F);
            animator.rotate(lowerArmLeft, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmLeftJoint, 0, 0F, -0.3F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderRight, 0, -0.5F, -0.75F);
            animator.rotate(upperArmRight, 0, -1F, -0.3F);
            animator.rotate(lowerArmRight, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmRightJoint, 0, 0F, -0.3F);
            animator.rotate(handRightJoint, -1F, 0, -0.5F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);

            animator.move(waistBendController, 1, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(30);

            animator.startKeyframe(10);
            animator.rotate(stomachJoint, -0.4F, 0, 0);
            animator.rotate(neck, -0.4F, 0, 0);

            animator.rotate(shoulderLeft, 0, 0.5F, 0.75F);
            animator.rotate(upperArmLeft, 0, 1.2F, -0.3F);
            animator.rotate(lowerArmLeft, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmLeftJoint, 0, 0F, -0.3F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderRight, 0, -0.5F, -0.75F);
            animator.rotate(upperArmRight, 0, -1F, -0.3F);
            animator.rotate(lowerArmRight, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmRightJoint, 0, 0F, -0.3F);
            animator.rotate(handRightJoint, -1F, 0, -0.5F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);
            animator.endKeyframe();

            animator.setStaticKeyframe(10);

            animator.startKeyframe(5);
            animator.move(rootBox, 0, 7, -10);
            animator.rotate(stomachJoint, -0.6F, 0, 0);
            animator.rotate(neck, -0.4F, 0, 0);

            animator.rotate(shoulderLeft, 0, 0.5F, 0.75F);
            animator.rotate(upperArmLeft, 0, 1.2F, -0.3F);
            animator.rotate(lowerArmLeft, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmLeftJoint, 0, 0F, -0.3F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderRight, 0, -0.5F, -0.75F);
            animator.rotate(upperArmRight, 0, -1F, -0.3F);
            animator.rotate(lowerArmRight, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmRightJoint, 0, 0F, -0.3F);
            animator.rotate(handRightJoint, -1F, 0, -0.5F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);

            animator.rotate(thighRightJoint, 0.3F, 0.5F, -0.3F);
            animator.rotate(calfRightJoint, 0.5F, 0, 0);
            animator.rotate(footRightJoint, 1.7F, 0, 0);

            animator.rotate(thighLeftJoint, 0.3F, -0.5F, 0.3F);
            animator.rotate(calfLeftJoint, 0.5F, 0, 0);
            animator.rotate(footLeftJoint, 1.7F, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(13);

            animator.startKeyframe(7);
            animator.move(rootBox, 0, 15, -33);
            animator.rotate(rootBox, 1.5F, 0, 0);

            animator.rotate(shoulderLeft, 0, 0.5F, 0.75F);
            animator.rotate(shoulderLeftJoint, 0, 0, 0.6F);
            animator.rotate(upperArmLeft, 2.4F, 0.6F, -0.3F);
            animator.rotate(lowerArmLeft, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmLeftJoint, 0, 0F, -0.3F);
            animator.rotate(handLeftJoint, -1.6F, 0, 0.5F);
            animator.rotate(handLeft, -0.2F, 0, 0.3F);

            animator.rotate(shoulderRight, 0, -0.5F, -0.75F);
            animator.rotate(shoulderRightJoint, 0, 0, -0.6F);
            animator.rotate(upperArmRight, -2.7F, -0.5F, 0.4F);
            animator.rotate(lowerArmRight, 0, 0.9F, -0.2F);
            animator.rotate(lowerArmRightJoint, 0, 0F, -0.3F);
            animator.rotate(handRightJoint, 0.4F, 0.1F, -0.7F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);
            animator.rotate(axeHandle, 0F, -0.5F, 0F);

            animator.rotate(thighRightJoint, 0.3F, 0.5F, -0.3F);
            animator.rotate(calfRightJoint, -0.8F, 0.1F, 0);
            animator.rotate(calfRight, 0, 0, 0.3F);
            animator.rotate(footRightJoint, 1.7F, 0, 0);

            animator.rotate(thighLeftJoint, 0.3F, -0.5F, 0.3F);
            animator.rotate(calfLeftJoint, -0.8F, -0.1F, 0);
            animator.rotate(calfLeft, 0, 0, -0.3F);
            animator.rotate(footLeftJoint, 1.7F, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(40);

            swing(stomachJoint, 0.5F, 0.2F * waistBendController.rotationPointX, false, 0, 0, (entityWroughtnaut.frame + LLibrary.PROXY.getPartialTicks()), 1F);
            walk(neck, 1.5F, 0.1F * waistBendController.rotationPointX, false, 0F, 0, (entityWroughtnaut.frame + LLibrary.PROXY.getPartialTicks()), 1F);
            swing(shoulderRight, 1.5F, 0.05F * waistBendController.rotationPointX, true, 0F, 0, (entityWroughtnaut.frame + LLibrary.PROXY.getPartialTicks()), 1F);
            swing(shoulderLeft, 1.5F, 0.05F * waistBendController.rotationPointX, false, 0F, 0, (entityWroughtnaut.frame + LLibrary.PROXY.getPartialTicks()), 1F);
        } else if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.ACTIVATE_ANIMATION) {
            animator.setAnimation(EntityWroughtnaut.ACTIVATE_ANIMATION);
            animator.startKeyframe(0);
            animator.rotate(shoulderLeft, 0, -0.4F, -0.4F);
            animator.rotate(shoulderRight, 0, 0.4F, 0.4F);
            animator.rotate(upperArmRight, 0, 0.5F, 0.5F);
            animator.rotate(upperArmLeft, 0, -0.5F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1, 1.5F);
            animator.rotate(lowerArmRight, 0, 1, -1.5F);
            animator.move(axeBase, 0, 30, 3.5F);
            animator.rotate(axeBase, 0, 0.35F, 0);
            animator.rotate(handRight, -0.9F, 0, 0);
            animator.rotate(handRightJoint, -0.2F, 0, -0.88F);
            animator.rotate(handLeftJoint, 0.8F, -0.3F, 0F);
            animator.rotate(neck, 0.5F, 0, 0);
            animator.move(rootBox, 0, -5, 0);
            animator.rotate(thighRightJoint, 0.35F, 0.5F, 0);
            animator.rotate(thighLeftJoint, 0.35F, -0.5F, 0);
            animator.rotate(calfRightJoint, -0.6F, 0, 0);
            animator.rotate(calfLeftJoint, -0.6F, 0, 0);
            animator.rotate(footLeft, 0.25F, 0, 0);
            animator.rotate(footRight, 0.25F, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(10);
            animator.rotate(shoulderLeft, 0, -0.4F, -0.4F);
            animator.rotate(shoulderRight, 0, 0.4F, 0.4F);
            animator.rotate(upperArmRight, 0, 0.5F, 0.5F);
            animator.rotate(upperArmLeft, 0, -0.5F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1, 1.5F);
            animator.rotate(lowerArmRight, 0, 1, -1.5F);
            animator.move(axeBase, 0, 30, 3.5F);
            animator.rotate(axeBase, 0, 0.35F, 0);
            animator.rotate(handRight, -0.9F, 0, 0);
            animator.rotate(handRightJoint, -0.2F, 0, -0.88F);
            animator.rotate(handLeftJoint, 0.8F, -0.3F, 0F);
            animator.move(rootBox, 0, -5, 0);
            animator.rotate(thighRightJoint, 0.35F, 0.5F, 0);
            animator.rotate(thighLeftJoint, 0.35F, -0.5F, 0);
            animator.rotate(calfRightJoint, -0.6F, 0, 0);
            animator.rotate(calfLeftJoint, -0.6F, 0, 0);
            animator.rotate(footLeft, 0.25F, 0, 0);
            animator.rotate(footRight, 0.25F, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(9);

            animator.startKeyframe(26);
            animator.rotate(shoulderLeft, 0, -0.3F, -0.4F);
            animator.rotate(shoulderRight, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.5F, 0.5F);
            animator.rotate(upperArmRight, 0, 0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 0.3F, 1.5F);
            animator.rotate(lowerArmRight, 0, 0.7F, -1.3F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);
            animator.rotate(handLeftJoint, 1.6F, 0, -0.5F);
            animator.rotate(axeHandle, 0, 0.8F, 0);
            animator.move(waistBendController, 26, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(20);

            float frame = waistBendController.rotationPointX;
            if (entityWroughtnaut.getAnimationTick() <= 27) {
                thighRightJoint.rotateAngleX += 0.01 * frame * (frame - 13);
                calfRightJoint.rotateAngleX -= 0.02 * frame * (frame - 13);
            }
            if (entityWroughtnaut.getAnimationTick() >= 28) {
                thighLeftJoint.rotateAngleX += 0.01 * (frame - 13) * (frame - 26);
                calfLeftJoint.rotateAngleX -= 0.02 * (frame - 13) * (frame - 26);
            }
            rootBox.rotateAngleZ -= 0.05 * frame * (frame - 13) * (frame - 26) / 845;
        } else if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.DEACTIVATE_ANIMATION) {
            animator.setAnimation(EntityWroughtnaut.DEACTIVATE_ANIMATION);
            animator.startKeyframe(0);
            animator.rotate(shoulderLeft, 0, -0.3F, -0.4F);
            animator.rotate(shoulderRight, 0, 0.2F, 0.35F);
            animator.rotate(upperArmLeft, 0, -0.5F, 0.5F);
            animator.rotate(upperArmRight, 0, 0.3F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 0.3F, 1.5F);
            animator.rotate(lowerArmRight, 0, 0.7F, -1.3F);
            animator.rotate(handRight, -0.2F, 0, 0.3F);
            animator.rotate(handLeftJoint, 1.6F, 0, -0.5F);
            animator.rotate(axeHandle, 0, 0.8F, 0);
            animator.endKeyframe();

            animator.startKeyframe(15);
            animator.rotate(shoulderLeft, 0, -0.4F, -0.4F);
            animator.rotate(shoulderRight, 0, 0.4F, 0.4F);
            animator.rotate(upperArmRight, 0, 0.5F, 0.5F);
            animator.rotate(upperArmLeft, 0, -0.5F, 0.5F);
            animator.rotate(lowerArmLeft, 0, 1, 1.5F);
            animator.rotate(lowerArmRight, 0, 1, -1.5F);
            animator.move(axeBase, 0, 30, 3.5F);
            animator.rotate(axeBase, 0, 0.35F, 0);
            animator.rotate(handRight, -0.9F, 0, 0);
            animator.rotate(handRightJoint, -0.2F, 0, -0.88F);
            animator.rotate(handLeftJoint, 0.8F, -0.3F, 0F);
            animator.rotate(neck, 0.5F, 0, 0);
            animator.move(rootBox, 0, -5, 0);
            animator.rotate(thighRightJoint, 0.35F, 0.5F, 0);
            animator.rotate(thighLeftJoint, 0.35F, -0.5F, 0);
            animator.rotate(calfRightJoint, -0.6F, 0, 0);
            animator.rotate(calfLeftJoint, -0.6F, 0, 0);
            animator.rotate(footLeft, 0.25F, 0, 0);
            animator.rotate(footRight, 0.25F, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(20);
        } else if (entityWroughtnaut.getAnimation() == EntityWroughtnaut.STOMP_ATTACK_ANIMATION) {
            animator.setAnimation(EntityWroughtnaut.STOMP_ATTACK_ANIMATION);
            animator.startKeyframe(12);
            animator.move(waist, 0, -4.5F, 0);
            animator.rotate(thighRight, 0.25F, 0, 0.15F);
            animator.rotate(calfRightJoint, -0.55F, 0, 0.1F);
            animator.rotate(footRight, 0.35F, 0, 0);
            animator.rotate(stomachJoint, -0.2F, 0, 0);
            animator.rotate(thighLeft, -0.4F, -0.7F, -0.6F);
            animator.rotate(groinFront, -0.4F, -0.3F, 0.1F);
            animator.rotate(groinBack, -0.1F, 0, 0);
            animator.rotate(neck, -0.25F, 0, 0);
            animator.rotate(shoulderRightJoint, -0.4F, 0, 0);
            animator.rotate(upperArmRightJoint, 0.3F, 0.1F, -0.2F);
            animator.rotate(shoulderLeftJoint, -0.4F, 0, 0);
            animator.rotate(upperArmLeftJoint, -0.45F, 0.3F, 0.3F);
            animator.rotate(handLeftJoint, 0, 0, -0.3F);
            animator.endKeyframe();
            animator.startKeyframe(2);
            animator.move(waist, 0, 0, -8);
            animator.rotate(thighLeft, 0, 0, 0);
            animator.rotate(thighLeftJoint, -0.35F, -0.9F, 0);
            animator.rotate(calfLeftJoint, -0.45F, 0, 0);
            animator.rotate(footLeft, 0.75F, 0, 0);
            animator.rotate(thighRightJoint, 0.6F, 0.25F, -0.3F);
            animator.rotate(footRight, -0.25F, 0.2F, 0);
            animator.rotate(calfRightJoint, -0.15F, 0, 0);
            animator.rotate(groinFront, -0.6F, -0.8F, 0.2F);
            animator.rotate(groinBack, 0.3F, 0, 0);
            animator.rotate(neck, 0.6F, 0, 0);
            animator.rotate(shoulderRightJoint, 0.1F, 0, 0);
            animator.rotate(upperArmRightJoint, 0.3F, 0, -0.1F);
            animator.rotate(lowerArmRightJoint, -0.4F, 0.2F, 0);
            animator.rotate(shoulderLeftJoint, 0.1F, 0, 0);
            animator.rotate(axeHandle, 0, 0.3F, 0.4F);
            animator.endKeyframe();
            animator.startKeyframe(3);
            animator.move(waist, 0, 0, -8);
            animator.rotate(thighLeft, 0, 0, 0);
            animator.rotate(thighLeftJoint, -0.35F, -0.9F, 0);
            animator.rotate(calfLeftJoint, -0.45F, 0, 0);
            animator.rotate(footLeft, 0.75F, 0, 0);
            animator.rotate(thighRightJoint, 0.6F, 0.25F, -0.3F);
            animator.rotate(footRight, -0.25F, 0.2F, 0);
            animator.rotate(calfRightJoint, -0.15F, 0, 0);
            animator.rotate(groinFront, -0.8F, -0.8F, 0);
            animator.rotate(groinBack, -0.2F, 0, 0);
            animator.rotate(neck, 0.6F, 0, 0);
            animator.rotate(shoulderLeftJoint, -0.1F, 0, 0);
            animator.rotate(shoulderRightJoint, -0.1F, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(6);
            animator.move(waist, 0, 0, -8);
            animator.rotate(thighLeft, 0, 0, 0);
            animator.rotate(thighLeftJoint, -0.35F, -0.9F, 0);
            animator.rotate(calfLeftJoint, -0.45F, 0, 0);
            animator.rotate(footLeft, 0.75F, 0, 0);
            animator.rotate(thighRightJoint, 0.6F, 0.25F, -0.3F);
            animator.rotate(footRight, -0.25F, 0.2F, 0);
            animator.rotate(calfRightJoint, -0.15F, 0, 0);
            animator.rotate(groinFront, -0.5F, -0.8F, 0);
            animator.rotate(neck, 0.6F, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(2);
            animator.startKeyframe(6);
            animator.move(waist, 0, 0, -5);
            animator.rotate(thighLeft, -0.15F, 0.1F, 0.0F);
            animator.rotate(thighLeftJoint, -0.1F, -0.2F, 0.2F);
            animator.rotate(calfLeftJoint, 0.3F, 0, 0.1F);
            animator.rotate(footLeft, 0.35F, 0, 0);
            animator.rotate(thighRightJoint, 0.3F, 0.1F, -0.2F);
            animator.rotate(footRight, -0.1F, 0.1F, 0);
            animator.rotate(groinFront, -0.2F, -0.1F, 0);
            animator.rotate(neck, 0.2F, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(4);
            animator.rotate(groinBack, -0.2F, 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
    }
}
