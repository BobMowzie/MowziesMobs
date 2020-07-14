package com.bobmowzie.mowziesmobs.server.potion;

/**
 * Created by Josh on 1/9/2019.
 */
public class MowziePotionPoisonResist extends MowziePotion {
    public MowziePotionPoisonResist() {
        super(false, 0x66ff33);
        setPotionName("potion.poisonResist");
        setIconIndex(0, 1);
    }
}
