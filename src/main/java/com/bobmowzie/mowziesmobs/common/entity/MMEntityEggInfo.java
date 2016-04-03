package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatComponentTranslation;

public class MMEntityEggInfo {
    /**
     * The entityID of the spawned mob
     */
    public final int spawnedID;
    /**
     * Base color of the egg
     */
    public final int primaryColor;
    /**
     * Color of the egg spots
     */
    public final int secondaryColor;
    public final StatBase field_151512_d;
    public final StatBase field_151513_e;

    public MMEntityEggInfo(int spawnedID, int primaryColor, int secondaryColor, String name) {
        this.spawnedID = spawnedID;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.field_151512_d = func_151182_a(name);
        this.field_151513_e = func_151176_b(name);
    }

    public static StatBase func_151182_a(String name) {
        return name == null ? null : (new StatBase("stat.killEntity." + name, new ChatComponentTranslation("stat.entityKill", new ChatComponentTranslation("entity." + name + ".name", new Object[0])))).registerStat();
    }

    public static StatBase func_151176_b(String name) {
        return name == null ? null : (new StatBase("stat.entityKilledBy." + name, new ChatComponentTranslation("stat.entityKilledBy", new ChatComponentTranslation("entity." + name + ".name", new Object[0])))).registerStat();
    }
}
