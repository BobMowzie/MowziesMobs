package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class MowzieEntityEggInfo {
    public final ResourceLocation id;

    public final Class<? extends MobEntity> clazz;

    public final int primaryColor;

    public final int secondaryColor;

    public MowzieEntityEggInfo(ResourceLocation id, Class<? extends MobEntity> clazz, int primaryColor, int secondaryColor) {
        this.id = id;
        this.clazz = clazz;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }
}
