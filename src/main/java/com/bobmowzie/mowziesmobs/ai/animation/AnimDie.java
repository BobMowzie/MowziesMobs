package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;

public class AnimDie extends MMAnimBase
{
    private int duration;
    private MMEntityBase dyingEntity;

    public AnimDie(MMEntityBase entity, int duration)
    {
        super(entity, duration);
        setMutexBits(8);
        this.duration = duration;
        dyingEntity = entity;
    }

    @Override
    public int getAnimID()
    {
        return MMAnimation.DIE.animID();
    }

    @Override
    public boolean isAutomatic()
    {
        return true;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        dyingEntity.setAnimTick(0);
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
    }
}
