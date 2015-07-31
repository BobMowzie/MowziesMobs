package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;

public class AnimTakeDamage extends MMAnimBase
{
    public AnimTakeDamage(MMEntityBase entity, int duration)
    {
        super(entity, MMAnimation.TAKEDAMAGE.animID(), duration);
        setMutexBits(8);
    }

    public int getAnimID()
    {
        return MMAnimation.TAKEDAMAGE.animID();
    }
}
