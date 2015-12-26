package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatComponentTranslation;

/**
 * Created by jnad325 on 12/16/15.
 */
public class MMEntityEggInfo {
    /** The entityID of the spawned mob */
    public final int spawnedID;
    /** Base color of the egg */
    public final int primaryColor;
    /** Color of the egg spots */
    public final int secondaryColor;
    public final StatBase field_151512_d;
    public final StatBase field_151513_e;
    private static final String __OBFID = "CL_00001539";

    public MMEntityEggInfo(int p_i1583_1_, int p_i1583_2_, int p_i1583_3_, String name)
    {
        this.spawnedID = p_i1583_1_;
        this.primaryColor = p_i1583_2_;
        this.secondaryColor = p_i1583_3_;
        this.field_151512_d = func_151182_a(this, name);
        this.field_151513_e = func_151176_b(this, name);
    }

    public static StatBase func_151182_a(MMEntityEggInfo p_151182_0_, String name)
    {
        String s = name;
        return s == null ? null : (new StatBase("stat.killEntity." + s, new ChatComponentTranslation("stat.entityKill", new Object[] {new ChatComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
    }

    public static StatBase func_151176_b(MMEntityEggInfo p_151176_0_, String name)
    {
        String s = name;
        return s == null ? null : (new StatBase("stat.entityKilledBy." + s, new ChatComponentTranslation("stat.entityKilledBy", new Object[] {new ChatComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
    }
}
