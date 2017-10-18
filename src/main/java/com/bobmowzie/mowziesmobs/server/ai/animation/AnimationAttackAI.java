package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

public class AnimationAttackAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
	protected EntityLivingBase entityTarget;
	protected SoundEvent attackSound;
	protected float knockback = 1;
	protected float range;
	protected float damageMultiplier;
	protected int damageFrame;
	protected SoundEvent hitSound;

	public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float knockback, float range, float damageMultiplier, int damageFrame) {
		super(entity, animation);
		setMutexBits(8);
		this.entity = entity;
		this.entityTarget = null;
		this.attackSound = attackSound;
		this.knockback = knockback;
		this.range = range;
		this.damageMultiplier = damageMultiplier;
		this.damageFrame = damageFrame;
		this.hitSound = hitSound;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		entityTarget = animatingEntity.getAttackTarget();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (animatingEntity.getAnimationTick() < damageFrame && entityTarget != null) {
			animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
		}
		if (animatingEntity.getAnimationTick() == damageFrame) {
			float damage = (float) animatingEntity.getAttack();
			if (entityTarget != null && animatingEntity.targetDistance <= range) {
				entityTarget.attackEntityFrom(DamageSource.causeMobDamage(animatingEntity), damage * damageMultiplier);
				entityTarget.motionX *= knockback;
				entityTarget.motionZ *= knockback;
				if (hitSound != null) {
					animatingEntity.playSound(hitSound, 1, 1);
				}
			}
			if (attackSound != null) {
				animatingEntity.playSound(attackSound, 1, 1);
			}
		}
	}
}
