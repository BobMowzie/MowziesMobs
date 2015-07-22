package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import com.bobmowzie.mowziesmobs.client.model.tools.MowzieModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * Tribe Leader - Undefined
 * Created using Tabula 5.1.0
 */
public class ModelTribeLeader extends ModelBase {
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer chest;
    public AdvancedModelRenderer rightThigh;
    public AdvancedModelRenderer leftThigh;
    public MowzieModelRenderer neckJoint;
    public AdvancedModelRenderer rightArmJoint;
    public AdvancedModelRenderer leftArmJoint;
    public MowzieModelRenderer neck;
    public MowzieModelRenderer headJoint;
    public MowzieModelRenderer head;
    public MowzieModelRenderer maskBase;
    public MowzieModelRenderer maskFace;
    public MowzieModelRenderer headdress1;
    public MowzieModelRenderer headdress2;
    public MowzieModelRenderer headdress3;
    public MowzieModelRenderer headdress4;
    public MowzieModelRenderer headdress5;
    public MowzieModelRenderer headdress6;
    public MowzieModelRenderer headdress7;
    public MowzieModelRenderer rightEar;
    public MowzieModelRenderer leftEar;
    public AdvancedModelRenderer maskMouth;
    public AdvancedModelRenderer forehead;
    public MowzieModelRenderer nose;
    public MowzieModelRenderer upperLip;
    public AdvancedModelRenderer jaw;
    public MowzieModelRenderer teethTop;
    public AdvancedModelRenderer lowerLip;
    public AdvancedModelRenderer leftLip;
    public AdvancedModelRenderer rightLip;
    public AdvancedModelRenderer teethBottom;
    public MowzieModelRenderer rightEarring;
    public MowzieModelRenderer leftEarring;
    public MowzieModelRenderer rightUpperArm;
    public MowzieModelRenderer rightLowerArm;
    public MowzieModelRenderer rightHanf;
    public MowzieModelRenderer rightUpperArm_1;
    public MowzieModelRenderer rightLowerArm_1;
    public MowzieModelRenderer rightHanf_1;
    public MowzieModelRenderer rightCalf;
    public MowzieModelRenderer rightFoot;
    public MowzieModelRenderer leftCalf;
    public MowzieModelRenderer leftFoot;

    public ModelTribeLeader() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.body = new AdvancedModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 24.0F, -3.0F);
        this.body.addBox(-12.5F, -16.0F, -11.0F, 25, 16, 22, 0.0F);
        this.teethTop = new MowzieModelRenderer(this, 0, 120);
        this.teethTop.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.teethTop.addBox(-4.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        this.maskBase = new MowzieModelRenderer(this, 0, 0);
        this.maskBase.setRotationPoint(0.0F, -5.0F, -6.0F);
        this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.leftEar = new MowzieModelRenderer(this, 38, 109);
        this.leftEar.mirror = true;
        this.leftEar.setRotationPoint(6.0F, -2.0F, 0.0F);
        this.leftEar.addBox(0.0F, 0.0F, 0.0F, 3, 6, 2, 0.0F);
        this.setRotateAngle(leftEar, 0.0F, -0.5235987755982988F, 0.0F);
        this.rightEarring = new MowzieModelRenderer(this, 0, 84);
        this.rightEarring.mirror = true;
        this.rightEarring.setRotationPoint(-2.5F, 6.0F, 1.0F);
        this.rightEarring.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.headdress3 = new MowzieModelRenderer(this, 27, 76);
        this.headdress3.mirror = true;
        this.headdress3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress3.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress3, -0.5235987755982988F, 0.0F, 1.3962634015954636F);
        this.rightLowerArm_1 = new MowzieModelRenderer(this, 56, 38);
        this.rightLowerArm_1.mirror = true;
        this.rightLowerArm_1.setRotationPoint(3.5F, 14.0F, 0.0F);
        this.rightLowerArm_1.addBox(-5.0F, 0.0F, -2.5F, 5, 11, 5, 0.0F);
        this.setRotateAngle(rightLowerArm_1, 0.0F, 0.0F, 1.2217304763960306F);
        this.rightCalf = new MowzieModelRenderer(this, 72, 0);
        this.rightCalf.setRotationPoint(0.0F, 14.0F, -4.0F);
        this.rightCalf.addBox(-3.0F, 0.0F, 0.0F, 6, 10, 6, 0.0F);
        this.setRotateAngle(rightCalf, 1.1838568316277536F, 0.0F, 0.0F);
        this.rightUpperArm = new MowzieModelRenderer(this, 38, 88);
        this.rightUpperArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightUpperArm.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
        this.setRotateAngle(rightUpperArm, -1.2217304763960306F, 1.0471975511965976F, 0.0F);
        this.lowerLip = new AdvancedModelRenderer(this, 50, 124);
        this.lowerLip.mirror = true;
        this.lowerLip.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.lowerLip.addBox(-6.0F, 0.0F, -2.0F, 12, 2, 2, 0.0F);
        this.setRotateAngle(lowerLip, 0.0F, 0.0F, 3.141592653589793F);
        this.rightFoot = new MowzieModelRenderer(this, 83, 27);
        this.rightFoot.setRotationPoint(0.0F, 11.0F, 5.0F);
        this.rightFoot.addBox(-4.0F, -2.0F, -10.0F, 8, 4, 11, 0.0F);
        this.setRotateAngle(rightFoot, -0.17453292519943295F, 0.0F, 0.0F);
        this.headdress2 = new MowzieModelRenderer(this, 27, 76);
        this.headdress2.mirror = true;
        this.headdress2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress2.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress2, -0.5235987755982988F, 0.0F, 0.6981317007977318F);
        this.neckJoint = new MowzieModelRenderer(this, 0, 0);
        this.neckJoint.setRotationPoint(0.0F, -8.0F, 10.0F);
        this.neckJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(neckJoint, 0.2617993877991494F, 0.0F, 0.0F);
        this.rightThigh = new AdvancedModelRenderer(this, 39, 66);
        this.rightThigh.setRotationPoint(-8.5F, -4.0F, 4.0F);
        this.rightThigh.addBox(-4.0F, 0.0F, -4.0F, 8, 14, 8, 0.0F);
        this.setRotateAngle(rightThigh, -1.3962634015954636F, 0.6108652381980153F, 0.0F);
        this.neck = new MowzieModelRenderer(this, 0, 62);
        this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck.addBox(-3.0F, -7.5F, -4.0F, 6, 10, 7, 0.0F);
        this.setRotateAngle(neck, 0.3490658503988659F, 0.0F, 0.0F);
        this.headdress4 = new MowzieModelRenderer(this, 27, 76);
        this.headdress4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress4.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress4, -0.5235987755982988F, 0.0F, -0.6981317007977318F);
        this.forehead = new AdvancedModelRenderer(this, 0, 122);
        this.forehead.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.forehead.addBox(-6.0F, -0.0F, -2.0F, 12, 4, 2, 0.0F);
        this.rightUpperArm_1 = new MowzieModelRenderer(this, 38, 88);
        this.rightUpperArm_1.mirror = true;
        this.rightUpperArm_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightUpperArm_1.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
        this.setRotateAngle(rightUpperArm_1, -1.2217304763960306F, -1.0471975511965976F, 0.0F);
        this.teethBottom = new AdvancedModelRenderer(this, 0, 120);
        this.teethBottom.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.teethBottom.addBox(-4.0F, -1.0F, 0.0F, 8, 1, 1, 0.0F);
        this.setRotateAngle(teethBottom, 3.141592653589793F, 0.0F, -3.141592653589793F);
        this.headdress5 = new MowzieModelRenderer(this, 27, 76);
        this.headdress5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress5.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress5, -0.5235987755982988F, 0.0F, -1.3962634015954636F);
        this.leftFoot = new MowzieModelRenderer(this, 83, 27);
        this.leftFoot.mirror = true;
        this.leftFoot.setRotationPoint(0.0F, 11.0F, 5.0F);
        this.leftFoot.addBox(-4.0F, -2.0F, -10.0F, 8, 4, 11, 0.0F);
        this.setRotateAngle(leftFoot, -0.17453292519943295F, 0.0F, 0.0F);
        this.rightLowerArm = new MowzieModelRenderer(this, 56, 38);
        this.rightLowerArm.setRotationPoint(-3.5F, 14.0F, 0.0F);
        this.rightLowerArm.addBox(0.0F, 0.0F, -2.5F, 5, 11, 5, 0.0F);
        this.setRotateAngle(rightLowerArm, 0.0F, 0.0F, -1.2217304763960306F);
        this.leftCalf = new MowzieModelRenderer(this, 72, 0);
        this.leftCalf.mirror = true;
        this.leftCalf.setRotationPoint(0.0F, 14.0F, -4.0F);
        this.leftCalf.addBox(-3.0F, 0.0F, 0.0F, 6, 10, 6, 0.0F);
        this.setRotateAngle(leftCalf, 1.1838568316277536F, 0.0F, 0.0F);
        this.rightHanf = new MowzieModelRenderer(this, 0, 43);
        this.rightHanf.setRotationPoint(2.5F, 11.0F, 0.0F);
        this.rightHanf.addBox(-3.0F, 0.0F, -1.5F, 6, 8, 3, 0.0F);
        this.setRotateAngle(rightHanf, 1.0471975511965976F, 0.6981317007977318F, 0.0F);
        this.headdress1 = new MowzieModelRenderer(this, 27, 76);
        this.headdress1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress1.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress1, -0.5235987755982988F, 0.0F, 0.0F);
        this.leftThigh = new AdvancedModelRenderer(this, 39, 66);
        this.leftThigh.mirror = true;
        this.leftThigh.setRotationPoint(8.5F, -4.0F, 4.0F);
        this.leftThigh.addBox(-4.0F, 0.0F, -4.0F, 8, 14, 8, 0.0F);
        this.setRotateAngle(leftThigh, -1.3962634015954636F, -0.6108652381980153F, 0.0F);
        this.jaw = new AdvancedModelRenderer(this, 48, 109);
        this.jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.jaw.addBox(-6.0F, 0.0F, 0.0F, 12, 4, 7, 0.01F);
        this.leftLip = new AdvancedModelRenderer(this, 26, 120);
        this.leftLip.mirror = true;
        this.leftLip.setRotationPoint(6.0F, 0.0F, 0.0F);
        this.leftLip.addBox(-2.0F, 0.0F, -2.0F, 2, 2, 2, 0.0F);
        this.maskMouth = new AdvancedModelRenderer(this, 0, 0);
        this.maskMouth.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.maskMouth.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.nose = new MowzieModelRenderer(this, 34, 117);
        this.nose.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.nose.addBox(-2.0F, 0.0F, 0.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(nose, -0.5235987755982988F, 0.0F, 0.0F);
        this.rightArmJoint = new AdvancedModelRenderer(this, 0, 0);
        this.rightArmJoint.setRotationPoint(-6.0F, -6.0F, 9.0F);
        this.rightArmJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(rightArmJoint, 0.2617993877991494F, 0.0F, 0.0F);
        this.leftArmJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leftArmJoint.setRotationPoint(6.0F, -6.0F, 9.0F);
        this.leftArmJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leftArmJoint, 0.2617993877991494F, 0.0F, 0.0F);
        this.headdress7 = new MowzieModelRenderer(this, 27, 76);
        this.headdress7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress7.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress7, -0.5235987755982988F, 0.0F, -2.0943951023931953F);
        this.rightLip = new AdvancedModelRenderer(this, 26, 120);
        this.rightLip.setRotationPoint(-6.0F, 0.0F, 0.0F);
        this.rightLip.addBox(0.0F, 0.0F, -2.0F, 2, 2, 2, 0.0F);
        this.upperLip = new MowzieModelRenderer(this, 50, 124);
        this.upperLip.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.upperLip.addBox(-6.0F, -2.0F, 0.0F, 12, 2, 2, 0.0F);
        this.rightHanf_1 = new MowzieModelRenderer(this, 0, 43);
        this.rightHanf_1.mirror = true;
        this.rightHanf_1.setRotationPoint(-2.5F, 11.0F, 0.0F);
        this.rightHanf_1.addBox(-3.0F, 0.0F, -1.5F, 6, 8, 3, 0.0F);
        this.setRotateAngle(rightHanf_1, 1.0471975511965976F, -0.6981317007977318F, 0.0F);
        this.rightEar = new MowzieModelRenderer(this, 38, 109);
        this.rightEar.setRotationPoint(-6.0F, -2.0F, 0.0F);
        this.rightEar.addBox(-3.0F, 0.0F, 0.0F, 3, 6, 2, 0.0F);
        this.setRotateAngle(rightEar, 0.0F, 0.5235987755982988F, 0.0F);
        this.headJoint = new MowzieModelRenderer(this, 0, 0);
        this.headJoint.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.headJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(headJoint, -0.3490658503988659F, 0.0F, 0.0F);
        this.headdress6 = new MowzieModelRenderer(this, 27, 76);
        this.headdress6.mirror = true;
        this.headdress6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress6.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress6, -0.5235987755982988F, 0.0F, 2.0943951023931953F);
        this.chest = new AdvancedModelRenderer(this, 2, 38);
        this.chest.setRotationPoint(0.0F, -16.0F, -7.0F);
        this.chest.addBox(-9.5F, -8.0F, 0.0F, 19, 8, 16, 0.0F);
        this.setRotateAngle(chest, -0.2617993877991494F, 0.0F, 0.0F);
        this.leftEarring = new MowzieModelRenderer(this, 0, 84);
        this.leftEarring.mirror = true;
        this.leftEarring.setRotationPoint(2.5F, 6.0F, 1.0F);
        this.leftEarring.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.maskFace = new MowzieModelRenderer(this, 0, 97);
        this.maskFace.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.maskFace.addBox(-6.0F, -6.0F, 0.0F, 12, 15, 7, 0.0F);
        this.head = new MowzieModelRenderer(this, 0, 79);
        this.head.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.head.addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, 0.0F);
        this.upperLip.addChild(this.teethTop);
        this.head.addChild(this.maskBase);
        this.maskBase.addChild(this.leftEar);
        this.rightEar.addChild(this.rightEarring);
        this.maskBase.addChild(this.headdress3);
        this.rightUpperArm_1.addChild(this.rightLowerArm_1);
        this.rightThigh.addChild(this.rightCalf);
        this.rightArmJoint.addChild(this.rightUpperArm);
        this.jaw.addChild(this.lowerLip);
        this.rightCalf.addChild(this.rightFoot);
        this.maskBase.addChild(this.headdress2);
        this.chest.addChild(this.neckJoint);
        this.body.addChild(this.rightThigh);
        this.neckJoint.addChild(this.neck);
        this.maskBase.addChild(this.headdress4);
        this.maskFace.addChild(this.forehead);
        this.leftArmJoint.addChild(this.rightUpperArm_1);
        this.lowerLip.addChild(this.teethBottom);
        this.maskBase.addChild(this.headdress5);
        this.leftCalf.addChild(this.leftFoot);
        this.rightUpperArm.addChild(this.rightLowerArm);
        this.leftThigh.addChild(this.leftCalf);
        this.rightLowerArm.addChild(this.rightHanf);
        this.maskBase.addChild(this.headdress1);
        this.body.addChild(this.leftThigh);
        this.maskMouth.addChild(this.jaw);
        this.jaw.addChild(this.leftLip);
        this.maskFace.addChild(this.maskMouth);
        this.maskFace.addChild(this.nose);
        this.chest.addChild(this.rightArmJoint);
        this.chest.addChild(this.leftArmJoint);
        this.maskBase.addChild(this.headdress7);
        this.jaw.addChild(this.rightLip);
        this.maskMouth.addChild(this.upperLip);
        this.rightLowerArm_1.addChild(this.rightHanf_1);
        this.maskBase.addChild(this.rightEar);
        this.neck.addChild(this.headJoint);
        this.maskBase.addChild(this.headdress6);
        this.body.addChild(this.chest);
        this.leftEar.addChild(this.leftEarring);
        this.maskBase.addChild(this.maskFace);
        this.headJoint.addChild(this.head);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.body.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float f, float f1, float partialTicks) {
        super.setLivingAnimations(entity, f, f1, partialTicks);

        float frame = entity.ticksExisted + partialTicks;

        float jawScale = 1;//(float) (1 + 1 * Math.pow(Math.sin(frame * 0.3), 2));
        float mouthScaleX = 1f;//(float) (1 - 0.25 * Math.pow(Math.sin(frame * 0.17), 2));
        float mouthScaleY = 1f;
        float foreheadScale = 1f;//(float) (0.75 + 0.5 * Math.pow(Math.sin(frame * 0.05),2));
        jaw.setScaleY(jawScale);
        lowerLip.setScaleY(1/jawScale);
        leftLip.setScaleY(1/jawScale * (jawScale * 4 - 2)/2);
        rightLip.setScaleY(1 / jawScale * (jawScale * 4 - 2) / 2);
        maskMouth.setScaleX(mouthScaleX);
        leftLip.setScaleX(1/mouthScaleX);
        rightLip.setScaleX(1/mouthScaleX);
        maskMouth.setScaleY(mouthScaleY);
        forehead.setScaleY(foreheadScale);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
