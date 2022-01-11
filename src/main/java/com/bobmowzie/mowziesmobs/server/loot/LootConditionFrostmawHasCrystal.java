package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;

import java.util.Set;

public class LootConditionFrostmawHasCrystal implements ILootCondition {

    private static final LootConditionFrostmawHasCrystal INSTANCE = new LootConditionFrostmawHasCrystal();

    private LootConditionFrostmawHasCrystal() {
    }

    public LootConditionType getConditionType() {
        return LootConditionManager.KILLED_BY_PLAYER;
    }

    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext context) {
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        if (entity instanceof EntityFrostmaw) {
            EntityFrostmaw frostmaw = (EntityFrostmaw) entity;
            return frostmaw.getHasCrystal();
        }
        return false;
    }

    public static IBuilder builder() {
        return () -> {
            return INSTANCE;
        };
    }

    public static class Serializer implements ILootSerializer<LootConditionFrostmawHasCrystal> {
        public Serializer() {
        }

        public void serialize(JsonObject p_230424_1_, LootConditionFrostmawHasCrystal p_230424_2_, JsonSerializationContext p_230424_3_) {
        }

        public LootConditionFrostmawHasCrystal deserialize(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
            return LootConditionFrostmawHasCrystal.INSTANCE;
        }
    }
}
