package com.bobmowzie.mowziesmobs.common.animation;

import thehippomaster.AnimationAPI.IAnimatedEntity;

public class AnimActivate extends MMAnimBase
{
    public AnimActivate(IAnimatedEntity entity, int duration)
    {
        super(entity, duration);
    }

    public int getAnimID()
    {
        return MMAnimation.ACTIVATE.animID();
    }

    public void resetTask()
    {
        super.resetTask();
        animatingEntity.active = true;
    }
}
