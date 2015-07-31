package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public abstract class MMAnimBase extends AIAnimation
{
    protected MMEntityBase animatingEntity;
    protected int duration;
    protected int id;

    public MMAnimBase(IAnimatedEntity entity, int id, int duration)
    {
        super(entity);
        this.duration = duration;
        animatingEntity = (MMEntityBase) entity;
        this.id = id;
    }

    @Override
    public int getAnimID() {
        return id;
    }

    public boolean isAutomatic()
    {
        return true;
    }

    public int getDuration()
    {
        return duration;
    }

    public void startExecuting()
    {
        super.startExecuting();
        animatingEntity.currentAnim = this;
    }

    public void resetTask()
    {
        super.resetTask();
        animatingEntity.currentAnim = null;
    }
}
