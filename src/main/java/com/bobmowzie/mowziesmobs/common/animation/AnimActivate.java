package com.bobmowzie.mowziesmobs.common.animation;

import thehippomaster.AnimationAPI.IAnimatedEntity;

public class AnimActivate extends MMAnimBase
{
    public AnimActivate(IAnimatedEntity entity, int id, int duration)
    {
        super(entity, id, duration);
    }

    public void resetTask()
    {
        super.resetTask();
        animatingEntity.active = true;
    }
}
