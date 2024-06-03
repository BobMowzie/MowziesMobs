package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.Set;

public class LootConditionFrostmawHasCrystal implements LootItemCondition {

    private static final LootConditionFrostmawHasCrystal INSTANCE = new LootConditionFrostmawHasCrystal();

    private LootConditionFrostmawHasCrystal() {
    }

    @Override
    public LootItemConditionType getType() {
        return LootTableHandler.FROSTMAW_HAS_CRYSTAL.get();
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

    public static class ConditionSerializer implements Serializer<LootConditionFrostmawHasCrystal> {
        @Override
        public void serialize(JsonObject json, LootConditionFrostmawHasCrystal value, JsonSerializationContext context) {
        }

        @Override
        public LootConditionFrostmawHasCrystal deserialize(JsonObject json, JsonDeserializationContext context) {
            return LootConditionFrostmawHasCrystal.INSTANCE;
        }
    }
}
