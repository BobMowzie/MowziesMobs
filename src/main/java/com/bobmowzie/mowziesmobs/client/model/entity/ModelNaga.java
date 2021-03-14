package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

/**
 * Created by BobMowzie on 9/9/2018.
 */

public class ModelNaga<T extends EntityNaga> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer root;
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer neck;
    public AdvancedModelRenderer tail1;
    public AdvancedModelRenderer spike1joint;
    public AdvancedModelRenderer spike2joint;
    public AdvancedModelRenderer spike3joint;
    public AdvancedModelRenderer shoulder1_L;
    public AdvancedModelRenderer shoulder1_R;
    public AdvancedModelRenderer backFin1;
    public AdvancedModelRenderer backFin1Reversed;
    public AdvancedModelRenderer headJoint;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer jaw;
    public AdvancedModelRenderer underHead;
    public AdvancedModelRenderer teethUpper;
    public AdvancedModelRenderer eyebrowJoint_R;
    public AdvancedModelRenderer eyebrowJoint_L;
    public AdvancedModelRenderer teethLower;
    public AdvancedModelRenderer eyebrow_R;
    public AdvancedModelRenderer eyebrow_L;
    public AdvancedModelRenderer tail2;
    public AdvancedModelRenderer spike4joint;
    public AdvancedModelRenderer backFin2;
    public AdvancedModelRenderer backFin2Reversed;
    public AdvancedModelRenderer tail3;
    public AdvancedModelRenderer spike5joint;
    public AdvancedModelRenderer backWing_L;
    public AdvancedModelRenderer backWing_R;
    public AdvancedModelRenderer backWing_LReversed;
    public AdvancedModelRenderer backWing_RReversed;
    public AdvancedModelRenderer backFin3;
    public AdvancedModelRenderer backFin3Reversed;
    public AdvancedModelRenderer tail4;
    public AdvancedModelRenderer tail5;
    public AdvancedModelRenderer tail6;
    public AdvancedModelRenderer tailFin;
    public AdvancedModelRenderer tailFinReversed;
    public AdvancedModelRenderer spike5;
    public AdvancedModelRenderer spike4;
    public AdvancedModelRenderer spike1;
    public AdvancedModelRenderer spike2;
    public AdvancedModelRenderer spike3;
    public AdvancedModelRenderer spike3Bottom;
    public AdvancedModelRenderer upperArmJoint_L;
    public AdvancedModelRenderer upperArm_L;
    public AdvancedModelRenderer lowerArmJoint_L;
    public AdvancedModelRenderer wingWebbing6_L;
    public AdvancedModelRenderer wingWebbing6_LReversed;
    public AdvancedModelRenderer lowerArm_L;
    public AdvancedModelRenderer handJoint_L;
    public AdvancedModelRenderer wingWebbing5_L;
    public AdvancedModelRenderer wingWebbing5_LReversed;
    public AdvancedModelRenderer hand_L;
    public AdvancedModelRenderer wingFrame1_L;
    public AdvancedModelRenderer wingFrame2_L;
    public AdvancedModelRenderer wingFrame3_L;
    public AdvancedModelRenderer wingFrame4_L;
    public AdvancedModelRenderer wingClaw_L;
    public AdvancedModelRenderer wingWebbing1_L;
    public AdvancedModelRenderer wingWebbing2_L;
    public AdvancedModelRenderer wingWebbing3_L;
    public AdvancedModelRenderer wingWebbing4_L;
    public AdvancedModelRenderer wingWebbing1_LReversed;
    public AdvancedModelRenderer wingWebbing2_LReversed;
    public AdvancedModelRenderer wingWebbing3_LReversed;
    public AdvancedModelRenderer wingWebbing4_LReversed;
    public AdvancedModelRenderer upperArmJoint_R;
    public AdvancedModelRenderer upperArm_R;
    public AdvancedModelRenderer lowerArmJoint_R;
    public AdvancedModelRenderer wingWebbing6_R;
    public AdvancedModelRenderer wingWebbing6_RReversed;
    public AdvancedModelRenderer lowerArm_R;
    public AdvancedModelRenderer handJoint_R;
    public AdvancedModelRenderer wingWebbing5_R;
    public AdvancedModelRenderer wingWebbing5_RReversed;
    public AdvancedModelRenderer hand_R;
    public AdvancedModelRenderer wingFrame1_R;
    public AdvancedModelRenderer wingFrame2_R;
    public AdvancedModelRenderer wingFrame3_R;
    public AdvancedModelRenderer wingFrame4_R;
    public AdvancedModelRenderer wingClaw_R;
    public AdvancedModelRenderer wingWebbing1_R;
    public AdvancedModelRenderer wingWebbing2_R;
    public AdvancedModelRenderer wingWebbing3_R;
    public AdvancedModelRenderer wingWebbing4_R;
    public AdvancedModelRenderer wingWebbing1_RReversed;
    public AdvancedModelRenderer wingWebbing2_RReversed;
    public AdvancedModelRenderer wingWebbing3_RReversed;
    public AdvancedModelRenderer wingWebbing4_RReversed;
    public AdvancedModelRenderer wingFolder;
    public AdvancedModelRenderer shoulderLJoint;
    public AdvancedModelRenderer shoulderRJoint;
    public AdvancedModelRenderer mouthSocket;
    public AdvancedModelRenderer scaler;
    public AdvancedModelRenderer swooper;

    public AdvancedModelRenderer tailEnd;

    public AdvancedModelRenderer[] tailOriginal;
    public AdvancedModelRenderer[] tailDynamic;

    public AdvancedModelRenderer[] reversePlanes;

    private DynamicChain tail;

    public ModelNaga() {
        super(MMRenderType::getEntityCutoutCull);
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.eyebrow_L = new AdvancedModelRenderer(this, 63, 0);
        this.eyebrow_L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.eyebrow_L.addBox(-3.5F, 0.0F, 0.0F, 9, 7, 3, 0.0F);
        setRotateAngle(eyebrow_L, 0.0F, 0.0F, 0.12217304763960307F);
        this.spike4 = new AdvancedModelRenderer(this, 45, 52);
        this.spike4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spike4.addBox(0.0F, 0.0F, 0.0F, 11, 6, 11, 0.0F);
        setRotateAngle(spike4, 0.0F, -0.7853981633974483F, 0.0F);
        this.spike3joint = new AdvancedModelRenderer(this, 0, 0);
        this.spike3joint.setRotationPoint(0.0F, 1.0F, 4.0F);
        this.spike3joint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(spike3joint, 0.5235987755982988F, 0.0F, 0.0F);
        this.headJoint = new AdvancedModelRenderer(this, 0, 0);
        this.headJoint.setRotationPoint(0.0F, 1.0F, -13.0F);
        this.headJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(headJoint, 0.4363323129985824F, 0.0F, 0.0F);
        this.body = new AdvancedModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, -9.0F, -9.0F);
        this.body.addBox(-10.5F, -4.0F, -3.0F, 21, 13, 21, 0.0F);
        this.spike2 = new AdvancedModelRenderer(this, 0, 54);
        this.spike2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spike2.addBox(0.0F, 0.0F, 0.0F, 15, 4, 15, 0.0F);
        setRotateAngle(spike2, 0.0F, -0.7853981633974483F, 0.0F);
        this.lowerArmJoint_R = new AdvancedModelRenderer(this, 0, 0);
        this.lowerArmJoint_R.setRotationPoint(-15.0F, 0.0F, -0.5F);
        this.lowerArmJoint_R.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.spike1 = new AdvancedModelRenderer(this, 0, 54);
        this.spike1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spike1.addBox(0.0F, 0.0F, 0.0F, 15, 4, 15, 0.0F);
        setRotateAngle(spike1, 0.0F, -0.7853981633974483F, 0.0F);
        this.wingWebbing5_L = new AdvancedModelRenderer(this, 193, 128);
        this.wingWebbing5_L.setRotationPoint(12.0F, 0.0F, 1.0F);
        this.wingWebbing5_L.addBox(-12.0F, 0.0F, 0.0F, 20, 0, 23, 0.0F);
        this.wingWebbing5_LReversed = new AdvancedModelRenderer(this, 193, 128);
        this.wingWebbing5_LReversed.mirror = true;
        this.wingWebbing5_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing5_LReversed.addBox(-12.0F, 0.0F, 0.0F, 20, 0, 23, 0.0F);
//        wingWebbing5_LReversed.setScale(1, -1, 1);
        this.tail5 = new AdvancedModelRenderer(this, 162, 30);
        this.tail5.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.tail5.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 13, 0.0F);
        this.tail3 = new AdvancedModelRenderer(this, 0, 34);
        this.tail3.setRotationPoint(0.0F, 0.0F, 15.0F);
        this.tail3.addBox(-5.5F, -2.5F, 0.0F, 11, 4, 16, 0.0F);
        this.wingWebbing2_L = new AdvancedModelRenderer(this, 20, 98);
        this.wingWebbing2_L.setRotationPoint(-1.5F, 0.0F, 0.0F);
        this.wingWebbing2_L.addBox(0.0F, 0.0F, -15.0F, 50, 0, 30, 0.0F);
        setRotateAngle(wingWebbing2_L, 0.0F, -0.30543261909900765F, 0.0F);
        this.wingWebbing2_LReversed = new AdvancedModelRenderer(this, 20, 98);
        this.wingWebbing2_LReversed.mirror = true;
        this.wingWebbing2_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing2_LReversed.addBox(0.0F, 0.0F, -15.0F, 50, 0, 30, 0.0F);
//        wingWebbing2_LReversed.setScale(1, -1, 1);
        this.root = new AdvancedModelRenderer(this, 0, 0);
        this.root.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.wingFrame1_L = new AdvancedModelRenderer(this, 50, 91);
        this.wingFrame1_L.setRotationPoint(11.0F, 0.0F, -0.5F);
        this.wingFrame1_L.addBox(0.0F, -1.5F, -1.5F, 53, 3, 3, 0.0F);
        this.hand_R = new AdvancedModelRenderer(this, 222, 0);
        this.hand_R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hand_R.addBox(-11.0F, -2.0F, -2.0F, 11, 4, 4, 0.0F);
        this.lowerArm_R = new AdvancedModelRenderer(this, 102, 83);
        this.lowerArm_R.mirror = true;
        this.lowerArm_R.setRotationPoint(0.0F, 0.0F, -0.5F);
        this.lowerArm_R.addBox(-22.0F, -2.0F, -2.0F, 22, 4, 4, 0.0F);
        this.head = new AdvancedModelRenderer(this, 68, 23);
        this.head.setRotationPoint(0.0F, -2.0F, -1.0F);
        this.head.addBox(-8.0F, -3.7F, -8.0F, 16, 6, 16, 0.0F);
        setRotateAngle(head, 0.0F, 0.7853981633974483F, 0.0F);
        this.eyebrowJoint_L = new AdvancedModelRenderer(this, 0, 0);
        this.eyebrowJoint_L.setRotationPoint(4.95F, -5.0F, -7.36F);
        this.eyebrowJoint_L.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(eyebrowJoint_L, 1.0344566476570392F, -0.3179989897133668F, -0.5363396791378574F);
        this.upperArm_L = new AdvancedModelRenderer(this, 106, 74);
        this.upperArm_L.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.upperArm_L.addBox(-2.0F, -2.0F, -3.0F, 18, 4, 5, 0.0F);
        this.wingFrame2_L = new AdvancedModelRenderer(this, 0, 79);
        this.wingFrame2_L.setRotationPoint(7.0F, 0.0F, 0.0F);
        this.wingFrame2_L.addBox(0.0F, -1.5F, -1.5F, 50, 3, 3, 0.0F);
        setRotateAngle(wingFrame2_L, 0.0F, -0.6108652381980153F, 0.0F);
        this.eyebrow_R = new AdvancedModelRenderer(this, 63, 0);
        this.eyebrow_R.mirror = true;
        this.eyebrow_R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.eyebrow_R.addBox(-5.5F, 0.0F, 0.0F, 9, 7, 3, 0.0F);
        setRotateAngle(eyebrow_R, 0.0F, 0.0F, -0.12217304763960307F);
        this.wingWebbing3_R = new AdvancedModelRenderer(this, 144, 71);
        this.wingWebbing3_R.mirror = true;
        this.wingWebbing3_R.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.wingWebbing3_R.addBox(-43.0F, 0.0F, -13.0F, 43, 0, 26, 0.0F);
        setRotateAngle(wingWebbing3_R, 0.0F, 0.30543261909900765F, 0.0F);
        this.wingWebbing3_RReversed = new AdvancedModelRenderer(this, 144, 71);
        this.wingWebbing3_RReversed.mirror = false;
        this.wingWebbing3_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing3_RReversed.addBox(-43.0F, 0.0F, -13.0F, 43, 0, 26, 0.0F);
//        wingWebbing3_RReversed.setScale(1, -1, 1);
        this.wingWebbing3_L = new AdvancedModelRenderer(this, 144, 71);
        this.wingWebbing3_L.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.wingWebbing3_L.addBox(0.0F, 0.0F, -13.0F, 43, 0, 26, 0.0F);
        setRotateAngle(wingWebbing3_L, 0.0F, -0.30543261909900765F, 0.0F);
        this.wingWebbing3_LReversed = new AdvancedModelRenderer(this, 144, 71);
        this.wingWebbing3_LReversed.mirror = true;
        this.wingWebbing3_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing3_LReversed.addBox(0.0F, 0.0F, -13.0F, 43, 0, 26, 0.0F);
//        wingWebbing3_LReversed.setScale(1, -1, 1);
        this.handJoint_R = new AdvancedModelRenderer(this, 0, 0);
        this.handJoint_R.setRotationPoint(-20.0F, 0.0F, 0.0F);
        this.handJoint_R.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.spike5 = new AdvancedModelRenderer(this, 38, 36);
        this.spike5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spike5.addBox(0.0F, 0.0F, 0.0F, 9, 4, 9, 0.0F);
        setRotateAngle(spike5, 0.0F, -0.7853981633974483F, 0.0F);
        this.shoulder1_L = new AdvancedModelRenderer(this, 189, 0);
        this.shoulder1_L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shoulder1_L.addBox(-2.0F, -3.0F, -7.0F, 12, 6, 9, 0.0F);
        setRotateAngle(shoulder1_L, 0.0F, -0.5235987755982988F, 0.0F);
        this.spike5joint = new AdvancedModelRenderer(this, 0, 0);
        this.spike5joint.setRotationPoint(0.0F, 0.0F, -4.05F);
        this.spike5joint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(spike5joint, 0.5235987755982988F, 0.0F, 0.0F);
        this.wingWebbing1_R = new AdvancedModelRenderer(this, 119, 97);
        this.wingWebbing1_R.mirror = true;
        this.wingWebbing1_R.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.wingWebbing1_R.addBox(-53.0F, 0.0F, -16.0F, 53, 0, 31, 0.0F);
        setRotateAngle(wingWebbing1_R, 0.0F, 0.30543261909900765F, 0.0F);
        this.wingWebbing1_RReversed = new AdvancedModelRenderer(this, 119, 97);
        this.wingWebbing1_RReversed.mirror = false;
        this.wingWebbing1_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing1_RReversed.addBox(-53.0F, 0.0F, -16.0F, 53, 0, 31, 0.0F);
//        wingWebbing1_RReversed.setScale(1, -1, 1);
        this.upperArmJoint_L = new AdvancedModelRenderer(this, 0, 0);
        this.upperArmJoint_L.setRotationPoint(9.0F, -1.0F, -1.0F);
        this.upperArmJoint_L.addBox(-2.0F, 0.0F, -3.0F, 0, 0, 0, 0.0F);
        setRotateAngle(upperArmJoint_L, 0.0F, 0.5235987755982988F, 0.0F);
        this.tail1 = new AdvancedModelRenderer(this, 140, 0);
        this.tail1.setRotationPoint(0.0F, 1.5F, 17.0F);
        this.tail1.addBox(-7.5F, -4.5F, -3.0F, 15, 9, 19, 0.0F);
        this.wingWebbing5_R = new AdvancedModelRenderer(this, 193, 128);
        this.wingWebbing5_R.mirror = true;
        this.wingWebbing5_R.setRotationPoint(-12.0F, 0.0F, 1.0F);
        this.wingWebbing5_R.addBox(-8.0F, 0.0F, 0.0F, 20, 0, 23, 0.0F);
        this.wingWebbing5_RReversed = new AdvancedModelRenderer(this, 193, 128);
        this.wingWebbing5_RReversed.mirror = false;
        this.wingWebbing5_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing5_RReversed.addBox(-8.0F, 0.0F, 0.0F, 20, 0, 23, 0.0F);
//        wingWebbing5_RReversed.setScale(1, -1, 1);
        this.backFin3 = new AdvancedModelRenderer(this, 170, 123);
        this.backFin3.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.backFin3.addBox(0.0F, -9.0F, -3.0F, 0, 9, 19, 0.0F);
        this.backFin3Reversed = new AdvancedModelRenderer(this, 170, 123);
        this.backFin3Reversed.mirror = true;
        this.backFin3Reversed.setRotationPoint(0.001F, 0.0F, 0.0F);
        this.backFin3Reversed.addBox(0.0F, -9.0F, -3.0F, 0, 9, 19, 0.0F);
//        backFin3Reversed.setScale(-1, 1, 1);
        this.handJoint_L = new AdvancedModelRenderer(this, 0, 0);
        this.handJoint_L.setRotationPoint(20.0F, 0.0F, 0.0F);
        this.handJoint_L.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.wingWebbing4_L = new AdvancedModelRenderer(this, 159, 36);
        this.wingWebbing4_L.setRotationPoint(0.5F, 0.0F, 0.0F);
        this.wingWebbing4_L.addBox(0.0F, 0.0F, -22.0F, 31, 0, 35, 0.0F);
        setRotateAngle(wingWebbing4_L, 0.0F, -0.6544984694978736F, 0.0F);
        this.wingWebbing4_LReversed = new AdvancedModelRenderer(this, 159, 36);
        this.wingWebbing4_LReversed.mirror = true;
        this.wingWebbing4_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing4_LReversed.addBox(0.0F, 0.0F, -22.0F, 31, 0, 35, 0.0F);
//        wingWebbing4_LReversed.setScale(1, -1, 1);
        this.wingFrame3_L = new AdvancedModelRenderer(this, 0, 73);
        this.wingFrame3_L.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.wingFrame3_L.addBox(0.0F, -1.5F, -1.5F, 43, 3, 3, 0.0F);
        setRotateAngle(wingFrame3_L, 0.0F, -1.2217304763960306F, 0.0F);
        this.wingWebbing1_L = new AdvancedModelRenderer(this, 119, 97);
        this.wingWebbing1_L.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.wingWebbing1_L.addBox(0.0F, 0.0F, -16.0F, 53, 0, 31, 0.0F);
        setRotateAngle(wingWebbing1_L, 0.0F, -0.30543261909900765F, 0.0F);
        this.wingWebbing1_LReversed = new AdvancedModelRenderer(this, 119, 97);
        this.wingWebbing1_LReversed.mirror = true;
        this.wingWebbing1_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing1_LReversed.addBox(0.0F, 0.0F, -16.0F, 53, 0, 31, 0.0F);
//        wingWebbing1_LReversed.setScale(1, -1, 1);
        this.wingClaw_R = new AdvancedModelRenderer(this, 231, 8);
        this.wingClaw_R.mirror = true;
        this.wingClaw_R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingClaw_R.addBox(-9.0F, -2.0F, -2.0F, 9, 3, 3, 0.0F);
        setRotateAngle(wingClaw_R, 0.0F, -0.5235987755982988F, 0.0F);
        this.eyebrowJoint_R = new AdvancedModelRenderer(this, 0, 0);
        this.eyebrowJoint_R.setRotationPoint(-4.95F, -5.0F, -7.36F);
        this.eyebrowJoint_R.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(eyebrowJoint_R, 1.0344566476570392F, 0.3179989897133668F, 0.5363396791378574F);
        this.wingFrame2_R = new AdvancedModelRenderer(this, 0, 79);
        this.wingFrame2_R.mirror = true;
        this.wingFrame2_R.setRotationPoint(-7.0F, 0.0F, 0.0F);
        this.wingFrame2_R.addBox(-50.0F, -1.5F, -1.5F, 50, 3, 3, 0.0F);
        setRotateAngle(wingFrame2_R, 0.0F, 0.6108652381980153F, 0.0F);
        this.wingWebbing2_R = new AdvancedModelRenderer(this, 20, 98);
        this.wingWebbing2_R.mirror = true;
        this.wingWebbing2_R.setRotationPoint(1.5F, 0.0F, 0.0F);
        this.wingWebbing2_R.addBox(-50.0F, 0.0F, -15.0F, 50, 0, 30, 0.0F);
        setRotateAngle(wingWebbing2_R, 0.0F, 0.30543261909900765F, 0.0F);
        this.wingWebbing2_RReversed = new AdvancedModelRenderer(this, 20, 98);
        this.wingWebbing2_RReversed.mirror = false;
        this.wingWebbing2_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing2_RReversed.addBox(-50.0F, 0.0F, -15.0F, 50, 0, 30, 0.0F);
//        wingWebbing2_RReversed.setScale(1, -1, 1);
        this.jaw = new AdvancedModelRenderer(this, 116, 62);
        this.jaw.setRotationPoint(0.0F, -1.34F, -4.38F);
        this.jaw.addBox(-4.5F, 0.16F, -9.37F, 9, 4, 8, 0.0F);
        setRotateAngle(jaw, -0.17453292519943295F, 0.0F, -0.005235987755982988F);
        this.neck = new AdvancedModelRenderer(this, 84, 0);
        this.neck.setRotationPoint(0.0F, 1.0F, -1.0F);
        this.neck.addBox(-6.5F, -4.0F, -12.2F, 13, 8, 15, 0.0F);
        setRotateAngle(neck, 0.17453292519943295F, 0.0F, 0.0F);
        this.spike3Bottom = new AdvancedModelRenderer(this, 78, 45);
        this.spike3Bottom.setRotationPoint(0.0F, 2.69F, 5.66F);
        this.spike3Bottom.addBox(0.0F, 0.0F, 0.0F, 11, 4, 11, 0.0F);
        setRotateAngle(spike3Bottom, 0.0F, -0.7853981633974483F, 0.0F);
        this.teethLower = new AdvancedModelRenderer(this, 125, 0);
        this.teethLower.setRotationPoint(0.0F, 0.14F, -9.4F);
        this.teethLower.addBox(-4.5F, -6.0F, 0.0F, 9, 6, 7, 0.0F);
        setRotateAngle(teethLower, -0.5009094953223726F, 0.0F, 0.0F);
        this.lowerArm_L = new AdvancedModelRenderer(this, 102, 83);
        this.lowerArm_L.setRotationPoint(0.0F, 0.0F, -0.5F);
        this.lowerArm_L.addBox(0.0F, -2.0F, -2.0F, 22, 4, 4, 0.0F);
        this.shoulder1_R = new AdvancedModelRenderer(this, 189, 0);
        this.shoulder1_R.mirror = true;
        this.shoulder1_R.setRotationPoint(0, 0, 0);
        this.shoulder1_R.addBox(-10.0F, -3.0F, -7.0F, 12, 6, 9, 0.0F);
        setRotateAngle(shoulder1_R, 0.0F, 0.5235987755982988F, 0.0F);
        this.wingFrame1_R = new AdvancedModelRenderer(this, 50, 91);
        this.wingFrame1_R.mirror = true;
        this.wingFrame1_R.setRotationPoint(-11.0F, 0.0F, -0.5F);
        this.wingFrame1_R.addBox(-53.0F, -1.5F, -1.5F, 53, 3, 3, 0.0F);
        this.teethUpper = new AdvancedModelRenderer(this, 171, 56);
        this.teethUpper.setRotationPoint(0.0F, 0.3F, -6.65F);
        this.teethUpper.addBox(-4.0F, -4.0F, -3.0F, 8, 8, 3, 0.0F);
        setRotateAngle(teethUpper, 1.5707963267948966F, 0.7853981633974483F, 0.0F);
        this.backWing_R = new AdvancedModelRenderer(this, 35, 128);
        this.backWing_R.mirror = true;
        this.backWing_R.setRotationPoint(-4.0F, 0.0F, 4.0F);
        this.backWing_R.addBox(-30.0F, 0.0F, 0.0F, 30, 0, 25, 0.0F);
        setRotateAngle(backWing_R, 0.0F, 0.5235987755982988F, 0.0F);
        this.backWing_RReversed = new AdvancedModelRenderer(this, 35, 128);
        this.backWing_RReversed.mirror = false;
        this.backWing_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.backWing_RReversed.addBox(-30.0F, 0.0F, 0.0F, 30, 0, 25, 0.0F);
//        backWing_RReversed.setScale(1, -1, 1);
        this.tailFin = new AdvancedModelRenderer(this, -30, 128);
        this.tailFin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tailFin.addBox(-5.0F, 0.0F, -5.0F, 30, 0, 30, 0.0F);
        setRotateAngle(tailFin, 0.0F, -0.7853981633974483F, 0.0F);
        this.tailFinReversed = new AdvancedModelRenderer(this, -30, 128);
        this.tailFinReversed.mirror = true;
        this.tailFinReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.tailFinReversed.addBox(-5.0F, 0.0F, -5.0F, 30, 0, 30, 0.0F);
//        tailFinReversed.setScale(1, -1, 1);
        this.lowerArmJoint_L = new AdvancedModelRenderer(this, 0, 0);
        this.lowerArmJoint_L.setRotationPoint(15.0F, 0.0F, -0.5F);
        this.lowerArmJoint_L.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.wingFrame4_L = new AdvancedModelRenderer(this, 0, 85);
        this.wingFrame4_L.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.wingFrame4_L.addBox(0.0F, -1.5F, -1.5F, 38, 3, 3, 0.0F);
        setRotateAngle(wingFrame4_L, 0.0F, -1.8325957145940461F, 0.0F);
        this.underHead = new AdvancedModelRenderer(this, 122, 51);
        this.underHead.setRotationPoint(0.0F, -1.82F, -1.59F);
        this.underHead.addBox(-4.5F, 1.12F, -4.03F, 9, 4, 7, 0.0F);
        setRotateAngle(underHead, -0.17453292519943295F, 0.0F, 0.0F);
        this.backFin2 = new AdvancedModelRenderer(this, 170, 109);
        this.backFin2.setRotationPoint(0.0F, -4.5F, 0.0F);
        this.backFin2.addBox(0.0F, -14.0F, -3.0F, 0, 14, 19, 0.0F);
        this.backFin2Reversed = new AdvancedModelRenderer(this, 170, 109);
        this.backFin2Reversed.mirror = true;
        this.backFin2Reversed.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.backFin2Reversed.addBox(0.0F, -14.0F, -3.0F, 0, 14, 19, 0.0F);
//        backFin2Reversed.setScale(-1, 1, 1);
        this.tail2 = new AdvancedModelRenderer(this, 115, 28);
        this.tail2.setRotationPoint(0.0F, -1.0F, 15.0F);
        this.tail2.addBox(-6.5F, -3.0F, -1.0F, 13, 6, 17, 0.0F);
        this.hand_L = new AdvancedModelRenderer(this, 222, 0);
        this.hand_L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hand_L.addBox(0.0F, -2.0F, -2.0F, 11, 4, 4, 0.0F);
        this.spike4joint = new AdvancedModelRenderer(this, 0, 0);
        this.spike4joint.setRotationPoint(0.0F, -1.0F, 0.55F);
        this.spike4joint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(spike4joint, 0.5235987755982988F, 0.0F, 0.0F);
        this.upperArmJoint_R = new AdvancedModelRenderer(this, 0, 0);
        this.upperArmJoint_R.setRotationPoint(-9.0F, -1.0F, -1.0F);
        this.upperArmJoint_R.addBox(-2.0F, 0.0F, -3.0F, 0, 0, 0, 0.0F);
        setRotateAngle(upperArmJoint_R, 0.0F, -0.5235987755982988F, 0.0F);
        this.spike1joint = new AdvancedModelRenderer(this, 0, 0);
        this.spike1joint.setRotationPoint(0.0F, 1.0F, -12.0F);
        this.spike1joint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(spike1joint, 0.5235987755982988F, 0.0F, 0.0F);
        this.backFin1 = new AdvancedModelRenderer(this, 120, 103);
        this.backFin1.setRotationPoint(0.0F, -4.0F, -3.0F);
        this.backFin1.addBox(0.0F, -15.0F, -4.0F, 0, 15, 25, 0.0F);
        this.backFin1Reversed = new AdvancedModelRenderer(this, 120, 127);
        this.backFin1Reversed.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.backFin1Reversed.addBox(0.0F, -15.0F, -4.0F, 0, 15, 25, 0.0F);
        this.backFin1Reversed.mirror = true;
//        backFin1Reversed.setScale(-1, 1, 1);
        this.wingWebbing4_R = new AdvancedModelRenderer(this, 159, 36);
        this.wingWebbing4_R.mirror = true;
        this.wingWebbing4_R.setRotationPoint(-0.5F, 0.0F, 0.0F);
        this.wingWebbing4_R.addBox(-31.0F, 0.0F, -22.0F, 31, 0, 35, 0.0F);
        setRotateAngle(wingWebbing4_R, 0.0F, 0.6544984694978736F, 0.0F);
        this.wingWebbing4_RReversed = new AdvancedModelRenderer(this, 159, 36);
        this.wingWebbing4_RReversed.mirror = false;
        this.wingWebbing4_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing4_RReversed.addBox(-31.0F, 0.0F, -22.0F, 31, 0, 35, 0.0F);
//        wingWebbing4_RReversed.setScale(1, -1, 1);
        this.upperArm_R = new AdvancedModelRenderer(this, 106, 74);
        this.upperArm_R.mirror = true;
        this.upperArm_R.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.upperArm_R.addBox(-16.0F, -2.0F, -3.0F, 18, 4, 5, 0.0F);
        this.backWing_L = new AdvancedModelRenderer(this, 35, 128);
        this.backWing_L.setRotationPoint(4.0F, 0.0F, 4.0F);
        this.backWing_L.addBox(0.0F, 0.0F, 0.0F, 30, 0, 25, 0.0F);
        setRotateAngle(backWing_L, 0.0F, -0.5235987755982988F, 0.0F);
        this.backWing_LReversed = new AdvancedModelRenderer(this, 35, 128);
        this.backWing_LReversed.mirror = true;
        this.backWing_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.backWing_LReversed.addBox(0.0F, 0.0F, 0.0F, 30, 0, 25, 0.0F);
//        backWing_LReversed.setScale(1, -1, 1);
        this.spike3 = new AdvancedModelRenderer(this, 0, 54);
        this.spike3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spike3.addBox(0.0F, 0.0F, 0.0F, 15, 4, 15, 0.0F);
        setRotateAngle(spike3, 0.0F, -0.7853981633974483F, 0.0F);
        this.wingWebbing6_R = new AdvancedModelRenderer(this, -24, 94);
        this.wingWebbing6_R.mirror = true;
        this.wingWebbing6_R.setRotationPoint(-0.5F, 0.0F, -2.0F);
        this.wingWebbing6_R.addBox(-14.5F, 0.0F, 0.0F, 25, 0, 24, 0.0F);
        this.wingWebbing6_RReversed = new AdvancedModelRenderer(this, -24, 94);
        this.wingWebbing6_RReversed.mirror = false;
        this.wingWebbing6_RReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing6_RReversed.addBox(-14.5F, 0.0F, 0.0F, 25, 0, 24, 0.0F);
//        wingWebbing6_RReversed.setScale(1, -1, 1);
        this.spike2joint = new AdvancedModelRenderer(this, 0, 0);
        this.spike2joint.setRotationPoint(0.0F, 1.0F, -4.0F);
        this.spike2joint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(spike2joint, 0.5235987755982988F, 0.0F, 0.0F);
        this.wingWebbing6_L = new AdvancedModelRenderer(this, -24, 94);
        this.wingWebbing6_L.setRotationPoint(0.5F, 0.0F, -2.0F);
        this.wingWebbing6_L.addBox(-10.5F, 0.0F, 0.0F, 25, 0, 24, 0.0F);
        this.wingWebbing6_LReversed = new AdvancedModelRenderer(this, -24, 94);
        this.wingWebbing6_LReversed.mirror = true;
        this.wingWebbing6_LReversed.setRotationPoint(0.0F, -0.004F, 0.0F);
        this.wingWebbing6_LReversed.addBox(-10.5F, 0.0F, 0.0F, 25, 0, 24, 0.0F);
//        wingWebbing6_LReversed.setScale(1, -1, 1);
        this.tail6 = new AdvancedModelRenderer(this, 0, 0);
        this.tail6.setRotationPoint(0.0F, 0.0F, 13.0F);
        this.tail6.addBox(-1.5F, -1.0F, 0.0F, 0, 0, 0, 0.0F);
        this.wingClaw_L = new AdvancedModelRenderer(this, 231, 8);
        this.wingClaw_L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingClaw_L.addBox(0.0F, -2.0F, -2.0F, 9, 3, 3, 0.0F);
        setRotateAngle(wingClaw_L, 0.0F, 0.5235987755982988F, 0.0F);
        this.wingFrame4_R = new AdvancedModelRenderer(this, 0, 85);
        this.wingFrame4_R.mirror = true;
        this.wingFrame4_R.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.wingFrame4_R.addBox(-38.0F, -1.5F, -1.5F, 38, 3, 3, 0.0F);
        setRotateAngle(wingFrame4_R, 0.0F, 1.8325957145940461F, 0.0F);
        this.tail4 = new AdvancedModelRenderer(this, 142, 52);
        this.tail4.setRotationPoint(0.0F, -0.5F, 15.0F);
        this.tail4.addBox(-3.5F, -1.5F, 0.0F, 7, 3, 15, 0.0F);
        this.wingFrame3_R = new AdvancedModelRenderer(this, 0, 73);
        this.wingFrame3_R.mirror = true;
        this.wingFrame3_R.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.wingFrame3_R.addBox(-43.0F, -1.5F, -1.5F, 43, 3, 3, 0.0F);
        setRotateAngle(wingFrame3_R, 0.0F, 1.2217304763960306F, 0.0F);
        this.shoulderLJoint = new AdvancedModelRenderer(this, 0, 0);
        this.shoulderLJoint.setRotationPoint(8.0F, -2.0F, -1.0F);
        this.shoulderRJoint = new AdvancedModelRenderer(this, 0, 0);
        this.shoulder1_R.setRotationPoint(-8.0F, -2.0F, -1.0F);
        this.mouthSocket = new AdvancedModelRenderer(this, 0, 0);
        this.mouthSocket.setRotationPoint(0, 5, -10);
        this.wingFolder = new AdvancedModelRenderer(this, 0, 0);
        this.tailEnd = new AdvancedModelRenderer(this, 0, 0);
        this.tailEnd.setRotationPoint(0, 0, 5);
        this.scaler = new AdvancedModelRenderer(this, 0, 0);
        this.swooper = new AdvancedModelRenderer(this, 0, 0);
        this.eyebrowJoint_L.addChild(this.eyebrow_L);
        this.spike4joint.addChild(this.spike4);
        this.body.addChild(this.spike3joint);
        this.neck.addChild(this.headJoint);
        this.root.addChild(this.body);
        this.spike2joint.addChild(this.spike2);
        this.upperArm_R.addChild(this.lowerArmJoint_R);
        this.spike1joint.addChild(this.spike1);
        this.lowerArm_L.addChild(this.wingWebbing5_L);
        this.tail4.addChild(this.tail5);
        this.tail2.addChild(this.tail3);
        this.wingFrame2_L.addChild(this.wingWebbing2_L);
        this.hand_L.addChild(this.wingFrame1_L);
        this.handJoint_R.addChild(this.hand_R);
        this.lowerArmJoint_R.addChild(this.lowerArm_R);
        this.headJoint.addChild(this.head);
        this.headJoint.addChild(this.eyebrowJoint_L);
        this.upperArmJoint_L.addChild(this.upperArm_L);
        this.hand_L.addChild(this.wingFrame2_L);
        this.eyebrowJoint_R.addChild(this.eyebrow_R);
        this.wingFrame3_R.addChild(this.wingWebbing3_R);
        this.wingFrame3_L.addChild(this.wingWebbing3_L);
        this.lowerArm_R.addChild(this.handJoint_R);
        this.spike5joint.addChild(this.spike5);
        this.body.addChild(this.shoulderLJoint);
        this.tail2.addChild(this.spike5joint);
        this.wingFrame1_R.addChild(this.wingWebbing1_R);
        this.shoulder1_L.addChild(this.upperArmJoint_L);
        this.body.addChild(this.tail1);
        this.lowerArm_R.addChild(this.wingWebbing5_R);
        this.tail2.addChild(this.backFin3);
        this.lowerArm_L.addChild(this.handJoint_L);
        this.wingFrame4_L.addChild(this.wingWebbing4_L);
        this.hand_L.addChild(this.wingFrame3_L);
        this.wingFrame1_L.addChild(this.wingWebbing1_L);
        this.hand_R.addChild(this.wingClaw_R);
        this.headJoint.addChild(this.eyebrowJoint_R);
        this.hand_R.addChild(this.wingFrame2_R);
        this.wingFrame2_R.addChild(this.wingWebbing2_R);
        this.headJoint.addChild(this.jaw);
        this.body.addChild(this.neck);
        this.spike3joint.addChild(this.spike3Bottom);
        this.jaw.addChild(this.teethLower);
        this.lowerArmJoint_L.addChild(this.lowerArm_L);
        this.body.addChild(this.shoulderRJoint);
        this.hand_R.addChild(this.wingFrame1_R);
        this.headJoint.addChild(this.teethUpper);
        this.tail2.addChild(this.backWing_R);
        this.tail6.addChild(this.tailFin);
        this.upperArm_L.addChild(this.lowerArmJoint_L);
        this.hand_L.addChild(this.wingFrame4_L);
        this.headJoint.addChild(this.underHead);
        this.tail1.addChild(this.backFin2);
        this.tail1.addChild(this.tail2);
        this.handJoint_L.addChild(this.hand_L);
        this.tail1.addChild(this.spike4joint);
        this.shoulder1_R.addChild(this.upperArmJoint_R);
        this.body.addChild(this.spike1joint);
        this.body.addChild(this.backFin1);
        this.wingFrame4_R.addChild(this.wingWebbing4_R);
        this.upperArmJoint_R.addChild(this.upperArm_R);
        this.tail2.addChild(this.backWing_L);
        this.spike3joint.addChild(this.spike3);
        this.upperArm_R.addChild(this.wingWebbing6_R);
        this.body.addChild(this.spike2joint);
        this.upperArm_L.addChild(this.wingWebbing6_L);
        this.tail5.addChild(this.tail6);
        this.hand_L.addChild(this.wingClaw_L);
        this.hand_R.addChild(this.wingFrame4_R);
        this.tail3.addChild(this.tail4);
        this.hand_R.addChild(this.wingFrame3_R);
        this.tail6.addChild(this.tailEnd);
        this.shoulderRJoint.addChild(this.shoulder1_R);
        this.shoulderLJoint.addChild(this.shoulder1_L);
        this.headJoint.addChild(this.mouthSocket);
        this.backFin3.addChild(this.backFin3Reversed);
        this.backFin2.addChild(this.backFin2Reversed);
        this.backFin1.addChild(this.backFin1Reversed);
        this.backWing_L.addChild(backWing_LReversed);
        this.backWing_R.addChild(backWing_RReversed);
        this.wingWebbing1_L.addChild(wingWebbing1_LReversed);
        this.wingWebbing2_L.addChild(wingWebbing2_LReversed);
        this.wingWebbing3_L.addChild(wingWebbing3_LReversed);
        this.wingWebbing4_L.addChild(wingWebbing4_LReversed);
        this.wingWebbing5_L.addChild(wingWebbing5_LReversed);
        this.wingWebbing6_L.addChild(wingWebbing6_LReversed);
        this.wingWebbing1_R.addChild(wingWebbing1_RReversed);
        this.wingWebbing2_R.addChild(wingWebbing2_RReversed);
        this.wingWebbing3_R.addChild(wingWebbing3_RReversed);
        this.wingWebbing4_R.addChild(wingWebbing4_RReversed);
        this.wingWebbing6_R.addChild(wingWebbing6_RReversed);
        this.wingWebbing5_R.addChild(wingWebbing5_RReversed);
        this.tailFin.addChild(tailFinReversed);

        updateDefaultPose();
        updateDefaultPoseExtendeds();

        tailOriginal = new AdvancedModelRenderer[]{tail1, tail2, tail3, tail4, tail5, tailEnd};
        tailDynamic = new AdvancedModelRenderer[tailOriginal.length];

        reversePlanes = new AdvancedModelRenderer[] {
                backWing_LReversed,
                backWing_RReversed,
                tailFinReversed,
                wingWebbing6_LReversed,
                wingWebbing5_LReversed,
                wingWebbing1_LReversed,
                wingWebbing2_LReversed,
                wingWebbing3_LReversed,
                wingWebbing4_LReversed,
                wingWebbing6_RReversed,
                wingWebbing5_RReversed,
                wingWebbing1_RReversed,
                wingWebbing2_RReversed,
                wingWebbing3_RReversed,
                wingWebbing4_RReversed
        };

        backFin1.setDoubleSided(false);
        backFin1Reversed.setDoubleSided(false);
        backFin2.setDoubleSided(false);
        backFin2Reversed.setDoubleSided(false);
        backFin3.setDoubleSided(false);
        backFin3Reversed.setDoubleSided(false);
        backWing_L.setDoubleSided(false);
        backWing_R.setDoubleSided(false);
        backWing_LReversed.setDoubleSided(false);
        backWing_RReversed.setDoubleSided(false);
        wingWebbing1_L.setDoubleSided(false);
        wingWebbing1_LReversed.setDoubleSided(false);
        wingWebbing1_R.setDoubleSided(false);
        wingWebbing1_RReversed.setDoubleSided(false);
        wingWebbing2_L.setDoubleSided(false);
        wingWebbing2_LReversed.setDoubleSided(false);
        wingWebbing2_R.setDoubleSided(false);
        wingWebbing2_RReversed.setDoubleSided(false);
        wingWebbing3_L.setDoubleSided(false);
        wingWebbing3_LReversed.setDoubleSided(false);
        wingWebbing3_R.setDoubleSided(false);
        wingWebbing3_RReversed.setDoubleSided(false);
        wingWebbing4_L.setDoubleSided(false);
        wingWebbing4_LReversed.setDoubleSided(false);
        wingWebbing4_R.setDoubleSided(false);
        wingWebbing4_RReversed.setDoubleSided(false);
        wingWebbing5_L.setDoubleSided(false);
        wingWebbing5_LReversed.setDoubleSided(false);
        wingWebbing5_R.setDoubleSided(false);
        wingWebbing5_RReversed.setDoubleSided(false);
        wingWebbing6_L.setDoubleSided(false);
        wingWebbing6_LReversed.setDoubleSided(false);
        wingWebbing6_R.setDoubleSided(false);
        wingWebbing6_RReversed.setDoubleSided(false);
        tailFin.setDoubleSided(false);
        tailFinReversed.setDoubleSided(false);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (tail != null) tail.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, tailDynamic);
        for (AdvancedModelRenderer AdvancedModelRenderer : tailOriginal) {
            AdvancedModelRenderer.showModel = false;
        }
    }

    public void setDefaultAngle(EntityNaga entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        tail = entity.dc;
        resetToDefaultPose();
        resetToDefaultPoseExtendeds();

        modelCorrections();

        float partial = delta;
        float frame = entity.ticksExisted + partial;

        float hoverAnim = entity.prevHoverAnimFrac + (entity.hoverAnimFrac - entity.prevHoverAnimFrac) * partial;
        float nonHoverAnim = 1f - hoverAnim;

        float flapAnim = entity.prevFlapAnimFrac + (entity.flapAnimFrac - entity.prevFlapAnimFrac) * partial;

        if (!entity.isPotionActive(PotionHandler.FROZEN)) {
            //Hover anim
            float globalSpeed = 0.28f;
            float globalDegree = 1f * hoverAnim;
            float flapTimingOffset = -0.5f;

            wingFolder.rotationPointX = globalDegree * flapAnim * (0.9f * (float) (0.5 * Math.cos(frame * globalSpeed + 1.4 + flapTimingOffset) + 0.5) + 0.05f);
            wingFolder.rotationPointY = globalDegree * flapAnim * (0.9f * (float) (0.5 * Math.cos(frame * globalSpeed + 1.4 + flapTimingOffset) + 0.5) + 0.05f);

            flap(shoulder1_R, globalSpeed, 1f * globalDegree * flapAnim, false, 0 + flapTimingOffset, -0.05f, frame, 1);
            flap(lowerArmJoint_R, globalSpeed, 0.7f * globalDegree * flapAnim, false, -0.6f + flapTimingOffset, 0, frame, 1);
            flap(handJoint_R, globalSpeed, 0.6f * globalDegree * flapAnim, false, -1.2f + flapTimingOffset, 0, frame, 1);

            flap(shoulder1_L, globalSpeed, 1f * globalDegree * flapAnim, true, 0 + flapTimingOffset, -0.05f, frame, 1);
            flap(lowerArmJoint_L, globalSpeed, 0.7f * globalDegree * flapAnim, true, -0.6f + flapTimingOffset, 0, frame, 1);
            flap(handJoint_L, globalSpeed, 0.6f * globalDegree * flapAnim, true, -1.2f + flapTimingOffset, 0, frame, 1);

            backWing_R.flap(globalSpeed, 0.8f * globalDegree * flapAnim, false, -1.5f + flapTimingOffset, -0.2f, frame, 1);
            backWing_L.flap(globalSpeed, 0.8f * globalDegree * flapAnim, true, -1.5f + flapTimingOffset, -0.2f, frame, 1);

            bob(root, globalSpeed, 18 * globalDegree * flapAnim, false, frame - 0.5f, 1);
            root.rotationPointY += 18 * globalDegree * flapAnim;

            body.rotateAngleX -= 0.2f * globalDegree;
            neck.rotateAngleX += 0.2f * globalDegree;
            headJoint.rotateAngleX += 0.2f * globalDegree;

            faceTarget(headYaw, headPitch, 2, neck, headJoint);

            //Glide anim
            Vec3d prevV = new Vec3d(entity.prevMotionX, entity.prevMotionY, entity.prevMotionZ);
            Vec3d dv = prevV.add(entity.getMotion().subtract(prevV).scale(delta));
            double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
            if (d != 0 && entity.getAnimation() != EntityNaga.DIE_AIR_ANIMATION) {
                double a = dv.y / d;
                a = Math.max(-1, Math.min(1, a));
                float pitch = -(float) Math.asin(a);
                root.rotateAngleX += pitch * nonHoverAnim;
                neck.rotateAngleX -= pitch / 2 * nonHoverAnim;
                head.rotateAngleX -= pitch / 2 * nonHoverAnim;
                shoulderLJoint.rotateAngleX -= Math.min(pitch, 0) * nonHoverAnim;
                shoulderRJoint.rotateAngleX -= Math.min(pitch, 0) * nonHoverAnim;

                wingFolder.rotationPointX += Math.max(Math.min(pitch * 2, 0.8), 0.1) * nonHoverAnim;
                wingFolder.rotationPointY += Math.max(Math.min(pitch * 2, 0.8), 0.1) * nonHoverAnim;

                //        root.rotateAngleZ -= Math.toRadians((entity.rotationYaw - entity.prevRotationYaw) * (LLibrary.PROXY.getPartialTicks()));
            }

            // Falling Anim
            if (entity.movement == EntityNaga.EnumNagaMovement.FALLING && entity.getAnimation() == EntityNaga.NO_ANIMATION) {
                root.rotateAngleX += Math.PI;
                root.rotateAngleY += 0.8f;
                wingFolder.rotationPointX += 0.4f;
                wingFolder.rotationPointY += 0.2f;
                shoulder1_R.rotateAngleZ += -1f;
                shoulder1_L.rotateAngleZ += 1f;
                lowerArm_L.rotateAngleZ += 0.3f;
                lowerArm_R.rotateAngleZ += -0.3f;
                handJoint_L.rotateAngleZ += 0.2f;
                handJoint_R.rotateAngleZ += -0.2f;
                neck.rotateAngleX += 0.3f;
                headJoint.rotateAngleX += 0.3f;
            } else if (entity.movement == EntityNaga.EnumNagaMovement.FALLEN && entity.getAnimation() == EntityNaga.NO_ANIMATION) {
                animator.move(root, 0, -16, 0);
                animator.rotate(root, (float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 2);
                animator.move(wingFolder, 0.6f, 0.7f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1.5f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(upperArm_L, 0.2f, 0, 0.4f);
                animator.rotate(lowerArmJoint_L, 0.4f, -0.5f, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(tail1, -0.35f, 0, 0);
                animator.rotate(tail2, -0.35f, 0, 0);
                animator.rotate(tail3, -0.35f, 0, 0);
                animator.rotate(tail4, -0.35f, 0, 0);
                animator.rotate(tail5, -0.35f, 0, 0);

                root.rotationPointY += -16;
                root.rotateAngleX += Math.PI;
                root.rotateAngleY += 0.8f;
                body.rotateAngleZ += Math.PI / 2;
                wingFolder.rotationPointX += 0.6f;
                wingFolder.rotationPointY += 0.7f;
                shoulder1_R.rotateAngleZ += -1.5f;
                shoulder1_L.rotateAngleZ += 1f;
                upperArm_L.rotateAngleX += 0.2f;
                upperArm_L.rotateAngleZ += 0.4;
                lowerArmJoint_L.rotateAngleX += 0.4f;
                lowerArmJoint_L.rotateAngleY += -0.5f;
                neck.rotateAngleX += 0.4f;
                headJoint.rotateAngleX += 0.4f;
                tail1.rotateAngleX += -0.35f;
                tail2.rotateAngleX += -0.35f;
                tail3.rotateAngleX += -0.35f;
                tail4.rotateAngleX += -0.35f;
                tail5.rotateAngleX += -0.35f;
            }

//        float roarFrac = (float)entity.roarAnimation/(float)EntityNaga.ROAR_DURATION;
//        jaw.rotateAngleX += 1.6f * (smoothBlend(roarFrac, 0.2f, 30) - smoothBlend(roarFrac, 0.8f, 30));
        }
    }

    @Override
    protected void animate(EntityNaga entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngle(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        float frame = entity.frame + delta;

        if (!entity.isPotionActive(PotionHandler.FROZEN)) {


            if (entity.getAnimation() == EntityNaga.FLAP_ANIMATION) {
                animator.setAnimation(EntityNaga.FLAP_ANIMATION);
                animator.startKeyframe(25);
                animator.rotate(wingFolder, 1, 0, 0);
                animator.endKeyframe();
                animator.resetKeyframe(0);
            }

            if (entity.getAnimation() == EntityNaga.SPIT_ANIMATION) {
                animator.setAnimation(EntityNaga.SPIT_ANIMATION);
                animator.startKeyframe(26);
                animator.rotate(body, -1f, 0, 0);
                animator.rotate(shoulderLJoint, 0.9f, 0, 0);
                animator.rotate(shoulderRJoint, 0.9f, 0, 0);
                animator.move(body, 0, -14, 28);
                animator.rotate(headJoint, 0.1f, 0, 0);
                animator.rotate(neck, 0.1f, 0, 0);
                animator.move(scaler, 0.2f, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(4);
                animator.startKeyframe(6);
                animator.rotate(body, 0.4f, 0, 0);
                animator.move(body, 0, 5, -10);
                animator.rotate(headJoint, -0.7f, 0, 0);
                animator.rotate(neck, -0.4f, 0, 0);
                animator.move(scaler, 0, 1, 0);
                animator.rotate(jaw, 1.8f, 0, 0);
                animator.endKeyframe();
                animator.startKeyframe(0);
                animator.rotate(body, 0.4f, 0, 0);
                animator.move(body, 0, 5, -10);
                animator.rotate(headJoint, -0.7f, 0, 0);
                animator.rotate(neck, -0.4f, 0, 0);
                animator.move(scaler, 0, 0, 0);
                animator.rotate(jaw, 1.8f, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(4);
                animator.resetKeyframe(10);

                body.setScale(1 + scaler.rotationPointX, 1 + scaler.rotationPointX * 2, 1 + scaler.rotationPointX);
                body.scaleChildren = false;
                float scaleSpeed = 1f;
                float neckScaler = 0.6f * (float) Math.max(Math.pow(Math.sin((scaler.rotationPointY * scaleSpeed + (1 - scaleSpeed) / 2) * Math.PI + 0.4), 3), 0);
                neck.setScale(1 + neckScaler, 1 + neckScaler * 1.5f, 1 + neckScaler);
                neck.rotationPointY += neckScaler * 6;
                headJoint.rotationPointY -= neckScaler * 6;
                neck.scaleChildren = false;
                float headScaler = 0.5f * (float) Math.max(Math.pow(Math.sin((scaler.rotationPointY * scaleSpeed + (1 - scaleSpeed) / 2) * Math.PI - 0.4), 3), 0);
                headJoint.setScale(1 + headScaler, 1 + headScaler, 1 + headScaler);
                headJoint.rotationPointY += headScaler * 6;
                headJoint.rotationPointZ -= headScaler * 6;
                headJoint.scaleChildren = true;
            } else {
                neck.setScale(1, 1, 1);
                body.setScale(1, 1, 1);
                headJoint.setScale(1, 1, 1);
            }

            if (entity.getAnimation() == EntityNaga.SWOOP_ANIMATION) {
                animator.setAnimation(EntityNaga.SWOOP_ANIMATION);
                int phase1Time = 15;
                animator.startKeyframe(3);
                animator.move(root, -16, -16, 18);
                animator.rotate(root, 0, -0.2f, 0);
                animator.rotate(body, -0.4f, 0, 0);
                animator.rotate(neck, 0.2f, 0, 0);
                animator.rotate(headJoint, 0.2f, 0, 0);
                animator.move(wingFolder, 0.1f, 0.1f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.6f);
                animator.rotate(lowerArm_R, 0, 0, -0.6f);
                animator.endKeyframe();
                animator.setStaticKeyframe(1);
                animator.startKeyframe(phase1Time);
                animator.move(swooper, 1, 0, 0);
                animator.endKeyframe();

                animator.startKeyframe(0);
                animator.move(root, 0, -10, 10);
                animator.move(wingFolder, 0.530f - 0.107f, 0.161f - 0.108f, 0);
                animator.rotate(shoulder1_R, 0, 0, 0.396f - 0.069f);
                animator.rotate(shoulder1_L, 0, 0.185f, -0.765f + 0.069f);
                animator.rotate(body, -0.696f + 0.153f, 0.185f, 0.027f);
                animator.move(body, 9.241f, 0, 0);
                animator.rotate(neck, 0.497f - 0.195f, 0.092f, 0);
                animator.rotate(headJoint, 0.497f - 0.195f, 0.092f, 0);
                animator.rotate(tail1, 0, -0.277f, 0);
                animator.rotate(tail2, 0, -0.277f, 0);
                animator.rotate(tail3, 0, -0.277f, 0);
                animator.rotate(tail4, 0, -0.277f, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(5);
                animator.startKeyframe(8);
                animator.move(wingFolder, 0.8f, 0.8f, 0);
                animator.rotate(shoulder1_R, 0, 0, -0.6f);
                animator.rotate(shoulder1_L, 0, 0, -0.6f);
                animator.rotate(handJoint_R, -1.0f, 0, 0);
                animator.rotate(handJoint_L, 1.0f, 0, 0);
                animator.rotate(jaw, 2f, 0, 0);
                animator.rotate(headJoint, -1.3f, 0, 0);
                animator.move(swooper, 0, 1, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(15);
                animator.startKeyframe(7);
                animator.rotate(body, 0, 0, (float) (Math.PI * 2));
                animator.endKeyframe();


                root.rotationPointX += 30 * (float) Math.sin(swooper.rotationPointX * Math.PI * 2);
                root.rotationPointZ -= 10 * (float) (swooper.rotationPointX * (-Math.cos(swooper.rotationPointX * 2 * Math.PI) + 1) - Math.pow(swooper.rotationPointX, 2));
                root.rotateAngleY += 2 * Math.PI * swooper.rotationPointX * swooper.rotationPointX;
                root.rotationPointY += 10 * (float) (swooper.rotationPointX * (-Math.cos(swooper.rotationPointX * 2 * Math.PI) + 1) - Math.pow(swooper.rotationPointX, 2));
                wingFolder.rotationPointX += 0.9 * smoothBlend(swooper.rotationPointX, 0.1f, 20) - 0.4 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                wingFolder.rotationPointY += 0.9 * smoothBlend(swooper.rotationPointX, 0.1f, 20) - 0.8 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                shoulder1_R.rotateAngleZ += 1.0 * smoothBlend(swooper.rotationPointX, 0.1f, 20) - 0.6 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                shoulder1_L.rotateAngleZ -= 1.0 * smoothBlend(swooper.rotationPointX, 0.1f, 20) - 0.2 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                shoulder1_L.rotateAngleY += 0.2 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                body.rotateAngleX += 1.0 * smoothBlend(swooper.rotationPointX, 0.15f, 20) - 1.5 * smoothBlend(swooper.rotationPointX, 0.7f, 20);
                body.rotateAngleZ -= 0.6 * smoothBlend(swooper.rotationPointX, 0.15f, 20) - 0.6 * smoothBlend(swooper.rotationPointX, 0.7f, 20);
                neck.rotateAngleX += -0.8 * smoothBlend(swooper.rotationPointX, 0.25f, 20) + 1.1 * smoothBlend(swooper.rotationPointX, 0.7f, 20);
                headJoint.rotateAngleX += -0.8 * smoothBlend(swooper.rotationPointX, 0.25f, 20) + 1.1 * smoothBlend(swooper.rotationPointX, 0.7f, 20);
                body.rotationPointX += 10 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                body.rotateAngleY += 0.2 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                headJoint.rotateAngleY += 0.1 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                neck.rotateAngleY += 0.1 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                tail1.rotateAngleY -= 0.3 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                tail2.rotateAngleY -= 0.3 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                tail3.rotateAngleY -= 0.3 * smoothBlend(swooper.rotationPointX, 0.95f, 50);
                tail4.rotateAngleY -= 0.3 * smoothBlend(swooper.rotationPointX, 0.95f, 50);

                body.rotateAngleZ += 1f * frame % (Math.PI * 2) * swooper.rotationPointY;

                if (entity.getAnimationTick() >= 23 && entity.getAnimationTick() < 60) {
                    Vec3d prevV = new Vec3d(entity.prevMotionX, entity.prevMotionY, entity.prevMotionZ);
                    Vec3d dv = prevV.add(entity.getMotion().subtract(prevV).scale(delta));
                    double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
                    if (d != 0) {
                        double a = dv.y / d;
                        a = Math.max(-1, Math.min(1, a));
                        float pitch = -(float) Math.asin(a);
                        root.rotateAngleX += pitch * swooper.rotationPointY;
                    }
                }

            }

            if (entity.getAnimation() == EntityNaga.HURT_TO_FALL_ANIMATION) {
                animator.setAnimation(EntityNaga.HURT_TO_FALL_ANIMATION);
                animator.startKeyframe(20);
                animator.rotate(root, -(float) (2.5 * Math.PI * 2), 0.8f, (float) (Math.PI * 2));
                animator.move(wingFolder, 0.4f, 0.2f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.3f);
                animator.rotate(lowerArm_R, 0, 0, -0.3f);
                animator.rotate(handJoint_L, 0, 0, 0.2f);
                animator.rotate(handJoint_R, 0, 0, -0.2f);
                animator.rotate(neck, 0.5f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.endKeyframe();
                animator.startKeyframe(0);
                animator.rotate(root, (float) (Math.PI), 0.8f, 0);
                animator.move(wingFolder, 0.4f, 0.2f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.3f);
                animator.rotate(lowerArm_R, 0, 0, -0.3f);
                animator.rotate(handJoint_L, 0, 0, 0.2f);
                animator.rotate(handJoint_R, 0, 0, -0.2f);
                animator.rotate(neck, 0.3f, 0, 0);
                animator.rotate(headJoint, 0.3f, 0, 0);
                animator.endKeyframe();
            }

            if (entity.getAnimation() == EntityNaga.LAND_ANIMATION) {
                animator.setAnimation(EntityNaga.LAND_ANIMATION);
                animator.startKeyframe(0);
                animator.rotate(root, (float) (Math.PI), 0.8f, 0);
                animator.move(root, 0, -11, 0);
                animator.move(wingFolder, 0.4f, 0.2f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.3f);
                animator.rotate(lowerArm_R, 0, 0, -0.3f);
                animator.rotate(handJoint_L, 0, 0, 0.2f);
                animator.rotate(handJoint_R, 0, 0, -0.2f);
                animator.rotate(neck, 0.3f, 0, 0);
                animator.rotate(headJoint, 0.3f, 0, 0);
                animator.endKeyframe();

                animator.startKeyframe(4);
                animator.move(root, 0, -34, 0);
                animator.rotate(root, (float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 4);
                animator.move(wingFolder, 0.6f, 0.6f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.3f);
                animator.rotate(lowerArm_R, 0, 0, -0.3f);
                animator.rotate(handJoint_L, 0, 0, 0.2f);
                animator.rotate(handJoint_R, 0, 0, -0.2f);
                animator.rotate(neck, 0.3f, 0, 0);
                animator.rotate(headJoint, 0.3f, 0, 0);
                animator.endKeyframe();
                animator.startKeyframe(4);
                animator.move(root, 0, -16, 0);
                animator.rotate(root, (float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 2);
                animator.move(wingFolder, 0.6f, 0.7f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1.5f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(upperArm_L, 0.2f, 0, 0.4f);
                animator.rotate(lowerArmJoint_L, 0.4f, -0.5f, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(tail1, -0.35f, 0, 0);
                animator.rotate(tail2, -0.35f, 0, 0);
                animator.rotate(tail3, -0.35f, 0, 0);
                animator.rotate(tail4, -0.35f, 0, 0);
                animator.rotate(tail5, -0.35f, 0, 0);
                animator.endKeyframe();

            }

            if (entity.getAnimation() == EntityNaga.GET_UP_ANIMATION) {
                animator.setAnimation(EntityNaga.GET_UP_ANIMATION);
                animator.startKeyframe(0);
                animator.move(root, 0, -16, 0);
                animator.rotate(root, -(float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 2);
                animator.move(wingFolder, 0.6f, 0.7f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1.5f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(upperArm_L, 0.2f, 0, 0.4f);
                animator.rotate(lowerArmJoint_L, 0.4f, -0.5f, 0);
                animator.rotate(handJoint_L, 0f, 0, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(tail1, -0.35f, 0, 0);
                animator.rotate(tail2, -0.35f, 0, 0);
                animator.rotate(tail3, -0.35f, 0, 0);
                animator.rotate(tail4, -0.35f, 0, 0);
                animator.rotate(tail5, -0.35f, 0, 0);
                animator.endKeyframe();
                animator.startKeyframe(13);
                animator.move(wingFolder, 0.3f, 0.3f, 0);
                animator.move(root, 0, -35, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(body, -0.8f, 0, 0);
                animator.rotate(shoulder1_R, 0, 0, 1f);
                animator.rotate(shoulder1_L, 0, 0, -1f);
                animator.rotate(shoulderRJoint, 0.8f, 0, 0);
                animator.rotate(shoulderLJoint, 0.8f, 0, 0);
                animator.rotate(lowerArmJoint_R, 0, 0, 0.3f);
                animator.rotate(lowerArmJoint_L, 0, 0, -0.3f);
                animator.endKeyframe();
                animator.setStaticKeyframe(2);
                animator.startKeyframe(4);
                animator.move(wingFolder, 0.1f, 0.1f, 0);
                animator.move(root, 0, 0, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(body, -0.8f, 0, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(shoulderRJoint, 0.8f, 0, 0);
                animator.rotate(shoulderLJoint, 0.8f, 0, 0);
                animator.rotate(lowerArmJoint_R, 0, 0, -0.3f);
                animator.rotate(lowerArmJoint_L, 0, 0, 0.3f);
                animator.endKeyframe();
                animator.setStaticKeyframe(8);
                animator.resetKeyframe(6);
            }

            if (entity.getAnimation() == EntityNaga.DIE_AIR_ANIMATION) {
                animator.setAnimation(EntityNaga.DIE_AIR_ANIMATION);
                animator.startKeyframe(20);
                animator.rotate(root, -(float) (2.5 * Math.PI * 2), 0.8f, (float) (Math.PI * 2));
                animator.move(wingFolder, 0.4f, 0.2f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.3f);
                animator.rotate(lowerArm_R, 0, 0, -0.3f);
                animator.rotate(handJoint_L, 0, 0, 0.2f);
                animator.rotate(handJoint_R, 0, 0, -0.2f);
                animator.rotate(neck, 0.5f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.endKeyframe();
                animator.startKeyframe(0);
                animator.rotate(root, (float) (Math.PI), 0.8f, 0);
                animator.move(wingFolder, 0.4f, 0.2f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(lowerArm_L, 0, 0, 0.3f);
                animator.rotate(lowerArm_R, 0, 0, -0.3f);
                animator.rotate(handJoint_L, 0, 0, 0.2f);
                animator.rotate(handJoint_R, 0, 0, -0.2f);
                animator.rotate(neck, 0.3f, 0, 0);
                animator.rotate(headJoint, 0.3f, 0, 0);
                animator.endKeyframe();
                animator.setStaticKeyframe(30);
            }

            if (entity.getAnimation() == EntityNaga.DIE_GROUND_ANIMATION) {
                animator.setAnimation(EntityNaga.DIE_GROUND_ANIMATION);
                animator.startKeyframe(0);
                animator.move(root, 0, -16, 0);
                animator.rotate(root, -(float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 2);
                animator.move(wingFolder, 0.6f, 0.7f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1.5f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(upperArm_L, 0.2f, 0, 0.4f);
                animator.rotate(lowerArmJoint_L, 0.4f, -0.5f, 0);
                animator.rotate(handJoint_L, 0f, 0, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(tail1, -0.35f, 0, 0);
                animator.rotate(tail2, -0.35f, 0, 0);
                animator.rotate(tail3, -0.35f, 0, 0);
                animator.rotate(tail4, -0.35f, 0, 0);
                animator.rotate(tail5, -0.35f, 0, 0);
                animator.endKeyframe();

                animator.startKeyframe(6);
                animator.move(root, 0, -16, 0);
                animator.rotate(root, -(float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 2);
                animator.move(wingFolder, 0.6f, 0.2f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1.5f);
                animator.rotate(shoulder1_L, 0, 0, 0.7f);
                animator.rotate(upperArm_L, 0.2f, 0, 0.3f);
                animator.rotate(lowerArmJoint_L, -0.1f, -0.5f, 0);
                animator.rotate(handJoint_L, 0f, 0, 0);
                animator.rotate(neck, 0.1f, -0.5f, 0);
                animator.rotate(headJoint, -0.2f, -0.5f, 0);
                animator.rotate(jaw, 1.3f, 0, 0);
                animator.rotate(tail1, 0, 0.3f, 0);
                animator.rotate(tail2, 0, 0.3f, 0);
                animator.rotate(tail3, 0, 0.3f, 0);
                animator.rotate(tail4, 0, 0.3f, 0);
                animator.rotate(tail5, 0, 0.3f, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(18);

                animator.startKeyframe(10);
                animator.move(root, 0, -16, 0);
                animator.rotate(root, -(float) (Math.PI), 0.8f, 0);
                animator.rotate(body, 0, 0, (float) Math.PI / 2);
                animator.move(wingFolder, 0.6f, 0.7f, 0);
                animator.rotate(shoulder1_R, 0, 0, -1.5f);
                animator.rotate(shoulder1_L, 0, 0, 1f);
                animator.rotate(upperArm_L, 0.2f, 0, 0.4f);
                animator.rotate(lowerArmJoint_L, 0.4f, -0.5f, 0);
                animator.rotate(handJoint_L, 0f, 0, 0);
                animator.rotate(neck, 0.4f, 0, 0);
                animator.rotate(headJoint, 0.4f, 0, 0);
                animator.rotate(tail1, -0.35f, 0, 0);
                animator.rotate(tail2, -0.35f, 0, 0);
                animator.rotate(tail3, -0.35f, 0, 0);
                animator.rotate(tail4, -0.35f, 0, 0);
                animator.rotate(tail5, -0.35f, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(16);
            }

            if (entity.getAnimation() == EntityNaga.TAIL_DEMO_ANIMATION) {
                animator.setAnimation(EntityNaga.TAIL_DEMO_ANIMATION);

                animator.startKeyframe(7);
                animator.move(root, 40, 0, 0);
                animator.endKeyframe();

                animator.setStaticKeyframe(10);

                animator.resetKeyframe(7);

                animator.setStaticKeyframe(10);

                animator.startKeyframe(3);
                animator.move(root, 0, -20, 0);
                animator.endKeyframe();

                animator.resetKeyframe(3);

                animator.setStaticKeyframe(10);

                animator.startKeyframe(4);
                animator.rotate(root, 0, 1f, 0);
                animator.endKeyframe();

                animator.startKeyframe(4);
                animator.rotate(root, 0, -1f, 0);
                animator.endKeyframe();

                animator.startKeyframe(4);
                animator.rotate(root, 0, 1f, 0);
                animator.endKeyframe();

                animator.startKeyframe(4);
                animator.rotate(root, 0, -1f, 0);
                animator.endKeyframe();

                animator.resetKeyframe(4);

                animator.setStaticKeyframe(10);
            }

            float globalSpeed = 0.27f;
            float globalDegree = 1.1f;

            float flapFrame = (float) ((wingFolder.rotateAngleX * Math.PI * 2f / globalSpeed) - (Math.PI * 0.5 / globalSpeed));
            globalDegree *= 1 - Math.pow(Math.sin(wingFolder.rotateAngleX * Math.PI - Math.PI / 2), 8);

            wingFolder.rotationPointX += globalDegree * (0.9f * (float) (0.5 * Math.cos(flapFrame * globalSpeed + 1.4) + 0.5) + 0.05f);
            wingFolder.rotationPointY += globalDegree * (0.9f * (float) (0.5 * Math.cos(flapFrame * globalSpeed + 1.4) + 0.5) + 0.05f);

            flap(shoulder1_R, globalSpeed, 1f * globalDegree, false, 0, 0, flapFrame, 1);
            flap(lowerArmJoint_R, globalSpeed, 0.7f * globalDegree, false, -0.6f, 0, flapFrame, 1);
            flap(handJoint_R, globalSpeed, 0.6f * globalDegree, false, -1.2f, 0, flapFrame, 1);

            flap(shoulder1_L, globalSpeed, 1f * globalDegree, true, 0, 0, flapFrame, 1);
            flap(lowerArmJoint_L, globalSpeed, 0.7f * globalDegree, true, -0.6f, 0, flapFrame, 1);
            flap(handJoint_L, globalSpeed, 0.6f * globalDegree, true, -1.2f, 0, flapFrame, 1);

            backWing_R.flap(globalSpeed, 1f * globalDegree, false, -0.5f, -0.2f, flapFrame, 1);
            backWing_L.flap(globalSpeed, 1f * globalDegree, true, -0.5f, -0.2f, flapFrame, 1);

            jawControls();
            wingFoldControls();

            entity.dc.updateChain(Minecraft.getInstance().getRenderPartialTicks(), tailOriginal, tailDynamic, 0.5f, 0.5f, 0.5f, 0.97f, 30, true);

            computeWingWebbing();

            entity.shoulderRot = shoulder1_R.rotateAngleZ;
        }
    }

    private void jawControls() {
        teethUpper.setScale(1, 1, 0.5f + 0.5f * Math.min(jaw.rotateAngleX, 1));
        underHead.setScale(1, 1, 1f - 0.2f * Math.min(jaw.rotateAngleX, 1));
        underHead.rotationPointZ += 1.2 * Math.min(jaw.rotateAngleX, 1);
    }

    private void wingFoldControls() {
        shoulder1_R.rotateAngleY += 0.8 * wingFolder.rotationPointX;
        upperArm_R.rotateAngleY += 0.3 * wingFolder.rotationPointX;
        lowerArm_R.rotateAngleY -= 2.7 * wingFolder.rotationPointX;
        hand_R.rotateAngleY += 1.8 * wingFolder.rotationPointX;
        wingFrame1_R.rotateAngleY += 0.8 * wingFolder.rotationPointX;
        wingFrame2_R.rotateAngleY += 0.3 * wingFolder.rotationPointX;
        wingFrame3_R.rotateAngleY -= 0.2 * wingFolder.rotationPointX;
        wingFrame4_R.rotateAngleY -= 0.7 * wingFolder.rotationPointX;

        shoulder1_L.rotateAngleY -= 0.8 * wingFolder.rotationPointY;
        upperArm_L.rotateAngleY -= 0.3 * wingFolder.rotationPointY;
        lowerArm_L.rotateAngleY += 2.7 * wingFolder.rotationPointY;
        hand_L.rotateAngleY -= 1.8 * wingFolder.rotationPointY;
        wingFrame1_L.rotateAngleY -= 0.8 * wingFolder.rotationPointY;
        wingFrame2_L.rotateAngleY -= 0.3 * wingFolder.rotationPointY;
        wingFrame3_L.rotateAngleY += 0.2 * wingFolder.rotationPointY;
        wingFrame4_L.rotateAngleY += 0.7 * wingFolder.rotationPointY;
    }

    private void modelCorrections() {
        backWing_R.rotationPointX -= 2;
        backWing_L.rotationPointX += 2;
        teethLower.setScale(1.01f, 1.01f, 1.01f);
        spike2joint.scaleChildren = true;
        spike2joint.setScale(0.99f, 1, 1);
        spike2joint.rotationPointY += 0.28;
        spike2joint.rotationPointZ -= 0.15;
        spike3joint.scaleChildren = true;
        spike3joint.setScale(0.99f, 1, 1);
        spike3joint.rotationPointY += 0.28;
        spike3joint.rotationPointZ -= 0.15;
        spike3Bottom.setScale(1, 1.8f, 1);
        spike4joint.scaleChildren = true;
        spike4joint.setScale(0.993f, 1, 1);
        spike4joint.rotationPointY += 0.35;
        spike4joint.rotationPointZ += 0.7;
        spike4.setScale(1, 1f, 1);
        spike5joint.scaleChildren = true;
        spike5joint.setScale(1.02f, 1, 1);
        spike5joint.rotationPointY += 0.15;
        spike5joint.rotationPointZ -= 0.42;
        tail1.setScale(1.03f, 1, 1);
        root.rotationPointY += 22;

        backFin1.rotationPointX += 0.001;
        backFin1Reversed.rotationPointX -= 0.002;
        backFin2.rotationPointX += 0.0005;
        backFin2Reversed.rotationPointX -= 0.001;
        handJoint_L.rotationPointY += 0.01;
        handJoint_R.rotationPointY += 0.01;

        for (AdvancedModelRenderer m : reversePlanes) {
            setRotateAngle(m, (float)Math.PI, (float)Math.PI, 0);
        }

        backWing_LReversed.rotationPointX = 30;
        backWing_RReversed.rotationPointX = -30;
        tailFinReversed.rotationPointX = 20;
        wingWebbing1_LReversed.rotationPointX = 53;
        wingWebbing1_RReversed.rotationPointX = -53;
        wingWebbing2_LReversed.rotationPointX = 50;
        wingWebbing2_RReversed.rotationPointX = -50;
        wingWebbing3_LReversed.rotationPointX = 43;
        wingWebbing3_RReversed.rotationPointX = -43;
        wingWebbing4_LReversed.rotationPointX = 31;
        wingWebbing4_RReversed.rotationPointX = -31;
        wingWebbing5_LReversed.rotationPointX = -4;
        wingWebbing5_RReversed.rotationPointX = 4;
        wingWebbing6_LReversed.rotationPointX = 4;
        wingWebbing6_RReversed.rotationPointX = -4;
        backFin1Reversed.rotateAngleY = (float) Math.PI;
        backFin1Reversed.rotationPointZ = 20;
    }

    private void computeWingWebbing() {
        wingWebbing1_L.scaleChildren = true;
        wingWebbing2_L.scaleChildren = true;
        wingWebbing3_L.scaleChildren = true;
        wingWebbing4_L.scaleChildren = true;
        wingWebbing5_L.scaleChildren = true;
        wingWebbing6_L.scaleChildren = true;
        wingWebbing1_R.scaleChildren = true;
        wingWebbing2_R.scaleChildren = true;
        wingWebbing3_R.scaleChildren = true;
        wingWebbing4_R.scaleChildren = true;
        wingWebbing6_R.scaleChildren = true;
        wingWebbing5_R.scaleChildren = true;
        float webbing1LScale = (float) ((wingFrame1_L.rotateAngleY - wingFrame2_L.rotateAngleY)/(Math.toRadians(35)));
        webbing1LScale = 0.8f * webbing1LScale + 0.2f;
        float webbing1LRot = (float) (-(wingFrame1_L.rotateAngleY - (wingFrame2_L.rotateAngleY + Math.toRadians(35)))/2);
        wingWebbing1_L.rotateAngleY += webbing1LRot;
        wingWebbing1_L.setScale(1 - webbing1LScale * 0.07f, 1, webbing1LScale);
        float webbing2LScale = (float) ((wingFrame2_L.rotateAngleY - wingFrame3_L.rotateAngleY)/(Math.toRadians(35)));
        webbing2LScale = 0.8f * webbing2LScale + 0.2f;
        float webbing2LRot = (float) (-(wingFrame2_L.rotateAngleY - (wingFrame3_L.rotateAngleY + Math.toRadians(35)))/2);
        wingWebbing2_L.rotateAngleY += webbing2LRot;
        wingWebbing2_L.setScale(1 - webbing2LScale * 0.07f, 1, webbing2LScale);
        float webbing3LScale = (float) ((wingFrame3_L.rotateAngleY - wingFrame4_L.rotateAngleY)/(Math.toRadians(35)));
        webbing3LScale = 0.8f * webbing3LScale + 0.2f;
        float webbing3LRot = (float) (-(wingFrame3_L.rotateAngleY - (wingFrame4_L.rotateAngleY + Math.toRadians(35)))/2);
        wingWebbing3_L.rotateAngleY += webbing3LRot;
        wingWebbing3_L.setScale(1 - webbing3LScale * 0.07f, 1, webbing3LScale);
        float webbing4LScale = 1 - (float) (-0.5 * hand_L.rotateAngleY);
        float webbing4LRot = (float) (-(wingFrame4_L.rotateAngleY + hand_L.rotateAngleY + Math.toRadians(105))/2);
        wingWebbing4_L.rotateAngleY += webbing4LRot;
        wingWebbing4_L.setScale(1 - webbing4LScale * 0.02f, 1, webbing4LScale);
        wingWebbing5_L.rotationPointX += 12 * (float) (upperArm_L.rotateAngleY/Math.toRadians(35));
        wingWebbing5_L.setScale((float) (2 - (-shoulder1_L.rotateAngleY + shoulder1_L.defaultRotationY)/1.5), 1, (float) (0.915 - (-shoulder1_L.rotateAngleY + shoulder1_L.defaultRotationY)/1.2));
        wingWebbing6_L.setScale(1 + (-shoulder1_L.rotateAngleY + shoulder1_L.defaultRotationY)/4, 1, (float) (1 - (-shoulder1_L.rotateAngleY + shoulder1_L.defaultRotationY)/1.6));
        wingWebbing6_L.rotationPointX += 14 * (-shoulder1_L.rotateAngleY + shoulder1_L.defaultRotationY);

        float webbing1RScale = (float) (-(wingFrame1_R.rotateAngleY - wingFrame2_R.rotateAngleY)/(Math.toRadians(35)));
        webbing1RScale = 0.8f * webbing1RScale + 0.2f;
        float webbing1RRot = (float) (-(wingFrame1_R.rotateAngleY - (wingFrame2_R.rotateAngleY - Math.toRadians(35)))/2);
        wingWebbing1_R.rotateAngleY += webbing1RRot;
        wingWebbing1_R.setScale(1 - webbing1RScale * 0.07f, 1, webbing1RScale);
        float webbing2RScale = (float) (-(wingFrame2_R.rotateAngleY - wingFrame3_R.rotateAngleY)/(Math.toRadians(35)));
        webbing2RScale = 0.8f * webbing2RScale + 0.2f;
        float webbing2RRot = (float) (-(wingFrame2_R.rotateAngleY - (wingFrame3_R.rotateAngleY - Math.toRadians(35)))/2);
        wingWebbing2_R.rotateAngleY += webbing2RRot;
        wingWebbing2_R.setScale(1 - webbing2RScale * 0.07f, 1, webbing2RScale);
        float webbing3RScale = (float) (-(wingFrame3_R.rotateAngleY - wingFrame4_R.rotateAngleY)/(Math.toRadians(35)));
        webbing3RScale = 0.8f * webbing3RScale + 0.2f;
        float webbing3RRot = (float) (-(wingFrame3_R.rotateAngleY - (wingFrame4_R.rotateAngleY - Math.toRadians(35)))/2);
        wingWebbing3_R.rotateAngleY += webbing3RRot;
        wingWebbing3_R.setScale(1 - webbing3RScale * 0.07f, 1, webbing3RScale);
        float webbing4RScale = 1 - (float) (0.5 * hand_R.rotateAngleY);
        float webbing4RRot = (float) (-(wingFrame4_R.rotateAngleY + hand_R.rotateAngleY - Math.toRadians(105))/2);
        wingWebbing4_R.rotateAngleY += webbing4RRot;
        wingWebbing4_R.setScale(1 - webbing4RScale * 0.02f, 1, webbing4RScale);
        wingWebbing5_R.rotationPointX -= 12 * (float) (-upperArm_R.rotateAngleY/Math.toRadians(35));
        wingWebbing5_R.setScale((float) (2 - (shoulder1_R.rotateAngleY - shoulder1_R.defaultRotationY)/1.5), 1, (float) (0.915 - (shoulder1_R.rotateAngleY - shoulder1_R.defaultRotationY)/1.2));
        wingWebbing6_R.setScale(1 + (shoulder1_R.rotateAngleY - shoulder1_R.defaultRotationY)/4, 1, (float) (1 - (shoulder1_R.rotateAngleY - shoulder1_R.defaultRotationY)/1.6));
        wingWebbing6_R.rotationPointX += 14 * (shoulder1_L.rotateAngleY - shoulder1_L.defaultRotationY);

//        wingWebbing4_L.rotateAngleX -= 0.9/2 * (lowerArmJoint_L.rotateAngleZ + handJoint_L.rotateAngleZ * 2);
//        wingWebbing4_L.rotateAngleZ += 0.5/2 * (lowerArmJoint_L.rotateAngleZ + handJoint_L.rotateAngleZ * 2);
//        wingWebbing4_R.rotateAngleX -= 0.9/2 * (-lowerArmJoint_R.rotateAngleZ - handJoint_R.rotateAngleZ * 2);
//        wingWebbing4_R.rotateAngleZ -= 0.5/2 * (-lowerArmJoint_R.rotateAngleZ - handJoint_R.rotateAngleZ * 2);

        wingWebbing4_R.rotationPointY -= 0.01f;
        wingWebbing4_L.rotationPointY -= 0.01f;
        wingWebbing5_R.rotationPointY += 0.01f;
        wingWebbing5_L.rotationPointY += 0.01f;
        wingWebbing3_R.rotationPointY += 0.01f;
        wingWebbing3_L.rotationPointY += 0.01f;

        tailFin.rotationPointY -= 0.1f;

        if (tailDynamic[0] != null) {
            backFin1.setScale(1, 1, 1 - 0.5f * tailDynamic[0].rotateAngleX);
            backFin1.scaleChildren = true;
            backFin1.rotationPointX += 0.002;
            backFin1Reversed.rotationPointX -= 0.004;

            backFin2.setScale(1, 1, 1 - 0.2f * tailDynamic[1].rotateAngleX);
            backFin2.scaleChildren = true;
            backFin2.rotationPointX += 0.001;
            backFin2Reversed.rotationPointX -= 0.002;
        }
    }

    /**
     * Sets the default pose to the current pose of this model
     */
    public void updateDefaultPoseExtendeds() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer extendedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            extendedModelRenderer.updateDefaultPose();
        });
    }

    /**
     * Sets the current pose to the previously set default pose
     */
    public void resetToDefaultPoseExtendeds() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer extendedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            extendedModelRenderer.resetToDefaultPose();
        });
    }

    private float smoothBlend(float x, float center, float speed) {
        return (float) (1.0 / (1 + Math.exp(-speed * (x - center))));
    }
}