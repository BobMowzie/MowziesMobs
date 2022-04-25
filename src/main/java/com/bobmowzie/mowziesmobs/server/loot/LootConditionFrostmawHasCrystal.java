package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.loot.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.Set;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootConditionFrostmawHasCrystal implements LootItemCondition {

    private static final LootConditionFrostmawHasCrystal INSTANCE = new LootConditionFrostmawHasCrystal();

    private LootConditionFrostmawHasCrystal() {
    }

    public LootItemConditionType getType() {
        return LootItemConditions.KILLED_BY_PLAYER;
    }

    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof EntityFrostmaw) {
            EntityFrostmaw frostmaw = (EntityFrostmaw) entity;
            return frostmaw.getHasCrystal();
        }
        return false;
    }

    public static Builder builder() {
        return () -> {
            return INSTANCE;
        };
    }

    public static class Serializer implements Serializer<LootConditionFrostmawHasCrystal> {
        public Serializer() {
        }

        public void serialize(JsonObject p_230424_1_, LootConditionFrostmawHasCrystal p_230424_2_, JsonSerializationContext p_230424_3_) {
        }

        public LootConditionFrostmawHasCrystal deserialize(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
            return LootConditionFrostmawHasCrystal.INSTANCE;
        }
    }
}
