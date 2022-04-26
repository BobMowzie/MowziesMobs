package com.bobmowzie.mowziesmobs.client.model.armor;

import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class BarakoaMaskModel<T extends LivingEntity> extends HumanoidModel<T> {
    public AdvancedModelRenderer maskBase;
    public ModelPart maskLeft;
    public ModelPart maskRight;
    public ModelPart mane;
    public ModelPart maneBack;

    public BarakoaMaskModel() {
        super(0.0f);
        this.texWidth = 128;
        this.texHeight = 64;
        head.cubes.clear();
        this.maskLeft = new ModelPart(this, 48, 18);
        this.maskLeft.setPos(0.0F, 0.0F, -1.0F);
        this.maskLeft.addBox(-7.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        this.setRotateAngle(maskLeft, 0.0F, 0.4363323129985824F, 0.0F);
        this.maskRight = new ModelPart(this, 48, 18);
        this.maskRight.mirror = true;
        this.maskRight.setPos(0.0F, 0.0F, -1.0F);
        this.maskRight.addBox(0.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
        this.setRotateAngle(maskRight, 0.0F, -0.4363323129985824F, 0.0F);
        this.maskBase = new AdvancedModelRenderer(this, 0, 0);
        this.maskBase.setPos(0.0F, -3.310181F, -8.267222F);
        this.maskBase.setScale(0.8f);
        this.mane = new ModelPart(this, 0, 0);
        this.mane.setPos(0.0F, -2.0F, 4.0F);
        this.mane.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
        this.maneBack = new ModelPart(this, 0, 0);
        this.maneBack.setPos(0.0F, -2.0F, 3.999F);
        this.maneBack.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
        this.maneBack.setYRot((float) Math.PI);
        this.maskBase.addChild(this.maskLeft);
        this.maskBase.addChild(this.maskRight);
        this.maskBase.addChild(this.mane);
        this.maskBase.addChild(this.maneBack);

        this.head.addChild(maskBase);
        this.hat.visible = false;
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
