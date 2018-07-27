package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityMMLantern;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by Josh on 7/24/2018.
 */
public class ModelMMLantern extends AdvancedModelBase {
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer bubble1;
    public AdvancedModelRenderer bubble2;
    public AdvancedModelRenderer bubble3;
    public AdvancedModelRenderer bubble4;
    public AdvancedModelRenderer bottomBits;
    public AdvancedModelRenderer stem;
    public AdvancedModelRenderer bottomBit1;
    public AdvancedModelRenderer bottomBit2;
    public AdvancedModelRenderer bottomBit3;
    public AdvancedModelRenderer bottomBit4;
    public AdvancedModelRenderer leaf1;
    public AdvancedModelRenderer leaf2;
    public AdvancedModelRenderer leaf3;
    public AdvancedModelRenderer leaf4;
    public AdvancedModelRenderer stem1;
    public AdvancedModelRenderer stem2;
    public AdvancedModelRenderer scaleController;

    private ModelAnimator animator;

    public ModelMMLantern() {
        animator = ModelAnimator.create();
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leaf1 = new AdvancedModelRenderer(this, -16, 42);
        this.leaf1.mirror = true;
        this.leaf1.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf1.addBox(-6.0F, 0.0F, -16.0F, 12, 0, 16, 0.0F);
        this.setRotateAngle(leaf1, -0.2617993877991494F, 3.141592653589793F, 0.0F);
        this.stem2 = new AdvancedModelRenderer(this, 0, 20);
        this.stem2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.stem2.addBox(0.0F, -10.0F, -5.0F, 0, 10, 10, 0.0F);
        this.setRotateAngle(stem2, 0.0F, 0.7853981633974483F, 0.0F);
        this.body = new AdvancedModelRenderer(this, 1, 0);
        this.body.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.body.addBox(-7.5F, -7.5F, -7.5F, 15, 15, 15, 0.0F);
        this.bottomBit4 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit4.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(bottomBit4, 1.0471975511965976F, 4.71238898038469F, 0.0F);
        this.leaf4 = new AdvancedModelRenderer(this, 8, 30);
        this.leaf4.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.leaf4.addBox(0.0F, 0.0F, -6.0F, 16, 0, 12, 0.0F);
        this.setRotateAngle(leaf4, 0.0F, 0.0F, -0.2617993877991494F);
        this.leaf2 = new AdvancedModelRenderer(this, 8, 30);
        this.leaf2.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.leaf2.addBox(0.0F, 0.0F, -6.0F, 16, 0, 12, 0.0F);
        this.setRotateAngle(leaf2, 0.0F, 3.141592653589793F, 0.2617993877991494F);
        this.bubble3 = new AdvancedModelRenderer(this, 0, 0);
        this.bubble3.setRotationPoint(-2.0F, 15.0F, -2.9F);
        this.bubble3.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.setRotateAngle(bubble3, -0.091106186954104F, 1.7756979809790308F, 0.40980330836826856F);
        this.bubble4 = new AdvancedModelRenderer(this, 0, 0);
        this.bubble4.setRotationPoint(3.0F, 9.2F, -2.4F);
        this.bubble4.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.setRotateAngle(bubble4, -0.7740535232594852F, 0.136659280431156F, 0.40980330836826856F);
        this.bubble1 = new AdvancedModelRenderer(this, 0, 7);
        this.bubble1.setRotationPoint(2.6F, 13.5F, 2.8F);
        this.bubble1.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(bubble1, 0.27314402793711257F, 0.6829473363053812F, 0.5462880558742251F);
        this.bottomBit3 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit3.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(bottomBit3, 1.0471975511965976F, 3.141592653589793F, 0.0F);
        this.stem1 = new AdvancedModelRenderer(this, 40, 42);
        this.stem1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stem1.addBox(-3.0F, -3.0F, -3.0F, 6, 3, 6, 0.0F);
        this.bubble2 = new AdvancedModelRenderer(this, 0, 7);
        this.bubble2.setRotationPoint(-2.8F, 8.0F, 1.8F);
        this.bubble2.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(bubble2, 1.3203415791337103F, 1.5025539530419183F, 0.5462880558742251F);
        this.bottomBit2 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit2.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(bottomBit2, 1.0471975511965976F, 1.5707963267948966F, 0.0F);
        this.bottomBit1 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit1.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(bottomBit1, 1.0471975511965976F, 0.0F, 0.0F);
        this.leaf3 = new AdvancedModelRenderer(this, -16, 42);
        this.leaf3.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.leaf3.addBox(-6.0F, 0.0F, -16.0F, 12, 0, 16, 0.0F);
        this.setRotateAngle(leaf3, -0.2617993877991494F, 0.0F, 0.0F);
        this.bottomBits = new AdvancedModelRenderer(this, 0, 0);
        this.bottomBits.setRotationPoint(0.0F, 7.5F, 0.0F);
        this.bottomBits.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(bottomBits, 0.0F, 0.7853981633974483F, 0.0F);
        this.stem = new AdvancedModelRenderer(this, 0, 0);
        this.stem.setRotationPoint(0.0F, -7.5F, 0.0F);
        this.stem.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(stem, 0.0F, 0.7853981633974483F, 0.0F);
        this.scaleController = new AdvancedModelRenderer(this, 0, 0);
        this.scaleController.setRotationPoint(1, 1, 1);
        this.stem.addChild(this.leaf1);
        this.stem1.addChild(this.stem2);
        this.bottomBits.addChild(this.bottomBit4);
        this.stem.addChild(this.leaf4);
        this.stem.addChild(this.leaf2);
        this.bottomBits.addChild(this.bottomBit3);
        this.stem.addChild(this.stem1);
        this.bottomBits.addChild(this.bottomBit2);
        this.bottomBits.addChild(this.bottomBit1);
        this.stem.addChild(this.leaf3);
        this.body.addChild(this.bottomBits);
        this.body.addChild(this.stem);
        updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.body.render(f5);
        this.bubble1.render(f5);
        this.bubble2.render(f5);
        this.bubble3.render(f5);
        this.bubble4.render(f5);
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
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        resetToDefaultPose();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        EntityMMLantern lantern = (EntityMMLantern) entity;
        animator.update(lantern);
        setRotationAngles(f, f1, f2, f3, f4, f5, lantern);

        float frame = lantern.frame + LLibrary.PROXY.getPartialTicks();

        if (lantern.getAnimation() == EntityMMLantern.PUFF_ANIMATION) {
            animator.setAnimation(EntityMMLantern.PUFF_ANIMATION);
            animator.startKeyframe(5);
            animator.move(scaleController, 0.4f, -0.4f, 0.4f);
            animator.move(body, 0f, -3f, 0f);
            animator.move(stem, 0f, 3f, 0f);
            animator.move(bottomBits, 0f, -3f, 0f);
            animator.endKeyframe();
            animator.startKeyframe(2);
            animator.move(scaleController, -0.45f, 0.6f, -0.45f);
            animator.move(body, 0f, 4f, 0f);
            animator.move(stem, 0f, -4f, 0f);
            animator.move(bottomBits, 0f, 4f, 0f);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }

        body.setScale(scaleController.rotationPointX, scaleController.rotationPointY, scaleController.rotationPointZ);
    }
}
