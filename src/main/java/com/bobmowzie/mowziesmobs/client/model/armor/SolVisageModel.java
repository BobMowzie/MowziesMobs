package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Tribe Leader - Undefined
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class SolVisageModel<T extends LivingEntity> extends ModelBipedAnimated {
    public RendererModel maskBase;
    public RendererModel maskFace;
    public RendererModel headdress1back;
    public RendererModel headdress2back;
    public RendererModel headdress3back;
    public RendererModel headdress4back;
    public RendererModel headdress5back;
    public RendererModel headdress6back;
    public RendererModel headdress7back;
    public RendererModel rightEar;
    public RendererModel leftEar;
    public RendererModel headdress7;
    public RendererModel headdress6;
    public RendererModel headdress5;
    public RendererModel headdress4;
    public RendererModel headdress3;
    public RendererModel headdress2;
    public RendererModel headdress1;
    public RendererModel maskMouth;
    public RendererModel forehead;
    public RendererModel nose;
    public RendererModel upperLip;
    public RendererModel jaw;
    public RendererModel teethTop;
    public RendererModel lowerLip;
    public RendererModel leftLip;
    public RendererModel rightLip;
    public RendererModel teethBottom;
    public RendererModel rightEarring;
    public RendererModel leftEarring;

    public SolVisageModel() {
        super(0.0f);
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.headdress5 = new RendererModel(this, 27, 76);
        this.headdress5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress5.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress5, -2.6179938779914944F, 0.0F, 1.7453292519943295F);
        this.lowerLip = new RendererModel(this, 50, 124);
        this.lowerLip.mirror = true;
        this.lowerLip.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.lowerLip.addBox(-6.0F, 0.0F, -2.0F, 12, 2, 2, 0.0F);
        this.setRotateAngle(lowerLip, 0.0F, 0.0F, 3.141592653589793F);
        this.headdress1 = new RendererModel(this, 27, 76);
        this.headdress1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress1.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress1, -2.6179938779914944F, 0.0F, 3.141592653589793F);
        this.upperLip = new RendererModel(this, 50, 124);
        this.upperLip.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.upperLip.addBox(-6.0F, -2.0F, 0.0F, 12, 2, 2, 0.0F);
        this.headdress1back = new RendererModel(this, 27, 76);
        this.headdress1back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress1back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress1back, -0.5235987755982988F, 0.0F, 0.0F);
        this.headdress5back = new RendererModel(this, 27, 76);
        this.headdress5back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress5back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress5back, -0.5235987755982988F, 0.0F, -1.3962634015954636F);
        this.headdress2back = new RendererModel(this, 27, 76);
        this.headdress2back.mirror = true;
        this.headdress2back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress2back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress2back, -0.5235987755982988F, 0.0F, 0.6981317007977318F);
        this.headdress3 = new RendererModel(this, 27, 76);
        this.headdress3.mirror = true;
        this.headdress3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress3.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress3, -2.6179938779914944F, 0.0F, -1.7453292519943295F);
        this.rightLip = new RendererModel(this, 26, 120);
        this.rightLip.setRotationPoint(-6.0F, 0.0F, 0.0F);
        this.rightLip.addBox(0.0F, 0.0F, -2.0F, 2, 2, 2, 0.0F);
        this.headdress2 = new RendererModel(this, 27, 76);
        this.headdress2.mirror = true;
        this.headdress2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress2.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress2, -2.6179938779914944F, 0.0F, -2.443460952792061F);
        this.forehead = new RendererModel(this, 0, 122);
        this.forehead.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.forehead.addBox(-6.0F, -0.0F, -2.0F, 12, 4, 2, 0.0F);
        this.leftLip = new RendererModel(this, 26, 120);
        this.leftLip.mirror = true;
        this.leftLip.setRotationPoint(6.0F, 0.0F, 0.0F);
        this.leftLip.addBox(-2.0F, 0.0F, -2.0F, 2, 2, 2, 0.0F);
        this.headdress7back = new RendererModel(this, 27, 76);
        this.headdress7back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress7back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress7back, -0.5235987755982988F, 0.0F, -2.0943951023931953F);
        this.headdress7 = new RendererModel(this, 27, 76);
        this.headdress7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress7.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress7, -2.6179938779914944F, 0.0F, 1.0471975511965976F);
        this.maskBase = new RendererModel(this, 0, 0);
        this.maskBase.setRotationPoint(0.0F, -7.777372F, -4F);
        this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.jaw = new RendererModel(this, 48, 109);
        this.jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.jaw.addBox(-6.0F, 0.0F, 0.0F, 12, 4, 7, 0.0F);
        this.headdress4 = new RendererModel(this, 27, 76);
        this.headdress4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress4.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress4, -2.6179938779914944F, 0.0F, 2.443460952792061F);
        this.leftEar = new RendererModel(this, 38, 109);
        this.leftEar.mirror = true;
        this.leftEar.setRotationPoint(6.0F, -2.0F, 0.0F);
        this.leftEar.addBox(0.0F, 0.0F, 0.0F, 3, 6, 2, 0.0F);
        this.setRotateAngle(leftEar, 0.0F, -0.5235987755982988F, 0.0F);
        this.headdress4back = new RendererModel(this, 27, 76);
        this.headdress4back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress4back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress4back, -0.5235987755982988F, 0.0F, -0.6981317007977318F);
        this.teethBottom = new RendererModel(this, 0, 120);
        this.teethBottom.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.teethBottom.addBox(-4.0F, -1.0F, 0.0F, 8, 1, 1, 0.0F);
        this.setRotateAngle(teethBottom, 3.141592653589793F, 0.0F, -3.141592653589793F);
        this.headdress6 = new RendererModel(this, 27, 76);
        this.headdress6.mirror = true;
        this.headdress6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress6.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress6, -2.6179938779914944F, 0.0F, -1.0471975511965976F);
        this.rightEar = new RendererModel(this, 38, 109);
        this.rightEar.setRotationPoint(-6.0F, -2.0F, 0.0F);
        this.rightEar.addBox(-3.0F, 0.0F, 0.0F, 3, 6, 2, 0.0F);
        this.setRotateAngle(rightEar, 0.0F, 0.5235987755982988F, 0.0F);
        this.headdress6back = new RendererModel(this, 27, 76);
        this.headdress6back.mirror = true;
        this.headdress6back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress6back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress6back, -0.5235987755982988F, 0.0F, 2.0943951023931953F);
        this.teethTop = new RendererModel(this, 0, 120);
        this.teethTop.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.teethTop.addBox(-4.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        this.leftEarring = new RendererModel(this, 0, 84);
        this.leftEarring.mirror = true;
        this.leftEarring.setRotationPoint(2.5F, 6.0F, 1.0F);
        this.leftEarring.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.headdress3back = new RendererModel(this, 27, 76);
        this.headdress3back.mirror = true;
        this.headdress3back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headdress3back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress3back, -0.5235987755982988F, 0.0F, 1.3962634015954636F);
        this.rightEarring = new RendererModel(this, 0, 84);
        this.rightEarring.mirror = true;
        this.rightEarring.setRotationPoint(-2.5F, 6.0F, 1.0F);
        this.rightEarring.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.maskMouth = new RendererModel(this, 0, 0);
        this.maskMouth.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.maskMouth.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.nose = new RendererModel(this, 34, 117);
        this.nose.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.nose.addBox(-2.0F, 0.0F, 0.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(nose, -0.5235987755982988F, 0.0F, 0.0F);
        this.maskFace = new RendererModel(this, 0, 97);
        this.maskFace.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.maskFace.addBox(-6.0F, -6.0F, 0.0F, 12, 15, 7, 0.0F);
        this.maskBase.addChild(this.headdress5);
        this.jaw.addChild(this.lowerLip);
        this.maskBase.addChild(this.headdress1);
        this.maskMouth.addChild(this.upperLip);
        this.maskBase.addChild(this.headdress1back);
        this.maskBase.addChild(this.headdress5back);
        this.maskBase.addChild(this.headdress2back);
        this.maskBase.addChild(this.headdress3);
        this.jaw.addChild(this.rightLip);
        this.maskBase.addChild(this.headdress2);
        this.maskFace.addChild(this.forehead);
        this.jaw.addChild(this.leftLip);
        this.maskBase.addChild(this.headdress7back);
        this.maskBase.addChild(this.headdress7);
        this.maskMouth.addChild(this.jaw);
        this.maskBase.addChild(this.headdress4);
        this.maskBase.addChild(this.leftEar);
        this.maskBase.addChild(this.headdress4back);
        this.lowerLip.addChild(this.teethBottom);
        this.maskBase.addChild(this.headdress6);
        this.maskBase.addChild(this.rightEar);
        this.maskBase.addChild(this.headdress6back);
        this.upperLip.addChild(this.teethTop);
        this.leftEar.addChild(this.leftEarring);
        this.maskBase.addChild(this.headdress3back);
        this.rightEar.addChild(this.rightEarring);
        this.maskFace.addChild(this.maskMouth);
        this.maskFace.addChild(this.nose);
        this.maskBase.addChild(this.maskFace);

        this.bipedHead.addChild(maskBase);
    }

    @Override
    public void render(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 0.8f);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
