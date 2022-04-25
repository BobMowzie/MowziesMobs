package com.bobmowzie.mowziesmobs.client.model.armor;

import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Tribe Leader - Undefined
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class SolVisageModel<T extends LivingEntity> extends HumanoidModel<T> {
    public AdvancedModelRenderer maskBase;
    public ModelPart maskFace;
    public ModelPart headdress1back;
    public ModelPart headdress2back;
    public ModelPart headdress3back;
    public ModelPart headdress4back;
    public ModelPart headdress5back;
    public ModelPart headdress6back;
    public ModelPart headdress7back;
    public ModelPart rightEar;
    public ModelPart leftEar;
    public ModelPart headdress7;
    public ModelPart headdress6;
    public ModelPart headdress5;
    public ModelPart headdress4;
    public ModelPart headdress3;
    public ModelPart headdress2;
    public ModelPart headdress1;
    public ModelPart maskMouth;
    public ModelPart forehead;
    public ModelPart nose;
    public ModelPart upperLip;
    public ModelPart jaw;
    public ModelPart teethTop;
    public ModelPart lowerLip;
    public ModelPart leftLip;
    public ModelPart rightLip;
    public ModelPart teethBottom;
    public ModelPart rightEarring;
    public ModelPart leftEarring;

    public SolVisageModel() {
        super(0.0f);
        this.texWidth = 128;
        this.texHeight = 128;
        head.cubes.clear();
        this.headdress5 = new ModelPart(this, 27, 76);
        this.headdress5.setPos(0.0F, 0.0F, 0.0F);
        this.headdress5.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress5, -2.6179938779914944F, 0.0F, 1.7453292519943295F);
        this.lowerLip = new ModelPart(this, 50, 124);
        this.lowerLip.mirror = true;
        this.lowerLip.setPos(0.0F, 4.0F, 0.0F);
        this.lowerLip.addBox(-6.0F, 0.0F, -2.0F, 12, 2, 2, 0.0F);
        this.setRotateAngle(lowerLip, 0.0F, 0.0F, 3.141592653589793F);
        this.headdress1 = new ModelPart(this, 27, 76);
        this.headdress1.setPos(0.0F, 0.0F, 0.0F);
        this.headdress1.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress1, -2.6179938779914944F, 0.0F, 3.141592653589793F);
        this.upperLip = new ModelPart(this, 50, 124);
        this.upperLip.setPos(0.0F, 0.0F, -2.0F);
        this.upperLip.addBox(-6.0F, -2.0F, 0.0F, 12, 2, 2, 0.0F);
        this.headdress1back = new ModelPart(this, 27, 76);
        this.headdress1back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress1back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress1back, -0.5235987755982988F, 0.0F, 0.0F);
        this.headdress5back = new ModelPart(this, 27, 76);
        this.headdress5back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress5back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress5back, -0.5235987755982988F, 0.0F, -1.3962634015954636F);
        this.headdress2back = new ModelPart(this, 27, 76);
        this.headdress2back.mirror = true;
        this.headdress2back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress2back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress2back, -0.5235987755982988F, 0.0F, 0.6981317007977318F);
        this.headdress3 = new ModelPart(this, 27, 76);
        this.headdress3.mirror = true;
        this.headdress3.setPos(0.0F, 0.0F, 0.0F);
        this.headdress3.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress3, -2.6179938779914944F, 0.0F, -1.7453292519943295F);
        this.rightLip = new ModelPart(this, 26, 120);
        this.rightLip.setPos(-6.0F, 0.0F, 0.0F);
        this.rightLip.addBox(0.0F, 0.0F, -2.0F, 2, 2, 2, 0.0F);
        this.headdress2 = new ModelPart(this, 27, 76);
        this.headdress2.mirror = true;
        this.headdress2.setPos(0.0F, 0.0F, 0.0F);
        this.headdress2.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress2, -2.6179938779914944F, 0.0F, -2.443460952792061F);
        this.forehead = new ModelPart(this, 0, 122);
        this.forehead.setPos(0.0F, -6.0F, 0.0F);
        this.forehead.addBox(-6.0F, -0.0F, -2.0F, 12, 4, 2, 0.0F);
        this.leftLip = new ModelPart(this, 26, 120);
        this.leftLip.mirror = true;
        this.leftLip.setPos(6.0F, 0.0F, 0.0F);
        this.leftLip.addBox(-2.0F, 0.0F, -2.0F, 2, 2, 2, 0.0F);
        this.headdress7back = new ModelPart(this, 27, 76);
        this.headdress7back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress7back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress7back, -0.5235987755982988F, 0.0F, -2.0943951023931953F);
        this.headdress7 = new ModelPart(this, 27, 76);
        this.headdress7.setPos(0.0F, 0.0F, 0.0F);
        this.headdress7.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress7, -2.6179938779914944F, 0.0F, 1.0471975511965976F);
        this.maskBase = new AdvancedModelRenderer(this, 0, 0);
        this.maskBase.setPos(0.0F, -6.777372F, -4F);
        this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.maskBase.setScale(0.8f);
        this.jaw = new ModelPart(this, 48, 109);
        this.jaw.setPos(0.0F, 0.0F, 0.0F);
        this.jaw.addBox(-6.0F, 0.0F, 0.0F, 12, 4, 7, 0.0F);
        this.headdress4 = new ModelPart(this, 27, 76);
        this.headdress4.setPos(0.0F, 0.0F, 0.0F);
        this.headdress4.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress4, -2.6179938779914944F, 0.0F, 2.443460952792061F);
        this.leftEar = new ModelPart(this, 38, 109);
        this.leftEar.mirror = true;
        this.leftEar.setPos(6.0F, -2.0F, 0.0F);
        this.leftEar.addBox(0.0F, 0.0F, 0.0F, 3, 6, 2, 0.0F);
        this.setRotateAngle(leftEar, 0.0F, -0.5235987755982988F, 0.0F);
        this.headdress4back = new ModelPart(this, 27, 76);
        this.headdress4back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress4back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress4back, -0.5235987755982988F, 0.0F, -0.6981317007977318F);
        this.teethBottom = new ModelPart(this, 0, 120);
        this.teethBottom.setPos(0.0F, 3.0F, 0.0F);
        this.teethBottom.addBox(-4.0F, -1.0F, 0.0F, 8, 1, 1, 0.0F);
        this.setRotateAngle(teethBottom, 3.141592653589793F, 0.0F, -3.141592653589793F);
        this.headdress6 = new ModelPart(this, 27, 76);
        this.headdress6.mirror = true;
        this.headdress6.setPos(0.0F, 0.0F, 0.0F);
        this.headdress6.addBox(-3.0F, -18.0F, 0.0F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress6, -2.6179938779914944F, 0.0F, -1.0471975511965976F);
        this.rightEar = new ModelPart(this, 38, 109);
        this.rightEar.setPos(-6.0F, -2.0F, 0.0F);
        this.rightEar.addBox(-3.0F, 0.0F, 0.0F, 3, 6, 2, 0.0F);
        this.setRotateAngle(rightEar, 0.0F, 0.5235987755982988F, 0.0F);
        this.headdress6back = new ModelPart(this, 27, 76);
        this.headdress6back.mirror = true;
        this.headdress6back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress6back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress6back, -0.5235987755982988F, 0.0F, 2.0943951023931953F);
        this.teethTop = new ModelPart(this, 0, 120);
        this.teethTop.setPos(0.0F, 0.0F, 1.0F);
        this.teethTop.addBox(-4.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        this.leftEarring = new ModelPart(this, 0, 84);
        this.leftEarring.mirror = true;
        this.leftEarring.setPos(2.5F, 6.0F, 1.0F);
        this.leftEarring.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.headdress3back = new ModelPart(this, 27, 76);
        this.headdress3back.mirror = true;
        this.headdress3back.setPos(0.0F, 0.0F, 0.0F);
        this.headdress3back.addBox(-3.0F, -18.0F, 0.01F, 6, 12, 0, 0.0F);
        this.setRotateAngle(headdress3back, -0.5235987755982988F, 0.0F, 1.3962634015954636F);
        this.rightEarring = new ModelPart(this, 0, 84);
        this.rightEarring.mirror = true;
        this.rightEarring.setPos(-2.5F, 6.0F, 1.0F);
        this.rightEarring.addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0, 0.0F);
        this.maskMouth = new ModelPart(this, 0, 0);
        this.maskMouth.setPos(0.0F, 5.0F, 0.0F);
        this.maskMouth.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.nose = new ModelPart(this, 34, 117);
        this.nose.setPos(0.0F, -4.0F, 0.0F);
        this.nose.addBox(-2.0F, 0.0F, 0.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(nose, -0.5235987755982988F, 0.0F, 0.0F);
        this.maskFace = new ModelPart(this, 0, 97);
        this.maskFace.setPos(0.0F, 0.0F, -2.0F);
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

        this.head.addChild(maskBase);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        hat.visible = false;
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
        ModelRenderer.xRot = x;
        ModelRenderer.yRot = y;
        ModelRenderer.zRot = z;
    }
}
