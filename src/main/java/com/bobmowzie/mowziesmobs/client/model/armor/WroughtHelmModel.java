package com.bobmowzie.mowziesmobs.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class WroughtHelmModel<T extends LivingEntity> extends BipedModel<T> {
    private final ModelRenderer shape1;
    private final ModelRenderer tuskRight1;
    private final ModelRenderer tuskRight2;
    private final ModelRenderer hornRight1;
    private final ModelRenderer hornLeft;
    private final ModelRenderer tuskLeft1;
    private final ModelRenderer tuskLeft2;
    private final ModelRenderer hornLeft1;
    private final ModelRenderer hornRight;

    public WroughtHelmModel() {
        super(0.0f);
        this.textureWidth = 64;
        this.textureHeight = 32;
        bipedHead.cubeList.clear();
        bipedHeadwear.cubeList.clear();

        shape1 = new ModelRenderer(this);
        shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        shape1.setTextureOffset(0, 12).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);

        tuskRight1 = new ModelRenderer(this);
        tuskRight1.setRotationPoint(-2.5F, -1.5F, -2.5F);
        setRotationAngle(tuskRight1, 0.4363F, 0.7854F, 0.0F);
        tuskRight1.setTextureOffset(40, 24).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        tuskRight2 = new ModelRenderer(this);
        tuskRight2.setRotationPoint(1.0F, 1.5F, -5.0F);
        tuskRight1.addChild(tuskRight2);
        setRotationAngle(tuskRight2, -0.8727F, 0.0F, 0.0F);
        tuskRight2.setTextureOffset(34, 4).addBox(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

        hornRight1 = new ModelRenderer(this);
        hornRight1.setRotationPoint(3.0F, -8.0F, -3.0F);
        setRotationAngle(hornRight1, -0.3491F, -0.7854F, 0.0F);
        hornRight1.setTextureOffset(8, 3).addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, 0.0F, true);

        hornLeft = new ModelRenderer(this);
        hornLeft.setRotationPoint(-1.0F, 1.5F, -6.0F);
        hornRight1.addChild(hornLeft);
        setRotationAngle(hornLeft, -1.2217F, 0.0F, 0.0F);
        hornLeft.setTextureOffset(43, 12).addBox(0.0F, -2.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, true);

        tuskLeft1 = new ModelRenderer(this);
        tuskLeft1.setRotationPoint(2.5F, -1.5F, -2.5F);
        setRotationAngle(tuskLeft1, 0.4363F, -0.7854F, 0.0F);
        tuskLeft1.setTextureOffset(40, 24).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        tuskLeft2 = new ModelRenderer(this);
        tuskLeft2.setRotationPoint(1.0F, 1.5F, -5.0F);
        tuskLeft1.addChild(tuskLeft2);
        setRotationAngle(tuskLeft2, -0.8727F, 0.0F, 0.0F);
        tuskLeft2.setTextureOffset(34, 4).addBox(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

        hornLeft1 = new ModelRenderer(this);
        hornLeft1.setRotationPoint(-3.0F, -8.0F, -3.0F);
        setRotationAngle(hornLeft1, -0.3491F, 0.7854F, 0.0F);
        hornLeft1.setTextureOffset(8, 3).addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, 0.0F, true);

        hornRight = new ModelRenderer(this);
        hornRight.setRotationPoint(-1.0F, 1.5F, -6.0F);
        hornLeft1.addChild(hornRight);
        setRotationAngle(hornRight, -1.2217F, 0.0F, 0.0F);
        hornRight.setTextureOffset(30, 12).addBox(0.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);

        this.bipedHead.addChild(this.shape1);
        this.bipedHead.addChild(this.hornLeft1);
        this.bipedHead.addChild(this.hornRight1);
        this.bipedHead.addChild(this.tuskLeft1);
        this.bipedHead.addChild(this.tuskRight1);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
