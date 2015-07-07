package com.bobmowzie.mowziesmobs.client.model.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * ModelWroughtHelm - Undefined
 * Created using Tabula 5.1.0
 */
public class ModelWroughtHelm extends ModelBiped {
    public ModelRenderer helmet;
    public ModelRenderer tuskRight1;
    public ModelRenderer hornRight1;
    public ModelRenderer tuskLeft1;
    public ModelRenderer hornLeft1;
    public ModelRenderer tuskRight2;
    public ModelRenderer hornRight2;
    public ModelRenderer tuskLeft2;
    public ModelRenderer hornLeft2;

    public ModelWroughtHelm() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.helmet = new ModelRenderer(this, 0, 12);
        this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet.addBox(-5.0F, -9.5F, -5.0F, 10, 10, 10, 0.0F);
        this.tuskLeft1 = new ModelRenderer(this, 40, 23);
        this.tuskLeft1.setRotationPoint(-2.5F, -1F, -2.5F);
        this.tuskLeft1.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        this.setRotateAngle(tuskLeft1, 0.4363323129985824F, 0.7853981633974483F, 0.0F);
        this.hornRight2 = new ModelRenderer(this, 34, 0);
        this.hornRight2.setRotationPoint(-1.0F, 1.5F, -8.0F);
        this.hornRight2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(hornRight2, -1.2217304763960306F, 0.0F, 0.0F);
        this.tuskRight2 = new ModelRenderer(this, 34, 0);
        this.tuskRight2.setRotationPoint(-1.0F, 1.5F, -6.0F);
        this.tuskRight2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(tuskRight2, -0.8726646259971648F, 0.0F, 0.0F);
        this.hornLeft1 = new ModelRenderer(this, 8, 1);
        this.hornLeft1.setRotationPoint(-3.0F, -7.5F, -3.0F);
        this.hornLeft1.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        this.setRotateAngle(hornLeft1, -0.3490658503988659F, 0.7853981633974483F, 0.0F);
        this.tuskRight1 = new ModelRenderer(this, 40, 23);
        this.tuskRight1.setRotationPoint(2.5F, -1F, -2.5F);
        this.tuskRight1.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        this.setRotateAngle(tuskRight1, 0.4363323129985824F, -0.7853981633974483F, 0.0F);
        this.hornLeft2 = new ModelRenderer(this, 30, 9);
        this.hornLeft2.setRotationPoint(-1.0F, 1.5F, -8.0F);
        this.hornLeft2.addBox(0.0F, -2.0F, -11.0F, 2, 2, 11, 0.0F);
        this.setRotateAngle(hornLeft2, -1.2217304763960306F, 0.0F, 0.0F);
        this.tuskLeft2 = new ModelRenderer(this, 34, 0);
        this.tuskLeft2.setRotationPoint(-1.0F, 1.5F, -6.0F);
        this.tuskLeft2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(tuskLeft2, -0.8726646259971648F, 0.0F, 0.0F);
        this.hornRight1 = new ModelRenderer(this, 8, 1);
        this.hornRight1.setRotationPoint(3F, -7.5F, -3.0F);
        this.hornRight1.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        this.setRotateAngle(hornRight1, -0.3490658503988659F, -0.7853981633974483F, 0.0F);
        this.tuskLeft1.addChild(this.tuskLeft2);
        this.hornLeft1.addChild(this.hornLeft2);
        this.tuskRight1.addChild(this.tuskRight2);
        this.hornRight1.addChild(this.hornRight2);
        bipedHead.addChild(hornLeft1);
        bipedHead.addChild(tuskLeft1);
        bipedHead.addChild(hornRight1);
        bipedHead.addChild(tuskRight1);
        bipedHead.addChild(helmet);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        float scale = 0.95f;
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        GL11.glScalef(scale, scale, scale);
        bipedHead.render(f5);
        GL11.glScalef(1/scale, 1/scale, 1/scale);
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
