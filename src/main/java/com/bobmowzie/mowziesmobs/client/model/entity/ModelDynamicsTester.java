package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.bobmowzie.mowziesmobs.client.model.tools.SocketModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by Josh on 8/30/2018.
 */
public class ModelDynamicsTester<T extends EntityDynamicsTester> extends AdvancedModelBase<T> {
    public AdvancedModelRenderer root;
    public SocketModelRenderer body1;
    public SocketModelRenderer body2;
    public SocketModelRenderer body3;
    public SocketModelRenderer body4;
    public SocketModelRenderer body5;
    public SocketModelRenderer body6;

    public SocketModelRenderer[] body;
    public SocketModelRenderer[] bodydynamic;

    private MMModelAnimator animator;

    public ModelDynamicsTester() {
        animator = MMModelAnimator.create();
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.root = new AdvancedModelRenderer(this, 1, 0);
        this.root.setRotationPoint(0F, 0.0F, -16F);
        this.root.addBox(-8F, -8F, -8F, 16, 16, 16, 0.0F);
        this.body1 = new SocketModelRenderer(this, 1, 0);
        this.body1.setRotationPoint(0F, 0.0F, 0.0F);
        this.body1.addBox(-5F, -5F, 0F, 10, 10, 8, 0.0F);
        this.body2 = new SocketModelRenderer(this, 1, 0);
        this.body2.setRotationPoint(0F, 0.0F, 8.0F);
        this.body2.addBox(-4F, -4F, 0F, 8, 8, 8, 0.0F);
        this.body3 = new SocketModelRenderer(this, 1, 0);
        this.body3.setRotationPoint(0F, 0.0F, 8.0F);
        this.body3.addBox(-3F, -3F, 0F, 6, 6, 8, 0.0F);
        this.body4 = new SocketModelRenderer(this, 1, 0);
        this.body4.setRotationPoint(0F, 0.0F, 8.0F);
        this.body4.addBox(-2F, -2F, 0F, 4, 4, 8, 0.0F);
        this.body5 = new SocketModelRenderer(this, 1, 0);
        this.body5.setRotationPoint(0F, 0.0F, 8.0F);
        this.body5.addBox(-1F, -1F, 0F, 2, 2, 8, 0.0F);
        this.body6 = new SocketModelRenderer(this, 1, 0);
        this.body6.setRotationPoint(0F, 0.0F, 8.0F);
        updateDefaultPose();

        root.addChild(body1);
        body1.addChild(body2);
        body2.addChild(body3);
        body3.addChild(body4);
        body4.addChild(body5);
        body5.addChild(body6);

        body = new SocketModelRenderer[]{
                body1,
                body2,
                body3,
                body4,
                body5,
                body6
        };
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        body1.isHidden = true;
        body2.isHidden = true;
        body3.isHidden = true;
        body4.isHidden = true;
        body5.isHidden = true;
        body6.isHidden = true;
        if (entity.dc != null) entity.dc.render(f5, bodydynamic);
        root.render(f5);
        body1.isHidden = false;
        body2.isHidden = false;
        body3.isHidden = false;
        body4.isHidden = false;
        body5.isHidden = false;
//        body1.render(f5);
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
    public void setRotationAngles(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        resetToDefaultPose();
//        bob(body1, 0.3f, 16, false, entity.ticksExisted + LLibrary.PROXY.getPartialTicks(), 1F);
        root.rotationPointZ += 16;
    }
}
