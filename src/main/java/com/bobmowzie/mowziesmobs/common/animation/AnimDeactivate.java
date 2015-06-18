package com.bobmowzie.mowziesmobs.common.animation;

import thehippomaster.AnimationAPI.IAnimatedEntity;

public class AnimDeactivate extends MMAnimBase
{
    public AnimDeactivate(IAnimatedEntity entity, int duration)
    {
        super(entity, duration);
    }

    public int getAnimID()
    {
        return MMAnimation.DEACTIVATE.animID();
    }

    public void resetTask()
    {
        super.resetTask();
        animatingEntity.active = false;
    }
}
