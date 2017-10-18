package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBarakoa extends AdvancedModelBase {
	public AdvancedModelRenderer modelCore;
	public AdvancedModelRenderer body;
	public AdvancedModelRenderer chest;
	public AdvancedModelRenderer thighLeft;
	public AdvancedModelRenderer thighRight;
	public AdvancedModelRenderer loinClothFront;
	public AdvancedModelRenderer loinClothBack;
	public AdvancedModelRenderer armRightJoint;
	public AdvancedModelRenderer armLeftJoint;
	public AdvancedModelRenderer neckJoint;
	public AdvancedModelRenderer armUpperRight;
	public AdvancedModelRenderer armLowerRight;
	public AdvancedModelRenderer handRight;
	public AdvancedModelRenderer spearBase;
	public AdvancedModelRenderer spear;
	public AdvancedModelRenderer blowgun;
	public AdvancedModelRenderer armUpperLeft;
	public AdvancedModelRenderer armLowerLeft;
	public AdvancedModelRenderer handLeft;
	public AdvancedModelRenderer shieldBase;
	public AdvancedModelRenderer shield;
	public AdvancedModelRenderer neck;
	public AdvancedModelRenderer headJoint;
	public AdvancedModelRenderer head;
	public AdvancedModelRenderer maskBase;
	public AdvancedModelRenderer earLeft;
	public AdvancedModelRenderer earRight;
	public AdvancedModelRenderer maskLeft;
	public AdvancedModelRenderer maskRight;
	public AdvancedModelRenderer mane;
	public AdvancedModelRenderer earringLeft;
	public AdvancedModelRenderer earringRight;
	public AdvancedModelRenderer calfLeft;
	public AdvancedModelRenderer footLeft;
	public AdvancedModelRenderer calfRight;
	public AdvancedModelRenderer footRight;
	public AdvancedModelRenderer thighLeftJoint;
	public AdvancedModelRenderer thighRightJoint;
	public AdvancedModelRenderer scaler;
	public AdvancedModelRenderer flailer;
	public AdvancedModelRenderer talker;

	private ModelAnimator animator;

	public ModelBarakoa() {
		animator = ModelAnimator.create();
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.footLeft = new AdvancedModelRenderer(this, 21, 53);
		this.footLeft.setRotationPoint(0.0F, 5.5F, 0.0F);
		this.footLeft.addBox(-2.5F, 0.5F, -7.0F, 5, 2, 9, 0.0F);
		this.setRotateAngle(footLeft, -0.5235987755982988F, 0.0F, 0.0F);
		this.thighLeft = new AdvancedModelRenderer(this, 41, 52);
		this.thighLeft.mirror = true;
		this.thighLeft.setRotationPoint(0, 0, 0);
		this.thighLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(thighLeft, -0.7853981633974483F, -0.6981317007977318F, 0.0F);
		this.thighLeftJoint = new AdvancedModelRenderer(this, 41, 52);
		this.thighLeftJoint.mirror = true;
		this.thighLeftJoint.setRotationPoint(4.0F, 0.0F, 0.0F);
		this.thighLeftJoint.addBox(0, 0, 0, 0, 0, 0, 0.0F);
		this.setRotateAngle(thighLeftJoint, 0, 0, 0.0F);
		this.spearBase = new AdvancedModelRenderer(this, 0, 0);
		this.spearBase.setRotationPoint(0.0F, 3.0F, 0.0F);
		this.spearBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.loinClothBack = new AdvancedModelRenderer(this, 32, 27);
		this.loinClothBack.setRotationPoint(0.0F, 2.0F, 2.0F);
		this.loinClothBack.addBox(-4.0F, 0.0F, 0.0F, 8, 7, 0, 0.0F);
		this.setRotateAngle(loinClothBack, 0.17453292519943295F, 0.0F, 0.0F);
		this.loinClothFront = new AdvancedModelRenderer(this, 32, 27);
		this.loinClothFront.setRotationPoint(0.0F, 2.0F, -6.0F);
		this.loinClothFront.addBox(-4.0F, 0.0F, 0.0F, 8, 7, 0, 0.0F);
		this.setRotateAngle(loinClothFront, -0.17453292519943295F, 0.0F, 0.0F);
		this.thighRight = new AdvancedModelRenderer(this, 41, 52);
		this.thighRight.setRotationPoint(0, 0, 0);
		this.thighRight.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(thighRight, -0.7853981633974483F, 0.6981317007977318F, 0.0F);
		this.thighRightJoint = new AdvancedModelRenderer(this, 41, 52);
		this.thighRightJoint.setRotationPoint(-4.0F, 0.0F, 0.0F);
		this.thighRightJoint.addBox(0, 0, 0, 0, 0, 0, 0.0F);
		this.setRotateAngle(thighRightJoint, 0, 0, 0);
		this.armLowerLeft = new AdvancedModelRenderer(this, 12, 55);
		this.armLowerLeft.mirror = true;
		this.armLowerLeft.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.armLowerLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
		this.setRotateAngle(armLowerLeft, -1.0471975511965976F, 0.0F, 0.0F);
		this.armLowerRight = new AdvancedModelRenderer(this, 12, 55);
		this.armLowerRight.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.armLowerRight.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
		this.setRotateAngle(armLowerRight, -1.0471975511965976F, 0.0F, 0.0F);
		this.earringRight = new AdvancedModelRenderer(this, 0, 29);
		this.earringRight.setRotationPoint(-3.0F, 4.0F, 0.5F);
		this.earringRight.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 0, 0.0F);
		this.neckJoint = new AdvancedModelRenderer(this, 0, 0);
		this.neckJoint.setRotationPoint(0.0F, -4.0F, 1.0F);
		this.neckJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.setRotateAngle(neckJoint, 0.3490658503988659F, 0.0F, 0.0F);
		this.armUpperLeft = new AdvancedModelRenderer(this, 41, 52);
		this.armUpperLeft.mirror = true;
		this.armUpperLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.armUpperLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(armUpperLeft, 0.0F, 0.0F, -0.9599310885968813F);
		this.spear = new AdvancedModelRenderer(this, 66, 0);
		this.spear.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.spear.add3DTexture(-4, -4, 0.5F, 15, 15);
		this.setRotateAngle(spear, 2.356194490192345F, 0.0F, 3.141592653589793F);
		this.earLeft = new AdvancedModelRenderer(this, 48, 0);
		this.earLeft.setRotationPoint(4.0F, -4.0F, -3.0F);
		this.earLeft.addBox(0.0F, -2.0F, 0.0F, 4, 6, 1, 0.0F);
		this.setRotateAngle(earLeft, 0.0F, -0.3490658503988659F, 0.0F);
		this.maskLeft = new AdvancedModelRenderer(this, 48, 18);
		this.maskLeft.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.maskLeft.addBox(-7.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
		this.setRotateAngle(maskLeft, 0.0F, 0.4363323129985824F, 0.0F);
		this.calfLeft = new AdvancedModelRenderer(this, 12, 55);
		this.calfLeft.mirror = true;
		this.calfLeft.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.calfLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
		this.setRotateAngle(calfLeft, 1.3089969389957472F, 0.0F, 0.0F);
		this.handLeft = new AdvancedModelRenderer(this, 0, 55);
		this.handLeft.mirror = true;
		this.handLeft.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.handLeft.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 4, 0.0F);
		this.setRotateAngle(handLeft, 0.0F, 0.0F, 0.7853981633974483F);
		this.calfRight = new AdvancedModelRenderer(this, 12, 55);
		this.calfRight.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.calfRight.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
		this.setRotateAngle(calfRight, 1.3089969389957472F, 0.0F, 0.0F);
		this.shield = new AdvancedModelRenderer(this, 66, 40);
		this.shield.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shield.addBox(1.0F, -6.0F, -6.0F, 2, 12, 12, 0.0F);
		this.setRotateAngle(shield, 0.7853981633974483F, 0.0F, 0.0F);
		this.handRight = new AdvancedModelRenderer(this, 0, 55);
		this.handRight.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.handRight.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 4, 0.0F);
		this.setRotateAngle(handRight, 0.0F, 0.0F, -0.7853981633974483F);
		this.maskBase = new AdvancedModelRenderer(this, 0, 0);
		this.maskBase.setRotationPoint(0.0F, -3.0F, -8.0F);
		this.maskBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.neck = new AdvancedModelRenderer(this, 49, 52);
		this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.neck.addBox(-1.5F, -9.0F, -1.5F, 3, 9, 3, 0.0F);
		this.setRotateAngle(neck, 0.3665191429188092F, 0.0F, 0.0F);
		this.modelCore = new AdvancedModelRenderer(this, 0, 0);
		this.modelCore.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.modelCore.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.head = new AdvancedModelRenderer(this, 34, 34);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -7.0F, -7.0F, 8, 8, 8, 0.0F);
		this.footRight = new AdvancedModelRenderer(this, 21, 53);
		this.footRight.mirror = true;
		this.footRight.setRotationPoint(0.0F, 5.5F, 0.0F);
		this.footRight.addBox(-2.5F, 0.5F, -7.0F, 5, 2, 9, 0.0F);
		this.setRotateAngle(footRight, -0.5235987755982988F, 0.0F, 0.0F);
		this.armRightJoint = new AdvancedModelRenderer(this, 0, 0);
		this.armRightJoint.setRotationPoint(-3.5F, -4.5F, 0.0F);
		this.armRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.setRotateAngle(armRightJoint, 0.3490658503988659F, 0.3490658503988659F, 0.0F);
		this.earRight = new AdvancedModelRenderer(this, 48, 0);
		this.earRight.mirror = true;
		this.earRight.setRotationPoint(-4.0F, -4.0F, -3.0F);
		this.earRight.addBox(-4.0F, -2.0F, 0.0F, 4, 6, 1, 0.0F);
		this.setRotateAngle(earRight, 0.0F, 0.3490658503988659F, 0.0F);
		this.shieldBase = new AdvancedModelRenderer(this, 0, 0);
		this.shieldBase.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.shieldBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.armLeftJoint = new AdvancedModelRenderer(this, 0, 0);
		this.armLeftJoint.setRotationPoint(3.5F, -4.5F, 0.0F);
		this.armLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.setRotateAngle(armLeftJoint, 0.3490658503988659F, -0.3490658503988659F, 0.0F);
		this.earringLeft = new AdvancedModelRenderer(this, 0, 29);
		this.earringLeft.setRotationPoint(3.0F, 4.0F, 0.5F);
		this.earringLeft.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 0, 0.0F);
		this.armUpperRight = new AdvancedModelRenderer(this, 41, 52);
		this.armUpperRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.armUpperRight.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(armUpperRight, 0.0F, 0.0F, 0.9599310885968813F);
		this.headJoint = new AdvancedModelRenderer(this, 0, 0);
		this.headJoint.setRotationPoint(0.0F, -7.0F, 1.0F);
		this.headJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.setRotateAngle(headJoint, -0.3665191429188092F, 0.0F, 0.0F);
		this.chest = new AdvancedModelRenderer(this, 0, 42);
		this.chest.setRotationPoint(0.0F, -4.0F, -3.0F);
		this.chest.addBox(-4.0F, -5.0F, -3.5F, 8, 5, 7, 0.0F);
		this.setRotateAngle(chest, -0.3490658503988659F, 0.0F, 0.0F);
		this.body = new AdvancedModelRenderer(this, 0, 24);
		this.body.setRotationPoint(0.0F, -13.0F, 2.0F);
		this.body.addBox(-5.5F, -6.0F, -8.0F, 11, 8, 10, 0.0F);
		this.maskRight = new AdvancedModelRenderer(this, 48, 18);
		this.maskRight.mirror = true;
		this.maskRight.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.maskRight.addBox(0.0F, -8.0F, 0.0F, 7, 14, 2, 0.0F);
		this.setRotateAngle(maskRight, 0.0F, -0.4363323129985824F, 0.0F);
		this.mane = new AdvancedModelRenderer(this, 0, 0);
		this.mane.setRotationPoint(0.0F, -2.0F, 4.0F);
		this.mane.addBox(-12.0F, -12.0F, 0.0F, 24, 24, 0, 0.0F);
		setRotateAngle(mane, 0, (float) Math.PI, 0);
		this.scaler = new AdvancedModelRenderer(this, 0, 0);
		this.scaler.setRotationPoint(0.0F, 0, 0F);
		this.flailer = new AdvancedModelRenderer(this, 0, 0);
		this.flailer.setRotationPoint(0.0F, 0, 0F);
		this.blowgun = new AdvancedModelRenderer(this, 82, 0);
		this.blowgun.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.blowgun.add3DTexture(-4, -4, 0.5F, 15, 15);
		this.setRotateAngle(blowgun, 2.356194490192345F, 0.0F, 0);
		this.talker = new AdvancedModelRenderer(this, 0, 0);
		this.talker.setRotationPoint(0, 0, 0);
		this.calfLeft.addChild(this.footLeft);
		this.body.addChild(this.thighLeftJoint);
		this.handRight.addChild(this.spearBase);
		this.body.addChild(this.loinClothBack);
		this.body.addChild(this.loinClothFront);
		this.body.addChild(this.thighRightJoint);
		thighRightJoint.addChild(thighRight);
		thighLeftJoint.addChild(thighLeft);
		this.armUpperLeft.addChild(this.armLowerLeft);
		this.armUpperRight.addChild(this.armLowerRight);
		this.earRight.addChild(this.earringRight);
		this.chest.addChild(this.neckJoint);
		this.armLeftJoint.addChild(this.armUpperLeft);
		this.spearBase.addChild(this.spear);
		this.spearBase.addChild(this.blowgun);
		this.head.addChild(this.earLeft);
		this.maskBase.addChild(this.maskLeft);
		this.thighLeft.addChild(this.calfLeft);
		this.armLowerLeft.addChild(this.handLeft);
		this.thighRight.addChild(this.calfRight);
		this.shieldBase.addChild(this.shield);
		this.armLowerRight.addChild(this.handRight);
		this.head.addChild(this.maskBase);
		this.neckJoint.addChild(this.neck);
		this.headJoint.addChild(this.head);
		this.calfRight.addChild(this.footRight);
		this.chest.addChild(this.armRightJoint);
		this.head.addChild(this.earRight);
		this.armLowerLeft.addChild(this.shieldBase);
		this.chest.addChild(this.armLeftJoint);
		this.earLeft.addChild(this.earringLeft);
		this.armRightJoint.addChild(this.armUpperRight);
		this.neck.addChild(this.headJoint);
		this.body.addChild(this.chest);
		this.modelCore.addChild(this.body);
		this.maskBase.addChild(this.maskRight);
		this.maskBase.addChild(this.mane);
		updateDefaultPose();

		modelCore.scaleChildren = true;
		body.scaleChildren = true;
		chest.scaleChildren = true;
		thighLeft.scaleChildren = true;
		thighRight.scaleChildren = true;
		loinClothFront.scaleChildren = true;
		loinClothBack.scaleChildren = true;
		armRightJoint.scaleChildren = true;
		armLeftJoint.scaleChildren = true;
		neckJoint.scaleChildren = true;
		armUpperRight.scaleChildren = true;
		armLowerRight.scaleChildren = true;
		handRight.scaleChildren = true;
		spearBase.scaleChildren = true;
		spear.scaleChildren = true;
		blowgun.scaleChildren = true;
		armUpperLeft.scaleChildren = true;
		armLowerLeft.scaleChildren = true;
		handLeft.scaleChildren = true;
		shieldBase.scaleChildren = true;
		shield.scaleChildren = true;
		neck.scaleChildren = true;
		headJoint.scaleChildren = true;
		head.scaleChildren = true;
		maskBase.scaleChildren = true;
		earLeft.scaleChildren = true;
		earRight.scaleChildren = true;
		maskLeft.scaleChildren = true;
		maskRight.scaleChildren = true;
		mane.scaleChildren = true;
		earringLeft.scaleChildren = true;
		earringRight.scaleChildren = true;
		calfLeft.scaleChildren = true;
		footLeft.scaleChildren = true;
		calfRight.scaleChildren = true;
		footRight.scaleChildren = true;
		thighLeftJoint.scaleChildren = true;
		thighRightJoint.scaleChildren = true;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
		GlStateManager.pushMatrix();
		if (((EntityBarakoa) entity).getMask() == MaskType.FURY) {
			GlStateManager.scale(0.85f, 0.85f, 0.85f);
			GlStateManager.translate(0, 0.25f, 0);
		} else {
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			GlStateManager.translate(0, 0.5f, 0);
		}
		this.modelCore.render(f5);
		GlStateManager.popMatrix();
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}


	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		EntityBarakoa barakoa = (EntityBarakoa) entity;
		resetToDefaultPose();
//                f = entity.ticksExisted;
//                f1 = 0.5f;
		if (barakoa.getWeapon() == 0) {
			spear.isHidden = false;
			blowgun.isHidden = true;
		} else {
			spear.isHidden = true;
			blowgun.isHidden = false;
		}
		if (!barakoa.active) {
			return;
		}
		float doWalk = barakoa.doWalk.getAnimationProgressSinSqrt();
		float dance = barakoa.dancing.getAnimationProgressSinSqrt();
		if (f1 > 0.55f) {
			f1 = 0.55f;
		}
		float globalSpeed = 1.5f;
		float globalHeight = 1 * doWalk;
		float globalDegree = 1 * doWalk * (1 - dance);
		if (barakoa.getAnimation() != EntityBarakoa.PROJECTILE_ATTACK_ANIMATION) {
			faceTarget(f3, f4, 2.0F, neck);
			faceTarget(f3, f4, 2.0F, head);
		}
		float frame = barakoa.frame + LLibrary.PROXY.getPartialTicks();

		if (barakoa.getMask() == MaskType.FURY) {
			armLeftJoint.rotateAngleX -= 0.2;
			armLeftJoint.rotateAngleY += 1.3;
			armLowerLeft.rotateAngleX -= 0.2;
			armLowerLeft.rotateAngleY += 0.2;
			armLowerLeft.rotateAngleZ += 1;
			spearBase.setScale(spearBase.scaleX, -1, 1.5f);

			if (!barakoa.isPotionActive(PotionHandler.INSTANCE.frozen)) {
				flap(armUpperLeft, 1 * globalSpeed, 0.1f * globalHeight, false, 0.5f, 0, f, f1);
				walk(armUpperLeft, 0.5f * globalSpeed, 0.3f * globalDegree, true, 0, 1, f, f1);
			}
		} else {
			if (!barakoa.isPotionActive(PotionHandler.INSTANCE.frozen)) {
				flap(armUpperLeft, 1 * globalSpeed, 0.3f * globalHeight, false, 0.5f, 0, f, f1);
				walk(armUpperLeft, 0.5f * globalSpeed, 0.7f * globalDegree, true, 0, 0, f, f1);
			}
		}

		if (!barakoa.isPotionActive(PotionHandler.INSTANCE.frozen)) {
			bob(body, 1 * globalSpeed, 2.5f * globalHeight, false, f, f1);
			walk(loinClothFront, 1 * globalSpeed, 0.5f * globalHeight, false, 2, 0, f, f1);
			walk(loinClothBack, 1 * globalSpeed, 0.5f * globalHeight, true, 2, 0, f, f1);
			walk(body, 1 * globalSpeed, 0.2f * globalHeight, false, 1, 0.2f * globalHeight, f, f1);
			walk(thighLeft, 1 * globalSpeed, 0.2f * globalHeight, true, 1, 0.4f * globalHeight, f, f1);
			walk(thighRight, 1 * globalSpeed, 0.2f * globalHeight, true, 1, 0.4f * globalHeight, f, f1);
			swing(body, 0.5f * globalSpeed, 0.7f * globalDegree, true, 0, 0, f, f1);
			swing(thighLeft, 0.5f * globalSpeed, 0.7f * globalDegree, false, 0, 0, f, f1);
			swing(thighRight, 0.5f * globalSpeed, 0.7f * globalDegree, false, 0, 0, f, f1);
			swing(chest, 0.5f * globalSpeed, 1.4f * globalDegree, false, 0, 0, f, f1);
			swing(neck, 0.5f * globalSpeed, 0.7f * globalDegree, true, 0, 0, f, f1);
			flap(modelCore, 0.5f * globalSpeed, 0.3f * globalHeight, false, 0, 0, f, f1);
			flap(neck, 0.5f * globalSpeed, 0.15f * globalHeight, true, 0, 0, f, f1);
			flap(head, 0.5f * globalSpeed, 0.15f * globalHeight, true, 0, 0, f, f1);
			walk(thighLeft, 0.5f * globalSpeed, 1.4f * globalDegree, false, 0, 1f * globalHeight, f, f1);
			walk(thighRight, 0.5f * globalSpeed, 1.4f * globalDegree, true, 0, -1f * globalHeight, f, f1);
			walk(calfLeft, 0.5f * globalSpeed, 1.2f * globalDegree, false, -1.5f, 0.3f * globalDegree, f, f1);
			walk(calfRight, 0.5f * globalSpeed, 1.2f * globalDegree, true, -1.5f, -0.3f * globalDegree, f, f1);
			walk(footLeft, 0.5f * globalSpeed, 1.2f * globalDegree, false, -3f, 0.15f * globalDegree, f, f1);
			walk(footRight, 0.5f * globalSpeed, 1.2f * globalDegree, true, -3f, -0.15f * globalDegree, f, f1);
			thighLeft.rotateAngleY += 1f * f1 * globalDegree;
			thighRight.rotateAngleY -= 1f * f1 * globalDegree;
			walk(neck, 1 * globalSpeed, 0.2f * globalHeight, true, 0.5f, 0.5f * globalDegree, f, f1);
			walk(head, 1 * globalSpeed, 0f * globalHeight, true, 0.5f, -0.5f * globalDegree, f, f1);
			flap(armUpperRight, 1 * globalSpeed, 0.3f * globalHeight, true, 0.5f, 0, f, f1);
			walk(armUpperRight, 0.5f * globalSpeed, 0.7f * globalDegree, false, 0, 0, f, f1);
			walk(armLowerRight, 0.5f * globalSpeed, 1 * globalDegree, false, -1, 0, f, f1);
			walk(armLowerLeft, 0.5f * globalSpeed, 1 * globalDegree, true, -1, 0, f, f1);
			walk(handRight, 0.5f * globalSpeed, 1 * globalDegree, false, -2, 0.8f * globalDegree, f, f1);
			swing(handRight, 0.5f * globalSpeed, 1f * globalDegree, true, 0, 0, f, f1);
			walk(handLeft, 0.5f * globalSpeed, 1 * globalDegree, true, -2, 0.4f * globalDegree, f, f1);
		}

		if (barakoa.getAnimation() != EntityBarakoa.DIE_ANIMATION && !barakoa.isPotionActive(PotionHandler.INSTANCE.frozen)) {
			walk(body, 0.2f, 0.05f, false, 0, 0, frame, 1f);
			walk(thighLeftJoint, 0.2f, 0.05f, true, 0, 0, frame, 1f);
			walk(thighRightJoint, 0.2f, 0.05f, true, 0, 0, frame, 1f);
			walk(neck, 0.2f, 0.02f, true, 1f, 0, frame, 1f);
			walk(head, 0.2f, 0.02f, true, 1f, 0, frame, 1f);
			flap(armUpperLeft, 0.2f, -0.1f, true, -1, 0, frame, 1F);
			flap(armUpperRight, 0.2f, 0.1f, true, -1, 0, frame, 1F);
		}

		//Dancing
		float danceSpeed = 1.4f;
		thighLeft.rotateAngleY -= 0.6f * dance;
		thighRight.rotateAngleY += 0.6f * dance;
		bob(modelCore, 0.3f * danceSpeed, 10f * dance, true, frame, 1f);
		flap(modelCore, 0.3f * danceSpeed, 0.5f * dance, false, 0, 0, frame, 1f);
		walk(thighLeft, 0.3f * danceSpeed, 0.6f * dance, false, 0, -0.6f * dance, frame, 1f);
		walk(calfLeft, 0.3f * danceSpeed, 0.5f * dance, true, 0, -0.6f * dance, frame, 1f);
		walk(footLeft, 0.3f * danceSpeed, 0.2f * dance, true, 0, -0.5f * dance, frame, 1f);
		walk(thighRight, 0.3f * danceSpeed, 0.6f * dance, true, 0, 0.6f * dance, frame, 1f);
		walk(calfRight, 0.3f * danceSpeed, 0.5f * dance, false, 0, 0.6f * dance, frame, 1f);
		walk(footRight, 0.3f * danceSpeed, 0.2f * dance, false, 0, 0.5f * dance, frame, 1f);
		armRightJoint.rotateAngleX -= 1.7 * dance;
		handRight.rotateAngleX += 1 * dance;
		walk(armUpperRight, 1.2f * danceSpeed, 0.5f * dance, false, 0, -0.3f * dance, frame, 1f);
		walk(armLowerRight, 1.2f * danceSpeed, 0.5f * dance, true, 0, 0, frame, 1f);
		armLeftJoint.rotateAngleX -= 1.7 * dance;
		walk(armUpperLeft, 1.2f * danceSpeed, 0.5f * dance, false, 0, -0.3f * dance, frame, 1f);
		walk(armLowerLeft, 1.2f * danceSpeed, 0.5f * dance, true, 0, 0, frame, 1f);
		flap(neck, 0.3f * danceSpeed, 0.2f * dance, true, 0, 0, frame, 1f);
		flap(head, 1.2f * danceSpeed, 0.4f * dance, true, 0, 0, frame, 1f);
		walk(loinClothFront, 0.6f * danceSpeed, 0.6f * dance, true, 1, 0.4f * dance, frame, 1f);
		walk(loinClothBack, 0.6f * danceSpeed, 0.6f * dance, false, 1, 0.4f * dance, frame, 1f);
		if (barakoa.getMask() == MaskType.FURY) {
			armLeftJoint.rotateAngleX += 0.2 * dance;
			armLeftJoint.rotateAngleY -= 1.3 * dance;
			armLowerLeft.rotateAngleX += 0.2 * dance;
			armLowerLeft.rotateAngleY -= 0.2 * dance;
			armLowerLeft.rotateAngleZ -= 1 * dance;
		}
	}

	public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		EntityBarakoa barakoa = (EntityBarakoa) entity;
		animator.update(barakoa);
		setRotationAngles(f, f1, f2, f3, f4, f5, barakoa);

		float frame = barakoa.frame + LLibrary.PROXY.getPartialTicks();

		if (barakoa.getMask() == MaskType.FURY) {
			animator.setAnimation(EntityBarakoa.ATTACK_ANIMATION);
			animator.setStaticKeyframe(3);
			animator.startKeyframe(4);
			animator.rotate(body, -0.3f, 1f, 0);
			animator.move(modelCore, -4, 0, -2);
			animator.rotate(chest, 0, 0.2f, 0);
			animator.rotate(neck, 0.1f, -0.6f, 0);
			animator.rotate(head, 0.1f, -0.6f, 0);
			animator.rotate(armUpperLeft, 0, 0, -0.5f);
			animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
			animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
			animator.rotate(armUpperRight, 0, 0.5f, 0);
			animator.rotate(armLowerRight, -1, -0.5f, 0);
			animator.rotate(handRight, 1f, 0, 0);
			animator.rotate(thighLeft, -0.9f, 0.3f, 0);
			animator.rotate(calfLeft, 0.1f, 0, 0);
			animator.rotate(footLeft, 0, 0, 0);
			animator.rotate(thighRight, 0.3f, -0.9f, 0);
			animator.rotate(calfRight, 0, 0, 0);
			animator.rotate(footRight, 0, 0, 0);
			animator.endKeyframe();
			animator.setStaticKeyframe(1);
			animator.startKeyframe(3);
			animator.rotate(body, 0.5f, -0.3f, 0);
			animator.move(modelCore, -1.5f, 1.2f, -8.5f);
			animator.rotate(chest, 0, -0.5f, 0);
			animator.rotate(neck, -0.1f, 0.3f, 0);
			animator.rotate(head, -0.2f, 0.3f, 0);
			animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
			animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
			animator.rotate(armUpperRight, -1.7f, 0, -0.5f);
			animator.rotate(armLowerRight, 0.7f, 0, 0);
			animator.rotate(handRight, 1.4f, 0.8f, 0.7f);
			animator.rotate(thighLeft, -0.8f, 0.7f, 0);
			animator.rotate(calfLeft, -0.6f, 0, 0);
			animator.rotate(footLeft, 0.9f, 0, 0);
			animator.move(thighRight, 0, 1, 0);
			animator.rotate(thighRight, 0.7f, 0, 0f);
			animator.rotate(calfRight, -0.5f, 0, 0);
			animator.rotate(footRight, -0.5f, 0, 0);
			animator.endKeyframe();
			animator.setStaticKeyframe(1);
			animator.resetKeyframe(6);

			animator.setAnimation(EntityBarakoana.BLOCK_ANIMATION);
			animator.startKeyframe(3);
			animator.move(body, 0, 5f, 1f);
			animator.rotate(body, 0.3f, 0, 0);
			animator.rotate(chest, 0, 0.8f, 0);
			animator.rotate(thighLeftJoint, -0.3f, 0, 0);
			animator.rotate(thighRightJoint, -0.3f, 0, 0);
			animator.rotate(thighLeft, -0.5f, 0, 0);
			animator.rotate(thighRight, -0.5f, 0, 0);
			animator.rotate(calfLeft, 0.8f, 0, 0);
			animator.rotate(calfRight, 0.8f, 0, 0);
			animator.rotate(footLeft, -0.3f, 0, 0);
			animator.rotate(footRight, -0.3f, 0, 0);
			animator.rotate(neck, 0.3f, -0.8f, 0);
			animator.rotate(head, -0.6f, 0, 0);
			animator.rotate(armLeftJoint, 0, -0.8f, 0);
			animator.rotate(armLeftJoint, -0.5f, 0, -1);
			animator.rotate(armLowerLeft, 0, 0, 0.7f);
			animator.endKeyframe();
			animator.resetKeyframe(7);

			animator.setAnimation(EntityBarakoa.HURT_ANIMATION);
			animator.startKeyframe(3);
			animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
			animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
			animator.move(body, 0, 5f, 1f);
			animator.rotate(body, 0.3f, 0, 0);
			animator.rotate(thighLeftJoint, -0.3f, 0, 0);
			animator.rotate(thighRightJoint, -0.3f, 0, 0);
			animator.rotate(thighLeft, -0.5f, 0, 0);
			animator.rotate(thighRight, -0.5f, 0, 0);
			animator.rotate(calfLeft, 0.8f, 0, 0);
			animator.rotate(calfRight, 0.8f, 0, 0);
			animator.rotate(footLeft, -0.3f, 0, 0);
			animator.rotate(footRight, -0.3f, 0, 0);
			animator.rotate(neck, 0.5f, 0, 0);
			animator.rotate(head, -0.3f, 0, 0);
			animator.rotate(armUpperLeft, -0.3f, 0, -1);
			animator.rotate(armUpperRight, -0.3f, 0, 1);
			animator.rotate(armLowerLeft, -0.7f, 0, 0);
			animator.rotate(armLowerRight, -0.7f, 0, 0);
			animator.endKeyframe();
			animator.resetKeyframe(7);

			animator.setAnimation(EntityBarakoa.DIE_ANIMATION);
			animator.startKeyframe(3);
			animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
			animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
			animator.move(body, 0, 5f, 1f);
			animator.rotate(body, 0.3f, 0, 0);
			animator.rotate(thighLeftJoint, -0.3f, 0, 0);
			animator.rotate(thighRightJoint, -0.3f, 0, 0);
			animator.rotate(thighLeft, -0.5f, 0, 0);
			animator.rotate(thighRight, -0.5f, 0, 0);
			animator.rotate(calfLeft, 0.8f, 0, 0);
			animator.rotate(calfRight, 0.8f, 0, 0);
			animator.rotate(footLeft, -0.3f, 0, 0);
			animator.rotate(footRight, -0.3f, 0, 0);
			animator.rotate(neck, 0.5f, 0, 0);
			animator.rotate(head, -0.3f, 0, 0);
			animator.rotate(armUpperLeft, -0.3f, 0, -1);
			animator.rotate(armUpperRight, -0.3f, 0, 1);
			animator.rotate(armLowerLeft, -0.7f, 0, 0);
			animator.rotate(armLowerRight, -0.7f, 0, 0);
			animator.endKeyframe();
			animator.startKeyframe(5);
			animator.rotate(head, -0.8f, 0, 0);
			animator.move(flailer, 1, 0, 0);
			animator.endKeyframe();
			animator.setStaticKeyframe(10);
			animator.startKeyframe(10);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -0.8f, 0, 0);
			animator.move(body, 0, -22f, -5f);
			animator.endKeyframe();
			animator.startKeyframe(4);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -0.8f, 0, 0);
			animator.move(body, 0, 7, 0);
			animator.endKeyframe();
			animator.startKeyframe(2);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -1.6f, 0, 0);
			animator.move(body, 0, -3, 5);
			animator.endKeyframe();
			animator.startKeyframe(2);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -1.58f, 0, 0);
			animator.move(body, 0, 9f, 5);
			animator.endKeyframe();
			animator.setStaticKeyframe(20);

			armLeftJoint.rotateAngleX += 0.2 * flailer.rotationPointX;
			armLeftJoint.rotateAngleY -= 1.3 * flailer.rotationPointX;
			armLowerLeft.rotateAngleX += 0.2 * flailer.rotationPointX;
			armLowerLeft.rotateAngleY -= 0.2 * flailer.rotationPointX;
			armLowerLeft.rotateAngleZ -= 1 * flailer.rotationPointX;
		} else {
			animator.setAnimation(EntityBarakoa.ATTACK_ANIMATION);
			animator.setStaticKeyframe(3);
			animator.startKeyframe(4);
			animator.rotate(body, -0.3f, 1f, 0);
			animator.move(modelCore, -4, 0, -2);
			animator.rotate(chest, 0, 0.2f, 0);
			animator.rotate(neck, 0.1f, -0.6f, 0);
			animator.rotate(head, 0.1f, -0.6f, 0);
			animator.rotate(armUpperLeft, 0, 0, -0.5f);
			animator.rotate(armUpperRight, -2, 0, -1.5f);
			animator.rotate(armLowerRight, -0.5f, 0, 0);
			animator.rotate(handRight, 0, 1f, 0);
			animator.rotate(thighLeft, -0.9f, 0.3f, 0);
			animator.rotate(calfLeft, 0.1f, 0, 0);
			animator.rotate(footLeft, 0, 0, 0);
			animator.rotate(thighRight, 0.3f, -0.9f, 0);
			animator.rotate(calfRight, 0, 0, 0);
			animator.rotate(footRight, 0, 0, 0);
			animator.endKeyframe();
			animator.setStaticKeyframe(1);
			animator.startKeyframe(3);
			animator.rotate(body, 0.5f, -0.3f, 0);
			animator.move(modelCore, -1.5f, 1.2f, -8.5f);
			animator.rotate(chest, 0, -0.5f, 0);
			animator.rotate(neck, -0.1f, 0.3f, 0);
			animator.rotate(head, -0.2f, 0.3f, 0);
			animator.rotate(armUpperRight, -0.8f, 0, -0.5f);
			animator.rotate(armLowerRight, 0.7f, 0, 0);
			animator.rotate(handRight, 0.6f, 0.6f, 0);
			animator.rotate(thighLeft, -0.8f, 0.7f, 0);
			animator.rotate(calfLeft, -0.6f, 0, 0);
			animator.rotate(footLeft, 0.9f, 0, 0);
			animator.move(thighRight, 0, 1, 0);
			animator.rotate(thighRight, 0.7f, 0, 0f);
			animator.rotate(calfRight, -0.5f, 0, 0);
			animator.rotate(footRight, -0.5f, 0, 0);
			animator.endKeyframe();
			animator.setStaticKeyframe(1);
			animator.resetKeyframe(6);

			animator.setAnimation(EntityBarakoa.PROJECTILE_ATTACK_ANIMATION);
			animator.startKeyframe(5);
			animator.rotate(body, -0.3f, 0, 0);
			animator.rotate(thighRightJoint, 0.3f, 0, 0);
			animator.rotate(thighLeftJoint, 0.3f, 0, 0);
			animator.rotate(loinClothFront, 0.3f, 0, 0);
			animator.rotate(loinClothBack, 0.3f, 0, 0);
			animator.rotate(neck, -0.4f, 0, 0);
			animator.rotate(head, 0.5f, 0, 0);
			animator.rotate(armUpperRight, -1.5f, 0, 0);
			animator.rotate(armLowerRight, 0, 0, -1f);
			animator.rotate(handRight, -1f, -0.2f, 1.2f);
			animator.move(blowgun, 0, 0, 4.5f);
			animator.rotate(blowgun, 0, 0, MathUtils.PI);
			animator.endKeyframe();
			animator.setStaticKeyframe(3);
			animator.startKeyframe(3);
			animator.rotate(body, 0.5f, 0, 0);
			animator.rotate(thighRightJoint, -0.5f, 0, 0);
			animator.rotate(thighLeftJoint, -0.5f, 0, 0);
			animator.rotate(loinClothBack, -0.5f, 0, 0);
			animator.rotate(loinClothFront, -0.5f, 0, 0);
			animator.rotate(neck, 0.2f, 0, 0);
			animator.rotate(head, -0.7f, 0, 0);
			animator.rotate(armUpperRight, -1.8f, 0, 0);
			animator.rotate(armRightJoint, -0.5f, 0, 0);
			animator.move(armRightJoint, 1, 0, -2);
			animator.rotate(armLowerRight, 0.8f, 0, -0.4f);
			animator.rotate(handRight, -1.5f, 0.4f, 1.0f);
			animator.move(blowgun, 0, 0, 5f);
			animator.rotate(blowgun, 0, 0, MathUtils.PI);
			animator.endKeyframe();
			animator.setStaticKeyframe(2);
			animator.resetKeyframe(7);

			animator.setAnimation(EntityBarakoa.HURT_ANIMATION);
			animator.startKeyframe(3);
			animator.move(body, 0, 5f, 1f);
			animator.rotate(body, 0.3f, 0, 0);
			animator.rotate(thighLeftJoint, -0.3f, 0, 0);
			animator.rotate(thighRightJoint, -0.3f, 0, 0);
			animator.rotate(thighLeft, -0.5f, 0, 0);
			animator.rotate(thighRight, -0.5f, 0, 0);
			animator.rotate(calfLeft, 0.8f, 0, 0);
			animator.rotate(calfRight, 0.8f, 0, 0);
			animator.rotate(footLeft, -0.3f, 0, 0);
			animator.rotate(footRight, -0.3f, 0, 0);
			animator.rotate(neck, 0.5f, 0, 0);
			animator.rotate(head, -0.3f, 0, 0);
			animator.rotate(armUpperLeft, -0.3f, 0, -1);
			animator.rotate(armUpperRight, -0.3f, 0, 1);
			animator.rotate(armLowerLeft, -0.7f, 0, 0);
			animator.rotate(armLowerRight, -0.7f, 0, 0);
			animator.endKeyframe();
			animator.resetKeyframe(7);

			animator.setAnimation(EntityBarakoa.DIE_ANIMATION);
			animator.startKeyframe(3);
			animator.rotate(armLeftJoint, 0.2f, -1.3f, 0);
			animator.rotate(armLowerLeft, 0.2f, -0.2f, -1);
			animator.move(body, 0, 5f, 1f);
			animator.rotate(body, 0.3f, 0, 0);
			animator.rotate(thighLeftJoint, -0.3f, 0, 0);
			animator.rotate(thighRightJoint, -0.3f, 0, 0);
			animator.rotate(thighLeft, -0.5f, 0, 0);
			animator.rotate(thighRight, -0.5f, 0, 0);
			animator.rotate(calfLeft, 0.8f, 0, 0);
			animator.rotate(calfRight, 0.8f, 0, 0);
			animator.rotate(footLeft, -0.3f, 0, 0);
			animator.rotate(footRight, -0.3f, 0, 0);
			animator.rotate(neck, 0.5f, 0, 0);
			animator.rotate(head, -0.3f, 0, 0);
			animator.rotate(armUpperLeft, -0.3f, 0, -1);
			animator.rotate(armUpperRight, -0.3f, 0, 1);
			animator.rotate(armLowerLeft, -0.7f, 0, 0);
			animator.rotate(armLowerRight, -0.7f, 0, 0);
			animator.endKeyframe();
			animator.startKeyframe(5);
			animator.rotate(head, -0.8f, 0, 0);
			animator.move(flailer, 1, 0, 0);
			animator.endKeyframe();
			animator.setStaticKeyframe(10);
			animator.startKeyframe(10);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -0.8f, 0, 0);
			animator.move(body, 0, -22f, -5f);
			animator.endKeyframe();
			animator.startKeyframe(4);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -0.8f, 0, 0);
			animator.move(body, 0, 7, 0);
			animator.endKeyframe();
			animator.startKeyframe(2);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -1.6f, 0, 0);
			animator.move(body, 0, -3, 5);
			animator.endKeyframe();
			animator.startKeyframe(2);
			animator.move(scaler, 0.999f, 0, 0);
			animator.rotate(head, -1.58f, 0, 0);
			animator.move(body, 0, 9f, 5);
			animator.endKeyframe();
			animator.setStaticKeyframe(20);
		}

		animator.setAnimation(EntityBarakoa.IDLE_ANIMATION);
		animator.startKeyframe(10);
		animator.move(talker, 1, 0, 0);
		animator.endKeyframe();
		animator.setStaticKeyframe(15);
		animator.resetKeyframe(10);

		animator.setAnimation(EntityBarakoa.ACTIVATE_ANIMATION);
		animator.setStaticKeyframe(3);
		animator.startKeyframe(10);
		animator.move(scaler, -0.999f, 0, 0);
		animator.move(flailer, 1, 0, 0);
		animator.move(body, 0, -9, -5);
		animator.endKeyframe();
		animator.setStaticKeyframe(2);
		animator.startKeyframe(5);
		animator.move(scaler, -0.999f, 0, 0);
		animator.move(body, 0, -9, -5);
		animator.rotate(head, 1.58f, 0, 0);
		animator.endKeyframe();
		animator.setStaticKeyframe(5);

		//Inactive
		if (!barakoa.active) {
			scaler.rotationPointX += 0.999f;
			head.rotateAngleX -= 1.58f;
			body.rotationPointY += 9f;
			body.rotationPointZ += 5f;
			if (!barakoa.onGround) {
				body.rotateAngleX += 0.4f * frame;
			}
		}

		float flailSpeed = 2.3f;
		bob(modelCore, 0.3f * flailSpeed, 10f * flailer.rotationPointX, true, frame, 1f);
		walk(thighLeft, 0.3f * flailSpeed, 0.6f * flailer.rotationPointX, false, 0, -0.3f * flailer.rotationPointX, frame, 1f);
		walk(calfLeft, 0.3f * flailSpeed, 0.5f * flailer.rotationPointX, true, 0, 0.3f * flailer.rotationPointX, frame, 1f);
		walk(footLeft, 0.3f * flailSpeed, 0.2f * flailer.rotationPointX, true, 0, 0, frame, 1f);
		walk(thighRight, 0.3f * flailSpeed, 0.6f * flailer.rotationPointX, true, 0, 0.3f * flailer.rotationPointX, frame, 1f);
		walk(calfRight, 0.3f * flailSpeed, 0.5f * flailer.rotationPointX, false, 0, -0.3f * flailer.rotationPointX, frame, 1f);
		walk(footRight, 0.3f * flailSpeed, 0.2f * flailer.rotationPointX, false, 0, 0, frame, 1f);
		armRightJoint.rotateAngleX -= 1.7 * flailer.rotationPointX;
		handRight.rotateAngleX += 1 * flailer.rotationPointX;
		walk(armUpperRight, 0.6f * flailSpeed, 0.5f * flailer.rotationPointX, false, 0, -0.3f * flailer.rotationPointX, frame, 1f);
		walk(armLowerRight, 0.6f * flailSpeed, 0.5f * flailer.rotationPointX, true, 0, 0, frame, 1f);
		armLeftJoint.rotateAngleX -= 1.7 * flailer.rotationPointX;
		walk(armUpperLeft, 0.6f * flailSpeed, 0.5f * flailer.rotationPointX, false, 0, -0.3f * flailer.rotationPointX, frame, 1f);
		walk(armLowerLeft, 0.6f * flailSpeed, 0.5f * flailer.rotationPointX, true, 0, 0, frame, 1f);

		float scale = 1 - scaler.rotationPointX;
		body.setScale(scale, scale, scale);
		maskBase.setScale(1 / scale, 1 / scale, 1 / scale);


		float talk = talker.rotationPointX;
		if (!barakoa.isPotionActive(PotionHandler.INSTANCE.frozen)) {
			walk(head, 1.5f, 0.1f * talk, false, 0, -0.5f * talk, frame, 1f);
			walk(neck, 0, 0, false, 0, 0.5f * talk, frame, 1f);
			walk(armUpperRight, 0.5f, 0.2f * talk, false, 0, -0.7f * talk, frame, 1f);
			flap(armUpperRight, 0.4f, 0.2f * talk, false, 2, 0, frame, 1f);
			walk(armLowerRight, 0.5f, 0.2f * talk, false, -1, 0.3f * talk, frame, 1f);
			swing(handRight, 0.5f, 0.2f * talk, false, -2, 1.8f * talk, frame, 1f);
			walk(armUpperLeft, 0.5f, 0.2f * talk, false, 0, -0.7f * talk, frame, 1f);
			flap(armUpperLeft, 0.4f, 0.2f * talk, true, 2, 0, frame, 1f);
			walk(armLowerLeft, 0.5f, 0.2f * talk, false, -1, 0.3f * talk, frame, 1f);
			swing(handLeft, 0.5f, 0.2f * talk, false, -2, -1.8f * talk, frame, 1f);
			if (barakoa.getMask() == MaskType.FURY) {
				armLeftJoint.rotateAngleX += 0.2 * talk;
				armLeftJoint.rotateAngleY -= 1.3 * talk;
				armLowerLeft.rotateAngleX += 0.2 * talk;
				armLowerLeft.rotateAngleY -= 0.2 * talk;
				armLowerLeft.rotateAngleZ -= 1 * talk;
			}
		}
	}
}

