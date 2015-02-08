package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimAttack extends AIAnimation
{
	private EntityFoliaath entity;
	private int duration;

	public AnimAttack(EntityFoliaath entity, int duration)
	{
		super(entity);
		this.entity = entity;
		this.duration = duration;
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
	public boolean shouldExecute() {
		return false;
	}

	@Override
	public void startExecuting()
	{
		super.startExecuting();
	}

	@Override
	public void updateTask()
	{
		super.updateTask();
	}

	@Override
	public void resetTask()
	{
		super.resetTask();
	}
}
