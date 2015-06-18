package com.bobmowzie.mowziesmobs.common.animation;

import thehippomaster.AnimationAPI.IAnimatedEntity;

public class AnimBabyFoliaathEat extends MMAnimBase
{
    public AnimBabyFoliaathEat(IAnimatedEntity entity, int duration)
    {
        super(entity, duration);
    }

    public int getAnimID()
    {
        return MMAnimation.BABY_FOLIAATH_EAT.animID();
    }
}
