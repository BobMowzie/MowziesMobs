package com.bobmowzie.mowziesmobs.enums;

public enum MMAnimation
{
    ATTACK(1);

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
