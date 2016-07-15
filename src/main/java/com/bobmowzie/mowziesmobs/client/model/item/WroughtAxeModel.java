package com.bobmowzie.mowziesmobs.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WroughtAxeModel extends ModelBase {
    public ModelRenderer axeHandle;
    public ModelRenderer axeBladeRight;
    public ModelRenderer axeBladeLeft;
    public ModelRenderer axeBladeRight1;
    public ModelRenderer axeBladeRight2;
    public ModelRenderer axeBladeRight3;
    public ModelRenderer axeBladeLeft1;
    public ModelRenderer axeBladeLeft2;
    public ModelRenderer axeBladeLeft3;

    public WroughtAxeModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.axeBladeLeft = new ModelRenderer(this, 0, 0);
        this.axeBladeLeft.setRotationPoint(0.0F, -37.0F, 0.0F);
        this.axeBladeLeft.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(axeBladeLeft, 0.0F, -3.9269908169872414F, 0.0F);
        this.axeHandle = new ModelRenderer(this, 0, 0);
        this.axeHandle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeHandle.addBox(-1.5F, -44.0F, -1.5F, 3, 50, 3, 0.0F);
        this.axeBladeRight3 = new ModelRenderer(this, 12, 0);
        this.axeBladeRight3.setRotationPoint(17.7F, 2.3F, 0.0F);
        this.axeBladeRight3.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeRight3, 0.0F, 0.0F, 2.6179938779914944F);
        this.axeBladeLeft3 = new ModelRenderer(this, 12, 0);
        this.axeBladeLeft3.setRotationPoint(17.7F, 2.3F, 0.0F);
        this.axeBladeLeft3.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeLeft3, 0.0F, 0.0F, 2.6179938779914944F);
        this.axeBladeLeft2 = new ModelRenderer(this, 12, 0);
        this.axeBladeLeft2.mirror = true;
        this.axeBladeLeft2.setRotationPoint(17.7F, -3.2F, 0.02F);
        this.axeBladeLeft2.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeLeft2, 0.0F, 0.0F, 0.5235987755982988F);
        this.axeBladeLeft1 = new ModelRenderer(this, 12, 19);
        this.axeBladeLeft1.setRotationPoint(0.0F, 0.0F, 0.01F);
        this.axeBladeLeft1.addBox(0.0F, -4.5F, -1.0F, 10, 8, 2, 0.0F);
        this.axeBladeRight2 = new ModelRenderer(this, 12, 0);
        this.axeBladeRight2.mirror = true;
        this.axeBladeRight2.setRotationPoint(17.7F, -3.2F, 0.02F);
        this.axeBladeRight2.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        this.setRotateAngle(axeBladeRight2, 0.0F, 0.0F, 0.5235987755982988F);
        this.axeBladeRight1 = new ModelRenderer(this, 12, 19);
        this.axeBladeRight1.setRotationPoint(0.0F, 0.0F, 0.01F);
        this.axeBladeRight1.addBox(0.0F, -4.5F, -1.0F, 10, 8, 2, 0.0F);
        this.axeBladeRight = new ModelRenderer(this, 0, 0);
        this.axeBladeRight.setRotationPoint(0.0F, -37.0F, 0.0F);
        this.axeBladeRight.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(axeBladeRight, 0.0F, -0.7853981633974483F, 0.0F);
        this.axeHandle.addChild(this.axeBladeLeft);
        this.axeBladeRight.addChild(this.axeBladeRight3);
        this.axeBladeLeft.addChild(this.axeBladeLeft3);
        this.axeBladeLeft.addChild(this.axeBladeLeft2);
        this.axeBladeLeft.addChild(this.axeBladeLeft1);
        this.axeBladeRight.addChild(this.axeBladeRight2);
        this.axeBladeRight.addChild(this.axeBladeRight1);
        this.axeHandle.addChild(this.axeBladeRight);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.axeHandle.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
