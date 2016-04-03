package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.client.model.tools.MowzieModelRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Created by jnad325 on 11/1/15.
 */
public class ModelBarakoaMask extends ModelBiped {
    public MowzieModelRenderer maskBase;
    public MowzieModelRenderer maskLeft;
    public MowzieModelRenderer maskRight;
    public MowzieModelRenderer mane;
    public MowzieModelRenderer joint;

    public ModelBarakoaMask() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.maskLeft = new MowzieModelRenderer(this, 48, 18);
        this.maskLeft.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.maskLeft.addBox(-7.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        this.setRotateAngle(maskLeft, 0.0F, 0.4363323129985824F, 0.0F);
        this.maskBase = new MowzieModelRenderer(this, 0, 0);
        this.maskBase.setRotationPoint(0.0F, -5.0F, -8.0F);
        this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.maskRight = new MowzieModelRenderer(this, 48, 18);
        this.maskRight.mirror = true;
        this.maskRight.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.maskRight.addBox(0.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        this.setRotateAngle(maskRight, 0.0F, -0.4363323129985824F, 0.0F);
        this.mane = new MowzieModelRenderer(this, 0, 0);
        this.mane.setRotationPoint(0.0F, -2.0F, 4.0F);
        this.mane.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
        setRotateAngle(mane, 0, (float) Math.PI, 0);
        this.joint = new MowzieModelRenderer(this, 0, 0);
        this.joint.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.joint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.maskBase.addChild(this.maskLeft);
        this.maskBase.addChild(this.maskRight);
        this.maskBase.addChild(this.mane);
        joint.addChild(maskBase);
        bipedHead.addChild(joint);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        float scale = 0.8f;
        joint.rotateAngleX = (float) (Math.PI / 180 * f4);
        joint.rotateAngleY = (float) (Math.PI / 180 * f3);
        GL11.glScalef(scale, scale, scale);
        joint.render(f5);
        GL11.glScalef(1 / scale, 1 / scale, 1 / scale);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}