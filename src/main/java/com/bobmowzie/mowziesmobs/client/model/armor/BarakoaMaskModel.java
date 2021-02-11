package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class BarakoaMaskModel<T extends LivingEntity> extends ModelBipedAnimated {
    public ModelRenderer maskBase;
    public ModelRenderer maskLeft;
    public ModelRenderer maskRight;
    public ModelRenderer mane;
    public ModelRenderer maneBack;

    public BarakoaMaskModel() {
        super(0.0f);
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.maskLeft = new ModelRenderer(this, 48, 18);
        this.maskLeft.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.maskLeft.addBox(-7.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        this.setRotateAngle(maskLeft, 0.0F, 0.4363323129985824F, 0.0F);
        this.maskRight = new ModelRenderer(this, 48, 18);
        this.maskRight.mirror = true;
        this.maskRight.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.maskRight.addBox(0.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        this.setRotateAngle(maskRight, 0.0F, -0.4363323129985824F, 0.0F);
        this.maskBase = new ModelRenderer(this, 0, 0);
        this.maskBase.setRotationPoint(0.0F, -4.310181F, -8.267222F);
        this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.mane = new ModelRenderer(this, 0, 0);
        this.mane.setRotationPoint(0.0F, -2.0F, 4.0F);
        this.mane.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
        this.maneBack = new ModelRenderer(this, 0, 0);
        this.maneBack.setRotationPoint(0.0F, -2.0F, 3.999F);
        this.maneBack.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
        this.maneBack.rotateAngleY = (float) Math.PI;
        this.maskBase.addChild(this.maskLeft);
        this.maskBase.addChild(this.maskRight);
        this.maskBase.addChild(this.mane);
        this.maskBase.addChild(this.maneBack);

        this.bipedHead.addChild(maskBase);
//        this.bipedHead.cubeList.clear();
        this.bipedHeadwear.showModel = false;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.scale(0.8f, 0.8f, 0.8f);
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}
