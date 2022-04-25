package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelBarakoa<T extends EntityBarakoa> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer modelCore;
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer chest;
    public AdvancedModelRenderer thighLeft;
    public AdvancedModelRenderer thighRight;
    public AdvancedModelRenderer loinClothFront;
    public AdvancedModelRenderer loinClothBack;
    public AdvancedModelRenderer armRightJoint;
    public AdvancedModelRenderer armLeftJoint;
    public AdvancedModelRenderer neckJoint;
    public AdvancedModelRenderer armUpperRight;
    public AdvancedModelRenderer armLowerRight;
    public AdvancedModelRenderer handRight;
    public AdvancedModelRenderer spearBase;
    public AdvancedModelRenderer bone;
    public AdvancedModelRenderer spear;
    public AdvancedModelRenderer blowgun;
    public AdvancedModelRenderer staff;
    public AdvancedModelRenderer staffEnd;
    public AdvancedModelRenderer armUpperLeft;
    public AdvancedModelRenderer armLowerLeft;
    public AdvancedModelRenderer handLeft;
    public AdvancedModelRenderer shieldBase;
    public AdvancedModelRenderer shield;
    public AdvancedModelRenderer neck;
    public AdvancedModelRenderer headJoint;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer maskBase;
    public AdvancedModelRenderer earLeft;
    public AdvancedModelRenderer earRight;
    public AdvancedModelRenderer maskLeft;
    public AdvancedModelRenderer maskRight;
    public AdvancedModelRenderer mane;
    public AdvancedModelRenderer earringLeft;
    public AdvancedModelRenderer earringRight;
    public AdvancedModelRenderer calfLeft;
    public AdvancedModelRenderer footLeft;
    public AdvancedModelRenderer calfRight;
    public AdvancedModelRenderer footRight;
    public AdvancedModelRenderer thighLeftJoint;
    public AdvancedModelRenderer thighRightJoint;
    public AdvancedModelRenderer earPointLeft;
    public AdvancedModelRenderer earPointRight;
    public AdvancedModelRenderer hair1;
    public AdvancedModelRenderer hair2;
    public AdvancedModelRenderer hair3;
    public AdvancedModelRenderer hair4;
    public AdvancedModelRenderer mouthLeft;
    public AdvancedModelRenderer mouthRight;
    public AdvancedModelRenderer scaler;
    public AdvancedModelRenderer flailer;
    public AdvancedModelRenderer talker;

    private MaskType maskType;

    public ModelBarakoa() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.footLeft = new AdvancedModelRenderer(this, 21, 53);
        this.footLeft.setPos(0.0F, 5.5F, 0.0F);
        this.footLeft.addBox(-2.5F, 0.5F, -7.0F, 5, 2, 9, 0.0F);
        setRotateAngle(footLeft, -0.5235987755982988F, 0.0F, 0.0F);
        this.thighLeft = new AdvancedModelRenderer(this, 41, 52);
        this.thighLeft.mirror = true;
        this.thighLeft.setPos(0, 0, 0);
        this.thighLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotateAngle(thighLeft, -0.7853981633974483F, -0.6981317007977318F, 0.0F);
        this.thighLeftJoint = new AdvancedModelRenderer(this, 41, 52);
        this.thighLeftJoint.mirror = true;
        this.thighLeftJoint.setPos(4.0F, 0.0F, 0.0F);
        this.thighLeftJoint.addBox(0, 0, 0, 0, 0, 0, 0.0F);
        setRotateAngle(thighLeftJoint, 0, 0, 0.0F);
        this.spearBase = new AdvancedModelRenderer(this, 0, 0);
        this.spearBase.setPos(0.0F, 3.0F, 0.0F);
        this.spearBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.loinClothBack = new AdvancedModelRenderer(this, 32, 27);
        this.loinClothBack.setPos(0.0F, 2.0F, 2.0F);
        this.loinClothBack.addBox(-4.0F, 0.0F, 0.0F, 8, 7, 0, 0.0F);
        setRotateAngle(loinClothBack, 0.17453292519943295F, 0.0F, 0.0F);
        this.loinClothFront = new AdvancedModelRenderer(this, 32, 27);
        this.loinClothFront.setPos(0.0F, 2.0F, -6.0F);
        this.loinClothFront.addBox(-4.0F, 0.0F, 0.0F, 8, 7, 0, 0.0F);
        setRotateAngle(loinClothFront, -0.17453292519943295F, 0.0F, 0.0F);
        this.thighRight = new AdvancedModelRenderer(this, 41, 52);
        this.thighRight.setPos(0, 0, 0);
        this.thighRight.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotateAngle(thighRight, -0.7853981633974483F, 0.6981317007977318F, 0.0F);
        this.thighRightJoint = new AdvancedModelRenderer(this, 41, 52);
        this.thighRightJoint.setPos(-4.0F, 0.0F, 0.0F);
        this.thighRightJoint.addBox(0, 0, 0, 0, 0, 0, 0.0F);
        setRotateAngle(thighRightJoint, 0, 0, 0);
        this.armLowerLeft = new AdvancedModelRenderer(this, 12, 55);
        this.armLowerLeft.mirror = true;
        this.armLowerLeft.setPos(0.0F, 8.0F, 0.0F);
        this.armLowerLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        setRotateAngle(armLowerLeft, -1.0471975511965976F, 0.0F, 0.0F);
        this.armLowerRight = new AdvancedModelRenderer(this, 12, 55);
        this.armLowerRight.setPos(0.0F, 8.0F, 0.0F);
        this.armLowerRight.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        setRotateAngle(armLowerRight, -1.0471975511965976F, 0.0F, 0.0F);
        this.earringRight = new AdvancedModelRenderer(this, 0, 29);
        this.earringRight.setPos(-3.0F, 4.0F, 0.5F);
        this.earringRight.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 0, 0.0F);
        this.neckJoint = new AdvancedModelRenderer(this, 0, 0);
        this.neckJoint.setPos(0.0F, -4.0F, 1.0F);
        this.neckJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(neckJoint, 0.3490658503988659F, 0.0F, 0.0F);
        this.armUpperLeft = new AdvancedModelRenderer(this, 41, 52);
        this.armUpperLeft.mirror = true;
        this.armUpperLeft.setPos(0.0F, 0.0F, 0.0F);
        this.armUpperLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotateAngle(armUpperLeft, 0.0F, 0.0F, -0.9599310885968813F);
        this.spear = new AdvancedModelRenderer(this, 66, 0);
        this.spear.setPos(0.0F, 0.0F, 0.0F);
        this.bone = new AdvancedModelRenderer(this, 66, 0);
        this.bone.setPos(0.0F, 0.0F, 0.0F);
        this.staff = new AdvancedModelRenderer(this, 66, 0);
        this.staff.setPos(0.0F, 0.0F, 0.0F);
        this.staffEnd = new AdvancedModelRenderer(this, 0, 0);
        this.staffEnd.setPos(0.0F, 0.0F, 0.0F);
        this.earLeft = new AdvancedModelRenderer(this, 48, 0);
        this.earLeft.setPos(4.0F, -4.0F, -3.0F);
        this.earLeft.addBox(0.0F, -2.0F, 0.0F, 4, 6, 1, 0.0F);
        setRotateAngle(earLeft, 0.0F, -0.3490658503988659F, 0.0F);
        this.maskLeft = new AdvancedModelRenderer(this, 48, 18);
        this.maskLeft.setPos(0.0F, 0.0F, -1.0F);
        this.maskLeft.addBox(-7.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        setRotateAngle(maskLeft, 0.0F, 0.4363323129985824F, 0.0F);
        this.calfLeft = new AdvancedModelRenderer(this, 12, 55);
        this.calfLeft.mirror = true;
        this.calfLeft.setPos(0.0F, 8.0F, 0.0F);
        this.calfLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        setRotateAngle(calfLeft, 1.3089969389957472F, 0.0F, 0.0F);
        this.handLeft = new AdvancedModelRenderer(this, 0, 55);
        this.handLeft.mirror = true;
        this.handLeft.setPos(0.0F, 7.0F, 0.0F);
        this.handLeft.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 4, 0.0F);
        setRotateAngle(handLeft, 0.0F, 0.0F, 0.7853981633974483F);
        this.calfRight = new AdvancedModelRenderer(this, 12, 55);
        this.calfRight.setPos(0.0F, 8.0F, 0.0F);
        this.calfRight.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        setRotateAngle(calfRight, 1.3089969389957472F, 0.0F, 0.0F);
        this.shield = new AdvancedModelRenderer(this, 66, 40);
        this.shield.setPos(0.0F, 0.0F, 0.0F);
        this.shield.addBox(1.0F, -6.0F, -6.0F, 2, 12, 12, 0.0F);
        setRotateAngle(shield, 0.7853981633974483F, 0.0F, 0.0F);
        this.handRight = new AdvancedModelRenderer(this, 0, 55);
        this.handRight.setPos(0.0F, 7.0F, 0.0F);
        this.handRight.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 4, 0.0F);
        setRotateAngle(handRight, 0.0F, 0.0F, -0.7853981633974483F);
        this.maskBase = new AdvancedModelRenderer(this, 0, 0);
        this.maskBase.setPos(0.0F, -3.0F, -8.0F);
        this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.neck = new AdvancedModelRenderer(this, 49, 52);
        this.neck.setPos(0.0F, 0.0F, 0.0F);
        this.neck.addBox(-1.5F, -9.0F, -1.5F, 3, 9, 3, 0.0F);
        setRotateAngle(neck, 0.3665191429188092F, 0.0F, 0.0F);
        this.modelCore = new AdvancedModelRenderer(this, 0, 0);
        this.modelCore.setPos(0.0F, 24.0F, 0.0F);
        this.modelCore.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.head = new AdvancedModelRenderer(this, 34, 34);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -7.0F, -7.0F, 8, 8, 8, 0.0F);
        this.footRight = new AdvancedModelRenderer(this, 21, 53);
        this.footRight.mirror = true;
        this.footRight.setPos(0.0F, 5.5F, 0.0F);
        this.footRight.addBox(-2.5F, 0.5F, -7.0F, 5, 2, 9, 0.0F);
        setRotateAngle(footRight, -0.5235987755982988F, 0.0F, 0.0F);
        this.armRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.armRightJoint.setPos(-3.5F, -4.5F, 0.0F);
        this.armRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(armRightJoint, 0.3490658503988659F, 0.3490658503988659F, 0.0F);
        this.earRight = new AdvancedModelRenderer(this, 48, 0);
        this.earRight.mirror = true;
        this.earRight.setPos(-4.0F, -4.0F, -3.0F);
        this.earRight.addBox(-4.0F, -2.0F, 0.0F, 4, 6, 1, 0.0F);
        setRotateAngle(earRight, 0.0F, 0.3490658503988659F, 0.0F);
        this.shieldBase = new AdvancedModelRenderer(this, 0, 0);
        this.shieldBase.setPos(0.0F, 7.0F, 0.0F);
        this.shieldBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.armLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.armLeftJoint.setPos(3.5F, -4.5F, 0.0F);
        this.armLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(armLeftJoint, 0.3490658503988659F, -0.3490658503988659F, 0.0F);
        this.earringLeft = new AdvancedModelRenderer(this, 0, 29);
        this.earringLeft.setPos(3.0F, 4.0F, 0.5F);
        this.earringLeft.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 0, 0.0F);
        this.armUpperRight = new AdvancedModelRenderer(this, 41, 52);
        this.armUpperRight.setPos(0.0F, 0.0F, 0.0F);
        this.armUpperRight.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotateAngle(armUpperRight, 0.0F, 0.0F, 0.9599310885968813F);
        this.headJoint = new AdvancedModelRenderer(this, 0, 0);
        this.headJoint.setPos(0.0F, -7.0F, 1.0F);
        this.headJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(headJoint, -0.3665191429188092F, 0.0F, 0.0F);
        this.chest = new AdvancedModelRenderer(this, 0, 42);
        this.chest.setPos(0.0F, -4.0F, -3.0F);
        this.chest.addBox(-4.0F, -5.0F, -3.5F, 8, 5, 7, 0.0F);
        setRotateAngle(chest, -0.3490658503988659F, 0.0F, 0.0F);
        this.body = new AdvancedModelRenderer(this, 0, 24);
        this.body.setPos(0.0F, -13.0F, 2.0F);
        this.body.addBox(-5.5F, -6.0F, -8.0F, 11, 8, 10, 0.0F);
        this.maskRight = new AdvancedModelRenderer(this, 48, 18);
        this.maskRight.mirror = true;
        this.maskRight.setPos(0.0F, 0.0F, -1.0F);
        this.maskRight.addBox(0.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        setRotateAngle(maskRight, 0.0F, -0.4363323129985824F, 0.0F);
        this.mane = new AdvancedModelRenderer(this, 0, 0);
        this.mane.setPos(0.0F, -2.0F, 4.0F);
        this.mane.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
        setRotateAngle(mane, 0, (float) Math.PI, 0);
        this.scaler = new AdvancedModelRenderer(this, 0, 0);
        this.scaler.setPos(0.0F, 0, 0F);
        this.flailer = new AdvancedModelRenderer(this, 0, 0);
        this.flailer.setPos(0.0F, 0, 0F);
        this.blowgun = new AdvancedModelRenderer(this, 82, 0);
        this.blowgun.setPos(0.0F, 0.0F, 0.0F);
//        this.blowgun.add3DTexture(-4, -4, 0.5F, 15, 15);
        this.talker = new AdvancedModelRenderer(this, 0, 0);
        this.talker.setPos(0, 0, 0);

        earPointLeft = new AdvancedModelRenderer(this, 48, 7);
        earPointLeft.setPos(3.0F, 0.0F, 0.9995F);
        earPointLeft.setScale(1f, 1f, 0.999f);
        earLeft.addChild(earPointLeft);
        setRotateAngle(earPointLeft, 0.0F, 0.0F, -0.1745F);
        earPointLeft.addBox(0.5F, -1.9F, -1.0F, 3, 2, 1, 0.0F, false);

        earPointRight = new AdvancedModelRenderer(this, 48, 7);
        earPointRight.setPos(-3.0F, 0.0F, 0.9995F);
        earPointRight.setScale(1f, 1f, 0.999f);
        earRight.addChild(earPointRight);
        setRotateAngle(earPointRight, 0.0F, 0.0F, 0.1745F);
        earPointRight.addBox(-3.5F, -1.9F, -1.0F, 3, 2, 1, 0.0F, false);

        hair1 = new AdvancedModelRenderer(this, 87, 50);
        hair1.setPos(0.0F, -7.0F, -4.0F);
        head.addChild(hair1);
        setRotateAngle(hair1, 0.3491F, 0.0F, 0.0F);
        hair1.addBox(-7.0F, 0.0F, 0.0F, 14, 0, 7, 0.0F, false);

        hair2 = new AdvancedModelRenderer(this, 87, 57);
        hair2.setPos(0.0F, -7.0F, -1.5F);
        head.addChild(hair2);
        setRotateAngle(hair2, 0.0436F, 0.0F, 0.0F);
        hair2.addBox(-7.0F, 0.0F, 0.0F, 14, 0, 7, 0.0F, false);

        hair3 = new AdvancedModelRenderer(this, 87, 50);
        hair3.setPos(0.0F, -7.0F, 1.0F);
        head.addChild(hair3);
        setRotateAngle(hair3, -0.7418F, 0.0F, 0.0F);
        hair3.addBox(-7.0F, 0.0F, 0.0F, 14, 0, 7, 0.0F, false);

        hair4 = new AdvancedModelRenderer(this, 87, 50);
        hair4.setPos(0.0F, -6.0F, 1.0F);
        head.addChild(hair4);
        setRotateAngle(hair4, -1.2217F, 0.0F, 0.0F);
        hair4.addBox(-7.0F, 0.0F, 0.0F, 14, 0, 7, 0.0F, false);

        mouthLeft = new AdvancedModelRenderer(this);
        mouthLeft.setPos(0.0F, -1.0F, 0.0F);
        maskLeft.addChild(mouthLeft);
        mouthLeft.texOffs(58, 34).addBox(-7.0F, 0.0F, -0.0005F, 7, 7, 0, false);

        mouthRight = new AdvancedModelRenderer(this);
        mouthRight.setPos(0.0F, -1.0F, 0.0F);
        maskRight.addChild(mouthRight);
        mouthRight.texOffs(58, 34).addBox(0.0F, 0.0F, -0.0005F, 7, 7, 0, true);

        this.calfLeft.addChild(this.footLeft);
        this.body.addChild(this.thighLeftJoint);
        this.handRight.addChild(this.spearBase);
        this.body.addChild(this.loinClothBack);
        this.body.addChild(this.loinClothFront);
        this.body.addChild(this.thighRightJoint);
        thighRightJoint.addChild(thighRight);
        thighLeftJoint.addChild(thighLeft);
        this.armUpperLeft.addChild(this.armLowerLeft);
        this.armUpperRight.addChild(this.armLowerRight);
        this.earRight.addChild(this.earringRight);
        this.chest.addChild(this.neckJoint);
        this.armLeftJoint.addChild(this.armUpperLeft);
        this.spearBase.addChild(this.spear);
        this.spearBase.addChild(this.blowgun);
        this.head.addChild(this.earLeft);
        this.maskBase.addChild(this.maskLeft);
        this.thighLeft.addChild(this.calfLeft);
        this.armLowerLeft.addChild(this.handLeft);
        this.thighRight.addChild(this.calfRight);
        this.shieldBase.addChild(this.shield);
        this.armLowerRight.addChild(this.handRight);
        this.head.addChild(this.maskBase);
        this.neckJoint.addChild(this.neck);
        this.headJoint.addChild(this.head);
        this.calfRight.addChild(this.footRight);
        this.chest.addChild(this.armRightJoint);
        this.head.addChild(this.earRight);
        this.armLowerLeft.addChild(this.shieldBase);
        this.chest.addChild(this.armLeftJoint);
        this.earLeft.addChild(this.earringLeft);
        this.armRightJoint.addChild(this.armUpperRight);
        this.neck.addChild(this.headJoint);
        this.body.addChild(this.chest);
        this.modelCore.addChild(this.body);
        this.maskBase.addChild(this.maskRight);
        this.maskBase.addChild(this.mane);
        this.spearBase.addChild(bone);
        this.spearBase.addChild(staff);
        this.spearBase.addChild(staffEnd);
        updateDefaultPose();

        modelCore.scaleChildren = true;
        body.scaleChildren = true;
        chest.scaleChildren = true;
        thighLeft.scaleChildren = true;
        thighRight.scaleChildren = true;
        loinClothFront.scaleChildren = true;
        loinClothBack.scaleChildren = true;
        armRightJoint.scaleChildren = true;
        armLeftJoint.scaleChildren = true;
        neckJoint.scaleChildren = true;
        armUpperRight.scaleChildren = true;
        armLowerRight.scaleChildren = true;
        handRight.scaleChildren = true;
        spearBase.scaleChildren = true;
        spear.scaleChildren = true;
        bone.scaleChildren = true;
        staff.scaleChildren = true;
        blowgun.scaleChildren = true;
        armUpperLeft.scaleChildren = true;
        armLowerLeft.scaleChildren = true;
        handLeft.scaleChildren = true;
        shieldBase.scaleChildren = true;
        shield.scaleChildren = true;
        neck.scaleChildren = true;
        headJoint.scaleChildren = true;
        head.scaleChildren = true;
        maskBase.scaleChildren = true;
        earLeft.scaleChildren = true;
        earRight.scaleChildren = true;
        maskLeft.scaleChildren = true;
        maskRight.scaleChildren = true;
        mane.scaleChildren = true;
        earringLeft.scaleChildren = true;
        earringRight.scaleChildren = true;
        calfLeft.scaleChildren = true;
        footLeft.scaleChildren = true;
        calfRight.scaleChildren = true;
        footRight.scaleChildren = true;
        thighLeftJoint.scaleChildren = true;
        thighRightJoint.scaleChildren = true;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.pushPose();
        if (maskType == MaskType.FURY || maskType == MaskType.FAITH) {
            modelCore.setScale(0.85f);
        } else {
            modelCore.setScale(0.75f);
        }
        this.modelCore.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.popPose();
    }

    public void setDefaultAngles(EntityBarakoa entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        maskType = entity.getMask();
        headYaw = Mth.wrapDegrees(headYaw);
        headPitch = Mth.wrapDegrees(headPitch);
        resetToDefaultPose();
//                f = entity.ticksExisted;
//                f1 = 0.5f;

        bone.setScale(1.25f);
        blowgun.setScale(1f);
        spear.setScale(1.25f);
        staff.setScale(1f);

        bone.zRot -= Math.PI;
        bone.xRot += Math.PI / 2f + 0.1;
        bone.y -= 1.5;
        bone.z -= 10.5;

        staff.zRot -= Math.PI;
        staff.xRot -= Math.PI / 2f;
        staff.y += 2;
        staff.z -= 1;

        staffEnd.z -= 12;

        spear.zRot -= Math.PI;
        spear.xRot -= Math.PI / 2f;
        spear.yRot -= 0.2f;
        spear.y += 1.5;
        spear.z -= 2;

        blowgun.xRot += Math.PI / 2f;
        blowgun.y += 1;

        if (entity.getWeapon() == 0) {
            if (entity.getMask() == MaskType.FURY) {
                spear.visible = true;
                bone.visible = false;
                staff.visible = false;
            }
            else {
                bone.visible = true;
                spear.visible = false;
                staff.visible = false;
            }
            blowgun.visible = false;
        }
        else if (entity.getWeapon() == 1) {
            spear.visible = false;
            bone.visible = false;
            blowgun.visible = true;
            staff.visible = false;
        }
        else {
            spear.visible = false;
            bone.visible = false;
            blowgun.visible = false;
            staff.visible = true;
        }

        if (!entity.active) {
            return;
        }
        float doWalk = entity.doWalk.getAnimationProgressSinSqrt(delta);
        float dance = entity.dancing.getAnimationProgressSinSqrt(delta);
        if (limbSwingAmount > 0.55f) {
            limbSwingAmount = 0.55f;
        }
        float globalSpeed = 1.5f;
        float globalHeight = 1 * doWalk;
        float globalDegree = 1 * doWalk * (1 - dance);
        if (entity.getAnimation() != EntityBarakoa.PROJECTILE_ATTACK_ANIMATION) {
            faceTarget(headYaw, headPitch, 2.0F, neck);
            faceTarget(headYaw, headPitch, 2.0F, head);
        }
        float frame = entity.frame + delta;
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
        boolean frozen = frozenCapability != null && frozenCapability.getFrozen();

        if (entity.getMask() == MaskType.FURY) {
            armLeftJoint.xRot -= 0.2;
            armLeftJoint.yRot += 1.3;
            armLowerLeft.xRot -= 0.2;
            armLowerLeft.yRot += 0.2;
            armLowerLeft.zRot += 1;

            if (!frozen) {
                flap(armUpperLeft, 1 * globalSpeed, 0.1f * globalHeight, false, 0.5f, 0, limbSwing, limbSwingAmount);
                walk(armUpperLeft, 0.5f * globalSpeed, 0.3f * globalDegree, true, 0, -1, limbSwing, limbSwingAmount);
            }
        } else {
            if (!frozen) {
                flap(armUpperLeft, 1 * globalSpeed, 0.3f * globalHeight, false, 0.5f, 0, limbSwing, limbSwingAmount);
                walk(armUpperLeft, 0.5f * globalSpeed, 0.7f * globalDegree, true, 0, 0, limbSwing, limbSwingAmount);
            }
            spearBase.z += 1.5;
        }

        if (!frozen) {
            bob(body, 1 * globalSpeed, 2.5f * globalHeight, false, limbSwing, limbSwingAmount);
            walk(loinClothFront, 1 * globalSpeed, 0.5f * globalHeight, false, 2, 0, limbSwing, limbSwingAmount);
            walk(loinClothBack, 1 * globalSpeed, 0.5f * globalHeight, true, 2, 0, limbSwing, limbSwingAmount);
            walk(body, 1 * globalSpeed, 0.2f * globalHeight, false, 1, 0.2f * globalHeight, limbSwing, limbSwingAmount);
            walk(thighLeft, 1 * globalSpeed, 0.2f * globalHeight, true, 1, 0.4f * globalHeight, limbSwing, limbSwingAmount);
            walk(thighRight, 1 * globalSpeed, 0.2f * globalHeight, true, 1, 0.4f * globalHeight, limbSwing, limbSwingAmount);
            swing(body, 0.5f * globalSpeed, 0.7f * globalDegree, true, 0, 0, limbSwing, limbSwingAmount);
            swing(thighLeft, 0.5f * globalSpeed, 0.7f * globalDegree, false, 0, 0, limbSwing, limbSwingAmount);
            swing(thighRight, 0.5f * globalSpeed, 0.7f * globalDegree, false, 0, 0, limbSwing, limbSwingAmount);
            swing(chest, 0.5f * globalSpeed, 1.4f * globalDegree, false, 0, 0, limbSwing, limbSwingAmount);
            swing(neck, 0.5f * globalSpeed, 0.7f * globalDegree, true, 0, 0, limbSwing, limbSwingAmount);
            flap(modelCore, 0.5f * globalSpeed, 0.3f * globalHeight, false, 0, 0, limbSwing, limbSwingAmount);
            flap(neck, 0.5f * globalSpeed, 0.15f * globalHeight, true, 0, 0, limbSwing, limbSwingAmount);
            flap(head, 0.5f * globalSpeed, 0.15f * globalHeight, true, 0, 0, limbSwing, limbSwingAmount);
            walk(thighLeft, 0.5f * globalSpeed, 1.4f * globalDegree, false, 0, 1f * globalHeight, limbSwing, limbSwingAmount);
            walk(thighRight, 0.5f * globalSpeed, 1.4f * globalDegree, true, 0, -1f * globalHeight, limbSwing, limbSwingAmount);
            walk(calfLeft, 0.5f * globalSpeed, 1.2f * globalDegree, false, -1.5f, 0.3f * globalDegree, limbSwing, limbSwingAmount);
            walk(calfRight, 0.5f * globalSpeed, 1.2f * globalDegree, true, -1.5f, -0.3f * globalDegree, limbSwing, limbSwingAmount);
            walk(footLeft, 0.5f * globalSpeed, 1.2f * globalDegree, false, -3f, 0.15f * globalDegree, limbSwing, limbSwingAmount);
            walk(footRight, 0.5f * globalSpeed, 1.2f * globalDegree, true, -3f, -0.15f * globalDegree, limbSwing, limbSwingAmount);
            thighLeft.yRot += 1f * limbSwingAmount * globalDegree;
            thighRight.yRot -= 1f * limbSwingAmount * globalDegree;
            walk(neck, 1 * globalSpeed, 0.2f * globalHeight, true, 0.5f, 0.5f * globalDegree, limbSwing, limbSwingAmount);
            walk(head, 1 * globalSpeed, 0f * globalHeight, true, 0.5f, -0.5f * globalDegree, limbSwing, limbSwingAmount);
            flap(armUpperRight, 1 * globalSpeed, 0.3f * globalHeight, true, 0.5f, 0, limbSwing, limbSwingAmount);
            walk(armUpperRight, 0.5f * globalSpeed, 0.7f * globalDegree, false, 0, 0, limbSwing, limbSwingAmount);
            walk(armLowerRight, 0.5f * globalSpeed, 1 * globalDegree, false, -1, 0, limbSwing, limbSwingAmount);
            walk(armLowerLeft, 0.5f * globalSpeed, 1 * globalDegree, true, -1, 0, limbSwing, limbSwingAmount);
            walk(handRight, 0.5f * globalSpeed, 1 * globalDegree, false, -2, 0.8f * globalDegree, limbSwing, limbSwingAmount);
            swing(handRight, 0.5f * globalSpeed, 1f * globalDegree, true, 0, 0, limbSwing, limbSwingAmount);
            walk(handLeft, 0.5f * globalSpeed, 1 * globalDegree, true, -2, 0.4f * globalDegree, limbSwing, limbSwingAmount);
        }

        if (entity.getAnimation() != EntityBarakoa.DIE_ANIMATION && !frozen) {
            walk(body, 0.2f, 0.05f, false, 0, 0, frame, 1f);
            walk(thighLeftJoint, 0.2f, 0.05f, true, 0, 0, frame, 1f);
            walk(thighRightJoint, 0.2f, 0.05f, true, 0, 0, frame, 1f);
            walk(neck, 0.2f, 0.02f, true, 1f, 0, frame, 1f);
            walk(head, 0.2f, 0.02f, true, 1f, 0, frame, 1f);
            flap(armUpperLeft, 0.2f, -0.1f, true, -1, 0, frame, 1F);
            flap(armUpperRight, 0.2f, 0.1f, true, -1, 0, frame, 1F);
        }

        //Dancing
        float danceSpeed = 1.4f;
        thighLeft.yRot -= 0.6f * dance;
        thighRight.yRot += 0.6f * dance;
        bob(modelCore, 0.3f * danceSpeed, 10f * dance, true, frame, 1f);
        flap(modelCore, 0.3f * danceSpeed, 0.5f * dance, false, 0, 0, frame, 1f);
        walk(thighLeft, 0.3f * danceSpeed, 0.6f * dance, false, 0, -0.6f * dance, frame, 1f);
        walk(calfLeft, 0.3f * danceSpeed, 0.5f * dance, true, 0, -0.6f * dance, frame, 1f);
        walk(footLeft, 0.3f * danceSpeed, 0.2f * dance, true, 0, -0.5f * dance, frame, 1f);
        walk(thighRight, 0.3f * danceSpeed, 0.6f * dance, true, 0, 0.6f * dance, frame, 1f);
        walk(calfRight, 0.3f * danceSpeed, 0.5f * dance, false, 0, 0.6f * dance, frame, 1f);
        walk(footRight, 0.3f * danceSpeed, 0.2f * dance, false, 0, 0.5f * dance, frame, 1f);
        armRightJoint.xRot -= 1.7 * dance;
        handRight.xRot += 1 * dance;
        walk(armUpperRight, 1.2f * danceSpeed, 0.5f * dance, false, 0, -0.3f * dance, frame, 1f);
        walk(armLowerRight, 1.2f * danceSpeed, 0.5f * dance, true, 0, 0, frame, 1f);
        armLeftJoint.xRot -= 1.7 * dance;
        walk(armUpperLeft, 1.2f * danceSpeed, 0.5f * dance, false, 0, -0.3f * dance, frame, 1f);
        walk(armLowerLeft, 1.2f * danceSpeed, 0.5f * dance, true, 0, 0, frame, 1f);
        flap(neck, 0.3f * danceSpeed, 0.2f * dance, true, 0, 0, frame, 1f);
        flap(head, 1.2f * danceSpeed, 0.4f * dance, true, 0, 0, frame, 1f);
        walk(loinClothFront, 0.6f * danceSpeed, 0.6f * dance, true, 1, 0.4f * dance, frame, 1f);
        walk(loinClothBack, 0.6f * danceSpeed, 0.6f * dance, false, 1, 0.4f * dance, frame, 1f);
        if (entity.getMask() == MaskType.FURY) {
            armLeftJoint.xRot += 0.2 * dance;
            armLeftJoint.yRot -= 1.3 * dance;
            armLowerLeft.xRot += 0.2 * dance;
            armLowerLeft.yRot -= 0.2 * dance;
            armLowerLeft.zRot -= 1 * dance;
        }
    }

    @Override
    protected void animate(EntityBarakoa entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngles(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        float frame = entity.frame + delta;

        if (entity.getMask() == MaskType.FURY) {
            animator.setAnimation(EntityBarakoa.ATTACK_ANIMATION);
            animator.setStaticKeyframe(3);
            animator.startKeyframe(4);
            animator.rotate(body, -0.3f, 1f, 0);
            animator.move(modelCore, -4, 0, -2);
            animator.rotate(chest, 0, 0.2f, 0);
            animator.rotate(neck, 0.1f, -0.6f, 0);
            animator.rotate(head, 0.1f, -0.6f, 0);
            animator.rotate(armUpperLeft, 0, 0, -0.5f);
            animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
            animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
            animator.rotate(armUpperRight, 0, 0.5f, 0);
            animator.rotate(armLowerRight, -1, -0.5f, 0);
            animator.rotate(handRight, 1f, 0, 0);
            animator.rotate(thighLeft, -0.9f, 0.3f, 0);
            animator.rotate(calfLeft, 0.1f, 0, 0);
            animator.rotate(footLeft, 0, 0, 0);
            animator.rotate(thighRight, 0.3f, -0.9f, 0);
            animator.rotate(calfRight, 0, 0, 0);
            animator.rotate(footRight, 0, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(1);
            animator.startKeyframe(3);
            animator.rotate(body, 0.5f, -0.3f, 0);
            animator.move(modelCore, -1.5f, 1.2f, -8.5f);
            animator.rotate(chest, 0, -0.5f, 0);
            animator.rotate(neck, -0.1f, 0.3f, 0);
            animator.rotate(head, -0.2f, 0.3f, 0);
            animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
            animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
            animator.rotate(armUpperRight, -1.7f, 0, -0.5f);
            animator.rotate(armLowerRight, 0.7f, 0, 0);
            animator.rotate(handRight, 1.4f, 0.8f, 0.7f);
            animator.rotate(thighLeft, -0.8f, 0.7f, 0);
            animator.rotate(calfLeft, -0.6f, 0, 0);
            animator.rotate(footLeft, 0.9f, 0, 0);
            animator.move(thighRight, 0, 1, 0);
            animator.rotate(thighRight, 0.7f, 0, 0f);
            animator.rotate(calfRight, -0.5f, 0, 0);
            animator.rotate(footRight, -0.5f, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(1);
            animator.resetKeyframe(6);

            animator.setAnimation(EntityBarakoana.BLOCK_ANIMATION);
            animator.startKeyframe(3);
            animator.move(body, 0, 5f, 1f);
            animator.rotate(body, 0.3f, 0, 0);
            animator.rotate(chest, 0, 0.8f, 0);
            animator.rotate(thighLeftJoint, -0.3f, 0, 0);
            animator.rotate(thighRightJoint, -0.3f, 0, 0);
            animator.rotate(thighLeft, -0.5f, 0, 0);
            animator.rotate(thighRight, -0.5f, 0, 0);
            animator.rotate(calfLeft, 0.8f, 0, 0);
            animator.rotate(calfRight, 0.8f, 0, 0);
            animator.rotate(footLeft, -0.3f, 0, 0);
            animator.rotate(footRight, -0.3f, 0, 0);
            animator.rotate(neck, 0.3f, -0.8f, 0);
            animator.rotate(head, -0.6f, 0, 0);
            animator.rotate(armLeftJoint, 0, -0.8f, 0);
            animator.rotate(armLeftJoint, -0.5f, 0, -1);
            animator.rotate(armLowerLeft, 0, 0, 0.7f);
            animator.endKeyframe();
            animator.resetKeyframe(7);

            animator.setAnimation(EntityBarakoa.HURT_ANIMATION);
            animator.startKeyframe(3);
            animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
            animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
            animator.move(body, 0, 5f, 1f);
            animator.rotate(body, 0.3f, 0, 0);
            animator.rotate(thighLeftJoint, -0.3f, 0, 0);
            animator.rotate(thighRightJoint, -0.3f, 0, 0);
            animator.rotate(thighLeft, -0.5f, 0, 0);
            animator.rotate(thighRight, -0.5f, 0, 0);
            animator.rotate(calfLeft, 0.8f, 0, 0);
            animator.rotate(calfRight, 0.8f, 0, 0);
            animator.rotate(footLeft, -0.3f, 0, 0);
            animator.rotate(footRight, -0.3f, 0, 0);
            animator.rotate(neck, 0.5f, 0, 0);
            animator.rotate(head, -0.3f, 0, 0);
            animator.rotate(armUpperLeft, -0.3f, 0, -1);
            animator.rotate(armUpperRight, -0.3f, 0, 1);
            animator.rotate(armLowerLeft, -0.7f, 0, 0);
            animator.rotate(armLowerRight, -0.7f, 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(7);

            animator.setAnimation(EntityBarakoa.DIE_ANIMATION);
            animator.startKeyframe(3);
            animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
            animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
            animator.move(body, 0, 5f, 1f);
            animator.rotate(body, 0.3f, 0, 0);
            animator.rotate(thighLeftJoint, -0.3f, 0, 0);
            animator.rotate(thighRightJoint, -0.3f, 0, 0);
            animator.rotate(thighLeft, -0.5f, 0, 0);
            animator.rotate(thighRight, -0.5f, 0, 0);
            animator.rotate(calfLeft, 0.8f, 0, 0);
            animator.rotate(calfRight, 0.8f, 0, 0);
            animator.rotate(footLeft, -0.3f, 0, 0);
            animator.rotate(footRight, -0.3f, 0, 0);
            animator.rotate(neck, 0.5f, 0, 0);
            animator.rotate(head, -0.3f, 0, 0);
            animator.rotate(armUpperLeft, -0.3f, 0, -1);
            animator.rotate(armUpperRight, -0.3f, 0, 1);
            animator.rotate(armLowerLeft, -0.7f, 0, 0);
            animator.rotate(armLowerRight, -0.7f, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.rotate(head, -0.8f, 0, 0);
            animator.move(flailer, 1, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(10);
            animator.startKeyframe(10);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -0.8f, 0, 0);
            animator.move(body, 0, -22f, -5f);
            animator.endKeyframe();
            animator.startKeyframe(4);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -0.8f, 0, 0);
            animator.move(body, 0, 7, 0);
            animator.endKeyframe();
            animator.startKeyframe(2);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -1.6f, 0, 0);
            animator.move(body, 0, -3, 5);
            animator.endKeyframe();
            animator.startKeyframe(2);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -1.58f, 0, 0);
            animator.move(body, 0, 9f, 5);
            animator.endKeyframe();
            animator.setStaticKeyframe(20);

            armLeftJoint.xRot += 0.2 * flailer.x;
            armLeftJoint.yRot -= 1.3 * flailer.x;
            armLowerLeft.xRot += 0.2 * flailer.x;
            armLowerLeft.yRot -= 0.2 * flailer.x;
            armLowerLeft.zRot -= 1 * flailer.x;
        } else {
            animator.setAnimation(EntityBarakoa.ATTACK_ANIMATION);
            animator.setStaticKeyframe(3);
            animator.startKeyframe(4);
            animator.rotate(body, -0.3f, 1f, 0);
            animator.move(modelCore, -4, 0, -2);
            animator.rotate(chest, 0, 0.2f, 0);
            animator.rotate(neck, 0.1f, -0.6f, 0);
            animator.rotate(head, 0.1f, -0.6f, 0);
            animator.rotate(armUpperLeft, 0, 0, -0.5f);
            animator.rotate(armUpperRight, -2, 0, -1.5f);
            animator.rotate(armLowerRight, -0.5f, 0, 0);
            animator.rotate(handRight, 0, 1f, 0);
            animator.rotate(thighLeft, -0.9f, 0.3f, 0);
            animator.rotate(calfLeft, 0.1f, 0, 0);
            animator.rotate(footLeft, 0, 0, 0);
            animator.rotate(thighRight, 0.3f, -0.9f, 0);
            animator.rotate(calfRight, 0, 0, 0);
            animator.rotate(footRight, 0, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(1);
            animator.startKeyframe(3);
            animator.rotate(body, 0.5f, -0.3f, 0);
            animator.move(modelCore, -1.5f, 1.2f, -8.5f);
            animator.rotate(chest, 0, -0.5f, 0);
            animator.rotate(neck, -0.1f, 0.3f, 0);
            animator.rotate(head, -0.2f, 0.3f, 0);
            animator.rotate(armUpperRight, -0.8f, 0, -0.5f);
            animator.rotate(armLowerRight, 0.7f, 0, 0);
            animator.rotate(handRight, 0.6f, 0.6f, 0);
            animator.rotate(thighLeft, -0.8f, 0.7f, 0);
            animator.rotate(calfLeft, -0.6f, 0, 0);
            animator.rotate(footLeft, 0.9f, 0, 0);
            animator.move(thighRight, 0, 1, 0);
            animator.rotate(thighRight, 0.7f, 0, 0f);
            animator.rotate(calfRight, -0.5f, 0, 0);
            animator.rotate(footRight, -0.5f, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(1);
            animator.resetKeyframe(6);

            animator.setAnimation(EntityBarakoa.PROJECTILE_ATTACK_ANIMATION);
            animator.startKeyframe(5);
            animator.rotate(body, -0.3f, 0, 0);
            animator.rotate(thighRightJoint, 0.3f, 0, 0);
            animator.rotate(thighLeftJoint, 0.3f, 0, 0);
            animator.rotate(loinClothFront, 0.3f, 0, 0);
            animator.rotate(loinClothBack, 0.3f, 0, 0);
            animator.rotate(neck, -0.4f, 0, 0);
            animator.rotate(head, 0.5f, 0, 0);
            animator.rotate(armUpperRight, -1.5f, 0, 0);
            animator.rotate(armLowerRight, 0, 0, -1f);
            animator.rotate(handRight, -1f, -0.2f, 1.2f);
//            animator.move(blowgun, 0, 0, 4.5f);
//            animator.rotate(blowgun, 0, 0, MathUtils.PI);
            animator.endKeyframe();
            animator.setStaticKeyframe(3);
            animator.startKeyframe(3);
            animator.rotate(body, 0.5f, 0, 0);
            animator.rotate(thighRightJoint, -0.5f, 0, 0);
            animator.rotate(thighLeftJoint, -0.5f, 0, 0);
            animator.rotate(loinClothBack, -0.5f, 0, 0);
            animator.rotate(loinClothFront, -0.5f, 0, 0);
            animator.rotate(neck, 0.2f, 0, 0);
            animator.rotate(head, -0.7f, 0, 0);
            animator.rotate(armUpperRight, -1.8f, 0, 0);
            animator.rotate(armRightJoint, -0.5f, 0, 0);
            animator.move(armRightJoint, 1, 0, -2);
            animator.rotate(armLowerRight, 0.8f, 0, -0.4f);
            animator.rotate(handRight, -1.5f, 0.4f, 1.0f);
//            animator.move(blowgun, 0, 0, 5f);
//            animator.rotate(blowgun, 0, 0, MathUtils.PI);
            animator.endKeyframe();
            animator.setStaticKeyframe(2);
            animator.resetKeyframe(7);

            animator.setAnimation(EntityBarakoa.HURT_ANIMATION);
            animator.startKeyframe(3);
            animator.move(body, 0, 5f, 1f);
            animator.rotate(body, 0.3f, 0, 0);
            animator.rotate(thighLeftJoint, -0.3f, 0, 0);
            animator.rotate(thighRightJoint, -0.3f, 0, 0);
            animator.rotate(thighLeft, -0.5f, 0, 0);
            animator.rotate(thighRight, -0.5f, 0, 0);
            animator.rotate(calfLeft, 0.8f, 0, 0);
            animator.rotate(calfRight, 0.8f, 0, 0);
            animator.rotate(footLeft, -0.3f, 0, 0);
            animator.rotate(footRight, -0.3f, 0, 0);
            animator.rotate(neck, 0.5f, 0, 0);
            animator.rotate(head, -0.3f, 0, 0);
            animator.rotate(armUpperLeft, -0.3f, 0, -1);
            animator.rotate(armUpperRight, -0.3f, 0, 1);
            animator.rotate(armLowerLeft, -0.7f, 0, 0);
            animator.rotate(armLowerRight, -0.7f, 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(7);

            animator.setAnimation(EntityBarakoa.DIE_ANIMATION);
            animator.startKeyframe(3);
            animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
            animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
            animator.move(body, 0, 5f, 1f);
            animator.rotate(body, 0.3f, 0, 0);
            animator.rotate(thighLeftJoint, -0.3f, 0, 0);
            animator.rotate(thighRightJoint, -0.3f, 0, 0);
            animator.rotate(thighLeft, -0.5f, 0, 0);
            animator.rotate(thighRight, -0.5f, 0, 0);
            animator.rotate(calfLeft, 0.8f, 0, 0);
            animator.rotate(calfRight, 0.8f, 0, 0);
            animator.rotate(footLeft, -0.3f, 0, 0);
            animator.rotate(footRight, -0.3f, 0, 0);
            animator.rotate(neck, 0.5f, 0, 0);
            animator.rotate(head, -0.3f, 0, 0);
            animator.rotate(armUpperLeft, -0.3f, 0, -1);
            animator.rotate(armUpperRight, -0.3f, 0, 1);
            animator.rotate(armLowerLeft, -0.7f, 0, 0);
            animator.rotate(armLowerRight, -0.7f, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.rotate(head, -0.8f, 0, 0);
            animator.move(flailer, 1, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(10);
            animator.startKeyframe(10);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -0.8f, 0, 0);
            animator.move(body, 0, -22f, -5f);
            animator.endKeyframe();
            animator.startKeyframe(4);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -0.8f, 0, 0);
            animator.move(body, 0, 7, 0);
            animator.endKeyframe();
            animator.startKeyframe(2);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -1.6f, 0, 0);
            animator.move(body, 0, -3, 5);
            animator.endKeyframe();
            animator.startKeyframe(2);
            animator.move(scaler, 0.999f, 0, 0);
            animator.rotate(head, -1.58f, 0, 0);
            animator.move(body, 0, 9f, 5);
            animator.endKeyframe();
            animator.setStaticKeyframe(20);
        }

        animator.setAnimation(EntityBarakoa.IDLE_ANIMATION);
        animator.startKeyframe(10);
        animator.move(talker, 1, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(15);
        animator.resetKeyframe(10);

        animator.setAnimation(EntityBarakoa.ACTIVATE_ANIMATION);
        animator.setStaticKeyframe(3);
        animator.startKeyframe(10);
        animator.move(scaler, -0.999f, 0, 0);
        animator.move(flailer, 1, 0, 0);
        animator.move(body, 0, -9, -5);
        animator.endKeyframe();
        animator.setStaticKeyframe(2);
        animator.startKeyframe(5);
        animator.move(scaler, -0.999f, 0, 0);
        animator.move(body, 0, -9, -5);
        animator.rotate(head, 1.58f, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(5);

        animator.setAnimation(EntityBarakoa.DEACTIVATE_ANIMATION);
        animator.startKeyframe(5);
        animator.rotate(head, -0.8f, 0, 0);
        animator.move(flailer, 1, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(10);
        animator.startKeyframe(10);
        animator.move(scaler, 0.999f, 0, 0);
        animator.rotate(head, -0.8f, 0, 0);
        animator.move(body, 0, -22f, -5f);
        animator.endKeyframe();
        animator.setStaticKeyframe(1);

        animator.setAnimation(EntityBarakoa.HEAL_LOOP_ANIMATION);
        animator.startKeyframe(0);
        animator.move(talker, 0, 1, 0);
        animator.rotate(body, 0.3f, 0, 0);
        animator.move(modelCore, 0, 0, -3.5f);
        animator.rotate(neck, -0.1f, 0, 0);
        animator.rotate(head, -0.2f, 0, 0);
        animator.rotate(thighLeftJoint, -0.3f, 0, 0f);
        animator.rotate(thighRight, 0.4f, 0, 0.1f);
        animator.rotate(footRight, -0.5f, -0.1f, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(21);

        animator.setAnimation(EntityBarakoa.HEAL_START_ANIMATION);
        animator.startKeyframe(5);
        animator.move(talker, 0, 0, 1);
        animator.rotate(body, -0.3f, 1f, 0);
        animator.move(modelCore, -2, 0, -2);
        animator.rotate(chest, 0, 0.2f, 0);
        animator.rotate(neck, 0.1f, -0.6f, 0);
        animator.rotate(head, 0.1f, -0.6f, 0);
        animator.rotate(armUpperLeft, 0, 0, -0.5f);
        animator.rotate(armUpperRight, -2, 0, -1.2f);
        animator.rotate(armLowerRight, -0.4f, 0, 0);
        animator.rotate(handRight, 0, 0.8f, 0);
        animator.rotate(thighLeft, -0.9f, 0.3f, 0);
        animator.rotate(calfLeft, 0.1f, 0, 0);
        animator.rotate(footLeft, 0, 0, 0);
        animator.rotate(thighRight, 0.3f, -0.9f, 0);
        animator.rotate(calfRight, 0, 0, 0);
        animator.rotate(footRight, 0, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(15);
        animator.startKeyframe(4);
        animator.move(talker, 0, 1, 0);
        animator.rotate(body, 0.3f, 0, 0);
        animator.move(modelCore, 0, 0, -3.5f);
        animator.rotate(neck, -0.1f, 0, 0);
        animator.rotate(head, -0.2f, 0, 0);
        animator.rotate(thighLeftJoint, -0.3f, 0, 0f);
        animator.rotate(thighRight, 0.4f, 0, 0.1f);
        animator.rotate(footRight, -0.5f, -0.1f, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(3);

        animator.setAnimation(EntityBarakoa.HEAL_STOP_ANIMATION);
        animator.startKeyframe(0);
        animator.move(talker, 0, 1, 0);
        animator.rotate(body, 0.3f, 0, 0);
        animator.move(modelCore, 0, 0, -3.5f);
        animator.rotate(neck, -0.1f, 0, 0);
        animator.rotate(head, -0.2f, 0, 0);
        animator.rotate(thighLeftJoint, -0.3f, 0, 0f);
        animator.rotate(thighRight, 0.4f, 0, 0.1f);
        animator.rotate(footRight, -0.5f, -0.1f, 0);
        animator.endKeyframe();
        animator.resetKeyframe(6);

        float heal = talker.y;
        float wandWave = talker.z;
        float healAnimSpeed = 0.4f;
        flap(modelCore, 0.6f * healAnimSpeed, 0.1f * heal, false, 0, 0, frame, 1f);
        flap(body, 0.6f * healAnimSpeed, 0.1f * heal, false, -0.5f, 0, frame, 1f);
        flap(chest, 0.6f * healAnimSpeed, 0.1f * heal, false, -1f, 0, frame, 1f);
        flap(head, 0.6f * healAnimSpeed, 0.1f * heal, false, -1.5f, 0, frame, 1f);
        armUpperRight.zRot += 0.4f * heal;
        armUpperRight.xRot -= 1.7f * heal;
        armLowerRight.xRot += 0.3f * heal;
        handRight.xRot += 0.3f * heal;
        handRight.yRot += 2f * heal;
        armUpperLeft.zRot -= 0.6f * heal;
        armUpperLeft.xRot -= 1.3f * heal;
        armLowerLeft.xRot -= 0.4f * heal;
        handLeft.xRot += 0.3f * heal;
        handLeft.yRot -= 1.8f * heal;
        flap(thighLeftJoint, 0.6f * healAnimSpeed, 0.1f * heal, true, 0, 0, frame, 1f);
        flap(thighLeftJoint, 0.6f * healAnimSpeed, 0.1f * heal, true, -0.5f, 0, frame, 1f);
        flap(thighRightJoint, 0.6f * healAnimSpeed, 0.1f * heal, true, 0, 0, frame, 1f);
        flap(thighRightJoint, 0.6f * healAnimSpeed, 0.1f * heal, true, -0.5f, 0, frame, 1f);
        walk(calfLeft, 0.6f * healAnimSpeed, 0.1f * heal, false, 0, 0, frame, 1f);
        walk(calfLeft, 0.6f * healAnimSpeed, 0.1f * heal, false, -0.5f, 0, frame, 1f);
        walk(calfRight, 0.6f * healAnimSpeed, 0.1f * heal, true, 0, 0, frame, 1f);
        walk(calfRight, 0.6f * healAnimSpeed, 0.1f * heal, true, -0.5f, 0, frame, 1f);
        walk(footLeft, 0.6f * healAnimSpeed, 0.1f * heal, true, 0, 0, frame, 1f);
        walk(footLeft, 0.6f * healAnimSpeed, 0.1f * heal, true, -0.5f, 0, frame, 1f);
        walk(footRight, 0.6f * healAnimSpeed, 0.1f * heal, false, 0, 0, frame, 1f);
        walk(footRight, 0.6f * healAnimSpeed, 0.1f * heal, false, -0.5f, 0, frame, 1f);
        float f = (float) (Math.cos(frame * 0.6f * healAnimSpeed) * 0.1f * heal);
        thighLeftJoint.z -= f * 5;
        thighLeftJoint.x -= f * 5;
        thighRightJoint.z += f * 5;
        thighRightJoint.x -= f * 5;

        float waveFrame = entity.getAnimationTick() + delta;
        float offset = -1f;
        flap(armUpperRight, 2.3f * healAnimSpeed, 0.2f * wandWave, false, 0 + offset, 0, waveFrame, 1f);
        walk(armUpperRight, 2.3f * healAnimSpeed, 0.2f * wandWave, false, (float) (Math.PI/2f) + offset, 0, waveFrame, 1f);
        flap(armLowerRight, 2.3f * healAnimSpeed, 0.2f * wandWave, false, -0.6f + offset, 0, waveFrame, 1f);
        walk(armLowerRight, 2.3f * healAnimSpeed, 0.2f * wandWave, false, (float) (Math.PI/2f) - 0.6f + offset, 0, waveFrame, 1f);
        flap(handRight, 2.3f * healAnimSpeed, 0.3f * wandWave, false, -1.2f + offset, 0, waveFrame, 1f);
        walk(handRight, 2.3f * healAnimSpeed, 0.2f * wandWave, false, (float) (Math.PI/2f) - 1.2f + offset, 0, waveFrame, 1f);


        animator.setAnimation(EntityBarakoa.TELEPORT_ANIMATION);
        animator.startKeyframe(2);
        animator.move(body, 0, 5f, 1f);
        animator.rotate(body, 0.3f, 0, 0);
        animator.rotate(thighLeftJoint, -0.3f, 0, 0);
        animator.rotate(thighRightJoint, -0.3f, 0, 0);
        animator.rotate(thighLeft, -0.5f, 0, 0);
        animator.rotate(thighRight, -0.5f, 0, 0);
        animator.rotate(calfLeft, 0.8f, 0, 0);
        animator.rotate(calfRight, 0.8f, 0, 0);
        animator.rotate(footLeft, -0.3f, 0, 0);
        animator.rotate(footRight, -0.3f, 0, 0);
        animator.rotate(neck, -0.4f, 0, 0);
        animator.rotate(head, -0.1f, 0, 0);
        animator.rotate(armUpperLeft, 0, -0.4f, -0.5f);
        animator.rotate(armUpperRight, 0, 0.4f, 0.5f);
        animator.rotate(armLowerLeft, -0.7f, 0, 0);
        animator.rotate(armLowerRight, -0.7f, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(1);
        animator.startKeyframe(6);
        animator.rotate(talker, 1, 0, 0);
        animator.move(body, 0, -10f, -1f);
        animator.rotate(body, 0.3f, 0, 0);
        animator.rotate(thighLeftJoint, -0.3f, 0, 0);
        animator.rotate(thighRightJoint, -0.3f, 0, 0);
        animator.rotate(thighLeft, 0.5f, 0, 0);
        animator.rotate(thighRight, 0.5f, 0, 0);
        animator.rotate(calfLeft, -0.8f, 0, 0);
        animator.rotate(calfRight, -0.8f, 0, 0);
        animator.rotate(footLeft, 0.7f, 0, 0);
        animator.rotate(footRight, 0.7f, 0, 0);
        animator.rotate(neck, 0f, 0, 0);
        animator.rotate(head, 0f, 0, 0);
        animator.rotate(armUpperLeft, 0.3f, 0, 0.3f);
        animator.rotate(armUpperRight, 0.3f, 0, -0.3f);
        animator.rotate(armLowerLeft, 0.2f, 0, 0);
        animator.rotate(armLowerRight, 0.2f, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(9);
        animator.resetKeyframe(9);

        float ballCurl = (float) Math.pow(talker.xRot, 7);
        float ballCurl2 = (float) Math.pow(talker.xRot, 15);
        body.xRot += ballCurl * 4;
        neck.xRot += ballCurl * 0.5;
        head.xRot += ballCurl * 0.5;
        armUpperLeft.xRot -= 1 * ballCurl;
        armUpperRight.xRot -= 1 * ballCurl;
        armLowerLeft.xRot -= 1 * ballCurl;
        armLowerRight.xRot -= 1 * ballCurl;
        thighLeftJoint.xRot -= 1 * ballCurl;
        thighRightJoint.xRot -= 1 * ballCurl;
        calfLeft.xRot += 0.5 * ballCurl;
        calfRight.xRot += 0.5 * ballCurl;

        //Inactive
        if (!entity.active) {
            scaler.x += 0.999f;
            head.xRot -= 1.58f;
            body.y += 9f;
            body.z += 5f;
            if (!entity.isOnGround() && !entity.isInLava() && !entity.isInWater()) {
                body.xRot += 0.4f * frame;
            }
        }

        float flailSpeed = 2.3f;
        bob(modelCore, 0.3f * flailSpeed, 10f * flailer.x, true, frame, 1f);
        walk(thighLeft, 0.3f * flailSpeed, 0.6f * flailer.x, false, 0, -0.3f * flailer.x, frame, 1f);
        walk(calfLeft, 0.3f * flailSpeed, 0.5f * flailer.x, true, 0, 0.3f * flailer.x, frame, 1f);
        walk(footLeft, 0.3f * flailSpeed, 0.2f * flailer.x, true, 0, 0, frame, 1f);
        walk(thighRight, 0.3f * flailSpeed, 0.6f * flailer.x, true, 0, 0.3f * flailer.x, frame, 1f);
        walk(calfRight, 0.3f * flailSpeed, 0.5f * flailer.x, false, 0, -0.3f * flailer.x, frame, 1f);
        walk(footRight, 0.3f * flailSpeed, 0.2f * flailer.x, false, 0, 0, frame, 1f);
        armRightJoint.xRot -= 1.7 * flailer.x;
        handRight.xRot += 1 * flailer.x;
        walk(armUpperRight, 0.6f * flailSpeed, 0.5f * flailer.x, false, 0, -0.3f * flailer.x, frame, 1f);
        walk(armLowerRight, 0.6f * flailSpeed, 0.5f * flailer.x, true, 0, 0, frame, 1f);
        armLeftJoint.xRot -= 1.7 * flailer.x;
        walk(armUpperLeft, 0.6f * flailSpeed, 0.5f * flailer.x, false, 0, -0.3f * flailer.x, frame, 1f);
        walk(armLowerLeft, 0.6f * flailSpeed, 0.5f * flailer.x, true, 0, 0, frame, 1f);

        float scale = 1 - scaler.x;
        body.setScale(scale, scale, scale);
        body.setScale(body.scaleX * (1 - ballCurl2), body.scaleY * (1 - ballCurl2), body.scaleZ * (1 - ballCurl2));
        maskBase.setScale(1 / scale, 1 / scale, 1 / scale);


        float talk = talker.x;
        float dance = entity.dancing.getAnimationProgressSinSqrt();
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
        boolean frozen = frozenCapability != null && frozenCapability.getFrozen();
        if (!frozen) {
            walk(head, 1.5f, 0.1f * talk, false, 0, -0.5f * talk, frame, 1f);
            walk(neck, 0, 0, false, 0, 0.5f * talk, frame, 1f);
            walk(armUpperRight, 0.5f, 0.2f * talk, false, 0, -0.7f * talk, frame, 1f);
            flap(armUpperRight, 0.4f, 0.2f * talk, false, 2, 0, frame, 1f);
            walk(armLowerRight, 0.5f, 0.2f * talk, false, -1, 0.3f * talk, frame, 1f);
            swing(handRight, 0.5f, 0.2f * talk, false, -2, 1.8f * talk, frame, 1f);
            walk(armUpperLeft, 0.5f, 0.2f * talk, false, 0, -0.7f * talk, frame, 1f);
            flap(armUpperLeft, 0.4f, 0.2f * talk, true, 2, 0, frame, 1f);
            walk(armLowerLeft, 0.5f, 0.2f * talk, false, -1, 0.3f * talk, frame, 1f);
            swing(handLeft, 0.5f, 0.2f * talk, false, -2, -1.8f * talk, frame, 1f);

//            armUpperRight.rotateAngleZ += 0.4f;

            
            if (Math.sin(frame * 1.8f) * (talk + dance) > 0.1f) {
                mouthLeft.visible = true;
                mouthRight.visible = true;
            }
            else {
                mouthLeft.visible = false;
                mouthRight.visible = false;
            }
            if (entity.getMask() == MaskType.FURY) {
                armLeftJoint.xRot += 0.2 * talk;
                armLeftJoint.yRot -= 1.3 * talk;
                armLowerLeft.xRot += 0.2 * talk;
                armLowerLeft.yRot -= 0.2 * talk;
                armLowerLeft.zRot -= 1 * talk;
            }
        }
    }
}

