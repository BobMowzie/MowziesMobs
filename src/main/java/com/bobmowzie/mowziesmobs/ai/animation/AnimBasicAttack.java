package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimBasicAttack extends AIAnimation
{
	private MMEntityBase entity;
	private int duration;
	private EntityLivingBase entityTarget;
	private String attackSound;

	public AnimBasicAttack(MMEntityBase entity, int duration, String sound)
	{
		super(entity);
		this.entity = entity;
		this.duration = duration;
		attackSound = sound;
	}

	@Override
	public int getAnimID()
	{
		return MMAnimation.ATTACK.id;
	}

	@Override
	public boolean isAutomatic()
	{
		return true;
	}

	@Override
	public int getDuration()
	{
		return this.duration;
	}

	@Override
	public void startExecuting()
	{
		super.startExecuting();
		System.out.println("Bite");
		this.entityTarget = this.entity.getAttackTarget();
	}

	@Override
	public void updateTask()
	{
		super.updateTask();
		if (this.entity.getAnimTick() == ((this.duration / 2) - 2)) {
			float damage = (float) this.entity.getAttack();
			this.entityTarget.attackEntityFrom(DamageSource.causeMobDamage(this.entity), damage);
			entity.playSound(attackSound, 1, 1);
		}
	}
}
