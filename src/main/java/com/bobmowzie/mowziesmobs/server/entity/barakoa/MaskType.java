package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

import java.util.Locale;

public enum MaskType {
    FURY(MobEffects.STRENGTH),
    FEAR(MobEffects.SPEED),
    RAGE(MobEffects.HASTE),
    BLISS(MobEffects.JUMP_BOOST),
    MISERY(MobEffects.RESISTANCE);

    public final String name;

    public final Potion potion;

    private MaskType(Potion potion) {
        this.potion = potion;
        name = name().toLowerCase(Locale.ENGLISH);
    }
}
