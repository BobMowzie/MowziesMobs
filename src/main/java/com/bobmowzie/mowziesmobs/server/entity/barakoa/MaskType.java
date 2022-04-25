package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.google.common.base.Defaults;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.EnumMap;
import java.util.Locale;

public enum MaskType {
    FURY(MobEffects.DAMAGE_BOOST, 0.7F, 2F, true),
    FEAR(MobEffects.MOVEMENT_SPEED),
    RAGE(MobEffects.DIG_SPEED),
    BLISS(MobEffects.JUMP),
    MISERY(MobEffects.DAMAGE_RESISTANCE),
    FAITH(MobEffects.HEALTH_BOOST, 0.7F, 2F, false);

    public static final int COUNT = MaskType.values().length;

    public final MobEffect potion;

    public final float entityWidth;

    public final float entityHeight;

    public final boolean canBlock;

    public final String name;

    MaskType(MobEffect potion) {
        this(potion, 0.6F, 1.7F, false);
    }

    MaskType(MobEffect potion, float entityWidth, float entityHeight, boolean canBlock) {
        this.potion = potion;
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
        this.canBlock = canBlock;
        name = name().toLowerCase(Locale.ENGLISH);
    }

    public static MaskType from(int id) {
        if (id < 0 || id >= COUNT) {
            return MISERY;
        }
        return values()[id];
    }

    public static <T> EnumMap<MaskType, T> newEnumMap(Class<T> type, T... defaultValues) {
        EnumMap map = new EnumMap<MaskType, T>(MaskType.class);
        MaskType[] masks = values();
        for (int i = 0; i < masks.length; i++) {
            map.put(masks[i], i >= 0 && i < defaultValues.length ? defaultValues[i] : Defaults.defaultValue(type));
        }
        return map;
    }
}
