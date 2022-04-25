package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.ResourceLocation;

public class MowzieEntityEggInfo {
    public final EntityType<? extends Mob> type;

    public final Class<? extends Mob> clazz;

    public final int primaryColor;

    public final int secondaryColor;

    public MowzieEntityEggInfo(EntityType<? extends Mob> type, Class<? extends Mob> clazz, int primaryColor, int secondaryColor) {
        this.type = type;
        this.clazz = clazz;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }
}
