package com.bobmowzie.mowziesmobs.server.advancement;

import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementHandler {
    public static final StealIceCrystalTrigger STEAL_ICE_CRYSTAL_TRIGGER = CriteriaTriggers.register(new StealIceCrystalTrigger());

    public static void preInit() { }
}