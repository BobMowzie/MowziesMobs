package com.bobmowzie.mowziesmobs.common.animation;

public enum MMAnimation
{
    BASIC(1),
    ATTACK(2),
    TAKEDAMAGE(3),
    DIE(4),
    BABY_FOLIAATH_EAT(5),
    ACTIVATE(6),
    DEACTIVATE(7);

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
