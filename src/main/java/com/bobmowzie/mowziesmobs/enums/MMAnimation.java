package com.bobmowzie.mowziesmobs.enums;

public enum MMAnimation
{
    BASIC(1),
    ATTACK(2),
    TAKEDAMAGE(3),
    DIE(4),
    BABY_FOLIAATH_EAT(5);

    private MMAnimation(int id)
    {
        this.id = id;
    }

    public int animID()
    {
        return this.id;
    }


    public final int id;
}
