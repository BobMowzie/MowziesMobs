package com.bobmowzie.mowziesmobs.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class WroughtHelmModel<T extends LivingEntity> extends BipedModel<T> {
    public ModelRenderer shape1;
    public ModelRenderer tuskRight1;
    public ModelRenderer hornRight1;
    public ModelRenderer tuskLeft1;
    public ModelRenderer hornLeft1;
    public ModelRenderer tuskRight2;
    public ModelRenderer hornRight2;
    public ModelRenderer tuskLeft2;
    public ModelRenderer hornLeft2;

    public WroughtHelmModel() {
        super(0.0f);
        this.textureWidth = 64;
        this.textureHeight = 64;
        bipedHead.cubeList.clear();
        this.shape1 = new ModelRenderer(this, 0, 44);
        this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(-5.0F, -9.0F, -5.0F, 10, 10, 10, 0.0F);
        this.tuskLeft1 = new ModelRenderer(this, 40, 55);
        this.tuskLeft1.setRotationPoint(-3.0F, -1.0F, -3.0F);
        this.tuskLeft1.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        this.setRotateAngle(tuskLeft1, 0.4363323129985824F, 0.7853981633974483F, 0.0F);
        this.hornRight2 = new ModelRenderer(this, 34, 32);
        this.hornRight2.setRotationPoint(-1.0F, 1.5F, -8.0F);
        this.hornRight2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(hornRight2, -1.2217304763960306F, 0.0F, 0.0F);
        this.tuskRight2 = new ModelRenderer(this, 34, 32);
        this.tuskRight2.setRotationPoint(-1.0F, 1.5F, -6.0F);
        this.tuskRight2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(tuskRight2, -0.8726646259971648F, 0.0F, 0.0F);
        this.hornLeft1 = new ModelRenderer(this, 8, 33);
        this.hornLeft1.setRotationPoint(-3.0F, -7.0F, -3.0F);
        this.hornLeft1.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        this.setRotateAngle(hornLeft1, -0.3490658503988659F, 0.7853981633974483F, 0.0F);
        this.tuskRight1 = new ModelRenderer(this, 40, 55);
        this.tuskRight1.setRotationPoint(3.0F, -1.0F, -3.0F);
        this.tuskRight1.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        this.setRotateAngle(tuskRight1, 0.4363323129985824F, -0.7853981633974483F, 0.0F);
        this.hornLeft2 = new ModelRenderer(this, 30, 41);
        this.hornLeft2.setRotationPoint(-1.0F, 1.5F, -8.0F);
        this.hornLeft2.addBox(0.0F, -2.0F, -11.0F, 2, 2, 11, 0.0F);
        this.setRotateAngle(hornLeft2, -1.2217304763960306F, 0.0F, 0.0F);
        this.tuskLeft2 = new ModelRenderer(this, 34, 32);
        this.tuskLeft2.setRotationPoint(-1.0F, 1.5F, -6.0F);
        this.tuskLeft2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(tuskLeft2, -0.8726646259971648F, 0.0F, 0.0F);
        this.hornRight1 = new ModelRenderer(this, 8, 33);
        this.hornRight1.setRotationPoint(3.0F, -7.0F, -3.0F);
        this.hornRight1.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        this.setRotateAngle(hornRight1, -0.3490658503988659F, -0.7853981633974483F, 0.0F);
        this.hornRight1.addChild(this.hornRight2);
        this.tuskRight1.addChild(this.tuskRight2);
        this.hornLeft1.addChild(this.hornLeft2);
        this.tuskLeft1.addChild(this.tuskLeft2);

        this.bipedHead.addChild(this.shape1);
        this.bipedHead.addChild(this.hornLeft1);
        this.bipedHead.addChild(this.hornRight1);
        this.bipedHead.addChild(this.tuskLeft1);
        this.bipedHead.addChild(this.tuskRight1);
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
