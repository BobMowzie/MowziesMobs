package com.bobmowzie.mowziesmobs.server.potion;

/**
 * Created by Josh on 5/31/2017.
 */
public class MowziePotionFrozen extends MowziePotion {
    public MowziePotionFrozen() {
        super(false, 0xd8e7ff);
        setPotionName("potion.frozen");
        setIconIndex(0, 0);
        setRegistryName("frozen");
    }
}
