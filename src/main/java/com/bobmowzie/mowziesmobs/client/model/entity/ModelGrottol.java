package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by Josh on 7/3/2018.
 */


public class ModelGrottol extends AdvancedModelBase {
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer crystal1;
    public AdvancedModelRenderer crystal2;
    public AdvancedModelRenderer crystal3;
    public AdvancedModelRenderer crystal4;
    public AdvancedModelRenderer crystal5;
    public AdvancedModelRenderer crystal6;
    public AdvancedModelRenderer clawLeftJoint;
    public AdvancedModelRenderer crystal7;
    public AdvancedModelRenderer clawRightJoint;
    public AdvancedModelRenderer leg1LeftJoint;
    public AdvancedModelRenderer leg2LeftJoint;
    public AdvancedModelRenderer leg3LeftJoint;
    public AdvancedModelRenderer leg1RightJoint;
    public AdvancedModelRenderer leg2RightJoint;
    public AdvancedModelRenderer leg3RightJoint;
    public AdvancedModelRenderer eyeLeft;
    public AdvancedModelRenderer eyeRight;
    public AdvancedModelRenderer clawLeftUpper;
    public AdvancedModelRenderer clawLeftLower;
    public AdvancedModelRenderer clawLeft;
    public AdvancedModelRenderer clawRightUpper;
    public AdvancedModelRenderer clawRightLower;
    public AdvancedModelRenderer clawRight;
    public AdvancedModelRenderer leg1LeftUpper;
    public AdvancedModelRenderer leg1LeftLower;
    public AdvancedModelRenderer foot1Left;
    public AdvancedModelRenderer leg2LeftUpper;
    public AdvancedModelRenderer leg2LeftLower;
    public AdvancedModelRenderer foot2Left;
    public AdvancedModelRenderer leg3LeftUpper;
    public AdvancedModelRenderer leg3LeftLower;
    public AdvancedModelRenderer foot3Left;
    public AdvancedModelRenderer leg1RightUpper;
    public AdvancedModelRenderer leg1RightLower;
    public AdvancedModelRenderer foot1Right;
    public AdvancedModelRenderer leg2RightUpper;
    public AdvancedModelRenderer leg2RightLower;
    public AdvancedModelRenderer foot2Right;
    public AdvancedModelRenderer leg3RightUpper;
    public AdvancedModelRenderer leg3RightLower;
    public AdvancedModelRenderer foot3Right;
    public AdvancedModelRenderer dieAnimController;

    private ModelAnimator animator;

    public ModelGrottol() {
        animator = ModelAnimator.create();
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.crystal7 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal7.setRotationPoint(-3.6F, -2.4F, 1.7F);
        this.crystal7.addBox(-1.5F, -5.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(crystal7, -1.3203415791337103F, 0.9105382707654417F, -1.9577358219620393F);
        this.clawRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.clawRightJoint.setRotationPoint(-2.0F, 2.0F, -3.5F);
        this.clawRightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(clawRightJoint, 0.17453292519943295F, -0.7740535232594852F, 0.0F);
        this.leg1RightUpper = new AdvancedModelRenderer(this, 0, 0);
        this.leg1RightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg1RightUpper.addBox(-3.0F, -1.0F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg1RightUpper, 0.0F, 0.0F, -0.6373942428283291F);
        this.leg1LeftUpper = new AdvancedModelRenderer(this, 0, 0);
        this.leg1LeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg1LeftUpper.addBox(0.0F, -1.0F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg1LeftUpper, 0.0F, 0.0F, 0.6373942428283291F);
        this.leg3LeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg3LeftJoint.setRotationPoint(3.5F, 2.0F, 1.5F);
        this.leg3LeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leg3LeftJoint, 0.17453292519943295F, -0.6829473363053812F, 0.0F);
        this.clawLeft = new AdvancedModelRenderer(this, 42, 0);
        this.clawLeft.setRotationPoint(3.5F, 0.0F, 0.0F);
        this.clawLeft.addBox(-1.0F, -1.5F, -5.0F, 2, 3, 6, 0.0F);
        this.setRotateAngle(clawLeft, 0.27314402793711257F, 0.27314402793711257F, -0.045553093477052F);
        this.leg3RightLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg3RightLower.setRotationPoint(-3.3F, -0.5F, 0.0F);
        this.leg3RightLower.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg3RightLower, 0.0F, 0.0F, 1.4486232791552935F);
        this.leg3RightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg3RightJoint.setRotationPoint(-3.5F, 2.0F, 1.5F);
        this.leg3RightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leg3RightJoint, 0.17453292519943295F, 0.6829473363053812F, 0.0F);
        this.clawRightUpper = new AdvancedModelRenderer(this, 0, 2);
        this.clawRightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.clawRightUpper.addBox(-3.0F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(clawRightUpper, 0.0F, 0.0F, -0.6373942428283291F);
        this.foot3Left = new AdvancedModelRenderer(this, 44, 9);
        this.foot3Left.setRotationPoint(3.2F, 0.0F, 0.0F);
        this.foot3Left.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(foot3Left, 0.0F, 0.0F, 0.45378560551852565F);
        this.crystal5 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal5.setRotationPoint(4.0F, -3.0F, -3.5F);
        this.crystal5.addBox(-1.5F, -4.5F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(crystal5, 0.5562364326105927F, 0.8134979643545569F, 1.392423677241076F);
        this.clawLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.clawLeftJoint.setRotationPoint(2.0F, 2.0F, -3.5F);
        this.clawLeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(clawLeftJoint, 0.17453292519943295F, 0.7740535232594852F, 0.0F);
        this.clawLeftLower = new AdvancedModelRenderer(this, 0, 2);
        this.clawLeftLower.setRotationPoint(3.0F, -0.5F, 0.0F);
        this.clawLeftLower.addBox(0.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(clawLeftLower, 0.0F, 0.0F, -0.9105382707654417F);
        this.crystal4 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal4.setRotationPoint(-4.5F, -3.0F, -2.0F);
        this.crystal4.addBox(-1.5F, -5.0F, -1.5F, 3, 7, 3, 0.0F);
        this.setRotateAngle(crystal4, 0.22759093446006054F, -0.31869712141416456F, -0.5462880558742251F);
        this.foot2Left = new AdvancedModelRenderer(this, 52, 9);
        this.foot2Left.setRotationPoint(3.3F, 0.0F, 0.0F);
        this.foot2Left.addBox(-1.0F, -1.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(foot2Left, 0.0F, 0.0F, 0.3543018381548489F);
        this.clawRightLower = new AdvancedModelRenderer(this, 0, 2);
        this.clawRightLower.setRotationPoint(-3.0F, -0.5F, 0.0F);
        this.clawRightLower.addBox(-4.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(clawRightLower, 0.0F, 0.0F, 0.9105382707654417F);
        this.clawLeftUpper = new AdvancedModelRenderer(this, 0, 2);
        this.clawLeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.clawLeftUpper.addBox(-1.0F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(clawLeftUpper, 0.0F, 0.0F, 0.6373942428283291F);
        this.leg1LeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg1LeftJoint.setRotationPoint(3.2F, 2.0F, -1.8F);
        this.leg1LeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leg1LeftJoint, 0.17453292519943295F, 0.45378560551852565F, 0.0F);
        this.crystal1 = new AdvancedModelRenderer(this, 12, 15);
        this.crystal1.setRotationPoint(2.0F, -4.0F, -1.0F);
        this.crystal1.addBox(-2.0F, -11.0F, -2.0F, 4, 13, 4, 0.0F);
        this.setRotateAngle(crystal1, 0.045553093477052F, -0.6829473363053812F, 0.22759093446006054F);
        this.clawRight = new AdvancedModelRenderer(this, 42, 0);
        this.clawRight.setRotationPoint(-3.5F, 0.0F, 0.0F);
        this.clawRight.addBox(-1.0F, -1.5F, -5.0F, 2, 3, 6, 0.0F);
        this.setRotateAngle(clawRight, 0.27314402793711257F, -0.27314402793711257F, 0.045553093477052F);
        this.leg2LeftUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg2LeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg2LeftUpper.addBox(-0.7F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(leg2LeftUpper, 0.0F, 0.0F, 0.5061454830783556F);
        this.leg1LeftLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg1LeftLower.setRotationPoint(3.0F, -0.5F, 0.0F);
        this.leg1LeftLower.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg1LeftLower, 0.0F, 0.0F, -1.2747884856566583F);
        this.leg2RightUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg2RightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg2RightUpper.addBox(-3.3F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(leg2RightUpper, 0.0F, 0.0F, -0.5061454830783556F);
        this.crystal3 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal3.setRotationPoint(0.0F, -3.0F, -4.8F);
        this.crystal3.addBox(-1.5F, -7.0F, -1.5F, 3, 9, 3, 0.0F);
        this.setRotateAngle(crystal3, 0.5918411493512771F, 0.31869712141416456F, 0.22759093446006054F);
        this.eyeLeft = new AdvancedModelRenderer(this, 0, 4);
        this.eyeLeft.mirror = true;
        this.eyeLeft.setRotationPoint(2.0F, -0.5F, -2.5F);
        this.eyeLeft.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(eyeLeft, -1.0471975511965976F, 1.7453292519943295F, 2.530727415391778F);
        this.head = new AdvancedModelRenderer(this, 34, 0);
        this.head.setRotationPoint(0.0F, 1.5F, -5.0F);
        this.head.addBox(-2.0F, -1.0F, -3.0F, 4, 3, 2, 0.0F);
        this.setRotateAngle(head, 0.17453292519943295F, 0.0F, 0.0F);
        this.foot1Right = new AdvancedModelRenderer(this, 44, 9);
        this.foot1Right.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.foot1Right.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(foot1Right, 0.0F, 0.0F, -0.27314402793711257F);
        this.leg2RightLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg2RightLower.setRotationPoint(-3.3F, -0.5F, 0.0F);
        this.leg2RightLower.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg2RightLower, 0.0F, 0.0F, 1.509709802975095F);
        this.foot3Right = new AdvancedModelRenderer(this, 44, 9);
        this.foot3Right.setRotationPoint(-3.2F, 0.0F, 0.0F);
        this.foot3Right.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(foot3Right, 0.0F, 0.0F, -0.45378560551852565F);
        this.crystal6 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal6.setRotationPoint(2.0F, -3.0F, 3.0F);
        this.crystal6.addBox(-1.5F, -5.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(crystal6, -0.5918411493512771F, -0.31869712141416456F, 0.5462880558742251F);
        this.foot2Right = new AdvancedModelRenderer(this, 52, 9);
        this.foot2Right.setRotationPoint(-3.3F, 0.0F, 0.0F);
        this.foot2Right.addBox(-1.0F, -1.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(foot2Right, 0.0F, 0.0F, -0.3508111796508603F);
        this.crystal2 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal2.setRotationPoint(-2.0F, -3.0F, 1.0F);
        this.crystal2.addBox(-1.5F, -10.0F, -1.5F, 3, 12, 3, 0.0F);
        this.setRotateAngle(crystal2, 0.6373942428283291F, 2.0032889154390916F, 0.40980330836826856F);
        this.body = new AdvancedModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.body.addBox(-6.0F, -3.0F, -6.0F, 12, 5, 10, 0.0F);
        this.setRotateAngle(body, -0.17453292519943295F, 0.0F, 0.0F);
        this.foot1Left = new AdvancedModelRenderer(this, 44, 9);
        this.foot1Left.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.foot1Left.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(foot1Left, 0.0F, 0.0F, 0.27314402793711257F);
        this.leg2LeftLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg2LeftLower.setRotationPoint(3.3F, -0.5F, 0.0F);
        this.leg2LeftLower.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg2LeftLower, 0.0F, 0.0F, -1.509709802975095F);
        this.leg1RightLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg1RightLower.setRotationPoint(-3.0F, -0.5F, 0.0F);
        this.leg1RightLower.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg1RightLower, 0.0F, 0.0F, 1.2747884856566583F);
        this.leg2RightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg2RightJoint.setRotationPoint(-3.8F, 2.0F, -0.1F);
        this.leg2RightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leg2RightJoint, 0.17453292519943295F, 0.04363323129985824F, 0.0F);
        this.leg3RightUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg3RightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg3RightUpper.addBox(-3.3F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(leg3RightUpper, 0.0F, 0.0F, -0.41887902047863906F);
        this.eyeRight = new AdvancedModelRenderer(this, 0, 4);
        this.eyeRight.setRotationPoint(-2.0F, -0.5F, -2.5F);
        this.eyeRight.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(eyeRight, -1.0471975511965976F, -1.7453292519943295F, -2.530727415391778F);
        this.leg3LeftUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg3LeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg3LeftUpper.addBox(-0.7F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(leg3LeftUpper, 0.0F, 0.0F, 0.41887902047863906F);
        this.leg1RightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg1RightJoint.setRotationPoint(-3.2F, 2.0F, -2.0F);
        this.leg1RightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leg1RightJoint, 0.17453292519943295F, -0.40142572795869574F, 0.0F);
        this.leg3LeftLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg3LeftLower.setRotationPoint(3.3F, -0.5F, 0.0F);
        this.leg3LeftLower.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(leg3LeftLower, 0.0F, 0.0F, -1.4486232791552935F);
        this.leg2LeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg2LeftJoint.setRotationPoint(3.8F, 2.0F, -0.1F);
        this.leg2LeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leg2LeftJoint, 0.17453292519943295F, -0.04363323129985824F, 0.0F);
        this.dieAnimController = new AdvancedModelRenderer(this, 0, 0);
        this.dieAnimController.setRotationPoint(0, 0, 0);
        this.body.addChild(this.crystal7);
        this.body.addChild(this.clawRightJoint);
        this.leg1RightJoint.addChild(this.leg1RightUpper);
        this.leg1LeftJoint.addChild(this.leg1LeftUpper);
        this.body.addChild(this.leg3LeftJoint);
        this.clawLeftLower.addChild(this.clawLeft);
        this.leg3RightUpper.addChild(this.leg3RightLower);
        this.body.addChild(this.leg3RightJoint);
        this.clawRightJoint.addChild(this.clawRightUpper);
        this.leg3LeftLower.addChild(this.foot3Left);
        this.body.addChild(this.crystal5);
        this.body.addChild(this.clawLeftJoint);
        this.clawLeftUpper.addChild(this.clawLeftLower);
        this.body.addChild(this.crystal4);
        this.leg2LeftLower.addChild(this.foot2Left);
        this.clawRightUpper.addChild(this.clawRightLower);
        this.clawLeftJoint.addChild(this.clawLeftUpper);
        this.body.addChild(this.leg1LeftJoint);
        this.body.addChild(this.crystal1);
        this.clawRightLower.addChild(this.clawRight);
        this.leg2LeftJoint.addChild(this.leg2LeftUpper);
        this.leg1LeftUpper.addChild(this.leg1LeftLower);
        this.leg2RightJoint.addChild(this.leg2RightUpper);
        this.body.addChild(this.crystal3);
        this.head.addChild(this.eyeLeft);
        this.body.addChild(this.head);
        this.leg1RightLower.addChild(this.foot1Right);
        this.leg2RightUpper.addChild(this.leg2RightLower);
        this.leg3RightLower.addChild(this.foot3Right);
        this.body.addChild(this.crystal6);
        this.leg2RightLower.addChild(this.foot2Right);
        this.body.addChild(this.crystal2);
        this.leg1LeftLower.addChild(this.foot1Left);
        this.leg2LeftUpper.addChild(this.leg2LeftLower);
        this.leg1RightUpper.addChild(this.leg1RightLower);
        this.body.addChild(this.leg2RightJoint);
        this.leg3RightJoint.addChild(this.leg3RightUpper);
        this.head.addChild(this.eyeRight);
        this.leg3LeftJoint.addChild(this.leg3LeftUpper);
        this.body.addChild(this.leg1RightJoint);
        this.leg3LeftUpper.addChild(this.leg3LeftLower);
        this.body.addChild(this.leg2LeftJoint);
        updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.body.render(f5);
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
//        f = entity.ticksExisted + LLibrary.PROXY.getPartialTicks();
//        f1 = 0.5f;

        f3 = Math.min(f3, 30f);
        f3 = Math.max(f3, -30f);
        faceTarget(f3, f4, 1, head);

        if (f1 > 0.5) f1 = 0.5f;
        float globalSpeed = 1.5f;
        float globalDegree = 0.5f;
        swing(leg1LeftJoint, globalSpeed, globalDegree * 1.2f, false, 0, 0.1f, f, f1);
        flap(leg1LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, 0.2f, f, f1);
        flap(leg1LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f, 0f, f, f1);
        flap(foot1Left, globalSpeed, globalDegree * 1.3f, false, 1.57f, 0f, f, f1);

        swing(leg2LeftJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f, 0.1f, f, f1);
        flap(leg2LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, 0.2f, f, f1);
        flap(leg2LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f, 0f, f, f1);
        flap(foot2Left, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f, 0f, f, f1);

        swing(leg3LeftJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f*2, 0.1f, f, f1);
        flap(leg3LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f*2, 0.2f, f, f1);
        flap(leg3LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f*2, 0f, f, f1);
        flap(foot3Left, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f*2, 0f, f, f1);

        swing(leg1RightJoint, globalSpeed, globalDegree * 1.2f, false, 0, -0.1f, f, f1);
        flap(leg1RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, -0.2f, f, f1);
        flap(leg1RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f, 0f, f, f1);
        flap(foot1Right, globalSpeed, globalDegree * 1.3f, false, 1.57f, 0f, f, f1);

        swing(leg2RightJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f, -0.1f, f, f1);
        flap(leg2RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, -0.2f, f, f1);
        flap(leg2RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f, 0f, f, f1);
        flap(foot2Right, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f, 0f, f, f1);

        swing(leg3RightJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f*2, -0.1f, f, f1);
        flap(leg3RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f*2, -0.2f, f, f1);
        flap(leg3RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f*2, 0f, f, f1);
        flap(foot3Right, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f*2, 0f, f, f1);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        EntityGrottol grottol = (EntityGrottol) entity;
        animator.update(grottol);
        setRotationAngles(f, f1, f2, f3, f4, f5, grottol);

        float frame = grottol.frame + LLibrary.PROXY.getPartialTicks();

        if (grottol.getAnimation() == EntityGrottol.IDLE_ANIMATION) {
            animator.setAnimation(EntityGrottol.IDLE_ANIMATION);

            animator.startKeyframe(6);//6
            animator.move(body, 1, -2, 0);
            animator.rotate(body, 0, 0, 0.1f);
            animator.rotate(head, 0, 0, 0.6f);
            animator.rotate(clawLeftUpper, 0.2f, -0.3f, 0.3f);
            animator.rotate(clawLeft, -0.3f, 0.0f, -0.3f);
            animator.rotate(clawRightUpper, 0.2f, 0.3f, -0.3f);
            animator.rotate(clawRight, -0.3f, 0.0f, 0.3f);
            animator.rotate(leg1RightUpper, 0, 0, -0.3f);
            animator.rotate(leg1RightLower, 0, 0, -0.45f);
            animator.rotate(foot1Right, 0, 0, 0.7f);
            animator.rotate(leg2RightUpper, 0, 0, -0.3f);
            animator.rotate(leg2RightLower, 0, 0, -0.35f);
            animator.rotate(foot2Right, 0, 0, 0.5f);
            animator.rotate(leg3RightUpper, 0, 0, -0.3f);
            animator.rotate(leg3RightLower, 0, 0, -0.45f);
            animator.rotate(foot3Right, 0, 0, 0.7f);
            animator.rotate(leg1LeftUpper, 0, 0, 0.2f);
            animator.rotate(leg1LeftLower, 0, 0, 0.1f);
            animator.rotate(foot1Left, 0, 0, -0.1f);
            animator.rotate(leg2LeftUpper, 0, 0, 0.1f);
            animator.rotate(leg2LeftLower, 0, 0, 0.1f);
            animator.rotate(foot2Left, 0, 0, 0f);
            animator.rotate(leg3LeftUpper, 0, 0, 0.2f);
            animator.rotate(leg3LeftLower, 0, 0, 0.1f);
            animator.rotate(foot3Left, 0, 0, -0.1f);
            animator.endKeyframe();

            animator.setStaticKeyframe(9);//15

            animator.startKeyframe(3);//19
            animator.move(body, 1, -2, 0);
            animator.rotate(body, 0, 0, 0.1f);
            animator.rotate(head, 0.5f, 0.3f, 0.4f);
            animator.rotate(clawLeftUpper, 0.2f, -0.3f, 0.3f);
            animator.rotate(clawLeft, -0.3f, 0.0f, -0.3f);
            animator.rotate(clawRightUpper, 0.2f, 0.3f, -0.3f);
            animator.rotate(clawRight, -0.3f, 0.0f, 0.3f);
            animator.rotate(leg1RightUpper, 0, 0, -0.3f);
            animator.rotate(leg1RightLower, 0, 0, -0.45f);
            animator.rotate(foot1Right, 0, 0, 0.7f);
            animator.rotate(leg2RightUpper, 0, 0, -0.3f);
            animator.rotate(leg2RightLower, 0, 0, -0.35f);
            animator.rotate(foot2Right, 0, 0, 0.5f);
            animator.rotate(leg3RightUpper, 0, 0, -0.3f);
            animator.rotate(leg3RightLower, 0, 0, -0.45f);
            animator.rotate(foot3Right, 0, 0, 0.7f);
            animator.rotate(leg1LeftUpper, 0, 0, 0.2f);
            animator.rotate(leg1LeftLower, 0, 0, 0.1f);
            animator.rotate(foot1Left, 0, 0, -0.1f);
            animator.rotate(leg2LeftUpper, 0, 0, 0.1f);
            animator.rotate(leg2LeftLower, 0, 0, 0.1f);
            animator.rotate(foot2Left, 0, 0, 0f);
            animator.rotate(leg3LeftUpper, 0, 0, 0.2f);
            animator.rotate(leg3LeftLower, 0, 0, 0.1f);
            animator.rotate(foot3Left, 0, 0, -0.1f);
            animator.endKeyframe();

            animator.setStaticKeyframe(3);//22

            animator.startKeyframe(4);//26
            animator.rotate(clawRightUpper, 0, 0.2f, 0.4f);
            animator.rotate(clawRightLower, 0, 0, 0.4f);
            animator.rotate(clawRight, 0.5f, 0.3f, -0.4f);
            animator.rotate(clawLeftUpper, -0.15f, -0.15f, -0.1f);
            animator.rotate(head, 0.4f, 0.2f, 0.4f);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);//28

            animator.startKeyframe(1);//29
            animator.rotate(clawRightUpper, 0, 0.2f, -0.05f);
            animator.rotate(clawRightLower, 0, 0, -0.05f);
            animator.rotate(clawRight, 0.5f, 0.3f, -0.4f);
            animator.rotate(clawLeftUpper, -0.15f, -0.15f, -0.1f);
            animator.rotate(head, 0.4f, 0.2f, 0.4f);
            animator.endKeyframe();

            animator.setStaticKeyframe(1);//30

            animator.startKeyframe(2);//32
            animator.rotate(clawRightUpper, 0, 0.2f, 0.4f);
            animator.rotate(clawRightLower, 0, 0, 0.4f);
            animator.rotate(clawRight, 0.5f, 0.3f, -0.4f);
            animator.rotate(clawLeftUpper, -0.15f, -0.15f, -0.1f);
            animator.rotate(head, 0.4f, 0.2f, 0.4f);
            animator.endKeyframe();

            animator.setStaticKeyframe(1);//33

            animator.startKeyframe(1);//34
            animator.rotate(clawRightUpper, 0, 0.2f, -0.05f);
            animator.rotate(clawRightLower, 0, 0, -0.05f);
            animator.rotate(clawRight, 0.5f, 0.3f, -0.4f);
            animator.rotate(clawLeftUpper, -0.15f, -0.15f, -0.1f);
            animator.rotate(head, 0.4f, 0.2f, 0.4f);
            animator.endKeyframe();

            animator.setStaticKeyframe(1);

            animator.startKeyframe(2);
            animator.rotate(clawRightUpper, 0, 0.2f, 0.4f);
            animator.rotate(clawRightLower, 0, 0, 0.4f);
            animator.rotate(clawRight, 0.5f, 0.3f, -0.4f);
            animator.rotate(clawLeftUpper, -0.15f, -0.15f, -0.1f);
            animator.rotate(head, 0.4f, 0.2f, 0.4f);
            animator.endKeyframe();

            animator.setStaticKeyframe(7);

            animator.resetKeyframe(4);
        }

        if (grottol.getAnimation() == EntityGrottol.DIE_ANIMATION) {
            animator.setAnimation(EntityGrottol.DIE_ANIMATION);

            animator.startKeyframe(7);
            animator.rotate(body, -3.5f, 0, 0);
            animator.rotate(head, 1f, 0, 0);
            animator.move(dieAnimController, 1, 1, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);
            animator.rotate(body, -3.5f, 0, 0);
            animator.move(body, 0, -5, 0);
            animator.rotate(head, 1f, 0, 0);
            animator.move(dieAnimController, 1, 1, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);
            animator.rotate(body, -3.5f, 0, 0);
            animator.rotate(head, 1f, 0, 0);
            animator.move(dieAnimController, 1, 1, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(18);

            animator.startKeyframe(12);
            animator.rotate(body, -3.5f, 0, 0);
            animator.rotate(head, 1f, 0, 0);
            animator.move(dieAnimController, 1, 0, 0);
            animator.rotate(leg1LeftUpper, 0, 0, 0.7f);
            animator.rotate(leg2LeftUpper, 0, 0, 0.7f);
            animator.rotate(leg3LeftUpper, 0, 0, 0.7f);
            animator.rotate(leg1RightUpper, 0, 0, -0.7f);
            animator.rotate(leg2RightUpper, 0, 0, -0.7f);
            animator.rotate(leg3RightUpper, 0, 0, -0.7f);
            animator.rotate(clawLeftUpper, 0, 0, 0.7f);
            animator.rotate(clawRightUpper, 0, 0, -0.7f);
            animator.rotate(clawLeft, 1, 0, 1);
            animator.rotate(clawRight, 1, 0, -1);
            animator.endKeyframe();

            animator.setStaticKeyframe(12);

            body.rotationPointY -= 18 * (-4 * dieAnimController.rotationPointX * dieAnimController.rotationPointX + 4 * dieAnimController.rotationPointX);
            float globalSpeed = 1f;
            float globalDegree = 0.5f * dieAnimController.rotationPointY;
            flap(body, globalSpeed, globalDegree * 0.2f, true, 0, 0, frame, 1);
            body.rotationPointX -= 1 * globalDegree * Math.cos(frame * globalSpeed);
            flap(leg1LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, -0.9f, frame, 1);
            flap(leg1LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 0.5f, 0.4f, frame, 1);
            flap(foot1Left, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1f, -0.1f, frame, 1);

            flap(leg2LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, -0.9f, frame, 1);
            flap(leg2LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f + 0.5f, 0.4f, frame, 1);
            flap(foot2Left, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f + 1f, -0.1f, frame, 1);

            flap(leg3LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f*2, -0.9f, frame, 1);
            flap(leg3LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f*2 + 0.5f, 0.4f, frame, 1);
            flap(foot3Left, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f*2 + 1f, -0.1f, frame, 1);

            flap(leg1RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, 0.9f, frame, 1);
            flap(leg1RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 0.5f, -0.4f, frame, 1);
            flap(foot1Right, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1f, 0.1f, frame, 1);

            flap(leg2RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, 0.9f, frame, 1);
            flap(leg2RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f + 0.5f, -0.4f, frame, 1);
            flap(foot2Right, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f + 1f, 0.1f, frame, 1);

            flap(leg3RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f*2, 0.9f, frame, 1);
            flap(leg3RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f*2 + 0.5f, -0.4f, frame, 1);
            flap(foot3Right, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f*2 + 1f, 0.1f, frame, 1);

            flap(clawLeftUpper, globalSpeed, globalDegree * 0.5f, true, 0, -0.3f, frame, 1);
            flap(clawLeftLower, globalSpeed, globalDegree * 0.5f, true, 0.5f, 0, frame, 1);

            flap(clawRightUpper, globalSpeed, globalDegree * 0.5f, true, 0, 0.3f, frame, 1);
            flap(clawRightLower, globalSpeed, globalDegree * 0.5f, true, 0.5f, 0, frame, 1);
        }

        if (grottol.getAnimation() == EntityGrottol.BURROW_ANIMATION) {
            animator.setAnimation(EntityGrottol.BURROW_ANIMATION);

            animator.startKeyframe(4);
            animator.rotate(body, 0.2f, 0.4f, 0);
            animator.move(body, 0, 1f, 0);
            animator.rotate(clawLeftJoint, 0, 0.4f, 0);
            animator.rotate(clawLeftUpper, 0, 0, -0.2f);
            animator.rotate(clawLeftLower, 0, 0, 0.7f);
            animator.rotate(clawLeft, 0.7f, 0, -1);
            animator.rotate(clawRightJoint, 0, 0.2f, 0);
            animator.rotate(clawRightUpper, 0, 0, 1.3f);
            animator.rotate(clawRightLower, 0, 0, -0.7f);
            animator.rotate(clawRight, 0.3f, 0, 1);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(body, 0.4f, -0.4f, 0);
            animator.move(body, 0, 3f, 0);
            animator.rotate(clawRightJoint, 0, -0.4f, 0);
            animator.rotate(clawRightUpper, 0, 0, 0.2f);
            animator.rotate(clawRightLower, 0, 0, -0.7f);
            animator.rotate(clawRight, 0.7f, 0, 1);
            animator.rotate(clawLeftJoint, 0, -0.2f, 0);
            animator.rotate(clawLeftUpper, 0, 0, -1.3f);
            animator.rotate(clawLeftLower, 0, 0, 0.7f);
            animator.rotate(clawLeft, 0.3f, 0, -1);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(body, 0.6f, 0.4f, 0);
            animator.move(body, 0, 7f, 0);
            animator.rotate(clawLeftJoint, 0, 0.4f, 0);
            animator.rotate(clawLeftUpper, 0, 0, -0.2f);
            animator.rotate(clawLeftLower, 0, 0, 0.7f);
            animator.rotate(clawLeft, 0.7f, 0, -1);
            animator.rotate(clawRightJoint, 0, 0.2f, 0);
            animator.rotate(clawRightUpper, 0, 0, 1.3f);
            animator.rotate(clawRightLower, 0, 0, -0.7f);
            animator.rotate(clawRight, 0.3f, 0, 1);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(body, 0.6f, -0.4f, 0);
            animator.move(body, 0, 13f, 0);
            animator.rotate(clawRightJoint, 0, -0.4f, 0);
            animator.rotate(clawRightUpper, 0, 0, 0.2f);
            animator.rotate(clawRightLower, 0, 0, -0.7f);
            animator.rotate(clawRight, 0.7f, 0, 1);
            animator.rotate(clawLeftJoint, 0, -0.2f, 0);
            animator.rotate(clawLeftUpper, 0, 0, -1.3f);
            animator.rotate(clawLeftLower, 0, 0, 0.7f);
            animator.rotate(clawLeft, 0.3f, 0, -1);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(body, 0.6f, 0.4f, 0);
            animator.move(body, 0, 21.6f, 0);
            animator.rotate(clawLeftJoint, 0, 0.4f, 0);
            animator.rotate(clawLeftUpper, 0, 0, -0.2f);
            animator.rotate(clawLeftLower, 0, 0, 0.7f);
            animator.rotate(clawLeft, 0.7f, 0, -1);
            animator.rotate(clawRightJoint, 0, 0.2f, 0);
            animator.rotate(clawRightUpper, 0, 0, 1.3f);
            animator.rotate(clawRightLower, 0, 0, -0.7f);
            animator.rotate(clawRight, 0.3f, 0, 1);
            animator.endKeyframe();

            animator.setStaticKeyframe(40);
        }
    }
}
