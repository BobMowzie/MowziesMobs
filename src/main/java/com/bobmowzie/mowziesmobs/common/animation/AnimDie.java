package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;

public class AnimDie extends MMAnimBase
{
    private MMEntityBase dyingEntity;

    public AnimDie(MMEntityBase entity, int duration)
    {
        super(entity, MMAnimation.DIE.animID(), duration);
        setMutexBits(8);
        this.duration = duration;
        dyingEntity = entity;
    }

    public int getAnimID()
    {
        return MMAnimation.DIE.animID();
    }

    public void startExecuting()
    {
        super.startExecuting();
        dyingEntity.setAnimTick(0);
    }

    @Override
    public void updateTask() {
        super.updateTask();
    }
}
