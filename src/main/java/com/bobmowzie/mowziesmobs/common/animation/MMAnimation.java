package com.bobmowzie.mowziesmobs.common.animation;

public enum MMAnimation
{
    TAKEDAMAGE(-3),
    DIE(-2);

    public final int id;

    MMAnimation(int id)
    {
        this.id = id;
    }

    public int animID()
    {
        return this.id;
    }
}
