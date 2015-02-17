package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimTakeDamage extends AIAnimation
{
    private int duration;

    public AnimTakeDamage(MMEntityBase entity, int duration)
    {
        super(entity);
        setMutexBits(8);
        this.duration = duration;
    }

    @Override
    public int getAnimID()
    {
        return MMAnimation.TAKEDAMAGE.animID();
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
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
    }
}
