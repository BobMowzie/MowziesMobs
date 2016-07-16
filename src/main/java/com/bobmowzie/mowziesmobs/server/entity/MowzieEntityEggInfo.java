package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.EntityLiving;

public class MowzieEntityEggInfo {
    public final String entityName;

    public final Class<? extends EntityLiving> clazz;

    public final int primaryColor;

    public final int secondaryColor;

    public MowzieEntityEggInfo(String entityName, Class<? extends EntityLiving> clazz, int primaryColor, int secondaryColor) {
        this.entityName = entityName;
        this.clazz = clazz;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }
}
