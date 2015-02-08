package com.bobmowzie.mowziesmobs.enums;

public enum MMAnimation
{
    ATTACK((byte) 1);

    private MMAnimation(byte id)
    {
        this.id = id;
    }

    public int animID()
    {
        return (int) this.id;
    }

    public final byte id;
}
