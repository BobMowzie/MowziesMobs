package com.bobmowzie.mowziesmobs.common.animation;

import thehippomaster.AnimationAPI.IAnimatedEntity;

public class AnimDeactivate extends MMAnimBase
{
    public AnimDeactivate(IAnimatedEntity entity, int id, int duration)
    {
        super(entity, id, duration);
    }

    public void resetTask()
    {
        super.resetTask();
        animatingEntity.active = false;
    }
}
