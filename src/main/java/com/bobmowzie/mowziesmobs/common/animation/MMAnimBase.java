package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class MMAnimBase extends AIAnimation
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

    public MMAnimBase(IAnimatedEntity entity, int id, int duration, boolean interruptsAI)
    {
        super(entity);
        this.duration = duration;
        animatingEntity = (MMEntityBase) entity;
        this.id = id;
        if (!interruptsAI) setMutexBits(8);
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
