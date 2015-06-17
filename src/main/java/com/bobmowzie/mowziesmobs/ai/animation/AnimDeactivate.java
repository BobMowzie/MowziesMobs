package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

/**
 * Created by jnad325 on 6/13/15.
 */
public class AnimDeactivate extends MMAnimBase{
    public AnimDeactivate(IAnimatedEntity entity, int duration) {
        super(entity, duration);
    }
    @Override
    public int getAnimID()
    {
        return MMAnimation.DEACTIVATE.animID();
    }

    @Override
    public boolean isAutomatic()
    {
        return true;
    }

    @Override
    public int getDuration()
    {
        return duration;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        animatingEntity.active = false;
    }
}
