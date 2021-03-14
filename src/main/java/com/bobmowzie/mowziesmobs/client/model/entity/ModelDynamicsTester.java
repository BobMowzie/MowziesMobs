package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class ModelDynamicsTester<T extends EntityDynamicsTester> extends AdvancedModelBase<T> {
    public AdvancedModelRenderer root;
    public AdvancedModelRenderer body1;
    public AdvancedModelRenderer body2;
    public AdvancedModelRenderer body3;
    public AdvancedModelRenderer body4;
    public AdvancedModelRenderer body5;
    public AdvancedModelRenderer body6;

    public AdvancedModelRenderer[] body;
    public AdvancedModelRenderer[] bodydynamic;

    private MMModelAnimator animator;

    public ModelDynamicsTester() {
        animator = MMModelAnimator.create();
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.root = new AdvancedModelRenderer(this, 1, 0);
        this.root.setRotationPoint(0F, 0.0F, -16F);
        this.root.addBox(-8F, -8F, -8F, 16, 16, 16, 0.0F);
        this.body1 = new AdvancedModelRenderer(this, 1, 0);
        this.body1.setRotationPoint(0F, 0.0F, 0.0F);
        this.body1.addBox(-5F, -5F, 0F, 10, 10, 8, 0.0F);
        this.body2 = new AdvancedModelRenderer(this, 1, 0);
        this.body2.setRotationPoint(0F, 0.0F, 8.0F);
        this.body2.addBox(-4F, -4F, 0F, 8, 8, 8, 0.0F);
        this.body3 = new AdvancedModelRenderer(this, 1, 0);
        this.body3.setRotationPoint(0F, 0.0F, 8.0F);
        this.body3.addBox(-3F, -3F, 0F, 6, 6, 8, 0.0F);
        this.body4 = new AdvancedModelRenderer(this, 1, 0);
        this.body4.setRotationPoint(0F, 0.0F, 8.0F);
        this.body4.addBox(-2F, -2F, 0F, 4, 4, 8, 0.0F);
        this.body5 = new AdvancedModelRenderer(this, 1, 0);
        this.body5.setRotationPoint(0F, 0.0F, 8.0F);
        this.body5.addBox(-1F, -1F, 0F, 2, 2, 8, 0.0F);
        this.body6 = new AdvancedModelRenderer(this, 1, 0);
        this.body6.setRotationPoint(0F, 0.0F, 8.0F);
        updateDefaultPose();

        root.addChild(body1);
        body1.addChild(body2);
        body2.addChild(body3);
        body3.addChild(body4);
        body4.addChild(body5);
        body5.addChild(body6);

        body = new AdvancedModelRenderer[]{
                body1,
                body2,
                body3,
                body4,
                body5,
                body6
        };
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        body1.showModel = false;
        body2.showModel = false;
        body3.showModel = false;
        body4.showModel = false;
        body5.showModel = false;
        body6.showModel = false;
//        if (entity.dc != null) entity.dc.render(f5, bodydynamic);
        root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
//        bob(body1, 0.3f, 16, false, entity.ticksExisted + LLibrary.PROXY.getPartialTicks(), 1F);
        root.rotationPointZ += 16;
    }
}
