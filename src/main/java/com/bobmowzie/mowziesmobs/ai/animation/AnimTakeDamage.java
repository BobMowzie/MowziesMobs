package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;

public class AnimTakeDamage extends MMAnimBase
{
    private int duration;

    public AnimTakeDamage(MMEntityBase entity, int duration)
    {
        super(entity, duration);
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
